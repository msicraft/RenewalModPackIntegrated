package com.msicraft.renewalmodpackintegrated.Utils;

import com.msicraft.renewalmodpackintegrated.RenewalModPackIntegrated;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.LivingEntity;

import java.util.UUID;

public class EtcEntityUtil {

    public void setHealthAttributeModifier(LivingEntity livingEntity, double healthPercent, int count) {
        AttributeInstance attributeInstance = livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        double cal = healthPercent * count;
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID().toString(), cal, AttributeModifier.Operation.ADD_SCALAR);
        if (attributeInstance != null) {
            attributeInstance.addModifier(modifier);
            if (RenewalModPackIntegrated.getPlugin().getConfig().getBoolean("Debug.Enabled")) {
                Bukkit.getConsoleSender().sendMessage(RenewalModPackIntegrated.getPrefix() + ChatColor.GREEN + livingEntity.getName() +" Health: " + ChatColor.WHITE + attributeInstance.getValue() + ChatColor.GREEN + " Percent: " + ChatColor.WHITE + healthPercent);
            }
        }
    }

    public void setDamageAttributeModifier(LivingEntity livingEntity, double damagePercent,int count) {
        AttributeInstance attributeInstance = livingEntity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        double cal = damagePercent * count;
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID().toString(), cal, AttributeModifier.Operation.ADD_SCALAR);
        if (attributeInstance != null) {
            attributeInstance.addModifier(modifier);
            if (RenewalModPackIntegrated.getPlugin().getConfig().getBoolean("Debug.Enabled")) {
                Bukkit.getConsoleSender().sendMessage(RenewalModPackIntegrated.getPrefix() + ChatColor.GREEN + livingEntity.getName() + " Damage: " + ChatColor.WHITE + attributeInstance.getValue() + ChatColor.GREEN + " Percent: " + ChatColor.WHITE + damagePercent);
            }
        }
    }

    public void setArmorAttributeModifier(LivingEntity livingEntity, String name, double armorCount, int count) {
        AttributeInstance attributeInstance = livingEntity.getAttribute(Attribute.GENERIC_ARMOR);
        if (attributeInstance != null) {
            double baseArmor = RenewalModPackIntegrated.entityData.getConfig().getInt("EntityData." + name + ".BaseArmor");
            double armorCal = count / armorCount;
            if (armorCal <= 0) {
                armorCal = 0;
            }
            double calArmor = baseArmor + armorCal;
            attributeInstance.setBaseValue(calArmor);
            if (RenewalModPackIntegrated.getPlugin().getConfig().getBoolean("Debug.Enabled")) {
                Bukkit.getConsoleSender().sendMessage(RenewalModPackIntegrated.getPrefix() + ChatColor.GREEN + livingEntity.getName() + " Armor: " + ChatColor.WHITE + attributeInstance.getValue());
            }
        }
    }

    public void setArmorToughnessAttributeModifier(LivingEntity livingEntity, String name, double armorToughnessCount, int count) {
        AttributeInstance attributeInstance = livingEntity.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS);
        if (attributeInstance != null) {
            double baseArmorToughness = RenewalModPackIntegrated.entityData.getConfig().getInt("EntityData." + name + ".BaseArmorToughness");
            double armorToughnessCal = count / armorToughnessCount;
            if (armorToughnessCal <= 0) {
                armorToughnessCal = 0;
            }
            double cal = baseArmorToughness + armorToughnessCal;
            attributeInstance.setBaseValue(cal);
            if (RenewalModPackIntegrated.getPlugin().getConfig().getBoolean("Debug.Enabled")) {
                Bukkit.getConsoleSender().sendMessage(RenewalModPackIntegrated.getPrefix() + ChatColor.GREEN + livingEntity.getName() + " ArmorToughness: " + ChatColor.WHITE + attributeInstance.getValue());
                Bukkit.getConsoleSender().sendMessage(RenewalModPackIntegrated.getPrefix() + ChatColor.GREEN + "Evolution Count: " + ChatColor.WHITE + count);
            }
        }
    }

}
