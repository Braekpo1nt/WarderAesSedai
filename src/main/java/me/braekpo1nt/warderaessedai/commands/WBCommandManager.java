package me.braekpo1nt.warderaessedai.commands;

import me.braekpo1nt.warderaessedai.Main;
import me.braekpo1nt.warderaessedai.commands.interfaces.CommandManager;
import me.braekpo1nt.warderaessedai.commands.interfaces.SubCommand;
import me.braekpo1nt.warderaessedai.commands.interfaces.SubTabCommand;
import me.braekpo1nt.warderaessedai.commands.subcommands.aessedai.AesSedaiSubCommand;
import me.braekpo1nt.warderaessedai.commands.subcommands.warder.WarderSubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class WBCommandManager implements CommandManager {

    private final Main plugin;
    
    public WBCommandManager(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("wb").setExecutor(this);
        subCommands.put("warder", new WarderSubCommand(plugin));
        subCommands.put("aessedai", new AesSedaiSubCommand(plugin));
        subCommands.put("list", new SubCommand() {
            @Override
            public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
                if (plugin.getWarder() != null)
                sender.sendMessage("Warder: " + plugin.getWarder().getName());
                return true;
            }
        });
    }
    
    /**
     * Run the onCommand() method of the subCommand whose key in the subCommands list
     * matches the first element in args
     * @param sender
     * @param command
     * @param label
     * @param args
     * @return
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("Please provide an argument.");
            return false;
        }
        
        if (!subCommands.containsKey(args[0])) {
            sender.sendMessage("Argument \"" + args[0] + "\" is not recognized.");
            return false;
        }
        
        return subCommands.get(args[0]).onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
    }
    
    /**
     * Returns a list of this CommandManager's SubCommands for tab completion.
     * @param sender
     * @param command
     * @param alias
     * @param args
     * @return The list of this CommandManager's SubCommand names or its subCommands'
     * onTabComplete() methods
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> subCommandNames = new ArrayList<>(subCommands.keySet());
            subCommandNames.sort(Comparator.naturalOrder());
            return subCommandNames;
        } else if (args.length > 1) {
            //return the arguments of the subcommand, if any exist
            if (subCommands.containsKey(args[0])) {
                SubCommand subCommand = subCommands.get(args[0]);
                if (subCommand instanceof SubTabCommand) {
                    SubTabCommand subTabCommand = (SubTabCommand) subCommand;
                    return subTabCommand.onTabComplete(sender, command, alias, Arrays.copyOfRange(args, 1, args.length));
                }
            } else {
                return null;
            }
        }
        return null;
    }
}
