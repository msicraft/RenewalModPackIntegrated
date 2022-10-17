package com.msicraft.renewalmodpackintegrated.Event;

import com.msicraft.renewalmodpackintegrated.RenewalModPackIntegrated;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEnterEvent;

public class EntityVehicleEvent implements Listener {

    @EventHandler
    public void onMountEvent(VehicleEnterEvent e) {
        boolean check = RenewalModPackIntegrated.getPlugin().getConfig().getBoolean("Setting.Disable-Mount");
        if (check) {
            EntityType entityType = e.getEntered().getType();
            if (entityType != EntityType.PLAYER) {
                e.setCancelled(true);
            }
        }
    }

}
