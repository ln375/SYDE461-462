package MilkCollectionInterface;

import javafx.scene.control.Alert;
import javafx.stage.DirectoryChooser;

import javax.bluetooth.RemoteDevice;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.util.Arrays;

public class MilkCollectionService {
    private JPanel panelMain;
    private JButton btnUSBTransfer;
    private JButton btnBlueTooth;
    private JButton btnWifi;
    private JTextPane txtMenuScreen;
    private JComboBox deviceList;
    private JButton btnStart;
    private JTextField txtFilePath;
    private JLabel lblDevice1;
    private JLabel lblWifi;
    private JLabel lblIPAddress;
    private JLabel lblSave;
    private JLabel lblDevice2;
    private JComboBox deviceList2;
    private JButton btnFileDialog;
    private int method = -1;
    public static String filePath = "";

    public MilkCollectionService() {

        btnFileDialog.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                //fc.showOpenDialog(null);
                fc.showDialog(null, "Select");

                filePath = fc.getSelectedFile().getPath();
                txtFilePath.setText(filePath);
                txtFilePath.setBorder(BorderFactory.createLineBorder(Color.black, 1));
                btnStart.setEnabled(true);
            }
        });

        btnUSBTransfer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deviceList.setEnabled(false);
                method = 1;
                btnStart.setEnabled(false);
                deviceList.setSelectedIndex(-1);
                txtFilePath.setEnabled(true);
                lblSave.setEnabled(true);
                txtFilePath.setText("");
                lblWifi.setEnabled(false);
                lblIPAddress.setEnabled(false);
                lblIPAddress.setText("");
                lblDevice1.setEnabled(false);
                deviceList.setEnabled(false);
                btnFileDialog.setEnabled(true);
            }
        });

        btnBlueTooth.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                txtFilePath.setEnabled(false);
                txtFilePath.setText("");
                txtFilePath.setEnabled(true);
                lblSave.setEnabled(true);
                deviceList.setEnabled(true);
                lblDevice1.setEnabled(true);
                //lblDevice2.setEnabled(false);
                //deviceList2.setSelectedIndex(-1);
                //deviceList2.setEnabled(false);
                btnStart.setEnabled(false);
                lblWifi.setEnabled(false);
                lblIPAddress.setEnabled(false);
                lblIPAddress.setText("");
                btnFileDialog.setEnabled(true);
                method = 2;
                try {
                    RemoteDevice[] devices = BluetoothServer.findPreKnownDevices();
                    for (RemoteDevice dev : devices) {
                        if (((DefaultComboBoxModel) deviceList.getModel()).getIndexOf(dev.getFriendlyName(false)) == -1) {
                            deviceList.addItem(dev.getFriendlyName(false));
                        }
                    }
                }catch (IOException error) {

                }
                }
        });

        btnWifi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtFilePath.setText("");
                txtFilePath.setEnabled(false);
                txtFilePath.setEnabled(true);
                lblSave.setEnabled(true);
                lblDevice1.setEnabled(false);
                btnStart.setEnabled(false);
                lblWifi.setEnabled(true);
                lblIPAddress.setEnabled(true);
                lblDevice2.setEnabled(false);
                deviceList2.setSelectedIndex(-1);
                deviceList2.setEnabled(false);
                lblDevice1.setEnabled(false);
                deviceList.setSelectedIndex(-1);
                btnFileDialog.setEnabled(true);
                deviceList.setEnabled(false);
                try{
                    lblIPAddress.setText(InetAddress.getLocalHost().toString());
                } catch (Exception error) {

                }

                method = 3;

            }
        });

        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (txtFilePath.getText().trim().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Please select a folder to export to.");
                    txtFilePath.setBorder(BorderFactory.createLineBorder(Color.red, 3));
                } else {
                    switch (method) {
                        case 1: // USB transfer
                            USBTransfer.jMTPeMethode(filePath);
                            break;
                        case 2: // Bluetooth transfer
                            BluetoothServer myBluetoothServer = new BluetoothServer();
                            break;
                        case 3: // IP address
                            wifiTransfer.transferFile(txtFilePath.getText().toString().trim());
                            break;
                    }
                }

            }
        });
    }

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("App");
        frame.setContentPane(new MilkCollectionService().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
