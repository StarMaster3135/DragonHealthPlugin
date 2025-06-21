package com.starmaster.dragonhealth;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class DragonHealthPlugin extends JavaPlugin implements Listener {
    
    private double dragonHealth = 600.0; // Default health
    
    @Override
    public void onEnable() {
        saveDefaultConfig();
        dragonHealth = getConfig().getDouble("dragon-health", 600.0);
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("DragonHealthPlugin enabled! Ender Dragon health set to " + dragonHealth);
    }
    
    @Override
    public void onDisable() {
        getLogger().info("DragonHealthPlugin disabled!");
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("dragonhealth")) {
            if (!sender.hasPermission("dragonhealth.admin")) {
                sender.sendMessage("§cYou don't have permission to use this command!");
                return true;
            }
            
            if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                reloadConfig();
                dragonHealth = getConfig().getDouble("dragon-health", 600.0);
                sender.sendMessage("§aDragonHealthPlugin reloaded! Dragon health set to " + dragonHealth);
                getLogger().info("Configuration reloaded by " + sender.getName());
                return true;
            } else {
                sender.sendMessage("§6DragonHealthPlugin v1.0.0");
                sender.sendMessage("§7Current dragon health: §e" + dragonHealth);
                sender.sendMessage("§7Use §e/dragonhealth reload §7to reload config");
                return true;
            }
        }
        return false;
    }
    
    /**
     * Handles dragon spawn events and applies custom health
     */
    @EventHandler
    public void onDragonSpawn(CreatureSpawnEvent event) {
        if (event.getEntityType() == EntityType.ENDER_DRAGON) {
            EnderDragon dragon = (EnderDragon) event.getEntity();
            
            // Apply health immediately
            applyDragonHealth(dragon);
            
            // Also apply with delays to ensure it sticks (Minecraft can override health on spawn)
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (dragon.isValid() && !dragon.isDead()) {
                        applyDragonHealth(dragon);
                    }
                }
            }.runTaskLater(this, 1L); // 1 tick delay
            
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (dragon.isValid() && !dragon.isDead()) {
                        applyDragonHealth(dragon);
                    }
                }
            }.runTaskLater(this, 5L); // 5 tick delay
            
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (dragon.isValid() && !dragon.isDead()) {
                        applyDragonHealth(dragon);
                        getLogger().info("Final health check: Dragon has " + dragon.getHealth() + "/" + dragon.getMaxHealth() + " HP");
                    }
                }
            }.runTaskLater(this, 20L); // 1 second delay (final check)
        }
    }
    
    /**
     * Applies the custom health to a dragon using multiple methods for maximum compatibility
     */
    private void applyDragonHealth(EnderDragon dragon) {
        try {
            // Method 1: Try attribute system first (most reliable)
            AttributeInstance maxHealthAttribute = null;
            
            for (Attribute attr : Attribute.values()) {
                if (attr.name().contains("MAX_HEALTH")) {
                    maxHealthAttribute = dragon.getAttribute(attr);
                    if (maxHealthAttribute != null) {
                        break;
                    }
                }
            }
            
            if (maxHealthAttribute != null) {
                maxHealthAttribute.setBaseValue(dragonHealth);
            }
            
            // Method 2: Also use direct method as backup
            dragon.setMaxHealth(dragonHealth);
            dragon.setHealth(dragonHealth);
            
        } catch (Exception e) {
            getLogger().warning("Could not set dragon health: " + e.getMessage());
            try {
                // Last resort fallback
                dragon.setMaxHealth(dragonHealth);
                dragon.setHealth(dragonHealth);
            } catch (Exception ex) {
                getLogger().severe("Failed to set dragon health completely: " + ex.getMessage());
            }
        }
    }
}