package com.transporterapp.syde.transporterapp.ExportData;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.widget.TextView;

import com.transporterapp.syde.transporterapp.Main;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by chari on 2018-02-08.
 */

public class WifiConnection {
    public static String   s_dns1 ;
    public static String   s_dns2;
    public static String   s_gateway;
    public static String   s_ipAddress;
    public static String   s_leaseDuration;
    public static String   s_netmask;
    public static String   s_serverAddress;
    public static TextView info;
    public static DhcpInfo d;
    public static WifiManager wifii;


    public static String getConnections() {
        wifii = (WifiManager) Main.instance.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        d = wifii.getDhcpInfo();

        s_dns1 = "DNS 1: " + String.valueOf(d.dns1);
        s_dns2 = "DNS 2: " + String.valueOf(d.dns2);
        s_gateway = "Default Gateway: " + String.valueOf(d.gateway);
        s_ipAddress = "IP Address: " + String.valueOf(d.ipAddress);
        s_leaseDuration = "Lease Time: " + String.valueOf(d.leaseDuration);
        s_netmask = "Subnet Mask: " + String.valueOf(d.netmask);
        s_serverAddress = "Server IP: " + String.valueOf(d.serverAddress);


        String connections = "";
        InetAddress host;
        try
        {
            host = InetAddress.getByName(intToIp(d.dns1));
            byte[] ip = host.getAddress();

            for(int i = 1; i <= 254; i++)
            {
                ip[3] = (byte) i;
                InetAddress address = InetAddress.getByAddress(ip);
                if(address.isReachable(100))
                {
                    System.out.println(address + " machine is turned on and can be pinged");
                    connections+= address+"\n";
                }
                else if(!address.getHostAddress().equals(address.getHostName()))
                {
                    System.out.println(address + " machine is known in a DNS lookup");
                }

            }
        }
        catch(UnknownHostException e1)
        {
            e1.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        System.out.println(connections);
        return connections;
    }

    public static String intToIp(int i) {
        return (i & 0xFF) + "." +
                ((i >> 8 ) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                ((i >> 24) & 0xFF);
    }

}
