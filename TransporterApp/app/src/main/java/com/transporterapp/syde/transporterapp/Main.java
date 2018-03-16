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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;

import com.transporterapp.syde.transporterapp.DataStructures.Jug;
import com.transporterapp.syde.transporterapp.DataStructures.Routes;
import com.transporterapp.syde.transporterapp.ExportData.DeviceList.DeviceItem;
import com.transporterapp.syde.transporterapp.ExportData.DeviceList.DeviceListFragment;
import com.transporterapp.syde.transporterapp.FarmerList.AddFarmerFrag;
import com.transporterapp.syde.transporterapp.FarmerList.FarmerListFrag;
import com.transporterapp.syde.transporterapp.FarmerList.FarmerListFrag.OnListFragmentInteractionListener;
import com.transporterapp.syde.transporterapp.CollectMilk.MilkEntryFrag;
import com.transporterapp.syde.transporterapp.DataStructures.FarmerItem;
import com.transporterapp.syde.transporterapp.DataStructures.MilkRecord;
import com.transporterapp.syde.transporterapp.ExportData.ExportDataFrag;
import com.transporterapp.syde.transporterapp.FarmerProfile.FarmerProfileFrag;
import com.transporterapp.syde.transporterapp.History.HistListFrag;
import com.transporterapp.syde.transporterapp.History.HistMainFrag;
import com.transporterapp.syde.transporterapp.History.HistOverviewDaily;
import com.transporterapp.syde.transporterapp.History.HistOverviewFrag;
import com.transporterapp.syde.transporterapp.History.HistOverviewMonthly;
import com.transporterapp.syde.transporterapp.History.HistRecordFrag;
import com.transporterapp.syde.transporterapp.LoginScreen.LoginFragment;
import com.transporterapp.syde.transporterapp.databases.DatabaseConstants;
import com.transporterapp.syde.transporterapp.databases.dbUtil;

import java.util.List;
import com.transporterapp.syde.transporterapp.Routes.ChooseRoutesFrag;

import static com.transporterapp.syde.transporterapp.LoginScreen.LoginFragment.PREFS_NAME;

//import com.testfairy.TestFairy;


public class Main extends AppCompatActivity
        implements OnListFragmentInteractionListener, HistListFrag.OnListFragmentInteractionListener, LoginFragment.OnFragmentInteractionListener, ExportDataFrag.OnFragmentInteractionListener,
        DeviceListFragment.OnListFragmentInteractionListener, ChooseRoutesFrag.OnListFragmentInteractionListener, HistMainFrag.OnFragmentInteractionListener , HistOverviewFrag.OnFragmentInteractionListener,
        HistOverviewMonthly.OnFragmentInteractionListener, HistOverviewDaily.OnFragmentInteractionListener, AddFarmerFrag.OnFragmentInteractionListener {
    public static Main instance = null;
    private HistRecordFrag histRecordFrag = new HistRecordFrag();
    private HistListFrag histListFrag = new HistListFrag();
    public FragmentManager fragmentManager = getSupportFragmentManager();
    private FarmerListFrag farmerListFragmentForMilkEntry = new FarmerListFrag();
    private FarmerListFrag farmerListFragmentForFarmerProfile = new FarmerListFrag();
    private MilkEntryFrag milkEntryFragment = new MilkEntryFrag();
    private LoginFragment loginFrag = new LoginFragment();
    private ExportDataFrag exportDataFrag = new ExportDataFrag();
    private FarmerProfileFrag farmerProfileFrag = new FarmerProfileFrag();
    private HistMainFrag histMainFrag = new HistMainFrag();
    private ChooseRoutesFrag chooseRoutesFrag = new ChooseRoutesFrag();
    private Toolbar toolbar;

    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    private String userId = "";

    private int menuFragmentId;
    private static final String BACK_STACK_ROOT_TAG = "root_fragment";

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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.fragment_container);

        //TestFairy.begin(this, "a61f203ba668965e0295409c7fec4b15d7a31770");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
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
                    farmerListFragmentForFarmerProfile.setArguments(bundle);
                    farmerListFragmentForMilkEntry.setArguments(bundle);
                    chooseRoutesFrag.setArguments(bundle);
                    fragmentManager.beginTransaction()
                            .add(R.id.container,chooseRoutesFrag, BACK_STACK_ROOT_TAG)
                            .addToBackStack(BACK_STACK_ROOT_TAG)
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
                Bundle bundle = new Bundle();
                bundle.putString("userid", userId);
                if (histMainFrag.getArguments() != null) {
                    histMainFrag.getArguments().clear();
                    histMainFrag.getArguments().putAll(bundle);
                } else {
                    histMainFrag.setArguments(bundle);
                }
                //fragmentManager.beginTransaction().replace(R.id.container, histListFrag).addToBackStack(null).commit();
                fragmentManager.beginTransaction().replace(R.id.container, histMainFrag).addToBackStack(null).commit();
                drawer.closeDrawer(GravityCompat.START);
            } else if(item.getTitle().equals("Add New Milk Record")) {
                Bundle bundle = new Bundle();
                bundle.putString("userid", userId);
                if (chooseRoutesFrag.getArguments() != null) {
                    chooseRoutesFrag.getArguments().clear();
                    chooseRoutesFrag.getArguments().putAll(bundle);
                } else {
                    chooseRoutesFrag.setArguments(bundle);
                }

                fragmentManager.beginTransaction().replace(R.id.container, chooseRoutesFrag).addToBackStack(null).commit();
                drawer.closeDrawer(GravityCompat.START);
            } else if (item.getTitle().equals("Farmers Screen")) {
                setActionBarTitle(commonUtil.getCurrentDate());
                menuFragmentId = item.getItemId();
                fragmentManager.beginTransaction().replace(R.id.container, farmerListFragmentForFarmerProfile).addToBackStack(null).commit();
                drawer.closeDrawer(GravityCompat.START);
            } else if (item.getTitle().equals("Export Data")) {
                setActionBarTitle("Export Data");
                fragmentManager.beginTransaction().replace(R.id.container, exportDataFrag).addToBackStack(null).commit();
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
                setActionBarTitle("Transporter App");
            }
            return true;
        }
    };



    @Override
    public void onBackPressed() {
        if(milkEntryFragment.isVisible()){
            if (milkEntryFragment.areDataFieldsEmpty()) {
                List<Jug> jugList = commonUtil.convertCursorToJugList(dbUtil.selectStatement(DatabaseConstants.tblJug, "", "", "", getApplicationContext()));
                double collectedMilk = 0;
                for (Jug jug : jugList) {
                    collectedMilk += Double.valueOf(jug.getCurrentVolume());
                }
                setActionBarTitle("Milk Collected: " + collectedMilk + "L");
                milkEntryFragment.clearData();
                super.onBackPressed();
            } else {
                showUnsavedDataMessage(milkEntryFragment, this);
            }
        } else if (fragmentManager.getBackStackEntryCount() == 1) {
            finish();
        } else if (histListFrag.isVisible() || farmerListFragmentForFarmerProfile.isVisible() || exportDataFrag.isVisible()){
            //Don't allow backpress on these screens
        }else {
            super.onBackPressed();

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
     * Listener for milkEntryFragment and farmerProfileFragment
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

       /* if (menuFragmentId == R.id.farmersActivity){
            //Navigate to farmer profile frag
            farmerProfileFrag.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.container, farmerProfileFrag)
                    .addToBackStack(null)
                    .commit();
        } else {*/
            //Navigate to milk entry frag
            milkEntryFragment.setArguments(bundle);
            try{
                fragmentManager.beginTransaction()
                        .replace(R.id.container, milkEntryFragment)
                        .addToBackStack(null)
                        .commit();
            } catch (Exception e) {
                String temp = e.getLocalizedMessage();
                String temp2 = "Hello";
            }

        //}
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
        fragmentManager.beginTransaction().replace(R.id.container, histRecordFrag, BACK_STACK_ROOT_TAG).addToBackStack(BACK_STACK_ROOT_TAG).commit();
    }


    /**
     * Displays a dialog to ensure user wants to backspace if there is still data on the milk entry page
     * Example Usage: showUnsavedDataMessage(milkEntryFrag, this);
     *
     * @param fragment
     * @param context
     */
    public void showUnsavedDataMessage(Fragment fragment, Context context){
        new AlertDialog.Builder(context)
                .setTitle("Unsaved data")
                .setMessage("Are you sure you want to go back?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, unsavedDataDialog)
                .setNegativeButton(android.R.string.no, unsavedDataDialog).show();

    }

    /**
     * DialogInterface listener used for showUnsavedDataMessage
     */
    private DialogInterface.OnClickListener unsavedDataDialog = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    milkEntryFragment.clearData();
                    Main.super.onBackPressed();
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

    @Override
    public void onListFragmentInteraction(DeviceItem item) {

    }

    @Override
    public void onListFragmentInteraction(Routes item) {
        Bundle bundle = new Bundle();
        bundle.putString("routeId", item.getRouteId());
        if (farmerListFragmentForMilkEntry.getArguments() != null) {
            farmerListFragmentForMilkEntry.getArguments().clear();
            farmerListFragmentForMilkEntry.getArguments().putAll(bundle);
        } else {
            farmerListFragmentForMilkEntry.setArguments(bundle);
        }
        fragmentManager.beginTransaction().replace(R.id.container, farmerListFragmentForMilkEntry, BACK_STACK_ROOT_TAG).addToBackStack(BACK_STACK_ROOT_TAG).commit();

    }
}
