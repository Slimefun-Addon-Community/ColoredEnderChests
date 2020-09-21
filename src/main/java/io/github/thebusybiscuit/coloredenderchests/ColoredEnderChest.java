package io.github.thebusybiscuit.coloredenderchests;

import java.util.stream.IntStream;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.EnderChest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import me.mrCookieSlime.Slimefun.cscorelib2.materials.MaterialCollections;

public class ColoredEnderChest extends SlimefunItem {

    public ColoredEnderChest(ColoredEnderChests plugin, int size, int c1, int c2, int c3) {
        super(plugin.category, new SlimefunItemStack("COLORED_ENDER_CHEST_" + (size == 27 ? "SMALL" : "BIG") + "_" + c1 + "_" + c2 + "_" + c3, Material.ENDER_CHEST, "&eColored Ender Chest &7(" + (size == 27 ? "Small" : "Big") + ")", "", "&7Size: &e" + size, "", "&7- " + plugin.colors.get(c1), "&7- " + plugin.colors.get(c2), "&7- "
                + plugin.colors.get(c3)), RecipeType.ANCIENT_ALTAR, (size == 27) ? new ItemStack[] { new ItemStack(MaterialCollections.getAllWoolColors().get(c1)), new ItemStack(MaterialCollections.getAllWoolColors().get(c2)), new ItemStack(MaterialCollections.getAllWoolColors().get(c3)), new ItemStack(Material.OBSIDIAN), new ItemStack(Material.CHEST), new ItemStack(Material.OBSIDIAN), SlimefunItems.ENDER_RUNE, new ItemStack(Material.OBSIDIAN), SlimefunItems.ENDER_RUNE } : new ItemStack[] { new ItemStack(MaterialCollections.getAllWoolColors().get(c1)), new ItemStack(MaterialCollections
                        .getAllWoolColors().get(c2)), new ItemStack(MaterialCollections.getAllWoolColors().get(c3)), SlimefunItems.WITHER_PROOF_OBSIDIAN, getSmallerEnderChest(c1, c2, c3), SlimefunItems.WITHER_PROOF_OBSIDIAN, SlimefunItems.ENDER_RUNE, SlimefunItems.GOLD_24K, SlimefunItems.ENDER_RUNE });

        int[] slots = IntStream.range(0, size).toArray();

        SlimefunItem.registerBlockHandler(getID(), new EnderChestBlockHandler(c1, c2, c3));

        new BlockMenuPreset(getID(), "&eEnder Chest", true) {

            @Override
            public void init() {
                setSize(size);

                addMenuOpeningHandler(p -> p.playSound(p.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1.8F, 1.6F));

                addMenuCloseHandler(p -> p.playSound(p.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.8F, 1.6F));
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow arg0) {
                return slots;
            }

            @Override
            public boolean canOpen(Block b, Player p) {
                String data = BlockStorage.getLocationInfo(b.getLocation(), "yaw");
                int yaw = 0;

                if (data != null) {
                    yaw = Integer.parseInt(data);
                }
                else if (b.getType() == Material.ENDER_CHEST) {
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
                }

                ColorIndicator.updateIndicator(b, c1, c2, c3, yaw + 45);
                return true;
            }
        };
    }

    private static ItemStack getSmallerEnderChest(int c1, int c2, int c3) {
        SlimefunItem enderChest = SlimefunItem.getByID("COLORED_ENDER_CHEST_SMALL_" + c1 + "_" + c2 + "_" + c3);
        return enderChest != null ? enderChest.getItem() : null;
    }

}
