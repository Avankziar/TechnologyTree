package main.java.me.avankziar.tt.spigot.database;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.logging.Level;

import main.java.me.avankziar.tt.spigot.TT;

public interface SQLiteHandable
{
	public boolean create(Connection conn, String tablename);
	
	public boolean update(Connection conn, String tablename, String whereColumn, Object... whereObject);
	
	public ArrayList<Object> get(Connection conn, String tablename, String orderby, String limit, String whereColumn, Object... whereObject);
	
	default void log(Level level, String log, Exception e)
	{
		TT.log.log(level, log, e);
	}
}
