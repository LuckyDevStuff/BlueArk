package de.luckydev.blueark.detect.movement;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class FlyDetection extends MovementDetection {
    public FlyDetection() {
        super("Fly Detection");
    }

    @Override
    public HandelingType onMove(PlayerMoveEvent event) {
        return null;
    }
}
