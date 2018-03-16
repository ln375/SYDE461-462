package com.transporterapp.syde.transporterapp.History;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * {@link HistOverviewMonthly.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HistOverviewMonthly#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistOverviewMonthly extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HistOverviewMonthly() {
        // Required empty public constructor
    }

    public static HistOverviewMonthly newInstance(int page, String title) {
        HistOverviewMonthly histFragMonthly = new HistOverviewMonthly();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        histFragMonthly.setArguments(args);
        return histFragMonthly;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_hist_overview_monthly, container, false);
        init(v);
        return v;
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void init(View v) {
        TableLayout monthlyList = (TableLayout) v.findViewById(R.id.monthlyTable);
        List<MilkRecord> relevantRecords = commonUtil.convertCursorToMilkRecordList(dbUtil.selectStatement(DatabaseConstants.tbltrFarmerTransporter, DatabaseConstants.transporter_id, "=", "9", v.getContext()));
        List<String> relevantDates = dbUtil.selectStatement(DatabaseConstants.tbltrFarmerTransporter, DatabaseConstants.date, DatabaseConstants.transporter_id, "=", "9", v.getContext());

        // Organize relevantDates by month and year
        Set<String> dates = new HashSet<>();
        for (String temp : relevantDates) {
            Date date = Date.valueOf(temp);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            String month = getMonthString(cal.get(Calendar.MONTH)) + " " + String.valueOf(cal.get(Calendar.YEAR));
            dates.add(month);
        }

        relevantDates.clear();
        relevantDates.addAll(dates);

        // Get ProgressBar Max
        List<Jug> jugList = commonUtil.convertCursorToJugList(dbUtil.selectStatement(DatabaseConstants.tblJug, "", "", "", v.getContext()));
        int progressBarMax = 0;
        for (Jug jug : jugList) {
            progressBarMax += (int) Math.ceil(Double.valueOf(jug.getCurrentVolume()));
        }

        progressBarMax = progressBarMax * 30;


        for (int i = 0; i < relevantDates.size(); i++) {
            int numOfTrips = 0;
            double totalVolume = 0;

            //Check milkRecord list to see which items match the relevant date
            for (MilkRecord record: relevantRecords) {
                Date date = Date.valueOf(record.getDate());
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                String month = getMonthString(cal.get(Calendar.MONTH)) + " " + String.valueOf(cal.get(Calendar.YEAR));
                if (relevantDates.get(i).equalsIgnoreCase(month)) {
                    numOfTrips++;
                    totalVolume += Double.valueOf(record.getMilkWeight());
                }
            }

            // Now add table row

            TableRow row = new TableRow(v.getContext());
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            TextView date = new TextView(v.getContext());
            date.setText(relevantDates.get(i));
            date.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);


            TextView numberOfCollections = new TextView(v.getContext());
            numberOfCollections.setText(Integer.toString(numOfTrips));
            numberOfCollections.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            TextView volumeCollected = new TextView(v.getContext());
            volumeCollected.setText(String.valueOf(totalVolume) + " L");

            RelativeLayout progressBarHolder = new RelativeLayout(getContext());
            ProgressBar progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);
            progressBar.setProgress((int) Math.ceil(totalVolume));
            progressBar.setMax(progressBarMax);
            progressBarHolder.addView(progressBar);
            progressBarHolder.setPadding(0, 0, 1, 0);

            row.addView(date);
            row.addView(numberOfCollections);
            row.addView(volumeCollected);
            row.addView(progressBarHolder);
            monthlyList.addView(row);
        }
    }

    public String getMonthString(int i){
        switch (i){
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
            default:
                return null;
        }


    }
}
