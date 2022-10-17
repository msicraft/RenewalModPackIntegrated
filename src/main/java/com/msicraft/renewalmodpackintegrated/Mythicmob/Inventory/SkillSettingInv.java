package com.msicraft.renewalmodpackintegrated.Mythicmob.Inventory;

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
import java.util.UUID;

public class SkillSettingInv implements InventoryHolder {

    private Inventory SkillSetInv;

    public HashMap<String, Integer> page = new HashMap<>();

    public HashMap<String, Integer> max_page = new HashMap<>();

    public SkillSettingInv(Player player) {
        SkillSetInv = Bukkit.createInventory(player, 54, player.getDisplayName() + " Skill Setting");
        setting();
    }

    private int maxSize = RenewalModPackIntegrated.registerSkill.getConfig().getStringList("Register-Skill-List").size();

    private static final int[] fill = {9,10,11,12,14,15,16,17};

    private static final int[] slots = {18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44};

    public void setting() {
        SkillSetInv.clear();;
        page_button_size();
        page_book();
        basic_button();
        //saveButton();
        setFill();
    }

    public void setSkillItem(Player player, int SlotId) {
        ItemStack itemStack = new ItemStack(Material.PAPER, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        UUID uuid = player.getUniqueId();
        ArrayList<String> learnSkillList = new ArrayList<>(RenewalModPackIntegrated.playerData.getConfig().getStringList("PlayerData." + uuid + ".Learn-Skills"));
        ArrayList<String> loreList = new ArrayList<>();
        if (!learnSkillList.isEmpty()) {
            if (itemMeta != null) {
                PersistentDataContainer data = itemMeta.getPersistentDataContainer();
                int maxLearnSkill = learnSkillList.size();
                int size = slots.length;
                int page_num = 0;
                if (page.containsKey("page")) {
                    page_num = page.get("page");
                }
                int gui_count = 0;
                int lastCount = page_num*size;
                for (int a = lastCount; a<maxLearnSkill; a++) {
                    String skillId = learnSkillList.get(a);
                    String skillName = RenewalModPackIntegrated.skillData.getConfig().getString("Skills." + skillId + ".Name");
                    if (skillName != null) {
                        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', skillName));
                    }
                    if (!loreList.isEmpty()) {
                        loreList.clear();
                    }
                    for (String lore : RenewalModPackIntegrated.skillData.getConfig().getStringList("Skills." + skillId + ".Lore")) {
                        loreList.add(ChatColor.translateAlternateColorCodes('&', lore));
                    }
                    data.set(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI_SELECT_SKILL-ID"), PersistentDataType.STRING, skillId);
                    data.set(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI_SELECT_SKILL-SLOT"), PersistentDataType.INTEGER, SlotId);
                    itemMeta.setLore(loreList);
                    itemStack.setItemMeta(itemMeta);
                    int slot = slots[a];
                    SkillSetInv.setItem(slot, itemStack);
                    gui_count++;
                    if (gui_count >= size) {
                        break;
                    }
                }
            }
        }
    }

    public void selectSlot(int slotId, String skillId, UUID uuid) {
        ArrayList<String> enableSkillList = new ArrayList<>(RenewalModPackIntegrated.playerData.getConfig().getStringList("PlayerData." + uuid + ".Skill-Slot"));
        List<String> loreList = new ArrayList<>();
        ItemStack itemStack = new ItemStack(Material.ENCHANTED_BOOK, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        String skillName = "unknown";
        if (!enableSkillList.isEmpty()) {
            String skill = enableSkillList.get(slotId);
            skillName = RenewalModPackIntegrated.skillData.getConfig().getString("Skills." + skill + ".Name");
        }
        if (itemMeta != null) {
            PersistentDataContainer data = itemMeta.getPersistentDataContainer();
            itemMeta.setDisplayName("Select Slot");
            loreList.add(ChatColor.WHITE + "Slot: " + (slotId+1));
            loreList.add("");
            loreList.add(ChatColor.WHITE + "Current Skill: " + skillName);
            itemMeta.setLore(loreList);
            data.set(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI_SELECT_SKILL"), PersistentDataType.STRING, "true");
            data.set(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI_SELECT_SKILL_SLOT"), PersistentDataType.INTEGER, slotId);
            itemStack.setItemMeta(itemMeta);
            SkillSetInv.setItem(13, itemStack);
        }
    }

    public void setSlotItem(Player player) {
        ItemStack itemStack = new ItemStack(Material.BOOK, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        UUID uuid = player.getUniqueId();
        ArrayList<String> learnSkillList = new ArrayList<>(RenewalModPackIntegrated.playerData.getConfig().getStringList("PlayerData." + uuid + ".Learn-Skills"));
        ArrayList<String> enableSkillList = new ArrayList<>(RenewalModPackIntegrated.playerData.getConfig().getStringList("PlayerData." + uuid + ".Skill-Slot"));
        String skillId = "unknown";
        if (!learnSkillList.isEmpty()) {
            skillId = learnSkillList.get(0);
        }
        String skillName = RenewalModPackIntegrated.skillData.getConfig().getString("Skills." + skillId + ".Name");
        String id = skillId;
        int enableSize = enableSkillList.size();
        if (itemMeta != null) {
            List<String> loreList = new ArrayList<>();
            PersistentDataContainer data = itemMeta.getPersistentDataContainer();
            itemMeta.setDisplayName(ChatColor.WHITE + "Slot");
            for (int a = 0; a<9; a++) {
                if (!loreList.isEmpty()) {
                    loreList.clear();
                }
                int slot = a + 1;
                loreList.add(ChatColor.BOLD + "" + ChatColor.WHITE + "[Slot " + slot +"]");
                loreList.add("");
                if (enableSkillList.isEmpty()) {
                    loreList.add(ChatColor.WHITE + "Skill Name: " + skillName);
                } else {
                    if (a > enableSize) {
                        loreList.add(ChatColor.WHITE + "Skill Name: " + skillName);
                    } else {
                        String name = enableSkillList.get(a);
                        String skill = RenewalModPackIntegrated.skillData.getConfig().getString("Skills." + name + ".Name");
                        loreList.add(ChatColor.WHITE + "Skill Name: " + skill);
                        id = skill;
                    }
                }
                if (id != null) {
                    data.set(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI_BOOK_SKILLID"), PersistentDataType.STRING, id);
                }
                data.set(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI_BOOK_SLOT"), PersistentDataType.INTEGER, a);
                itemMeta.setLore(loreList);
                itemStack.setItemMeta(itemMeta);
                SkillSetInv.setItem(a, itemStack);
            }
        }
    }

    private void setFill() {
        ItemStack itemStack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(" ");
        itemStack.setItemMeta(itemMeta);
        for (int slot : fill) {
            SkillSetInv.setItem(slot, itemStack);
        }
    }

    private void basic_button() {
        ItemStack itemStack = new ItemStack(Material.ARROW, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("Next");
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI"), PersistentDataType.STRING, "mpi_gui_next");
        itemStack.setItemMeta(itemMeta);
        SkillSetInv.setItem(50, itemStack);
        itemMeta.setDisplayName("Previous");
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI"), PersistentDataType.STRING, "mpi_gui_previous");
        itemStack.setItemMeta(itemMeta);
        SkillSetInv.setItem(48, itemStack);
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
        SkillSetInv.setItem(49, itemStack);
    }

    public void page_button_size() {
        int page_count = maxSize / 45;
        max_page.put("max-page", page_count);
    }

    public void saveButton() {
        ItemStack itemStack = new ItemStack(Material.WRITABLE_BOOK, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.WHITE + "Save");
        PersistentDataContainer data = itemMeta.getPersistentDataContainer();
        data.set(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI_SKILL_SAVE"), PersistentDataType.STRING, "MPI-Save");
        itemStack.setItemMeta(itemMeta);
        SkillSetInv.setItem(53, itemStack);
    }

    @Override
    public Inventory getInventory() {
        return SkillSetInv;
    }
}
