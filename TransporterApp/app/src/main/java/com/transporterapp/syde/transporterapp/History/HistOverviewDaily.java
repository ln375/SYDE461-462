package com.transporterapp.syde.transporterapp.History;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.transporterapp.syde.transporterapp.DataStructures.Jug;
import com.transporterapp.syde.transporterapp.DataStructures.MilkRecord;
import com.transporterapp.syde.transporterapp.R;
import com.transporterapp.syde.transporterapp.commonUtil;
import com.transporterapp.syde.transporterapp.databases.DatabaseConstants;
import com.transporterapp.syde.transporterapp.databases.dbUtil;

import java.sql.Date;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HistOverviewDaily.OnListFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HistOverviewDaily#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistOverviewDaily extends Fragment {
    RecyclerView recyclerView;
    private String transporterId = "";

    private OnListFragmentInteractionListener mListener;

    public HistOverviewDaily() {
        // Required empty public constructor
    }

    public static HistOverviewDaily newInstance(int page, String title) {
        HistOverviewDaily histFragDaily = new HistOverviewDaily();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        histFragDaily.setArguments(args);
        return histFragDaily;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            transporterId = getArguments().getString("userid");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_hist_overview_daily, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        init(view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(MilkRecord item);
    }

    public void init(View v){
        TableLayout dailyList = (TableLayout) v.findViewById(R.id.dailyTable);
        List<String> relevantDates = dbUtil.selectStatement(DatabaseConstants.tbltrFarmerTransporter, DatabaseConstants.date, DatabaseConstants.transporter_id, "=", "9", v.getContext());
        List<MilkRecord> relevantRecords = commonUtil.convertCursorToMilkRecordList(dbUtil.selectStatement(DatabaseConstants.tbltrFarmerTransporter, DatabaseConstants.transporter_id, "=", "9", v.getContext()));
        Set<String> dates = new HashSet<>();
        dates.addAll(relevantDates);
        relevantDates.clear();
        relevantDates.addAll(dates);

        // Sort dates so most recent collections are shown first
        Collections.sort(relevantDates, new Comparator<String>() {
            @Override
            public int compare(String r1, String r2) {
                if (Date.valueOf(r1).after(Date.valueOf(r2))) {
                    return -1;
                } else if (Date.valueOf(r2).after(Date.valueOf(r1))) {
                    return 1;
                }
                return 0;
            }
        });

        // Get progressbar max
        List<Jug> jugList = commonUtil.convertCursorToJugList(dbUtil.selectStatement(DatabaseConstants.tblJug, DatabaseConstants.transporter_id, "=", "9", v.getContext()));
        int progressBarMax = 0;
        for (Jug jug : jugList) {
            progressBarMax += (int) Math.ceil(Double.valueOf(jug.getSize()));
        }


        for (int i = 0; i <relevantDates.size(); i++) {
            /*List<String> whereCondition = new ArrayList<String>(Arrays.asList(DatabaseConstants.transporter_id, DatabaseConstants.date));
            List<String> whereOperator = new ArrayList<String>(Arrays.asList("=", "="));
            List<String> whereValue = new ArrayList<String>(Arrays.asList("9", relevantDates.get(i)));
            Cursor records = dbUtil.selectStatement(DatabaseConstants.tbltrFarmerTransporter, "*", whereCondition, whereOperator, whereValue, getContext());
            List<MilkRecord> milkRecords = commonUtil.convertCursorToMilkRecordList(records);*/

            int numOfTrips = 0;
            double totalVolume = 0;

            for (MilkRecord record : relevantRecords) {
                if (relevantDates.get(i).equalsIgnoreCase(record.getDate())) {
                    numOfTrips++;
                    if (!record.getMilkWeight().isEmpty()) {
                        totalVolume += Double.valueOf(record.getMilkWeight());
                    }
                }
            }

            Date temp = Date.valueOf(relevantDates.get(i));
            Calendar cal = Calendar.getInstance();
            cal.setTime(temp);
            String collectionDate = commonUtil.getMonthString(cal.get(Calendar.MONTH)) + " " + String.valueOf(cal.get(Calendar.DAY_OF_MONTH));

            TableRow row= new TableRow(v.getContext());
            TextView date = new TextView(v.getContext());
            date.setText(collectionDate);
            date.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);


            TextView numberOfCollections = new TextView(v.getContext());
            numberOfCollections.setText(Integer.toString(numOfTrips));
            numberOfCollections.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            DecimalFormat df = new DecimalFormat("#.#");

            TextView volumeCollected = new TextView(v.getContext());
            volumeCollected.setText(String.valueOf(df.format(totalVolume)) + " L");
            volumeCollected.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            RelativeLayout progressBarHolder = new RelativeLayout(getContext());
            ProgressBar progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);
            progressBar.setProgress((int) Math.ceil(totalVolume));
            progressBar.setMax(progressBarMax);
            progressBarHolder.addView(progressBar);
            progressBarHolder.setPadding(0,0,1,0);

            row.addView(date);
            row.addView(numberOfCollections);
            row.addView(volumeCollected);
            row.addView(progressBarHolder);
            dailyList.addView(row);
        }
    }
}
