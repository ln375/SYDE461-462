package com.transporterapp.syde.transporterapp.History;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.transporterapp.syde.transporterapp.DataStructures.FarmerItem;
import com.transporterapp.syde.transporterapp.DataStructures.HistMilkRecord;
import com.transporterapp.syde.transporterapp.DataStructures.Jug;
import com.transporterapp.syde.transporterapp.DataStructures.MilkRecord;
import com.transporterapp.syde.transporterapp.DataStructures.TransporterItem;
import com.transporterapp.syde.transporterapp.R;
import com.transporterapp.syde.transporterapp.commonUtil;
import com.transporterapp.syde.transporterapp.databases.DatabaseConstants;
import com.transporterapp.syde.transporterapp.databases.dbUtil;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HistRecordFrag extends Fragment {

    private TextView milkVolume;
    private TextView selectedJug;
    private RadioGroup smellTest;
    private RadioGroup densityTest;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HistRecordFrag() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hist_record, container, false);

        Context context = view.getContext();
        Cursor curFarmer = dbUtil.selectStatement(DatabaseConstants.tblFarmer, DatabaseConstants.id, "=", getArguments().getString("farmerId"), context);
        FarmerItem farmer = commonUtil.convertCursortToFarmerItem(curFarmer);

        TextView farmerName = (TextView) view.findViewById(R.id.hist_record_farmer_name);
        farmerName.setText(farmer.getFirstName() + " " + farmer.getLastName());

        TextView farmerId = (TextView) view.findViewById(R.id.hist_record_farmer_id);
        farmerId.setText(farmer.getId());

        TextView date = (TextView) view.findViewById(R.id.hist_record_date);
        date.setText(getArguments().getString("date"));

        TextView time = (TextView) view.findViewById(R.id.hist_record_time);
        time.setText(getArguments().getString("time"));

        Cursor curJug = dbUtil.selectStatement(DatabaseConstants.tblJug, DatabaseConstants.id, "=", getArguments().getString("jugId"), context);
        Jug jug = commonUtil.convertCursorToJug(curJug);
        TextView txtJug = (TextView) view.findViewById(R.id.hist_record_jug);
        if (jug != null) {
            txtJug.setText(jug.getId());
        } else {
            txtJug.setText("N/A");
        }

        TextView farmerPhone = (TextView) view.findViewById(R.id.hist_record_farmer_phone);
        farmerPhone.setText(getArguments().getString("phoneNumber"));


        TextView milkWeight = (TextView) view.findViewById(R.id.hist_record_milk_volume);
        String milk = getArguments().getString("milkweight");
        if (milk.isEmpty()) {
            milkWeight.setText("N/A");
        } else {
            milkWeight.setText(getArguments().getString("milkweight") + " L");
        }

        TextView transactionID = (TextView) view.findViewById(R.id.hist_record_transaction_id);
        String milkRecordId = getArguments().getString("milkRecordId");
        transactionID.setText(milkRecordId);


        TextView alcohol = (TextView) view.findViewById(R.id.hist_record_alcohol);
        String temp = "N/A";
        if (getArguments().getString("alcohol").isEmpty()){
            alcohol.setText(temp);
        } else {
            temp = commonUtil.getQualityRating(getArguments().getString("alcohol"), context);
            alcohol.setText(temp);
        }

        TextView density = (TextView) view.findViewById(R.id.hist_record_density);
        if (getArguments().getString("density").isEmpty()){
            temp = "N/A";
            density.setText(temp);
        } else {
            density.setText(getArguments().getString("density"));
        }

        TextView smell = (TextView) view.findViewById(R.id.hist_record_smell);
        if (getArguments().getString("smell").isEmpty()){
            temp = "N/A";
            smell.setText(temp);
        } else {
            temp = commonUtil.getQualityRating(getArguments().getString("smell"), context);
            smell.setText(temp);
        }

        TextView comments = (TextView) view.findViewById(R.id.hist_record_comments);
        if (getArguments().getString("comments").isEmpty()){
            comments.setText("N/A");
        } else {
            comments.setText(getArguments().getString("comments"));
        }

        LinearLayout mainLayout = (LinearLayout) view.findViewById(R.id.hist_record_layout);
        LinearLayout editHistoryLayout = new LinearLayout(getContext());
        editHistoryLayout.setOrientation(LinearLayout.VERTICAL);
        ScrollView scrollView = new ScrollView(getContext());

        // Get relevant info for edit history
        List<HistMilkRecord> histMilkRecords = commonUtil.convertCursorToHistMilkRecordList(dbUtil.selectStatement(DatabaseConstants.tblHisttrFarmerTransporter, DatabaseConstants.tr_farmer_transporter_id, "=", milkRecordId, context));
        Collections.sort(histMilkRecords, new Comparator<HistMilkRecord>() {
            @Override
            public int compare(HistMilkRecord o1, HistMilkRecord o2) {
                if (Integer.valueOf(o1.getId()) > Integer.valueOf(o2.getId())) {
                    return 1;
                } else if (Integer.valueOf(o1.getId()) < Integer.valueOf(o2.getId())){
                    return -1;
                }
                return 0;
            }
        });
        if (histMilkRecords != null) {
            try {
            if (histMilkRecords.size() == 1) {
                HistMilkRecord firstRecord = histMilkRecords.get(0);
                DateFormat df = new SimpleDateFormat("HH:mm");
                String edit = firstRecord.getDate() + " " + df.format(new SimpleDateFormat("HH:mm:ss").parse(firstRecord.getTime())) + " - Record created";
                TextView editHistory = new TextView(context);
                editHistory.setText(edit);
                editHistoryLayout.addView(editHistory);
            } else {

                for (int i = 0; i < histMilkRecords.size() - 1; i++) {
                    List<String> changes = new ArrayList<>();

                    HistMilkRecord firstRecord = histMilkRecords.get(i);
                    HistMilkRecord secondRecord = histMilkRecords.get(i+1);

                    if (i == 0) {
                        DateFormat df = new SimpleDateFormat("HH:mm");
                        changes.add(firstRecord.getDate() + " " + df.format(new SimpleDateFormat("HH:mm:ss").parse(firstRecord.getTime())) + " - Record created");
                    }

                    //compare firstRecord with secondRecord
                    changes.addAll(commonUtil.compareHistMilkRecords(firstRecord, secondRecord));

                    for (String change : changes) {
                        TextView editHistory = new TextView(context);
                        editHistory.setText(change);
                        editHistoryLayout.addView(editHistory);
                    }

                }
            }

        } catch (ParseException e){

        }
            scrollView.addView(editHistoryLayout);
            mainLayout.addView(scrollView);
        }

        return view;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

}
