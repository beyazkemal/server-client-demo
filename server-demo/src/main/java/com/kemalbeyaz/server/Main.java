package com.kemalbeyaz.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final List<MessageListener> messageListeners = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Hello from the server side!");

        var threadPoolExecutor = new ThreadPoolExecutor(20, 20, 1, TimeUnit.SECONDS, new LinkedBlockingDeque());

        try (var serverSocket = new ServerSocket(9090, 1)) {

            while (true) {
                System.out.println("-- Waiting for new connection.");
                var socket = serverSocket.accept();
                System.out.println("++ New connection accepted.");

                MessageListener messageListener = new MessageListener(socket, messageListeners);
                messageListeners.add(messageListener);
                threadPoolExecutor.submit(messageListener);
                countAliveConnection();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void countAliveConnection() {
        messageListeners.removeIf(messageListener -> !messageListener.isAlive());
        System.out.println("Active client count: " + messageListeners.size());
    }
}
