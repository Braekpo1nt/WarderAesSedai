package me.braekpo1nt.warderaessedai.listeners;

import me.braekpo1nt.warderaessedai.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;

public class AesSedaiListener implements Listener {

    private final Main plugin;
    
    private final int healCooldownStart = 2;
    private int healCoolDown = 0;
    
    public AesSedaiListener(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
        
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (healCoolDown <= 0) {
                    return;
                }
                healCoolDown -= 1;
            }
        }, 0, 20);
    }
    
    @EventHandler
    public void onRightClickWarder(PlayerInteractEntityEvent event) {
        Player warder = plugin.getWarder();
        Player aesSedai = plugin.getAesSedai();
        if (warder == null || aesSedai == null) {
            return;
        }
        if (aesSedai.getInventory().getItemInMainHand().getType() == Material.AIR) {
            Bukkit.getLogger().info("Interact entity");
            if (healCoolDown <= 0) {
                if (event.getPlayer().equals(aesSedai) && event.getRightClicked().equals(warder)) {
                    if (aesSedai.getFoodLevel() > 1) {
                        if (warder.getHealth() < 20) {
                            warder.setHealth(warder.getHealth() + 1);
                            aesSedai.setFoodLevel(aesSedai.getFoodLevel() - 1);
                            healCoolDown = healCooldownStart;
                        }
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onPlayerDamageEntity(EntityDamageByEntityEvent event) {
        Player aesSedai = plugin.getAesSedai();
        if (aesSedai == null) {
            return;
        }
        if (event.getDamager().equals(aesSedai)) {
            if (!event.getEntity().getType().equals(EntityType.ENDER_DRAGON)) {
                event.setCancelled(true);
            }
        }
    }
    
    /**
     * If a player logs in and matches the warder, set the warder to them
     * @param event
     */
    @EventHandler
    public void aesSedaiJoin(PlayerJoinEvent event) {
        String aesSedaiName = plugin.getConfig().getString(Main.AES_SEDAI_NAME);
        if (aesSedaiName != null && event.getPlayer().getName().equals(aesSedaiName)) {
            plugin.setAesSedai(event.getPlayer());
        }
    }
    
    @EventHandler
    public void onAesSedaiRegainHealth(EntityRegainHealthEvent event) {
        Player warder = plugin.getWarder();
        Player aesSedai = plugin.getAesSedai();
        if (warder == null || aesSedai == null) {
            return;
        }
        if (event.getEntity().equals(aesSedai)) {
            if (warder.getHealth() < 20) {
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onAesSedaiRespawn(PlayerRespawnEvent event) {
        Player aesSedai = plugin.getAesSedai();
        if (aesSedai != null && event.getPlayer().equals(aesSedai)) {
            plugin.giveAesSedaiCompass();
        }
    }
    
    @EventHandler
    public void onAesSedaiDeath(PlayerDeathEvent event) {
        Player aesSedai = plugin.getAesSedai();
        if (aesSedai != null && event.getEntity().equals(aesSedai)) {
            Iterator<ItemStack> iterator = event.getDrops().iterator();
            while (iterator.hasNext()) {
                if (iterator.next().getType().equals(Material.COMPASS)) {
                    iterator.remove();
                }
            }
        }
    }
}
