package dev.aduxx.aDUXXZGLOSZENIA;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ReportListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        ReportGui.handleClick(e);
    }
}