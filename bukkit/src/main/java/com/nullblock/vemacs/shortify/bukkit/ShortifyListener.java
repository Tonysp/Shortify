package com.nullblock.vemacs.shortify.bukkit;

/**
 * Listener for Shortify
 * By vemacs, some parts contributed by minecrafter
 *
 */

import com.google.common.collect.ImmutableSet;
import com.nullblock.vemacs.shortify.common.Shortener;
import com.nullblock.vemacs.shortify.common.ShortifyException;
import com.nullblock.vemacs.shortify.util.ShortifyUtility;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Set;

public class ShortifyListener implements Listener {

    private Shortify plugin;

    public ShortifyListener(Shortify Shortify) {
        plugin = Shortify;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        if (event.getPlayer().hasPermission("shortify.shorten")) {
            final Shortener shortener = Shortify.getShortenerManager().getShortener(Shortify.getConfiguration().getString("shortener", "isgd"));
            try {
                event.setMessage(ShortifyUtility.shortenAll(
                        event.getMessage(),
                        Integer.valueOf(Shortify.getConfiguration().getString(
                                "minlength", "20")),
                        shortener)
                );
            } catch (NumberFormatException e1) {
                plugin.getServer().getConsoleSender()
                        .sendMessage(
                                ChatColor.RED
                                        + "Warning: Your config.yml is invalid: minlength is not a number or invalid.");
            } catch (ShortifyException e1) {
                plugin.getServer().getConsoleSender().sendMessage(
                        ChatColor.RED + "Warning: " + e1.getMessage());
            }
        }
    }
}
