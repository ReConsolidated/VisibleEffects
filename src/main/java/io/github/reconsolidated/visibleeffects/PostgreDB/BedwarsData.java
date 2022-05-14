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

    public BedwarsData(VisibleEffects plugin) {
        if (instance != null) {
            throw new RuntimeException("Attempted to create 2nd instance of BedwarsData.");
        }
        instance = this;

        this.plugin = plugin;
        this.playersData = DatabaseFunctions.loadPlayersData("elo", 999999);
        this.dataByFinalKills = DatabaseFunctions.loadPlayersData("final_kills", 10);
        this.dataByStreak = DatabaseFunctions.loadPlayersData("streak", 10);
        if (playersData == null) {
            Bukkit.getLogger().warning("Couldn't load Bedwars data to VisualEffects " +
                    "plugin due to database problem. The feature will not work.");
        }

        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            List<PlayerData> newPlayerData = DatabaseFunctions.loadPlayersData("elo", 999999);
            List<PlayerData> newFinalKillData = DatabaseFunctions.loadPlayersData("final_kills", 10);
            List<PlayerData> newStreakData = DatabaseFunctions.loadPlayersData("streak", 10);
            Bukkit.getScheduler().runTask(plugin, () -> {
                playersData = newPlayerData;
                dataByFinalKills = newFinalKillData;
                dataByStreak = newStreakData;
            });
        }, 0L, 1000L);
    }

    public PlayerData getFinalKillsTop(int number) {
        if (number == 0) return null;
        if (number-1 >= dataByFinalKills.size()) return null;
        return dataByFinalKills.get(number);
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
