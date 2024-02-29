package main.java.me.avankziar.tt.spigot.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

import main.java.me.avankziar.tt.spigot.TT;

public class MysqlSetup 
{
	private String host;
	private int port;
	private String database;
	private String user;
	private String password;
	private boolean isAutoConnect;
	private boolean isVerifyServerCertificate;
	private boolean isSSLEnabled;
	
	public MysqlSetup(TT plugin, boolean adm, String path)
	{
		if(adm)
		{
			plugin.getLogger().log(Level.INFO, "Using IFH Administration");
		}
		host = adm ? plugin.getAdministration().getHost(path)
				: plugin.getYamlHandler().getConfig().getString("Mysql.Host");
		port = adm ? plugin.getAdministration().getPort(path)
				: plugin.getYamlHandler().getConfig().getInt("Mysql.Port", 3306);
		database = adm ? plugin.getAdministration().getDatabase(path)
				: plugin.getYamlHandler().getConfig().getString("Mysql.DatabaseName");
		user = adm ? plugin.getAdministration().getUsername(path)
				: plugin.getYamlHandler().getConfig().getString("Mysql.User");
		password = adm ? plugin.getAdministration().getPassword(path)
				: plugin.getYamlHandler().getConfig().getString("Mysql.Password");
		isAutoConnect = adm ? plugin.getAdministration().isAutoReconnect(path)
				: plugin.getYamlHandler().getConfig().getBoolean("Mysql.AutoReconnect", true);
		isVerifyServerCertificate = adm ? plugin.getAdministration().isVerifyServerCertificate(path)
				: plugin.getYamlHandler().getConfig().getBoolean("Mysql.VerifyServerCertificate", false);
		isSSLEnabled = adm ? plugin.getAdministration().useSSL(path)
				: plugin.getYamlHandler().getConfig().getBoolean("Mysql.SSLEnabled", false);
		loadMysqlSetup();
	}
	
	public boolean connectToDatabase() 
	{
		TT.log.info("Connecting to the database...");
		try
		{
			getConnection();
			TT.log.info("Database connection successful!");
			return true;
		} catch(Exception e) 
		{
			TT.log.log(Level.WARNING, "Could not connect to Database!", e);
			return false;
		}
	}
	
	public Connection getConnection() throws SQLException
	{
		return reConnect();
	}
	
	private Connection reConnect() throws SQLException
	{
		String url = "jdbc:mysql://";
		boolean bool = false;
	    try
	    {
	    	// Load new Drivers for papermc
	    	Class.forName("com.mysql.cj.jdbc.Driver");
	    	bool = true;
	    } catch (Exception e)
	    {
	    	bool = false;
	    } 
	    if (bool == false)
    	{
    		// Load old Drivers for spigot
    		try
    		{
    			Class.forName("com.mysql.jdbc.Driver");
    		}  catch (Exception e) {}
    	}
        Properties properties = new Properties();
        properties.setProperty("user", user);
        properties.setProperty("password", password);
        properties.setProperty("autoReconnect", String.valueOf(isAutoConnect));
        properties.setProperty("verifyServerCertificate", String.valueOf(isVerifyServerCertificate));
        properties.setProperty("useSSL", String.valueOf(isSSLEnabled));
        properties.setProperty("requireSSL", String.valueOf(isSSLEnabled));
        //Connect to database
        Connection conn = DriverManager.getConnection(url + host + ":" + port + "/" + database, properties);
        return conn;
	}
	
	private boolean baseSetup(String data) 
	{
		try (Connection conn = getConnection(); PreparedStatement query = conn.prepareStatement(data))
		{
			query.execute();
		} catch (SQLException e) 
		{
			TT.log.log(Level.WARNING, "Could not build data source. Or connection is null", e);
		}
		return true;
	}
	
	public boolean loadMysqlSetup()
	{
		if(!connectToDatabase())
		{
			return false;
		}
		if(!setupDatabaseI())
		{
			return false;
		}
		if(!setupDatabaseII())
		{
			return false;
		}
		if(!setupDatabaseIII())
		{
			return false;
		}
		if(!setupDatabaseIV())
		{
			return false;
		}
		if(!setupDatabaseV())
		{
			return false;
		}
		if(!setupDatabaseVI())
		{
			return false;
		}
		if(!setupDatabaseVII())
		{
			return false;
		}
		if(!setupDatabaseVIII())
		{
			return false;
		}
		if(!setupDatabaseIX())
		{
			return false;
		}
		return true;
	}
	
	private boolean setupDatabaseI()
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.PLAYERDATA.getValue()
		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
		+ " player_uuid char(36) NOT NULL UNIQUE,"
		+ " player_name varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,"
		+ " show_sync_msg boolean,"
		+ " show_reward_msg boolean,"
		+ " ttexp_actual double,"
		+ " ttexp_total_received double,"
		+ " vanilla_exp_still_to_be_obtained int,"
		+ " lastSettingLevel text);";
		baseSetup(data);
		return true;
	}
	
	private boolean setupDatabaseII() 
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.SOLO_ENTRYQUERYSTATUS.getValue()
		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
		+ " intern_name text NOT NULL,"
		+ " player_uuid char(36) NOT NULL,"
		+ " entry_query_type text NOT NULL,"
		+ " status_type text NOT NULL,"
		+ " research_level int,"
		+ " duration_until_expiration BIGINT);";
		baseSetup(data);
		return true;
	}
	
	private boolean setupDatabaseIII() 
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.REGISTEREDBLOCK.getValue()
		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
		+ " player_uuid char(36) NOT NULL,"
		+ " block_type text,"
		+ " server text NOT NULL,"
		+ " world text NOT NULL,"
		+ " block_x int,"
		+ " block_y int,"
		+ " block_z int);";
		baseSetup(data);
		return true;
	}
	
	private boolean setupDatabaseIV() 
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.GLOBAL_TECHNOLOGYPOLL.getValue()
		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
		+ " player_uuid char(36) NOT NULL,"
		+ " choosen_technology text not NULL,"
		+ " choosen_technology_researchlevel int,"
		+ " processed_in_poll boolean,"
		+ " processed_in_repayment boolean,"
		+ " global_choosen_technology text,"
		+ " global_choosen_technology_id int,"
		+ " total_participants int);"; //global_choosen_technology_id = GlobalEntryQueryStatus.id
		baseSetup(data);
		return true;
	}
	
	private boolean setupDatabaseV() 
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.GLOBAL_ENTRYQUERYSTATUS.getValue()
		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
		+ " intern_name text NOT NULL,"
		+ " entry_query_type text NOT NULL,"
		+ " status_type text NOT NULL,"
		+ " research_level int,"
		+ " duration_until_expiration BIGINT);";
		baseSetup(data);
		return true;
	}
	
	private boolean setupDatabaseVI() 
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.GROUP_DATA.getValue()
		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
		+ " group_name text NOT NULL,"
		+ " grandmaster_uuid char(36),"
		+ " creation_time BIGINT,"
		+ " display_descrition longtext," 	//Beschreibung der Gruppe
		+ " group_tech_exp double," 		//Konto für ttexp
		+ " group_level int," 				//Gruppenlvl, wird als Variabel in den Tech Formel gebrauch
		+ " max_amount_player int,"			//Wieviel Spieler maximal in der Gruppe sein dürfen, werden durch bezahlen von gebühren erhöht
		+ " max_amount_master int,"			//Wieviel Spieler in der Gruppe maximal Master-Rang sein dürfen etc.
		+ " max_amount_vice int,"
		+ " max_amount_councilmember int,"
		+ " max_amount_member int,"
		+ " group_counter_failed_upkeep int,"		//Die Anzahl Tage, wielange die Gruppe Ihren Unterhalt (berechnet durch Formel in der Config)
													//nicht gezahlt hat. Aka nach x Tage, wird das Gruppenlvl verringert.
		+ " default_group_tech_exp_daily_upkeep_master double," 			//Der Tägliche Standart Unterhalt, wieviel die Spieler
		+ " default_group_tech_exp_daily_upkeep_vice double,"
		+ " default_group_tech_exp_daily_upkeep_councilmember double,"
		+ " default_group_tech_exp_daily_upkeep_member double,"
		+ " default_group_tech_exp_daily_upkeep_adept double"
		+ ");";
		baseSetup(data);
		return true;
	}
	
	private boolean setupDatabaseVII() 
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.GROUP_ENTRYQUERYSTATUS.getValue()
		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
		+ " intern_name text NOT NULL,"
		+ " group_name text NOT NULL,"
		+ " entry_query_type text NOT NULL,"
		+ " status_type text NOT NULL,"
		+ " research_level int,"
		+ " duration_until_expiration BIGINT);";
		baseSetup(data);
		return true;
	}
	
	private boolean setupDatabaseVIII() 
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.GROUP_PLAYERAFFILIATION.getValue()
		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
		+ " group_name text NOT NULL,"
		+ " player_uuid char(36) NOT NULL,"
		+ " rank text NOT NULL,"
		+ " rank_ordinal int,"
		+ " individual_tech_exp_daily_upkeep double,"
		+ " can_research boolean,"
		+ " can_kick boolean,"
		+ " can_invite boolean,"
		+ " can_acceptapplication boolean,"
		+ " can_promote boolean,"
		+ " can_demote boolean,"
		+ " can_increaselevel boolean,"
		+ " can_setdefault_daily_upkeep boolean,"
		+ " can_setindividual_daily_upkeep boolean"
		+ ");";
		baseSetup(data);
		return true;
	}
	
	private boolean setupDatabaseIX()
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.UPDATE_TECH.getValue()
		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
		+ " player_uuid char(36) NOT NULL,"
		+ " player_associated_type text NOT NULL,"
		+ " technology text,"
		+ " global_technology_poll_id int,"
		+ " research_level int"
		+ ");";
		baseSetup(data);
		return true;
	}
}