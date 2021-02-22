package de.luckydev.blueark.detect.movement;

import de.luckydev.blueark.detect.Detection;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

public abstract class MovementDetection extends Detection {
    public MovementDetection(String name) {
        super(name);
    }

    public abstract HandelingType onMove(PlayerMoveEvent event);
    public HandelingType onEvent(Event event) {
        if(event instanceof PlayerMoveEvent) return onMove((PlayerMoveEvent) event);
        return null;
    };
}
