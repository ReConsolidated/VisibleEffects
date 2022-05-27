package io.github.reconsolidated.visibleeffects.EffectsMenu.EffectsProfiles;

import io.github.reconsolidated.visibleeffects.CustomInventory.ClickOnlyItem;
import io.github.reconsolidated.visibleeffects.CustomInventory.InventoryMenu;
import io.github.reconsolidated.visibleeffects.VisibleEffects;
import org.bukkit.entity.Player;

public class EffectsProfileMenu extends InventoryMenu {
    public EffectsProfileMenu(Player player) {
        super(VisibleEffects.getInstance(), player, "Profil efektów", 6);

        addItem(new ClickOnlyItem(VisibleEffects.getInstance().getItemStack("menu_kill"), 2, 3, (e) -> {
            new EffectsEventMenu(player, VisibleEffects.EFFECT_EVENT.KILL, 1);
        }));

        addItem(new ClickOnlyItem(VisibleEffects.getInstance().getItemStack("menu_final_kill"), 2, 5, (e) -> {
            new EffectsEventMenu(player, VisibleEffects.EFFECT_EVENT.FINAL_KILL, 1);
        }));

        addItem(new ClickOnlyItem(VisibleEffects.getInstance().getItemStack("menu_bed_destroyed"), 2, 7, (e) -> {
            new EffectsEventMenu(player, VisibleEffects.EFFECT_EVENT.BED_DESTROYED, 1);
        }));
    }
}
