package io.github.reconsolidated.visibleeffects;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public class EffectsProfile {
    @Getter
    private UUID playerUUID;
    private Map<VisibleEffects.EFFECT_EVENT, String> effects;

    public String getEffect(VisibleEffects.EFFECT_EVENT event) {
        return effects.get(event);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EffectsProfile) {
            EffectsProfile ep = (EffectsProfile) obj;
            return ep.playerUUID.equals(playerUUID);
         }
        return false;
    }
}
