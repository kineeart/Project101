package com.example.middleware.delivery.storage;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

public class FileStorage {

    public void write(String filePath, List<String> lines) {

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {

            for (String line : lines) {
                writer.println(line);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to write file: " + filePath, e);
        }
    }
}