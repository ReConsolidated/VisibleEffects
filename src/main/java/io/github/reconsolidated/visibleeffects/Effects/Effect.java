package io.github.reconsolidated.visibleeffects.Effects;

import io.github.reconsolidated.visibleeffects.VisibleEffects;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Effect {
    @Getter
    private long ID;
    @Getter
    private String name;
    @Getter
    private VisibleEffects.EFFECT_EVENT event;
}
