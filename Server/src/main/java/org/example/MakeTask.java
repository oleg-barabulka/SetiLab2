package org.example;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Logger;

public class MakeTask {
    private final Logger logger = Logger.getLogger(MakeTask.class.getName());
    private final int TIME_INTERVAL = 3000;
    private final Socket receiveSocket;

    public MakeTask(Socket socket) {
        this.receiveSocket = socket;
    }

    public void getFile() {
        DataInputStream inputStream;
        DataOutputStream writer;
        final int PACKET_SIZE = 100;
        try {
            int acceptedBytes = 0;
            inputStream = new DataInputStream(receiveSocket.getInputStream());
            writer = new DataOutputStream(receiveSocket.getOutputStream());
            final String fileName = inputStream.readLine();
            logger.info("file name: " + fileName);
            final int fileSize = Integer.parseInt(inputStream.readLine());
            logger.info("file size: " + fileSize + " B");
            int leftToAccept = fileSize;
            byte[] contentArray;
            byte[] fileContent = new byte[fileSize];
            int iterationNum = 0;
            long startTimeForAverageSpeed = System.currentTimeMillis();
            long timeForCurrentSpeed = System.currentTimeMillis();
            while (leftToAccept > PACKET_SIZE) {
                if (System.currentTimeMillis() - timeForCurrentSpeed > TIME_INTERVAL) {
                    timeForCurrentSpeed = printSpeed(acceptedBytes, startTimeForAverageSpeed, timeForCurrentSpeed);
                }
                contentArray = inputStream.readNBytes(PACKET_SIZE);
                System.arraycopy(contentArray, 0, fileContent, iterationNum * PACKET_SIZE, PACKET_SIZE);
                leftToAccept -= PACKET_SIZE;
                acceptedBytes += PACKET_SIZE;
                iterationNum++;
            }
            if (leftToAccept > 0) {
                contentArray = inputStream.readNBytes(leftToAccept);
                System.arraycopy(contentArray, 0, fileContent, iterationNum * PACKET_SIZE, contentArray.length);
                leftToAccept -= contentArray.length;
            }
            if (leftToAccept <= 0) {
                logger.info("File has made");
                writer.write("File accepted".getBytes());
            } else {
                logger.warning("File not accepted");
                writer.write("File not accepted".getBytes());
            }
            makeFile(fileName, fileContent);

            writer.flush();
            receiveSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void makeFile(String fileName, byte[] fileContent) throws IOException {
        File dir = new File("uploads");
        dir.mkdir();
        File file = new File(dir, fileName);
        file.createNewFile();
        OutputStream outputStream = new FileOutputStream(file);
        outputStream.write(fileContent);
    }

    long printSpeed(int acceptedBytes, long startTimeForAverageSpeed, Long timeForCurrentSpeed) {
        printCurrentSpeed(acceptedBytes);
        printAverageSpeed(acceptedBytes, startTimeForAverageSpeed);
        return System.currentTimeMillis();
    }

    void printAverageSpeed(int acceptedBytes, long startTime) {
        long receiptTime = System.currentTimeMillis() - startTime;
        double averageSpeed = (double) acceptedBytes / receiptTime;
        System.out.println("Average speed: " + averageSpeed + " B\\ms:");
    }

    void printCurrentSpeed(int acceptedBytes) {
        double averageSpeed = (double) acceptedBytes / TIME_INTERVAL;
        System.out.println("Current speed: " + averageSpeed + " B\\ms:");
    }
}
