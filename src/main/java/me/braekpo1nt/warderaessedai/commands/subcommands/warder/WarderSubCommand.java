package me.braekpo1nt.warderaessedai.commands.subcommands.warder;

import me.braekpo1nt.warderaessedai.Main;
import me.braekpo1nt.warderaessedai.commands.interfaces.SubCommand;
import me.braekpo1nt.warderaessedai.commands.interfaces.SubTabCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class WarderSubCommand implements SubTabCommand {

    private final Main plugin;
    
    public WarderSubCommand(Main plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("please provide an argument");
            return false;
        }
        
        if (args[0].equals("add")) {
            if (args.length < 2) {
                sender.sendMessage("Please provide a player name: /wb warder add <warder>");
                return false;
            }
            
            String playerName = args[1];
            Player warder = Bukkit.getServer().getPlayerExact(playerName);
            if (warder == null) {
                sender.sendMessage("Can't make player \""+ playerName +"\" a warder, they are not online");
                return false;
            }
            
            plugin.setWarder(warder);
            plugin.getConfig().set(Main.WARDER_NAME, warder.getName());
            plugin.saveConfig();
            
            sender.sendMessage(playerName + " is now a Warder");
            return true;
            
        } else if (args[0].equals("remove")) {
            if (plugin.getWarder() == null) {
                sender.sendMessage("A warder was not set.");
                return false;
            }
            
            String warderName = plugin.getWarder().getName();
            plugin.setWarder(null);
            plugin.getConfig().set(Main.WARDER_NAME, null);
            plugin.saveConfig();

            sender.sendMessage(warderName + " is no longer a Warder");
            return true;
        }

        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("add", "remove");
        }
        return null;
    }
}
