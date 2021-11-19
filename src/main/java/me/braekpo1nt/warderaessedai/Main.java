package me.braekpo1nt.warderaessedai;

import me.braekpo1nt.warderaessedai.commands.WBCommandManager;
import me.braekpo1nt.warderaessedai.listeners.AesSedaiListener;
import me.braekpo1nt.warderaessedai.listeners.WarderListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class Main extends JavaPlugin {
    
    public static final String WARDER_NAME = "warder";
    public static final String AES_SEDAI_NAME = "aes_sedai";
    
    private Player warder;
    private Player aesSedai;
    private HealthScoreboardManager healthScoreboardManager;
    
    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        
        String warderName = this.getConfig().getString(Main.WARDER_NAME);
        if (warderName != null) {
            Player warderPlayer = Bukkit.getServer().getPlayerExact(warderName);
            if (warderPlayer != null) {
                setWarder(warderPlayer);
            }
        }
        String aesSedaiName = this.getConfig().getString(Main.AES_SEDAI_NAME);
        if (aesSedaiName != null) {
            Player aesSedaiPlayer = Bukkit.getServer().getPlayerExact(aesSedaiName);
            if (aesSedaiPlayer != null) {
                setAesSedai(aesSedaiPlayer);
            }
        }
        
        new WBCommandManager(this);
        
        new AesSedaiListener(this);
        new WarderListener(this);
        
        this.healthScoreboardManager = new HealthScoreboardManager(this);
        
        cast();
    }
    
    public Player getWarder() {
        return this.warder;
    }
    
    public void setWarder(Player warder) {
        this.warder = warder;
    }
    
    public Player getAesSedai() {
        return this.aesSedai;
    }
    
    public void setAesSedai(Player aesSedai) {
        this.aesSedai = aesSedai;
    }
    
    private void cast() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                Player warder = getWarder();
                if (warder == null) {
                    return;
                }
                warder.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 3*20, 0, false, false));
                warder.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 3*20, 0, false, false));
            }
        }, 0, 20);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                if (warder == null || aesSedai == null) {
                    return;
                }
                
                warder.setCompassTarget(aesSedai.getLocation());
                aesSedai.setCompassTarget(warder.getLocation());
            }
        }, 0, 20);
        
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                healthScoreboardManager.update();
            }
        }, 0, 1);
    }
    
    public void giveWarderCompass() {
        if (warder == null) {
            return;
        }
        warder.getInventory().remove(Material.COMPASS);
        warder.getInventory().addItem(new ItemStack(Material.COMPASS));
        if (aesSedai != null) {
            warder.setCompassTarget(aesSedai.getLocation());
        }
    }
    
    public void giveAesSedaiCompass() {
        if (aesSedai == null) {
            return;
        }
        aesSedai.getInventory().remove(Material.COMPASS);
        aesSedai.getInventory().addItem(new ItemStack(Material.COMPASS));
        if (warder != null) {
            aesSedai.setCompassTarget(warder.getLocation());
        }
    }
    
    @Override
    public void onDisable() {
        if (warder != null) {
            this.getConfig().set(Main.WARDER_NAME, warder.getName());
        }
    }
}
