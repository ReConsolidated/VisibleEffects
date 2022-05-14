package io.github.reconsolidated.visibleeffects.Particles.Styles;

import org.bukkit.Location;
import org.bukkit.Particle;

import java.util.List;
import java.util.Random;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Spiral extends ParticleEffect {
    double t = 0;
    double r = 1;
    private final List<Particle> particles;
    private Random rand;

    public Spiral(Location start, List<Particle> particles) {
        super(start);
        this.particles = particles;
        rand = new Random();
        if (particles.size() == 0) throw new RuntimeException("List of particles for effect Spiral can't be empty!");
    }

    @Override
    protected void onTick() {
        t = t + Math.PI / 8;
        double x = r * cos(t);
        double y = t/4;
        double z = r * sin(t);
        world.spawnParticle(particles.get(rand.nextInt(particles.size())), startLocation.clone().add(x, y, z), 1, 0, 0, 0, 0);
        if (t > Math.PI * 4) {
            cancel();
        }
    }
}
