package me.sllly.buildwands;

import me.sllly.buildwands.commands.BuildWandCommandSystem;
import org.bukkit.plugin.java.JavaPlugin;

public final class BuildWands extends JavaPlugin {

    @Override
    public void onEnable() {
        new BuildWandCommandSystem("buildwands", "bw").registerCommandBranch(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
