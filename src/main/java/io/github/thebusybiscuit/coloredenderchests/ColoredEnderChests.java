package io.github.thebusybiscuit.coloredenderchests;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.core.researching.Research;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.bstats.bukkit.Metrics;
import me.mrCookieSlime.Slimefun.cscorelib2.config.Config;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import me.mrCookieSlime.Slimefun.cscorelib2.updater.GitHubBuildsUpdater;
import me.mrCookieSlime.Slimefun.cscorelib2.updater.Updater;

public class ColoredEnderChests extends JavaPlugin implements SlimefunAddon {

    protected Config cfg;
    protected Map<Integer, String> colors = new HashMap<>();
    protected Category category;

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
            item.register(this);
            smallResearch.addItems(item);
        }

        if (cfg.getBoolean("big_chests")) {
            ColoredEnderChest item = new ColoredEnderChest(this, 54, c1, c2, c3);
            item.register(this);
            bigResearch.addItems(item);
        }
    }

    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Override
    public String getBugTrackerURL() {
        return "https://github.com/TheBusyBiscuit/ColoredEnderChests/issues";
    }
}
