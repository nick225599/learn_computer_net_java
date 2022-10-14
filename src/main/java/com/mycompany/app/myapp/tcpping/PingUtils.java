package com.mycompany.app.myapp.tcpping;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;

public class PingUtils {

    public static final int PING_SERVER_PORT = 12001;
    /*
     * Print ping data to the standard output stream.
     */
    public static void printData(DatagramPacket request) throws Exception {
        // Obtain references to the packet's array of bytes.
        byte[] buf = request.getData();

        // Wrap the bytes in a byte array input stream,
        // so that you can read the data as a stream of bytes.
        ByteArrayInputStream bais = new ByteArrayInputStream(buf);

        // Wrap the byte array output stream in an input stream reader,
        // so you can read the data as a stream of characters.
        InputStreamReader isr = new InputStreamReader(bais);

        // Wrap the input stream reader in a bufferred reader,
        // so you can read the character data a line at a time.
        // (A line is a sequence of chars terminated by any combination of \r and \n.)
        BufferedReader br = new BufferedReader(isr);

        // The message data is contained in a single line, so read this line.
        String line = br.readLine();

        // Print host address and data received from it.
        System.out.println("Received from " + request.getAddress().getHostAddress() + ": " + line);
    }


}
