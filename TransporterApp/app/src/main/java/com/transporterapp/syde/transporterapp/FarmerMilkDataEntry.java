package com.transporterapp.syde.transporterapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class FarmerMilkDataEntry extends AppCompatActivity {

    DatabaseSaveHelper myDB;
    EditText milkVolume;
    Button SaveData;
    RadioGroup smellTest;
    RadioGroup densityTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_milk_data_entry);
        myDB = new DatabaseSaveHelper(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get intent data, the farmer name, passed by MikEntryActivity
        Intent i = getIntent();
        String farmerName = i.getStringExtra("farmerName");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(farmerName);

        Spinner jugDropdown = (Spinner) findViewById(R.id.jug_spinner);
        DataBaseAccess databaseAccess = DataBaseAccess.getInstance(this);
        databaseAccess.open();
        List<String> jug_list = databaseAccess.getJugs(3);
        Log.d("Jug", jug_list.get(0));

        ArrayAdapter<String> jugListAdapter = new ArrayAdapter<>(
                this, R.layout.jug_row_layout, R.id.jug_id, jug_list);

        jugDropdown.setAdapter(jugListAdapter);

        //Edit data fields
        smellTest = (RadioGroup) findViewById(R.id.smell_test);
        densityTest = (RadioGroup) findViewById(R.id.density_test);
        milkVolume = (EditText) findViewById(R.id.milk_volume);
        SaveData = (Button)findViewById(R.id.save_data);

        AddData();
    }

    public  void AddData() {
        SaveData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int smellIndex = smellTest.indexOfChild(findViewById(smellTest.getCheckedRadioButtonId()));
                        int densityIndex = densityTest.indexOfChild(findViewById(densityTest.getCheckedRadioButtonId()));

                        boolean isInserted = myDB.insertData(milkVolume.getText().toString(), smellIndex, densityIndex);
                        if(isInserted == true) {
                            Toast.makeText(FarmerMilkDataEntry.this,"Data Inserted", Toast.LENGTH_LONG).show();
                            Cursor res = myDB.getAllData();
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
                            Toast.makeText(FarmerMilkDataEntry.this,"Data not Inserted",Toast.LENGTH_LONG).show();
                        }

                    }
                }
        );

    }

    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
}
