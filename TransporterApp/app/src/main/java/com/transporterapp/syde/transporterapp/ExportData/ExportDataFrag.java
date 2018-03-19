package com.transporterapp.syde.transporterapp.ExportData;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.transporterapp.syde.transporterapp.DataStructures.MilkRecord;
import com.transporterapp.syde.transporterapp.ExportData.DeviceList.DeviceItem;
import com.transporterapp.syde.transporterapp.ExportData.DeviceList.DeviceListFragment;
import com.transporterapp.syde.transporterapp.Main;
import com.transporterapp.syde.transporterapp.R;
import com.transporterapp.syde.transporterapp.commonUtil;
import com.transporterapp.syde.transporterapp.databases.DatabaseConstants;
import com.transporterapp.syde.transporterapp.databases.dbUtil;

import java.io.BufferedInputStream;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExportDataFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExportDataFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExportDataFrag extends Fragment implements DeviceListFragment.OnListFragmentInteractionListener {

    private OnFragmentInteractionListener mListener;
    private Button createCSVFile;
    private String exportSucceeded = null;
    private EditText ipAddress;
    private Button sendCSVFile;
    private RadioGroup exportMethod;
    private TextView lblExportMethod;
    private TextView lblExportInstructionsTitle;
    private TextView lblExportInstructions;
    private RadioButton btnWifi;
    private Spinner devicesSpinner;
    private RadioButton btnBluetooth;
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private static List<BluetoothDevice> devices = new ArrayList<>();
    private String transporterId = "";
    private String routeId = "";



    public ExportDataFrag() {
        // Required empty public constructor
    }

    public static ExportDataFrag newInstance() {
        ExportDataFrag fragment = new ExportDataFrag();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            transporterId = getArguments().getString("transporterId");
            routeId = getArguments().getString("routeId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_export_data, container, false);
        createCSVFile = (Button) view.findViewById(R.id.prep_export_data);
        sendCSVFile = (Button) view.findViewById(R.id.start_export);
        ipAddress = (EditText) view.findViewById(R.id.ipAddress);
        exportMethod = (RadioGroup) view.findViewById(R.id.export_method);
        btnWifi = (RadioButton) view.findViewById(R.id.wifiTransfer);
        btnBluetooth = (RadioButton) view.findViewById(R.id.bluetoothTransfer);
        lblExportMethod = (TextView) view.findViewById(R.id.lblExport);
        lblExportInstructions = (TextView) view.findViewById(R.id.lblInstructions);
        lblExportInstructionsTitle = (TextView) view.findViewById(R.id.lblExportInstructionsTitle);
        devicesSpinner = (Spinner) view.findViewById(R.id.devices_spinner);

        // Set title bar
        ((Main) getActivity()).setActionBarTitle("Export Data");

        prepCSVFiles();
        sendCSVFiles();
        chooseExportMethod();

        return view;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onExportFragmentInteraction(uri);
        }
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

    public void prepCSVFiles() {
        createCSVFile.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        File file = new File(DatabaseConstants.tbltrFarmerTransporter);
                        try {
                            exportSucceeded = USBTransfer.exportTable(DatabaseConstants.tbltrFarmerTransporter, v.getContext().getApplicationContext());
                        } catch (Exception e) {
                            exportSucceeded = null;
                        }
                        if (exportSucceeded != null) {
                            Toast.makeText(v.getContext(), "Successfully exported data", Toast.LENGTH_LONG).show();
                            lblExportMethod.setVisibility(View.VISIBLE);
                            exportMethod.setVisibility(View.VISIBLE);
                            exportMethod.setEnabled(true);

                            ArrayList<String> toBeScanned = new ArrayList<String>();
                            toBeScanned.add(exportSucceeded);

                            String[] toBeScannedStr = new String[toBeScanned.size()];
                            toBeScannedStr = toBeScanned.toArray(toBeScannedStr);

                            MediaScannerConnection.scanFile(getActivity(), toBeScannedStr, null, new MediaScannerConnection.OnScanCompletedListener() {

                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    System.out.println("SCAN COMPLETED: " + path);

                                }
                            });

                            // Update status of records internally
                            List<String> whereOptions = Arrays.asList(DatabaseConstants.status, DatabaseConstants.route_id);
                            List<String> whereParams = Arrays.asList("=", "=");
                            List<String> whereValues = Arrays.asList(DatabaseConstants.status_pending, routeId);
                            List<MilkRecord> records = commonUtil.convertCursorToMilkRecordList(dbUtil.selectStatement(DatabaseConstants.tbltrFarmerTransporter, "*", whereOptions, whereParams, whereValues, getContext() ));

                            for (MilkRecord record : records) {
                                List<String> col = new ArrayList<>();
                                col.addAll(Arrays.asList(DatabaseConstants.colHistTrFarmerTransporter));
                                List<String> vals = new ArrayList<>();

                                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = new Date();
                                String todayDate= dateFormat.format(date);

                                dateFormat = new SimpleDateFormat("HH:mm:ss");
                                String todayTime = dateFormat.format(date);

                                vals.add(record.getTransporterId());
                                vals.add(record.getFarmerId());
                                vals.add(record.getJugId());
                                vals.add(todayDate);
                                vals.add(todayTime);
                                vals.add(record.getMilkWeight());
                                vals.add(record.getAlcohol());
                                vals.add(record.getSmell());
                                vals.add(record.getComments());
                                vals.add(record.getDensity());
                                vals.add(routeId);
                                vals.add(DatabaseConstants.status_synced);
                                vals.add(record.getId());

                                col.remove(Arrays.asList(DatabaseConstants.colHistTrFarmerTransporter).indexOf(DatabaseConstants.tr_transporter_cooling_id));
                                col.remove(0);

                                dbUtil.insertStatement(DatabaseConstants.tblHisttrFarmerTransporter, col, vals, getContext());
                            }

                            dbUtil.updateStatement(DatabaseConstants.tbltrFarmerTransporter, DatabaseConstants.status, DatabaseConstants.status_synced, DatabaseConstants.status, "=", DatabaseConstants.status_pending, getContext());

                            dbUtil.updateStatement(DatabaseConstants.tblJug, DatabaseConstants.currentVolume, "0", DatabaseConstants.transporter_id, "=", transporterId, getContext());
                        }


                    }
                }
        );
    }

    public void sendCSVFiles() {
        sendCSVFile.setOnClickListener(
                new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View v) {
                        if (btnWifi.isChecked()) {
                            final String ipString = ipAddress.getText().toString();
                            if (ipString.isEmpty()) {
                                new AlertDialog.Builder(getContext())
                                        .setTitle("Incorrect data")
                                        .setMessage("Please enter a valid ip address.")
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton(android.R.string.ok, incorrectData).show();
                            } else {
                                AsyncTask.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        Socket sock;
                                        File myFile = new File(exportSucceeded);
                                        try {
                                            sock = new Socket(ipString, 1149);
                                            System.out.println("Connecting...");

                                            // sendfile

                                            byte[] mybytearray = new byte[(int) myFile.length()];
                                            FileInputStream fis = new FileInputStream(myFile);
                                            BufferedInputStream bis = new BufferedInputStream(fis);
                                            bis.read(mybytearray, 0, mybytearray.length);
                                            OutputStream os = sock.getOutputStream();
                                            System.out.println("Sending...");
                                            os.write(mybytearray, 0, mybytearray.length);
                                            os.flush();

                                            sock.close();

                                        } catch (UnknownHostException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }

                                    }
                                });

                                InputMethodManager inputManager = (InputMethodManager)
                                        Main.instance.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                                inputManager.hideSoftInputFromWindow((null == Main.instance.getCurrentFocus()) ? null : Main.instance.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                                Toast.makeText( Main.instance.getApplicationContext(), "Successfully sent exported data", Toast.LENGTH_LONG).show();

                            }
                        } else if (btnBluetooth.isChecked()) {
                            BluetoothTransfer.SendMessageToServer btransfer = new BluetoothTransfer.SendMessageToServer();

                            String deviceHardwareAddress = devicesSpinner.getSelectedItem().toString();
                            String temp = deviceHardwareAddress;
                            deviceHardwareAddress = deviceHardwareAddress.substring(temp.indexOf('/') + 1);


                            btransfer.execute(deviceHardwareAddress);
                        }

                    }
                }
        );
    }

    public void chooseExportMethod() {
        exportMethod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                lblExportInstructions.setVisibility(View.VISIBLE);
                lblExportInstructionsTitle.setVisibility(View.VISIBLE);
                if (checkedId == R.id.usbTransfer) {
                    lblExportInstructions.setText(getString(R.string.usbTransfer));
                    ipAddress.setVisibility(View.INVISIBLE);
                    devicesSpinner.setVisibility(View.INVISIBLE);
                    ipAddress.setEnabled(false);
                    sendCSVFile.setVisibility(View.INVISIBLE);
                    sendCSVFile.setEnabled(false);

                } else if (checkedId == R.id.bluetoothTransfer) {
                    lblExportInstructions.setText(getString(R.string.bluetoothTransfer));
                    devicesSpinner.setVisibility(View.VISIBLE);
                    ipAddress.setVisibility(View.INVISIBLE);
                    ipAddress.setEnabled(false);
                    sendCSVFile.setVisibility(View.VISIBLE);
                    sendCSVFile.setEnabled(true);

                    if (mBluetoothAdapter != null) {
                        if (!mBluetoothAdapter.isEnabled()) {
                            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                            alertBuilder.setCancelable(true);
                            alertBuilder.setMessage("Do you want to enable bluetooth?");
                            alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    mBluetoothAdapter.enable();

                                    Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

                                    List<String> s = new ArrayList<String>();
                                    for (BluetoothDevice bt : pairedDevices)
                                        s.add(bt.getName());
                                }
                            });
                            alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog alert = alertBuilder.create();
                            alert.show();
                        }

                        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
                        List<String> devices = new ArrayList<>();
                        if (pairedDevices.size() > 0) {
                            // There are paired devices. Get the name and address of each paired device.
                            for (BluetoothDevice device : pairedDevices) {
                                devices.add(device.getName() + "/" + device.getAddress());
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, devices);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            devicesSpinner.setAdapter(adapter);
                        }
                    } else {
                            Toast.makeText( Main.instance.getApplicationContext(), "Bluetooth is not supported on this device", Toast.LENGTH_LONG).show();
                    }

                } else if (checkedId == R.id.wifiTransfer) {
                    lblExportInstructions.setText(getString(R.string.wifiTransfer));
                    ipAddress.setVisibility(View.VISIBLE);
                    ipAddress.setEnabled(true);
                    sendCSVFile.setVisibility(View.VISIBLE);
                    sendCSVFile.setEnabled(true);
                    devicesSpinner.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void onListFragmentInteraction(DeviceItem item) {

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
        void onExportFragmentInteraction(Uri uri);
    }

        public DialogInterface.OnClickListener incorrectData = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        break;
                }
            }
        };


}
