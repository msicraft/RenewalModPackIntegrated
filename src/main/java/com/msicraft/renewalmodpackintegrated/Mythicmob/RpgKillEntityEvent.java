package com.msicraft.renewalmodpackintegrated.Mythicmob;

import com.msicraft.renewalmodpackintegrated.Mythicmob.CustomEvent.PlayerGetPointEvent;
import com.msicraft.renewalmodpackintegrated.Mythicmob.CustomItem.CustomItem;
import com.msicraft.renewalmodpackintegrated.RenewalModPackIntegrated;
import io.lumine.mythic.bukkit.MythicBukkit;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Random;

public class RpgKillEntityEvent implements Listener {

    private Random random = new Random();

    private CustomItem customItem = new CustomItem();

    @EventHandler
    public void onDropPlayerPoint(EntityDeathEvent e) {
        if (RenewalModPackIntegrated.getPlugin().getConfig().getBoolean("Player-Point-Setting.Enabled")) {
            if (e.getEntityType() != EntityType.PLAYER) {
                LivingEntity livingEntity = e.getEntity();
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
                if (RenewalModPackIntegrated.getPlugin().getConfig().getBoolean("Player-Point-Setting.Fix-DeathEvent.Enabled")) {
                    ItemStack itemStack = customItem.getPlayerPointPaper(cal);
                    Location loc = livingEntity.getLocation();
                    World world = loc.getWorld();
                    if (world != null) {
                        world.dropItemNaturally(loc, itemStack);
                        Bukkit.getScheduler().runTaskLater(RenewalModPackIntegrated.getPlugin(), () -> {
                            for (Entity entity : world.getNearbyEntities(loc, 5, 2, 5)) {
                                if (entity instanceof Item item) {
                                    ItemMeta itemMeta = item.getItemStack().getItemMeta();
                                    if (itemMeta != null) {
                                        PersistentDataContainer data = itemMeta.getPersistentDataContainer();
                                        if (data.has(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI_POINT-ITEM"), PersistentDataType.STRING)) {
                                            item.setCustomName(ChatColor.GREEN + "Player Point");
                                            item.setCustomNameVisible(true);
                                        }
                                    }
                                }
                            }
                        }, 1L);
                    }
                } else {
                    Player player = livingEntity.getKiller();
                    if (player != null) {
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
    }

    @EventHandler
    public void onPlayerPointItemPickup(EntityPickupItemEvent e) {
        if (RenewalModPackIntegrated.getPlugin().getConfig().getBoolean("Player-Point-Setting.Enabled")) {
            if (RenewalModPackIntegrated.getPlugin().getConfig().getBoolean("Player-Point-Setting.Fix-DeathEvent.Enabled")) {
                Item item = e.getItem();
                ItemStack itemStack = item.getItemStack();
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    PersistentDataContainer data = itemMeta.getPersistentDataContainer();
                    if (data.has(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI_POINT-ITEM"), PersistentDataType.STRING)) {
                        e.setCancelled(true);
                        int value = data.get(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI_POINT-ITEM-VALUE"), PersistentDataType.INTEGER);
                        LivingEntity livingEntity = e.getEntity();
                        if (livingEntity instanceof Player player) {
                            item.remove();
                            Bukkit.getServer().getPluginManager().callEvent(new PlayerGetPointEvent(player, value));
                            if (RenewalModPackIntegrated.getPlugin().getConfig().getBoolean("Debug.Enabled")) {
                                Bukkit.getConsoleSender().sendMessage(RenewalModPackIntegrated.getPrefix() + ChatColor.GREEN + "Player PickUp Player Point item ");
                                Bukkit.getConsoleSender().sendMessage(RenewalModPackIntegrated.getPrefix() + ChatColor.GREEN + "Player: " + ChatColor.WHITE + player.getName());
                                Bukkit.getConsoleSender().sendMessage(RenewalModPackIntegrated.getPrefix() + ChatColor.GREEN + "Player Point : " + ChatColor.WHITE + value);
                            }
                        }
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
