package de.luckydev.blueark.util;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredListener;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URLClassLoader;
import java.util.*;

public class PluginUnloader {

    public static PluginManager pluginManager = Bukkit.getPluginManager();

    public static void disablePlugin(final Plugin plugin) {
        if (plugin.isEnabled()) {
            pluginManager.disablePlugin(plugin);
        }
    }

    public static void unloadPlugin(final Plugin plugin) {
        disablePlugin(plugin);

        List<Plugin> plugins;
        Map<String, Plugin> lookupNames;
        SimpleCommandMap commandMap;
        Map<String, Command> knownCommands;
        Map<Event, SortedSet<RegisteredListener>> listeners;

        try { //Get plugins list
            Field f = pluginManager.getClass().getDeclaredField("plugins");
            f.setAccessible(true);
            plugins = (List<Plugin>) f.get(pluginManager);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new IllegalStateException("Unable to get plugins list");
        }
        try { //Get lookup names
            Field f = pluginManager.getClass().getDeclaredField("lookupNames");
            f.setAccessible(true);
            lookupNames = (Map<String, Plugin>) f.get(pluginManager);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new IllegalStateException("Unable to get lookup names");
        }
        try { //Get command map
            Field f = pluginManager.getClass().getDeclaredField("commandMap");
            f.setAccessible(true);
            commandMap = (SimpleCommandMap) f.get(pluginManager);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new IllegalStateException("Unable to get command map");
        }
        try { //Get known commands
            Field f = SimpleCommandMap.class.getDeclaredField("knownCommands");
            f.setAccessible(true);
            knownCommands = (Map<String, Command>) f.get(commandMap);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new IllegalStateException("Unable to get known commands");
        }
        try {
            Field f = pluginManager.getClass().getDeclaredField("listeners");
            f.setAccessible(true);
            listeners = (Map<Event, SortedSet<RegisteredListener>>) f.get(pluginManager);
        } catch (Throwable e) {
            listeners = null;
        }

        plugins.remove(plugin);
        lookupNames.remove(plugin.getName());
        { //Remove plugin commands
            Iterator<Map.Entry<String, Command>> iterator = knownCommands.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Command> entry = iterator.next();
                if (entry.getValue() instanceof PluginCommand) {
                    PluginCommand command = (PluginCommand) entry.getValue();
                    if (command.getPlugin().equals(plugin)) {
                        iterator.remove();
                    }
                }
            }
        }
        if (listeners != null) {
            for (Set<RegisteredListener> registeredListeners : listeners.values()) {
                registeredListeners.removeIf(registeredListener -> registeredListener.getPlugin().equals(plugin));
            }
        }

        if (plugin.getClass().getClassLoader() instanceof URLClassLoader) {
            URLClassLoader classLoader = (URLClassLoader) plugin.getClass().getClassLoader();

            try {
                for (Field f : classLoader.getClass().getDeclaredFields()) {
                    f.setAccessible(true);
                    if (Modifier.isFinal(f.getModifiers())) {
                        Field mf = f.getClass().getDeclaredField("modifiers");
                        mf.setAccessible(true);
                        mf.setInt(f, f.getModifiers() & ~Modifier.FINAL);
                    }
                    f.set(classLoader, null);
                }
            } catch (Throwable e) {
                throw new IllegalStateException("Unable to remove class loader handles", e);
            }
        }

        System.gc(); //Hopefully remove all leftover plugin classes and references
    }
}
