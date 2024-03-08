package me.sllly.buildwands.listeners;

import me.sllly.buildwands.BuildWands;
import me.sllly.buildwands.Util.NbtApiUtils;
import me.sllly.buildwands.Util.Util;
import me.sllly.buildwands.objects.Wand;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.List;

public class BuildListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK){
            return;
        }
        if (event.getHand() == EquipmentSlot.OFF_HAND){
            return;
        }
        if (!NbtApiUtils.hasNBTKey(event.getPlayer().getInventory().getItemInMainHand(), Wand.nbtKey+"-wandid")){
            return;
        }


        Wand wand = new Wand(event.getPlayer().getInventory().getItemInMainHand());
        List<Block> blockList = wand.findBlocks(event.getPlayer());
        if (blockList == null) {
            return;
        }
        Material lookingAtMaterial = event.getPlayer().getTargetBlock(null, BuildWands.maxDistance).getType();

        if (event.getPlayer().getGameMode() == GameMode.CREATIVE){
            for (Block block : blockList) {
                if (block.getType() == Material.AIR){
                    block.setType(lookingAtMaterial);
                }
            }
            return;
        }
        if (wand.getDurability() < blockList.size() && wand.getMaxDurability() != -1){
            Util.sendMessage(event.getPlayer(), BuildWands.languageConfig.notEnoughDurability);
            return;
        }

        Integer materialAmount = wand.getMaterialAmounts().get(lookingAtMaterial);
        int requiredAmount = blockList.size();
        int combinedAmount = materialAmount != null ? materialAmount : 0; // Amount in wand
        combinedAmount += Util.countUnenchantedItemsOfMaterial(event.getPlayer(), lookingAtMaterial); // Add amount in player's inventory

        if (combinedAmount < requiredAmount) {
            // Not enough material in both wand and inventory
            Util.sendMessage(event.getPlayer(), BuildWands.languageConfig.notEnoughMaterial.replace("%material%", lookingAtMaterial.name().toLowerCase()));
            return;
        }

// Determine how much material needs to be taken from the wand
        int removeFromWand = materialAmount != null ? Math.min(materialAmount, requiredAmount) : 0;
        if (materialAmount != null) {
            wand.getMaterialAmounts().put(lookingAtMaterial, materialAmount - removeFromWand);
        }

// Update wand durability
        wand.setDurability(wand.getDurability() - requiredAmount);
        wand.updateWandItem();
        event.getPlayer().getInventory().setItemInMainHand(wand.getWandItem());

// Determine how much needs to be taken from the player's inventory
        int removeFromInventory = requiredAmount - removeFromWand;

// Remove materials from the player's inventory if needed
        if (removeFromInventory > 0) {
            Util.removeItems(event.getPlayer(), lookingAtMaterial, removeFromInventory);
        }

        for (Block block : blockList) {
            if (block.getType() == Material.AIR){
                block.setType(lookingAtMaterial);
            }
        }
    }
}
