package main.java.me.avankziar.tt.spigot.database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.cmdtree.BaseConstructor;

public class SQLiteSetup
{
	private String database = "technologytree";
	
	public SQLiteSetup()
	{
		loadSQLitelSetup();
	}
	
	public boolean connectToDatabase() 
	{
		TT.log.info("Connecting to the SQLite database...");
		try
		{
			getConnection();
			TT.log.info("SQLite Database connection successful!");
			return true;
		} catch(Exception e) 
		{
			TT.log.log(Level.WARNING, "Could not connect to SQLite Database!", e);
			return false;
		}		
	}
	
	public Connection getConnection() throws SQLException
	{
		return reConnect();
	}
	
	private Connection reConnect() throws SQLException
	{
		File directory = new File(BaseConstructor.getPlugin().getDataFolder()+"/SQLite/");
		if(!directory.exists())
		{
			directory.mkdir();
		}
		File db = new File(directory.getPath(), database+".db");
		if(!db.exists())
		{
			try
			{
				db.createNewFile();
			} catch (IOException e)
			{
				TT.log.log(Level.WARNING, "Could not build db file!", e);
				e.printStackTrace();
			}
		}
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
        //Connect to database
        return DriverManager.getConnection("jdbc:sqlite:" + db);
	}
	
	private boolean baseSetup(String data) 
	{
		try (Connection conn = getConnection(); PreparedStatement query = conn.prepareStatement(data))
		{
			query.execute();
		} catch (SQLException e) 
		{
			TT.log.log(Level.WARNING, "Could not build SQLite data source. Or connection is null", e);
		}
		return true;
	}
	
	public boolean loadSQLitelSetup()
	{
		if(!connectToDatabase())
		{
			return false;
		}
		if(!setupDatabaseI())
		{
			return false;
		}
		return true;
	}
	
	private boolean setupDatabaseI()
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + SQLiteHandler.Type.PLACEDBLOCKS.getValue()
		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
		+ " world text,"
		+ " x int,"
		+ " y int,"
		+ " z int,"
		+ " expiration_date BIGINT);";
		baseSetup(data);
		return true;
	}
}
