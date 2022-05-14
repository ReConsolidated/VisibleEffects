package io.github.reconsolidated.visibleeffects;

import dev.esophose.playerparticles.api.PlayerParticlesAPI;
import dev.esophose.playerparticles.particles.FixedParticleEffect;
import io.github.reconsolidated.itemprovider.ItemProvider;
import io.github.reconsolidated.visibleeffects.BattlePass.BattlePassCommand;
import io.github.reconsolidated.visibleeffects.EffectsMenu.PlayerEffectsInventory;
import io.github.reconsolidated.visibleeffects.Placeholders.BedwarsRankPlaceholders;
import io.github.reconsolidated.visibleeffects.PostgreDB.DatabaseConnector;
import io.github.reconsolidated.visibleeffects.PostgreDB.DatabaseFunctions;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.util.List;

public final class VisibleEffects extends JavaPlugin {


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
        getServer().getServicesManager().register(VisibleEffects.class, this, this, ServicePriority.Normal);

        cmdProvider = new CustomModelDataProvider();

        itemProvider = getServer().getServicesManager().load(ItemProvider.class);
        if (itemProvider == null) {
            Bukkit.getPluginManager().disablePlugin(this);
            throw new RuntimeException("Couldn't load ItemProvider plugin. Make sure it's added as a dependency.");
        }
    }


    public boolean giveEffect(Player player, String effect, Long time) {
        String type = effect.split(":")[0];
        String name = effect.split(":")[1];
        if (type.equalsIgnoreCase("player_effect")) {
            return DatabaseFunctions.addPlayerEffect(player, effect, System.currentTimeMillis() + time);
        }
        else if (type.equalsIgnoreCase("custom_item")) {
            // TODO
            return false;
        }
        throw new RuntimeException("Nie ma takiego efektu: " + effect);
    }

    @Nullable
    public List<String> getEffects(Player player) {
        return DatabaseFunctions.getPlayerEffects(player);
    }

    public void showBedwarsEffectsMenu(Player player) {
        List<String> effects = getEffects(player);
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



    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public ItemStack getItemStack(String name) {
        return itemProvider.getItem("visible_effects", name);
    }
}
