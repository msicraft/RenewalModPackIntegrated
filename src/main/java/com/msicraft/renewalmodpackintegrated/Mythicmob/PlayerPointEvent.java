package com.msicraft.renewalmodpackintegrated.Mythicmob;

import com.msicraft.renewalmodpackintegrated.Mythicmob.CustomEvent.PlayerGetPointEvent;
import com.msicraft.renewalmodpackintegrated.RenewalModPackIntegrated;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class PlayerPointEvent implements Listener {

    @EventHandler
    public void onPlayerGetPoint(PlayerGetPointEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        int value = e.getPoint();
        int currentPointExp = RenewalModPackIntegrated.getPlugin().getPlayerPointExp().get(uuid);
        int cal = currentPointExp + value;
        RenewalModPackIntegrated.getPlugin().getPlayerPointExp().put(uuid, cal);
        if (RenewalModPackIntegrated.getPlugin().getConfig().getBoolean("Debug.Enabled")) {
            Bukkit.getConsoleSender().sendMessage(RenewalModPackIntegrated.getPrefix() + ChatColor.GREEN + e.getEventName());
            Bukkit.getConsoleSender().sendMessage(RenewalModPackIntegrated.getPrefix() + ChatColor.GREEN + "Player: " + ChatColor.WHITE + player.getName());
            Bukkit.getConsoleSender().sendMessage(RenewalModPackIntegrated.getPrefix() + ChatColor.GREEN + "Get Point Exp: " + ChatColor.WHITE + value);
        }
    }

}
