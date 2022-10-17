package com.msicraft.renewalmodpackintegrated.Mythicmob.Inventory;

import com.msicraft.renewalmodpackintegrated.Mythicmob.Utils.MythicMobsUtil;
import com.msicraft.renewalmodpackintegrated.RenewalModPackIntegrated;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LearnSkillInv implements InventoryHolder {

    private Inventory LearnSkillInv;

    public HashMap<String, Integer> page = new HashMap<>();

    public HashMap<String, Integer> max_page = new HashMap<>();

    public LearnSkillInv(Player player) {
        LearnSkillInv = Bukkit.createInventory(this, 54, "Skill List");
        LearnSkillInv.clear();
        page_button_size();
        page_book();
        basic_button(player);
        setInv();
    }

    private MythicMobsUtil mythicMobsUtil = new MythicMobsUtil();

    private int maxSize = RenewalModPackIntegrated.registerSkill.getConfig().getStringList("Register-Skill-List").size();

    public void setInv() {
        int page_num = 0;
        if (page.containsKey("page")) {
            page_num = page.get("page");
        }
        int gui_count = 0;
        int gui_max = 45;
        int lastCount = page_num*45;
        List<String> skillList = RenewalModPackIntegrated.registerSkill.getConfig().getStringList("Register-Skill-List");
        for (int a = lastCount; a<maxSize; a++) {
            ItemStack itemStack = new ItemStack(Material.PAPER, 1);
            ItemMeta itemMeta = itemStack.getItemMeta();
            PersistentDataContainer data = itemMeta.getPersistentDataContainer();
            List<String> loreList = new ArrayList<>();
            String b = skillList.get(a);
            String[] c = b.split(":");
            String skill_id = c[0];
            int point = Integer.parseInt(c[1]);
            if (mythicMobsUtil.isSkillExist(skill_id)) {
                String name = RenewalModPackIntegrated.skillData.getConfig().getString("Skills." + skill_id + ".Name");
                String cooldown = RenewalModPackIntegrated.skillData.getConfig().getString("Skills." + skill_id + ".CoolDown");
                if (cooldown == null) {
                    cooldown = "6";
                }
                loreList.add(ChatColor.YELLOW + "Required Skill Point: " + ChatColor.WHITE + point);
                loreList.add(ChatColor.YELLOW + "CoolDown: " + ChatColor.WHITE + cooldown);
                loreList.add("");
                loreList.add(ChatColor.YELLOW + "Left Click: " + ChatColor.GREEN + "Learn Skill ");
                //loreList.add(ChatColor.YELLOW + "Right Click:" + ChatColor.RED + " Remove Skill ");
                loreList.add("");
                loreList.add(ChatColor.RED + "" + ChatColor.BOLD+ "Skills can only be used offhand");
                loreList.add("");
                loreList.add(ChatColor.WHITE + "" + ChatColor.BOLD +"[Description]");
                for (String lore : RenewalModPackIntegrated.skillData.getConfig().getStringList("Skills." + skill_id + ".Lore")) {
                    loreList.add(ChatColor.translateAlternateColorCodes('&', lore));
                }
                if (name != null) {
                    itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
                }
                data.set(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI_Skill"), PersistentDataType.STRING, skill_id);
                data.set(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI_SkillPoint"), PersistentDataType.STRING, String.valueOf(point));
                itemMeta.setLore(loreList);
                itemStack.setItemMeta(itemMeta);
                LearnSkillInv.setItem(gui_count, itemStack);
                gui_count++;
            }
            if (gui_count >= gui_max) {
                break;
            }
        }
    }


    private void basic_button(Player player) {
        ItemStack itemStack = new ItemStack(Material.ARROW, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("Next");
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI"), PersistentDataType.STRING, "mpi_gui_next");
        itemStack.setItemMeta(itemMeta);
        LearnSkillInv.setItem(50, itemStack);
        itemMeta.setDisplayName("Previous");
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI"), PersistentDataType.STRING, "mpi_gui_previous");
        itemStack.setItemMeta(itemMeta);
        LearnSkillInv.setItem(48, itemStack);
        ItemStack getPoint = new ItemStack(Material.BOOK, 1);
        List<String> loreList = new ArrayList<>();
        ItemMeta getPointMeta = getPoint.getItemMeta();
        getPointMeta.setDisplayName(ChatColor.GREEN + "Player Skill Point");
        loreList.add("");
        loreList.add(ChatColor.GREEN + "Current Skill Point: " + ChatColor.WHITE + mythicMobsUtil.getPlayerSkillPoint(player));
        getPointMeta.setLore(loreList);
        getPoint.setItemMeta(getPointMeta);
        LearnSkillInv.setItem(45, getPoint);
    }

    public void page_book() {
        ItemStack itemStack = new ItemStack(Material.BOOK, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        String get_page = String.valueOf(page.get("page"));
        if (get_page.equals("null")) {
            get_page = "0";
        }
        int page = Integer.parseInt(get_page) + 1;
        itemMeta.setDisplayName("Page: " + page);
        itemStack.setItemMeta(itemMeta);
        LearnSkillInv.setItem(49, itemStack);
    }

    public void page_button_size() {
        int page_count = maxSize/45;
        max_page.put("max-page", page_count);
    }

    @Override
    public Inventory getInventory() {
        return LearnSkillInv;
    }

}
