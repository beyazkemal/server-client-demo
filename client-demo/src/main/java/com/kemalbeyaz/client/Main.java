package com.kemalbeyaz.client;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello from the client side!");

        var threadPoolExecutor =
                new ThreadPoolExecutor(2, 2, 1, TimeUnit.SECONDS, new LinkedBlockingDeque());

        try (var socket = new Socket("127.0.0.1", 9090)) {
            System.out.println("Connected!");

            var writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()), 10);
            var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()), 10);

            var exitFlag = new AtomicBoolean(false);

            threadPoolExecutor.submit(new MessageSender(writer, exitFlag));

            var messageReceiver = new MessageReceiver(reader, exitFlag);
            threadPoolExecutor.submit(messageReceiver);

            while (!exitFlag.get()) {
            }

            threadPoolExecutor.remove(messageReceiver);
            reader.close();

            threadPoolExecutor.shutdown();
            while (!threadPoolExecutor.isShutdown()) {
            }

            System.out.println("Exiting...");
        }
    }
}
