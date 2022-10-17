package com.msicraft.renewalmodpackintegrated.Mythicmob;

import com.msicraft.renewalmodpackintegrated.RenewalModPackIntegrated;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.UUID;

public class RpgPlayerJoinEvent implements Listener {

    @EventHandler
    public void onPlayerFirstJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!RenewalModPackIntegrated.playerData.getConfig().contains("PlayerData." + uuid)) {
            String name = player.getName();
            int default_skillPoints = RenewalModPackIntegrated.getPlugin().getConfig().getInt("RpgSetting.default-skill-points");
            RenewalModPackIntegrated.playerData.getConfig().set("PlayerData." + uuid + ".Name", name);
            RenewalModPackIntegrated.playerData.getConfig().set("PlayerData." + uuid + ".Points", 0);
            RenewalModPackIntegrated.playerData.getConfig().set("PlayerData." + uuid + ".Points-Exp", 0);
            RenewalModPackIntegrated.playerData.getConfig().set("PlayerData." + uuid + ".Skill-Points", default_skillPoints);
            RenewalModPackIntegrated.playerData.getConfig().set("PlayerData." + uuid + ".Learn-Skills", "");
            RenewalModPackIntegrated.playerData.getConfig().set("PlayerData." + uuid + ".Skill-Slot", "");
            RenewalModPackIntegrated.playerData.saveConfig();
        }
    }

    @EventHandler
    public void onJoinPlayerSkillDataCheck(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!RenewalModPackIntegrated.playerSkillData.getConfig().contains("PlayerSkillData." + uuid)) {
            ArrayList<String> learnSkillList = new ArrayList<>(RenewalModPackIntegrated.playerData.getConfig().getStringList("PlayerData." + player.getUniqueId() + ".Learn-Skills"));
            for (String a : learnSkillList) {
                RenewalModPackIntegrated.playerSkillData.getConfig().set("PlayerSkillData." + uuid + ".Skills." + a, 1);
            }
            RenewalModPackIntegrated.playerSkillData.saveConfig();
        }
    }

    @EventHandler
    public void onJoinEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (!RenewalModPackIntegrated.getPlugin().scroll_SKillId.containsKey(player.getUniqueId())) {
            if (!RenewalModPackIntegrated.playerData.getConfig().getStringList("PlayerData." + player.getUniqueId() + ".Skill-Slot").isEmpty()) {
                RenewalModPackIntegrated.getPlugin().scroll_SKillId.put(player.getUniqueId(), 0);
            }
        }
    }

}
