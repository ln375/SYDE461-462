package com.transporterapp.syde.transporterapp.CollectMilk;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.transporterapp.syde.transporterapp.Main;
import com.transporterapp.syde.transporterapp.R;
import com.transporterapp.syde.transporterapp.databases.DatabaseConstants;
import com.transporterapp.syde.transporterapp.databases.dbUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class MilkEntryFrag extends Fragment {

    private static List<String> milkEntryColumns = Arrays.asList("MILK_WEIGHT", "SMELL", "DENSITY");

    private EditText milkVolume;
    private Button SaveData;
    private RadioGroup smellTest;
    private RadioGroup densityTest;
    private RadioGroup alcoholTest;
    private Spinner jugDropdown;
    private EditText txtComments;
    private String mFarmerName;
    private static final String FARMER_NAME = "farmername";


    public MilkEntryFrag() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            mFarmerName = getArguments().getString(FARMER_NAME);

            // Set title bar
            ((Main) getActivity()).setActionBarTitle(mFarmerName);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_farmer_milk_data_entry, container, false);
        Context context = view.getContext();

        //Data fields
        TextView farmerName = (TextView) view.findViewById(R.id.milk_entry_farmer_name);
        jugDropdown = (Spinner) view.findViewById(R.id.jug_spinner);
        smellTest = (RadioGroup) view.findViewById(R.id.smell_test);
        densityTest = (RadioGroup) view.findViewById(R.id.density_test);
        alcoholTest = (RadioGroup) view.findViewById(R.id.alcohol_test);
        milkVolume = (EditText) view.findViewById(R.id.milk_volume);
        SaveData = (Button)view.findViewById(R.id.save_data);
        txtComments = (EditText) view.findViewById(R.id.comments);

        List<String> jug_list = dbUtil.selectStatement("jug","id", "transporter_id", "=", getArguments().getString("transporterId"), context);
        ArrayAdapter<String> jugListAdapter = new ArrayAdapter<>(context, R.layout.jug_row_layout, R.id.jug_id, jug_list);

        farmerName.setText(mFarmerName);
        jugDropdown.setAdapter(jugListAdapter);

        AddData();

        return view;
    }

    public  void AddData() {
        SaveData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String farmerId = getArguments().getString("farmerid");
                        String transporterId = getArguments().getString("transporterId");
                        String smellIndex = String.valueOf(smellTest.indexOfChild(getView().findViewById(smellTest.getCheckedRadioButtonId())));
                        String densityIndex = String.valueOf(densityTest.indexOfChild(getView().findViewById(densityTest.getCheckedRadioButtonId())));
                        String alcoholIndex = String.valueOf(alcoholTest.indexOfChild(getView().findViewById(alcoholTest.getCheckedRadioButtonId())));
                        String jugId = jugDropdown.getSelectedItem().toString();
                        String comments = txtComments.getText().toString();
                        String milkweight = milkVolume.getText().toString();

                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = new Date();
                        String todayDate= dateFormat.format(date);

                        dateFormat = new SimpleDateFormat("HH:mm:ss");
                        String todayTime = dateFormat.format(date);

                        // Make Milk volume required field
                        if(TextUtils.isEmpty(milkweight)){
                            Toast.makeText(getContext(),"Milk Volume required", Toast.LENGTH_LONG).show();
                        } else {
                            List<String> columns = new ArrayList<>();
                            columns.addAll(Arrays.asList(DatabaseConstants.coltrFarmerTransporter));
                            columns.remove(DatabaseConstants.coltrFarmerTransporter.length - 1);
                            columns.remove(0);

                            List<String> values = Arrays.asList(transporterId, farmerId, jugId, todayDate, todayTime, milkweight, alcoholIndex, smellIndex, comments, densityIndex);

                            dbUtil.insertStatement(DatabaseConstants.tbltrFarmerTransporter, columns, values, v.getContext());
                        }

                        Toast.makeText(getContext(),"Data Inserted", Toast.LENGTH_LONG).show();

                    }
                }
        );

    }

    public boolean areDataFieldsEmpty(){
        if ((smellTest.getCheckedRadioButtonId() == R.id.rb_unchecked) && (densityTest.getCheckedRadioButtonId() == R.id.density_rb_unchecked) && (alcoholTest.getCheckedRadioButtonId() == R.id.alcohol_rb_unchecked)
                && (txtComments.getText().toString().isEmpty()) && (milkVolume.getText().toString().isEmpty())) {
            return true;
        }
        return false;
    }

    public void clearData() {
         smellTest.check(R.id.rb_unchecked);
         densityTest.check(R.id.density_rb_unchecked);
         alcoholTest.check(R.id.alcohol_rb_unchecked);
         milkVolume.setText("");
    }


}
