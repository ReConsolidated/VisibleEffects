package io.github.reconsolidated.visibleeffects.Effects.EffectsMenu.EffectsProfiles;

import io.github.reconsolidated.visibleeffects.CustomInventory.ClickOnlyItem;
import io.github.reconsolidated.visibleeffects.CustomInventory.InventoryMenu;
import io.github.reconsolidated.visibleeffects.Effects.Effect;
import io.github.reconsolidated.visibleeffects.Effects.EffectsImplementation;
import io.github.reconsolidated.visibleeffects.Effects.EffectsProfile;
import io.github.reconsolidated.visibleeffects.PostgreDB.DatabaseFunctions;
import io.github.reconsolidated.visibleeffects.VisibleEffects;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class EffectsEventMenu extends InventoryMenu {
    private final int itemsPerPage = 28;

    public EffectsEventMenu(Player player, VisibleEffects.EFFECT_EVENT event, int page) {
        super(VisibleEffects.getInstance(), player, "Efekty gracza " + player.getName(), 6);

        long current = -1;

        EffectsProfile profile = VisibleEffects.getInstance().getEffectsProfile(player);

        if (profile == null) {
            player.closeInventory();
            player.sendMessage(ChatColor.RED + "Podczas ładowania menu efektów wystąpił błąd.");
            throw new RuntimeException("Profile was null for player: " + player.getName());
        }

        if (profile.getEffect(event) != null) {
            current = profile.getEffect(event).getID();
        }

        List<Effect> effects = VisibleEffects.getInstance().getEffects(player);
        if (effects == null) {
            player.sendMessage(ChatColor.RED + "Nie udało się otworzyć menu. Zgłoś to administratorowi.");
            player.closeInventory();
            return;
        }


        fillBorder(Material.GRAY_STAINED_GLASS_PANE);

        addItem(new ClickOnlyItem(VisibleEffects.getInstance().getItemStack("menu_back"), 6, 1, (e) -> {
            new EffectsProfileMenu(player);
        }));


        List<Effect> eventEffects = new ArrayList<>();
        for (Effect effect : effects) {
            if (effect.getEvent().equals(event)) {
                eventEffects.add(effect);
            }
        }


        int startIndex = getPageStartIndex(page);
        int endIndex = startIndex + itemsPerPage;

        int row = 2;
        int column = 2;
        for (int i = startIndex; i<Math.min(endIndex, eventEffects.size()); i++) {

            Effect effect = eventEffects.get(i);

            ItemStack item = VisibleEffects.getInstance().getItemStack("" + effect.getID());
            if (effect.getID() == current) {
                item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
                item.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            addItem(new ClickOnlyItem(item, row, column, (e) -> {
                player.sendMessage("ID: " + effect.getID() + ", EF: " + effect.getName());
                profile.setEffect(event, effect);

                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                    DatabaseFunctions.setProfileActiveEffect(player, event, effect);
                    EffectsImplementation.onProfileLoad(player);
                });

                new EffectsEventMenu(player, event, page);
            }));

            column++;
            if (column == 9) {
                row++;
                column = 2;
            }
        }

    }

    private void fillBorder(Material type) {
        for (int i = 1; i<10; i++) {
            addItem(new ClickOnlyItem(new ItemStack(type), 1, i, (e) -> {}));
            addItem(new ClickOnlyItem(new ItemStack(type), 6, i, (e) -> {}));
        }

        for (int i = 1; i<6; i++) {
            addItem(new ClickOnlyItem(new ItemStack(type), i, 1, (e) -> {}));
            addItem(new ClickOnlyItem(new ItemStack(type), i, 9, (e) -> {}));
        }
    }

    private int getPageStartIndex(int page) {
        return (page-1) * itemsPerPage;
    }
}
