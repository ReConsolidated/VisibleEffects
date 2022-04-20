package io.github.reconsolidated.visibleeffects;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultConfig {
    public static void loadDefaultConfig() {
        FileConfiguration config = VisibleEffects.getInstance().getConfig();

        Map<String, Object> defaults = new HashMap<>();
        defaults.put("current_pass", "default_pass");
        defaults.put("battlepass.default_pass", List.of("DIRT", "DIRT", "DIRT", "DIRT", "DIRT", "DIRT", "DIRT", "DIRT", "DIRT"));
        defaults.put("battlepass_premium.default_pass", List.of("STONE", "STONE", "STONE", "STONE", "STONE", "STONE", "STONE", "STONE", "STONE"));
        defaults.put("battlepass.definitions.example", "msg <p> To jest przykład :D");
        for (String key : defaults.keySet()) {
            if (!config.contains(key)) {
                config.set(key, defaults.get(key));
            }
        }
        VisibleEffects.getInstance().saveConfig();
    }
}
