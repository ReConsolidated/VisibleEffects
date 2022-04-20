package io.github.reconsolidated.visibleeffects.PostgreDB;

import io.github.reconsolidated.visibleeffects.VisibleEffects;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseFunctions {


    public static void setPlayerMeta(Player player, String metaKey, String metaValue) {
        if (DatabaseConnector.getSql() == null) {
            Bukkit.getLogger().warning("Database is not connected.");
            return;
        }

        try {
            Statement statement = DatabaseConnector.getSql().createStatement();

            String sql = "DELETE FROM playermetadata " +
                            "WHERE playername='" + player.getName() + "' AND metakey='" + metaKey + "';";
            statement.executeUpdate(sql);

            sql = "INSERT into playermetadata \n" +
                            "(playername, metakey, metavalue)\n" +
                            "values ('" + player.getName() + "', '" + metaKey + "'," +
                            " '" + metaValue + "');";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public static Long getEffectID(String effectName) {
        if (DatabaseConnector.getSql() == null) {
            Bukkit.getLogger().warning("Database is not connected.");
            return null;
        }

        try {
            Statement statement = DatabaseConnector.getSql().createStatement();

            String sql = "SELECT effect_id FROM visible_effects " +
                    "WHERE name='" + effectName + "';";
            statement.executeQuery(sql);
            ResultSet result = statement.getResultSet();
            if (result.next()) {
                return result.getLong("effect_id");
            }
            return null;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }


    /**
     *
     * @param player
     * @param effect
     * @param expireTimestamp
     * @return if successfully added effect
     */
    public static boolean addPlayerEffect(Player player, String effect, long expireTimestamp) {
        if (DatabaseConnector.getSql() == null) {
            Bukkit.getLogger().warning("Database is not connected.");
            return false;
        }

        Long effectID = getEffectID(effect);

        if (effectID == null) {
            return false;
        }

        try {
            Statement statement = DatabaseConnector.getSql().createStatement();

            String sql = "DELETE FROM player_effects " +
                    "WHERE playername='" + player.getName() + "' AND effect_id=" + effectID + ";";
            statement.executeUpdate(sql);

            sql = "INSERT into player_effects \n" +
                    "(playername, effect_id, expires_on)\n" +
                    "values ('" + player.getName() + "', " + effectID + "," +
                    " " + expireTimestamp + ");";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }

    public static List<String> getPlayerEffects(Player player) {
        if (DatabaseConnector.getSql() == null) {
            Bukkit.getLogger().warning("Database is not connected.");
            return null;
        }

        try {
            Statement statement = DatabaseConnector.getSql().createStatement();

            String sql = "SELECT name FROM " +
                    "visible_effects a INNER JOIN (SELECT effect_id " +
                    "FROM visible_effects_players WHERE " +
                    "player_name='" + player.getName() + "') b ON " +
                    " a.effect_id=b.effect_id;";
            statement.executeQuery(sql);
            ResultSet result = statement.getResultSet();
            List<String> r = new ArrayList<>();
            while (result.next()) {
                r.add(result.getString("name"));
            }
            return r;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public static Map<Material, Integer> getPlayerCMDData(String name) {
        if (DatabaseConnector.getSql() == null) {
            Bukkit.getLogger().warning("Database is not connected.");
            return null;
        }

        try {
            Statement statement = DatabaseConnector.getSql().createStatement();

            String sql = "SELECT * FROM " +
                    "cmd_skins WHERE " +
                    "player_name='" + name + "';";
            statement.executeQuery(sql);
            ResultSet result = statement.getResultSet();
            Map<Material, Integer> data = new HashMap<>();
            while (result.next()) {
                try {
                    data.put(Material.valueOf(result.getString("material")), result.getInt("cmd"));
                } catch (IllegalArgumentException exception) {
                    Bukkit.getLogger().warning("Invalid material value while parsing " + name + " cmd data: "
                            + result.getString("material"));
                }
            }
            return data;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public static boolean setPlayerCMD(String name, String material, int cmd) {
        if (DatabaseConnector.getSql() == null) {
            Bukkit.getLogger().warning("Database is not connected.");
            return false;
        }

        try {
            Statement statement = DatabaseConnector.getSql().createStatement();

            String sql = "DELETE FROM cmd_skins " +
                    "WHERE player_name='" + name + "' AND material='" + material + "';";
            statement.executeUpdate(sql);

            sql = "INSERT into cmd_skins \n" +
                    "(player_name, material, cmd)\n" +
                    "values ('" + name + "', '" + material + "'," +
                    " " + cmd + ");";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }

    public static int getCurrentCMD(String name, String material) {
        if (DatabaseConnector.getSql() == null) {
            Bukkit.getLogger().warning("Database is not connected.");
            return 0;
        }

        try {
            Statement statement = DatabaseConnector.getSql().createStatement();

            String sql = "SELECT cmd FROM " +
                    "cmd_skins WHERE " +
                    "player_name='%s' AND material='%s';".formatted(name, material);
            statement.executeQuery(sql);
            ResultSet result = statement.getResultSet();
            if (result.next()) {
                return result.getInt("cmd");
            }
            return 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public static boolean getPlayerBattlePass(Player player, String passName) {
        if (DatabaseConnector.getSql() == null) {
            Bukkit.getLogger().warning("Database is not connected.");
            return false;
        }

        try {
            Statement statement = DatabaseConnector.getSql().createStatement();

            String sql = "SELECT * FROM " +
                    "battlepass WHERE premium=true AND " +
                    "player_name='" + player.getName() + "' AND pass_name='" + passName + "';";
            statement.executeQuery(sql);
            ResultSet result = statement.getResultSet();
            return result.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public static Integer getBattlePassProgress(Player player) {
        if (DatabaseConnector.getSql() == null) {
            Bukkit.getLogger().warning("Database is not connected.");
            return -1;
        }

        try {
            Statement statement = DatabaseConnector.getSql().createStatement();

            String sql = "SELECT progress FROM " +
                    "battlepass_progress WHERE " +
                    "player_name='" + player.getName() + "';";
            statement.executeQuery(sql);
            ResultSet result = statement.getResultSet();
            if (result.next()) {
                return result.getInt("progress");
            }
            return 0;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return -1;
        }
    }

    public static void increaseBattlePassProgress(String playerName, int increase) {
        if (DatabaseConnector.getSql() == null) {
            Bukkit.getLogger().warning("Database is not connected.");
            return;
        }

        try {
            Statement statement = DatabaseConnector.getSql().createStatement();

            String sql = ("INSERT INTO battlepass_progress VALUES ('%s', 0) ON CONFLICT DO NOTHING; " +
                    "UPDATE battlepass_progress SET progress = progress + %d WHERE player_name='%s'").formatted(
                    playerName, increase, playerName);

            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static boolean setOpened(Player player, int number) {
        if (DatabaseConnector.getSql() == null) {
            Bukkit.getLogger().warning("Database is not connected.");
            return false;
        }

        try {
            Statement statement = DatabaseConnector.getSql().createStatement();

            String sql = "INSERT into battlepass_opened \n" +
                    "(player_name, opened)\n" +
                    "values ('" + player.getName() + "', " + number + ") ON CONFLICT DO NOTHING;";
            statement.executeUpdate(sql);

            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }

    public static boolean setOpenedPremium(Player player, int number) {
        if (DatabaseConnector.getSql() == null) {
            Bukkit.getLogger().warning("Database is not connected.");
            return false;
        }

        try {
            Statement statement = DatabaseConnector.getSql().createStatement();

            String sql = "INSERT into battlepass_opened_premium \n" +
                    "(player_name, opened)\n" +
                    "values ('" + player.getName() + "', " + number + ") ON CONFLICT DO NOTHING;";
            statement.executeUpdate(sql);

            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }

    public static List<Integer> getBattlePassOpened(Player player) {
        if (DatabaseConnector.getSql() == null) {
            Bukkit.getLogger().warning("Database is not connected.");
            return null;
        }

        try {
            Statement statement = DatabaseConnector.getSql().createStatement();

            String sql = "SELECT opened FROM " +
                    "battlepass_opened WHERE " +
                    "player_name='" + player.getName() + "';";
            statement.executeQuery(sql);
            ResultSet result = statement.getResultSet();
            List<Integer> list = new ArrayList<>();
            while (result.next()) {
                list.add(result.getInt("opened"));
            }
            return list;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public static List<Integer> getBattlePassPremiumOpened(Player player) {
        if (DatabaseConnector.getSql() == null) {
            Bukkit.getLogger().warning("Database is not connected.");
            return null;
        }

        try {
            Statement statement = DatabaseConnector.getSql().createStatement();

            String sql = "SELECT opened FROM " +
                    "battlepass_opened_premium WHERE " +
                    "player_name='" + player.getName() + "';";
            statement.executeQuery(sql);
            ResultSet result = statement.getResultSet();
            List<Integer> list = new ArrayList<>();
            while (result.next()) {
                list.add(result.getInt("opened"));
            }
            return list;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public static boolean addAvailableCMD(String name, String material, int cmd) {
        if (DatabaseConnector.getSql() == null) {
            Bukkit.getLogger().warning("Database is not connected.");
            return false;
        }

        try {
            Statement statement = DatabaseConnector.getSql().createStatement();

            String sql = "INSERT into available_skins \n" +
                    "(player_name, material, cmd)\n" +
                    "values ('" + name + "', '" + material + "', " + cmd + ") ON CONFLICT DO NOTHING;";
            statement.executeUpdate(sql);

            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }

    public static List<Integer> getAvailableCMD(String name, String material) {
        if (DatabaseConnector.getSql() == null) {
            Bukkit.getLogger().warning("Database is not connected.");
            return null;
        }

        try {
            Statement statement = DatabaseConnector.getSql().createStatement();

            String sql = "SELECT cmd FROM " +
                    "available_skins WHERE " +
                    "player_name='%s' AND material='%s';".formatted(name, material);
            statement.executeQuery(sql);
            ResultSet result = statement.getResultSet();
            List<Integer> list = new ArrayList<>();
            list.add(0);
            while (result.next()) {
                list.add(result.getInt("cmd"));
            }
            return list;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public static void givePremium(String name) {
        if (DatabaseConnector.getSql() == null) {
            Bukkit.getLogger().warning("Database is not connected.");
            return;
        }

        try {
            String passName = VisibleEffects.getInstance().getConfig().getString("current_pass", "default_pass");
            Statement statement = DatabaseConnector.getSql().createStatement();

            String sql = ("INSERT INTO battlepass (player_name, pass_name, number, premium) VALUES ('%s', '%s', 0, true) ON CONFLICT DO NOTHING; " +
                    "UPDATE battlepass SET premium = true WHERE player_name='%s' AND pass_name='%s'").formatted(name, passName, name, passName);

            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void clearData(String name) {
        if (DatabaseConnector.getSql() == null) {
            Bukkit.getLogger().warning("Database is not connected.");
            return;
        }

        try {
            Statement statement = DatabaseConnector.getSql().createStatement();
            String sql = """
                    DELETE FROM battlepass WHERE player_name='%s';
                    DELETE FROM battlepass_opened WHERE player_name='%s';
                    DELETE FROM battlepass_opened_premium WHERE player_name='%s';
                    DELETE FROM battlepass_progress WHERE player_name='%s';
                    """.formatted(name, name, name, name);

            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }



}
