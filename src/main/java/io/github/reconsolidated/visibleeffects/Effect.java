package io.github.reconsolidated.visibleeffects;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Effect {
    @Getter
    private long ID;
    @Getter
    private String name;
}
