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
                        boolean isInserted = myDB.insertData(milkVolume.getText().toString(), 1, 1);
                        if(isInserted == true)
                            Toast.makeText(FarmerMilkDataEntry.this,"Data Inserted", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(FarmerMilkDataEntry.this,"Data not Inserted",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }


}
