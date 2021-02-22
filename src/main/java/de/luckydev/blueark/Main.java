package de.luckydev.blueark;

import com.google.gson.Gson;
import de.luckydev.blueark.updater.UpdateManager;
import de.luckydev.blueark.util.FileUtil;
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
        Bukkit.getConsoleSender().sendMessage(PREFIX + "§6Loading Plugin...");

        getDataFolder().mkdir();

        File updateFile = new File(getDataFolder(), "update.txt");
        if(updateFile.exists()) {
            try {
                Files.delete(updateFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        CountDownLatch update = null;

        // UPDATE
        Bukkit.getConsoleSender().sendMessage(PREFIX + "§bChecking for Updates...");
        String tag = UpdateManager.checkForUpdates();
        if(tag != null) {
            update = new CountDownLatch(1);
            Bukkit.getConsoleSender().sendMessage(PREFIX + "§bFound Update! Updating in a new Thread");
            CountDownLatch finalUpdate = update;
            Runnable runnable = ()->{
                UpdateManager.downloadRelease(tag, "BlueArk.jar", new File(getDataFolder() + "/../", "BlueArk" + tag + ".jar"));
                finalUpdate.countDown();
            };
            new Thread(runnable).start();
        } else {
            Bukkit.getConsoleSender().sendMessage(PREFIX + "§6You have the newest Update!");
        }

        Bukkit.getConsoleSender().sendMessage(PREFIX + "§aLoaded Plugin!");
        if(update != null) {
            try {
                update.await();
                String name = new java.io.File(Main.class.getProtectionDomain()
                        .getCodeSource()
                        .getLocation()
                        .getPath())
                        .getName();
                FileUtil.write(name, new File(getDataFolder(), "update.txt"));
                Bukkit.getConsoleSender().sendMessage(PREFIX + "§bUPDATED! You are now on BlueArk" + tag + "!");
                Bukkit.getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin("Blueark"));
                Bukkit.getPluginManager().enablePlugin(Bukkit.getPluginManager().loadPlugin(new File(getDataFolder() + "/../", "BlueArk" + tag + ".jar")));
                System.exit(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getConsoleSender().sendMessage(PREFIX + "§6Stopping Plugin...");
        Bukkit.getConsoleSender().sendMessage(PREFIX + "§6Stopped Plugin!");
    }
}
