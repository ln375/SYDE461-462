package com.transporterapp.syde.transporterapp;

import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.transporterapp.syde.transporterapp.databases.DataBaseUtil;

import java.util.List;
import com.testfairy.TestFairy;


public class CollectMilkActivity extends AppCompatActivity
        implements FarmerListFrag.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);

        if(findViewById(R.id.container) != null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (savedInstanceState == null){
                FarmerListFrag farmerListFragment = new FarmerListFrag();
                fragmentManager.beginTransaction().add(R.id.container,farmerListFragment).commit();
            }
        }

        TestFairy.begin(this, "a61f203ba668965e0295409c7fec4b15d7a31770");

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/



        //Get listview object from XML
        /*ListView farmerListView = (ListView) findViewById(R.id.farmer_list_view);
        List<String> farmer_list = DataBaseUtil.selectStatement("farmers","first_name", "", "", "", this);

        //Create new adapter for listview
            // First parameter - Context
            // Second parameter - Layout for the row
            // Third parameter - ID of the TextView to which the data is written
            // Fourth - the Array of data
        ArrayAdapter<String> farmerListAdapter = new ArrayAdapter<String>
                (this, R.layout.farmer_row_layout, R.id.farmer_name, farmer_list);

        //Assign adapter to list view
        farmerListView.setAdapter(farmerListAdapter);

        farmerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
                String farmerName = (String) listView.getAdapter().getItem(position);
                Intent intent = new Intent(listView.getContext(), FarmerMilkDataEntry.class);
                //Pass in farmer name to FarmerMilkDataEntry
                //Maybe want to pass in entire farmer object or something?
                intent.putExtra("farmerName", farmerName);
                listView.getContext().startActivity(intent);
            }
        });*/
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.milk_entry, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
