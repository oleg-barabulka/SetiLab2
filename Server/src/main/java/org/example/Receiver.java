package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Receiver {
    private ServerSocket serverSocket;
    public Receiver(){
        try {
            serverSocket = new ServerSocket(8080);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void takeNewTask(){
        try {
            Socket newConnection = serverSocket.accept();
            MakeTask makeTask = new MakeTask(newConnection);
            makeTask.getFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
