package com.transporterapp.syde.transporterapp;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.transporterapp.syde.transporterapp.CollectMilk.FarmerListFrag;
import com.transporterapp.syde.transporterapp.CollectMilk.FarmerListFrag.OnListFragmentInteractionListener;
import com.transporterapp.syde.transporterapp.CollectMilk.MilkEntryFrag;
import com.transporterapp.syde.transporterapp.DataStructures.FarmerItem;
import com.transporterapp.syde.transporterapp.DataStructures.MilkRecord;
import com.transporterapp.syde.transporterapp.History.HistListFrag;
import com.transporterapp.syde.transporterapp.History.HistRecordFrag;
import com.transporterapp.syde.transporterapp.databases.DatabaseConstants;
import com.transporterapp.syde.transporterapp.databases.dbUtil;

//import com.testfairy.TestFairy;


public class Main extends AppCompatActivity
        implements OnListFragmentInteractionListener, HistListFrag.OnListFragmentInteractionListener {

    private HistRecordFrag histRecordFrag = new HistRecordFrag();
    private HistListFrag histListFrag = new HistListFrag();
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FarmerListFrag farmerListFragment = new FarmerListFrag();
    private MilkEntryFrag milkEntryFragment = new MilkEntryFrag();
    private DrawerLayout drawer;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_container);

        if(findViewById(R.id.container) != null){
            if (savedInstanceState == null){
                fragmentManager.beginTransaction().add(R.id.container,farmerListFragment, commonUtil.FARMER_LIST_TAG_FRAGMENT).commit();
            }
        }

        //TestFairy.begin(this, "a61f203ba668965e0295409c7fec4b15d7a31770");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        //Handling Search intent
        handleIntent(getIntent());
        navigationView.setNavigationItemSelectedListener(navSelectListener);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Cursor testing = dbUtil.selectStatement(DatabaseConstants.tblFarmer, DatabaseConstants.first_name, "=", query, this);

        }
    }


    private NavigationView.OnNavigationItemSelectedListener navSelectListener = new NavigationView.OnNavigationItemSelectedListener(){
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            item.setChecked(true);
            if(item.getTitle().equals("History Screen")) {
                fragmentManager.beginTransaction().replace(R.id.container, histListFrag, commonUtil.HIST_LIST_TAG_FRAGMENT).commit();
                drawer.closeDrawer(GravityCompat.START);
            } else if(item.getTitle().equals("Collect Milk")) {
                fragmentManager.beginTransaction().replace(R.id.container, farmerListFragment, commonUtil.FARMER_LIST_TAG_FRAGMENT).commit();
                drawer.closeDrawer(GravityCompat.START);
            }
            return true;
        }
    };



    @Override
    public void onBackPressed() {
        if(milkEntryFragment.isVisible()){
            showUnsavedDataMessage(milkEntryFragment, this);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.milk_entry, menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public void onListFragmentInteraction(FarmerItem item) {
        Bundle bundle = new Bundle();
        String fullFarmerName = item.getFirstName() + " " + item.getLastName();
        bundle.putString("farmername", fullFarmerName);
        bundle.putString("farmerid", item.getId());
        bundle.putString("transporterId", "3");
        milkEntryFragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.container, milkEntryFragment, commonUtil.MILK_ENTRY_TAG_FRAGMENT).commit();
    }

    @Override
    public void onListFragmentInteraction(MilkRecord item) {
        Bundle bundle = new Bundle();
        bundle.putString("milkRecordId", item.getId());
        bundle.putString("transporterId", item.getTransporterId());
        bundle.putString("farmerId", item.getFarmerId());
        bundle.putString("jugId", item.getJugId());
        bundle.putString("date", item.getDate());
        bundle.putString("time", item.getTime());
        bundle.putString("milkweight", item.getMilkWeight());
        bundle.putString("alcohol", item.getAlcohol());
        bundle.putString("smell", item.getSmell());
        bundle.putString("density", item.getDensity());
        bundle.putString("comments", item.getComments());
        bundle.putString("trTransporterCoolingId", item.getTrTransporterCoolingId());
        histRecordFrag.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.container, histRecordFrag, commonUtil.HIST_REC_TAG_FRAGMENT).commit();
    }

    /**
     * Displays a dialog to ensure user wants to backspace if there is still data on the milk entry page
     * Example Usage: showUnsavedDataMessage(milkEntryFrag, this);
     *
     * @param fragment
     * @param context
     */
    public void showUnsavedDataMessage(Fragment fragment, Context context){
        if (fragment.getTag().equals(commonUtil.MILK_ENTRY_TAG_FRAGMENT)) {
            final MilkEntryFrag milkEntryFrag = (MilkEntryFrag) fragment;
            if (!milkEntryFrag.areDataFieldsEmpty()) {
                new AlertDialog.Builder(context)
                        .setTitle("Unsaved data")
                        .setMessage("Are you sure you want to go back?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, unsavedDataDialog)
                        .setNegativeButton(android.R.string.no, unsavedDataDialog).show();
            }
        }
    }

    /**
     * DialogInterface listener used for showUnsavedDataMessage
     */
    private DialogInterface.OnClickListener unsavedDataDialog = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    fragmentManager.beginTransaction().detach(milkEntryFragment);
                    milkEntryFragment.clearData();
                    fragmentManager.beginTransaction().replace(R.id.container, farmerListFragment, commonUtil.FARMER_LIST_TAG_FRAGMENT).commit();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    };
}
