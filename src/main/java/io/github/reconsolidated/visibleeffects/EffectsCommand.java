package io.github.reconsolidated.visibleeffects;

import io.github.reconsolidated.visibleeffects.EffectsMenu.EffectsProfiles.EffectsEventMenu;
import io.github.reconsolidated.visibleeffects.EffectsMenu.EffectsProfiles.EffectsProfileMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EffectsCommand implements CommandExecutor {

    public EffectsCommand() {

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            new EffectsProfileMenu(player);
        }
        return true;
    }
}
