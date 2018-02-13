package com.transporterapp.syde.transporterapp.FarmerProfile;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.transporterapp.syde.transporterapp.Main;
import com.transporterapp.syde.transporterapp.R;


public class FarmerProfileFrag extends Fragment {

    private static final String FARMER_ID = "farmerid";
    private static final String FARMER_NAME = "farmername";

    private String mFarmerId;
    private String mFarmerName;

    public FarmerProfileFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            mFarmerId = getArguments().getString(FARMER_ID);
            mFarmerName = getArguments().getString(FARMER_NAME);

            // Set title bar
            ((Main) getActivity()).setActionBarTitle(mFarmerName);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_farmer_profile, container, false);


        TextView farmerName = (TextView) view.findViewById(R.id.profile_farmer_name);
        farmerName.setText("Name: " + mFarmerName);

        TextView farmerId = (TextView) view.findViewById(R.id.profile_farmer_id);
        farmerId.setText("Id: " + mFarmerId);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}

