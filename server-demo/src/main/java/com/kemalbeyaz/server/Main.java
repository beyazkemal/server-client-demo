package com.kemalbeyaz.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {

    private static final List<MessageListener> messageListeners = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Hello from the server side!");

        var executorService = Executors.newFixedThreadPool(10);

        try (var serverSocket = new ServerSocket(9090, 1)) {

            while (true) {
                System.out.println("-- Waiting for new connection.");
                var socket = serverSocket.accept();
                System.out.println("++ New connection accepted.");

                MessageListener messageListener = new MessageListener(socket, messageListeners);
                messageListeners.add(messageListener);
                executorService.submit(messageListener);
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
