package io.github.thebusybiscuit.coloredenderchests;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;

class EnderChestBlockHandler implements SlimefunBlockHandler {

    @Override
    public boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason) {
        ;
        return true;
    }

}
