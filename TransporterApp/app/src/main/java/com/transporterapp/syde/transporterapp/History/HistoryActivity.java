package com.transporterapp.syde.transporterapp.History;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.transporterapp.syde.transporterapp.DataStructures.MilkRecord;
import com.transporterapp.syde.transporterapp.R;
import com.transporterapp.syde.transporterapp.commonUtil;

/**
 * Created by chari on 2018-01-25.
 */

public class HistoryActivity extends AppCompatActivity
        implements HistListFrag.OnListFragmentInteractionListener {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private HistListFrag histListFrag = new HistListFrag();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_container);

        if(findViewById(R.id.container) != null){
            if (savedInstanceState == null){
               fragmentManager.beginTransaction().add(R.id.container,histListFrag, commonUtil.HIST_LIST_TAG_FRAGMENT).commit();
            }
        }
    }

    @Override
    public void onListFragmentInteraction(MilkRecord item) {
        Bundle bundle = new Bundle();
        String recordID = item.getId();
        bundle.putString("milkRecordId", recordID);
        histListFrag.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.container, histListFrag, commonUtil.HIST_LIST_TAG_FRAGMENT).commit();
    }
}
