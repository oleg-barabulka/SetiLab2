package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.channels.MembershipKey;

public class MakeTask {
    private Socket receiveSocket;
    public MakeTask(Socket socket){
        this.receiveSocket = socket;
    }
    public void getFile(){
        BufferedReader inputStream;
        try {
            inputStream = new BufferedReader(new InputStreamReader(receiveSocket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            final String fileName = inputStream.readLine();
            final int fileSize = Integer.parseInt(inputStream.readLine());
            byte[] fileContent = inputStream.readLine().getBytes();
            System.out.println(new String(fileContent));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
