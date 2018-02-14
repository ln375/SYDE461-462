package MilkCollectionInterface;

import javax.bluetooth.RemoteDevice;
import javax.microedition.io.StreamConnection;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BluetoothThread extends Thread {

    private StreamConnection conn;
    private String clientName;
    private InputStream in;
    private OutputStreamWriter out;

    // globals
    private volatile boolean isRunning = false;

    public BluetoothThread(StreamConnection conn) {
        this.conn = conn;
        // store the name of the connected client
        clientName = reportDeviceName(conn);
        System.out.println("Handler spawned for client: " + clientName);
    } // end of ThreadedEchoHandler()

    private String reportDeviceName(StreamConnection conn) {
        // return the friendly name of the device being connected
        String devName;
        try {
            RemoteDevice rd = RemoteDevice.getRemoteDevice(conn);
            devName = rd.getFriendlyName(false);
        } catch(IOException e) {
            devName = "device ??";
        }
        return devName;
    }

    @Override
    public void run() {
        try {
            in = conn.openInputStream();
            File outputFile = new File("C:\\Users\\chari\\Desktop\\test\\test.csv");
            if (!outputFile.exists()) {
                outputFile.createNewFile();
            }
            byte[] buffer = new byte[8192];
            DataInputStream mmInStream = new DataInputStream(in);

            int count;

            FileOutputStream fop = new FileOutputStream(outputFile);
            count = mmInStream.read(buffer);
            fop.write(buffer, 0, count);

            fop.flush();
            fop.close();


            System.out.println(" Closing " + clientName + " connection");
            if(conn != null) {
                in.close();
                conn.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    } // end of run()

    public void closeDown() {
        isRunning = false;
    }
}
