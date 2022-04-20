package io.github.reconsolidated.visibleeffects.SkinsMenu;

import io.github.reconsolidated.visibleeffects.CustomInventory.ClickOnlyItem;
import io.github.reconsolidated.visibleeffects.CustomInventory.InventoryMenu;
import io.github.reconsolidated.visibleeffects.VisibleEffects;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SkinsMenuInventory extends InventoryMenu {
    public SkinsMenuInventory(VisibleEffects plugin, Player player) {
        super(plugin, player, "Choose your Skins", 6);

        fillWith(Material.BROWN_STAINED_GLASS_PANE);

        ItemStack item1 = plugin.getItemProvider().getItem("skins_menu", "WOODEN_SWORD");
        ItemMeta meta = item1.getItemMeta();
        meta.displayName(Component.text("ELO Elo"));
        item1.setItemMeta(meta);
        addItem(new ClickOnlyItem(item1, 2, 2, (e) -> {
            new PickSkinInventory(plugin, player, item1);
        }));

        ItemStack item2 = plugin.getItemProvider().getItem("skins_menu", "STONE_SWORD");
        addItem(new ClickOnlyItem(item2, 2, 3, (e) -> {
            new PickSkinInventory(plugin, player, item2);
        }));
    }
}
