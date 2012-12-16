package de.mc_zone.ipl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import de.mc_zone.ipl.IO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


public class IPL extends JavaPlugin implements Listener

{

	Logger log = Logger.getLogger("Minecraft");
	ArrayList<Object> list = new ArrayList<Object>();
	ArrayList<String> Locked = new ArrayList<String>();
	ArrayList<String> Messages2 = new ArrayList<String>();
	ArrayList<String> Admins = new ArrayList<String>();
	String Settings;
	boolean dev = false;
	
    Connection conn;
    Statement stat;
	

	public void onEnable() {
		
		
		getServer().getPluginManager().registerEvents(this, this);
		log.info("Config will be now checked");
		
		if(dev){log.info("SQLConnect");}
		//conect methode


		
				
	
			try {
				sqlConnection();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		
       
		IO.checkConfig();
		try {
		mergeold(); // Methode die die lock.yml in die datenbank schreibt
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.info("Error - Merging old Datafailed failed :-(");
			e.printStackTrace();
		}
		log.info("Config is checked");
		
		Settings = IO.reader2();
		Messages2 = IO.reader4();
		log.info("IP Lock is running and ready");

	}

	private void mergeold() throws Exception {
		
		//alte daten zur db hinzufügen
		PreparedStatement prep;
	
		prep = conn.prepareStatement("insert into lock(playername, host) values (?, ?);");
		
		 
	   
	 
	    
	  
		Locked = null;
		Locked = IO.reader();
		
		for(String item : Locked)
		{
			
		
		if(item != "")		
		{
			
				 	prep.setString(1, item.split(":")[0]);
				    prep.setString(2, item.split(":")[1]);
				    prep.addBatch();
				
		
			
		}
		}
		  	conn.setAutoCommit(false);
		    prep.executeBatch();
		    conn.setAutoCommit(true);
		
	}
	 
	private void sqlConnection() throws Exception{
		
		
		
		if(dev){log.info("SQLConnect");}
		//conect methode


			Class.forName("org.sqlite.JDBC");
			 
				// TODO Auto-generated catch block
			
			
			conn = DriverManager.getConnection("jdbc:sqlite:ipl.db");
			if(dev){log.info("Connected");}
		
		
		
			stat = conn.createStatement();
		
			// TODO Auto-generated catch block
		
			stat.executeUpdate("CREATE TABLE IF NOT EXISTS lock (id INT PRIMARY KEY, playername VARCHAR(50),  host VARCHAR(50));");
		
	}

	public void onDisable() {

		log.info("IP Lock is deactivated");
		try {
			conn.close(); //datenbank wird geschlossen
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@EventHandler
	public void normalJoin(PlayerJoinEvent event) {
		
		if(dev){log.info("Join-Event");}
		// Some code here
		
		Boolean ok = false;

		
		

		
		if (Settings.split(":")[1].trim().contains("false")) { //überprüft ob IPLock immer benötigt wird
		
			//code
			ResultSet rs = null;
			try {
				rs = stat.executeQuery("select count(*) from lock where playername='"+event.getPlayer().getName().toString().trim().toLowerCase()+"';");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				if(dev){log.info("Anzahl der Einträge zu diesem User"+rs.getString(1));}
				if(rs.getString(1).equals("0")) //prüfen ob der spieler berreits in der Datenabnk steht
				{
					if(dev){log.info("IPL: No records of player in database");}
					return;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//ResultSet wieder schließen
			try {
				if (rs != null)
				    rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try{
				//Spieler wird überprüft
				if(!checkUser(event))
				{
					
					event.getPlayer().kickPlayer(Messages2.get(2).split(":")[1]);
					log.info(event.getPlayer().getName()+" got kicked");
					
				}
				}
				catch(Exception e){
					log.info("error while checking :-(");
				}
			
			
		}
			

			// code

			
		 else { //IPLock wird immer benötigt
			 
			 try{
					//Spieler wird überprüft
					if(!checkUser(event))
					{
						
						event.getPlayer().kickPlayer(Messages2.get(2).split(":")[1]);
						log.info(event.getPlayer().getName()+" got kicked");
						
					}
					}
					catch(Exception e){
						log.info("error while checking :-(  2" ) ;
					}
			

			
		}
		

	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

    	Player player = null;
    	
    	
    	if (sender instanceof Player) {
    		player = (Player) sender;
    	}
    
     if (cmd.getName().equalsIgnoreCase("ipl")) {
    	 if(player != null){
    	 if(!player.hasPermission("ipl.basic") ) {
    		 sender.sendMessage(ChatColor.RED + "Sorry :-( You don't have the permission to use IPLock");
    		 return false;
    	 }
    	 }
    		   //Do something
    		
    			
    			
    			if (args.length < 1) {
    				sender.sendMessage(ChatColor.YELLOW+"/ipl remove [player] ");
    				sender.sendMessage(ChatColor.YELLOW+"/ipl add [player] [DNS / IP] ");
    				sender.sendMessage(ChatColor.YELLOW+"/ipl help ");
    	
    				return true;
  		          
  		        }
    			
    			//an reload denken
    			if(args[0].toLowerCase().contains("add"))
    			{
    				if (args.length < 3) {
     		           sender.sendMessage(ChatColor.RED + "Not enough arguments!");
     		           return false;
     		        }
    				
    				try {
    					String x = args[1];
    					String y = args[2];
    				
    					if(dev){log.info("hinzufügen prep statement");}
    					PreparedStatement prep = conn.prepareStatement("INSERT INTO lock(playername, host) VALUES(?, ?);");
    					 prep.setString(1, x);
    					    prep.setString(2, y);
    					    prep.addBatch();
    					conn.setAutoCommit(false);
    				    prep.executeBatch();
    				    conn.setAutoCommit(true);
						
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} //This is optional. You can do this later if you want.
    		
    				
    				
    				sender.sendMessage(ChatColor.GREEN + "Successful");
    				return true;
    				
    			}
    			
    			if(args[0].toLowerCase().contains("remove"))
    			{
    				if (args.length < 2) {
      		           sender.sendMessage(ChatColor.RED + "Not enough arguments!");
      		           return false;
      		        }
    				
    				try {
    					stat.executeUpdate("DELETE FROM lock WHERE playername = '"+args[1]+"';");
    					
    					
    					sender.sendMessage(ChatColor.GREEN + "Successful");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						sender.sendMessage(ChatColor.GREEN + "Error");
					}
    				
    				
    			
    				
    				return true;
    				
    			}
    			if(args[0].toLowerCase().contains("list"))
    			{
    				if (args.length < 2) {
      		           sender.sendMessage(ChatColor.RED + "Not enough arguments!");
      		           return false;
      		        }
    				
    				ResultSet rs = null;
    				try {
    					rs = stat.executeQuery("SELECT playername, host FROM lock WHERE LOWER(playername) like '"+ args[1].trim()+"'");
    				} catch (SQLException e2) {
    					// TODO Auto-generated catch block
    				log.info("query error :-( "+ e2.toString() );
    				}
    				if(dev){log.info("2");}
    				
    				
    				if(rs != null)
    				{
    				try {
    					while ( rs.next() )
    					{
    						if(dev){log.info("3");}
    						try {
    							
    							sender.sendMessage(rs.getString(1)+":"+rs.getString(2));
    						} catch (Exception e) {
    							// TODO Auto-generated catch block
    							e.printStackTrace();
    						}
    						if(dev){log.info("3 fertig");}
    					
    					}
    				} catch (SQLException e1) {
    					if(dev){log.info("list fehler");}
    					// TODO Auto-generated catch block
    					e1.printStackTrace();
    				}
    				}
    				try {
    					
    					if (rs != null)
    					    rs.close();
    				} catch (SQLException e1) {
    					// TODO Auto-generated catch block
    					e1.printStackTrace();
    				}	
    				
    				
    			
    				
    				return true;
    				
    			}
    			
    			if(args[0].toLowerCase().contains("help"))
    			{
    				
    				
    				sender.sendMessage(ChatColor.YELLOW+"/ipl remove [player] ");
    				sender.sendMessage(ChatColor.YELLOW+"/ipl add [player] [DNS / IP] ");
    				sender.sendMessage(ChatColor.YELLOW+"/ipl help ");
    				sender.sendMessage(ChatColor.YELLOW+"/ipl list [player] ");
    				return true;
    				
    			}
    				
    			
    			//Code
    			
    		}
    		
   
	
    	return false;
    }
	
	boolean checkUser(PlayerJoinEvent event)//Abgleich des Spielers mit der Liste
	{
		
		boolean fail = false; // zeigt an ob der user mindestens einmal in der liste enthalten ist
		InetAddress ip = null;
		String playerip = null;
		if(dev){log.info("1");}
		ResultSet rs = null;
		try {
			rs = stat.executeQuery("SELECT playername, host FROM lock WHERE LOWER(playername) like '"+event.getPlayer().getName().toString().trim().toLowerCase()+"';");
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
		log.info("query error :-(");
		}
		if(dev){log.info("2");}
		
		Locked.clear();
		if(rs != null)
		{
		try {
			while ( rs.next() )
			{
				if(dev){log.info("3");}
				try {
					if(dev){log.info(rs.getString(1)+":"+rs.getString(2));}
					Locked.add(rs.getString(1)+":"+rs.getString(2));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(dev){log.info("3 fertig");}
			
			}
		} catch (SQLException e1) {
			if(dev){log.info("add fehler");}
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		}
		try {
			
			if (rs != null)
			    rs.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	  
		
		if(dev){log.info("4");}
		for (String item : Locked) {

			String[] g = item.split(":");
			if(dev){log.info(event.getPlayer().getName()+":"+g[0]+"<");}
			if(dev){log.info("MATCH!");}
				
				// überprüfung IP
				
				

				try {

					ip = InetAddress.getByName(g[1]);
					if(dev){log.info("Resolved! " + ip);}

				} catch (Exception e) {

					// TODO Auto-generated catch block
					//event.getPlayer()
					//		.kickPlayer(
					//				"IP Lock: Your not allowed to Login (entered right domain address) ? ");
					
					fail = true;
					continue;

				}

				try {
					playerip = event.getPlayer().getAddress().getAddress().toString().split("/")[1];
					if(dev){log.info("Resolved 2"+ playerip);}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					log.info("Error resolving PlayerIP ");
				}
				if(dev){log.info("Vergleiche");}

				if (playerip.trim().equals(ip.toString().split("/")[1].trim())
						&& ip.toString().split("/")[1] != null) {
					
					event.getPlayer().sendMessage(ChatColor.GREEN + "IP Lock: "+Messages2.toArray()[1].toString().split(":")[1]);
					return true;

				}

				else {
					
					if(dev){log.info("Not the Same"+ playerip+" ! "+ip.toString().split("/")[1]);}
					
					fail = true;
					
				

				}
				
				

			
			
			

		}
		//ende der foreach
		return false;
	
		
			
			
		
		
		
		
		
	}
	
	

}
