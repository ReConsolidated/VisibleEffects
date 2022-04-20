package io.github.reconsolidated.visibleeffects.BattlePass;

import io.github.reconsolidated.visibleeffects.VisibleEffects;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BattlePassCommand implements CommandExecutor {
    private final VisibleEffects plugin;

    public BattlePassCommand(VisibleEffects plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        BattlePass.showBattlePass((Player) sender);
        return true;
    }
}
