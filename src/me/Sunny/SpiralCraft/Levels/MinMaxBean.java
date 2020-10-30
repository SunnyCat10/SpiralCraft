package me.Sunny.SpiralCraft.Levels;

import org.bukkit.util.Vector;

import java.io.Serializable;

// TODO: Check about serializable...
public class MinMaxBean implements Serializable {
    private Vector minPoint;
    private Vector maxPoint;

    public MinMaxBean() {}

    public Vector getMinPoint() {
        return new Vector(
                minPoint.getX(),
                minPoint.getY(),
                minPoint.getZ());
    }
    public Vector getMaxPoint() {
        return new Vector(
                maxPoint.getX(),
                maxPoint.getY(),
                maxPoint.getZ());
    }

    public void setMinPoint(Vector minPoint) {
        this.minPoint = new Vector(
                minPoint.getX(),
                minPoint.getY(),
                minPoint.getZ());
    }
    public void setMaxPoint(Vector maxPoint) {
        this.maxPoint = new Vector(
                maxPoint.getX(),
                maxPoint.getY(),
                maxPoint.getZ());
    }
}
