package de.luckydev.blueark;

import de.luckydev.blueark.detect.movement.MovementDetection;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static final String PREFIX = "§7[§2BLUEARK§7] §r";

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getConsoleSender().sendMessage(PREFIX + "§6Loading Plugin...");

        Bukkit.getConsoleSender().sendMessage(PREFIX + "§aLoaded Plugin!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getConsoleSender().sendMessage(PREFIX + "§Stopping Plugin...");
        Bukkit.getConsoleSender().sendMessage(PREFIX + "§Stopped Plugin!");
    }
}
