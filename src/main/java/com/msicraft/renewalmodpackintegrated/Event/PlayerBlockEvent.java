package com.msicraft.renewalmodpackintegrated.Event;

import com.msicraft.renewalmodpackintegrated.RenewalModPackIntegrated;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlayerBlockEvent implements Listener {

    @EventHandler
    public void DisableBreakSpawner(BlockBreakEvent e) {
        if (RenewalModPackIntegrated.getPlugin().getConfig().getBoolean("Setting.Disable-Spawner")) {
            if (e.getBlock().getType() == Material.SPAWNER) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void DisablePlaceSpawner(BlockPlaceEvent e) {
        if (RenewalModPackIntegrated.getPlugin().getConfig().getBoolean("Setting.Disable-Spawner")) {
            if (e.getBlock().getType() == Material.SPAWNER) {
                e.setCancelled(true);
            }
        }
    }

}
