package me.Inted.chestshoplog.listener;

import java.sql.Timestamp;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import me.Inted.chestshoplog.chestshoplog;
import me.Inted.chestshoplog.mysql.MySQL;
import com.Acrobot.ChestShop.Events.TransactionEvent;
import com.Acrobot.ChestShop.Events.TransactionEvent.TransactionType;




public class chestshoptransactionlistener implements Listener{
	chestshoplog plugin;
	
	
	public chestshoptransactionlistener(chestshoplog plugin){
		this.plugin = plugin;
		
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void TransactionEvent(TransactionEvent event){
		
	ItemStack[] is = event.getStock();
	int amount = 0;
	for(ItemStack itemstack : is){
		 amount += itemstack.getAmount();
		}
	double price = event.getPrice();
	
	Timestamp tstamp = new Timestamp(System.currentTimeMillis());
	
	MySQL sql = this.plugin.getMySQL();
	
	String table = "adminshoplog";
	if(!event.getSign().getLine(0).equals("AdminShop")){
		table = "usershoplog";
	}
	else{
		if(event.getTransactionType() == TransactionType.BUY){
			return;
		}
	}
	sql.queryUpdate("INSERT INTO " + table + " (name, menge, preis, transactiontype, shopuser, user, time) VALUES ('" + event.getSign().getLine(3) +  "', '" + amount + "', '" + price + "', '" + event.getTransactionType().name().toLowerCase() + "', '" + event.getSign().getLine(0) + "', '" + event.getClient().getName() + "', '" + tstamp.getTime() + "')");
	}
}
