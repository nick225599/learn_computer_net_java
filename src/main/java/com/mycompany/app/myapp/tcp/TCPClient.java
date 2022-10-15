package com.mycompany.app.myapp.tcp;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.mycompany.app.myapp.CommonUtils.printLines;
import static com.mycompany.app.myapp.tcp.TCPServer.SERVER_PORT;

public class TCPClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", SERVER_PORT);
        String message = "Hello, world! \r\n ---- from BBC News";

        // request
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(message.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        socket.shutdownOutput(); //TODO scs 必须关流才能写入内容，怎么不关能重复写呢？
        
        System.out.println("client send request to server:");
        System.out.println(message);
        System.out.println();

        // response
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        List<String> response = IOUtils.readLines(bufferedReader);
        System.out.println("client receive response from server: ");
        printLines(response);

        IOUtils.closeQuietly(socket);
    }

}
