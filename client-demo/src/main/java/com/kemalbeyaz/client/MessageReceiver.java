package com.kemalbeyaz.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class MessageReceiver implements Runnable {

    private final AtomicBoolean isExit;
    private final BufferedReader reader;

    public MessageReceiver(BufferedReader reader, AtomicBoolean isExit) {
        this.isExit = isExit;
        this.reader = reader;
    }

    @Override
    public void run() {
        try {
            do {
                var message = reader.readLine();
                System.out.println(">> " + message);
            } while (!isExit.get());

            reader.close();
        } catch (IOException ignored) {
        }
    }
}
