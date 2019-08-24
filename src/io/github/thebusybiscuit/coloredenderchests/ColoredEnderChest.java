package io.github.thebusybiscuit.coloredenderchests;

import java.util.stream.IntStream;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.EnderChest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;

public class ColoredEnderChest extends SlimefunItem {

	public ColoredEnderChest(ColoredEnderChests plugin, int size, int c1, int c2, int c3) {
		super(plugin.category, 
			new CustomItem(Material.ENDER_CHEST, "&eColored Ender Chest &7(" + (size == 27 ? "Small": "Big") + ")", "", "&7Size: &e" + size, "", "&7- " + plugin.colors.get(c1), "&7- " + plugin.colors.get(c2), "&7- " + plugin.colors.get(c3)), 
			"COLORED_ENDER_CHEST_" + (size == 27 ? "SMALL": "BIG") + "_" + c1 + "_" + c2 + "_" + c3, 
			RecipeType.ANCIENT_ALTAR,
			(size == 27) ?
				new ItemStack[] {
					new ItemStack(plugin.wool[c1]), new ItemStack(plugin.wool[c2]), new ItemStack(plugin.wool[c3]), 
					new ItemStack(Material.OBSIDIAN), new ItemStack(Material.CHEST), new ItemStack(Material.OBSIDIAN), 
					SlimefunItems.RUNE_ENDER, new ItemStack(Material.OBSIDIAN), SlimefunItems.RUNE_ENDER
				}
			:
				new ItemStack[] {
					new ItemStack(plugin.wool[c1]), new ItemStack(plugin.wool[c2]), new ItemStack(plugin.wool[c3]), 
					SlimefunItems.WITHER_PROOF_OBSIDIAN, SlimefunItem.getItem("COLORED_ENDER_CHEST_SMALL_" + c1 + "_" + c2 + "_" + c3), SlimefunItems.WITHER_PROOF_OBSIDIAN, 
					SlimefunItems.RUNE_ENDER, SlimefunItems.GOLD_24K, SlimefunItems.RUNE_ENDER
				}
		);
		
		int[] slots = IntStream.range(0, size).toArray();
		
		SlimefunItem.registerBlockHandler(getID(), new SlimefunBlockHandler() {
			
			@Override
			public void onPlace(Player p, Block b, SlimefunItem item) {
				int yaw = 0;
				
				EnderChest chest = (EnderChest) b.getBlockData();
				
				switch (chest.getFacing()) {
					case NORTH:
						yaw = 180;
						break;
					case SOUTH:
						yaw = 0;
						break;
					case WEST:
						yaw = 90;
						break;
					case EAST:
						yaw = -90;
						break;
					default:
						break;
				}
				
				BlockStorage.addBlockInfo(b, "yaw", String.valueOf(yaw));
				plugin.updateIndicator(b, c1, c2, c3, yaw + 45);
			}
			
			@Override
			public boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason) {
				plugin.removeIndicator(b);
				return true;
			}
		});
		
		new BlockMenuPreset(getID(), "&eEnder Chest", true) {
			
			@Override
			public void newInstance(BlockMenu menu, Block b) {
				// We don't need this method for our Ender Chest
			}
					
			@Override
			public void init() {
				setSize(size);
						
				addMenuOpeningHandler((p) -> {
					p.playSound(p.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1.8F, 1.6F);
				});
						
				addMenuCloseHandler((p) -> {
					p.playSound(p.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.8F, 1.6F);
				});
			}
					
			@Override
			public int[] getSlotsAccessedByItemTransport(ItemTransportFlow arg0) {
				return slots;
			}
					
			@Override
			public boolean canOpen(Block b, Player p) {
				plugin.updateIndicator(b, c1, c2, c3, Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(), "yaw")) + 45);
				return true;
			}
		};
	}

}
