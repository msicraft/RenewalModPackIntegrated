package com.msicraft.renewalmodpackintegrated.Inventory.Event;

import com.msicraft.renewalmodpackintegrated.Inventory.EvolutionListInv;
import com.msicraft.renewalmodpackintegrated.RenewalModPackIntegrated;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;

public class EvolutionListInvEvent implements Listener {

    public HashMap<String, Integer> page_count = new HashMap<>();

    @EventHandler
    public void onEvolutionListClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) {
            return;
        }
        if (e.getView().getTitle().equalsIgnoreCase("Evolution List")) {
            if (e.getInventory().getHolder() == e.getWhoClicked()) {
                e.setCancelled(true);
                Player player = (Player) e.getWhoClicked();
                if (e.getCurrentItem() == null) {
                    return;
                }
                EvolutionListInv evolutionListInv = new EvolutionListInv(player);
                ItemStack itemStack = e.getCurrentItem();
                ItemMeta itemMeta = itemStack.getItemMeta();
                int max = evolutionListInv.max_page.get("max-page");
                if (!page_count.containsKey("page")) {
                    page_count.put("page", 0);
                }
                if (itemMeta != null) {
                    PersistentDataContainer data = itemMeta.getPersistentDataContainer();
                    if (e.isLeftClick() && itemStack.getType() == Material.ARROW) {
                        if (data.has(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "mpi_evolution"), PersistentDataType.STRING)) {
                            String dataVal = data.get(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "mpi_evolution"), PersistentDataType.STRING);
                            if (dataVal != null) {
                                switch (dataVal) {
                                    case "mpi_evolution_next" -> {
                                        int current_page = page_count.get("page");
                                        int next_page = current_page + 1;
                                        if (next_page > max) {
                                            next_page = 0;
                                        }
                                        page_count.put("page", next_page);
                                        evolutionListInv.page.put("page", next_page);
                                    }
                                    case "mpi_evolution_previous" -> {
                                        int current_page = page_count.get("page");
                                        int next_page = current_page - 1;
                                        if (next_page < 0) {
                                            next_page = max;
                                        }
                                        page_count.put("page", next_page);
                                        evolutionListInv.page.put("page", next_page);
                                    }
                                }
                                player.closeInventory();
                                player.openInventory(evolutionListInv.getInventory());
                                evolutionListInv.setInv();
                            }
                        }
                    }
                }
            }
        }
    }

}
