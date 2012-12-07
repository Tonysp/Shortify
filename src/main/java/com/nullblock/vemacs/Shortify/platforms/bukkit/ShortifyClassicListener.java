package com.nullblock.vemacs.Shortify.platforms.bukkit;

/**
 * Listener for Shortify (classic mode)
 * Still a WIP
 * By vemacs, some parts contributed by minecrafter
 * 
 */

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.nullblock.vemacs.Shortify.GenericShortifyListener;
import com.nullblock.vemacs.Shortify.Shortener;
import com.nullblock.vemacs.Shortify.ShortifyException;

public class ShortifyClassicListener extends GenericShortifyListener implements Listener {

	private Shortify plugin;
	
	public ShortifyClassicListener(Shortify Shortify) {
		plugin = Shortify;
	}

	@EventHandler(priority = EventPriority.LOW)
	public void playerChat(AsyncPlayerChatEvent e) {
		String message = e.getMessage();
		Shortener shortener = BukkitShared.getShortener(plugin);
		// regex operations aren't that expensive, now www. to www9s99. URLs
		// should be shortened
		if (e.getPlayer().hasPermission("shortify.shorten")) {
			// REGEX from Daring Fireball
			Pattern p = Pattern
					.compile("(?i)\\b((?:https?://|www\\d{0,3}[.]|[a-z0-9.\\-]+[.][a-z]{2,4}/)(?:[^\\s()<>]+|\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\))+(?:\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\)|[^\\s`!()\\[\\]{};:'\".,<>?ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½]))");
			Matcher m = p.matcher(message);
			StringBuffer sb = new StringBuffer();
			String urlTmp = "";
			String minlength = plugin.getConfig().getString("minlength");
			int min = Integer.parseInt(minlength);
			String output = "The following URLs were shortened: ";
			while (m.find()) {
				try {
					urlTmp = m.group(1);
					if (urlTmp.length() > min) {
						try {
							output = output + ChatColor.RESET + ChatColor.AQUA + shortener
									.getShortenedUrl(java.net.URLEncoder
											.encode(urlTmp, "UTF-8")) + " ,";
							// might as well put the encoder in the listener to
							// prevent possible injections
						} catch (UnsupportedEncodingException e1) {
							// do absolutely nothing
						}
					}
				} catch (ShortifyException e1) {
					Bukkit.getConsoleSender().sendMessage(
							ChatColor.RED + "Warning: " + e1.getMessage());
				}
				m.appendReplacement(sb, "");
				sb.append(urlTmp);
			}
			m.appendTail(sb);
			e.setMessage(sb.substring(0, sb.length() - 3).toString());
		}
	}
}