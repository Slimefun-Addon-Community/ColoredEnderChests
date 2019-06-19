package io.github.thebusybiscuit.coloredenderchests;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.EulerAngle;

import me.mrCookieSlime.CSCoreLibPlugin.PluginUtils;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.MenuItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.ArmorStandFactory;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.Research;

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
		PluginUtils utils = new PluginUtils(this);
		utils.setupConfig();
		cfg = utils.getConfig();
		
		utils.setupMetrics();
		utils.setupUpdater(99696, getFile());
		
		Research r = new Research(2610, "Colored Ender Chests", 20);
		Research r2 = new Research(2611, "Big Colored Ender Chests", 30);
		Research r3 = new Research(2612, "Colored Ender Backpacks", 20);
		Research r4 = new Research(2613, "Big Colored Ender Backpacks", 30);
		
		r.register();
		r2.register();
		r3.register();
		r4.register();
		
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
		
		category = new Category(new MenuItem(Material.ENDER_CHEST, "&5Colored Ender Chests", 0, "open"), 2);
		Category category2 = null;
		try {
			category2 = new Category(new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmEzYjM0ODYyYjlhZmI2M2NmOGQ1Nzc5OTY2ZDNmYmE3MGFmODJiMDRlODNmM2VhZjY0NDlhZWJhIn19fQ=="), "&5Colored Ender Backpacks", "", "&a> Click to open"), 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (int c1 = 0; c1 < 16; c1++) {
			for (int c2 = 0; c2 < 16; c2++) {
				for (int c3 = 0; c3 < 16; c3++) {
					try {
						registerEnderChest(category, category2, r, r2, r3, r4, c1, c2, c3);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
	}
	
	private void registerEnderChest(Category category, Category category2, Research r, Research r2, Research r3, Research r4, final int c1, final int c2, final int c3) throws Exception {
		if (cfg.getBoolean("small_chests")) {
			ColoredEnderChest item = new ColoredEnderChest(this, 27, c1, c2, c3);
			item.register();
			r.addItems(item);
		}
		
		if (cfg.getBoolean("big_chests")) {
			ColoredEnderChest item = new ColoredEnderChest(this, 54, c1, c2, c3);
			item.register();
			r2.addItems(item);
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
		for (Entity n: b.getChunk().getEntities()) {
			if (n instanceof ArmorStand) {
				if (n.getCustomName() == null && b.getLocation().add(0.5D, 0.5D, 0.5D).distanceSquared(n.getLocation()) < 0.75D) {
					n.remove();
				}
			}
		}
	}
}
