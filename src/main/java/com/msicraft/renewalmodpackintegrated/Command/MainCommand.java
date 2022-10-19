package com.msicraft.renewalmodpackintegrated.Command;

import com.msicraft.renewalmodpackintegrated.Mythicmob.Inventory.LearnSkillInv;
import com.msicraft.renewalmodpackintegrated.Mythicmob.Inventory.SkillSettingInv;
import com.msicraft.renewalmodpackintegrated.Mythicmob.Inventory.UtilInv;
import com.msicraft.renewalmodpackintegrated.Mythicmob.Utils.MythicMobsUtil;
import com.msicraft.renewalmodpackintegrated.RenewalModPackIntegrated;
import com.msicraft.renewalmodpackintegrated.Utils.ExpUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Random;
import java.util.UUID;

public class MainCommand implements CommandExecutor {

    private Random random = new Random();

    private MythicMobsUtil mythicMobsUtil = new MythicMobsUtil();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("modpackintegrated")) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "/modpackintegrated help");
            }
            if (args.length >= 1) {
                String command_val = args[0];
                switch (command_val) {
                    case "help" -> {
                        if (args.length == 1) {
                            sender.sendMessage(ChatColor.YELLOW + "/modpackintegrated help: " + ChatColor.WHITE + " Show Command List");
                            sender.sendMessage(ChatColor.YELLOW + "/modpackintegrated reload: " + ChatColor.WHITE + " Reload Plugin");
                        }
                    }
                    case "reload" -> {
                        if (args.length == 1) {
                            if (sender.isOp()) {
                                RenewalModPackIntegrated.getPlugin().FilesReload();
                                sender.sendMessage(RenewalModPackIntegrated.getPrefix() + ChatColor.GREEN + " Plugin reloaded");
                            }
                        }
                    }
                    case "skill" -> {
                        if (args.length == 2) {
                            if (sender instanceof Player player) {
                                switch (args[1]) {
                                    case "open" -> {
                                        LearnSkillInv learnSkillInv = new LearnSkillInv(player);
                                        player.openInventory(learnSkillInv.getInventory());
                                    }
                                    case "set" -> {
                                        SkillSettingInv skillSettingInv = new SkillSettingInv(player);
                                        player.openInventory(skillSettingInv.getInventory());
                                        Bukkit.getScheduler().runTaskLater(RenewalModPackIntegrated.getPlugin(), () -> skillSettingInv.setSlotItem(player),1L);
                                    }
                                    case "scroll" -> {
                                        MythicMobsUtil mythicMobsUtil = new MythicMobsUtil();
                                        mythicMobsUtil.getSkillItem(player);
                                    }
                                }
                            }
                        }
                    }
                    case "skill-point" -> {
                        if (args.length < 4 && !args[1].equals("get")) {
                            sender.sendMessage(ChatColor.RED + "/modpackintegrated skill-point (set,add) <player> <value>");
                        }
                        if (sender.isOp()) {
                            String val = args[1];
                            switch (val) {
                                case "set" -> {
                                    int value = Integer.parseInt(args[3]);
                                    Player target = Bukkit.getPlayer(args[2]);
                                    if (target != null) {
                                        mythicMobsUtil.setPlayerSkillPoint(target, value);
                                        sender.sendMessage(ChatColor.GREEN + "Skill point value has been changed");
                                        sender.sendMessage(ChatColor.GREEN + "Skill point: " + ChatColor.WHITE + value);
                                    }
                                }
                                case "add" -> {
                                    int value = Integer.parseInt(args[3]);
                                    Player target = Bukkit.getPlayer(args[2]);
                                    if (target != null) {
                                        int getPoint = mythicMobsUtil.getPlayerSkillPoint(target);
                                        int cal = getPoint + value;
                                        mythicMobsUtil.setPlayerSkillPoint(target, cal);
                                        sender.sendMessage(ChatColor.GREEN + "Skill points have been added");
                                        sender.sendMessage(ChatColor.GREEN + "Skill point: " + ChatColor.WHITE + cal);
                                    }
                                }
                                case "get" -> {
                                    Player target = Bukkit.getPlayer(args[2]);
                                    if (target != null) {
                                        int point = mythicMobsUtil.getPlayerSkillPoint(target);
                                        sender.sendMessage(ChatColor.GREEN + "Skill point: " + ChatColor.WHITE + point);
                                    }
                                }
                            }
                        }
                    }
                    case "point" -> {
                        if (args.length == 3 && args[1].equals("get") && sender.isOp()) { // mpi point get <player>
                            Player player = Bukkit.getPlayer(args[2]);
                            if (player != null) {
                                UUID uuid = player.getUniqueId();
                                int getPoint = RenewalModPackIntegrated.getPlugin().getPlayerPoint().get(uuid);
                                sender.sendMessage(ChatColor.GREEN + player.getName() + " Point: " + ChatColor.WHITE + getPoint);
                            } else {
                                sender.sendMessage(ChatColor.RED + "Player does not exist");
                            }
                        }
                        if (args.length == 4 && sender.isOp()) { // mpi point [set,add] <player> <value>
                            String val = args[1];
                            Player player = Bukkit.getPlayer(args[2]);
                            if (player != null) {
                                UUID uuid = player.getUniqueId();
                                switch (val) {
                                    case "set" -> {
                                        int value = Integer.parseInt(args[3]);
                                        RenewalModPackIntegrated.getPlugin().getPlayerPoint().put(uuid, value);
                                        sender.sendMessage(ChatColor.GREEN + player.getName() + " Point: " + ChatColor.WHITE + value);
                                    }
                                    case "add" -> {
                                        int value = Integer.parseInt(args[3]);
                                        int getPoint = RenewalModPackIntegrated.getPlugin().getPlayerPoint().get(uuid);
                                        int cal = getPoint + value;
                                        RenewalModPackIntegrated.getPlugin().getPlayerPoint().put(uuid, cal);
                                        sender.sendMessage(ChatColor.GREEN + player.getName() + " Point: " + ChatColor.WHITE + cal);
                                    }
                                }
                            } else {
                                sender.sendMessage(ChatColor.RED + "Player does not exist");
                            }
                        }
                    }
                    case "point-exp" -> {
                        if (args.length == 3 && args[1].equals("get") && sender.isOp()) { //mpi point-exp get <player>
                            Player player = Bukkit.getPlayer(args[2]);
                            if (player != null) {
                                UUID uuid = player.getUniqueId();
                                int exp = RenewalModPackIntegrated.getPlugin().getPlayerPointExp().get(uuid);
                                sender.sendMessage(ChatColor.GREEN + player.getName() + " Point-Exp: " + ChatColor.WHITE + exp);
                            } else {
                                sender.sendMessage(ChatColor.RED + "Player does not exist");
                            }
                        }
                        if (args.length == 4 && sender.isOp()) { //mpi point-exp [set, add] <player> <value>
                            String val = args[1];
                            Player player = Bukkit.getPlayer(args[2]);
                            if (player != null) {
                                UUID uuid = player.getUniqueId();
                                switch (val) {
                                    case "set" -> {
                                        int value = Integer.parseInt(args[3]);
                                        RenewalModPackIntegrated.getPlugin().getPlayerPointExp().put(uuid, value);
                                        sender.sendMessage(ChatColor.GREEN + player.getName() + " Point-Exp: " + ChatColor.WHITE + value);
                                    }
                                    case "add" -> {
                                        int value = Integer.parseInt(args[3]);
                                        int getPoint = RenewalModPackIntegrated.getPlugin().getPlayerPointExp().get(uuid);
                                        int cal = getPoint + value;
                                        RenewalModPackIntegrated.getPlugin().getPlayerPointExp().put(uuid, cal);
                                        sender.sendMessage(ChatColor.GREEN + player.getName() + " Point-Exp: " + ChatColor.WHITE + cal);
                                    }
                                }
                            }
                        }
                    }
                    case "get-xp" -> {
                        if (args.length == 2) { //mpi get-xp <player>
                            if (sender instanceof Player player) {
                                ExpUtil expModule = new ExpUtil();
                                Player target = Bukkit.getPlayer(args[1]);
                                if (player.isOp()) {
                                    if (target != null) {
                                        int xp = expModule.getPlayerExp(target);
                                        player.sendMessage("Level: " + target.getLevel()  + " Total xp: " + xp);
                                    }
                                }
                            }
                        }
                    }
                    case "util" -> {
                        if (args.length == 1) {
                            if (sender instanceof Player player) {
                                UtilInv utilInv = new UtilInv(player);
                                player.openInventory(utilInv.getInventory());
                                utilInv.basicMenu();
                            }
                        }
                    }
                    case "main-spawn" -> {
                        if (args.length == 2) { // mpi main-spawn set
                            if (args[1].equals("set")) {
                                if (sender instanceof Player player) {
                                    if (player.isOp()) {
                                        Location location = player.getLocation();
                                        World world = location.getWorld();
                                        if (world != null) {
                                            String worldName = location.getWorld().getName().toUpperCase();
                                            double x = location.getX();
                                            double y = location.getY();
                                            double z = location.getZ();
                                            float pitch = location.getPitch();
                                            float yaw = location.getYaw();
                                            String loc = worldName + ":" + x + ":" + y + ":" + z + ":" + yaw + ":" + pitch;
                                            RenewalModPackIntegrated.getPlugin().getConfig().set("Setting.Replace-Spawn.Main-Spawn", loc);
                                            RenewalModPackIntegrated.getPlugin().saveConfig();
                                            player.sendMessage(ChatColor.GREEN + "Change main-spawn location: ");
                                            player.sendMessage(ChatColor.GREEN + "World: " + ChatColor.WHITE + worldName);
                                            Location new_loc = new Location(world, x, y, z, yaw, pitch);
                                            world.setSpawnLocation(new_loc);
                                            int i_x = (int) x;
                                            int i_y = (int) y;
                                            int i_z = (int) z;
                                            player.sendMessage(ChatColor.GREEN + "X|Y|Z: " + ChatColor.WHITE + i_x + " | " + i_y + " | " + i_z);
                                        } else {
                                            player.sendMessage(ChatColor.RED + " Invalid World");
                                        }
                                    }
                                }
                            } else {
                                sender.sendMessage("/mpi main-spawn set");
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

}
