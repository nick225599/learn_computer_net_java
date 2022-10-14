package com.mycompany.app.myapp.tcpproxy;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * ProxyCache.java - Simple caching proxy
 * <p>
 * $Id: ProxyCache.java,v 1.3 2004/02/16 15:22:00 kangasha Exp $
 */
public class ProxyCache {
    /**
     * Port for the proxy
     */
    private static int port;
    /**
     * Socket for client connections
     */
    private static ServerSocket socket;


    public static void handle(Socket socketFromProxyClient) {
        try {
            Socket socketToRemoteWebServer = null;
            DataOutputStream toServer;

            HttpRequest httpRequest = HttpRequest.getHttpRequestFromSocketInputStream(
                    socketFromProxyClient.getInputStream());

            System.out.println("http request: ");
            System.out.println(httpRequest);
            System.out.println();



            /* Open socket and write request to socket */
            socketToRemoteWebServer = new Socket(httpRequest.getHost(), httpRequest.getPort());
            toServer = new DataOutputStream(socketToRemoteWebServer.getOutputStream());
            toServer.write(httpRequest.toString().getBytes(StandardCharsets.UTF_8));

            HttpResponse httpResponse = HttpResponse.getHttpRequestFromSocketInputStream(
                    socketToRemoteWebServer.getInputStream());

            System.out.println("http response: ");
            System.out.println(httpResponse);
            System.out.println();


            DataOutputStream toClient = new DataOutputStream(socketFromProxyClient.getOutputStream());
            toClient.writeUTF(httpResponse.toString());
            /* Write response to client. First headers, then body */
            socketFromProxyClient.close();
            socketToRemoteWebServer.close();
            /* Insert object into the cache */
            /* Fill in (optional exercise only) */
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    /**
     * Read command line arguments and start proxy
     */
    public static void main(String[] args) throws IOException {
        ServerSocket proxyServerSocket = new ServerSocket(12001);
        while (true) {
            Socket client = proxyServerSocket.accept();
            new Thread(() -> handle(client)).start();
        }
    }
}
