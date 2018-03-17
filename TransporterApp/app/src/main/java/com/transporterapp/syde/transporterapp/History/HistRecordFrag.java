package com.transporterapp.syde.transporterapp.History;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.transporterapp.syde.transporterapp.DataStructures.FarmerItem;
import com.transporterapp.syde.transporterapp.DataStructures.Jug;
import com.transporterapp.syde.transporterapp.DataStructures.TransporterItem;
import com.transporterapp.syde.transporterapp.R;
import com.transporterapp.syde.transporterapp.commonUtil;
import com.transporterapp.syde.transporterapp.databases.DatabaseConstants;
import com.transporterapp.syde.transporterapp.databases.dbUtil;

import org.w3c.dom.Text;

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
        transactionID.setText(getArguments().getString("milkRecordId"));


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


        return view;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

}
