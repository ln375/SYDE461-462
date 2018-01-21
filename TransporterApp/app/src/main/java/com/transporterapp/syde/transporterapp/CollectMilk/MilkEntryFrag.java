package com.transporterapp.syde.transporterapp.CollectMilk;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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

import com.transporterapp.syde.transporterapp.R;
import com.transporterapp.syde.transporterapp.databases.DataBaseUtil;
import com.transporterapp.syde.transporterapp.databases.DatabaseConstants;

import java.util.Arrays;
import java.util.List;

public class MilkEntryFrag extends Fragment {

    private static List<String> milkEntryColumns = Arrays.asList("MILK_WEIGHT", "SMELL", "DENSITY");

    private EditText milkVolume;
    private Button SaveData;
    private RadioGroup smellTest;
    private RadioGroup densityTest;

    public MilkEntryFrag() {
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_farmer_milk_data_entry, container, false);

        Context context = view.getContext();

        TextView farmerName = (TextView) view.findViewById(R.id.milk_entry_farmer_name);
        farmerName.setText(getArguments().getString("farmername"));

        String id = getArguments().getString("farmerid");

        List<String> jug_list = DataBaseUtil.selectStatement("jug","id", "transporter_id", "=", "3", context);

        Spinner jugDropdown = (Spinner) view.findViewById(R.id.jug_spinner);

        ArrayAdapter<String> jugListAdapter = new ArrayAdapter<>(
                context, R.layout.jug_row_layout, R.id.jug_id, jug_list);

        jugDropdown.setAdapter(jugListAdapter);

        //Edit data fields
        smellTest = (RadioGroup) view.findViewById(R.id.smell_test);
        densityTest = (RadioGroup) view.findViewById(R.id.density_test);
        milkVolume = (EditText) view.findViewById(R.id.milk_volume);
        SaveData = (Button)view.findViewById(R.id.save_data);

        AddData();

        return view;
    }

    public  void AddData() {
        SaveData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int smellIndex = smellTest.indexOfChild(getView().findViewById(smellTest.getCheckedRadioButtonId()));
                        int densityIndex = densityTest.indexOfChild(getView().findViewById(densityTest.getCheckedRadioButtonId()));
                        boolean isInserted = insertData(milkVolume.getText().toString(), smellIndex, densityIndex);
                        if(isInserted == true) {
                            Toast.makeText(getContext(),"Data Inserted", Toast.LENGTH_LONG).show();
                            Cursor res = DataBaseUtil.selectStatement(DatabaseConstants.tbltrFarmerTransporter, "","",  "", getContext());
                            if(res.getCount() == 0) {
                                // show message
                                showMessage("Error","Nothing found");
                                return;
                            }

                            StringBuffer buffer = new StringBuffer();
                            while (res.moveToNext()) {
                                buffer.append("Id :"+ res.getString(0)+"\n");
                                buffer.append("Milk Weight :"+ res.getString(6)+"\n");
                                buffer.append("Density :"+ getQualityRating(res.getString(10))+"\n");
                                buffer.append("Smell :"+ getQualityRating(res.getString(8))+"\n\n");
                            }

                            // Show all data
                            showMessage("Data",buffer.toString());

                        } else {
                            Toast.makeText(getContext(),"Data not Inserted",Toast.LENGTH_LONG).show();
                        }

                    }
                }
        );

    }

    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    public String getQualityRating(String index) {
        String temp = "";
        switch (index) {
            case "0":
                temp = "Good";
                break;
            case "1":
                temp = "Okay";
                break;
            case "2":
                temp = "Bad";
                break;
            case "3":
                temp = "N/A";
                break;
            default:
                temp = "N/A";

        }
        return temp;
    }

    public boolean insertData(String milkData, int smellIndex, int densityIndex) {
        List<String> values = Arrays.asList(milkData, String.valueOf(smellIndex), String.valueOf(densityIndex));
        return DataBaseUtil.insertStatement(DatabaseConstants.tbltrFarmerTransporter, milkEntryColumns, values, getContext());
    }

    public boolean areDataFieldsEmpty(){
        if ((smellTest.getCheckedRadioButtonId() != R.id.rb_unchecked) || (densityTest.getCheckedRadioButtonId() != R.id.rb_unchecked) || (!milkVolume.getText().toString().isEmpty())) {
            return false;
        }
        return true;
    }

    public void clearData() {
         smellTest.check(R.id.rb_unchecked);
         densityTest.check(R.id.density_rb_unchecked);
         milkVolume.setText("");
    }


}
