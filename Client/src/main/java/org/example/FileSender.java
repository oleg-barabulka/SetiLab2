package org.example;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.UnknownHostException;

public class FileSender {
    private final byte[] fileContent;
    private final String fileName;
    private int fileSize;
    public FileSender(byte[] fileContent, String fileName, int fileSize){
        this.fileContent = fileContent;
        this.fileName = fileName;
        this.fileSize = fileSize;
    }
    public void sendFile(){
        final int datagramSize = 100;
        Socket sendSocket;
        try {
            sendSocket = new Socket(Inet4Address.getLocalHost().getHostAddress(), 8080);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(sendSocket.getOutputStream());
            outputStreamWriter.write(this.fileName + "\n");
            outputStreamWriter.write( this.fileSize+ "\n");
            while (fileSize > datagramSize){
                outputStreamWriter.write(this.fileContent[datagramSize]);
                System.arraycopy(fileContent, 0,fileContent, 100, datagramSize);
                this.fileSize -= datagramSize;
            }
            outputStreamWriter.write(this.fileContent[this.fileSize - 1]);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            sendSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
