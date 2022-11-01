package org.example;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        Receiver receiver = new Receiver(Integer.parseInt(args[0]));
        receiver.takeNewTask();
    }
}