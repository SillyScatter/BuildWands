package me.sllly.buildwands.listeners;

import me.sllly.buildwands.BuildWands;
import me.sllly.buildwands.Util.NbtApiUtils;
import me.sllly.buildwands.Util.Util;
import me.sllly.buildwands.menus.EditMaterialsGUI;
import me.sllly.buildwands.objects.Wand;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class AddMaterialViaGUIListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = player.getOpenInventory().getTopInventory();
        InventoryHolder inventoryHolder = inventory.getHolder();
        if (inventoryHolder == null) return;
        if (!(inventoryHolder instanceof EditMaterialsGUI)) return;
        Inventory clickedInv = event.getClickedInventory();
        if (clickedInv == null) return;
        if (clickedInv.equals(inventory)) return;
        if (clickedInv.getType() != InventoryType.PLAYER) return;

        if (event.getCurrentItem() == null){
            return;
        }

        if (!NbtApiUtils.hasNBTKey(player.getInventory().getItemInMainHand(), Wand.nbtKey + "-wandid")) {
            Util.sendMessage(player, BuildWands.languageConfig.antiDupe);
            player.closeInventory();
            return;
        }

        EditMaterialsGUI editMaterialsGUI = (EditMaterialsGUI) inventoryHolder;
        Wand wand = editMaterialsGUI.getWand();
        if (canItemBeAdded(wand, event.getCurrentItem(), player)){
            addItem(wand, event.getCurrentItem());

            wand.updateWandItem();
            player.getInventory().setItemInMainHand(wand.getWandItem());
            new EditMaterialsGUI(wand).open(player);
            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
        }
    }

    public static boolean canItemBeAdded(Wand wand, ItemStack itemStack, Player player){
        if (BuildWands.generalConfig.blacklistedMaterials.contains(itemStack.getType().toString())){
            Util.sendMessage(player, BuildWands.languageConfig.blacklistedItemError);
            return false;
        }
        if (itemStack.hasItemMeta()){
            Util.sendMessage(player, BuildWands.languageConfig.cantInputEnchantedStuff);
            return false;
        }

        Integer materialAmount = wand.getMaterialAmounts().get(itemStack.getType());

        if (wand.getMaterialAmounts().size() >= wand.getMaxUniqueMaterials() && materialAmount == null){
            Util.sendMessage(player, BuildWands.languageConfig.tooManyMaterials);
            return false;
        }
        return true;
    }

    public static void addItem(Wand wand, ItemStack itemStack){
        Integer materialAmount = wand.getMaterialAmounts().get(itemStack.getType());

        if (materialAmount == null) {
            materialAmount = 0;
        }
        Map<Material, Integer> materialAmounts = wand.getMaterialAmounts();
        materialAmounts.put(itemStack.getType(), materialAmount+ itemStack.getAmount());
        wand.setMaterialAmounts(materialAmounts);
        wand.updateWandItem();
        itemStack.setAmount(0);
        itemStack.setType(Material.AIR);
    }
}
