package com.msicraft.renewalmodpackintegrated.Event;

import com.msicraft.renewalmodpackintegrated.RenewalModPackIntegrated;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerReplaceSpawn implements Listener {

    @EventHandler
    public void onReplaceSpawn(PlayerRespawnEvent e) {
        boolean check = RenewalModPackIntegrated.getPlugin().getConfig().getBoolean("Setting.Replace-Spawn.Enabled");
        if (check) {
            String main_spawn = RenewalModPackIntegrated.getPlugin().getConfig().getString("Setting.Replace-Spawn.Main-Spawn");
            if (main_spawn != null) {
                Player player = e.getPlayer();
                Bukkit.getScheduler().runTaskLater(RenewalModPackIntegrated.getPlugin(), () -> {
                    if (!main_spawn.equals("")) {
                        String[] loc = main_spawn.split(":");
                        String worldName = loc[0];
                        World world = Bukkit.getWorld(worldName);
                        if (world != null) {
                            double x = Double.parseDouble(loc[1]);
                            double y = Double.parseDouble(loc[2]);
                            double z = Double.parseDouble(loc[3]);
                            float yaw = Float.parseFloat(loc[4]);
                            float pitch = Float.parseFloat(loc[5]);
                            Location location = new Location(world, x, y ,z , yaw, pitch);
                            player.teleport(location);
                        }
                    }
                }, 1L);
            }
        }
    }

}
