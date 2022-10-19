package com.msicraft.renewalmodpackintegrated.Event;

import com.msicraft.renewalmodpackintegrated.RenewalModPackIntegrated;
import com.msicraft.renewalmodpackintegrated.Utils.EtcEntityUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.ArrayList;
import java.util.Random;

public class EtcEntityScalingEvent implements Listener {

    private Random random = new Random();

    private EtcEntityUtil etcEntityUtil = new EtcEntityUtil();

    private ArrayList<String> bossEntity = new ArrayList<>(RenewalModPackIntegrated.entityScaleData.getConfig().getStringList("EntityScale.Boss-Entity.Boss-List"));

    private ArrayList<String> etcEntityBlackList = new ArrayList<>(RenewalModPackIntegrated.entityScaleData.getConfig().getStringList("EntityScale.Etc-Entity.BlackList"));

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEtcEntityScalingSpawn(CreatureSpawnEvent e) {
        if (RenewalModPackIntegrated.entityScaleData.getConfig().getBoolean("EntityScale.Etc-Entity.Enabled")) {
            String entityName = e.getEntity().getName().toUpperCase();
            String replaceName = entityName.replaceAll("\s", "_");
            if (RenewalModPackIntegrated.entityData.getConfig().contains("EntityData." + replaceName)) {
                if (RenewalModPackIntegrated.entityScaleData.getConfig().contains("Etc." + replaceName)) {
                    LivingEntity livingEntity = e.getEntity();
                    livingEntity.setInvulnerable(true);
                    Bukkit.getScheduler().runTaskLater(RenewalModPackIntegrated.getPlugin(), () -> {
                        int count = RenewalModPackIntegrated.entityScaleData.getConfig().getInt("Etc." + replaceName + ".Count");
                        double healthPercent = RenewalModPackIntegrated.entityScaleData.getConfig().getDouble("EntityScale.Etc-Entity.Health-Scale");
                        double damagePercent = RenewalModPackIntegrated.entityScaleData.getConfig().getDouble("EntityScale.Etc-Entity.Damage-Scale");
                        int armorCount = RenewalModPackIntegrated.entityScaleData.getConfig().getInt("EntityScale.Etc-Entity.Armor-Count");
                        int armorToughnessCount = RenewalModPackIntegrated.entityScaleData.getConfig().getInt("EntityScale.Etc-Entity.ArmorToughness-Count");
                        etcEntityUtil.setHealthAttributeModifier(livingEntity, healthPercent, count);
                        etcEntityUtil.setDamageAttributeModifier(livingEntity, damagePercent, count);
                        etcEntityUtil.setArmorAttributeModifier(livingEntity, replaceName, armorCount, count);
                        etcEntityUtil.setArmorToughnessAttributeModifier(livingEntity, replaceName, armorToughnessCount, count);
                        livingEntity.setInvulnerable(false);
                    }, 10L);
                    Bukkit.getScheduler().runTaskLater(RenewalModPackIntegrated.getPlugin(), () -> {
                        if (livingEntity.isInvulnerable()) {
                            livingEntity.setInvulnerable(false);
                        }
                    }, 20L);
                }
            }
        }
    }

    @EventHandler
    public void onSaveEtcEntityData(EntityDeathEvent e) {
        if (RenewalModPackIntegrated.entityScaleData.getConfig().getBoolean("EntityScale.Etc-Entity.Enabled")) {
            String entityName = e.getEntity().getName().toUpperCase();
            String replaceName = entityName.replaceAll("\s", "_");
            if (!bossEntity.contains(replaceName) && !etcEntityBlackList.contains(replaceName) && e.getEntityType() != EntityType.PLAYER) {
                if (!RenewalModPackIntegrated.entityData.getConfig().contains("EntityData." + replaceName)) {
                    LivingEntity livingEntity = e.getEntity();
                    AttributeInstance healthInstance = livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                    AttributeInstance damageInstance = livingEntity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
                    AttributeInstance armorInstance = livingEntity.getAttribute(Attribute.GENERIC_ARMOR);
                    AttributeInstance armorToughnessInstance = livingEntity.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS);
                    if (healthInstance != null && damageInstance != null) {
                        double maxHealth = livingEntity.getMaxHealth();
                        double damage = damageInstance.getBaseValue();
                        double armor = 1;
                        double armorToughness = 1;
                        if (armorInstance != null) {
                            armor = armorInstance.getBaseValue();
                        }
                        if (armorToughnessInstance != null) {
                            armorToughness = armorToughnessInstance.getBaseValue();
                        }
                        RenewalModPackIntegrated.entityData.getConfig().set("EntityData." + replaceName + ".BaseHealth", Math.round(maxHealth));
                        RenewalModPackIntegrated.entityData.getConfig().set("EntityData." + replaceName + ".BaseDamage", Math.round(damage));
                        RenewalModPackIntegrated.entityData.getConfig().set("EntityData." + replaceName + ".BaseArmor", Math.round(armor));
                        RenewalModPackIntegrated.entityData.getConfig().set("EntityData." + replaceName + ".BaseArmorToughness", Math.round(armorToughness));
                        RenewalModPackIntegrated.entityData.saveConfig();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEtcEntityEvolution(EntityDeathEvent e) {
        if (RenewalModPackIntegrated.entityScaleData.getConfig().getBoolean("EntityScale.Etc-Entity.Enabled")) {
            String entityName = e.getEntity().getName().toUpperCase();
            String replaceName = entityName.replaceAll("\s", "_");
            if (RenewalModPackIntegrated.entityData.getConfig().contains("EntityData." + replaceName)) {
                double chance = RenewalModPackIntegrated.entityScaleData.getConfig().getDouble("EntityScale.Etc-Entity.Evolution-Chance");
                double randomChance = random.nextDouble();
                if (randomChance <= chance) {
                    LivingEntity livingEntity = e.getEntity();
                    int evolutionCount = RenewalModPackIntegrated.entityScaleData.getConfig().getInt("Etc." + replaceName + ".Count");
                    int calCount = evolutionCount + 1;
                    RenewalModPackIntegrated.entityScaleData.getConfig().set("Etc." + replaceName + ".Count", calCount);
                    Bukkit.getServer().broadcastMessage(ChatColor.GREEN + replaceName + ChatColor.RED + " has evolved " + ChatColor.GREEN + calCount + ChatColor.RED + " times");
                    AttributeInstance healthInstance = livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                    AttributeInstance damageInstance = livingEntity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
                    AttributeInstance armorInstance = livingEntity.getAttribute(Attribute.GENERIC_ARMOR);
                    AttributeInstance armorToughnessInstance = livingEntity.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS);
                    if (healthInstance != null) {
                        double maxHealth = livingEntity.getMaxHealth();
                        RenewalModPackIntegrated.entityScaleData.getConfig().set("Etc." + replaceName + ".Last-Health", Math.round(maxHealth));
                    }
                    if (damageInstance != null) {
                        double getDamage = damageInstance.getValue();
                        RenewalModPackIntegrated.entityScaleData.getConfig().set("Etc." + replaceName + ".Last-Damage", getDamage);
                    }
                    if (armorInstance != null) {
                        double getArmor = armorInstance.getValue();
                        RenewalModPackIntegrated.entityScaleData.getConfig().set("Etc." + replaceName + ".Last-Armor", getArmor);
                    }
                    if (armorToughnessInstance != null) {
                        double armorToughness = armorToughnessInstance.getValue();
                        RenewalModPackIntegrated.entityScaleData.getConfig().set("Etc." + replaceName + ".Last-ArmorToughness", armorToughness);
                    }
                    RenewalModPackIntegrated.entityScaleData.saveConfig();
                }
            }
        }
    }

}
