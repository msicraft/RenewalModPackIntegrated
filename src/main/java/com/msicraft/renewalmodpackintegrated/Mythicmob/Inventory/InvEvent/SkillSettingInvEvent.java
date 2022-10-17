package com.msicraft.renewalmodpackintegrated.Mythicmob.Inventory.InvEvent;

import com.msicraft.renewalmodpackintegrated.Mythicmob.Inventory.SkillSettingInv;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class SkillSettingInvEvent implements Listener {

    public HashMap<String, Integer> page_count = new HashMap<>();

    private HashMap<String, Integer> skillCount = new HashMap<>();

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (e.getClickedInventory() == null) {
            return;
        }
        //if (e.getClickedInventory().getHolder() instanceof SkillSettingInv skillSettingInv)
        if (e.getView().getTitle().equalsIgnoreCase(player.getDisplayName() + " Skill Setting")) {
            e.setCancelled(true);
            UUID uuid = player.getUniqueId();
            if (e.getCurrentItem() == null) {
                return;
            }
            SkillSettingInv skillSettingInv = new SkillSettingInv(player);
            Material material = e.getCurrentItem().getType();
            ItemStack itemStack = e.getCurrentItem();
            ItemMeta itemMeta = itemStack.getItemMeta();
            int max = skillSettingInv.max_page.get("max-page");
            if (itemMeta != null) {
                PersistentDataContainer data = itemMeta.getPersistentDataContainer();
                if (!page_count.containsKey("page")) {
                    page_count.put("page", 0);
                }
                if (e.isLeftClick() && material == Material.ARROW) {
                    if (data.has(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI"), PersistentDataType.STRING)) {
                        String dataContainer = data.get(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI"), PersistentDataType.STRING);
                        if (dataContainer != null) {
                            switch (dataContainer) {
                                case "mpi_gui_next" -> {
                                    int current_page = page_count.get("page");
                                    int next_page = current_page + 1;
                                    if (next_page > max) {
                                        next_page = 0;
                                    }
                                    page_count.put("page", next_page);
                                    skillSettingInv.page.put("page", next_page);
                                }
                                case "mpi_gui_previous" -> {
                                    int current_page = page_count.get("page");
                                    int next_page = current_page - 1;
                                    if (next_page < 0) {
                                        next_page = max;
                                    }
                                    page_count.put("page", next_page);
                                    skillSettingInv.page.put("page", next_page);
                                }
                            }
                            player.closeInventory();
                            //player.openInventory(skillSettingInv.getInventory());
                            //skillSettingInv.setting();
                            //skillSettingInv.setSlotItem(player);
                        }
                    }
                }
                int slot = e.getSlot();
                if (e.isLeftClick() && material == Material.BOOK) {
                    if (slot >= 0 && slot <= 8) {
                        ItemStack bookItem = e.getCurrentItem();
                        ItemMeta bookMeta = bookItem.getItemMeta();
                        if (bookMeta != null) {
                            PersistentDataContainer bookSlotId = bookMeta.getPersistentDataContainer();
                            int getSkillSlot = bookSlotId.get(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI_BOOK_SLOT"), PersistentDataType.INTEGER);
                            String id = bookSlotId.get(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI_BOOK_SKILLID"), PersistentDataType.STRING);
                            player.closeInventory();
                            player.openInventory(skillSettingInv.getInventory());
                            skillSettingInv.setting();
                            skillSettingInv.setSlotItem(player);
                            skillSettingInv.selectSlot(getSkillSlot, id, uuid);
                            skillSettingInv.setSkillItem(player, getSkillSlot);
                        }
                    }
                }
                if (e.isLeftClick() && material == Material.PAPER) {
                    if (slot >= 18 && slot <= 44) {
                        ArrayList<String> enableSkillList = new ArrayList<>(RenewalModPackIntegrated.playerData.getConfig().getStringList("PlayerData." + uuid + ".Skill-Slot"));
                        ItemStack selectItem = e.getCurrentItem();
                        ItemMeta selectItemMeta = selectItem.getItemMeta();
                        if (selectItemMeta != null) {
                            PersistentDataContainer selectItemData = selectItemMeta.getPersistentDataContainer();
                            if (selectItemData.has(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI_SELECT_SKILL-ID"), PersistentDataType.STRING)) {
                                String skillId = selectItemData.get(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI_SELECT_SKILL-ID"), PersistentDataType.STRING);
                                int slotId = selectItemData.get(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI_SELECT_SKILL-SLOT"), PersistentDataType.INTEGER);
                                enableSkillList.set(slotId, skillId);
                                RenewalModPackIntegrated.playerData.getConfig().set("PlayerData." + uuid + ".Skill-Slot", enableSkillList);
                                RenewalModPackIntegrated.playerData.saveConfig();
                                player.closeInventory();
                                player.openInventory(skillSettingInv.getInventory());
                                skillSettingInv.setting();
                                skillSettingInv.setSlotItem(player);
                            }
                        }
                    }
                }
                if (e.isLeftClick() && material == Material.WRITABLE_BOOK && e.getSlot() == 53) {
                    ArrayList<String> enableSkillList = new ArrayList<>(RenewalModPackIntegrated.playerData.getConfig().getStringList("PlayerData." + uuid + ".Skill-Slot"));
                    ArrayList<String> replaceSkill = new ArrayList<>();
                    if (enableSkillList.isEmpty()) {
                        for (int a = 0; a < 9; a++) {
                            ItemStack skillSlot = e.getClickedInventory().getItem(a);
                            if (skillSlot != null) {
                                ItemMeta skillMeta = skillSlot.getItemMeta();
                                if (skillMeta != null) {
                                    PersistentDataContainer skilldata = skillMeta.getPersistentDataContainer();
                                    String skillID = skilldata.get(new NamespacedKey(RenewalModPackIntegrated.getPlugin(), "MPI_BOOK_SKILLID"), PersistentDataType.STRING);
                                    replaceSkill.add(skillID);
                                }
                            }
                        }
                        RenewalModPackIntegrated.playerData.getConfig().set("PlayerData." + uuid + ".Skill-Slot", replaceSkill);
                        RenewalModPackIntegrated.playerData.saveConfig();
                        player.closeInventory();
                    } else {
                        player.closeInventory();
                    }
                }
            }
        }
    }

}
