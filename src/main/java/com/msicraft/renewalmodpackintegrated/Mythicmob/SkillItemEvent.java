package com.msicraft.renewalmodpackintegrated.Mythicmob;

import com.msicraft.renewalmodpackintegrated.Mythicmob.Utils.MythicMobsUtil;
import com.msicraft.renewalmodpackintegrated.RenewalModPackIntegrated;
import io.lumine.mythic.api.MythicProvider;
import io.lumine.mythic.api.skills.SkillManager;
import io.lumine.mythic.bukkit.MythicBukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SkillItemEvent implements Listener {

    private Map<UUID, Integer> skillCount = new HashMap<>();

    private MythicMobsUtil mythicMobsUtil = new MythicMobsUtil();

    private HashMap<UUID, Long> scrollCooldown = new HashMap<>();

    @EventHandler
    public void onSkillCast(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        ItemStack handItem = player.getInventory().getItemInMainHand();
        ItemMeta handMeta = handItem.getItemMeta();
        if (handMeta != null) {
            PersistentDataContainer handData = handMeta.getPersistentDataContainer();
            if (handData.has(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI_ITEM_SCROLL"), PersistentDataType.STRING)) {
                return;
            }
        }
        Action action = e.getAction();
        if (player.isSneaking()) {
            if (action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR) {
                ItemStack itemStack = e.getItem();
                if (itemStack != null) {
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    if (itemMeta != null) {
                        PersistentDataContainer data = itemMeta.getPersistentDataContainer();
                        if (data.has(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI_ITEM_SCROLL"), PersistentDataType.STRING)) {
                            int count;
                            count = skillCount.getOrDefault(uuid, 0);
                            ArrayList<String> enableSkillList = new ArrayList<>(RenewalModPackIntegrated.playerData.getConfig().getStringList("PlayerData." + uuid + ".Skill-Slot"));
                            int cal = 0;
                            cal = count + 1;
                            if (cal > 8) {
                                cal = 0;
                            }
                            skillCount.put(uuid, cal);
                            RenewalModPackIntegrated.getPlugin().scroll_SKillId.put(uuid, cal);
                            String skillId = enableSkillList.get(cal);
                            mythicMobsUtil.replaceSkillItem(player, skillId);
                            return;
                        }
                    }
                }
            }
        }
        if (player.isSneaking()) {
            if (action == Action.LEFT_CLICK_BLOCK || action == Action.LEFT_CLICK_AIR) {
                ItemStack offHand = player.getInventory().getItemInOffHand();
                ItemMeta offHandMeta = offHand.getItemMeta();
                if (offHandMeta != null) {
                    PersistentDataContainer data = offHandMeta.getPersistentDataContainer();
                    if (data.has(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI_ITEM_SCROLL"), PersistentDataType.STRING)) {
                        int count;
                        count = skillCount.getOrDefault(uuid, 0);
                        ArrayList<String> enableSkillList = new ArrayList<>(RenewalModPackIntegrated.playerData.getConfig().getStringList("PlayerData." + uuid + ".Skill-Slot"));
                        int cal = 0;
                        cal = count - 1;
                        if (cal < 0) {
                            cal = 8;
                        }
                        skillCount.put(uuid, cal);
                        RenewalModPackIntegrated.getPlugin().scroll_SKillId.put(uuid, cal);
                        String skillId = enableSkillList.get(cal);
                        mythicMobsUtil.replaceSkillItem(player, skillId);
                    }
                }
            }
        } else {
            if (action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR) {
                ItemStack offHand = e.getItem();
                if (offHand != null) {
                    ItemMeta offHandMeta = offHand.getItemMeta();
                    if (offHandMeta != null) {
                        PersistentDataContainer data = offHandMeta.getPersistentDataContainer();
                        if (data.has(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI_ITEM_SCROLL"), PersistentDataType.STRING)) {
                            String skillId = data.get(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI_SCROLL_SKILL"), PersistentDataType.STRING);
                            ArrayList<String> learnSkillList = new ArrayList<>(RenewalModPackIntegrated.playerData.getConfig().getStringList("PlayerData." + uuid + ".Learn-Skills"));
                            SkillManager skillManager = MythicProvider.get().getSkillManager();
                            ArrayList<String> skillList = new ArrayList<>(skillManager.getSkillNames());
                            if (skillList.contains(skillId)) {
                                if (learnSkillList.contains(skillId)) {
                                    long cooldown;
                                    if (RenewalModPackIntegrated.skillData.getConfig().contains("Skills." + skillId + ".CoolDown")) {
                                        cooldown = RenewalModPackIntegrated.skillData.getConfig().getLong("Skills." + skillId + ".CoolDown");
                                    } else {
                                        cooldown = 6;
                                    }
                                    String skillName = RenewalModPackIntegrated.skillData.getConfig().getString("Skills." + skillId + ".Name");
                                    String map_string = uuid + "-" + skillId;
                                    if (RenewalModPackIntegrated.getPlugin().getSkillCooldown().containsKey(map_string)) {
                                        if (RenewalModPackIntegrated.getPlugin().getSkillCooldown().get(map_string) > System.currentTimeMillis()) {
                                            long left_cooldown = (RenewalModPackIntegrated.getPlugin().getSkillCooldown().get(map_string) - System.currentTimeMillis()) / 1000;
                                            player.sendMessage(ChatColor.RED + "Skill Name: " + ChatColor.WHITE + skillName);
                                            player.sendMessage(ChatColor.RED + "Time remaining: " + ChatColor.WHITE + left_cooldown);
                                            return;
                                        }
                                    }
                                    long calCooldown = cooldown * 1000;
                                    RenewalModPackIntegrated.getPlugin().getSkillCooldown().put(map_string, System.currentTimeMillis() + calCooldown);
                                    int skillLevel = mythicMobsUtil.getSkillLevel(player, skillId);
                                    float getPower = mythicMobsUtil.getSkillPower(skillId, skillLevel);
                                    MythicBukkit.inst().getAPIHelper().castSkill(player, skillId, getPower);
                                    player.sendMessage(ChatColor.GREEN + "Casting Skill: " + ChatColor.WHITE + skillName);
                                } else {
                                    player.sendMessage(ChatColor.RED + "This is a skill that has not been acquired.");
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
