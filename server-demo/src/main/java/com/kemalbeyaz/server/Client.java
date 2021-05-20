package com.kemalbeyaz.server;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Random;

public class Client implements Runnable {

    private final int id = new Random().nextInt();

    private final Socket socket;
    private final List<Client> clients;

    public Client(Socket socket, List<Client> clients) {
        this.socket = socket;
        this.clients = clients;
    }

    @Override
    public void run() {
        try {
            var writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()), 10);
            var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()), 10);

            String message;
            do {
                message = reader.readLine();
                sendToOtherClients(writer, message);

                System.out.println(message);
            } while (message != null && !message.equals("exit"));

            reader.close();
            writer.close();
            this.socket.close();
            this.clients.removeIf(m -> m.getId() == getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getId() {
        return id;
    }

    private void sendMessage(BufferedWriter writer, String message) {
        try {
            writer.write(message);
            writer.newLine();
            writer.flush();
        } catch (IOException ignored) {
        }
    }

    private void sendToOtherClients(BufferedWriter writer, String message) {
        if (message == null) {
            return;
        }

        clients.stream()
                .filter(client -> client.getId() != getId())
                .forEach(client -> client.sendMessage(writer, message));
    }
}
