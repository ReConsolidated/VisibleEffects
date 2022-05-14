package io.github.reconsolidated.visibleeffects.PostgreDB;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class PlayerData {
    @Getter
    private final String playerName;
    @Getter
    private final String queueName;
    @Getter
    private final double elo;
    @Getter
    private final int gamesPlayed;
    @Getter
    private final int streak;
    @Getter
    private final int finalKills;
    @Getter
    private final int deaths;
    @Getter
    private final int kills;
    @Getter
    private final int bedsDestroyed;
    @Getter
    private final int sumOfPlaces;
    @Getter
    private final int rank;


    public PlayerData(String playerName, String queueName, double elo, int gamesPlayed,
                      int streak, int finalKills, int deaths, int kills, int bedsDestroyed, int sumOfPlaces, int rank) {
        this.playerName = playerName;
        this.queueName = queueName;
        this.elo = elo;
        this.gamesPlayed = gamesPlayed;
        this.streak = streak;
        this.finalKills = finalKills;
        this.deaths = deaths;
        this.kills = kills;
        this.bedsDestroyed = bedsDestroyed;
        this.sumOfPlaces = sumOfPlaces;
        this.rank = rank;
    }

    public String getRankPrefix() {
        TextColor prefixColor = TextColor.color(166,163,154);
        return "&" + prefixColor.asHexString() + "[" + getRankDisplayName() + "&" + prefixColor.asHexString() + "] ";
    }

    public String getRankDisplayName() {
        TextColor bronzeColor = TextColor.color(176, 141, 87);
        TextColor silverColor = TextColor.color(192, 192, 192);
        TextColor goldColor = TextColor.color(218, 165, 32);
        TextColor lapisColor = TextColor.color(70, 115, 219);
        TextColor diamondColor = TextColor.color(37, 194, 178);
        TextColor titanColor = TextColor.color(217, 95, 213);
        if (elo == 0 || gamesPlayed < 5) {
            return "Brak rangi";
        }
        if (elo < 600) {
            return "&" + bronzeColor.asHexString() + "&lBrąz I";
        }
        if (elo < 700) {
            return "&" + bronzeColor.asHexString() + "&lBrąz II";
        }
        if (elo < 800) {
            return "&" + bronzeColor.asHexString() + "&lBrąz III";
        }
        if (elo < 900) {
            return "&" + silverColor.asHexString() + "&lSrebro I";
        }
        if (elo < 1000) {
            return "&" + silverColor.asHexString() + "&lSrebro II";
        }
        // 1000-1100 : silver 3, starting rank
        if (elo < 1100) {
            return "&" + silverColor.asHexString() + "&lSrebro III";
        }
        if (elo < 1200) {
            return "&" + goldColor.asHexString() + "&lZłoto I";
        }
        if (elo < 1300) {
            return "&" + goldColor.asHexString() + "&lZłoto II";
        }
        if (elo < 1400) {
            return "&" + goldColor.asHexString() + "&lZłoto III";
        }
        if (elo < 1500) {
            return "&" + lapisColor.asHexString() + "&lLapis I";
        }
        if (elo < 1600) {
            return "&" + lapisColor.asHexString() + "&lLapis II";
        }
        if (elo < 1700) {
            return "&" + lapisColor.asHexString() + "&lLapis III";
        }
        if (elo < 1800) {
            return "&" + diamondColor.asHexString() + "&lDiament I";
        }
        if (elo < 1900) {
            return "&" + diamondColor.asHexString() + "&lDiament II";
        }
        if (elo < 2000) {
            return "&" + diamondColor.asHexString() + "&lDiament III";
        }
        return "&" + titanColor.asHexString() + "&lTytan";
    }
}
