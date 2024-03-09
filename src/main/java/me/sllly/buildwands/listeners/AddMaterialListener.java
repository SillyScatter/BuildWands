package me.sllly.buildwands.listeners;

import me.sllly.buildwands.BuildWands;
import me.sllly.buildwands.Util.NbtApiUtils;
import me.sllly.buildwands.Util.Util;
import me.sllly.buildwands.objects.Wand;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class AddMaterialListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getWhoClicked().getGameMode() == GameMode.CREATIVE){
            return;
        }
        if (e.getClickedInventory() == null){
            return;
        }
        if (e.getClickedInventory().getType() != InventoryType.PLAYER){
            return;
        }
        if (e.getCursor() != null && e.getCursor().getType() != Material.AIR) {
            ItemStack cursorItem = e.getCursor();
            ItemStack clickedItem = e.getCurrentItem();

            if (!NbtApiUtils.hasNBTKey(clickedItem, Wand.nbtKey+"-wandid")){
                return;
            }
            if (BuildWands.generalConfig.blacklistedMaterials.contains(cursorItem.getType().toString())){
                Util.sendMessage(e.getWhoClicked(), BuildWands.languageConfig.blacklistedItemError);
                return;
            }
            if (Util.isItemEnchanted(cursorItem)){
                Util.sendMessage(e.getWhoClicked(), BuildWands.languageConfig.cantInputEnchantedStuff);
                return;
            }
            Wand wand = new Wand(clickedItem);
            Integer materialAmount = wand.getMaterialAmounts().get(cursorItem.getType());

            if (wand.getMaterialAmounts().size() >= wand.getMaxUniqueMaterials() && materialAmount == null){
                Util.sendMessage(e.getWhoClicked(), BuildWands.languageConfig.tooManyMaterials);
                return;
            }

            if (materialAmount == null) {
                materialAmount = 0;
            }
            Map<Material, Integer> materialAmounts = wand.getMaterialAmounts();
            materialAmounts.put(cursorItem.getType(), materialAmount+ cursorItem.getAmount());
            wand.setMaterialAmounts(materialAmounts);
            wand.updateWandItem();
            cursorItem.setAmount(0);
            cursorItem.setType(Material.AIR);
            e.setCancelled(true);
            int index = e.getSlot();
            e.getWhoClicked().getInventory().setItem(index, wand.getWandItem());
        }
    }
}
