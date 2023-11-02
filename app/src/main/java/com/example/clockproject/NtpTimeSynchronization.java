package com.example.clockproject;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import java.io.IOException;
import java.net.InetAddress;

public class NtpTimeSynchronization {
    // Method to fetch NTP time from a specified NTP server
    public static long getNtpTime(String ntpServer) throws IOException {
        // Create an NTPUDPClient to communicate with NTP servers
        NTPUDPClient client = new NTPUDPClient();

        // Set a reasonable timeout for the NTP request (5 seconds)
        client.setDefaultTimeout(5000); // Set a reasonable timeout

        // Resolve the IP address of the NTP server using its hostname
        InetAddress hostAddr = InetAddress.getByName(ntpServer);

        // Get the time information from the NTP server
        TimeInfo timeInfo = client.getTime(hostAddr);

        // Close the NTP client to release resources
        client.close();

        // Extract and return the transmit timestamp from the NTP response
        return timeInfo.getMessage().getTransmitTimeStamp().getTime();
    }
}
