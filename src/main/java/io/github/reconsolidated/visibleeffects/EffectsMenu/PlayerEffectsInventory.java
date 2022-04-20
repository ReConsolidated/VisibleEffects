package io.github.reconsolidated.visibleeffects.EffectsMenu;

import io.github.reconsolidated.visibleeffects.CustomInventory.InventoryMenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PlayerEffectsInventory extends InventoryMenu {
    public PlayerEffectsInventory(Plugin plugin, Player player,@NotNull List<String> effects, int page) {
        super(plugin, player, "Twoje efekty", 6);
        fillWithEmptyItems();

        Bukkit.broadcastMessage("" + effects);
        int row = 1;
        int column = 1;
        for (String effect : effects) {
            addItem(new PlayerEffectItem(player, effect, row, column));
            column++;
            if (column == 10) {
                column = 1;
                row++;
                if (row == 7) {
                    break;
                }
            }
        }

    }


}
