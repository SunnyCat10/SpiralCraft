package me.Sunny.SpiralCraft.Triggers;

import java.io.Serializable;

public class TriggerBean implements Serializable {
    private Triggable trigger;
    private boolean state = true;

    public TriggerBean() {}

    public Triggable getTrigger() { return trigger; }
    public boolean getState() { return state; }

    public void setTrigger(Triggable trigger) { this.trigger = trigger; }
    public void setState(boolean state) { this.state = state; }
}
