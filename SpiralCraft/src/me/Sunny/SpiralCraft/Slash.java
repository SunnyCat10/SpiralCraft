package me.Sunny.SpiralCraft;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Slash implements CommandExecutor, Listener{
	
	private final JavaPlugin plugin;
	
	public Slash(JavaPlugin instance) {
		plugin = instance;
	}

	@Override
	public boolean onCommand(CommandSender sender,Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("slash")) {
			if (! (sender instanceof Player)) {
				return true;
			}
			
			Player player = (Player) sender;
			player.getInventory().addItem(new ItemBuilder(Material.GOLDEN_SWORD).setName("Demonstrator").setLore("slashing stuff").build());
			return true;
		}

		return false;
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		if (event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.GOLDEN_SWORD)) {
			if (event.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasLore()) {
				Player player = event.getPlayer();
				
				if (event.getAction() == Action.RIGHT_CLICK_AIR) {
					player.sendMessage("Right click");
					spawnParticles(player);
				}
			}
			
		}
	}
	
	public void spawnParticles(Player player)
	{		
		new BukkitRunnable() {
			double degree = 360;
			World world = player.getWorld();
			DustOptions dustOptions = new DustOptions(Color.fromRGB(0, 127, 255), 1);
			
			Vector up = new Vector(0, 1, 0); 
			
			Vector playerLocation = player.getLocation().toVector().add(up);
			Vector playerDirection = player.getLocation().getDirection().normalize().setY(0);
				
			ArmorStand armorStand = armorStandBuilder(player);
			
			float swordYaw = player.getLocation().getYaw();
			
			ItemStack sword = player.getInventory().getItemInMainHand();
				
			@Override
			public void run() {
				if (degree == 360) { // Start of the thread
					Stun(player, true);	
					player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
				}
				if (degree == 0) { // End of the thread
					Stun(player, false);
					player.getInventory().setItemInMainHand(sword);
					armorStand.remove();
					cancel();
				}				
				Vector direction = playerDirection.clone().rotateAroundAxis(new Vector(0, 1, 0), Math.toRadians(degree));
				Vector particleLocation = playerLocation.clone().add(direction);
				
				
				player.spawnParticle(Particle.REDSTONE, particleLocation.toLocation(world), 50, dustOptions);
				
				armorStand.setRotation(swordYaw, 0);
				
				
				degree -= 30;
				swordYaw += 30;
			}			
		}.runTaskTimer(plugin, 0, 1);
	}
	
	private ArmorStand armorStandBuilder(Player player)
	{
		ArmorStand armorStand = (ArmorStand) player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
		EntityEquipment armorStandEquipment = armorStand.getEquipment();
		
		armorStand.setVisible(false);
		armorStand.setBasePlate(false);
		armorStand.setMarker(true);
		armorStand.setRightArmPose(armorStand.getRightArmPose().add(0, 0, Math.toRadians(90)));
		
		armorStandEquipment.setItemInMainHand(new ItemStack(Material.GOLDEN_SWORD));
		
		
		
		return armorStand;
	}
	
	private static void Stun(Player player, boolean isStunned) {
		if (isStunned) {
			player.setWalkSpeed(0f);
		}
		else {
			player.setWalkSpeed(0.2f);
		}
	}

}
