package io.github.reconsolidated.visibleeffects.Particles.Styles;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public abstract class ParticleEffect {
    private static List<ParticleEffect> effects = new ArrayList<>();
    protected Location startLocation;
    protected World world;

    public static void tickParticles() {
        for (ParticleEffect effect : effects) {
            effect.onTick();
        }
    }


    public ParticleEffect(Location start) {
        effects.add(this);
        this.startLocation = start;
        this.world = start.getWorld();
    }



    protected abstract void onTick();

    protected void cancel() {
        effects.remove(this);
    }
}
