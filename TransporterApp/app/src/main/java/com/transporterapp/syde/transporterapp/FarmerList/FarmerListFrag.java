package com.transporterapp.syde.transporterapp.FarmerList;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.transporterapp.syde.transporterapp.DataStructures.FarmerItem;
import com.transporterapp.syde.transporterapp.DataStructures.Jug;
import com.transporterapp.syde.transporterapp.ExportData.ExportDataFrag;
import com.transporterapp.syde.transporterapp.UIDecorations.DividerItemDecoration;
import com.transporterapp.syde.transporterapp.Main;
import com.transporterapp.syde.transporterapp.R;
import com.transporterapp.syde.transporterapp.commonUtil;
import com.transporterapp.syde.transporterapp.databases.DatabaseConstants;
import com.transporterapp.syde.transporterapp.databases.dbUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class FarmerListFrag extends Fragment implements SearchView.OnQueryTextListener{

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private MyFarmerRecyclerViewAdapter farmerRecyclerViewAdapter;
    private String routeId = "";
    private ExportDataFrag exportDataFrag = new ExportDataFrag();
    private AddFarmerFrag addFarmerFrag = new AddFarmerFrag();
    private List<String> farmerContributed;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FarmerListFrag() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FarmerListFrag newInstance(int columnCount) {
        FarmerListFrag fragment = new FarmerListFrag();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            routeId = getArguments().getString("routeId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        View view = inflater.inflate(R.layout.fragment_farmer_list, container, false);

        // Set the adapter
        if (view instanceof RelativeLayout) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.farmer_list);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            initFarmerList(recyclerView, context);
           }

        FloatingActionButton addFarmerFAB = (FloatingActionButton) view.findViewById(R.id.add_farmer);
        addFarmerFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("routeId", routeId);

                if (addFarmerFrag.getArguments() != null) {
                    addFarmerFrag.getArguments().clear();
                    addFarmerFrag.getArguments().putAll(bundle);
                } else {
                    addFarmerFrag.setArguments(bundle);
                }
                fragmentManager.beginTransaction().replace(R.id.container, addFarmerFrag).addToBackStack(null).commit();

            }
        });

        final Button saveLogbookButton = (Button) view.findViewById(R.id.save_logbook);
        if (exportDataFrag.getArguments() != null) {
            exportDataFrag.getArguments().clear();
        }
        saveLogbookButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.container, exportDataFrag).addToBackStack(null).commit();
            }
        });

        List<Jug> jugList = commonUtil.convertCursorToJugList(dbUtil.selectStatement(DatabaseConstants.tblJug, "", "", "", this.getContext()));
        double collectedMilk = 0;
        for (Jug jug : jugList) {
            collectedMilk += Double.valueOf(jug.getCurrentVolume());
        }

        final TextView totalMilkCollected = (TextView) view.findViewById(R.id.total_milk_collected);
        totalMilkCollected.setText(collectedMilk + " L Total");

        return view;
    }

    /*
    * Initialize farmer list
     */
    private void initFarmerList(RecyclerView recyclerView, Context context){
        Cursor farmers = dbUtil.selectStatement("farmers", "route_id", "=", routeId, context);
        List<FarmerItem> convertedFarmerList = commonUtil.convertCursorToFarmerItemList(farmers);
        ArrayList<FarmerItem> convertedFarmerArrayList = new ArrayList<FarmerItem>(convertedFarmerList);

        for (FarmerItem farmerItem : convertedFarmerArrayList){
            String farmer_id = farmerItem.getId();
            String farmer_name = farmerItem.getFirstName();
            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            farmerContributed = dbUtil.selectStatement(DatabaseConstants.tbltrFarmerTransporter,"farmer_id", "date", "=", date, context);
        }

        farmerRecyclerViewAdapter = new MyFarmerRecyclerViewAdapter(convertedFarmerArrayList, mListener, farmerContributed);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        recyclerView.setAdapter(farmerRecyclerViewAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        farmerRecyclerViewAdapter.getFilter().filter(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        farmerRecyclerViewAdapter.getFilter().filter(query);
        return true;
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
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(FarmerItem item);
    }
}
