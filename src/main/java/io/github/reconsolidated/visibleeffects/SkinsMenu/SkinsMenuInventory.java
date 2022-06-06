package io.github.reconsolidated.visibleeffects.SkinsMenu;

import io.github.reconsolidated.visibleeffects.CustomInventory.ClickOnlyItem;
import io.github.reconsolidated.visibleeffects.CustomInventory.InventoryMenu;
import io.github.reconsolidated.visibleeffects.VisibleEffects;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SkinsMenuInventory extends InventoryMenu {
    public SkinsMenuInventory(VisibleEffects plugin, Player player) {
        super(plugin, player, "Choose your Skins", 6);

        fillWith(Material.BROWN_STAINED_GLASS_PANE);

        ItemStack wSword = plugin.getItemProvider().getItem("skins_menu", "WOODEN_SWORD");
        addItem(new ClickOnlyItem(wSword, 2, 2, (e) -> {
            new PickSkinInventory(plugin, player, wSword);
        }));

        ItemStack sSword = plugin.getItemProvider().getItem("skins_menu", "STONE_SWORD");
        addItem(new ClickOnlyItem(sSword, 2, 3, (e) -> {
            new PickSkinInventory(plugin, player, sSword);
        }));

        ItemStack iSword = plugin.getItemProvider().getItem("skins_menu", "IRON_SWORD");
        addItem(new ClickOnlyItem(iSword, 2, 4, (e) -> {
            new PickSkinInventory(plugin, player, iSword);
        }));

        ItemStack dSword = plugin.getItemProvider().getItem("skins_menu", "DIAMOND_SWORD");
        addItem(new ClickOnlyItem(dSword, 2, 5, (e) -> {
            new PickSkinInventory(plugin, player, dSword);
        }));

        ItemStack bow = plugin.getItemProvider().getItem("skins_menu", "BOW");
        addItem(new ClickOnlyItem(bow, 2, 6, (e) -> {
            new PickSkinInventory(plugin, player, bow);
        }));

        ItemStack gapple = plugin.getItemProvider().getItem("skins_menu", "GOLDEN_APPLE");
        addItem(new ClickOnlyItem(gapple, 2, 7, (e) -> {
            new PickSkinInventory(plugin, player, gapple);
        }));

        ItemStack shears = plugin.getItemProvider().getItem("skins_menu", "SHEARS");
        addItem(new ClickOnlyItem(shears, 2, 8, (e) -> {
            new PickSkinInventory(plugin, player, shears);
        }));



        ItemStack wAxe = plugin.getItemProvider().getItem("skins_menu", "WOODEN_AXE");
        addItem(new ClickOnlyItem(wAxe, 3, 2, (e) -> {
            new PickSkinInventory(plugin, player, wAxe);
        }));

        ItemStack sAxe = plugin.getItemProvider().getItem("skins_menu", "STONE_AXE");
        addItem(new ClickOnlyItem(sAxe, 3, 3, (e) -> {
            new PickSkinInventory(plugin, player, sAxe);
        }));

        ItemStack iAxe = plugin.getItemProvider().getItem("skins_menu", "IRON_AXE");
        addItem(new ClickOnlyItem(iAxe, 3, 4, (e) -> {
            new PickSkinInventory(plugin, player, iAxe);
        }));

        ItemStack dAxe = plugin.getItemProvider().getItem("skins_menu", "DIAMOND_AXE");
        addItem(new ClickOnlyItem(dAxe, 3, 5, (e) -> {
            new PickSkinInventory(plugin, player, dAxe);
        }));

        ItemStack back = plugin.getItemProvider().getItem("visible_effects", "menu_back");
        addItem(new ClickOnlyItem(back, 6, 1, (e) -> {
            player.closeInventory();
        }));

    }
}
