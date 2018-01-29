package com.transporterapp.syde.transporterapp.CollectMilk;

import android.content.Context;
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

    public MilkEntryFrag() {
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_farmer_milk_data_entry, container, false);

        Context context = view.getContext();

        TextView farmerName = (TextView) view.findViewById(R.id.milk_entry_farmer_name);
        farmerName.setText(getArguments().getString("farmername"));

        List<String> jug_list = dbUtil.selectStatement("jug","id", "transporter_id", "=", getArguments().getString("transporterId"), context);

        jugDropdown = (Spinner) view.findViewById(R.id.jug_spinner);

        ArrayAdapter<String> jugListAdapter = new ArrayAdapter<>(
                context, R.layout.jug_row_layout, R.id.jug_id, jug_list);

        jugDropdown.setAdapter(jugListAdapter);

        //Edit data fields
        smellTest = (RadioGroup) view.findViewById(R.id.smell_test);
        densityTest = (RadioGroup) view.findViewById(R.id.density_test);
        alcoholTest = (RadioGroup) view.findViewById(R.id.alcohol_test);
        milkVolume = (EditText) view.findViewById(R.id.milk_volume);
        SaveData = (Button)view.findViewById(R.id.save_data);
        txtComments = (EditText) view.findViewById(R.id.comments);

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

                        List<String> columns = new ArrayList<>();
                        columns.addAll(Arrays.asList(DatabaseConstants.coltrFarmerTransporter));
                        columns.remove(DatabaseConstants.coltrFarmerTransporter.length - 1);
                        columns.remove(0);

                        List<String> values = Arrays.asList(transporterId, farmerId, jugId, todayDate, todayTime, milkweight, alcoholIndex, smellIndex, comments, densityIndex);


                        dbUtil.insertStatement(DatabaseConstants.tbltrFarmerTransporter, columns, values, v.getContext());


                        /*
                            Toast.makeText(getContext(),"Data Inserted", Toast.LENGTH_LONG).show();
                            Cursor res = dbUtil.selectStatement(DatabaseConstants.tbltrFarmerTransporter, "","",  "", getContext());
                            if(res.getCount() == 0) {
                                // show message
                                showMessage("Error","Nothing found");
                                return;
                            }

                            StringBuffer buffer = new StringBuffer();
                            while (res.moveToNext()) {
                                buffer.append("Id :"+ res.getString(0)+"\n");
                                buffer.append("Milk Weight :"+ res.getString(6)+"\n");
                                buffer.append("Density :"+ commonUtil.getQualityRating(res.getString(10))+"\n");
                                buffer.append("Smell :"+ commonUtil.getQualityRating(res.getString(8))+"\n\n");
                            }

                            // Show all data
                            showMessage("Data",buffer.toString());
                        */

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

    public boolean insertData(String milkData, int smellIndex, int densityIndex) {
        List<String> values = Arrays.asList(milkData, String.valueOf(smellIndex), String.valueOf(densityIndex));
        return dbUtil.insertStatement(DatabaseConstants.tbltrFarmerTransporter, milkEntryColumns, values, getContext());
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
         alcoholTest.check(R.id.alcohol_rb_unchecked);
         milkVolume.setText("");
    }


}
