package io.github.reconsolidated.visibleeffects.Placeholders;

import io.github.reconsolidated.visibleeffects.PostgreDB.BedwarsData;
import io.github.reconsolidated.visibleeffects.PostgreDB.PlayerData;
import io.github.reconsolidated.visibleeffects.VisibleEffects;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.OfflinePlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.event.Listener;

public class BedwarsRankPlaceholders extends PlaceholderExpansion implements Listener {

    private final VisibleEffects plugin;

    public BedwarsRankPlaceholders(VisibleEffects plugin) {
        this.plugin = plugin;

        new BedwarsData(plugin);
    }

    @Override
    public String getAuthor() {
        return "ReConsolidated";
    }

    @Override
    public String getIdentifier() {
        return "ve";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if(params.equalsIgnoreCase("player_rank_number")){
            return "" + BedwarsData.getInstance().getData(player.getName()).getRank();
        }

        if(params.startsWith("elo_rank_")) {
            try {
                int number = Integer.parseInt(params.split("_")[2]);
                PlayerData data = BedwarsData.getInstance().getPlayerAt(number);
                return "" + (int) data.getElo();
            } catch (NumberFormatException e) {
                return "Incorrect number: " + params.split("_")[2];
            }
        }
        if(params.startsWith("name_rank_")) {
            try {
                int number = Integer.parseInt(params.split("_")[2]);
                PlayerData data = BedwarsData.getInstance().getPlayerAt(number);
                return "" + data.getPlayerName();
            } catch (NumberFormatException e) {
                return "Incorrect number: " + params.split("_")[2];
            }
        }
        if(params.startsWith("rankname_rank_")) {
            try {
                int number = Integer.parseInt(params.split("_")[2]);
                PlayerData data = BedwarsData.getInstance().getPlayerAt(number);
                return "" + data.getRankPrefix();
            } catch (NumberFormatException e) {
                return "Incorrect number: " + params.split("_")[2];
            }
        }


        if(params.startsWith("finalkills_finalkillsrank_")) {
            try {
                int number = Integer.parseInt(params.split("_")[2]);
                PlayerData data = BedwarsData.getInstance().getFinalKillsTop(number);
                return "" + (int) data.getFinalKills();
            } catch (NumberFormatException e) {
                return "Incorrect number: " + params.split("_")[2];
            }
        }
        if(params.startsWith("name_finalkillsrank_")) {
            try {
                int number = Integer.parseInt(params.split("_")[2]);
                PlayerData data = BedwarsData.getInstance().getFinalKillsTop(number);
                return "" + data.getPlayerName();
            } catch (NumberFormatException e) {
                return "Incorrect number: " + params.split("_")[2];
            }
        }

        if(params.startsWith("streak_streakrank_")) {
            try {
                int number = Integer.parseInt(params.split("_")[2]);
                PlayerData data = BedwarsData.getInstance().getMatchesWonTop(number);
                return "" + data.getStreak();
            } catch (NumberFormatException e) {
                return "Incorrect number: " + params.split("_")[2];
            }
        }
        if(params.startsWith("name_streakrank_")) {
            try {
                int number = Integer.parseInt(params.split("_")[2]);
                PlayerData data = BedwarsData.getInstance().getMatchesWonTop(number);
                return "" + data.getPlayerName();
            } catch (NumberFormatException e) {
                return "Incorrect number: " + params.split("_")[2];
            }
        }


        return null; // Placeholder is unknown by the Expansion
    }


}