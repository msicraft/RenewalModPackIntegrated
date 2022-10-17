package com.msicraft.renewalmodpackintegrated.Mythicmob.Tasks;

import com.msicraft.renewalmodpackintegrated.Mythicmob.Utils.PlayerPointCheckUtil;
import com.msicraft.renewalmodpackintegrated.RenewalModPackIntegrated;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerPointTask extends BukkitRunnable{

    RenewalModPackIntegrated plugin;

    public PlayerPointTask(RenewalModPackIntegrated plugin) {
        this.plugin = plugin;
    }

    private PlayerPointCheckUtil playerPointCheckUtil = new PlayerPointCheckUtil();

    @Override
    public void run() {
        playerPointCheckUtil.playerPointCheck();
    }

}
