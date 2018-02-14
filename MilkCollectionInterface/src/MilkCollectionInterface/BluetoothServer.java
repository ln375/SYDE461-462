package MilkCollectionInterface;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import java.io.IOException;
import java.util.ArrayList;

public class BluetoothServer {
    private static final String UUID_STRING = "04c6093b00001000800000805f9b34fb"; // 32 hex digits
    private static final String SERVICE_NAME = "bluetoothserver";

    private StreamConnectionNotifier server;

    // when program is terminated, shutdown all the client threads
    public BluetoothServer() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                closeDown();
            }
        });

        initDevice();
        createRFCOMMConnection();
        processClients();
    }

    private void initDevice() {
        try { // make the server's bluetooth device discoverable
            LocalDevice local = LocalDevice.getLocalDevice();

            System.out.println("Device name: " + local.getFriendlyName());
            System.out.println("Bluetooth Address: " + local.getBluetoothAddress());

            boolean res = local.setDiscoverable(DiscoveryAgent.GIAC);

            System.out.println("Discoverability set: " + res);
        } catch(BluetoothStateException e) {
            System.out.println(e);
            System.exit(1);
        }
    } // end of initDevice()



    private void createRFCOMMConnection() {
        try {
            System.out.println("Start advertising " + SERVICE_NAME + "...");
            UUID uuid = new UUID(80087355);
            server = (StreamConnectionNotifier) Connector.open(
                    "btspp://localhost:" + UUID_STRING +
                            ";name=" + SERVICE_NAME + ";authenticate=false");
        } catch(IOException e) {
            System.out.println(e);
            System.exit(1);
        }
    } // end of createRFCOMMConnection()


    // globals
    private ArrayList<BluetoothThread> handlers = new ArrayList<>();
    private volatile boolean isRunning = false;

    private void processClients() {
        isRunning = true;
        try {
            while(isRunning) {
                System.out.println("Waiting for incoming connection...");
                StreamConnection conn = server.acceptAndOpen();
                // wait for a client connection
                BluetoothThread handler = new BluetoothThread(conn);
                handlers.add(handler);
                handler.start();
            }
        } catch(IOException e) {
            System.err.println(e);
        }
    } // end of processClients


    private void closeDown() {
        System.out.println("closing down server");
        if(isRunning) {
            isRunning = false;
            try {
                server.close();
            } catch(IOException e) {
                System.err.println(e);
            }

            // close all the handlers
            for(BluetoothThread hand: handlers) {
                hand.closeDown();
            }
            handlers.clear();
        }
    } // end of closeDown()
}
