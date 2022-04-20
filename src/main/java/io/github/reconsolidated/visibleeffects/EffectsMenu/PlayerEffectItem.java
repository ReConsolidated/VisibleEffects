package io.github.reconsolidated.visibleeffects.EffectsMenu;

import io.github.reconsolidated.visibleeffects.CustomInventory.ClickOnlyItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerEffectItem extends ClickOnlyItem {
    public PlayerEffectItem(Player player, String effect, int row, int column) {
        super(getItemStack(player, effect), row, column, ( event ) -> {
            onPlayerClickEffect(player, effect);
        });
    }

    private static void onPlayerClickEffect(Player player, String effect) {
        player.sendMessage(ChatColor.GREEN + "effect clicked: " + effect);
    }

    public static ItemStack getItemStack(Player player, String effect) {

        return new ItemStack(Material.DIRT);
    }
}
