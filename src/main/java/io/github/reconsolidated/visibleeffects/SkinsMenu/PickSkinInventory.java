package io.github.reconsolidated.visibleeffects.SkinsMenu;

import io.github.reconsolidated.visibleeffects.CustomInventory.ClickOnlyItem;
import io.github.reconsolidated.visibleeffects.CustomInventory.InventoryItem;
import io.github.reconsolidated.visibleeffects.CustomInventory.InventoryMenu;
import io.github.reconsolidated.visibleeffects.PostgreDB.DatabaseFunctions;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class PickSkinInventory extends InventoryMenu {
    public PickSkinInventory(Plugin plugin, Player player, ItemStack item) {
        super(plugin, player, "Dostępne skiny: " + PlainTextComponentSerializer.plainText().serialize(item.getItemMeta().displayName()), 6);

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            load(item);
        });


    }

    private void load(ItemStack item) {
        int currentCMD = DatabaseFunctions.getCurrentCMD(player.getName(), item.getType().toString());
        List<Integer> cmds = DatabaseFunctions.getAvailableCMD(player.getName(), item.getType().toString());
        if (cmds == null) {
            player.sendMessage(ChatColor.RED + "Wystąpił błąd podczas ładowania skinów. Zgłoś to administratorowi.");
            player.closeInventory();
            return;
        }
        int row = 2;
        int column = 2;
        for (int cmd : cmds) {
            if (cmd == currentCMD) {
                addItem(new ClickOnlyItem(current(cmd(item.getType(), cmd)), row, column, (e) -> {
                }));
            } else {
                addItem(new ClickOnlyItem(pickable(cmd(item.getType(), cmd)), row, column, (e) -> {
                    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                        DatabaseFunctions.setPlayerCMD(player.getName(), item.getType().toString(), cmd);
                        player.sendMessage(Component.text("Ustawiono skin: ").append(item.getItemMeta().displayName()));
                        load(item);
                    });
                }));
            }
            column++;
            if (column == 9) {
                row++;
                column = 2;
            }
        }
    }

    private ItemStack current(ItemStack item) {
        item.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        ItemMeta meta = item.getItemMeta();
        meta.lore(List.of(
                Component.text("Aktualnie wybrany")
        ));
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack pickable(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        meta.lore(List.of(
                Component.text("Kliknij, by wybrać")
        ));
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack cmd(Material type, int cmd) {
        ItemStack item = new ItemStack(type);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(cmd);
        item.setItemMeta(meta);
        return item;
    }
}
