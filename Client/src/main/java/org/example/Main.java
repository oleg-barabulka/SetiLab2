package org.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Main {
    public static void main(String[] args) {
        String fileName = "test.txt";
        String filePath = "D:\\JavaPrograms\\Lab2\\Client\\src\\main\\java\\org\\example\\test.txt";
        File file = new File(filePath);
        byte[] fileContent;
        try {
            fileContent = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FileSender fileSender = new FileSender(fileContent, fileName, fileContent.length );
        fileSender.sendFile();
    }
}