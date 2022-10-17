package com.msicraft.renewalmodpackintegrated.Mythicmob.Tasks;

import com.msicraft.renewalmodpackintegrated.RenewalModPackIntegrated;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.UUID;

public class SkillSelectTask extends BukkitRunnable {

    RenewalModPackIntegrated plugin;

    public SkillSelectTask(RenewalModPackIntegrated plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            ItemStack itemStack = player.getInventory().getItemInOffHand();
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta != null) {
                PersistentDataContainer data = itemMeta.getPersistentDataContainer();
                if (data.has(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI_ITEM_SCROLL"), PersistentDataType.STRING)) {
                    UUID uuid = player.getUniqueId();
                    ArrayList<String> enableSkillList = new ArrayList<>(RenewalModPackIntegrated.playerData.getConfig().getStringList("PlayerData." + uuid + ".Skill-Slot"));
                    if (RenewalModPackIntegrated.getPlugin().scroll_SKillId.containsKey(uuid)) {
                        int currentSkill = RenewalModPackIntegrated.getPlugin().scroll_SKillId.get(uuid);
                        int nextSkill = currentSkill + 1;
                        if (nextSkill > 8) {
                            nextSkill = 0;
                        }
                        int previousSkill = currentSkill - 1;
                        if (previousSkill < 0) {
                            previousSkill = 8;
                        }
                        String current = enableSkillList.get(currentSkill);
                        String currentName = RenewalModPackIntegrated.skillData.getConfig().getString("Skills." + current + ".Name");
                        String next = enableSkillList.get(nextSkill);
                        String nextName = RenewalModPackIntegrated.skillData.getConfig().getString("Skills." + next + ".Name");
                        String previous = enableSkillList.get(previousSkill);
                        String previousName = RenewalModPackIntegrated.skillData.getConfig().getString("Skills." + previous + ".Name");
                        String currentKey = uuid + "-" + current;
                        long currentCoolDown = 0;
                        long nextCoolDown = 0;
                        long previousCoolDown = 0;
                        if (RenewalModPackIntegrated.getPlugin().getSkillCooldown().containsKey(currentKey)) {
                            currentCoolDown = (RenewalModPackIntegrated.getPlugin().getSkillCooldown().get(currentKey) - System.currentTimeMillis()) / 1000;
                        }
                        String nextKey = uuid + "-" + next;
                        if (RenewalModPackIntegrated.getPlugin().getSkillCooldown().containsKey(nextKey)) {
                            nextCoolDown = (RenewalModPackIntegrated.getPlugin().getSkillCooldown().get(nextKey) - System.currentTimeMillis()) / 1000;
                        }
                        String previousKey = uuid + "-" + previous;
                        if (RenewalModPackIntegrated.getPlugin().getSkillCooldown().containsKey(previousKey)) {
                            previousCoolDown = (RenewalModPackIntegrated.getPlugin().getSkillCooldown().get(previousKey) - System.currentTimeMillis()) / 1000;
                        }
                        String currentMessage;
                        if (currentCoolDown <= 0) {
                            currentMessage = "§a§o§l" + currentName;
                        } else {
                            currentMessage = "§c§o§l" + currentName + " [§5" + currentCoolDown + "§c]";
                        }
                        String nextMessage;
                        if (nextCoolDown <= 0) {
                            nextMessage = "§a§o§l" + nextName;
                        } else {
                            nextMessage = "§c§o§l" + nextName + " [§5" + nextCoolDown + "§c]";
                        }
                        String previousMessage;
                        if (previousCoolDown <= 0) {
                            previousMessage = "§a§o§l" + previousName;
                        } else {
                            previousMessage = "§c§o§l" + previousName + " [§5" + previousCoolDown + "§c]";
                        }
                        String message = previousMessage + " §a§o§l| " + currentMessage + " §a§o§l| " + nextMessage;
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
                    }
                }
            }
        }
    }

}
