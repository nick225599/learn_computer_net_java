package com.mycompany.app.myapp.tcpping;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.StopWatch;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static com.mycompany.app.myapp.tcpping.PingUtils.PING_SERVER_PORT;
import static com.mycompany.app.myapp.tcpping.PingUtils.printData;

public class PingClient {
    static void sendPing(String message) {
        DatagramSocket socket = null;
        try {
            DatagramPacket response = new DatagramPacket(new byte[1024], 1024);

            socket = new DatagramSocket();

            DatagramPacket request = new DatagramPacket(
                    new byte[1024], 1024, new InetSocketAddress("127.0.0.1", PING_SERVER_PORT));
            request.setData(message.getBytes(StandardCharsets.UTF_8));
            socket.send(request);
            socket.receive(response);

            printData(response);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(socket);
        }
    }

    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newCachedThreadPool();
        StopWatch stopWatch = StopWatch.create();
        for (int i = 0; i < 10; i++) {
            System.out.println("start... " + i);
            int finalI = i;
            stopWatch.start();
            Future<?> future = executorService.submit(() -> {
                sendPing("hello, thread_" + finalI);
            });
            long secondsElapsed = 0;
            while (!future.isDone() && secondsElapsed < 1000) {
                secondsElapsed = stopWatch.getTime(TimeUnit.MILLISECONDS);
                Thread.yield();
            }
            if(secondsElapsed > 1000){
                System.out.println("超时");
            }
            stopWatch.reset();
        }
        executorService.shutdown();
    }
}
