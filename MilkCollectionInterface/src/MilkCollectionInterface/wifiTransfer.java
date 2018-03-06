package MilkCollectionInterface;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;

public class wifiTransfer {
    public static void transferFile(String filePath)
    {
        int filesize=6022386; // filesize temporary hardcoded

        long start = System.currentTimeMillis();
        int bytesRead;
        int current = 0;
        try {
            // create socket
            ServerSocket servsock = new ServerSocket(1149);
            while (true) {
                System.out.println("Waiting...");

                Socket sock = servsock.accept();
                System.out.println("Accepted connection : " + sock);

                // receive file
                byte[] mybytearray = new byte[filesize];
                InputStream is = sock.getInputStream();
                String fileName = filePath + "\\" + LocalDateTime.now().getYear() + LocalDateTime.now().getMonthValue() + LocalDateTime.now().getDayOfMonth() + "_trFarmerTransporter.csv";
                FileOutputStream fos = new FileOutputStream(fileName); // destination path and name of file
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                bytesRead = is.read(mybytearray, 0, mybytearray.length);
                current = bytesRead;

                // thanks to A. Cádiz for the bug fix
                do {
                    bytesRead =
                            is.read(mybytearray, current, (mybytearray.length - current));
                    if (bytesRead >= 0) current += bytesRead;
                } while (bytesRead > -1);

                bos.write(mybytearray, 0, current);
                bos.flush();
                long end = System.currentTimeMillis();
                System.out.println(end - start);
                bos.close();


                sock.close();
                break;
            }

        } catch (IOException exception) {

        }
    }
}
