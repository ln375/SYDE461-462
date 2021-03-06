package com.transporterapp.syde.transporterapp.History;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Entity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.transporterapp.syde.transporterapp.DataStructures.FarmerItem;
import com.transporterapp.syde.transporterapp.DataStructures.MilkRecord;
import com.transporterapp.syde.transporterapp.Main;
import com.transporterapp.syde.transporterapp.R;
import com.transporterapp.syde.transporterapp.commonUtil;
import com.transporterapp.syde.transporterapp.databases.dbUtil;
import com.transporterapp.syde.transporterapp.databases.DatabaseConstants;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class HistListFrag extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private String transporterId = "";
    public String mrouteId = "";
    private OnListFragmentInteractionListener mListener;
    private Button sortButton;
    private Button calendarButton;
    private TextView routeId;
    private RecyclerView recyclerView;
    Calendar myCalendar = Calendar.getInstance();
    private int selectedPosition = 0;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HistListFrag() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static HistListFrag newInstance(int columnCount) {
        HistListFrag fragment = new HistListFrag();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            transporterId = getArguments().getString("userid");
            mrouteId = getArguments().getString("routeid");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_milkrecord_list, container, false);
        calendarButton = (Button) view.findViewById(R.id.calendarButton);
        sortButton = (Button) view.findViewById(R.id.sortButton);

        // Set title bar
        ((Main) getActivity()).setActionBarTitle("History");

        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Log.d("cancel", "cancelled");
                        calendarButton.setText("No date Selected");
                        updateList("","");
                    }
                });

                datePickerDialog.show();
            }
        });


        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Integer[] selectedItem = {0};
                String[] sortOptions = getResources().getStringArray(R.array.sort_options);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.sort_options)
                        .setSingleChoiceItems(R.array.sort_options, selectedPosition, null)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String[] sortOptions = getResources().getStringArray(R.array.sort_options);
                        selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                        sortButton.setText(sortOptions[selectedPosition]);
                        String date = calendarButton.getText().toString();
                        if (date.equalsIgnoreCase("No date selected")) {
                            date = "";
                        }
                        String sort = sortButton.getText().toString();

                        updateList(date, sort);
                    }
                })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        })
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                sortButton.setText("No sort selected");
                                updateList("","");
                            }
                        });
                ;

                builder.create().show();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String date = calendarButton.getText().toString();
        if (date.equalsIgnoreCase("No date selected")) {
            date = "";
        }
        String sort = sortButton.getText().toString();

        updateList(date, sort);
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
        void onListFragmentInteraction(MilkRecord item);
    }


    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String temp = updateLabel();
            updateList(temp, "");
        }

    };

    private String updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String date = sdf.format(myCalendar.getTime());
        calendarButton.setText(date);
        String sqlFormat = "yyyy-MM-dd";
        sdf = new SimpleDateFormat(sqlFormat, Locale.US);
        return sdf.format(myCalendar.getTime());
    }

    private void updateList(String date, String sort){
        recyclerView = (RecyclerView) getView().findViewById(R.id.hist_record_list);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), mColumnCount));
        }

        Cursor records;
        List<MilkRecord> milkRecords = new ArrayList<>();
        if (date.isEmpty()) {
            records = dbUtil.selectStatement(DatabaseConstants.tbltrFarmerTransporter, DatabaseConstants.transporter_id, "=", transporterId, getContext());
            milkRecords = commonUtil.convertCursorToMilkRecordList(records);
        } else {
            List<String> whereCondition = new ArrayList<String>(Arrays.asList(DatabaseConstants.transporter_id, DatabaseConstants.date));
            List<String> whereOperator = new ArrayList<String>(Arrays.asList("=", "="));
            List<String> whereValue = new ArrayList<String>(Arrays.asList(transporterId, date));
            records = dbUtil.selectStatement(DatabaseConstants.tbltrFarmerTransporter, "*", whereCondition, whereOperator, whereValue, getContext());
            milkRecords = commonUtil.convertCursorToMilkRecordList(records);
        }

        if (!sort.equalsIgnoreCase("No sort selected")) {
            if (sort.equalsIgnoreCase("Route")){
                 Collections.sort(milkRecords, new Comparator<MilkRecord>() {
                    @Override
                    public int compare(MilkRecord r1, MilkRecord r2) {
                        if (Integer.valueOf(r1.getRouteId()) > Integer.valueOf(r2.getRouteId())) {
                            return 1;
                        } else if (Integer.valueOf(r1.getRouteId()) < Integer.valueOf(r2.getRouteId())){
                            return -1;
                        }
                        return 0;
                    }
                });
            } else if (sort.equalsIgnoreCase("Alphabetical")) {
                Collections.sort(milkRecords, new Comparator<MilkRecord>() {
                    @Override
                    public int compare(MilkRecord r1, MilkRecord r2) {
                        return r1.toString().compareTo(r2.toString());
                    }
                });
                List<String> farmerIds = new ArrayList<>();
                for (MilkRecord milkRecord : milkRecords) {
                    farmerIds.add(milkRecord.getFarmerId());
                }

                List<FarmerItem> farmers = commonUtil.convertCursorToFarmerItemList(dbUtil.selectStatement(DatabaseConstants.tblFarmer, "", "", "", getContext()));
                for (int i = farmers.size() - 1; i <= 0; i--) {
                    if (!farmerIds.contains(farmers.get(i).getId())) {
                        farmers.remove(i);
                    }
                }

                Collections.sort(farmers, new Comparator<FarmerItem>() {
                    @Override
                    public int compare(FarmerItem o1, FarmerItem o2) {
                        int res = o1.getFirstName().compareToIgnoreCase(o2.getFirstName());
                        if (res != 0) {
                            return res;
                        }
                        return o1.getLastName().compareToIgnoreCase(o2.getLastName());
                    }
                });

                List<MilkRecord> temp = new ArrayList<>();
                for (FarmerItem farmer : farmers) {
                    for (int i = 0; i < milkRecords.size(); i++) {
                        if (farmer.getId().equalsIgnoreCase(milkRecords.get(i).getFarmerId())) {
                            temp.add(milkRecords.get(i));
                            break;
                        }
                    }
                }

                milkRecords.clear();
                milkRecords.addAll(temp);

            } else if (sort.equalsIgnoreCase("Time of Collection")) {
                Collections.sort(milkRecords, new Comparator<MilkRecord>() {
                    @Override
                    public int compare(MilkRecord r1, MilkRecord r2) {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            Date temp = sdf.parse(r1.getDate());
                            Date temp2 = sdf.parse(r2.getDate());
                            if (temp.equals(temp2)) {
                                String ugh = "";
                            }
                            if (temp.before(temp2)) {
                                return 1;
                            } else if (temp2.before(temp)) {
                                return -1;
                            } else {
                                // same date, sort by time
                                sdf = new SimpleDateFormat("HH:mm:ss");
                                temp = sdf.parse(r1.getTime());
                                temp2 = sdf.parse(r2.getTime());

                                if (temp.before(temp2)) {
                                    return 1;
                                } else if (temp2.before(temp)) {
                                    return -1;
                                }
                            }
                            return 0;
                        } catch (ParseException e) {
                            return 0;
                        }
                    }
                });
            } else if (sort.equalsIgnoreCase("None")) {
                sortButton.setText("No sort selected");
            }
        }

        if (milkRecords == null) {
            milkRecords = new ArrayList<>();
        }

        recyclerView.setAdapter(new MyMilkRecordRecyclerViewAdapter(milkRecords, mListener));
    }

}
