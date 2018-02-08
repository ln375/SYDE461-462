package MilkCollectionInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MilkCollectionService {
    private JButton btnFromPhone;
    private JPanel panelMain;

    public MilkCollectionService() {
        btnFromPhone.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
    }

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("App");
        frame.setContentPane(new MilkCollectionService().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        int filesize=6022386; // filesize temporary hardcoded

        long start = System.currentTimeMillis();
        int bytesRead;
        int current = 0;

        // create socket
        ServerSocket servsock = new ServerSocket(1149);
        while (true) {
            System.out.println("Waiting...");

            Socket sock = servsock.accept();
            System.out.println("Accepted connection : " + sock);

            // receive file
            byte [] mybytearray  = new byte [filesize];
            InputStream is = sock.getInputStream();
            FileOutputStream fos = new FileOutputStream("C:\\Users\\chari\\Desktop\\tr_farmer_transporter.csv"); // destination path and name of file
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bytesRead = is.read(mybytearray,0,mybytearray.length);
            current = bytesRead;

            // thanks to A. Cádiz for the bug fix
            do {
                bytesRead =
                        is.read(mybytearray, current, (mybytearray.length-current));
                if(bytesRead >= 0) current += bytesRead;
            } while(bytesRead > -1);

            bos.write(mybytearray, 0 , current);
            bos.flush();
            long end = System.currentTimeMillis();
            System.out.println(end-start);
            bos.close();



            sock.close();
        }
    }
}
