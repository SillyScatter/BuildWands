package me.sllly.buildwands.commands.arguments;

import com.octanepvp.splityosis.commandsystem.SYSArgument;
import com.octanepvp.splityosis.commandsystem.SYSCommand;
import me.sllly.buildwands.objects.Wand;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WandArgument extends SYSArgument {
    @Override
    public boolean isValid(String s) {
        return Wand.definedWands.containsKey(s);
    }

    @Override
    public List<String> getInvalidInputMessage(String s) {
        return Arrays.asList("That type of build wand is not recognized.");
    }

    @Override
    public @NonNull List<String> tabComplete(CommandSender sender, SYSCommand command, String input) {
        List<String> results = new ArrayList<>();
        for (String s : Wand.definedWands.keySet()) {
            if (s.startsWith(input)){
                results.add(s);
            }
        }
        return results;
    }
}
