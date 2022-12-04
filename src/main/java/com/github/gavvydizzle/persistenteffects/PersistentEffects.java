package com.github.gavvydizzle.persistenteffects;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class PersistentEffects extends JavaPlugin {

    private static PersistentEffects instance;
    private DeathHandler deathHandler;

    @Override
    public void onEnable() {
        instance = this;

        deathHandler = new DeathHandler();
        getServer().getPluginManager().registerEvents(deathHandler, this);

        Objects.requireNonNull(getCommand("persistenteffects")).setExecutor(new AdminCommands());
    }


    public static PersistentEffects getInstance() {
        return instance;
    }

    public DeathHandler getDeathHandler() {
        return deathHandler;
    }
}
