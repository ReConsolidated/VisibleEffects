package io.github.reconsolidated.visibleeffects;

import dev.esophose.playerparticles.api.PlayerParticlesAPI;
import dev.esophose.playerparticles.particles.FixedParticleEffect;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.data.OrdinaryColor;
import dev.esophose.playerparticles.styles.ParticleStyle;
import io.github.reconsolidated.itemprovider.ItemProvider;
import io.github.reconsolidated.visibleeffects.BattlePass.BattlePassCommand;
import io.github.reconsolidated.visibleeffects.EffectsMenu.PlayerEffectsInventory;
import io.github.reconsolidated.visibleeffects.Particles.TempPPEffect;
import io.github.reconsolidated.visibleeffects.Placeholders.BedwarsRankPlaceholders;
import io.github.reconsolidated.visibleeffects.PostgreDB.DatabaseConnector;
import io.github.reconsolidated.visibleeffects.PostgreDB.DatabaseFunctions;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public final class VisibleEffects extends JavaPlugin implements Listener {


    // boolean enableEffect(Player player, String effect)
    // boolean disableEffect(Player player, String effect)

    // boolean canUseEffect(Player player, String effect)
    // void giveEffect(Player player, String effect, Long time) - done
    // void removeEffect(Player player, String effect)
    // int getItemCustomModelData(Player player, Material type)
    // void showBedwarsEffectsMenu(Player player)
    // List<String> getEffects(Player player) - done

    public static PlayerParticlesAPI ppAPI;


    @Getter
    private CustomModelDataProvider cmdProvider;

    @Getter
    private ItemProvider itemProvider;

    @Getter
    private static VisibleEffects instance;


    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        ppAPI = PlayerParticlesAPI.getInstance();

        Bukkit.getScheduler().runTaskLater(this, () -> {
            for (FixedParticleEffect effect : ppAPI.getFixedParticleEffects(Bukkit.getConsoleSender())) {
                ppAPI.removeFixedEffect(Bukkit.getConsoleSender(), effect.getId());
            }
        }, 40L);

        new BedwarsRankPlaceholders(this).register();
        DefaultConfig.loadDefaultConfig();
        DatabaseConnector.connect();
        getCommand("vp").setExecutor(new VPCommand(this));
        getCommand("karnet").setExecutor(new BattlePassCommand(this));
        getCommand("efekty").setExecutor(new EffectsCommand());
        getServer().getServicesManager().register(VisibleEffects.class, this, this, ServicePriority.Normal);
        getServer().getPluginManager().registerEvents(this, this);

        cmdProvider = new CustomModelDataProvider();

        itemProvider = getServer().getServicesManager().load(ItemProvider.class);
        if (itemProvider == null) {
            Bukkit.getPluginManager().disablePlugin(this);
            throw new RuntimeException("Couldn't load ItemProvider plugin. Make sure it's added as a dependency.");
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            DatabaseFunctions.loadPlayerEffects(event.getPlayer());
            DatabaseFunctions.loadProfile(event.getPlayer());
        });
    }


    /**
     * Makes database request.
     * Should be run asynchronously.
     * @param player
     * @param effect
     * @param time in milliseconds
     * @return
     */
    public boolean giveEffect(Player player, String effect, Long time) {
        return DatabaseFunctions.addPlayerEffect(player, effect, System.currentTimeMillis() + time);
    }

    @Nullable
    public List<Effect> getEffects(Player player) {
        return DatabaseFunctions.getPlayerEffects(player);
    }

    public void showBedwarsEffectsMenu(Player player) {
        List<Effect> effects = getEffects(player);
        if (effects == null) {
            player.sendMessage(ChatColor.RED + "Wystąpił błąd podczas otwierania menu." +
                    " Spróbuj ponownie za chwilę lub skontaktuj się z administracją.");
            return;
        }
        new PlayerEffectsInventory(this, player, effects, 1);
    }

    public void setModelData(Player player, ItemStack item) {
        int cmd = cmdProvider.getPlayerCMD(player, item.getType());
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(cmd);
        item.setItemMeta(meta);
    }

    public void playEffect(Player player, EFFECT_EVENT event, @Nullable Location location) {
        Bukkit.broadcastMessage("Playing event effect: " + event.name() + " for player: " + player.getName());

        EffectsProfile profile = getEffectsProfile(player);

        String effect = profile.getVisibleEffect(event);
        if (effect == null) return;

        String[] split = effect.split(";;");
        if (split.length < 2) {
            Bukkit.getLogger().warning("Niepoprawny efekt: " + effect);
            return;
        }


        Bukkit.broadcastMessage("" + Arrays.toString(split));

        String style = split[0];
        String particle = split[1];

        long time = 1000;

        if (split.length == 3) {
            String timeS = split[2];
            try {
                time = Long.parseLong(timeS);
            } catch (NumberFormatException ignored) {
            }
        }

        if (particle.contains(".")) {
            String[] particleArgs = particle.split("\\.");
            particle = particleArgs[0];
            int red = Integer.parseInt(particleArgs[1]);
            int green = Integer.parseInt(particleArgs[2]);
            int blue = Integer.parseInt(particleArgs[3]);

            OrdinaryColor color = new OrdinaryColor(red, green, blue);

            if (event.isFixed()) {
                TempPPEffect.createFixed(ParticleEffect.valueOf(particle), ParticleStyle.fromName(style), location, time, color);
            } else {
                TempPPEffect.createPlayer(ParticleEffect.valueOf(particle), ParticleStyle.fromName(style), player, time, color);
            }
        } else {
            if (event.isFixed()) {
                TempPPEffect.createFixed(ParticleEffect.valueOf(particle), ParticleStyle.fromName(style), location, time);
            } else {
                TempPPEffect.createPlayer(ParticleEffect.valueOf(particle), ParticleStyle.fromName(style), player, time);
            }
        }




    }

    public EffectsProfile getEffectsProfile(Player player) {
        return DatabaseFunctions.getEffectsProfile(player);
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public ItemStack getItemStack(String name) {
        return itemProvider.getItem("visible_effects", name);
    }

    public enum EFFECT_EVENT {
        KILL, ARROW, FINAL_KILL, VICTORY, BED_DESTROYED;

        public boolean isFixed() {
            return this != ARROW;
        }
    }

}


