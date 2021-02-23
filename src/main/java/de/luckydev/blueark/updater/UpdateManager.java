package de.luckydev.blueark.updater;

import de.luckydev.blueark.util.FileUtil;

import java.io.*;
import java.net.HttpURLConnection;

import java.net.URL;

public class UpdateManager {

    public static String api = "https://luckydev.de/api/BlueArk";
    public static String download = "https://github.com/LuckyDevStuff/BlueArk";
    public static double version = 1.2;

    public static String checkForUpdates() {
        double id = Double.parseDouble(getFromURL(api + "/version.php?scope=last"));
        return id > version?"v"+id:null;
    }

    public static void downloadFromRelease(String release, String resource, File out) {
        FileUtil.downloadToFile(download + "/releases/download/" + release + "/" + resource, out);
    }

    public static BufferedReader getBuffFromURL(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            BufferedReader stream = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            return stream;
        } catch (Exception e) {
            return null;
        }
    }

    public static String getFromURL(String url) {
        try {
            BufferedReader stream = getBuffFromURL(url);
            String line; StringBuffer content = new StringBuffer();
            while ((line = stream.readLine()) != null) {
                content.append(line);
            }
            stream.close();
            return content.toString();
        } catch (Exception e) {
            return null;
        }
    }

}
