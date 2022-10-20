package com.msicraft.renewalmodpackintegrated.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class TabComplete implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("modpackintegrated")) {
            if (args.length == 1) {
                List<String> arguments = new ArrayList<>();
                if (sender.isOp()) {
                    arguments.add("help");
                    arguments.add("reload");
                    arguments.add("main-spawn");
                    arguments.add("skill");
                    arguments.add("skill-point");
                    arguments.add("util");
                    arguments.add("point");
                    arguments.add("point-exp");
                    arguments.add("get-xp");
                    arguments.add("evolution");
                } else {
                    arguments.add("skill");
                    arguments.add("util");
                    arguments.add("evolution");
                }
                return arguments;
            }
            if (args.length == 2) {
                if (args[0].equals("point") || args[0].equals("point-exp")) {
                    List<String> argument = new ArrayList<>();
                    argument.add("get");
                    argument.add("set");
                    argument.add("add");

                    return argument;
                }
            }
            if (args.length == 2 && args[0].equals("skill")) {
                List<String> argument = new ArrayList<>();
                argument.add("open");
                argument.add("scroll");
                argument.add("set");

                return argument;
            }
            if (args.length == 2 && args[0].equals("skill-point")) {
                List<String> argument = new ArrayList<>();
                argument.add("set");
                argument.add("add");
                argument.add("get");

                return argument;
            }
            if (args.length == 2 && args[0].equals("main-spawn")) {
                List<String> argument = new ArrayList<>();
                argument.add("set");

                return argument;
            }
        }

        return null;
    }

}
