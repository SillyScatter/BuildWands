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
        if (wand.getDurability() < blockList.size()){
            Util.sendMessage(event.getPlayer(), BuildWands.languageConfig.notEnoughDurability);
            return;
        }

        Integer materialAmount = wand.getMaterialAmounts().get(lookingAtMaterial);
        if (materialAmount == null || blockList.size() > materialAmount) {
            Util.sendMessage(event.getPlayer(), BuildWands.languageConfig.notEnoughMaterial.replace("%material%", lookingAtMaterial.name().toLowerCase()));
            return;
        }
        wand.getMaterialAmounts().put(lookingAtMaterial, materialAmount-blockList.size());
        wand.setDurability(wand.getDurability()-blockList.size());
        wand.updateWandItem();
        event.getPlayer().getInventory().setItemInMainHand(wand.getWandItem());

        for (Block block : blockList) {
            if (block.getType() == Material.AIR){
                block.setType(lookingAtMaterial);
            }
        }
    }
}
