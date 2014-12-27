package io.minimum.minecraft.shortify.bukkit;

/**
 * Listener for Shortify
 * By vemacs, some parts contributed by minecrafter
 *
 */

import io.minimum.minecraft.shortify.common.Shortener;
import io.minimum.minecraft.shortify.common.ShortifyException;
import io.minimum.minecraft.shortify.util.ShortifyUtility;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ShortifyListener implements Listener
{

    private final Shortify plugin;

    public ShortifyListener(Shortify Shortify)
    {
        plugin = Shortify;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event)
    {
        if (event.getPlayer().hasPermission("shortify.shorten"))
        {
            final Shortener shortener = Shortify.getShortenerManager().getShortener(Shortify.getConfiguration().getString("shortener", "isgd"));
            try
            {
                event.setMessage(ShortifyUtility.shortenAll(
                                event.getMessage(),
                                Integer.valueOf(Shortify.getConfiguration().getString(
                                        "minlength", "20")),
                                shortener)
                );
            }
            catch (NumberFormatException e1)
            {
                plugin.getServer().getConsoleSender()
                        .sendMessage(
                                ChatColor.RED
                                        + "Warning: Your config.yml is invalid: minlength is not a number or invalid.");
            }
            catch (ShortifyException e1)
            {
                plugin.getServer().getConsoleSender().sendMessage(
                        ChatColor.RED + "Warning: " + e1.getMessage());
            }
        }
    }
}
