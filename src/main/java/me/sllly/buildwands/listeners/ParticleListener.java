package me.sllly.buildwands.listeners;

import me.sllly.buildwands.Util.NbtApiUtils;
import me.sllly.buildwands.Util.Util;
import me.sllly.buildwands.objects.Wand;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.List;

public class ParticleListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }
        if (event.getHand() == EquipmentSlot.OFF_HAND) {
            return;
        }
        if (!NbtApiUtils.hasNBTKey(event.getPlayer().getInventory().getItemInMainHand(), Wand.nbtKey + "-wandid")) {
            //Util.broadcast("no");

//            List<String> debug = NbtApiUtils.getNBTKeys(event.getPlayer().getInventory().getItemInMainHand());
//            for (String s : debug) {
//                Util.broadcast(s);
//            }
            return;
        }
        event.setCancelled(true);

        Wand wand = new Wand(event.getPlayer().getInventory().getItemInMainHand());
        List<Block> blockList = wand.findBlocks(event.getPlayer());
        if (blockList == null) {
            return;
        }
        World world = event.getPlayer().getWorld();
        for (Block block : blockList) {
            int x = block.getX();
            int y = block.getY();
            int z = block.getZ();

            world.spawnParticle(Particle.ELECTRIC_SPARK, x, y, z, 1,0,0,0,0);
            world.spawnParticle(Particle.ELECTRIC_SPARK, x, y, z+1, 1,0,0,0,0);
            world.spawnParticle(Particle.ELECTRIC_SPARK, x, y+1, z, 1,0,0,0,0);
            world.spawnParticle(Particle.ELECTRIC_SPARK, x, y+1, z+1, 1,0,0,0,0);
            world.spawnParticle(Particle.ELECTRIC_SPARK, x+1, y, z, 1,0,0,0,0);
            world.spawnParticle(Particle.ELECTRIC_SPARK, x+1, y, z+1, 1,0,0,0,0);
            world.spawnParticle(Particle.ELECTRIC_SPARK, x+1, y+1, z, 1,0,0,0,0);
            world.spawnParticle(Particle.ELECTRIC_SPARK, x+1, y+1, z+1, 1,0,0,0,0);
        }
    }
}
