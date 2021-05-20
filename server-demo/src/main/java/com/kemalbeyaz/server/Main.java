package com.kemalbeyaz.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {

    private static final List<Client> CLIENTS = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Hello from the server side!");

        var executorService = Executors.newFixedThreadPool(10);

        try (var serverSocket = new ServerSocket(9090, 1)) {

            while (true) {
                System.out.println("-- Waiting for new connection.");
                var socket = serverSocket.accept();
                System.out.println("++ New connection accepted.");

                Client client = new Client(socket, CLIENTS);
                CLIENTS.add(client);
                executorService.submit(client);
                countAliveConnection();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void countAliveConnection() {
        System.out.println("Active client count: " + CLIENTS.size());
    }
}
