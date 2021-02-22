package de.luckydev.blueark.updater;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;

import java.net.URL;

public class UpdateManager {

    public static String githubAPI = "https://api.github.com/repos/LuckyDevStuff/BlueArk";
    public static String githubRelease = "https://github.com/LuckyDevStuff/BlueArk";
    public static double version = 1.0;

    public static String checkForUpdates() {
        try {
            JSONArray releases = new JSONArray(getFromURL(githubAPI + "/releases"));
            double id = Double.parseDouble( ((JSONObject)releases.get(0)).get("name").toString().substring(1) );
            return id > version?"v"+id:null;
        } catch (Exception ignored) {
        }
        return null;
    }

    public static void downloadRelease(String release, String file, File out) {
        try {
            try (BufferedInputStream inputStream = new BufferedInputStream(new URL(githubRelease + "/releases/download/" + release + "/" + file).openStream());
                 FileOutputStream fileOS = new FileOutputStream(out)) {
                byte data[] = new byte[1024];
                int byteContent;
                while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
                    fileOS.write(data, 0, byteContent);
                }
            } catch (IOException e) {
                // handles IO exceptions
            }
            getBuffFromURL(githubRelease + "/releases/download/" + release + "/" + file);
        } catch (Exception ignored) {
        }
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
