package me.sllly.buildwands.listeners;

import me.sllly.buildwands.BuildWands;
import me.sllly.buildwands.Util.NbtApiUtils;
import me.sllly.buildwands.Util.Util;
import me.sllly.buildwands.menus.EditMaterialsGUI;
import me.sllly.buildwands.objects.Wand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class OpenMenuListener implements Listener {

    @EventHandler()
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_AIR) {
            return;
        }
        if ( !event.getPlayer().isSneaking()){
            return;
        }
        if (event.getHand() == EquipmentSlot.OFF_HAND) {
            return;
        }
        if (!NbtApiUtils.hasNBTKey(event.getPlayer().getInventory().getItemInMainHand(), Wand.nbtKey + "-wandid")) {
            return;
        }
        Util.sendMessage(event.getPlayer(), BuildWands.languageConfig.openingMenu);
        new EditMaterialsGUI(new Wand(event.getPlayer().getInventory().getItemInMainHand())).open(event.getPlayer());
    }
}
