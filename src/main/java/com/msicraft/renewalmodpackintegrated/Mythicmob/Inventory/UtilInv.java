package com.msicraft.renewalmodpackintegrated.Mythicmob.Inventory;

import com.msicraft.renewalmodpackintegrated.Mythicmob.Utils.MythicMobsUtil;
import com.msicraft.renewalmodpackintegrated.Mythicmob.Utils.PlayerUpgradeUtil;
import com.msicraft.renewalmodpackintegrated.RenewalModPackIntegrated;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UtilInv implements InventoryHolder{

    private PlayerUpgradeUtil playerUpgradeUtil = new PlayerUpgradeUtil();

    private Inventory utilInventory;

    private MythicMobsUtil mythicMobsUtil = new MythicMobsUtil();

    private static final int[] slots = {9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44};

    public UtilInv(Player player) {
        utilInventory = Bukkit.createInventory(player, 54, "Util");
        utilInventory.clear();
        backButton();
        getPointBook(player.getUniqueId());
    }

    private void backButton() {
        ItemStack itemStack = new ItemStack(Material.BARRIER, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RED + "Back");
        PersistentDataContainer data = itemMeta.getPersistentDataContainer();
        data.set(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI_UTIL"), PersistentDataType.STRING, "MPI_UTIL_BACK");
        itemStack.setItemMeta(itemMeta);
        utilInventory.setItem(0, itemStack);
    }

    private void getPointBook(UUID uuid) {
        List<String> loreList = new ArrayList<>();
        ItemStack itemStack = new ItemStack(Material.BOOK, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GREEN + "Current Point");
        loreList.add("");
        loreList.add(ChatColor.YELLOW + "Point: " + ChatColor.WHITE + playerUpgradeUtil.getPoint(uuid));
        double maxExp = RenewalModPackIntegrated.getPlugin().getConfig().getDouble("Player-Point-Setting.Get-Point-Value");
        double playerExp = playerUpgradeUtil.getPointExp(uuid);
        int cal = (int) ((playerExp / maxExp) * 100);
        if (cal > 100) {
            cal = 100;
        }
        loreList.add(ChatColor.YELLOW + "Point Exp: " + ChatColor.WHITE + cal + "%");
        loreList.add("");
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            loreList.add(ChatColor.YELLOW + "Skill-Points: " + ChatColor.WHITE + mythicMobsUtil.getPlayerSkillPoint(player));
        }
        itemMeta.setLore(loreList);
        itemStack.setItemMeta(itemMeta);
        utilInventory.setItem(4, itemStack);
    }

    public void basicMenu() {
        List<String> loreList = new ArrayList<>();
        /*
        ItemStack upgradeItem = new ItemStack(Material.ENCHANTED_BOOK, 1);
        ItemMeta itemMeta = upgradeItem.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GREEN + "Upgrade Equipment");
        PersistentDataContainer upgradeData = itemMeta.getPersistentDataContainer();
        upgradeData.set(new NamespacedKey(ModPackPluginIntegrated.getPlugin(), "MPI_UTIL"), PersistentDataType.STRING, "MPI_UTIL_ITEM-UPGRADE");
        upgradeItem.setItemMeta(itemMeta);
        utilInventory.setItem(12, upgradeItem);
         */
        ItemStack skillUpgrade = new ItemStack(Material.WHITE_BANNER, 1);
        ItemMeta skillUpgradeMeta = skillUpgrade.getItemMeta();
        skillUpgradeMeta.setDisplayName(ChatColor.GREEN + "Upgrade Skill");
        PersistentDataContainer skillUpgradeData = skillUpgradeMeta.getPersistentDataContainer();
        skillUpgradeData.set(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI_UTIL"), PersistentDataType.STRING, "MPI_UTIL_SKILL-UPGRADE");
        skillUpgrade.setItemMeta(skillUpgradeMeta);
        utilInventory.setItem(14, skillUpgrade);
        int requiredPoint = RenewalModPackIntegrated.getPlugin().getConfig().getInt("Custom-Skill.Return-Spawn.Skill-Point");
        ItemStack returnSkill = new ItemStack(Material.COMPASS, 1);
        ItemMeta returnSkillMeta = returnSkill.getItemMeta();
        returnSkillMeta.setDisplayName(ChatColor.GREEN + "Return Spawn");
        if (!loreList.isEmpty()) {
            loreList.clear();
        }
        PersistentDataContainer returnSkillData = returnSkillMeta.getPersistentDataContainer();
        returnSkillData.set(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI_UTIL"), PersistentDataType.STRING, "MPI_UTIL_SKILL-ReturnSpawn");
        loreList.add(ChatColor.YELLOW + "Left Click: " + ChatColor.GREEN + " Use Skill");
        loreList.add("");
        loreList.add(ChatColor.YELLOW + "Required Skill Point: " + ChatColor.WHITE + " " + requiredPoint);
        loreList.add("");
        loreList.add(ChatColor.WHITE + "Return to the spawn point with nearby players");
        returnSkillMeta.setLore(loreList);
        returnSkill.setItemMeta(returnSkillMeta);
        utilInventory.setItem(18, returnSkill);
    }

    public void upgradeSkillSetting(Player player) {
        UUID uuid = player.getUniqueId();
        ArrayList<String> learnSkillList = new ArrayList<>(RenewalModPackIntegrated.playerData.getConfig().getStringList("PlayerData." + uuid + ".Learn-Skills"));
        List<String> loreList = new ArrayList<>();
        ItemStack itemStack = new ItemStack(Material.PAPER, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            PersistentDataContainer data = itemMeta.getPersistentDataContainer();
            int count = 0;
            for (String skillId : learnSkillList) {
                if (mythicMobsUtil.isUpgradeSkill(skillId)) {
                    if (!loreList.isEmpty()) {
                        loreList.clear();
                    }
                    String skillName = RenewalModPackIntegrated.skillData.getConfig().getString("Skills." + skillId + ".Name");
                    if (skillName != null) {
                        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', skillName));
                    }
                    loreList.add("");
                    loreList.add(ChatColor.YELLOW + "Left Click: " + ChatColor.GREEN + "Skill Level Up");
                    loreList.add("");
                    int skillLevel = mythicMobsUtil.getSkillLevel(player, skillId);
                    loreList.add(ChatColor.GREEN + "Current Skill Level: " + ChatColor.WHITE + skillLevel);
                    loreList.add("");
                    int requiredPoint = mythicMobsUtil.getRequiredSkillPoint(skillId);
                    loreList.add(ChatColor.GREEN + "Required point: " + ChatColor.WHITE + requiredPoint);
                    loreList.add("");
                    double percent = RenewalModPackIntegrated.skillData.getConfig().getDouble("Skills." + skillId + ".LevelPerPower");
                    int cal = (int) (percent * 100);
                    String s = cal + "%";
                    loreList.add(ChatColor.GREEN + "Power increase per level: " + ChatColor.WHITE + s);
                    itemMeta.setLore(loreList);
                    data.set(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI_UTIL_UPGRADE-SKILL"), PersistentDataType.STRING, skillId);
                    itemStack.setItemMeta(itemMeta);
                    int slot = slots[count];
                    utilInventory.setItem(slot, itemStack);
                    count++;
                }
            }
        }
    }

    @Override
    public Inventory getInventory() {
        return utilInventory;
    }
}
