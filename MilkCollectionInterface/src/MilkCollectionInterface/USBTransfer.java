package MilkCollectionInterface;

import java.io.File;
import java.math.BigInteger;
import java.util.Date;


import be.derycke.pieter.com.COMException;
import jmtp.*;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import javax.swing.filechooser.FileSystemView;

/**
 * Created by chari on 2018-01-29.
 */

public class USBTransfer {
    public static void jMTPeMethode(String filePath)
    {
        PortableDeviceFolderObject targetFolder = null;
        PortableDeviceManager manager = new PortableDeviceManager();
        PortableDevice device = manager.getDevices()[0];
        // Connect to USB tablet
        device.open();
        System.out.println(device.getModel());

        System.out.println("---------------");

        // Iterate over deviceObjects
        for (PortableDeviceObject object : device.getRootObjects())
        {
            // If the object is a storage object
            if (object instanceof PortableDeviceStorageObject)
            {
                PortableDeviceStorageObject storage = (PortableDeviceStorageObject) object;

                for (PortableDeviceObject o2 : storage.getChildObjects())
                {
                    if(o2.getOriginalFileName().equalsIgnoreCase("trFarmerTransporter"))
                    {
                        targetFolder = (PortableDeviceFolderObject) o2;

                    }

                    System.out.println(o2.getOriginalFileName());
                }

                PortableDeviceObject[] folderFiles = targetFolder.getChildObjects();
                for (PortableDeviceObject pDO : folderFiles) {
                    Date test = pDO.getDateAuthored();

                    copyFileFromDeviceToComputerFolder(pDO, device, filePath);

                }

            }
        }

        manager.getDevices()[0].close();
    }

    private static void copyFileFromDeviceToComputerFolder(PortableDeviceObject pDO, PortableDevice device, String filePath)
    {
        try {
            new PortableDeviceToHostImpl32().copyFromPortableDeviceToHost(pDO.getID(), filePath, device);
        } catch (COMException ex) {
            ex.printStackTrace();
        }

    }
/*
    private static void copyFileFromComputerToDeviceFolder(PortableDeviceFolderObject targetFolder)
    {
        BigInteger bigInteger1 = new BigInteger("123456789");
        File file = new File("C:\\GettingJMTP.pdf");
        try {
            targetFolder.addAudioObject(file, "jj", "jj", bigInteger1);
        } catch (Exception e) {
            System.out.println("Exception e = " + e);
        }
    }*/

}