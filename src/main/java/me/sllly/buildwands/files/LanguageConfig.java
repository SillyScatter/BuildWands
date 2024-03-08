package me.sllly.buildwands.files;

import com.octanepvp.splityosis.configsystem.configsystem.AnnotatedConfig;
import com.octanepvp.splityosis.configsystem.configsystem.ConfigField;

import java.io.File;

public class LanguageConfig extends AnnotatedConfig {
    public LanguageConfig(File parentDirectory, String name) {
        super(parentDirectory, name);
    }

    @ConfigField(path = "not-enough-material")
    public String notEnoughMaterial = "&cYou don't have enough %material% in your wand to build that!";

    @ConfigField(path = "blacklisted-item")
    public String blacklistedItemError = "&cYou can't add that material to a build wand!";

    @ConfigField(path = "too-many-materials-already")
    public String tooManyMaterials = "&cThere are already too many materials attached to this build wand.";

    @ConfigField(path = "cant-input-enchanted-stuff")
    public String cantInputEnchantedStuff = "&cYou can't input any item that has an enchant on it!";

    @ConfigField(path = "not-enough-durability")
    public String notEnoughDurability = "&cYour build wand doesn't have enough durability to do this!";
}
