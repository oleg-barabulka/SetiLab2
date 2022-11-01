package org.example;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

public class FileSender {
    private final Logger logger = Logger.getLogger(FileSender.class.getName());
    private final byte[] fileContent;
    private final String fileName;
    private int fileSize;
    public FileSender(byte[] fileContent, String fileName, int fileSize){
        this.fileContent = fileContent;
        this.fileName = fileName;
        this.fileSize = fileSize;
    }
    public void sendFile(String serverAddress, int serverPort){
        Socket sendSocket;
        final int DATAGRAM_SIZE = 100;
        final int MAX_MASSAGE_BYTE = 20;
        try {
            sendSocket = new Socket(serverAddress, serverPort);
            DataOutputStream outputStreamWriter = new DataOutputStream(sendSocket.getOutputStream());
            DataInputStream reader = new DataInputStream(sendSocket.getInputStream());
            outputStreamWriter.write((this.fileName + "\n").getBytes());
            outputStreamWriter.flush();
            outputStreamWriter.write((this.fileSize + "\n").getBytes());
            outputStreamWriter.flush();
            int leftToPass = this.fileSize;

            byte[] copyArray = new byte[DATAGRAM_SIZE];
            int iterationNum = 0;
            while (leftToPass > DATAGRAM_SIZE){
                System.arraycopy(fileContent, DATAGRAM_SIZE *iterationNum, copyArray, 0, DATAGRAM_SIZE);
                outputStreamWriter.write(copyArray);
                outputStreamWriter.flush();
                leftToPass -= DATAGRAM_SIZE;
                iterationNum++;
            }
            if (leftToPass != 0) {
                byte[] leftArray = new byte[leftToPass];
                System.arraycopy(fileContent, DATAGRAM_SIZE *iterationNum, leftArray, 0, leftToPass);
                outputStreamWriter.write(leftArray);
                outputStreamWriter.flush();
            }
            String dispatchStatus = new String(reader.readNBytes(MAX_MASSAGE_BYTE));
            if (dispatchStatus.equals("File accepted")){
                logger.info(dispatchStatus);
            } else if (dispatchStatus.equals("File not accepted")) {
                logger.warning(dispatchStatus);
            }
            sendSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
