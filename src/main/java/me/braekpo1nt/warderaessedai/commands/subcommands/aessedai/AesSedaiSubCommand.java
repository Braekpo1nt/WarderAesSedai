package me.braekpo1nt.warderaessedai.commands.subcommands.aessedai;

import me.braekpo1nt.warderaessedai.Main;
import me.braekpo1nt.warderaessedai.commands.interfaces.SubTabCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class AesSedaiSubCommand implements SubTabCommand {
    
    private final Main plugin;
    
    public AesSedaiSubCommand(Main plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("Please provide an argument");
            return false;
        }
        
        if (args[0].equals("add")) {
            if (args.length < 2) {
                sender.sendMessage("Please provide a player name: /wb warder add <aessedai>");
                return false;
            }

            String playerName = args[1];
            Player aesSedai = Bukkit.getServer().getPlayerExact(playerName);
            if (aesSedai == null) {
                sender.sendMessage("Can't make player \""+ playerName +"\" an Aes Sedai, they are not online");
                return false;
            }

            plugin.setAesSedai(aesSedai);
            plugin.getConfig().set(Main.AES_SEDAI_NAME, aesSedai.getName());
            plugin.saveConfig();

            sender.sendMessage(playerName + " is now an Aes Sedai");
            return true;
        } else if (args[0].equals("remove")) {
            if (plugin.getAesSedai() == null) {
                sender.sendMessage("An Aes Sedai was not set.");
                return false;
            }
            
            String aesSedaiName = plugin.getAesSedai().getName();
            plugin.setAesSedai(null);
            plugin.getConfig().set(Main.AES_SEDAI_NAME, null);
            plugin.saveConfig();
            
            sender.sendMessage(aesSedaiName + " is no longer an Aes Sedai");
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
