package me.khrmff.queueplugin.queue;

import me.khrmff.queueplugin.QueuePlugin;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.List;

public class QueueListener implements Listener {
    private List<String> players = new ArrayList<>();

    @EventHandler
    public void playerPreConnect(PreLoginEvent e) {
        players.add(e.getConnection().getName());
    }

    @EventHandler
    public void playerServerconnect(ServerConnectEvent e) {
        // if player just connected to proxy, redirect him to queue server and tell him his position
        if (!players.contains(e.getPlayer().getName())) {
            return;
        }
        players.remove(e.getPlayer().getName());

        if (Utils.getPlayercount(QueuePlugin.getInstance().getConfig().getString("queue-server-name")) == 0) {
            players.remove(e.getPlayer().getName());
        }

        e.setTarget(QueuePlugin.getInstance().getProxy().getServerInfo(QueuePlugin.getInstance().getConfig().getString("queue-server-name")));

        QueueManager.getInstance().addToQueue(e.getPlayer());
        e.getPlayer().sendMessage(new TextComponent(Utils.getQueueMessage(e.getPlayer())));
    }

    @EventHandler
    public void playerLeave(PlayerDisconnectEvent e) {
        // if it isn't the queue server then nothing bad happens btw
        QueueManager.getInstance().removeFromQueue(e.getPlayer());
    }

//    @EventHandler
//    public void playerLeave(ServerDisconnectEvent e) {
////        if (e.getTarget())
//    }

}
