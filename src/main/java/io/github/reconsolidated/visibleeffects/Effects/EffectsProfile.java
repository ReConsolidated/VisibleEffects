package io.github.reconsolidated.visibleeffects.Effects;

import io.github.reconsolidated.visibleeffects.VisibleEffects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public class EffectsProfile {
    @Getter
    private UUID playerUUID;
    private Map<VisibleEffects.EFFECT_EVENT, Effect> effects;

    public Effect getEffect(VisibleEffects.EFFECT_EVENT event) {
        return effects.get(event);
    }

    public String getVisibleEffect(VisibleEffects.EFFECT_EVENT event) {
        Effect effect = getEffect(event);
        if (effect == null) return null;
        String[] split = effect.getName().split(":");
        if (split.length == 1) {
            Bukkit.getLogger().warning("Niepoprawny effekt: " + effect);
            return null;
        }
        return split[1];
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EffectsProfile ep) {
            return ep.playerUUID.equals(playerUUID);
         }
        return false;
    }

    public void setEffect(VisibleEffects.EFFECT_EVENT event, Effect effect) {
        effects.put(event, effect);
    }
}
