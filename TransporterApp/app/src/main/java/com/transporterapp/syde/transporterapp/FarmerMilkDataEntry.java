package com.transporterapp.syde.transporterapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.List;

public class FarmerMilkDataEntry extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_milk_data_entry);
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
        List<String> jug_list = databaseAccess.getJugs(5);

        ArrayAdapter<String> jugListAdapter = new ArrayAdapter<String>(
                this, R.layout.jug_row_layout, R.id.jug_id, jug_list);

        jugDropdown.setAdapter(jugListAdapter);



    }

}
