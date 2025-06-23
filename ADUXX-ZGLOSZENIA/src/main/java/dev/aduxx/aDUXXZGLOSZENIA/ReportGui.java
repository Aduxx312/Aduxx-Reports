package dev.aduxx.aDUXXZGLOSZENIA;

import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ReportGui implements Listener {

    public static void openReportGUI(Player player, String targetName) {
        FileConfiguration config = Main.getInstance().getConfig();
        String rawTitle = config.getString("gui.name").replace("&", "§").replace("[PLAYER]", targetName);
        int size = config.getInt("gui.size");
        Inventory gui = Bukkit.createInventory((InventoryHolder) null, size, rawTitle);

        ConfigurationSection slots = config.getConfigurationSection("gui.slots");
        if (slots != null) {
            for (String key : slots.getKeys(false)) {
                int slot = Integer.parseInt(key);
                ConfigurationSection itemSec = slots.getConfigurationSection(key);
                if (itemSec != null) {
                    Material material = Material.matchMaterial(itemSec.getString("item"));
                    if (material != null) {
                        ItemStack item = new ItemStack(material);
                        ItemMeta meta = item.getItemMeta();
                        if (itemSec.contains("name")) {
                            meta.setDisplayName(itemSec.getString("name").replace("&", "§"));
                        }
                        if (itemSec.contains("lore")) {
                            List<String> lore = itemSec.getStringList("lore").stream().map(line ->
                                    line.replace("&", "§")).toList();
                            meta.setLore(lore);
                        }
                        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                        item.setItemMeta(meta);
                        gui.setItem(slot, item);
                    }
                }
            }
        }

        player.openInventory(gui);
    }

    public static void handleClick(InventoryClickEvent e) {
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        String configTitle = Main.getInstance().getConfig().getString("gui.name").replace("&", "§");

        String guiTitle = event.getView().getTitle();
        if (guiTitle == null || !configTitle.contains("[PLAYER]")) return;

        String[] parts = configTitle.split("\\[PLAYER\\]", 2);
        String prefix = parts.length > 0 ? parts[0] : "";
        String suffix = parts.length > 1 ? parts[1] : "";

        if (!guiTitle.startsWith(prefix) || !guiTitle.endsWith(suffix)) return;

        event.setCancelled(true);

        String target = guiTitle.substring(prefix.length(), guiTitle.length() - suffix.length());


        Player targetPlayer = Bukkit.getPlayerExact(target);
        if (targetPlayer != null) {
            String blockPermission = Main.getInstance().getConfig().getString("block-report-permission", "zgloszenia.bypass");
            if (targetPlayer.hasPermission(blockPermission)) {
                player.sendMessage(Main.getInstance().getConfig().getString("cannot-report").replace("&", "§"));
                player.closeInventory();
                return;
            }
        }

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) return;

        if (!clicked.hasItemMeta() || !clicked.getItemMeta().hasDisplayName()) return;

        String displayName = clicked.getItemMeta().getDisplayName();
        if (!(displayName.contains("&") || displayName.contains("§"))) return;



        String reason = displayName;
        ReportManager.addReport(player.getName(), target, reason);

        String confirmMsg = Main.getInstance().getConfig().getString("player-reported")
                .replace("&", "§")
                .replace("[PLAYER]", target);
        player.sendMessage(confirmMsg);

        String reportTitle = Main.getInstance().getConfig().getString("report-title")
                .replace("&", "§").replace("[PLAYER]", target);
        player.sendTitle(reportTitle, "", 10, 60, 10);
        player.closeInventory();

        String adminMsg = Main.getInstance().getConfig().getString("admin-report-msg")
                .replace("&", "§").replace("[PLAYER]", target).replace("[REASON]", reason);

        for (Player online : Bukkit.getOnlinePlayers()) {
            String permission = Main.getInstance().getConfig().getString("permission");
            if (online.hasPermission(permission)) {
                online.sendMessage(adminMsg);
            }
        }
        DiscordWebhookSender.sendReport(player.getName(), target, reason);
    }
}

