package com.msicraft.renewalmodpackintegrated.Event;

import com.msicraft.renewalmodpackintegrated.RenewalModPackIntegrated;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDeathPenalty implements Listener {

    private Map<UUID, Integer> M_food_level = new HashMap<>();
    private Map<UUID, Double> M_saturation_level = new HashMap<>();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        boolean check_enable = RenewalModPackIntegrated.getPlugin().getConfig().getBoolean("Setting.Death-Penalty.Enabled");
        Player player = e.getEntity();
        if (check_enable) {
            if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) {
                UUID uuid = player.getUniqueId();
                int food_level = player.getFoodLevel();
                Double saturation_level = (double) player.getSaturation();
                M_food_level.put(uuid, food_level);
                M_saturation_level.put(uuid, saturation_level);
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        boolean check_enable = RenewalModPackIntegrated.getPlugin().getConfig().getBoolean("Setting.Death-Penalty.Enabled");
        Player player = e.getPlayer();
        if (check_enable) {
            if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) {
                UUID uuid = player.getUniqueId();
                if (M_food_level.containsKey(uuid) && M_saturation_level.containsKey(uuid)) {
                    int food_level = M_food_level.get(uuid);
                    double saturation_level = M_saturation_level.get(uuid);
                    //
                    double h_percentage = RenewalModPackIntegrated.getPlugin().getConfig().getDouble("Setting.Death-Penalty.Health-Percentage");
                    double f_percentage = RenewalModPackIntegrated.getPlugin().getConfig().getDouble("Setting.Death-Penalty.Food-Percentage");
                    double s_percentage = RenewalModPackIntegrated.getPlugin().getConfig().getDouble("Setting.Death-Penalty.Saturation-Percentage");
                    //
                    double final_health_value = player.getHealth() * h_percentage;
                    int final_food_value = (int) (food_level * f_percentage);
                    double final_saturation_value = saturation_level * s_percentage;
                    //
                    int min_health = RenewalModPackIntegrated.getPlugin().getConfig().getInt("Setting.Death-Penalty.Min-Health");
                    int min_food_level = RenewalModPackIntegrated.getPlugin().getConfig().getInt("Setting.Death-Penalty.Min-Food-Level");
                    //
                    Bukkit.getScheduler().runTaskLater(RenewalModPackIntegrated.getPlugin(), () -> {
                        if (final_health_value <= min_health) {
                            player.setHealth(min_health);
                        } else {
                            player.setHealth(final_health_value);
                        }
                        player.setFoodLevel(Math.max(final_food_value, min_food_level));
                        player.setSaturation((float) final_saturation_value);
                    }, 5L);
                }
            }
        }
    }
}
