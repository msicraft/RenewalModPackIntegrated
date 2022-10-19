package com.msicraft.renewalmodpackintegrated;

import com.msicraft.renewalmodpackintegrated.Command.MainCommand;
import com.msicraft.renewalmodpackintegrated.Command.TabComplete;
import com.msicraft.renewalmodpackintegrated.Event.*;
import com.msicraft.renewalmodpackintegrated.FlatFileData.*;
import com.msicraft.renewalmodpackintegrated.Mythicmob.Inventory.InvEvent.LearnSkillInvEvent;
import com.msicraft.renewalmodpackintegrated.Mythicmob.Inventory.InvEvent.SkillSettingInvEvent;
import com.msicraft.renewalmodpackintegrated.Mythicmob.Inventory.InvEvent.UtilInvEvent;
import com.msicraft.renewalmodpackintegrated.Mythicmob.PlayerPointEvent;
import com.msicraft.renewalmodpackintegrated.Mythicmob.RpgKillEntityEvent;
import com.msicraft.renewalmodpackintegrated.Mythicmob.RpgPlayerJoinEvent;
import com.msicraft.renewalmodpackintegrated.Mythicmob.SkillItemEvent;
import com.msicraft.renewalmodpackintegrated.Mythicmob.Tasks.PlayerPointTask;
import com.msicraft.renewalmodpackintegrated.Mythicmob.Tasks.SkillSelectTask;
import com.msicraft.renewalmodpackintegrated.Mythicmob.Utils.MythicMobsUtil;
import com.msicraft.renewalmodpackintegrated.Mythicmob.Utils.PlayerUpgradeUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public final class RenewalModPackIntegrated extends JavaPlugin {

    private static RenewalModPackIntegrated plugin;

    public static RegisterSkill registerSkill;

    public static SkillData skillData;

    public static PlayerData playerData;

    public static PlayerSkillData playerSkillData;

    public static EntityScaleData entityScaleData;

    public static EntityData entityData;

    protected FileConfiguration config;

    public static String getPrefix() {
        return "[Renewal ModPack Integrated]";
    }

    public static RenewalModPackIntegrated getPlugin() {
        return plugin;
    }

    private PluginManager pluginManager = Bukkit.getServer().getPluginManager();

    private PlayerUpgradeUtil playerUpgradeUtil = new PlayerUpgradeUtil();

    public HashMap<UUID, Integer> scroll_SKillId = new HashMap<>();

    private HashMap<String, Long> skillCooldown = new HashMap<>();
    private HashMap<UUID, Integer> playerPoint = new HashMap<>();
    private HashMap<UUID, Integer> playerPointExp = new HashMap<>();

    public HashMap<String, Long> getSkillCooldown() {
        return skillCooldown;
    }
    public HashMap<UUID, Integer> getPlayerPoint() {
        return playerPoint;
    }
    public HashMap<UUID, Integer> getPlayerPointExp() {
        return playerPointExp;
    }

    public ArrayList<String> getDataUUIDList() {
        ArrayList<String> list = new ArrayList<>();
        ConfigurationSection configurationSection = playerData.getConfig().getConfigurationSection("PlayerData");
        if (configurationSection != null) {
            Set<String> uuid = configurationSection.getKeys(false);
            list.addAll(uuid);
        }
        return list;
    }

    @Override
    public void onEnable() {
        createFiles();
        registerSkill = new RegisterSkill(this);
        skillData = new SkillData(this);
        playerData = new PlayerData(this);
        playerSkillData = new PlayerSkillData(this);
        entityScaleData = new EntityScaleData(this);
        entityData = new EntityData(this);
        plugin = this;
        FilesReload();
        final int configVersion = plugin.getConfig().contains("config-version", true) ? plugin.getConfig().getInt("config-version") : -1;
        if (configVersion != 1) {
            getServer().getConsoleSender().sendMessage(ChatColor.RED + getPrefix() + " You are using the old config");
            getServer().getConsoleSender().sendMessage(ChatColor.RED + getPrefix() +" Created the latest config.yml after replacing the old config.yml with config_old.yml");
            replaceconfig();
            createFiles();
        } else {
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + getPrefix() + " You are using the latest version of config.yml");
        }
        boolean mythicmobs = plugin.getConfig().getBoolean("Compatibility.Mythicmobs");
        if (mythicmobs) {
            if (getServer().getPluginManager().getPlugin("MythicMobs") != null) {
                registerMythicMobsEvent();
                MythicMobsUtil mythicMobsUtil = new MythicMobsUtil();
                mythicMobsUtil.SkillIdentification();
            }
        }
        registerCommand();
        registerEventList();
        Bukkit.getServer().getConsoleSender().sendMessage(getPrefix() + ChatColor.GREEN + " Plugin Enabled");
    }

    @Override
    public void onDisable() {
        playerUpgradeUtil.playerDataSave();
        getServer().getConsoleSender().sendMessage(getPrefix() + ChatColor.GREEN + " Saving PlayerData");
        Bukkit.getServer().getConsoleSender().sendMessage(getPrefix() + ChatColor.RED + " Plugin disabled");
    }

    private void registerCommand() {
        PluginCommand mpiCommand = Bukkit.getServer().getPluginCommand("modpackintegrated");
        if (mpiCommand != null) {
            mpiCommand.setExecutor(new MainCommand());
            mpiCommand.setTabCompleter(new TabComplete());
        }
    }

    private void registerEventList() {
        pluginManager.registerEvents(new EntityVehicleEvent(), this);
        pluginManager.registerEvents(new PlayerDeathPenalty(), this);
        pluginManager.registerEvents(new PlayerReplaceSpawn(), this);
        pluginManager.registerEvents(new PlayerDisableItem(), this);
        pluginManager.registerEvents(new EntityDamage(), this);
        pluginManager.registerEvents(new PlayerBlockEvent(), this);
        pluginManager.registerEvents(new EtcEntityScalingEvent(), this);
        pluginManager.registerEvents(new PlayerExp(), this);
    }

    private void registerMythicMobsEvent() {
        pluginManager.registerEvents(new RpgPlayerJoinEvent(), this);
        pluginManager.registerEvents(new LearnSkillInvEvent(), this);
        pluginManager.registerEvents(new SkillSettingInvEvent(), this);
        pluginManager.registerEvents(new UtilInvEvent(), this);
        pluginManager.registerEvents(new SkillItemEvent(), this);
        pluginManager.registerEvents(new RpgKillEntityEvent(), this);
        pluginManager.registerEvents(new PlayerPointEvent(), this);
    }

    private void setTask() {
        cancelTask();
        Bukkit.getScheduler().runTaskLater(getPlugin(), this::task, 20L);
    }

    private void cancelTask() {
        Bukkit.getScheduler().cancelTasks(getPlugin());
    }

    private void task() {
        int checkInterval = plugin.getConfig().getInt("RpgSetting.ActionBar-Interval");
        BukkitTask selectSkillTask = new SkillSelectTask(getPlugin()).runTaskTimer(getPlugin(), 0, checkInterval);
        BukkitTask playerPointTask = new PlayerPointTask(getPlugin()).runTaskTimerAsynchronously(getPlugin(), 0, 200);
        boolean check = getPlugin().getConfig().getBoolean("Debug.Enabled");
        if (check) {
            ArrayList<Integer> taskList = new ArrayList<>();
            taskList.add(selectSkillTask.getTaskId());
            taskList.add(playerPointTask.getTaskId());
            Bukkit.getServer().getConsoleSender().sendMessage(getPrefix() + ChatColor.GREEN + " Register Task: " + ChatColor.WHITE + taskList);
        }
    }

    public void create_config_files() {
        File configf = new File(getDataFolder(), "config.yml");

        if (!configf.exists()) {
            configf.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }
        FileConfiguration config = new YamlConfiguration();

        try {
            config.load(configf);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void replaceconfig() {
        File file = new File(getDataFolder(), "config.yml");
        this.config = YamlConfiguration.loadConfiguration(file);
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        File config_old = new File(getDataFolder(),"config_old-" + dateFormat.format(date) + ".yml");
        file.renameTo(config_old);
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + getPrefix() + " Plugin replaced the old config.yml with config_old.yml and created a new config.yml");
    }

    public void createFiles() {
        create_config_files();
    }

    public void FilesReload() {
        playerUpgradeUtil.playerDataSave();
        getDataUUIDList();
        getPlugin().reloadConfig();
        registerSkill.reloadConfig();
        skillData.reloadConfig();
        entityScaleData.reloadConfig();
        playerUpgradeUtil.playerDataLoad();
        boolean mythicmobs = plugin.getConfig().getBoolean("Compatibility.Mythicmobs");
        if (mythicmobs) {
            if (getServer().getPluginManager().getPlugin("MythicMobs") != null) {
                MythicMobsUtil mythicMobsUtil = new MythicMobsUtil();
                mythicMobsUtil.SkillIdentification();
                mythicMobsUtil.registerScrollId();
            }
            setTask();
        }
    }

}
