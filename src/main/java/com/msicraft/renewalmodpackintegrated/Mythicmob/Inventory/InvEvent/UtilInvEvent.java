package com.msicraft.renewalmodpackintegrated.Mythicmob.Inventory.InvEvent;

import com.msicraft.renewalmodpackintegrated.Mythicmob.Inventory.UtilInv;
import com.msicraft.renewalmodpackintegrated.Mythicmob.UtillSkill.ReturnSpawn;
import com.msicraft.renewalmodpackintegrated.Mythicmob.Utils.MythicMobsUtil;
import com.msicraft.renewalmodpackintegrated.Mythicmob.Utils.PlayerUpgradeUtil;
import com.msicraft.renewalmodpackintegrated.RenewalModPackIntegrated;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class UtilInvEvent implements Listener {

    private MythicMobsUtil mythicMobsUtil = new MythicMobsUtil();

    private PlayerUpgradeUtil playerUpgradeUtil = new PlayerUpgradeUtil();

    @EventHandler
    public void onClickUtilInv(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) {
            return;
        }
        if (e.getView().getTitle().equalsIgnoreCase("Util")) {
            if (e.getInventory().getHolder() == e.getWhoClicked()) {
                e.setCancelled(true);
                Player player = (Player) e.getWhoClicked();
                if (e.getCurrentItem() == null) {
                    return;
                }
                UtilInv utilInv = new UtilInv(player);
                ItemStack itemStack = e.getCurrentItem();
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    PersistentDataContainer data = itemMeta.getPersistentDataContainer();
                    if (e.isLeftClick() && data.has(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI_UTIL"), PersistentDataType.STRING)) {
                        String dataVal = data.get(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI_UTIL"), PersistentDataType.STRING);
                        if (dataVal != null) {
                            switch (dataVal) {
                                case "MPI_UTIL_BACK" -> {
                                    player.closeInventory();
                                    player.openInventory(utilInv.getInventory());
                                    utilInv.basicMenu();
                                }
                                case "MPI_UTIL_ITEM-UPGRADE" -> {
                                    player.sendMessage(ChatColor.RED + "This feature is not yet available");
                                }
                                case "MPI_UTIL_SKILL-UPGRADE" -> {
                                    player.closeInventory();
                                    player.openInventory(utilInv.getInventory());
                                    utilInv.upgradeSkillSetting(player);
                                }
                                case "MPI_UTIL_SKILL-ReturnSpawn" -> {
                                    boolean check = RenewalModPackIntegrated.getPlugin().getConfig().getBoolean("Custom-Skill.Return-Spawn.Enabled");
                                    if (check) {
                                        ReturnSpawn returnSpawn = new ReturnSpawn();
                                        int requiredPoint = RenewalModPackIntegrated.getPlugin().getConfig().getInt("Custom-Skill.Return-Spawn.Player-Point");
                                        int radius = RenewalModPackIntegrated.getPlugin().getConfig().getInt("Custom-Skill.Return-Spawn.Radius");
                                        int point = playerUpgradeUtil.getPoint(player.getUniqueId());
                                        if (point >= requiredPoint) {
                                            int calPoint = point - requiredPoint;
                                            playerUpgradeUtil.setPoint(player.getUniqueId(), calPoint);
                                            returnSpawn.castReturnSkill(player, radius);
                                        } else {
                                            player.sendMessage(ChatColor.RED + "You don't have enough points");
                                        }
                                    } else {
                                        player.sendMessage(ChatColor.RED + "Disabled skill");
                                    }
                                }
                            }
                        }
                    }
                    if (e.isLeftClick() && e.getSlot() >= 9 && e.getSlot() <= 44) {
                        if (data.has(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI_UTIL_UPGRADE-SKILL"), PersistentDataType.STRING)) {
                            String skillId = data.get(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI_UTIL_UPGRADE-SKILL"), PersistentDataType.STRING);
                            int currentSkillLevel = mythicMobsUtil.getSkillLevel(player, skillId);
                            int nextLevel = currentSkillLevel + 1;
                            int getPlayerPoint = playerUpgradeUtil.getPoint(player.getUniqueId());
                            int requiredPoint = RenewalModPackIntegrated.skillData.getConfig().getInt("Skills." + skillId + ".LevelUpPoint");
                            if (getPlayerPoint >= requiredPoint) {
                                int point = getPlayerPoint - requiredPoint;
                                mythicMobsUtil.setSkillLevel(player, skillId, nextLevel);
                                playerUpgradeUtil.setPoint(player.getUniqueId(), point);
                                player.closeInventory();
                            } else {
                                player.sendMessage(ChatColor.RED + "You don't have enough point");
                                player.closeInventory();
                            }
                        }
                    }
                }
            }
        }
    }

}
