package me.jay.uuidconverter.dataManager;

import me.jay.uuidconverter.UUIDConverter;
import org.bukkit.plugin.Plugin;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connecter {

    Plugin plugin = UUIDConverter.getPlugin(UUIDConverter.class);


    private Connection connection;

    public boolean isConnected(){
        return(this.connection != null);
    }

    public void connect() throws SQLException {
        String host = plugin.getConfig().getString("Plugin.Database.MySQL.Address");
        String port = plugin.getConfig().getString("Plugin.Database.MySQL.Port");
        String user = plugin.getConfig().getString("Plugin.Database.MySQL.Username");
        String pass = plugin.getConfig().getString("Plugin.Database.MySQL.Password");
        String database = plugin.getConfig().getString("Plugin.Database.MySQL.DBName");

        if (!isConnected()) {
            try{
                this.connection = DriverManager.getConnection("jdbc:mysql://" + URLEncoder.encode(user, StandardCharsets.UTF_8) + ":" + URLEncoder.encode(pass, StandardCharsets.UTF_8) + "@" + host + ":" + port + "/" + database + "?autoReconnect=true");
            } catch (SQLException e) {
                plugin.getLogger().severe("[UUIDConverter] Database unsuccessful. Disabling plugin.");
            }
        }
    }

    public void disconnect(){
        if (isConnected()){
            try{
                this.connection.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }

    public Connection getConnection(){
        return this.connection;
    }
}
