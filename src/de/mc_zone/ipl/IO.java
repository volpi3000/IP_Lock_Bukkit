package de.mc_zone.ipl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class IO {
	
	
	
	static Logger log = Logger.getLogger("Minecraft");
	
	public static ArrayList reader() {

		ArrayList x = new ArrayList();
		try {

			FileReader fr = new FileReader("plugins/IPL/lock.yml");
			BufferedReader br = new BufferedReader(fr);
			String zeile;
			do {
				zeile = br.readLine();
			
				if (zeile != null && !zeile.startsWith("#")) {
					x.add(zeile);
				}
			} while (zeile != null);

			br.close();
			fr.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			File file = new File("plugins/IPL/lock.yml");

			FileWriter writer = new FileWriter(file, false);

			writer.write("");
			
			writer.flush();

			writer.close();

		}

		catch (IOException e) {

			e.printStackTrace();
		}

		return x;
	}

	public static String reader2() {

		String zeile = null;

		try {

			FileReader fr = new FileReader("plugins/IPL/settings.yml");
			BufferedReader br = new BufferedReader(fr);

			zeile = br.readLine();

			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return zeile;
	}
	
	
	
	public static ArrayList reader4() {

		ArrayList x = new ArrayList();
		try {

			FileReader fr = new FileReader("plugins/IPL/messages.yml");
			BufferedReader br = new BufferedReader(fr);
			String zeile;
			do {
				zeile = br.readLine();
				if (zeile != null) {
					x.add(zeile);
				}
			} while (zeile != null);

			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return x;
	}

	public static void checkConfig() {

		
		if (!new File("plugins/IPL").exists()) {
			new File("plugins/IPL").mkdir();
		}

		File file = new File("plugins/IPL/lock.yml");
		if (!file.exists()) {
			try {

				FileWriter writer = new FileWriter(file, true);

				writer.write("");
				
				writer.flush();

				writer.close();

			}

			catch (IOException e) {

				e.printStackTrace();
			}

		}
		

		File file5 = new File("plugins/IPL/messages.yml");
		if (!file5.exists()) {
			try {

				FileWriter writer = new FileWriter(file5, true);

				writer.write("Admin login: You are Admin");
				writer.write(System.getProperty("line.separator"));
				writer.write("User login:Valid!");
				writer.write(System.getProperty("line.separator"));
				writer.write("Failed User login:Your Ip does not Match(IP Updater started? and entered correct host)!");
				
				

				writer.flush();

				writer.close();

			}

			catch (IOException e) {

				e.printStackTrace();
			}

		}
		// settings

		File file2 = new File("plugins/IPL/settings.yml");
		if (!file2.exists()) {
			try {

				FileWriter writer = new FileWriter(file2, true);

				writer.write("Everybody needs IPLock:false");

				writer.flush();

				writer.close();

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		
		
	}
		
	
		
	}


