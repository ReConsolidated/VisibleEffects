package io.github.reconsolidated.visibleeffects.BattlePass;

import io.github.reconsolidated.visibleeffects.PostgreDB.DatabaseFunctions;
import io.github.reconsolidated.visibleeffects.VisibleEffects;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public class ItemReadyRemainder implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Bukkit.getScheduler().runTaskLaterAsynchronously(VisibleEffects.getInstance(), () -> {
            int progress = DatabaseFunctions.getBattlePassProgress(player);
            List<Integer> openedNormal = DatabaseFunctions.getBattlePassOpened(player);
            if (openedNormal != null) {
                int max = 0;
                for (Integer pot : openedNormal) {
                    if (pot != null && pot > max) {
                        max = pot;
                    }
                }
                if (max > progress / 1000) {
                    player.sendMessage("Masz do odebrania efekt z karnetu!");
                }
            }

        }, 100L);

    }

}
