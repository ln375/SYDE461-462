package com.transporterapp.syde.transporterapp.FarmerList;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.transporterapp.syde.transporterapp.DataStructures.Jug;
import com.transporterapp.syde.transporterapp.Main;
import com.transporterapp.syde.transporterapp.R;
import com.transporterapp.syde.transporterapp.commonUtil;
import com.transporterapp.syde.transporterapp.databases.DatabaseConstants;
import com.transporterapp.syde.transporterapp.databases.dbUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddFarmerFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddFarmerFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFarmerFrag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private EditText firstName;
    private  EditText lastName;
    private EditText phoneNumber;
    private Button addFarmer;
    private FragmentManager fragmentManager;
    private String mRouteId;

    private OnFragmentInteractionListener mListener;

    public AddFarmerFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddFarmerFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFarmerFrag newInstance(String param1, String param2) {
        AddFarmerFrag fragment = new AddFarmerFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mRouteId = getArguments().getString("routeId");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_farmer, container, false);

        // Set title bar
        ((Main) getActivity()).setActionBarTitle("Add Farmer");

        fragmentManager = getActivity().getSupportFragmentManager();
        firstName = (EditText) view.findViewById(R.id.first_name);
        lastName = (EditText) view.findViewById(R.id.last_name);
        phoneNumber = (EditText) view.findViewById(R.id.phone_number);
        addFarmer = (Button) view.findViewById(R.id.add_farmer_button);

        addFarmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String routeId = mRouteId;
                String firstNameText = firstName.getText().toString();
                String lastNameText = lastName.getText().toString();
                String phoneNumberText = phoneNumber.getText().toString();
                int numFarmers = (int) DatabaseUtils.queryNumEntries(dbUtil.database, DatabaseConstants.tblFarmer);
                String id = Integer.toString(numFarmers + 1);

                List<String> columns = new ArrayList<>();
                columns.addAll(Arrays.asList(DatabaseConstants.colFarmer));

                List<String> values = Arrays.asList(id, firstNameText, lastNameText, phoneNumberText, routeId);
                saveData(columns, values, view.getContext());

                Toast.makeText(getContext(),"Data Inserted", Toast.LENGTH_LONG).show();

                InputMethodManager inputManager = (InputMethodManager)
                        Main.instance.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow((null == Main.instance.getCurrentFocus()) ? null : Main.instance.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);


                getActivity().onBackPressed();



            }
        });

        return view;

    }

    public void saveData(List<String> columns, List<String> values, Context context) {
        dbUtil.insertStatement(DatabaseConstants.tblFarmer, columns, values, context);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
