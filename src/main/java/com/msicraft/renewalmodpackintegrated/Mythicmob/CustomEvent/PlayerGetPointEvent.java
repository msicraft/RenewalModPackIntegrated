package com.msicraft.renewalmodpackintegrated.Mythicmob.CustomEvent;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerGetPointEvent extends Event{

    private final static HandlerList handlers = new HandlerList();

    private Player player;
    private int point;

    public PlayerGetPointEvent(Player player,int point) {
        this.player = player;
        this.point = point;
    }

    public Player getPlayer() {
        return player;
    }

    public int getPoint() {
        return point;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
