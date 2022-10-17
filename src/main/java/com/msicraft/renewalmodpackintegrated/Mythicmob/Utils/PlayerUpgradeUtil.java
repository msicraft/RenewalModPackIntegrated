package com.msicraft.renewalmodpackintegrated.Mythicmob.Utils;

import com.msicraft.renewalmodpackintegrated.RenewalModPackIntegrated;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerUpgradeUtil {

    private void loadYmlToMap(UUID uuid) {
        if (RenewalModPackIntegrated.playerData.getConfig().contains("PlayerData." + uuid + ".Points")) {
            int point = RenewalModPackIntegrated.playerData.getConfig().getInt("PlayerData." + uuid + ".Points");
            setPoint(uuid, point);
        }
        if (RenewalModPackIntegrated.playerData.getConfig().contains("PlayerData." + uuid + ".Points-Exp")) {
            int exp = RenewalModPackIntegrated.playerData.getConfig().getInt("PlayerData." + uuid + ".Points-Exp");
            setPointExp(uuid, exp);
        }
    }

    private void saveMapToYml(UUID uuid) {
        if (RenewalModPackIntegrated.getPlugin().getPlayerPoint().containsKey(uuid)) {
            int point = getPoint(uuid);
            RenewalModPackIntegrated.playerData.getConfig().set("PlayerData." + uuid + ".Points", point);
        }
        if (RenewalModPackIntegrated.getPlugin().getPlayerPointExp().containsKey(uuid)) {
            int exp = getPointExp(uuid);
            RenewalModPackIntegrated.playerData.getConfig().set("PlayerData." + uuid + ".Points-Exp", exp);
        }
        RenewalModPackIntegrated.playerData.saveConfig();
    }

    public void playerDataLoad() {
        ArrayList<String> list = new ArrayList<>(RenewalModPackIntegrated.getPlugin().getDataUUIDList());
        for (String s : list) {
            loadYmlToMap(UUID.fromString(s));
        }
    }

    public void playerDataSave() {
        ArrayList<String> list = new ArrayList<>(RenewalModPackIntegrated.getPlugin().getDataUUIDList());
        for (String s : list) {
            saveMapToYml(UUID.fromString(s));
        }
    }

    public int getPoint(UUID uuid) {
        int point = 0;
        if (RenewalModPackIntegrated.getPlugin().getPlayerPoint().containsKey(uuid)) {
            point = RenewalModPackIntegrated.getPlugin().getPlayerPoint().get(uuid);
        }
        return point;
    }

    public void setPoint(UUID uuid, int point) {
        RenewalModPackIntegrated.getPlugin().getPlayerPoint().put(uuid, point);
    }

    public int getPointExp(UUID uuid) {
        int exp = 0;
        if (RenewalModPackIntegrated.getPlugin().getPlayerPointExp().containsKey(uuid)) {
            exp = RenewalModPackIntegrated.getPlugin().getPlayerPointExp().get(uuid);
        }
        return exp;
    }

    public void setPointExp(UUID uuid, int exp) {
        RenewalModPackIntegrated.getPlugin().getPlayerPointExp().put(uuid, exp);
    }
}
