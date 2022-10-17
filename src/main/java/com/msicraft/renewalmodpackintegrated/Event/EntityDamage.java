package com.msicraft.renewalmodpackintegrated.Event;

import com.msicraft.renewalmodpackintegrated.RenewalModPackIntegrated;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamage implements Listener {

    @EventHandler
    public void onDisableFallDamage(EntityDamageEvent e) {
        if (e.getEntityType() == EntityType.PLAYER) {
            return;
        }
        if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
            e.setDamage(0);
        }
    }

    @EventHandler
    public void onArrowDamage(EntityDamageByEntityEvent e) {
        boolean check = RenewalModPackIntegrated.getPlugin().getConfig().getBoolean("Setting.Reduce-ArrowDamage.Enabled");
        if (check) {
            Entity damager = e.getDamager();
            if ((damager instanceof Arrow arrow)) {
                double damage = e.getDamage();
                double percent = RenewalModPackIntegrated.getPlugin().getConfig().getDouble("Setting.Reduce-ArrowDamage.Percent");
                double cal = damage * percent;
                e.setDamage(cal);
                boolean debugCheck = RenewalModPackIntegrated.getPlugin().getConfig().getBoolean("Debug.Enabled");
                if (debugCheck) {
                    double arrowDamage = arrow.getDamage();
                    Bukkit.getConsoleSender().sendMessage(RenewalModPackIntegrated.getPrefix() + " original arrow damage: " + damage + "\ncal arrow damage: " + cal + "\n| " + arrowDamage) ;
                }
            }
        }
    }

}
