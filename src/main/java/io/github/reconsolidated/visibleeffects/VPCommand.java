package io.github.reconsolidated.visibleeffects;

import io.github.reconsolidated.visibleeffects.PostgreDB.DatabaseFunctions;
import io.github.reconsolidated.visibleeffects.SkinsMenu.SkinsMenuInventory;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class VPCommand implements CommandExecutor {
    private final VisibleEffects plugin;

    public VPCommand(VisibleEffects plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            plugin.showBedwarsEffectsMenu((Player) sender);
        } else {
            if (args[0].equalsIgnoreCase("givepremium")) {
                if (args.length == 2) {
                    String name = args[1];
                    DatabaseFunctions.givePremium(name);
                    sender.sendMessage(ChatColor.GREEN + "Sukces");

                }
            }
            else if (args[0].equalsIgnoreCase("reset")) {
                if (args.length == 2) {
                    String name = args[1];
                    DatabaseFunctions.clearData(name);
                    sender.sendMessage(ChatColor.GREEN + "Sukces");

                }
            }
            else if (args[0].equalsIgnoreCase("addprogress")) {
                if (args.length == 3) {
                    String name = args[1];
                    try {
                        int progress = Integer.parseInt(args[2]);
                        DatabaseFunctions.increaseBattlePassProgress(name, progress);
                        sender.sendMessage(ChatColor.GREEN + "Sukces");

                    } catch (NumberFormatException e) {
                        sender.sendMessage(ChatColor.RED + "Podaj poprawną liczbę.");
                    }
                }
            }
            else if (args[0].equalsIgnoreCase("giveskin")) {
                if (args.length == 4) {
                    String name = args[1];
                    String material = args[2];
                    try {
                        int cmd = Integer.parseInt(args[3]);
                        DatabaseFunctions.addAvailableCMD(name, material, cmd);
                        sender.sendMessage(ChatColor.GREEN + "Sukces");
                    } catch (NumberFormatException e) {
                        sender.sendMessage(ChatColor.RED + "Podaj poprawną liczbę.");
                    }
                }
            }
            else if (args[0].equalsIgnoreCase("skins")) {
                new SkinsMenuInventory(plugin, (Player) sender);
            }
            else if (args[0].equalsIgnoreCase("uuid")) {
                sender.sendMessage("ID: " + ((Player)sender).getUniqueId());
            }
            else {
                onHelp(sender);
            }
        }

        return true;
    }

    private void onHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.AQUA + "VP Komendy: ");
        sender.sendMessage(ChatColor.AQUA + " - /vp");
        sender.sendMessage(ChatColor.AQUA + " - /vp givepremium <nick>");
        sender.sendMessage(ChatColor.AQUA + " - /vp reset <nick>");
        sender.sendMessage(ChatColor.AQUA + " - /vp addprogress <nick> <progress>");
        sender.sendMessage(ChatColor.AQUA + " - /vp giveskin <nick> <material> <cmd>");
    }
}
