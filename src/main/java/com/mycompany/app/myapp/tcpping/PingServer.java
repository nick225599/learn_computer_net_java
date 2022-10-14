package com.mycompany.app.myapp.tcpping;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

import static com.mycompany.app.myapp.tcpping.PingUtils.PING_SERVER_PORT;
import static com.mycompany.app.myapp.tcpping.PingUtils.printData;

public class PingServer {
    private static final double LOSS_RATE = 0.3;
    private static final int AVERAGE_DELAY = 100;  // milliseconds

    public static void main(String[] args) throws Exception {
        // Get command line argument.
        int port;
        if (args.length != 1) {
            port = PING_SERVER_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }
        // Create random number generator for use in simulating
        // packet loss and network delay.
        Random random = new Random();

        // Create a datagram socket for receiving and sending UDP packets
        // through the port specified on the command line.
        DatagramSocket socket = new DatagramSocket(port);

        // Processing loop.
        while (true) {
            // Create a datagram packet to hold incomming UDP packet.
            DatagramPacket request = new DatagramPacket(new byte[1024], 1024);

            // Block until the host receives a UDP packet.
            socket.receive(request);
            System.out.println("get something... ");

            // Print the recieved data.
            printData(request);

            // Decide whether to reply, or simulate packet loss.
            if (random.nextDouble() < LOSS_RATE) {
                System.out.println("   Reply not sent.");
                continue;
            }

            // Simulate network delay.
            Thread.sleep((int) (random.nextDouble() * 2 * AVERAGE_DELAY));

            // Send reply.
            InetAddress clientHost = request.getAddress();
            int clientPort = request.getPort();
            byte[] buf = request.getData();
            DatagramPacket reply = new DatagramPacket(buf, buf.length, clientHost, clientPort);
            socket.send(reply);

            System.out.println("   Reply sent.");
        }
    }


}