package com.msicraft.renewalmodpackintegrated.Mythicmob.Utils;

import com.msicraft.renewalmodpackintegrated.RenewalModPackIntegrated;
import io.lumine.mythic.api.MythicProvider;
import io.lumine.mythic.api.skills.SkillManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MythicMobsUtil {

    private final String prefix = RenewalModPackIntegrated.getPrefix();

    public void SkillIdentification() {
        ArrayList<String> skillIdList = new ArrayList<>(RenewalModPackIntegrated.registerSkill.getConfig().getStringList("Register-Skill-List"));
        int count = skillIdList.size();
        SkillManager skillManager = MythicProvider.get().getSkillManager();
        ArrayList<String> mythicmobsSkillList = new ArrayList<>(skillManager.getSkillNames());
        for (String skillId : skillIdList) {
            String[] a = skillId.split(":");
            String id = a[0];
            if (!mythicmobsSkillList.contains(id)) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + prefix + ChatColor.RED + " Invalid Skill: " + ChatColor.WHITE + id);
                count--;
            }
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + prefix + " " + ChatColor.WHITE + count + ChatColor.GREEN + " skills identified");
    }

    public boolean isSkillExist(String skillId) {
        boolean exist = false;
        SkillManager skillManager = MythicProvider.get().getSkillManager();
        ArrayList<String> mythicmobsSkillList = new ArrayList<>(skillManager.getSkillNames());
        if (mythicmobsSkillList.contains(skillId)) {
            exist = true;
        }
        return exist;
    }

    public boolean isFirstSkill(Player player) {
        boolean check = false;
        ArrayList<String> skillList = new ArrayList<>(RenewalModPackIntegrated.playerData.getConfig().getStringList("PlayerData." + player.getUniqueId() + ".Skill-Slot"));
        if (skillList.isEmpty()) {
            check = true;
        }
        return check;
    }

    public void registerFirstSkill(Player player, String skillId) {
        ArrayList<String> skillList = new ArrayList<>();
        for (int a = 0; a<9 ; a++) {
            skillList.add(skillId);
        }
        RenewalModPackIntegrated.playerData.getConfig().set("PlayerData." + player.getUniqueId() + ".Skill-Slot", skillList);
        RenewalModPackIntegrated.playerData.saveConfig();
    }

    public void registerPlayerSkillData(Player player, String skillId) {
        UUID uuid = player.getUniqueId();
        RenewalModPackIntegrated.playerSkillData.getConfig().set("PlayerSkillData." + uuid + ".Skills." + skillId, 1);
        RenewalModPackIntegrated.playerSkillData.saveConfig();
    }

    public boolean isUpgradeSkill(String skillId) {
        boolean check;
        check = RenewalModPackIntegrated.skillData.getConfig().contains("Skills." + skillId + ".LevelPerPower");
        return check;
    }

    public int getPlayerSkillPoint(Player player) {
        int point = 0;
        if (RenewalModPackIntegrated.playerData.getConfig().contains("PlayerData." + player.getUniqueId() + ".Skill-Points")) {
            point = RenewalModPackIntegrated.playerData.getConfig().getInt("PlayerData." + player.getUniqueId() + ".Skill-Points");
        }
        return point;
    }

    public void setPlayerSkillPoint(Player player, int setPoint) {
        RenewalModPackIntegrated.playerData.getConfig().set("PlayerData." + player.getUniqueId() + ".Skill-Points", setPoint);
        RenewalModPackIntegrated.playerData.saveConfig();
    }

    public int getSkillLevel(Player player, String skillId) {
        int level = 1;
        UUID uuid = player.getUniqueId();
        if (RenewalModPackIntegrated.playerSkillData.getConfig().contains("PlayerSkillData." + uuid + ".Skills." + skillId)) {
            level = RenewalModPackIntegrated.playerSkillData.getConfig().getInt("PlayerSkillData." + uuid + ".Skills." + skillId);
        }
        return level;
    }

    public float getSkillPower(String skillId, int skillLevel) {
        float power = 1;
        if (RenewalModPackIntegrated.skillData.getConfig().contains("Skills." + skillId + ".LevelPerPower")) {
            double percent = RenewalModPackIntegrated.skillData.getConfig().getDouble("Skills." + skillId + ".LevelPerPower");
            power = (float) (1 + (percent * (skillLevel - 1)));
        }
        return power;
    }

    public void setSkillLevel(Player player, String skillId, int setLevel) {
        if (RenewalModPackIntegrated.playerSkillData.getConfig().contains("PlayerSkillData." + player.getUniqueId() + ".Skills." + skillId)) {
            RenewalModPackIntegrated.playerSkillData.getConfig().set("PlayerSkillData." + player.getUniqueId() + ".Skills." + skillId, setLevel);
            RenewalModPackIntegrated.playerSkillData.saveConfig();
            String skillName = RenewalModPackIntegrated.skillData.getConfig().getString("Skills." + skillId + ".Name");
            if (skillName != null) {
                player.sendMessage(ChatColor.GREEN + "Skill level increase");
                player.sendMessage(ChatColor.GREEN + "Skill Name: " + ChatColor.WHITE + skillName);
                player.sendMessage(ChatColor.GREEN + "Skill Level: " + ChatColor.WHITE + setLevel);
            }
        }
    }

    public int getRequiredSkillPoint(String skillId) {
        int point = 2;
        if (RenewalModPackIntegrated.skillData.getConfig().contains("Skills." + skillId + ".LevelUpPoint")) {
            point = RenewalModPackIntegrated.skillData.getConfig().getInt("Skills." + skillId + ".LevelUpPoint");
        }
        return point;
    }

    public void registerScrollId() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            RenewalModPackIntegrated.getPlugin().scroll_SKillId.put(player.getUniqueId(), 0);
        }
    }

    public void getSkillItem(Player player) {
        ItemStack itemStack = new ItemStack(Material.PAPER, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> loreList = new ArrayList<>();
        if (itemMeta != null) {
            PersistentDataContainer data = itemMeta.getPersistentDataContainer();
            itemMeta.setDisplayName("Skill Scroll");
            loreList.add(ChatColor.YELLOW + "Right Click: " + ChatColor.WHITE + "Cast Select Skill");
            loreList.add("");
            loreList.add(ChatColor.RED + "" + ChatColor.BOLD + "Skills can only be used offhand");
            loreList.add("");
            itemMeta.setLore(loreList);
            itemMeta.addEnchant(Enchantment.DURABILITY, 1, false);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            data.set(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI_ITEM_SCROLL"), PersistentDataType.STRING, "mpi_item-scroll");
            ArrayList<String> skillList = new ArrayList<>(RenewalModPackIntegrated.playerData.getConfig().getStringList("PlayerData." + player.getUniqueId() + ".Skill-Slot"));
            if (!skillList.isEmpty()) {
                String skillId = skillList.get(0);
                data.set(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI_SCROLL_SKILL"), PersistentDataType.STRING, skillId);
            }
            itemStack.setItemMeta(itemMeta);
        }
        player.getInventory().addItem(itemStack);
    }

    public void replaceSkillItem(Player player, String skillId) {
        ItemStack itemStack = new ItemStack(Material.PAPER, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> loreList = new ArrayList<>();
        if (itemMeta != null) {
            PersistentDataContainer data = itemMeta.getPersistentDataContainer();
            itemMeta.setDisplayName("Skill Scroll");
            loreList.add(ChatColor.YELLOW + "Right Click: " + ChatColor.WHITE + "Cast Select Skill");
            loreList.add("");
            loreList.add(ChatColor.RED + "" + ChatColor.BOLD + "Skills can only be used offhand");
            loreList.add("");
            itemMeta.setLore(loreList);
            data.set(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI_ITEM_SCROLL"), PersistentDataType.STRING, "mpi_item-scroll");
            data.set(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI_SCROLL_SKILL"), PersistentDataType.STRING, skillId);
            itemStack.setItemMeta(itemMeta);
        }
        player.getInventory().setItem(EquipmentSlot.OFF_HAND, itemStack);
    }

}
