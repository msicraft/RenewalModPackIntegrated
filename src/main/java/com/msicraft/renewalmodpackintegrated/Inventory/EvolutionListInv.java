package com.msicraft.renewalmodpackintegrated.Inventory;

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
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EvolutionListInv implements InventoryHolder {

    private Inventory evolutionListInv;

    public HashMap<String, Integer> page = new HashMap<>();

    public HashMap<String, Integer> max_page = new HashMap<>();

    private int maxSize = RenewalModPackIntegrated.getPlugin().getEtcEvolutionList().size();

    public EvolutionListInv(Player player) {
        evolutionListInv = Bukkit.createInventory(player, 54, "Evolution List");
        setInv();
    }

    public void setInv() {
        evolutionListInv.clear();
        page_button_size();
        page_book();
        basic_button();
        int page_num = 0;
        if (page.containsKey("page")) {
            page_num = page.get("page");
        }
        int gui_count = 0;
        int gui_max = 45;
        int lastCount = page_num*45;
        ArrayList<String> entityNameList = new ArrayList<>(RenewalModPackIntegrated.getPlugin().getEtcEvolutionList());
        for (int a = lastCount; a<maxSize; a++) {
            List<String> loreList = new ArrayList<>();
            String entityName = entityNameList.get(a);
            ItemStack itemStack = new ItemStack(Material.BOOK, 1);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(entityName);
            int evolutionCount = RenewalModPackIntegrated.entityScaleData.getConfig().getInt("Etc." + entityName + ".Count");
            double lastHealth = Math.round(RenewalModPackIntegrated.entityScaleData.getConfig().getDouble("Etc." + entityName + ".Last-Health") * 1000) / 1000.0;
            double lastDamage = Math.round(RenewalModPackIntegrated.entityScaleData.getConfig().getDouble("Etc." + entityName + ".Last-Damage") * 1000) / 1000.0;
            double lastArmor = Math.round(RenewalModPackIntegrated.entityScaleData.getConfig().getDouble("Etc." + entityName + ".Last-Armor") * 1000) / 1000.0;
            double lastArmorToughness = Math.round(RenewalModPackIntegrated.entityScaleData.getConfig().getDouble("Etc." + entityName + ".Last-ArmorToughness") * 1000) / 1000.0;
            loreList.add(ChatColor.GREEN + "Evolution Count: " + ChatColor.WHITE + evolutionCount);
            loreList.add("");
            loreList.add(ChatColor.GREEN + "Last Health" + ChatColor.WHITE + lastHealth);
            loreList.add(ChatColor.GREEN + "Last Damage" + ChatColor.WHITE + lastDamage);
            loreList.add(ChatColor.GREEN + "Last Armor" + ChatColor.WHITE + lastArmor);
            loreList.add(ChatColor.GREEN + "Last ArmorToughness" + ChatColor.WHITE + lastArmorToughness);
            itemMeta.setLore(loreList);
            itemStack.setItemMeta(itemMeta);
            evolutionListInv.setItem(gui_count, itemStack);
            gui_count++;
            if (gui_count >= gui_max) {
                break;
            }
        }
    }

    private void basic_button() {
        ItemStack itemStack = new ItemStack(Material.ARROW, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("Next");
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "mpi_evolution"), PersistentDataType.STRING, "mpi_evolution_next");
        itemStack.setItemMeta(itemMeta);
        evolutionListInv.setItem(50, itemStack);
        itemMeta.setDisplayName("Previous");
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "mpi_evolution"), PersistentDataType.STRING, "mpi_evolution_previous");
        itemStack.setItemMeta(itemMeta);
        evolutionListInv.setItem(48, itemStack);
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
        evolutionListInv.setItem(49, itemStack);
    }

    public void page_button_size() {
        int page_count = maxSize/45;
        max_page.put("max-page", page_count);
    }


    @Override
    public Inventory getInventory() {
        return evolutionListInv;
    }

}
