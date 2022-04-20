package io.github.reconsolidated.visibleeffects.BattlePass;

import io.github.reconsolidated.visibleeffects.VisibleEffects;
import org.bukkit.entity.Player;

public class BattlePass {

    public static void showBattlePass(Player player) {
        new BattlePassInventory(VisibleEffects.getInstance(), player, 1);
    }
}
