package io.github.reconsolidated.visibleeffects.BattlePass;

import io.github.reconsolidated.visibleeffects.CustomInventory.ClickOnlyItem;
import io.github.reconsolidated.visibleeffects.PostgreDB.DatabaseFunctions;
import io.github.reconsolidated.visibleeffects.CustomInventory.InventoryMenu;
import io.github.reconsolidated.visibleeffects.VisibleEffects;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class BattlePassInventory extends InventoryMenu {
    private static int ITEMS_ON_PAGE = 7;

    private boolean hasPremium = false;
    private String currentPass = "default_pass";

    private static boolean hasPremiumPass(Player player) {
        String currentPass = VisibleEffects.getInstance().getConfig().getString("current_pass", "default_pass");
        return DatabaseFunctions.getPlayerBattlePass(player, currentPass);
    }

    private static String getTitle(Player player) {
        boolean premium = hasPremiumPass(player);
        if (premium) return "Karnet Bojowy Premium";
        return "Zwykły Karnet Bojowy";
    }

    public BattlePassInventory(Plugin plugin, Player player, int page) {
        super(plugin, player, getTitle(player), 5);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            hasPremium = hasPremiumPass(player);
            load(player, page);
        });
    }

    private void load(Player player, int page) {

        currentPass = VisibleEffects.getInstance().getConfig().getString("current_pass", "default_pass");
        int progress = DatabaseFunctions.getBattlePassProgress(player);
        List<Integer> openedNormal = DatabaseFunctions.getBattlePassOpened(player);
        List<Integer> openedPremium = DatabaseFunctions.getBattlePassPremiumOpened(player);
        if (progress == -1 || openedNormal == null || openedPremium == null) {
            player.sendMessage(ChatColor.RED + "Wystąpił błąd podczas otwierania Karnetu Bojowego. Spróbuj ponownie za chwilę.");
            player.closeInventory();
            return;
        }

        fillWith(Material.GRAY_STAINED_GLASS_PANE);

        List<String> premiumItems = getPremiumItems(page);
        List<String> items = getItems(page);

        for (int i = 0; i<7; i++) {
            int number = (page-1) * 7 + i+1;
            boolean hasEnoughProgressToOpen = progress/1000 >= number;
            if (openedPremium.contains(number)) {
                addItem(new ClickOnlyItem(enchanted(VisibleEffects.getInstance().getItemStack(premiumItems.get(i))), 2, 2+i, (event) -> {
                }));
            } else {
                int finalI = i;
                addItem(new ClickOnlyItem(VisibleEffects.getInstance().getItemStack(premiumItems.get(i)), 2, 2+i, (event) -> {
                    if (!hasPremium) {
                        player.sendMessage(ChatColor.GOLD + "Ten przedmiot pochodzi z Karnetu Premium. " +
                                "Aby go zakupić, udaj się na stronę Grypciocraft.pl/karnety.");
                    }
                    else {
                        if (hasEnoughProgressToOpen) {
                            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                                DatabaseFunctions.setOpenedPremium(player, number);
                                String action = getAction(premiumItems.get(finalI));
                                Bukkit.getScheduler().runTask(plugin, () -> {
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), action.replaceAll("<p>", player.getName()));
                                });
                                load(player, page);
                            });
                        }
                    }

                }));
            }


            ItemStack pane;

            if (hasEnoughProgressToOpen) {
                pane = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
            } else {
                pane = new ItemStack(Material.RED_STAINED_GLASS_PANE);
            }
            addItem(new ClickOnlyItem(pane, 3, 2+i, (event) -> {

            }));

            if (openedNormal.contains(number)) {
                addItem(new ClickOnlyItem(enchanted(VisibleEffects.getInstance().getItemStack(items.get(i))), 4, 2+i, (event) -> {
                }));
            } else {
                addItem(new ClickOnlyItem(VisibleEffects.getInstance().getItemStack(items.get(i)), 4, 2+i, (event) -> {
                    if (hasEnoughProgressToOpen) {
                        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                            DatabaseFunctions.setOpened(player, number);
                            load(player, page);
                        });
                    }
                }));
            }

        }
    }

    private String getAction(String itemName) {
        FileConfiguration config = VisibleEffects.getInstance().getConfig();
        return config.getString("battlepass.definitions." + itemName, "msg <p> Nieustawiona akcja itemu: %s".formatted(itemName));
    }

    private ItemStack enchanted(ItemStack item) {
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        item.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return item;
    }

    private List<String> getPremiumItems(int page) {
        List<String> allItems;
        plugin.reloadConfig(); // todo remove this line on production

        allItems = plugin.getConfig().getStringList("battlepass." + currentPass);

        int startingIndex = (page-1) * ITEMS_ON_PAGE;
        int endingIndex = page * ITEMS_ON_PAGE - 1;
        if (startingIndex >= allItems.size()) {
            player.sendMessage(ChatColor.RED + "[You shouldn't see this] Not enough items to open this page.");
            return new ArrayList<>();
        } else {
            List<String> result = new ArrayList<>();
            for (int i = startingIndex; i<=Math.min(endingIndex, allItems.size()-1); i++) {
                result.add(allItems.get(i));
            }
            return result;
        }
    }

    private List<String> getItems(int page) {
        List<String> allItems;
        plugin.reloadConfig(); // todo remove this line on production

        allItems = plugin.getConfig().getStringList("battlepass_premium." + currentPass);

        int startingIndex = (page-1) * ITEMS_ON_PAGE;
        int endingIndex = page * ITEMS_ON_PAGE - 1;
        if (startingIndex >= allItems.size()) {
            player.sendMessage(ChatColor.RED + "[You shouldn't see this] Not enough items to open this page.");
            return new ArrayList<>();
        } else {
            List<String> result = new ArrayList<>();
            for (int i = startingIndex; i<=Math.min(endingIndex, allItems.size()-1); i++) {
                result.add(allItems.get(i));
            }
            return result;
        }
    }



}
