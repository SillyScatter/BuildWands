package me.sllly.buildwands.commands;

import com.octanepvp.splityosis.commandsystem.SYSCommand;
import com.octanepvp.splityosis.commandsystem.SYSCommandBranch;
import com.octanepvp.splityosis.commandsystem.arguments.IntegerArgument;
import me.sllly.buildwands.Util.Util;
import me.sllly.buildwands.objects.Wand;
import org.bukkit.block.Block;

import java.util.List;

public class BuildWandCommandSystem extends SYSCommandBranch {
    public BuildWandCommandSystem(String... names) {
        super(names);
        setPermission("buildwand.admin");

//        addCommand(new SYSCommand("test")
//                .setArguments(new IntegerArgument())
//                .executesPlayer((player, strings) -> {
//                    int radius = Integer.parseInt(strings[0]);
//                    Wand wand = new Wand();
//                    List<Block> blocks = wand.findBlocks(player, radius, 5);
//                    Util.sendMessage(player, "&aBlocklist Size: "+blocks.size());
//                    for (Block block : blocks) {
//                        block.setType(player.getTargetBlock(null, 5).getType());
//                    }
//                    Util.sendMessage(player, "&aDone!");
//                }));
    }
}
