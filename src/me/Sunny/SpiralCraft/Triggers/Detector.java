package me.Sunny.SpiralCraft.Triggers;

import me.Sunny.SpiralCraft.Data.Party;
import me.Sunny.SpiralCraft.Data.PartyManager;
import me.Sunny.SpiralCraft.Data.SpiralPlayer;
import me.Sunny.SpiralCraft.Levels.Level;
import me.Sunny.SpiralCraft.Levels.LevelManager;
import me.Sunny.SpiralGeneration.Utils.Point;
import org.bukkit.Chunk;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Detector {

    private final JavaPlugin plugin;

    public Detector(JavaPlugin instance) {
        plugin = instance;
    }

    public void run() {
        new BukkitRunnable() {

            public void run() {
                Collection<Party> partyList = PartyManager.getPartyCollection();
                for (Party party : partyList) {
                    if (party.isPlaying()) {
                        List<SpiralPlayer> spiralPlayerList = party.getPartyMembers();
                        Level partyLevel = LevelManager.getLevel(party.getLevelStage(), party.getLevelID());

                        for (SpiralPlayer spiralPlayer : spiralPlayerList) {
                            List<TriggerBean> triggerBeanList = partyLevel.getTriggerList();
                            Chunk playerChunk = spiralPlayer.getPlayer().getLocation().getChunk();
                            Point playerPoint = new Point(playerChunk.getX(), playerChunk.getZ());

                            // TODO: the current null check is not working...S
                            if (triggerBeanList == null)
                                break;

                            for (TriggerBean triggerBean : triggerBeanList) {
                                Triggable trigger = triggerBean.getTrigger();
                                if (playerPoint.equals(trigger.getTriggerLocation())) {
                                    if (triggerBean.getState()) {
                                        System.out.println("Trigger is set and disabled.");
                                        trigger.Trigger();
                                        triggerBean.setState(false);
                                        break;
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }
}
