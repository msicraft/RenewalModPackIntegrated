package com.msicraft.renewalmodpackintegrated.Mythicmob.UtillSkill;

import com.msicraft.renewalmodpackintegrated.Mythicmob.Utils.MythicMobsUtil;
import com.msicraft.renewalmodpackintegrated.Mythicmob.Utils.PlayerUpgradeUtil;
import com.msicraft.renewalmodpackintegrated.RenewalModPackIntegrated;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ReturnSpawn {

    private PlayerUpgradeUtil playerUpgradeUtil = new PlayerUpgradeUtil();

    public void castReturnSkill(Player player, double radius) {
        boolean check = RenewalModPackIntegrated.getPlugin().getConfig().getBoolean("Custom-Skill.Return-Spawn.Enabled");
        if (check) {
            List<Player> playerList = getNearbyPlayer(player, radius);
            Location location = null;
            String spawnLocation = RenewalModPackIntegrated.getPlugin().getConfig().getString("Setting.Replace-Spawn.Main-Spawn");
            if (spawnLocation != null) {
                String [] spawn = spawnLocation.split(":");
                World world = Bukkit.getWorld(spawn[0]);
                if (world != null) {
                    double x = Double.parseDouble(spawn[1]);
                    double y = Double.parseDouble(spawn[2]);
                    double z = Double.parseDouble(spawn[3]);
                    double yaw = Double.parseDouble(spawn[4]);
                    double pitch = Double.parseDouble(spawn[5]);
                    location = new Location(world, x, y, z, (float) yaw, (float) pitch);
                }
            }
            ArrayList<String> nearbyPlayerList = new ArrayList<>();
            if (location != null) {
                for (Player nearbyPlayer : playerList) {
                    nearbyPlayer.teleport(location);
                    nearbyPlayer.sendMessage(ChatColor.GREEN + "Return Spawn");
                    nearbyPlayerList.add(nearbyPlayer.getName());
                }
                player.teleport(location);
            } else {
                Location loc = player.getWorld().getSpawnLocation();
                for (Player nearbyPlayer : playerList) {
                    nearbyPlayer.teleport(loc);
                    nearbyPlayer.sendMessage(ChatColor.GREEN + "Return Spawn");
                    nearbyPlayerList.add(nearbyPlayer.getName());
                }
                player.teleport(loc);
            }
            player.sendMessage(ChatColor.GREEN + "Return Spawn");
            player.sendMessage(ChatColor.GREEN + "Left Point: " + ChatColor.WHITE + playerUpgradeUtil.getPoint(player.getUniqueId()));
            if (RenewalModPackIntegrated.getPlugin().getConfig().getBoolean("Debug.Enabled")) {
                if (!nearbyPlayerList.isEmpty()) {
                    Bukkit.getConsoleSender().sendMessage(RenewalModPackIntegrated.getPrefix() + ChatColor.GREEN + "Cast-Return Spawn: " + ChatColor.WHITE + "\nNearby Players: " + nearbyPlayerList.size() + "\nPlayers: " + nearbyPlayerList);
                }
            }
        }
    }

    private List<Player> getNearbyPlayer(Player player, double radius) {
        List<Player> playerList = new ArrayList<>();
        for (Entity entity : player.getNearbyEntities(radius, 2 , radius)) {
            if (entity instanceof Player nearPlayer) {
                playerList.add(nearPlayer);
            }
        }
        return playerList;
    }

}
