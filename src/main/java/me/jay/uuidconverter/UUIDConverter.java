package me.jay.uuidconverter;

import me.jay.uuidconverter.dataManager.connecter;
import me.jay.uuidconverter.dataManager.queries;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public final class UUIDConverter extends JavaPlugin implements Listener {

    public connecter DB;
    public queries DBQ;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("[UUIDConverter] Plugin starting up.");
        this.DB = new connecter();
        this.DBQ = new queries(this);
        try {
            DB.connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (DB.isConnected()) {
            try {
                DBQ.createTable();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        getConfig().options().copyDefaults();
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        DB.disconnect();
    }

    @EventHandler
    public void join(PlayerJoinEvent e) throws SQLException {

        try {
            DBQ.createPlayer(e.getPlayer());
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        PreparedStatement ps1 = DB.getConnection().prepareStatement("SELECT * FROM " + getConfig().getString("Plugin.Database.MySQL.tableName")  + " WHERE username=?");
        ps1.setString(1, e.getPlayer().getName());
        ResultSet rs1 = ps1.executeQuery();
        if (rs1.next()){
            return;
        }else{
            PreparedStatement ps2 = DB.getConnection().prepareStatement("UPDATE " + getConfig().getString("Plugin.Database.MySQL.tableName") + " SET username=? WHERE " + getConfig().getString("Plugin.Database.MySQL.UUIDColumn") + "=?" + "=?");
            ps2.setString(1, e.getPlayer().getName());
            ps2.setString(2, e.getPlayer().getUniqueId().toString());
        }


    }
}
