package me.einjojo.survivalengine.listener;


import me.einjojo.survivalengine.SurvivalEngine;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class GlobalListener implements Listener {


    private final SurvivalEngine plugin;



    public GlobalListener(SurvivalEngine plugin) {
        this.plugin = plugin;
        boolean ignoreCancelled = false;
        RegisteredListener registeredListener = new RegisteredListener(this, new EventExecutor() {
            @Override
            public void execute(Listener listener, Event event) throws EventException {
                onEvent(event);
            }
        }, EventPriority.NORMAL, plugin, ignoreCancelled);
        for (HandlerList handler : HandlerList.getHandlerLists()) {
            handler.register(registeredListener);
        }
    }

    public void onEvent(Event event) {
        if(plugin.getEventBlacklist().contains(event.getEventName())) return;
        String s = String.format("Event called: \"%s\"", event.getEventName());
        Bukkit.broadcastMessage(s);
    };




}

