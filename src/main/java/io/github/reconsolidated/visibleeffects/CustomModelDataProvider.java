package io.github.reconsolidated.visibleeffects;

import io.github.reconsolidated.visibleeffects.PostgreDB.DatabaseFunctions;
import it.unimi.dsi.fastutil.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class CustomModelDataProvider implements Listener {
    private final Map<UUID, CMDPlayerData> playerData;
    private final Map<UUID, AvailableCMDData> availableCMDData;

    public CustomModelDataProvider() {
        VisibleEffects.getInstance().getServer().getPluginManager().registerEvents(this, VisibleEffects.getInstance());
        playerData = new HashMap<>();
        availableCMDData = new HashMap<>();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        playerData.put(event.getPlayer().getUniqueId(), getCMDPlayerData(event.getPlayer()));
        availableCMDData.put(event.getPlayer().getUniqueId(), DatabaseFunctions.getPlayerAvailableCMDData(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        availableCMDData.remove(event.getPlayer().getUniqueId());
        playerData.remove(event.getPlayer().getUniqueId());
    }

    public AvailableCMDData getPlayerAvailableCMDData(Player player) {
        return availableCMDData.get(player.getUniqueId());
    }

    public void load(List<Player> players) {
    }

    public int getPlayerCMD(Player player, Material material) {
        if (playerData == null) return 0;
        CMDPlayerData data = playerData.get(player.getUniqueId());
        if (data != null) {
            return data.data.getOrDefault(material, 0);
        }
        return 0;
    }

    public void setPlayerCMD(Player player, Material material, int cmd) {
        CMDPlayerData data = getPlayerData(player);
        data.data.put(material, cmd);
        DatabaseFunctions.setPlayerCMD(player.getName(), material.toString(), cmd);
    }

    public boolean addAvailableCMD(Player player, Material material, int cmd) {
        return DatabaseFunctions.addAvailableCMD(player.getName(), material.toString(), cmd);
    }

    private CMDPlayerData getCMDPlayerData(Player player) {
        Map<Material, Integer> data = DatabaseFunctions.getPlayerCMDData(player);
        return new CMDPlayerData(data);
    }

    public CMDPlayerData getPlayerData(Player player) {
        return playerData.get(player.getUniqueId());
    }

    public static class CMDPlayerData {
        private final Map<Material, Integer> data;

        public CMDPlayerData(Map<Material, Integer> data) {
            this.data = data;
        }
    }

    public static class AvailableCMDData {
        private final List<Pair<Material, Integer>> data;

        public List<Integer> get(Material type) {
            List<Integer> result = new ArrayList<>();
            for (Pair<Material, Integer> record : data) {
                if (record.first().equals(type)) {
                    result.add(record.second());
                }
            }
            return result;
        }

        public AvailableCMDData(List<Pair<Material, Integer>> data) {
            this.data = data;
        }
    }
}
