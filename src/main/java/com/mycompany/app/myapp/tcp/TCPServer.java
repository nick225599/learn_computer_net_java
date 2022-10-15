package com.mycompany.app.myapp.tcp;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.mycompany.app.myapp.CommonUtils.printLines;

public class TCPServer {
    public static final int SERVER_PORT = 12000;

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(SERVER_PORT));
        Socket connectionSocket = null;
        ExecutorService executorService = Executors.newCachedThreadPool();
        while (true) {
            connectionSocket = serverSocket.accept();

            Socket finalConnectionSocket = connectionSocket;
            executorService.execute(() -> {
                try {
                    // request
                    InputStream inputStream = finalConnectionSocket.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    List<String> request = IOUtils.readLines(bufferedReader);
                    System.out.println("server receive request from client: ");
                    printLines(request);

                    // response
                    String response = request.get(0).toUpperCase(Locale.ROOT);
                    OutputStream outputStream = finalConnectionSocket.getOutputStream();
                    outputStream.write(response.getBytes(StandardCharsets.UTF_8));
                    outputStream.flush();
                    System.out.println("server send response to client: " + response);

                    IOUtils.closeQuietly(finalConnectionSocket);
                } catch (Exception e) {
                    e.printStackTrace();

                    IOUtils.closeQuietly(finalConnectionSocket);
                }
            });


        }
    }
}
