package org.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    public static void main(String[] args) {
        String filePath = args[0];
        logger.info("file path: " + filePath);
        String fileName = filePath.split("\\\\")[filePath.split("\\\\").length - 1];
        logger.info("file name: " + fileName);
        File file = new File(filePath);
        byte[] fileContent;
        try {
            fileContent = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int fileSize = fileContent.length;
        logger.info("file size: " + fileSize + " B");
        FileSender fileSender = new FileSender(fileContent, fileName, fileSize);
        fileSender.sendFile(args[1], Integer.parseInt(args[2]));
    }
}