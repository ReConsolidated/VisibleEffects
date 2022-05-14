package io.github.reconsolidated.visibleeffects.Particles.Styles;

import org.bukkit.Location;
import org.bukkit.Particle;

public class Firework extends ParticleEffect {

    private Location currentLocation = null;
    private double height = 0;
    private double splashHeight = 4;
    private double step = 0.2;

    private double l = 0;

    public Firework(Location start) {
        super(start);
    }

    @Override
    protected void onTick() {
        if (height < splashHeight) {
            height += step;
            world.spawnParticle(Particle.FLAME, startLocation.clone().add(0, height, 0), 1, 0, 0, 0, 0);
        } else {
            if (currentLocation == null) {
                currentLocation = startLocation.clone().add(0, height, 0);
            }

            l += step;
            world.spawnParticle(Particle.FLAME, currentLocation.clone().add(l, l, l), 1, 0.1, 0.1, 0.2, 0);
            world.spawnParticle(Particle.FLAME, currentLocation.clone().add(-l, l, l), 1, 0.1, 0.1, 0.2, 0);
            world.spawnParticle(Particle.FLAME, currentLocation.clone().add(-l, -l, l), 1, 0.1, 0.1, 0.1, 0);
            world.spawnParticle(Particle.FLAME, currentLocation.clone().add(-l, -l, -l), 1, 0.1, 0.1, 0.1, 0);
            world.spawnParticle(Particle.FLAME, currentLocation.clone().add(l, -l, l), 1, 0.1, 0.1, 0.1, 0);
            world.spawnParticle(Particle.FLAME, currentLocation.clone().add(l, -l, -l), 1, 0.1, 0.1, 0.1, 0);
            world.spawnParticle(Particle.FLAME, currentLocation.clone().add(l, l, -l), 1, 0.1, 0.1, 0.1, 0);
            world.spawnParticle(Particle.FLAME, currentLocation.clone().add(-l, l, -l), 1, 0.1, 0.1, 0.1, 0);

        }
    }
}
