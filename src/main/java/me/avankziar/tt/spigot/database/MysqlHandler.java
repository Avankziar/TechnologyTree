package main.java.me.avankziar.tt.spigot.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.objects.mysql.ExternBooster;
import main.java.me.avankziar.tt.spigot.objects.mysql.GlobalEntryQueryStatus;
import main.java.me.avankziar.tt.spigot.objects.mysql.GlobalTechnologyPoll;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupData;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupEntryQueryStatus;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupPlayerAffiliation;
import main.java.me.avankziar.tt.spigot.objects.mysql.PlayerData;
import main.java.me.avankziar.tt.spigot.objects.mysql.RegisteredBlock;
import main.java.me.avankziar.tt.spigot.objects.mysql.SoloEntryQueryStatus;
import main.java.me.avankziar.tt.spigot.objects.mysql.UpdateTech;

public class MysqlHandler
{
	public enum Type
	{
		PLAYERDATA("ttPlayerData", new PlayerData()),
		REGISTEREDBLOCK("ttRegisteredBlock", new RegisteredBlock()),
		
		SOLO_ENTRYQUERYSTATUS("ttSolo_EntryQueryStatus", new SoloEntryQueryStatus()),
		
		GROUP_DATA("ttGroup_Data", new GroupData()),
		GROUP_PLAYERAFFILIATION("ttGroup_PlayerAffiliation", new GroupPlayerAffiliation()),
		GROUP_ENTRYQUERYSTATUS("ttGroup_EntryQueryStatus", new GroupEntryQueryStatus()),
		
		GLOBAL_TECHNOLOGYPOLL("ttGlobal_TechnologyPoll", new GlobalTechnologyPoll()),
		GLOBAL_ENTRYQUERYSTATUS("ttGlobal_EntryQueryStatus", new GlobalEntryQueryStatus()),
		
		UPDATE_TECH("ttUpdate_Tech", new UpdateTech()),
		
		EXTERN_BOOSTER("ttExtern_Booster", new ExternBooster());
		
		private Type(String value, Object object)
		{
			this.value = value;
			this.object = object;
		}
		
		private final String value;
		private final Object object;

		public String getValue()
		{
			return value;
		}
		
		public Object getObject()
		{
			return object;
		}
	}
	
	public enum QueryType
	{
		INSERT, UPDATE, DELETE, READ;
	}
	
	/*
	 * Alle Mysql Reihen, welche durch den Betrieb aufkommen.
	 */
	public static long startRecordTime = System.currentTimeMillis();
	public static int inserts = 0;
	public static int updates = 0;
	public static int deletes = 0;
	public static int reads = 0;
	
	public static void addRows(QueryType type, int amount)
	{
		switch(type)
		{
		case DELETE:
			deletes += amount;
			break;
		case INSERT:
			inserts += amount;
		case READ:
			reads += amount;
			break;
		case UPDATE:
			updates += amount;
			break;
		}
	}
	
	public static void resetsRows()
	{
		inserts = 0;
		updates = 0;
		reads = 0;
		deletes = 0;
	}
	
	private TT plugin;
	
	public MysqlHandler(TT plugin) 
	{
		this.plugin = plugin;
	}
	
	private PreparedStatement getPreparedStatement(Connection conn, String sql, int count, Object... whereObject) throws SQLException
	{
		PreparedStatement ps = conn.prepareStatement(sql);
		int i = count;
        for(Object o : whereObject)
        {
        	ps.setObject(i, o);
        	i++;
        }
        return ps;
	}
	
	public boolean exist(Type type, String whereColumn, Object... whereObject)
	{
		//All Object which leaves the try-block, will be closed. So conn and ps is closed after the methode
		//No finally needed.
		//So much as possible in async methode use
		try (Connection conn = plugin.getMysqlSetup().getConnection();)
		{
			PreparedStatement ps = getPreparedStatement(conn,
					"SELECT `id` FROM `" + type.getValue()+ "` WHERE "+whereColumn+" LIMIT 1",
					1,
					whereObject);
	        ResultSet rs = ps.executeQuery();
	        MysqlHandler.addRows(QueryType.READ, rs.getMetaData().getColumnCount());
	        while (rs.next()) 
	        {
	        	return true;
	        }
	    } catch (SQLException e) 
		{
			  if(type.getObject() instanceof MysqlHandable)
			  {
				  MysqlHandable mh = (MysqlHandable) type.getObject();
				  mh.log(Level.WARNING, "Could not check "+type.getObject().getClass().getName()+" Object if it exist!", e);
			  }
		}
		return false;
	}
	
	public boolean create(Type type, Object object)
	{
		if(object instanceof MysqlHandable)
		{
			MysqlHandable mh = (MysqlHandable) object;
			try (Connection conn = plugin.getMysqlSetup().getConnection();)
			{
				mh.create(conn, type.getValue());
				return true;
			} catch (Exception e)
			{
				mh.log(Level.WARNING, "Could not create "+object.getClass().getName()+" Object!", e);
			}
		}
		return false;
	}
	
	public boolean updateData(Type type, Object object, String whereColumn, Object... whereObject)
	{
		if(object instanceof MysqlHandable)
		{
			MysqlHandable mh = (MysqlHandable) object;
			try (Connection conn = plugin.getMysqlSetup().getConnection();)
			{
				mh.update(conn, type.getValue(), whereColumn, whereObject);
				return true;
			} catch (Exception e)
			{
				mh.log(Level.WARNING, "Could not create "+object.getClass().getName()+" Object!", e);
			}
		}
		return false;
	}
	
	public Object getData(Type type, String whereColumn, Object... whereObject)
	{
		Object object = type.getObject();
		if(object instanceof MysqlHandable)
		{
			MysqlHandable mh = (MysqlHandable) object;
			try (Connection conn = plugin.getMysqlSetup().getConnection();)
			{
				ArrayList<Object> list = mh.get(conn, type.getValue(), "`id` ASC", " Limit 1", whereColumn, whereObject);
				if(!list.isEmpty())
				{
					return list.get(0);
				}
			} catch (Exception e)
			{
				mh.log(Level.WARNING, "Could not create "+object.getClass().getName()+" Object!", e);
			}
		}
		return null;
	}
	
	public int deleteData(Type type, String whereColumn, Object... whereObject)
	{
		try (Connection conn = plugin.getMysqlSetup().getConnection();)
		{
			PreparedStatement ps = getPreparedStatement(conn,
					"DELETE FROM `" + type.getValue() + "` WHERE "+whereColumn,
					1,
					whereObject);
	        int d = ps.executeUpdate();
			MysqlHandler.addRows(QueryType.DELETE, d);
			return d;
	    } catch (SQLException e) 
		{
	    	if(type.getObject() instanceof MysqlHandable)
			  {
				  MysqlHandable mh = (MysqlHandable) type.getObject();
				  mh.log(Level.WARNING, "Could not delete "+type.getObject().getClass().getName()+" Object!", e);
			  }
		}
		return 0;
	}
	
	public int truncate(Type type)
	{
		try (Connection conn = plugin.getMysqlSetup().getConnection();)
		{
			PreparedStatement ps = getPreparedStatement(conn,
					"TRUNCATE TABLE `" + type.getValue() + "`", 0, new Object[]{});
	        int d = ps.executeUpdate();
			MysqlHandler.addRows(QueryType.DELETE, d);
			return d;
	    } catch (SQLException e)
		{
	    	if(type.getObject() instanceof MysqlHandable)
			  {
				  MysqlHandable mh = (MysqlHandable) type.getObject();
				  mh.log(Level.WARNING, "Could not delete "+type.getObject().getClass().getName()+" Object!", e);
			  }
		}
		return 0;
	}
	
	public int lastID(Type type)
	{
		try (Connection conn = plugin.getMysqlSetup().getConnection();)
		{
			PreparedStatement ps = getPreparedStatement(conn,
					"SELECT `id` FROM `" + type.getValue() + "` ORDER BY `id` DESC LIMIT 1",
					1);
	        ResultSet rs = ps.executeQuery();
	        MysqlHandler.addRows(QueryType.READ, rs.getMetaData().getColumnCount());
	        while (rs.next()) 
	        {
	        	return rs.getInt("id");
	        }
	    } catch (SQLException e) 
		{
			  if(type.getObject() instanceof MysqlHandable)
			  {
				  MysqlHandable mh = (MysqlHandable) type.getObject();
				  mh.log(Level.WARNING, "Could not get last id from "+type.getObject().getClass().getName()+" Object table!", e);
			  }
		}
		return 0;
	}
	
	public int getCount(Type type, String whereColumn, Object... whereObject)
	{
		try (Connection conn = plugin.getMysqlSetup().getConnection();)
		{
			PreparedStatement ps = getPreparedStatement(conn,
					" SELECT count(*) FROM `" + type.getValue() + "` WHERE "+whereColumn,
					1,
					whereObject);
	        ResultSet rs = ps.executeQuery();
	        MysqlHandler.addRows(QueryType.READ, rs.getMetaData().getColumnCount());
	        while (rs.next()) 
	        {
	        	return rs.getInt(1);
	        }
	    } catch (SQLException e) 
		{
			  if(type.getObject() instanceof MysqlHandable)
			  {
				  MysqlHandable mh = (MysqlHandable) type.getObject();
				  mh.log(Level.WARNING, "Could not count "+type.getObject().getClass().getName()+" Object!", e);
			  }
		}
		return 0;
	}
	
	public double getSum(Type type, String whereColumn, Object... whereObject)
	{
		try (Connection conn = plugin.getMysqlSetup().getConnection();)
		{
			PreparedStatement ps = getPreparedStatement(conn,
					"SELECT sum("+whereColumn+") FROM `" + type.getValue() + "` WHERE 1",
					1,
					whereObject);
	        ResultSet rs = ps.executeQuery();
	        MysqlHandler.addRows(QueryType.READ, rs.getMetaData().getColumnCount());
	        while (rs.next()) 
	        {
	        	return rs.getInt(1);
	        }
	    } catch (SQLException e) 
		{
			  if(type.getObject() instanceof MysqlHandable)
			  {
				  MysqlHandable mh = (MysqlHandable) type.getObject();
				  mh.log(Level.WARNING, "Could not summarized "+type.getObject().getClass().getName()+" Object!", e);
			  }
		}
		return 0;
	}
	
	public ArrayList<Object> getList(Type type, String orderByColumn, int start, int quantity, String whereColumn, Object...whereObject)
	{
		Object object = type.getObject();
		if(object instanceof MysqlHandable)
		{
			MysqlHandable mh = (MysqlHandable) object;
			try (Connection conn = plugin.getMysqlSetup().getConnection();)
			{
				ArrayList<Object> list = mh.get(conn, type.getValue(), orderByColumn, " Limit "+start+", "+quantity, whereColumn, whereObject);
				if(!list.isEmpty())
				{
					return list;
				}
			} catch (Exception e)
			{
				mh.log(Level.WARNING, "Could not create "+object.getClass().getName()+" Object!", e);
			}
		}
		return new ArrayList<>();
	}
	
	public ArrayList<Object> getFullList(Type type, String orderByColumn,
			String whereColumn, Object...whereObject)
	{
		Object object = type.getObject();
		if(object instanceof MysqlHandable)
		{
			MysqlHandable mh = (MysqlHandable) object;
			try (Connection conn = plugin.getMysqlSetup().getConnection();)
			{
				ArrayList<Object> list = mh.get(conn, type.getValue(), orderByColumn, "", whereColumn, whereObject);
				if(!list.isEmpty())
				{
					return list;
				}
			} catch (Exception e)
			{
				mh.log(Level.WARNING, "Could not create "+object.getClass().getName()+" Object!", e);
			}
		}
		return new ArrayList<>();
	}
}