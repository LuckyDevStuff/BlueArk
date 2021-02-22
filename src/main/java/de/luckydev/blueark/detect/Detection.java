package de.luckydev.blueark.detect;

import org.bukkit.event.Event;

public abstract class Detection {
    public String name;
    public Detection(String name) {
        this.name = name;
    }
    public abstract HandelingType onEvent(Event event);

    public static enum HandelingType {
        FLAG, BAN, KICK;
    }
}
