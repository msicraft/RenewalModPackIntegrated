package com.msicraft.renewalmodpackintegrated.Event;

import com.msicraft.renewalmodpackintegrated.RenewalModPackIntegrated;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerDisableItem implements Listener {

    @EventHandler
    public void onDisableFireWork(PlayerInteractEvent e) {
        ItemStack itemStack = e.getItem();
        Action action = e.getAction();
        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            if (itemStack != null && itemStack.getType() == Material.FIREWORK_ROCKET) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBoneMealInteract(PlayerInteractEvent e) {
        boolean check = RenewalModPackIntegrated.getPlugin().getConfig().getBoolean("Setting.Disable-BoneMeal.Enabled");
        if (check) {
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getItem() != null && e.getItem().getType() == Material.BONE_MEAL) {
                e.setCancelled(true);
            }
        }
    }

}
