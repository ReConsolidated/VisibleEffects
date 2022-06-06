package io.github.reconsolidated.visibleeffects.Effects;

import dev.esophose.playerparticles.particles.ParticlePair;
import io.github.reconsolidated.visibleeffects.VisibleEffects;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

public class EffectsImplementation implements Listener {

    private final VisibleEffects plugin;

    public EffectsImplementation(VisibleEffects plugin) {
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onKill(PlayerDeathEvent event) {
        if (event.getPlayer().getKiller() == null) return;

        Player killer = event.getPlayer().getKiller();
        plugin.playEffect(killer, VisibleEffects.EFFECT_EVENT.KILL, event.getPlayer().getLocation());
    }

    public static void onProfileLoad(Player player) {

        for (ParticlePair effect : VisibleEffects.ppAPI.getActivePlayerParticles(player)) {
            VisibleEffects.ppAPI.removeActivePlayerParticle(player, effect.getId());
        }


        VisibleEffects.getInstance().playEffect(player, VisibleEffects.EFFECT_EVENT.ARROW, player.getLocation());
        VisibleEffects.getInstance().playEffect(player, VisibleEffects.EFFECT_EVENT.SWORD, player.getLocation());
        VisibleEffects.getInstance().playEffect(player, VisibleEffects.EFFECT_EVENT.PICKAXE, player.getLocation());
    }

    @EventHandler
    public void onArrowHit(ProjectileHitEvent event) {
        if (event.getEntity().getShooter() != null && event.getEntity().getShooter() instanceof Player player) {
            plugin.playEffect(player, VisibleEffects.EFFECT_EVENT.ARROW_HIT, event.getEntity().getLocation());
        }
    }
}
