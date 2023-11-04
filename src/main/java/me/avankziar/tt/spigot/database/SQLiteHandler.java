package main.java.me.avankziar.tt.spigot.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.objects.sqllite.PlacedBlock;

public class SQLiteHandler
{
	public enum Type
	{
		PLACEDBLOCKS("ttPlacedBlocks", new PlacedBlock());
		
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
	
	private TT plugin;
	
	public SQLiteHandler(TT plugin)
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
		try (Connection conn = plugin.getSQLLiteSetup().getConnection();)
		{
			PreparedStatement ps = getPreparedStatement(conn,
					"SELECT `id` FROM `" + type.getValue()+ "` WHERE "+whereColumn+" LIMIT 1",
					1,
					whereObject);
	        ResultSet rs = ps.executeQuery();
	        //SQLLiteHandler.addRows(QueryType.READ, rs.getMetaData().getColumnCount());
	        while (rs.next()) 
	        {
	        	return true;
	        }
	    } catch (SQLException e) 
		{
			  if(type.getObject() instanceof SQLiteHandable)
			  {
				  SQLiteHandable mh = (SQLiteHandable) type.getObject();
				  mh.log(Level.WARNING, "Could not check "+type.getObject().getClass().getName()+" Object if it exist!", e);
			  }
		}
		return false;
	}
	
	public boolean create(Type type, Object object)
	{
		if(object instanceof SQLiteHandable)
		{
			SQLiteHandable mh = (SQLiteHandable) object;
			try (Connection conn = plugin.getSQLLiteSetup().getConnection();)
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
		if(object instanceof SQLiteHandable)
		{
			SQLiteHandable mh = (SQLiteHandable) object;
			try (Connection conn = plugin.getSQLLiteSetup().getConnection();)
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
		if(object instanceof SQLiteHandable)
		{
			SQLiteHandable mh = (SQLiteHandable) object;
			try (Connection conn = plugin.getSQLLiteSetup().getConnection();)
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
		try (Connection conn = plugin.getSQLLiteSetup().getConnection();)
		{
			PreparedStatement ps = getPreparedStatement(conn,
					"DELETE FROM `" + type.getValue() + "` WHERE "+whereColumn,
					1,
					whereObject);
	        int d = ps.executeUpdate();
	        //SQLLiteHandler.addRows(QueryType.DELETE, d);
			return d;
	    } catch (SQLException e) 
		{
	    	if(type.getObject() instanceof SQLiteHandable)
			  {
				  SQLiteHandable mh = (SQLiteHandable) type.getObject();
				  mh.log(Level.WARNING, "Could not delete "+type.getObject().getClass().getName()+" Object!", e);
			  }
		}
		return 0;
	}
	
	public int truncate(Type type)
	{
		try (Connection conn = plugin.getSQLLiteSetup().getConnection();)
		{
			PreparedStatement ps = getPreparedStatement(conn,
					"TRUNCATE TABLE `" + type.getValue() + "`", 0, new Object[]{});
	        int d = ps.executeUpdate();
	        //SQLLiteHandler.addRows(QueryType.DELETE, d);
			return d;
	    } catch (SQLException e)
		{
	    	if(type.getObject() instanceof SQLiteHandable)
			  {
				  SQLiteHandable mh = (SQLiteHandable) type.getObject();
				  mh.log(Level.WARNING, "Could not delete "+type.getObject().getClass().getName()+" Object!", e);
			  }
		}
		return 0;
	}
	
	public int lastID(Type type)
	{
		try (Connection conn = plugin.getSQLLiteSetup().getConnection();)
		{
			PreparedStatement ps = getPreparedStatement(conn,
					"SELECT `id` FROM `" + type.getValue() + "` ORDER BY `id` DESC LIMIT 1",
					1);
	        ResultSet rs = ps.executeQuery();
	        //SQLLiteHandler.addRows(QueryType.READ, rs.getMetaData().getColumnCount());
	        while (rs.next()) 
	        {
	        	return rs.getInt("id");
	        }
	    } catch (SQLException e) 
		{
			  if(type.getObject() instanceof SQLiteHandable)
			  {
				  SQLiteHandable mh = (SQLiteHandable) type.getObject();
				  mh.log(Level.WARNING, "Could not get last id from "+type.getObject().getClass().getName()+" Object table!", e);
			  }
		}
		return 0;
	}
	
	public int getCount(Type type, String whereColumn, Object... whereObject)
	{
		try (Connection conn = plugin.getSQLLiteSetup().getConnection();)
		{
			PreparedStatement ps = getPreparedStatement(conn,
					" SELECT count(*) FROM `" + type.getValue() + "` WHERE "+whereColumn,
					1,
					whereObject);
	        ResultSet rs = ps.executeQuery();
	        //SQLLiteHandler.addRows(QueryType.READ, rs.getMetaData().getColumnCount());
	        while (rs.next()) 
	        {
	        	return rs.getInt(1);
	        }
	    } catch (SQLException e) 
		{
			  if(type.getObject() instanceof SQLiteHandable)
			  {
				  SQLiteHandable mh = (SQLiteHandable) type.getObject();
				  mh.log(Level.WARNING, "Could not count "+type.getObject().getClass().getName()+" Object!", e);
			  }
		}
		return 0;
	}
	
	public double getSum(Type type, String whereColumn, Object... whereObject)
	{
		try (Connection conn = plugin.getSQLLiteSetup().getConnection();)
		{
			PreparedStatement ps = getPreparedStatement(conn,
					"SELECT sum("+whereColumn+") FROM `" + type.getValue() + "` WHERE 1",
					1,
					whereObject);
	        ResultSet rs = ps.executeQuery();
	        //SQLLiteHandler.addRows(QueryType.READ, rs.getMetaData().getColumnCount());
	        while (rs.next()) 
	        {
	        	return rs.getInt(1);
	        }
	    } catch (SQLException e) 
		{
			  if(type.getObject() instanceof SQLiteHandable)
			  {
				  SQLiteHandable mh = (SQLiteHandable) type.getObject();
				  mh.log(Level.WARNING, "Could not summarized "+type.getObject().getClass().getName()+" Object!", e);
			  }
		}
		return 0;
	}
	
	public ArrayList<Object> getList(Type type, String orderByColumn, int start, int quantity, String whereColumn, Object...whereObject)
	{
		Object object = type.getObject();
		if(object instanceof SQLiteHandable)
		{
			SQLiteHandable mh = (SQLiteHandable) object;
			try (Connection conn = plugin.getSQLLiteSetup().getConnection();)
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
		if(object instanceof SQLiteHandable)
		{
			SQLiteHandable mh = (SQLiteHandable) object;
			try (Connection conn = plugin.getSQLLiteSetup().getConnection();)
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