package me.braekpo1nt.warderaessedai;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class HealthScoreboardManager {

    private final Main plugin;
    
    
    public HealthScoreboardManager(Main plugin) {
        this.plugin = plugin;
        update();
    }
    
    public void update() {
        Player warder = plugin.getWarder();
        Player aesSedai = plugin.getAesSedai();
        if (warder == null || aesSedai == null) {
            return;
        }
        String displayName = ((int) warder.getLocation().distance(aesSedai.getLocation())) + "m";
        
        Scoreboard warderBoard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective warderDistObj = warderBoard.registerNewObjective("aessedaihealth", "dummy", ChatColor.GRAY + displayName);
        warderDistObj.setDisplaySlot(DisplaySlot.SIDEBAR);
        
        Score wLine1 = warderDistObj.getScore(ChatColor.RED + healthString(aesSedai.getHealth()));
        wLine1.setScore(1);
        warder.setScoreboard(warderBoard);
        
        
        Scoreboard aesSedaiBoard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective aesSedaiDistObj = aesSedaiBoard.registerNewObjective("warderhealth", "dummy", ChatColor.GRAY + displayName);
        aesSedaiDistObj.setDisplaySlot(DisplaySlot.SIDEBAR);
        
        Score aLine1 = aesSedaiDistObj.getScore(ChatColor.RED + healthString(warder.getHealth()));
        aLine1.setScore(1);
        aesSedai.setScoreboard(aesSedaiBoard);
    }
    
    private String healthString(double health) {
        int healthInt = (int) health/2;
        return ChatColor.RED + StringUtils.repeat("❤", healthInt) + ChatColor.BLACK + StringUtils.repeat("❤", 10 - healthInt);
        
    }
    
    
}
