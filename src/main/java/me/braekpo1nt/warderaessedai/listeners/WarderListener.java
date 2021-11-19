package me.braekpo1nt.warderaessedai.listeners;

import me.braekpo1nt.warderaessedai.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;

public class WarderListener implements Listener {
    
    private final Main plugin;
    
    public WarderListener(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void warderBreakBlock(BlockBreakEvent event) {
        if (event.getPlayer().equals(plugin.getWarder())) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void warderPlaceBlock(BlockPlaceEvent event) {
        if (event.getPlayer().equals(plugin.getWarder())) {
            event.setCancelled(true);
        }
    }
    
    /**
     * If a player logs in and matches the warder, set the warder to them
     * @param event
     */
    @EventHandler
    public void warderJoin(PlayerJoinEvent event) {
        String warderName = plugin.getConfig().getString(Main.WARDER_NAME);
        if (warderName != null && event.getPlayer().getName().equals(warderName)) {
            plugin.setWarder(event.getPlayer());
        }
    }
    
    @EventHandler
    public void onWarderRegainHealth(EntityRegainHealthEvent event) {
        Player warder = plugin.getWarder();
        Player aesSedai = plugin.getAesSedai();
        if (warder == null || aesSedai == null) {
            return;
        }
        if (event.getEntity().equals(warder)) {
            if (warder.getHealth() >= 9) {
                if (aesSedai.getHealth() < 20) {
                    double value = aesSedai.getHealth() + event.getAmount();
                    if (value > 20) {
                        aesSedai.setHealth(20);
                    } else {
                        aesSedai.setHealth(value);
                    }
                    event.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler
    public void onWarderRespawn(PlayerRespawnEvent event) {
        Player warder = plugin.getWarder();
        if (warder != null && event.getPlayer().equals(warder)) {
            plugin.giveWarderCompass();
        }
    }

    @EventHandler
    public void onAesSedaiDeath(PlayerDeathEvent event) {
        Player warder = plugin.getWarder();
        if (warder != null && event.getEntity().equals(warder)) {
            Iterator<ItemStack> iterator = event.getDrops().iterator();
            while (iterator.hasNext()) {
                if (iterator.next().getType().equals(Material.COMPASS)) {
                    iterator.remove();
                }
            }
        }
    }
    
}
