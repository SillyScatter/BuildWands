package me.sllly.buildwands;

import me.sllly.buildwands.Util.Util;
import me.sllly.buildwands.commands.BuildWandCommandSystem;
import me.sllly.buildwands.files.EditMaterialsConfig;
import me.sllly.buildwands.files.GeneralConfig;
import me.sllly.buildwands.files.LanguageConfig;
import me.sllly.buildwands.files.WandConfig;
import me.sllly.buildwands.listeners.AddMaterialListener;
import me.sllly.buildwands.listeners.BuildListener;
import me.sllly.buildwands.listeners.ParticleListener;
import me.sllly.buildwands.objects.Wand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class BuildWands extends JavaPlugin {

    public static BuildWands plugin;
    public static int maxDistance = 5;
    public static LanguageConfig languageConfig;
    public static GeneralConfig generalConfig;
    public static EditMaterialsConfig editMaterialsConfig;

    private File wandDirectory;


    @Override
    public void onEnable() {
        plugin = this;

        new BuildWandCommandSystem("buildwands", "bw").registerCommandBranch(this);
        getServer().getPluginManager().registerEvents(new BuildListener(), this);
        getServer().getPluginManager().registerEvents(new ParticleListener(), this);
        getServer().getPluginManager().registerEvents(new AddMaterialListener(), this);

        initializeDirectories();
        reloadConfigs();
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

    public void reloadConfigs(){
        languageConfig = new LanguageConfig(getDataFolder(), "language");
        languageConfig.initialize();
        generalConfig = new GeneralConfig(getDataFolder(), "General-Config");
        generalConfig.initialize();
        editMaterialsConfig = new EditMaterialsConfig(getDataFolder(), "edit-materials-config");
        editMaterialsConfig.initialize();
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
