package me.sllly.buildwands;

import me.sllly.buildwands.Util.Util;
import me.sllly.buildwands.commands.BuildWandCommandSystem;
import me.sllly.buildwands.files.WandConfig;
import me.sllly.buildwands.objects.Wand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class BuildWands extends JavaPlugin {

    public static BuildWands plugin;

    private File wandDirectory;

    @Override
    public void onEnable() {
        plugin = this;

        new BuildWandCommandSystem("buildwands", "bw").registerCommandBranch(this);

        initializeDirectories();
        reloadWands();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void initializeDirectories(){
        wandDirectory = new File(getDataFolder(), "Titles");
        if (!wandDirectory.exists()) {
            wandDirectory.mkdirs();
        }
    }

    public void reloadWands(){
        int count = 0;
        Wand.definedWands.clear();
        File[] wandFiles = wandDirectory.listFiles();
        if (wandFiles == null || wandFiles.length == 0){
            new WandConfig(new File(wandDirectory, "default-wand.yml")).initialize();
        }
        wandFiles = wandDirectory.listFiles();
        for (File wandFile : wandFiles) {
            WandConfig wandConfig = new WandConfig(wandFile);
            wandConfig.initialize();

            Wand wand = new Wand(wandConfig.wandGroupId, wandConfig.maxRadius, wandConfig.maxDurability, wandConfig.maxUniqueMaterials, wandConfig.wandItem);
            Wand.definedWands.put(wand.getWandGroupId(), wand);
            count++;
        }
        Util.log("&aSuccessfully registered wands: &2&l"+count);
    }

}
