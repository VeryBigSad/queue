package me.khrmff.queueplugin.queue;

import me.khrmff.queueplugin.QueuePlugin;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Utils {

    public static int getPlayercount(String serverName) {
        return getPlayercount(QueuePlugin.getInstance().getProxy().getServerInfo(serverName));
    }

    public static int getPlayercount(ServerInfo si) {
        return si.getPlayers().size();
    }

    public static String getQueueMessage(ProxiedPlayer p) {
        return ChatColor.translateAlternateColorCodes('&', QueuePlugin.getInstance().getConfig().getString("queue-position-message")
                .replace("<POSITION>", QueueManager.getInstance().getQueuePosition(p).toString())
                .replace("<QUEUESIZE>", ((Integer) QueueManager.getInstance().getPlayers().size()).toString()));
    }

}
