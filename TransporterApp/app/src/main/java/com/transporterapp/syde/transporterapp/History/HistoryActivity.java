package com.transporterapp.syde.transporterapp.History;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.transporterapp.syde.transporterapp.DataStructures.MilkRecord;
import com.transporterapp.syde.transporterapp.R;
import com.transporterapp.syde.transporterapp.commonUtil;

/**
 * Created by chari on 2018-01-25.
 */

public class HistoryActivity extends AppCompatActivity
        implements HistListFrag.OnListFragmentInteractionListener {

    private DrawerLayout drawer;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private HistListFrag histListFrag = new HistListFrag();
    private HistRecordFrag histRecordFrag = new HistRecordFrag();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_container);

        if (findViewById(R.id.container) != null) {
            if (savedInstanceState == null) {

                fragmentManager.beginTransaction().add(R.id.container, histListFrag, commonUtil.HIST_LIST_TAG_FRAGMENT).commit();
            }
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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



}
