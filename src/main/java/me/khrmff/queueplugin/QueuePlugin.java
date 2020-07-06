package me.khrmff.queueplugin;

import com.google.common.io.ByteStreams;
import me.khrmff.queueplugin.queue.QueueListener;
import me.khrmff.queueplugin.queue.QueueManager;
import me.khrmff.queueplugin.queue.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.util.concurrent.TimeUnit;

public final class QueuePlugin extends Plugin {
    private QueueManager manager;
    private static QueuePlugin instance;
    private Configuration config;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        instance = this;
        manager = new QueueManager();

        getProxy().getPluginManager().registerListener(this, new QueueListener());

        // WARNING AND IMPORTANT
        // sometimes when telling position it can trash your console because
        // someone joins at the same time (e.g. updates the list which is used to
        // tell your position) there would be an AccessError (or something like that,
        // I don't remember exact name)


        // each N seconds to each queue pleb the message about his position will be sent
        getProxy().getScheduler().schedule(QueuePlugin.getInstance(), () -> {
            for (ProxiedPlayer p : getProxy().getServerInfo(config.getString("queue-server-name")).getPlayers()) {
                p.sendMessage(new TextComponent(Utils.getQueueMessage(p)));

            }
        }, 0, config.getInt("seconds-per-queue-position-message"), TimeUnit.SECONDS);
    }

    public static QueuePlugin getInstance() {
        return instance;
    }

    public Configuration getConfig() {
        return config;
    }

    private void saveDefaultConfig() {
        File dataFolder = getDataFolder();

        // saving default config if it is not saved already
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }
        File configFile = new File(dataFolder, "config.yml");
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                try (
                        InputStream is = getResourceAsStream("config.yml");
                        OutputStream os = new FileOutputStream(configFile)
                ) {
                    if (is != null) {
                        ByteStreams.copy(is, os);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("Unable to create configuration file", e);
            }
        }

        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(dataFolder, "config.yml"));
        } catch (IOException e) {
            getLogger().severe("Couldn't load up the config file!");
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
