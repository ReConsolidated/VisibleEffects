package io.github.reconsolidated.visibleeffects.PostgreDB;

import io.github.reconsolidated.visibleeffects.VisibleEffects;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.List;

public class BedwarsData implements Listener {
    private static BedwarsData instance;

    public static BedwarsData getInstance() {
        return instance;
    }

    private final VisibleEffects plugin;
    private List<PlayerData> playersData;

    private List<PlayerData> dataByFinalKills;
    private List<PlayerData> dataByStreak;
    private List<PlayerData> dataByBedsDestroyed;

    public BedwarsData(VisibleEffects plugin) {
        if (instance != null) {
            throw new RuntimeException("Attempted to create 2nd instance of BedwarsData.");
        }
        instance = this;

        this.plugin = plugin;
        this.playersData = DatabaseFunctions.loadPlayersData("elo", 999999);
        this.dataByFinalKills = DatabaseFunctions.loadPlayersData("final_kills", 15);
        this.dataByStreak = DatabaseFunctions.loadPlayersData("streak", 15);
        this.dataByBedsDestroyed = DatabaseFunctions.loadPlayersData("beds_destroyed", 15);
        if (playersData == null) {
            Bukkit.getLogger().warning("Couldn't load Bedwars data to VisualEffects " +
                    "plugin due to database problem. The feature will not work.");
        }

        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            List<PlayerData> newPlayerData = DatabaseFunctions.loadPlayersData("elo", 999999);
            List<PlayerData> newFinalKillData = DatabaseFunctions.loadPlayersData("final_kills", 15);
            List<PlayerData> newStreakData = DatabaseFunctions.loadPlayersData("streak", 15);
            List<PlayerData> newBedsDestroyedData = DatabaseFunctions.loadPlayersData("beds_destroyed", 15);
            Bukkit.getScheduler().runTask(plugin, () -> {
                playersData = newPlayerData;
                dataByFinalKills = newFinalKillData;
                dataByStreak = newStreakData;
                dataByBedsDestroyed = newBedsDestroyedData;
            });
        }, 0L, 1000L);
    }

    public PlayerData getFinalKillsTop(int number) {
        if (number == 0) return null;
        if (number-1 >= dataByFinalKills.size()) return null;
        return dataByFinalKills.get(number);
    }

    public PlayerData getBedsDestroyedTop(int number) {
        if (number == 0) return null;
        if (number-1 >= dataByBedsDestroyed.size()) return null;
        return dataByBedsDestroyed.get(number);
    }

    public PlayerData getMatchesWonTop(int number) {
        if (number == 0) return null;
        if (number-1 >= dataByStreak.size()) return null;
        return dataByStreak.get(number);
    }

    public PlayerData getPlayerAt(int number) {
        if (number == 0) return null;
        if (number-1 >= playersData.size()) return null;
        return playersData.get(number-1);
    }

    public PlayerData getData(String name) {
        for (PlayerData data : playersData) {
            if (data.getPlayerName().equals(name)) {
                return data;
            }
        }
        return null;
    }
}
