package me.sllly.buildwands.files;

import com.octanepvp.splityosis.configsystem.configsystem.AnnotatedConfig;
import com.octanepvp.splityosis.configsystem.configsystem.ConfigField;
import me.sllly.buildwands.Util.Util;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class EditMaterialsConfig extends AnnotatedConfig {
    public EditMaterialsConfig(File parentDirectory, String name) {
        super(parentDirectory, name);
    }

    @ConfigField(path = "general-items.menu-size")
    public int menuSize = 36;

    @ConfigField(path = "general-items.menu-title")
    public String menuTitle = "Cosmetics";

    @ConfigField(path = "general-items.border.item")
    public ItemStack borderItem = Util.createItemStack(Material.GRAY_STAINED_GLASS_PANE, 1, "&7");

    @ConfigField(path = "general-items.border.slot")
    public List<Integer> borderSlots = Arrays.asList(0,1,2,3,4,5,6,7,8,9,17,18,26,27,28,29,30,32,33,34,35);

    @ConfigField(path = "general-items.back.item")
    public ItemStack backItem = Util.createItemStack(Material.BARRIER, 1, "&eExit");

    @ConfigField(path = "general-items.back.slot")
    public int backSlot = 31;

    @ConfigField(path = "material-menu-item.name-format")
    public String materialMenuItemNameFormat = "&e%amount%x &c%material%";
}
