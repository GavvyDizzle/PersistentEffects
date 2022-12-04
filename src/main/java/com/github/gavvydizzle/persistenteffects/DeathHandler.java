package com.github.gavvydizzle.persistenteffects;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class DeathHandler implements Listener {

    private boolean isEnabled;
    private final HashMap<UUID, Collection<PotionEffect>> playerEffects;

    public DeathHandler() {
        playerEffects = new HashMap<>();
        reload();
    }

    public void reload() {
        FileConfiguration config = PersistentEffects.getInstance().getConfig();
        config.options().copyDefaults(true);
        config.addDefault("enabled", true);
        PersistentEffects.getInstance().saveConfig();

        isEnabled = config.getBoolean("enabled");
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (!isEnabled) return;

        Collection<PotionEffect> effects = e.getEntity().getActivePotionEffects();
        if (effects.isEmpty()) return;

        playerEffects.put(e.getEntity().getUniqueId(), e.getEntity().getActivePotionEffects());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        if (!isEnabled) {
            playerEffects.remove(e.getPlayer().getUniqueId());
            return;
        }

        if (playerEffects.containsKey(e.getPlayer().getUniqueId())) {
            Player player = e.getPlayer();

            Bukkit.getScheduler().scheduleSyncDelayedTask(PersistentEffects.getInstance(), () -> {
                if (!player.isOnline()) return;

                for (PotionEffect effect : playerEffects.remove(player.getUniqueId())) {
                    player.addPotionEffect(effect);
                    System.out.println(effect);
                }
            }, 1);
        }
    }


    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean b) {
        isEnabled = b;
    }

}
