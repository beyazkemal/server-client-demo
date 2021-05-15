package com.kemalbeyaz.server;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Random;

public class MessageListener implements Runnable {

    private final int id = new Random().nextInt();
    private final Socket socket;
    private final List<MessageListener> messageListeners;

    private boolean isAlive = true;
    private BufferedWriter writer;

    public MessageListener(Socket socket, List<MessageListener> messageListeners) {
        this.socket = socket;
        this.messageListeners = messageListeners;
    }

    @Override
    public void run() {
        try {
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()), 10);
            var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()), 10);

            String message;
            do {
                message = reader.readLine();
                sendToOtherClients(message);

                System.out.println(message);
            } while (message != null && !message.equals("exit"));

            this.isAlive = false;

            reader.close();
            this.writer.close();
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isAlive() {
        return isAlive;
    }

    private int getId() {
        return id;
    }

    private void sendMessage(String message) {
        try {
            this.writer.write(message);
            this.writer.newLine();
            this.writer.flush();
        } catch (IOException ignored) {
        }
    }

    private void sendToOtherClients(String message) {
        if (message == null) {
            return;
        }

        messageListeners.stream()
                .filter(MessageListener::isAlive)
                .filter(messageListener -> messageListener.getId() != getId())
                .forEach(messageListener -> messageListener.sendMessage(message));
    }
}
