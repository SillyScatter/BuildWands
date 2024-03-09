package me.sllly.buildwands.menus;

import com.octanepvp.splityosis.menulib.MenuItem;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

public class ExitMenuButton extends MenuItem {
    public ExitMenuButton(ItemStack displayItem) {
        super(displayItem);
        this.executes((event, menu) -> {
            event.getWhoClicked().closeInventory();
        });
    }

    public ExitMenuButton(ItemStack displayItem, Sound sound) {
        super(displayItem, sound);
        this.executes((event, menu) -> {
            event.getWhoClicked().closeInventory();
        });
    }

    public ExitMenuButton(ItemStack displayItem, Sound sound, float soundVolume, float soundPitch) {
        super(displayItem, sound, soundVolume, soundPitch);
        this.executes((event, menu) -> {
            event.getWhoClicked().closeInventory();
        });
    }
}