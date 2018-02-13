package com.transporterapp.syde.transporterapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.transporterapp.syde.transporterapp.CollectMilk.FarmerListFrag;
import com.transporterapp.syde.transporterapp.CollectMilk.FarmerListFrag.OnListFragmentInteractionListener;
import com.transporterapp.syde.transporterapp.CollectMilk.MilkEntryFrag;
import com.transporterapp.syde.transporterapp.DataStructures.FarmerItem;
import com.transporterapp.syde.transporterapp.DataStructures.MilkRecord;
import com.transporterapp.syde.transporterapp.ExportData.ExportDataFrag;
import com.transporterapp.syde.transporterapp.History.HistListFrag;
import com.transporterapp.syde.transporterapp.History.HistRecordFrag;
import com.transporterapp.syde.transporterapp.LoginScreen.LoginFragment;

import static com.transporterapp.syde.transporterapp.LoginScreen.LoginFragment.PREFS_NAME;

//import com.testfairy.TestFairy;


public class Main extends AppCompatActivity
        implements OnListFragmentInteractionListener, HistListFrag.OnListFragmentInteractionListener, LoginFragment.OnFragmentInteractionListener, ExportDataFrag.OnFragmentInteractionListener {
    public static Main instance = null;
    private HistRecordFrag histRecordFrag = new HistRecordFrag();
    private HistListFrag histListFrag = new HistListFrag();
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FarmerListFrag farmerListFragment = new FarmerListFrag();
    private MilkEntryFrag milkEntryFragment = new MilkEntryFrag();
    private LoginFragment loginFrag = new LoginFragment();
    private ExportDataFrag exportDataFrag = new ExportDataFrag();

    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    private String userId = "";

    @Override
    public void onResume()
    {
        super.onResume();
        instance = this;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        instance = null;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_container);

        //TestFairy.begin(this, "a61f203ba668965e0295409c7fec4b15d7a31770");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(navSelectListener);

        if(findViewById(R.id.container) != null){
            if (savedInstanceState == null){
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);
                userId = settings.getString("transporterId", "");
                if (hasLoggedIn) {
                    Bundle bundle = new Bundle();
                    bundle.putString("userid", userId);
                    farmerListFragment.setArguments(bundle);
                    fragmentManager.beginTransaction()
                            .add(R.id.container,farmerListFragment, commonUtil.FARMER_LIST_TAG_FRAGMENT)
                            .commit();


                } else {
                    toggle.setDrawerIndicatorEnabled(false);
                    fragmentManager.beginTransaction().add(R.id.container,loginFrag, commonUtil.LOGIN_TAG_FRAGMENT).commit();
                }
            }
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
            } else if (item.getTitle().equals("Farmers Screen")) {
                if (!farmerListFragment.isVisible()) {
                    fragmentManager.beginTransaction().replace(R.id.container, farmerListFragment, commonUtil.FARMER_LIST_TAG_FRAGMENT).commit();
                }

                drawer.closeDrawer(GravityCompat.START);
            } else if (item.getTitle().equals("Export Data")) {
                fragmentManager.beginTransaction().replace(R.id.container, exportDataFrag, commonUtil.EXPORT_TAG_FRAGMENT).commit();
                drawer.closeDrawer(GravityCompat.START);
            } else if(item.getTitle().equals("Log Out")) {
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0); // 0 - for private mode
                SharedPreferences.Editor editor = settings.edit();

                editor.putBoolean("hasLoggedIn", false);

                editor.commit();

                fragmentManager.beginTransaction().replace(R.id.container, loginFrag, commonUtil.LOGIN_TAG_FRAGMENT).commit();
                drawer.closeDrawer(GravityCompat.START);
                //drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                toggle.setDrawerIndicatorEnabled(false);
            }
            return true;
        }
    };



    @Override
    public void onBackPressed() {
        if(milkEntryFragment.isVisible()){
            if (milkEntryFragment.areDataFieldsEmpty()) {
                fragmentManager.beginTransaction().replace(R.id.container, farmerListFragment, commonUtil.FARMER_LIST_TAG_FRAGMENT).commit();
            } else {
                showUnsavedDataMessage(milkEntryFragment, this);
            }
        } else if (histRecordFrag.isVisible()) {
            fragmentManager.beginTransaction().replace(R.id.container, histListFrag, commonUtil.HIST_LIST_TAG_FRAGMENT).commit();
        }
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.milk_entry, menu);

        return true;
    }

    //================================================================================
    // Fragment Interaction Listeners
    //================================================================================

    /**
     * Listener for milkEntryFragment
     * @param item
     */
    @Override
    public void onListFragmentInteraction(FarmerItem item) {
        Bundle bundle = new Bundle();
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        userId = settings.getString("transporterId", "");
        String fullFarmerName = item.getFirstName() + " " + item.getLastName();
        bundle.putString("farmername", fullFarmerName);
        bundle.putString("farmerid", item.getId());
        bundle.putString("transporterId", userId);


        milkEntryFragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.container, milkEntryFragment, commonUtil.MILK_ENTRY_TAG_FRAGMENT)
                .commit();
    }

    /**
     * Listener for histListFrag
     *
     * @param item
     */
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
                new AlertDialog.Builder(context)
                        .setTitle("Unsaved data")
                        .setMessage("Are you sure you want to go back?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, unsavedDataDialog)
                        .setNegativeButton(android.R.string.no, unsavedDataDialog).show();
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onExportFragmentInteraction(Uri uri) {

    }
}
