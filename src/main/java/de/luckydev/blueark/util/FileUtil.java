package de.luckydev.blueark.util;

import java.nio.file.Files;

import java.io.*;
import java.nio.file.Paths;

public class FileUtil {
    public static void write(String data, File file) {
        try {
            Files.write(file.toPath(), data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
