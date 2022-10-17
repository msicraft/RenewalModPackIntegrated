package com.msicraft.renewalmodpackintegrated.Mythicmob.Utils;

import com.msicraft.renewalmodpackintegrated.RenewalModPackIntegrated;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerPointCheckUtil {

    private PlayerUpgradeUtil playerUpgradeUtil = new PlayerUpgradeUtil();

    private ArrayList<String> uuidList = new ArrayList<>(RenewalModPackIntegrated.getPlugin().getDataUUIDList());

    private int maxPointValue = RenewalModPackIntegrated.getPlugin().getConfig().getInt("Player-Point-Setting.Get-Point-Value");

    public void playerPointCheck() {
        long start = System.nanoTime();
        for (String s : uuidList) {
            UUID uuid = UUID.fromString(s);
            int currentPoint = RenewalModPackIntegrated.getPlugin().getPlayerPoint().get(uuid);
            int currentPointExp = RenewalModPackIntegrated.getPlugin().getPlayerPointExp().get(uuid);
            if (currentPointExp >= maxPointValue) {
                int leftExp = currentPointExp - maxPointValue;
                int calPoint = currentPoint + 1;
                playerUpgradeUtil.setPoint(uuid, calPoint);
                playerUpgradeUtil.setPointExp(uuid, leftExp);
            }
        }
        long end = System.nanoTime();
        if (RenewalModPackIntegrated.getPlugin().getConfig().getBoolean("Debug.Enabled")) {
            long finalCal = (end - start) / 1000000000;
            System.out.println(RenewalModPackIntegrated.getPrefix() + ChatColor.GREEN + " Player Point Check" + ChatColor.WHITE + " [" + finalCal + " s]");
        }
    }

}
