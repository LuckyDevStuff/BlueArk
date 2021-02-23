package de.luckydev.blueark;

import com.google.gson.Gson;
import de.luckydev.blueark.updater.UpdateManager;
import de.luckydev.blueark.util.FileUtil;
import de.luckydev.blueark.util.PluginUnloader;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.CountDownLatch;

public class Main extends JavaPlugin {

    public static final String PREFIX = "§7[§bBLUEARK§7] §r";
    public static final Gson gson = new Gson();

    @Override
    public void onEnable() {
        // Plugin startup logic


        getDataFolder().mkdir();

        // UPDATE
        File updateFile = new File(getDataFolder(), "update.txt");
        if(updateFile.exists()) {
            try {
                File old = new File(getDataFolder() , "/../" + FileUtil.read(updateFile));
                Bukkit.getConsoleSender().sendMessage(PREFIX + "§bYour on the newest version!");
                Files.delete(updateFile.toPath());
                Files.delete(old.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            String tag = UpdateManager.checkForUpdates();
            if(tag != null) {
                try {
                    // Download the Update
                    UpdateManager.downloadFromRelease(tag, "BlueArk.jar", new File(getDataFolder(), "/../BlueArk " + tag + ".jar"));

                    String name = new java.io.File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getName();
                    FileUtil.write(name, new File(getDataFolder(), "update.txt"));
                    Bukkit.getConsoleSender().sendMessage(PREFIX + "§bUPDATED! You are now on BlueArk" + tag + "!");
                    PluginUnloader.unloadPlugin(Bukkit.getPluginManager().getPlugin("Blueark"));
                    Bukkit.getPluginManager().enablePlugin(Bukkit.getPluginManager().loadPlugin(new File(getDataFolder() + "/../", "BlueArk " + tag + ".jar")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(PREFIX + "§bYour on the newest version!");
            }
        }

        Bukkit.getConsoleSender().sendMessage(PREFIX + "§6Loading Plugin...");
        Bukkit.getConsoleSender().sendMessage(PREFIX + "§aLoaded Plugin!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getConsoleSender().sendMessage(PREFIX + "§6Stopping Plugin...");
        Bukkit.getConsoleSender().sendMessage(PREFIX + "§6Stopped Plugin!");
    }
}
