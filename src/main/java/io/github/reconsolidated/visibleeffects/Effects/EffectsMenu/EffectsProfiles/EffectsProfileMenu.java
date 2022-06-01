package io.github.reconsolidated.visibleeffects.Effects.EffectsMenu.EffectsProfiles;

import io.github.reconsolidated.visibleeffects.CustomInventory.ClickOnlyItem;
import io.github.reconsolidated.visibleeffects.CustomInventory.InventoryMenu;
import io.github.reconsolidated.visibleeffects.VisibleEffects;
import org.bukkit.entity.Player;

public class EffectsProfileMenu extends InventoryMenu {
    public EffectsProfileMenu(Player player) {
        super(VisibleEffects.getInstance(), player, "Profil efektów", 5);

        addItem(new ClickOnlyItem(VisibleEffects.getInstance().getItemStack("menu_kill"), 2, 2, (e) -> {
            new EffectsEventMenu(player, VisibleEffects.EFFECT_EVENT.KILL, 1);
        }));

        addItem(new ClickOnlyItem(VisibleEffects.getInstance().getItemStack("menu_final_kill"), 2, 4, (e) -> {
            new EffectsEventMenu(player, VisibleEffects.EFFECT_EVENT.FINAL_KILL, 1);
        }));

        addItem(new ClickOnlyItem(VisibleEffects.getInstance().getItemStack("menu_bed_destroyed"), 2, 6, (e) -> {
            new EffectsEventMenu(player, VisibleEffects.EFFECT_EVENT.BED_DESTROYED, 1);
        }));

        addItem(new ClickOnlyItem(VisibleEffects.getInstance().getItemStack("menu_victory"), 2, 8, (e) -> {
            new EffectsEventMenu(player, VisibleEffects.EFFECT_EVENT.VICTORY, 1);
        }));

        addItem(new ClickOnlyItem(VisibleEffects.getInstance().getItemStack("menu_arrow"), 4, 2, (e) -> {
            new EffectsEventMenu(player, VisibleEffects.EFFECT_EVENT.ARROW, 1);
        }));

        addItem(new ClickOnlyItem(VisibleEffects.getInstance().getItemStack("menu_arrow_hit"), 4, 4, (e) -> {
            new EffectsEventMenu(player, VisibleEffects.EFFECT_EVENT.ARROW_HIT, 1);
        }));

        addItem(new ClickOnlyItem(VisibleEffects.getInstance().getItemStack("menu_sword"), 4, 6, (e) -> {
            new EffectsEventMenu(player, VisibleEffects.EFFECT_EVENT.SWORD, 1);
        }));

        addItem(new ClickOnlyItem(VisibleEffects.getInstance().getItemStack("menu_pickaxe"), 4, 8, (e) -> {
            new EffectsEventMenu(player, VisibleEffects.EFFECT_EVENT.PICKAXE, 1);
        }));
    }
}
