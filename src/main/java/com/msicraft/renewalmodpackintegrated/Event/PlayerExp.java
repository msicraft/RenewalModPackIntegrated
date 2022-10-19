package com.msicraft.renewalmodpackintegrated.Event;

import com.msicraft.renewalmodpackintegrated.RenewalModPackIntegrated;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class PlayerExp implements Listener {

    @EventHandler
    public void onDropExp(EntityDeathEvent e) {
        if (RenewalModPackIntegrated.getPlugin().getConfig().getBoolean("Setting.Exp-Setting.Enabled")) {
            int maxCap = RenewalModPackIntegrated.getPlugin().getConfig().getInt("Setting.Exp-Setting.Max-Cap");
            double percent = RenewalModPackIntegrated.getPlugin().getConfig().getDouble("Setting.Exp-Setting.Percent");
            if (RenewalModPackIntegrated.getPlugin().getConfig().getBoolean("Setting.Exp-Setting.Fix-DeathExp.Enabled")) {
                Location location = e.getEntity().getLocation();
                World world = location.getWorld();
                if (world != null) {
                    for (Entity entity : world.getNearbyEntities(location, 2.5, 1, 2.5)) {
                        if (entity instanceof ExperienceOrb experienceOrb) {
                            int orbExp = experienceOrb.getExperience();
                            int calExp = (int) Math.round(orbExp * percent);
                            if (calExp <= 1) {
                                calExp = 2;
                            }
                            int value = Math.min(calExp, maxCap);
                            experienceOrb.setExperience(value);
                            if (RenewalModPackIntegrated.getPlugin().getConfig().getBoolean("Debug.Enabled")) {
                                Bukkit.getConsoleSender().sendMessage(RenewalModPackIntegrated.getPrefix() + ChatColor.GREEN + "Fix DeathEvent True | Drop Exp: " + ChatColor.WHITE + value);
                            }
                        }
                    }
                }
            } else {
                int dropExp = e.getDroppedExp();
                int calExp = (int) Math.round(dropExp * percent);
                if (calExp <= 1) {
                    calExp = 2;
                }
                if (calExp > maxCap) {
                    calExp = maxCap;
                }
                e.setDroppedExp(calExp);
                if (RenewalModPackIntegrated.getPlugin().getConfig().getBoolean("Debug.Enabled")) {
                    Bukkit.getConsoleSender().sendMessage(RenewalModPackIntegrated.getPrefix() + ChatColor.GREEN + "Fix DeathEvent False | Drop Exp: " + ChatColor.WHITE + calExp);
                }
            }
        }
    }

}
