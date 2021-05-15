package com.kemalbeyaz.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicBoolean;

public class MessageSender implements Runnable {

    private final AtomicBoolean isExit;
    private final BufferedWriter writer;

    public MessageSender(BufferedWriter writer, AtomicBoolean isExit) {
        this.isExit = isExit;
        this.writer = writer;
    }

    @Override
    public void run() {
        System.out.print("What is your name: ");
        var consoleReader = new BufferedReader(new InputStreamReader(System.in));

        try {
            String name = consoleReader.readLine();
            String readLine;

            do {
                readLine = consoleReader.readLine();

                writer.write(name + ": " + readLine);
                writer.newLine();
                writer.flush();
            } while (!readLine.equals("exit"));

            writer.close();
            isExit.set(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
