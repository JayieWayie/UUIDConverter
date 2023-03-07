package me.jay.uuidconverter.dataManager;

import me.jay.uuidconverter.UUIDConverter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class queries {

    private final UUIDConverter plugin;
    public queries(UUIDConverter plugin){
        this.plugin = plugin;
    }

    public void createTable() throws SQLException {
        PreparedStatement column = plugin.DB.getConnection().prepareStatement("IF COL_LENGTH('" + plugin.getConfig().getString("Plugin.Database.MySQL.tableName")  + "', 'username') IS NULL BEGIN ALTER TABLE "+ plugin.getConfig().getString("Plugin.Database.MySQL.tableName") + " ADD username TEXT END");
        column.executeUpdate();
    }


    public void createPlayer(Player player) throws SQLException {
        UUID uuid = player.getUniqueId();
        if (!doesPlayerExist(player)){
            PreparedStatement ps1 = plugin.DB.getConnection().prepareStatement("SELECT * FROM " + plugin.getConfig().getString("Plugin.Database.MySQL.tableName")  + " WHERE " + plugin.getConfig().getString("Plugin.Database.MySQL.UUIDColumn") + "=?");
            ps1.setString(1, String.valueOf(uuid));
            ResultSet rs1 = ps1.executeQuery();
            Player playerName = null;
            if (rs1.next()){
                for (Player p : Bukkit.getOnlinePlayers()){
                    if(p.getUniqueId().equals(rs1.getString("uuid"))){
                        playerName = p;
                    }
                }
            }
            PreparedStatement createPlayer = plugin.DB.getConnection().prepareStatement("UPDATE " + plugin.getConfig().getString("Plugin.Database.MySQL.tableName") + " SET username=? WHERE " + plugin.getConfig().getString("Plugin.Database.MySQL.UUIDColumn") + "=?");
            createPlayer.setString(1 , playerName.getName());
            createPlayer.setString(2, playerName.getUniqueId().toString());
            createPlayer.executeUpdate();
        }
    }


    public boolean doesPlayerExist(Player player) throws SQLException{
        UUID uuid = player.getUniqueId();
        PreparedStatement ps1 = plugin.DB.getConnection().prepareStatement("SELECT * FROM " + plugin.getConfig().getString("Plugin.Database.MySQL.tableName")  + " WHERE " + plugin.getConfig().getString("Plugin.Database.MySQL.UUIDColumn") + "=?");
        ps1.setString(1, String.valueOf(uuid));
        ResultSet rs1 = ps1.executeQuery();
        if (rs1.next()){
            return true;
        }else{
            return false;
        }
    }

    private String Color(String s){
        s = ChatColor.translateAlternateColorCodes('&',s);
        return s;
    }

}
