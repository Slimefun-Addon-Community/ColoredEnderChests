package io.github.thebusybiscuit.coloredenderchests;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.EulerAngle;

import me.mrCookieSlime.CSCoreLibPlugin.general.World.ArmorStandFactory;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.Research;
import me.mrCookieSlime.Slimefun.bstats.bukkit.Metrics;
import me.mrCookieSlime.Slimefun.cscorelib2.config.Config;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import me.mrCookieSlime.Slimefun.cscorelib2.updater.GitHubBuildsUpdater;
import me.mrCookieSlime.Slimefun.cscorelib2.updater.Updater;

public class ColoredEnderChests extends JavaPlugin {
	
	protected Config cfg;
	protected Map<Integer, String> colors = new HashMap<>();
	protected Category category;
	
	protected Material[] wool = {
		Material.WHITE_WOOL,
		Material.ORANGE_WOOL,
		Material.MAGENTA_WOOL,
		Material.LIGHT_BLUE_WOOL,
		Material.YELLOW_WOOL,
		Material.LIME_WOOL,
		Material.PINK_WOOL,
		Material.GRAY_WOOL,
		Material.LIGHT_GRAY_WOOL,
		Material.CYAN_WOOL,
		Material.PURPLE_WOOL,
		Material.BLUE_WOOL,
		Material.BROWN_WOOL,
		Material.GREEN_WOOL,
		Material.RED_WOOL,
		Material.BLACK_WOOL
	};
	
	protected double angle = Math.toRadians(345);
	protected double offset = -0.08;
	
	@Override
	public void onEnable() {
		cfg = new Config(this);
		
		// Setting up bStats
		new Metrics(this, 4907);

		// Setting up the Auto-Updater
		if (getDescription().getVersion().startsWith("DEV - ")) {
			// If we are using a development build, we want to switch to our custom 
			Updater updater = new GitHubBuildsUpdater(this, getFile(), "TheBusyBiscuit/ColoredEnderChests/master");
			
			if (cfg.getBoolean("options.auto-update")) updater.start();
		}
		
		Research enderChestsResearch = new Research(new NamespacedKey(this, "colored_enderchests"), 2610, "Colored Ender Chests", 20);
		Research bigEnderChestsResearch = new Research(new NamespacedKey(this, "big_colored_enderchests"), 2611, "Big Colored Ender Chests", 30);
		
		enderChestsResearch.register();
		bigEnderChestsResearch.register();
		
		colors.put(0, "&rWhite");
		colors.put(1, "&6Orange");
		colors.put(2, "&dMagenta");
		colors.put(3, "&bLight Blue");
		colors.put(4, "&eYellow");
		colors.put(5, "&aLime");
		colors.put(6, "&dPink");
		colors.put(7, "&8Dark Gray");
		colors.put(8, "&7Light Gray");
		colors.put(9, "&3Cyan");
		colors.put(10, "&5Purple");
		colors.put(11, "&9Blue");
		colors.put(12, "&6Brown");
		colors.put(13, "&2Green");
		colors.put(14, "&4Red");
		colors.put(15, "&8Black");
		
		category = new Category(new NamespacedKey(this, "colored_enderchests"), new CustomItem(Material.ENDER_CHEST, "&5Colored Ender Chests"), 2);
		
		for (int c1 = 0; c1 < 16; c1++) {
			for (int c2 = 0; c2 < 16; c2++) {
				for (int c3 = 0; c3 < 16; c3++) {
					registerEnderChest(enderChestsResearch, bigEnderChestsResearch, c1, c2, c3);
				}
			}
		}
		
	}
	
	private void registerEnderChest(Research smallResearch, Research bigResearch, final int c1, final int c2, final int c3) {
		if (cfg.getBoolean("small_chests")) {
			ColoredEnderChest item = new ColoredEnderChest(this, 27, c1, c2, c3);
			item.register();
			smallResearch.addItems(item);
		}
		
		if (cfg.getBoolean("big_chests")) {
			ColoredEnderChest item = new ColoredEnderChest(this, 54, c1, c2, c3);
			item.register();
			bigResearch.addItems(item);
		}
	}
	
	protected void updateIndicator(Block b, int c1, int c2, int c3, int yaw) {
		removeIndicator(b);
		EulerAngle euler = new EulerAngle(angle, 0F, 0F);
		
		Location l = b.getLocation().add(0.5D, 0.5D + offset, 0.5D);
		ArmorStandFactory.createSmall(l, new ItemStack(wool[c1]), euler, (float) yaw);
		ArmorStandFactory.createSmall(getLocation(l, 1, yaw), new ItemStack(wool[c2]), euler, (float) yaw);
		ArmorStandFactory.createSmall(getLocation(l, 1, yaw), new ItemStack(wool[c3]), euler, (float) yaw);
	}
	
	private Location getLocation(Location l, int direction, int yaw) {
		if (yaw == 45) { // 0
			return l.add(0.275 * direction, 0, 0);
		}
		else if (yaw == 225) { // 180
			return l.add(-0.275 * direction, 0, 0);
		}
		else if (yaw == -45) { // -90
			return l.add(0, 0, -0.275 * direction);
		}
		else { // 90
			return l.add(0, 0, 0.275 * direction);
		}
	}

	protected void removeIndicator(Block b) {
		for (Entity n : b.getChunk().getEntities()) {
			if (n instanceof ArmorStand && n.getCustomName() == null && b.getLocation().add(0.5D, 0.5D, 0.5D).distanceSquared(n.getLocation()) < 0.75D) {
				n.remove();
			}
		}
	}
}
