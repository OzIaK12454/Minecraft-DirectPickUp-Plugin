package org.OZI.directPickUp;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public final class DirectPickUp extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        ItemStack tool = player.getInventory().getItemInMainHand();

        // itemy do EQ
        event.setDropItems(false);

        for (ItemStack drop : block.getDrops(tool)) {
            var left = player.getInventory().addItem(drop);
            left.values().forEach(item ->
                    block.getWorld().dropItemNaturally(block.getLocation(), item)
            );
        }

        Material type = block.getType();

        // RUDY – XP vanilla, ale od razu do gracza
        if (type.name().endsWith("_ORE")) {
            int xp = event.getExpToDrop(); // ile XP normalnie by wypadło
            event.setExpToDrop(0);         // blok nie dropi orbów
            player.giveExp(xp);            // XP trafia prosto do gracza
            return;
        }

        // WSZYSTKO INNE – stałe 1 XP
        event.setExpToDrop(0);
        player.giveExp(1);
    }

}
