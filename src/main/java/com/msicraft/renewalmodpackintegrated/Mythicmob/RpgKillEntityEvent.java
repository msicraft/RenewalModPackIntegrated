package com.msicraft.renewalmodpackintegrated.Mythicmob;

import com.msicraft.renewalmodpackintegrated.Mythicmob.CustomEvent.PlayerGetPointEvent;
import com.msicraft.renewalmodpackintegrated.RenewalModPackIntegrated;
import io.lumine.mythic.bukkit.MythicBukkit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class RpgKillEntityEvent implements Listener {

    private Random random = new Random();

    @EventHandler
    public void onKillEntity(EntityDeathEvent e) {
        if (RenewalModPackIntegrated.getPlugin().getConfig().getBoolean("Player-Point-Setting.Enabled")) {
            if (!RenewalModPackIntegrated.getPlugin().getConfig().getBoolean("Player-Point-Setting.Fix-DeathEvent.Enabled")) {  //false
                LivingEntity livingEntity = e.getEntity();
                Player player = livingEntity.getKiller();
                if (player != null) {
                    double maxHealth = livingEntity.getMaxHealth();
                    double percent = RenewalModPackIntegrated.getPlugin().getConfig().getDouble("Player-Point-Setting.Exchange-Percent");
                    double weight = random.nextDouble();
                    double calHealth = maxHealth * percent;
                    double calWeight = calHealth * weight;
                    int roundHealth = (int) Math.round(calHealth);
                    int roundWeight = (int) Math.round(calWeight);
                    int cal = roundHealth + roundWeight;
                    int cap = RenewalModPackIntegrated.getPlugin().getConfig().getInt("Player-Point-Setting.Max-Cap");
                    if (cal > cap) {
                        cal = cap;
                    }
                    Bukkit.getServer().getPluginManager().callEvent(new PlayerGetPointEvent(player, cal));
                    if (RenewalModPackIntegrated.getPlugin().getConfig().getBoolean("Debug.Enabled")) {
                        Bukkit.getConsoleSender().sendMessage(RenewalModPackIntegrated.getPrefix() + ChatColor.GREEN + "Exchange Percent: " + ChatColor.WHITE + percent);
                        Bukkit.getConsoleSender().sendMessage(RenewalModPackIntegrated.getPrefix() + ChatColor.GREEN + "Target Mhp: " + ChatColor.WHITE + maxHealth);
                        Bukkit.getConsoleSender().sendMessage(RenewalModPackIntegrated.getPrefix() + ChatColor.GREEN + "Weight: " + ChatColor.WHITE + weight);
                        Bukkit.getConsoleSender().sendMessage(RenewalModPackIntegrated.getPrefix() + ChatColor.GREEN + "CalHealth: " + ChatColor.WHITE + calHealth);
                        Bukkit.getConsoleSender().sendMessage(RenewalModPackIntegrated.getPrefix() + ChatColor.GREEN + "CalWeight: " + ChatColor.WHITE + calWeight);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDropSkillPointItem(EntityDeathEvent e) {
        if (RenewalModPackIntegrated.getPlugin().getConfig().getBoolean("RpgSetting.Skill-Point-Item.Enabled")) {
            if (e.getEntityType() != EntityType.PLAYER) {
                String internalName = RenewalModPackIntegrated.getPlugin().getConfig().getString("RpgSetting.Skill-Point-Item.InternalName");
                if (MythicBukkit.inst().getItemManager().getItemNames().contains(internalName)) {
                    Location location = e.getEntity().getLocation();
                    World world = location.getWorld();
                    if (world != null) {
                        double cal = random.nextDouble();
                        double chance = RenewalModPackIntegrated.getPlugin().getConfig().getDouble("RpgSetting.Skill-Point-Item.Chance");
                        if (cal <= chance) {
                            ItemStack itemStack = MythicBukkit.inst().getItemManager().getItemStack(internalName, 1);
                            world.dropItemNaturally(location, itemStack);
                        }
                        if (RenewalModPackIntegrated.getPlugin().getConfig().getBoolean("Debug.Enabled")) {
                            Bukkit.getConsoleSender().sendMessage(RenewalModPackIntegrated.getPrefix() + ChatColor.GREEN + "Skill Point Drop");
                            Bukkit.getConsoleSender().sendMessage(RenewalModPackIntegrated.getPrefix() + ChatColor.WHITE + "Cal Chance: " + cal);
                            Bukkit.getConsoleSender().sendMessage(RenewalModPackIntegrated.getPrefix() + ChatColor.WHITE + "Chance: " + chance);
                        }
                    }
                } else {
                    if (RenewalModPackIntegrated.getPlugin().getConfig().getBoolean("Debug.Enabled")) {
                        Bukkit.getConsoleSender().sendMessage(RenewalModPackIntegrated.getPrefix() + ChatColor.GREEN + "Skill Point Drop " + ChatColor.WHITE + "\n" + ChatColor.RED + "Invalid Internal Name: " + ChatColor.WHITE + internalName);
                    }
                }
            }
        }
    }

}
