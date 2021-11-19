package me.braekpo1nt.warderaessedai.commands.interfaces;

import org.bukkit.command.TabExecutor;

import java.util.HashMap;
import java.util.Map;

/**
 * This interface is implemented by command managers looking to manage
 * a set of sub commands, e.g. objects that implement the {
 * @link SubCommand} interface.
 */
public interface CommandManager extends TabExecutor {

    /**
     * Maps SubComands by their name. 
     */
    Map<String, SubCommand> subCommands = new HashMap<>();
    
}
