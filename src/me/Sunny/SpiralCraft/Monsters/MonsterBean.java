package me.Sunny.SpiralCraft.Monsters;

import org.bukkit.util.Vector;

public class MonsterBean {

    private String type;
    private Vector location;
    // More attributes will be added:

    public MonsterBean() {}

    public String getType() { return type; }
    public Vector getLocation() { return location; }
    public void setType(String type) { this.type = type; }
    public void setLocation(Vector location) { this.location = location; }
}
