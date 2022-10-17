package com.msicraft.renewalmodpackintegrated.Mythicmob.Inventory.InvEvent;

import com.msicraft.renewalmodpackintegrated.Mythicmob.Inventory.LearnSkillInv;
import com.msicraft.renewalmodpackintegrated.Mythicmob.Utils.MythicMobsUtil;
import com.msicraft.renewalmodpackintegrated.RenewalModPackIntegrated;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;

public class LearnSkillInvEvent implements Listener {

    public HashMap<String, Integer> page_count = new HashMap<>();

    public HashMap<String, Integer> page_count_2 = new HashMap<>();


    private MythicMobsUtil mythicMobsUtil = new MythicMobsUtil();

    @EventHandler
    public void onLearnSkillInvClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) {
            return; }
        if (e.getClickedInventory().getHolder() instanceof LearnSkillInv) {
            e.setCancelled(true);
            Player player = (Player) e.getWhoClicked();
            LearnSkillInv learnSkillInv = new LearnSkillInv(player);
            if (e.getCurrentItem() == null) { return; }
            Material material = e.getCurrentItem().getType();
            ItemStack itemStack = e.getCurrentItem();
            ItemMeta itemMeta = itemStack.getItemMeta();
            int max = learnSkillInv.max_page.get("max-page");
            if (itemMeta != null) {
                PersistentDataContainer data = itemMeta.getPersistentDataContainer();
                if (!page_count.containsKey("page")) {
                    page_count.put("page", 0);
                }
                if (e.isLeftClick() && material == Material.ARROW) {
                    if (data.has(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI"), PersistentDataType.STRING)) {
                        String dataContainer = data.get(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI"), PersistentDataType.STRING);
                        if (dataContainer != null) {
                            switch (dataContainer) {
                                case "mpi_gui_next" -> {
                                    int current_page = page_count.get("page");
                                    int next_page = current_page + 1;
                                    if (next_page > max) {
                                        next_page = 0;
                                    }
                                    page_count.put("page", next_page);
                                    learnSkillInv.page.put("page", next_page);
                                }
                                case "mpi_gui_previous" -> {
                                    int current_page = page_count.get("page");
                                    int next_page = current_page - 1;
                                    if (next_page < 0) {
                                        next_page = max;
                                    }
                                    page_count.put("page", next_page);
                                    learnSkillInv.page.put("page", next_page);
                                }
                            }
                            player.closeInventory();
                            player.openInventory(learnSkillInv.getInventory());
                            learnSkillInv.setInv();
                        }
                    }
                }
                if (e.isLeftClick() && e.getSlot() <= 44) {
                    if (data.has(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI_Skill"), PersistentDataType.STRING) && data.has(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI_SkillPoint"), PersistentDataType.STRING)) {
                        String skillId = data.get(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI_Skill"), PersistentDataType.STRING);
                        String point = data.get(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI_SkillPoint"), PersistentDataType.STRING);
                        int getPlayerSkillPoint = RenewalModPackIntegrated.playerData.getConfig().getInt("PlayerData." + player.getUniqueId() + ".Skill-Points");
                        ArrayList<String> learnSkillList = new ArrayList<>(RenewalModPackIntegrated.playerData.getConfig().getStringList("PlayerData." + player.getUniqueId() + ".Learn-Skills"));
                        if (point != null) {
                            int requiredPoint = Integer.parseInt(point);
                            if (!learnSkillList.contains(skillId)) {
                                if (getPlayerSkillPoint >= requiredPoint) {
                                    int lastValue = getPlayerSkillPoint - requiredPoint;
                                    learnSkillList.add(skillId);
                                    RenewalModPackIntegrated.playerData.getConfig().set("PlayerData." + player.getUniqueId() + ".Learn-Skills", learnSkillList);
                                    RenewalModPackIntegrated.playerData.getConfig().set("PlayerData." + player.getUniqueId() + ".Skill-Points", lastValue);
                                    RenewalModPackIntegrated.playerData.saveConfig();
                                    player.sendMessage(ChatColor.GREEN + "Get a new skill");
                                    player.sendMessage(ChatColor.GREEN + "Remaining skill points: " + ChatColor.WHITE + lastValue);
                                    player.closeInventory();
                                    mythicMobsUtil.registerPlayerSkillData(player, skillId);
                                    if (mythicMobsUtil.isFirstSkill(player)) {
                                        mythicMobsUtil.registerFirstSkill(player, skillId);
                                    }
                                } else {
                                    player.sendMessage(ChatColor.RED + "Not enough skill points");
                                }
                            } else {
                                player.sendMessage(ChatColor.RED +"This skill has already been acquired");
                            }
                        }
                    }
                }
            }
        }
    }

}
