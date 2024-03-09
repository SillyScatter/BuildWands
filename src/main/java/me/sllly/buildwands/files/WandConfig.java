package me.sllly.buildwands.files;

import com.octanepvp.splityosis.configsystem.configsystem.AnnotatedConfig;
import com.octanepvp.splityosis.configsystem.configsystem.ConfigField;
import com.octanepvp.splityosis.configsystem.configsystem.InvalidConfigFileException;
import me.sllly.buildwands.Util.Util;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class WandConfig extends AnnotatedConfig {
    public WandConfig(File file) throws InvalidConfigFileException {
        super(file);
    }

    @ConfigField(path = "wand-group-id", comment = "must not have any spaces")
    public String wandGroupId = "default-group";

    @ConfigField(path = "max-radius")
    public int maxRadius = 10;

    @ConfigField(path = "max-durability")
    public int maxDurability = 23040;

    @ConfigField(path = "max-unique-materials")
    public int maxUniqueMaterials = 10;

    @ConfigField(path = "wand-item", comment = "placeholders are %radius% and %durability% and %materials%")
    public ItemStack wandItem = Util.createItemStack(Material.BONE, 1, "&7&lDefault Build Wand",
            "&4*&7 Radius: &c%radius%", "&4*&7 Durability: &c%durability%", "&7-", "%materials%");
}
