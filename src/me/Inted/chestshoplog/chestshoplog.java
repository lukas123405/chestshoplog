package me.Inted.chestshoplog;





import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
			if(!sender.hasPermission("chesthoplog.*") || !sender.hasPermission("chesthoplog.admin") || !sender.hasPermission("chesthoplog.password")){
				
				sender.sendMessage("Du hast keine Rechte diesen Command auszuführen!");
				return true;
			}
			if(args.length < 2)
			{
				if(sender.hasPermission("chesthoplog.*") || sender.hasPermission("chesthoplog.admin")){
					sender.sendMessage("/shop [password <password> | admin <True/False>] | admin <username> <True/False>]");
				}
				else
				{
					sender.sendMessage("/shop [password <password>]");
				}
				return true;
			}
			
			if(args[0].equalsIgnoreCase("password")){
				Connection conn = sql.getConnection();
				ResultSet rs = null;
				PreparedStatement st = null;
				int rowCount = -1;
				try {
					st = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE name LIKE '" + sender.getName() + "'");
					rs = st.executeQuery();
					rs.next();
					rowCount = rs.getInt(1);
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					sql.closeRessources(rs, st);
				}
				
				if(rowCount < 1){
					sql.queryUpdate("INSERT INTO users (name, password, admin, session) VALUES ('" + sender.getName() + "','" + sha1(md5(args[1]) + args[1]) + "','0','')");
					sender.sendMessage("Das Passwort wurde gesetzt.");
				}
				else{
				sql.queryUpdate("UPDATE users SET password = '" + sha1(md5(args[1]) + args[1]) + "' WHERE name LIKE '" + sender.getName() + "'");
				sender.sendMessage("Das Passwort wurde geändert.");
				}
					return true;
				
			}
			
			if(args[0].equalsIgnoreCase("admin")){
				String username = sender.getName();
				String admin = "0";
				if(args.length > 2){
					username = args[1];
					if(args[2].equalsIgnoreCase("true") ||args[2].equalsIgnoreCase("wahr") ||args[2].equalsIgnoreCase("1") ||args[2].equalsIgnoreCase("ja")){
						admin = "1";
					}
				}
				else{
				
				if(args[1].equalsIgnoreCase("true") ||args[1].equalsIgnoreCase("wahr") ||args[1].equalsIgnoreCase("1") ||args[1].equalsIgnoreCase("ja")){
					admin = "1";
				}
				}
				
				
				Connection conn = sql.getConnection();
				ResultSet rs = null;
				PreparedStatement st = null;
				int rowCount = -1;
				try {
					st = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE name LIKE '" + username + "'");
					rs = st.executeQuery();
					rs.next();
					rowCount = rs.getInt(1);
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					sql.closeRessources(rs, st);
				}
				
				if(rowCount < 1){
					sql.queryUpdate("INSERT INTO users (name, password, admin, session) VALUES ('" + username + "','','" + admin + "','')");
					sender.sendMessage("Benutzer wurde hinzugefügt und Admin wurde gesetzt.");
				}
				else{
				sql.queryUpdate("UPDATE users SET admin = '" + admin + "' WHERE name LIKE '" + username + "'");
				sender.sendMessage("Admin wurde gesetzt.");
				}
					
				return true;
			}
			
			
			if(sender.hasPermission("chesthoplog.*") || sender.hasPermission("chesthoplog.admin")){
				sender.sendMessage("/shop [password <password> | admin <True/False>] | admin <username> <True/False>]");
			}
			else
			{
				sender.sendMessage("/shop [password <password>]");
			}
			return true;
		}
		return false;
	}
	
	public MySQL getMySQL(){
		return sql;
	}

    static String sha1(String input){
        MessageDigest mDigest = null;
		try {
			mDigest = MessageDigest.getInstance("SHA1");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
         
        return sb.toString();
    }
    
    public static String md5(String value) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
	        md.update(value.getBytes("UTF-8"));
	        byte[] byteValues = md.digest();
	        byte singleChar = 0;
	        if (byteValues == null || byteValues.length <= 0) return null;
	 
	        String entries[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
	        StringBuffer out = new StringBuffer(byteValues.length * 2);
	 
	        for (int i = 0; i < byteValues.length; i++) {
	            singleChar = (byte) (byteValues[i] & 0xF0);
	            singleChar = (byte) (singleChar >>> 4);
	            singleChar = (byte) (singleChar & 0x0F);
	            out.append(entries[(int) singleChar]); 
	            singleChar = (byte) (byteValues[i] & 0x0F); 
	            out.append(entries[(int) singleChar]);
	        }
	        String rslt = new String(out);
	        return rslt;
		}
		catch (NoSuchAlgorithmException e) { e.printStackTrace(); return null; }
		catch (UnsupportedEncodingException e) { e.printStackTrace(); return null; }
    }
	

}