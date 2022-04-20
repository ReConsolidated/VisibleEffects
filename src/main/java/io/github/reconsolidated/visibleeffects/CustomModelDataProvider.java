package io.github.reconsolidated.visibleeffects;

import io.github.reconsolidated.visibleeffects.PostgreDB.DatabaseFunctions;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CustomModelDataProvider {
    private Map<UUID, CMDPlayerData> playerData;

    public void load(List<Player> players) {
        playerData = new HashMap<>();
        for (Player player : players) {
            playerData.put(player.getUniqueId(), getCMDPlayerData(player));
        }
    }

    public int getPlayerCMD(Player player, Material material) {
        if (playerData == null) return 0;
        CMDPlayerData data = playerData.get(player.getUniqueId());
        if (data != null) {
            return data.data.getOrDefault(material, 0);
        }
        return 0;
    }

    public boolean setPlayerCMD(Player player, Material material, int cmd) {
        return DatabaseFunctions.setPlayerCMD(player.getName(), material.toString(), cmd);
    }

    public boolean addAvailableCMD(Player player, Material material, int cmd) {
        return DatabaseFunctions.addAvailableCMD(player.getName(), material.toString(), cmd);
    }

    private CMDPlayerData getCMDPlayerData(Player player) {
        Map<Material, Integer> data = DatabaseFunctions.getPlayerCMDData(player.getName());
        return new CMDPlayerData(data);
    }

    private static class CMDPlayerData {
        private Map<Material, Integer> data;

        public CMDPlayerData(Map<Material, Integer> data) {
            this.data = data;
        }
    }
}
