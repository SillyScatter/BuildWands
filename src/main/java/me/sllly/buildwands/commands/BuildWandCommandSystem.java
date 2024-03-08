package me.sllly.buildwands.commands;

import com.octanepvp.splityosis.commandsystem.SYSCommand;
import com.octanepvp.splityosis.commandsystem.SYSCommandBranch;
import com.octanepvp.splityosis.commandsystem.arguments.IntegerArgument;
import com.octanepvp.splityosis.commandsystem.arguments.PlayerArgument;
import me.sllly.buildwands.BuildWands;
import me.sllly.buildwands.Util.Util;
import me.sllly.buildwands.commands.arguments.WandArgument;
import me.sllly.buildwands.objects.Wand;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BuildWandCommandSystem extends SYSCommandBranch {
    public BuildWandCommandSystem(String... names) {
        super(names);
        setPermission("buildwand.admin");

        addCommand(new SYSCommand("give")
                .setArguments(new PlayerArgument(), new WandArgument())
                .setUsage("/buildwand give player-name wand-type")
                .executes((commandSender, strings) -> {
                    Player player = Bukkit.getPlayer(strings[0]);
                    if (Util.countEmptySlots(player) == 0){
                        Util.sendMessage(commandSender, "&cThat player doesn't have an empty inventory slot, so they didn't receive the item.");
                        return;
                    }
                    Util.giveItemsToPlayer(player, Wand.getWand(strings[1]), 1);
                    Util.sendMessage(commandSender, "&aSuccessfully given " + player.getName() + " a wand!");
                }));

        addCommand(new SYSCommand("reload")
                .setArguments()
                .executes((commandSender, strings) -> {
                    BuildWands.plugin.reloadWands();
                    Util.sendMessage(commandSender, "&aBuildWands reloaded!");
                }));
    }
}
