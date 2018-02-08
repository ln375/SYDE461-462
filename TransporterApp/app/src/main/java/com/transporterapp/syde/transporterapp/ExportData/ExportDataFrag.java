package com.transporterapp.syde.transporterapp.ExportData;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.transporterapp.syde.transporterapp.R;
import com.transporterapp.syde.transporterapp.databases.DatabaseConstants;
import com.transporterapp.syde.transporterapp.databases.dbUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExportDataFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExportDataFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExportDataFrag extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Button createCSVFile;
    private String exportSucceeded = null;
    private EditText ipAddress;
    private Button sendCSVFile;
    private RadioGroup exportMethod;
    private TextView lblExportMethod;
    private TextView lblExportInstructionsTitle;
    private TextView lblExportInstructions;
    private String filePath = "";


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
        lblExportMethod = (TextView) view.findViewById(R.id.lblExport);
        lblExportInstructions = (TextView) view.findViewById(R.id.lblInstructions);
        lblExportInstructionsTitle = (TextView) view.findViewById(R.id.lblExportInstructionsTitle);

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
                            exportSucceeded = USBTransfer.exportTable(dbUtil.database, DatabaseConstants.tbltrFarmerTransporter, file, v.getContext().getApplicationContext());
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
                        }

                    }
                }
        );
    }

    public void sendCSVFiles() {
        sendCSVFile.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {
                                    Socket sock;
                                    File myFile = new File (exportSucceeded);
                                    try {
                                    sock = new Socket("192.168.2.18", 1149);
                                    System.out.println("Connecting...");

                                    // sendfile

                                    byte [] mybytearray  = new byte [(int)myFile.length()];
                                    FileInputStream fis = new FileInputStream(myFile);
                                    BufferedInputStream bis = new BufferedInputStream(fis);
                                    bis.read(mybytearray,0,mybytearray.length);
                                    OutputStream os = sock.getOutputStream();
                                    System.out.println("Sending...");
                                    os.write(mybytearray,0,mybytearray.length);
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
                    ipAddress.setEnabled(false);
                    sendCSVFile.setVisibility(View.INVISIBLE);
                    sendCSVFile.setEnabled(false);

                } else if (checkedId == R.id.bluetoothTransfer) {
                    lblExportInstructions.setText(getString(R.string.bluetoothTransfer));
                    ipAddress.setVisibility(View.INVISIBLE);
                    ipAddress.setEnabled(false);
                    sendCSVFile.setVisibility(View.INVISIBLE);
                    sendCSVFile.setEnabled(false);

                } else if (checkedId == R.id.wifiTransfer) {
                    lblExportInstructions.setText(getString(R.string.wifiTransfer));
                    ipAddress.setVisibility(View.VISIBLE);
                    ipAddress.setEnabled(true);
                    sendCSVFile.setVisibility(View.VISIBLE);
                    sendCSVFile.setEnabled(true);
                }
            }
        });
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
}
