package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Receiver {
    private final ExecutorService threadPool = Executors.newCachedThreadPool();
    private ServerSocket serverSocket;
    public Receiver(int portNum){
        try {
            serverSocket = new ServerSocket(portNum);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void takeNewTask(){
        try {
            while (!serverSocket.isClosed()) {
                Socket newConnection = serverSocket.accept();
                threadPool.submit(() -> new MakeTask(newConnection).getFile());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
