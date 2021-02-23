package de.luckydev.blueark.util;

import java.net.URL;
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

    public static String read(File file) {
        try {
            return new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void downloadToFile(String url, File out) {
        try (BufferedInputStream inputStream = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOS = new FileOutputStream(out)) {
            byte data[] = new byte[1024];
            int byteContent;
            while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
                fileOS.write(data, 0, byteContent);
            }
        } catch (IOException e) {
            // handles IO exceptions
        }
    }
}
