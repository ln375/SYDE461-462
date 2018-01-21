package com.transporterapp.syde.transporterapp.CollectMilk;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.testfairy.TestFairy;
import com.transporterapp.syde.transporterapp.CollectMilk.FarmerListFrag.OnListFragmentInteractionListener;
import com.transporterapp.syde.transporterapp.DataStructures.FarmerItem;
import com.transporterapp.syde.transporterapp.R;
import com.transporterapp.syde.transporterapp.commonUtil;


public class CollectMilkActivity extends AppCompatActivity
        implements OnListFragmentInteractionListener {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FarmerListFrag farmerListFragment = new FarmerListFrag();
    private MilkEntryFrag milkEntryFragment = new MilkEntryFrag();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_container);

        if(findViewById(R.id.container) != null){
            if (savedInstanceState == null){
                fragmentManager.beginTransaction().add(R.id.container,farmerListFragment, commonUtil.FARMER_LIST_TAG_FRAGMENT).commit();
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
    }

    @Override
    public void onBackPressed() {
        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }*/

        if(milkEntryFragment.isVisible()){
            showUnsavedDataMessage(milkEntryFragment, this);

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

    @Override
    public void onListFragmentInteraction(FarmerItem.farmer item) {
        //fragmentManager.beginTransaction().hide(farmerListFragment);
        Bundle bundle = new Bundle();
        String fullFarmerName = item.getFirstName() + " " + item.getLastName();
        bundle.putString("farmername", fullFarmerName);
        bundle.putString("farmerid", item.getId());
        milkEntryFragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.container, milkEntryFragment, commonUtil.MILK_ENTRY_TAG_FRAGMENT).commit();
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
