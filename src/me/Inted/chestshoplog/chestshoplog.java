package me.Inted.chestshoplog;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import me.Inted.chestshoplog.listener.chestshoptransactionlistener;
import me.Inted.chestshoplog.mysql.MySQL;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class chestshoplog extends JavaPlugin{


	MySQL sql;
	
	@Override
	public void onDisable() {
	}
	
	@Override
	public void onEnable() {
		
		this.sql = new MySQL();
		
		
		new chestshoptransactionlistener(this);
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("shop")){
			Connection conn = sql.getConnection();
			ResultSet rs = null;
			PreparedStatement st = null;
			
			try {
				st = conn.prepareStatement("SELECT * FROM usershoplog WHERE shopuser LIKE 'testuser'");
				rs = st.executeQuery();
				rs.next();
				while(rs.getRow() != 0){
				sender.sendMessage(rs.getString("name") + "   " + rs.getString("menge") + "   " + rs.getString("preis") + "   " + rs.getString("transactiontype") + "   " + rs.getString("user") + "   " + rs.getString("date") + "   " + rs.getString("time"));
				rs.next();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				sql.closeRessources(rs, st);
			}
			return true;
		}
		return false;
	}
	
	public MySQL getMySQL(){
		return sql;
	}

	

}