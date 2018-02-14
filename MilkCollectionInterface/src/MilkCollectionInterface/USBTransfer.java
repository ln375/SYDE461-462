package MilkCollectionInterface;

import java.io.File;
import java.math.BigInteger;
import be.derycke.pieter.com.COMException;
import jmtp.*;

/**
 * Created by chari on 2018-01-29.
 */

public class USBTransfer {
    public static void jMTPeMethode()
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
                        String temp = device.getFriendlyName() + "/" + storage.getName() + "/" +  o2.getName();
                        String temp2 = o2.getName();
                    }

                    System.out.println(o2.getOriginalFileName());
                }

                //copyFileFromComputerToDeviceFolder(targetFolder);
                PortableDeviceObject[] folderFiles = targetFolder.getChildObjects();
                for (PortableDeviceObject pDO : folderFiles) {
                    copyFileFromDeviceToComputerFolder(pDO, device);
                }

            }
        }

        manager.getDevices()[0].close();
    }

    private static void copyFileFromDeviceToComputerFolder(PortableDeviceObject pDO, PortableDevice device)
    {
        try {

            new PortableDeviceToHostImpl32().copyFromPortableDeviceToHost(pDO.getID(), "C:\\Desktop\test", device);
        } catch (COMException ex) {
            ex.printStackTrace();
        }

    }

    private static void copyFileFromComputerToDeviceFolder(PortableDeviceFolderObject targetFolder)
    {
        BigInteger bigInteger1 = new BigInteger("123456789");
        File file = new File("C:\\GettingJMTP.pdf");
        try {
            targetFolder.addAudioObject(file, "jj", "jj", bigInteger1);
        } catch (Exception e) {
            System.out.println("Exception e = " + e);
        }
    }
}