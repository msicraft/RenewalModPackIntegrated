package com.msicraft.renewalmodpackintegrated.Mythicmob.CustomItem;

import com.msicraft.renewalmodpackintegrated.RenewalModPackIntegrated;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class CustomItem {

    public ItemStack getPlayerPointPaper(int value) {
        ItemStack itemStack = new ItemStack(Material.PAPER, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer data = itemMeta.getPersistentDataContainer();
        itemMeta.setDisplayName(ChatColor.WHITE + "Player Point");
        data.set(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI_POINT-ITEM"), PersistentDataType.STRING, "MPI_POINT-ITEM");
        data.set(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI_POINT-ITEM-VALUE"), PersistentDataType.INTEGER, value);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
