package io.minimum.minecraft.shortify.bukkit;

import io.minimum.minecraft.shortify.common.ShortenerManager;
import io.minimum.minecraft.shortify.util.CommonConfiguration;
import io.minimum.minecraft.shortify.util.ShortifyUtility;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import java.io.IOException;
import java.util.logging.Level;

public final class Shortify extends JavaPlugin
{

    private Listener listener;
    private static ShortenerManager shortenerManager;
    private static CommonConfiguration configuration;

    @Override
    public void onEnable()
    {
        // Load config.yml with snakeyaml
        configuration = ShortifyUtility.loadCfg(getFile());
        shortenerManager = ShortifyUtility.setupShorteners();
        ShortifyUtility.reloadConfigShorteners(shortenerManager, configuration);
        ShortifyUtility.verifyConfiguration(configuration, getLogger());
        if (configuration.getBoolean("metrics"))
        {
            try
            {
                Metrics metrics = new Metrics(this);
                Metrics.Graph g = metrics.createGraph("URL Shortener");
                g.addPlotter(new Metrics.Plotter(configuration.getString("shortener", "isgd"))
                {
                    @Override
                    public int getValue()
                    {
                        return 1;
                    }
                });
                metrics.start();
                getLogger().info("Metrics setup.");
            }
            catch (IOException e)
            {
                getLogger().log(Level.WARNING, "Unable to set up Metrics", e);
            }
        }
        listener = new ShortifyListener(this);
        getServer().getPluginManager().registerEvents(listener, this);
        getServer().getPluginManager().registerEvents(new ShortifyCommandListener(this), this);
        ShortifyUtility.dumpData(getFile(), configuration);
    }

    @Override
    public void onDisable()
    {
        listener = null;
        configuration = null;
        shortenerManager = null;
    }

    public boolean onCommand(CommandSender sender, Command command,
                             String commandLabel, String[] args)
    {
        // Check for permissions
        if (!sender.hasPermission("shortify.admin"))
        {
            sender.sendMessage(ChatColor.RED
                    + "You do not have permission to administer Shortify.");
            return true;
        }
        // Handle the command
        // This is currently only /shortify reload
        if (args.length > 0)
        {
            if (args[0].equals("reload"))
            {
                configuration = ShortifyUtility.loadCfg(getFile());
                ShortifyUtility.verifyConfiguration(configuration, getLogger());
                ShortifyUtility.reloadConfigShorteners(shortenerManager, configuration);
                sender.sendMessage(ChatColor.GREEN
                        + "Shortify has been reloaded.");
            } else
            {
                sender.sendMessage("/shortify reload");
            }
        } else
        {
            sender.sendMessage("/shortify reload");
        }
        return true;
    }

    public static ShortenerManager getShortenerManager()
    {
        return shortenerManager;
    }

    public static CommonConfiguration getConfiguration()
    {
        return configuration;
    }
}
