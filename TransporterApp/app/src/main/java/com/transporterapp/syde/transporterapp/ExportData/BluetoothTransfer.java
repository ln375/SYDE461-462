package com.transporterapp.syde.transporterapp.ExportData;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.transporterapp.syde.transporterapp.commonUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.content.ContentValues.TAG;

/**
 * Created by chari on 2018-02-13.
 */

public class BluetoothTransfer {
    private static final String UUID_STRING = "04c6093b-0000-1000-8000-00805f9b34fb"; // 32 hex digits
    public static String rcv_msg = "";

    public static class SendMessageToServer extends AsyncTask<String, Void, String> {

        private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        private BluetoothSocket clientSocket = null;

        @Override
        protected String doInBackground(String... msg) {
            Log.d(TAG, "doInBackground");
            mBluetoothAdapter.enable();

            byte[] tosend= commonUtil.convertFileToBytes(Environment.getExternalStorageDirectory() + "/trFarmerTransporter/tr_farmer_transporter.csv");

            // Client knows the server MAC address
            String temp = msg[0];
            BluetoothDevice mmDevice = mBluetoothAdapter.getRemoteDevice(temp);
            Log.d(TAG, "got hold of remote device");
            try {
                // UUID string same used by server
                clientSocket = mmDevice.createInsecureRfcommSocketToServiceRecord(UUID
                        .fromString(UUID_STRING));

                Log.d(TAG, "bluetooth socket created");

                mBluetoothAdapter.cancelDiscovery(); 	// Cancel, discovery slows connection

                clientSocket.connect();
                Log.d(TAG, "connected to server");

                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

                out.write(tosend); 			// Send message to server
                Log.d(TAG, "Message Successfully sent to server");
                return in.readUTF();            // Read response from server
            } catch (Exception e) {

                Log.d(TAG, "Error creating bluetooth socket");
                Log.d(TAG, e.getMessage());

                try {
                    clientSocket.close();
                } catch (IOException e2) {
                    Log.e(TAG, "unable to close() " + clientSocket.toString() +
                            " socket during connection failure", e2);
                }

                return "";
            }

        }

        @Override
        protected void onPostExecute(String result) {
            Log.d(TAG, "onPostExecute");
            rcv_msg = result;
        }

        public static final BroadcastReceiver bReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // Create a new device item

                }
            }
        };

        public void startDiscovery(){
            mBluetoothAdapter.startDiscovery();
        }

        public void stopDiscovery(){
            mBluetoothAdapter.cancelDiscovery();
        }
    }
    }
