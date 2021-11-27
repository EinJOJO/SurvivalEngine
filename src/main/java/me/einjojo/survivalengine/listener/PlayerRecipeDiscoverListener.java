package me.einjojo.survivalengine.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRecipeDiscoverEvent;

public class PlayerRecipeDiscoverListener  implements Listener {

    public void onDiscover(PlayerRecipeDiscoverEvent e) {
        Player player = e.getPlayer();

        player.getDiscoveredRecipes();

    }


}
