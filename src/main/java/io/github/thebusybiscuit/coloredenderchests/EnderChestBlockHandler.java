package io.github.thebusybiscuit.coloredenderchests;

import org.bukkit.block.Block;
import org.bukkit.block.data.type.EnderChest;
import org.bukkit.entity.Player;

import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

class EnderChestBlockHandler implements SlimefunBlockHandler {

    private final int c1;
    private final int c2;
    private final int c3;

    EnderChestBlockHandler(int c1, int c2, int c3) {
        this.c1 = c1;
        this.c2 = c2;
        this.c3 = c3;
    }

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
        ColorIndicator.updateIndicator(b, c1, c2, c3, yaw + 45);
    }

    @Override
    public boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason) {
        ColorIndicator.removeIndicator(b);
        return true;
    }

}
