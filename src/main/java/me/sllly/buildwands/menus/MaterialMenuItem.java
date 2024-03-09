package me.sllly.buildwands.menus;

import com.octanepvp.splityosis.menulib.MenuItem;
import me.sllly.buildwands.BuildWands;
import me.sllly.buildwands.Util.Util;
import me.sllly.buildwands.objects.Wand;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MaterialMenuItem extends MenuItem {
    private final Material material;
    private final int amount;
    public MaterialMenuItem(Wand wand, Material material, int amount) {
        super(null, Sound.BLOCK_DISPENSER_FAIL);
        this.material = material;
        this.amount = amount;
        executes((inventoryClickEvent, menu) -> {
            Player player = (Player) inventoryClickEvent.getWhoClicked();
            int availableSlots = Util.countEmptySlots(player);

        });
    }

    @Override
    public ItemStack getDisplayItem() {
        ItemStack itemStack = Util.createItemStack(material, 1, BuildWands.editMaterialsConfig.materialMenuItemNameFormat);
        itemStack = Util.replaceTextInItem(itemStack, "%amount%", Util.formatInt(amount));
        itemStack = Util.replaceTextInItem(itemStack, "%material%", Util.formatMaterialName(material));

        return itemStack;
    }
}
