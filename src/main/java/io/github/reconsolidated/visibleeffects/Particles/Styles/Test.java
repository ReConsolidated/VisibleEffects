package io.github.reconsolidated.visibleeffects.Particles.Styles;

import dev.esophose.playerparticles.particles.FixedParticleEffect;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.styles.ParticleStyle;
import io.github.reconsolidated.visibleeffects.VisibleEffects;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static io.github.reconsolidated.visibleeffects.VisibleEffects.ppAPI;


public class Test {
    public static void run(Player player) {
        FixedParticleEffect effect = ppAPI.createFixedParticleEffect(
                Bukkit.getConsoleSender(),
                player.getLocation().clone().add(0, 5, 0),
                ParticleEffect.TOTEM_OF_UNDYING, ParticleStyle.fromName("halo"));

        Bukkit.getScheduler().runTaskLater(VisibleEffects.getInstance(), () -> {
            ppAPI.removeFixedEffect(Bukkit.getConsoleSender(), effect.getId());
        }, 100L);
    }
}
