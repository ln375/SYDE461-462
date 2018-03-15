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
        farmerName.setText("Farmer: " + farmer.getFirstName() + " " + farmer.getLastName());

        TextView transporterName = (TextView) view.findViewById(R.id.hist_record_transporter_name);
        Cursor curTransporter = dbUtil.selectStatement(DatabaseConstants.tblTransporter, DatabaseConstants.id, "=", getArguments().getString("transporterId"), context);
        TransporterItem transporter = commonUtil.convertCursortToTransporterItem(curTransporter);
        transporterName.setText("Transporter: " +transporter.getFirstName() + " " + transporter.getLastName());

        TextView date = (TextView) view.findViewById(R.id.hist_record_date);
        date.setText("Date: " + getArguments().getString("date"));

        TextView time = (TextView) view.findViewById(R.id.hist_record_time);
        time.setText("Time: " + getArguments().getString("time"));

        Cursor curJug = dbUtil.selectStatement(DatabaseConstants.tblJug, DatabaseConstants.id, "=", getArguments().getString("jugId"), context);
        Jug jug = commonUtil.convertCursorToJug(curJug);
        TextView txtJug = (TextView) view.findViewById(R.id.hist_record_jug);
        if (jug != null) {
            txtJug.setText("Jug: " + jug.getSize() + " L " + jug.getType());
        } else {
            txtJug.setText("Jug: N/A");
        }


        TextView milkWeight = (TextView) view.findViewById(R.id.hist_record_milk_volume);
        String milk = getArguments().getString("milkweight");
        if (milk.isEmpty()) {
            milkWeight.setText("Milk Weight: N/A");
        } else {
            milkWeight.setText("Milk Weight: " + getArguments().getString("milkweight"));
        }


        TextView alcohol = (TextView) view.findViewById(R.id.hist_record_alcohol);
        String temp = "N/A";
        if (getArguments().getString("alcohol").isEmpty()){
            alcohol.setText("Alcohol Test: " + temp);
        } else {
            temp = commonUtil.getQualityRating(getArguments().getString("alcohol"), context);
            alcohol.setText("Alcohol Test: " + temp);
        }

        TextView density = (TextView) view.findViewById(R.id.hist_record_density);
        if (getArguments().getString("density").isEmpty()){
            temp = "N/A";
            density.setText("Density Test: " + temp);
        } else {
            temp = commonUtil.getQualityRating(getArguments().getString("density"), context);
            density.setText("Density Test: " + temp);
        }

        TextView smell = (TextView) view.findViewById(R.id.hist_record_smell);
        if (getArguments().getString("smell").isEmpty()){
            temp = "N/A";
            smell.setText("Smell Test: " + temp);
        } else {
            temp = commonUtil.getQualityRating(getArguments().getString("smell"), context);
            smell.setText("Smell Test: " + temp);
        }

        TextView comments = (TextView) view.findViewById(R.id.hist_record_comments);
        if (getArguments().getString("comments").isEmpty()){
            comments.setText("Comments: N/A");
        } else {
            comments.setText("Comments: " + getArguments().getString("comments"));
        }


        return view;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

}
