package me.sllly.buildwands.files;

import com.octanepvp.splityosis.configsystem.configsystem.AnnotatedConfig;
import com.octanepvp.splityosis.configsystem.configsystem.ConfigField;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class GeneralConfig extends AnnotatedConfig {
    public GeneralConfig(File parentDirectory, String name) {
        super(parentDirectory, name);
    }

    @ConfigField(path = "blacklisted-materials")
    public List<String> blacklistedMaterials = Arrays.asList("TNT", "DIAMOND_BLOCK");

    @ConfigField(path = "material-lore-format")
    public String materialLoreFormat = "&4*&7 %material%: &c%amount%";
}
