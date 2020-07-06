package me.khrmff.queueplugin.queue;

import me.khrmff.queueplugin.QueuePlugin;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class QueueManager {
    private static QueueManager instance;
    private List<ProxiedPlayer> players = new ArrayList<>();
    private final int maxMainServerPlayercount;

    public QueueManager() {
        instance = this;
        maxMainServerPlayercount = QueuePlugin.getInstance().getConfig().getInt("max-main-server-players");
    }

    private void updateQueue() {
        // if possible adds players to main server

        int playerCount = Utils.getPlayercount(QueuePlugin.getInstance().getConfig().getString("main-server-name"));
        System.out.println(playerCount);
        System.out.println(maxMainServerPlayercount);
        if (playerCount < maxMainServerPlayercount) {
            int playersToAdd = maxMainServerPlayercount - playerCount;

            while (playersToAdd != 0) {
                ProxiedPlayer transferredPlayer;
                try {
                    transferredPlayer = players.remove(0);
                } catch (IndexOutOfBoundsException ignored) { continue; }

                transferredPlayer.connect(QueuePlugin.getInstance().getProxy().getServerInfo(QueuePlugin.getInstance().getConfig().getString("main-server-name")));
                transferredPlayer.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', QueuePlugin.getInstance().getConfig().getString("player-join-message"))));
                playersToAdd -= 1;
            }
        }
    }

    public void addToQueue(ProxiedPlayer p) {
        players.add(p);
        updateQueue();
    }

    public List<ProxiedPlayer> getPlayers() {
        return players;
    }

    public Integer getQueuePosition(ProxiedPlayer p) {
        return players.indexOf(p) + 1;
    }

    public void removeFromQueue(ProxiedPlayer p) {
        players.remove(p);
        updateQueue();
    }

    public static QueueManager getInstance() {
        return instance;
    }
}
