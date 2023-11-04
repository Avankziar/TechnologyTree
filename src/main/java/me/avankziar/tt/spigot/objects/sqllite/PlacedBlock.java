package main.java.me.avankziar.tt.spigot.objects.sqllite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.Location;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.database.MysqlHandler;
import main.java.me.avankziar.tt.spigot.database.SQLiteHandable;
import main.java.me.avankziar.tt.spigot.database.SQLiteHandler;

public class PlacedBlock implements SQLiteHandable
{
	private int id;
	private String world;
	private int x;
	private int y;
	private int z;
	private long expirationDate;
	
	public PlacedBlock()
	{
		
	}
	
	public PlacedBlock(int id, String world, int x, int y, int z, long expirationDate)
	{
		setId(id);
		setWorld(world);
		setX(x);
		setY(y);
		setZ(z);
		setExpirationDate(expirationDate);
	}
	
	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getWorld()
	{
		return world;
	}

	public void setWorld(String world)
	{
		this.world = world;
	}

	public int getX()
	{
		return x;
	}

	public void setX(int x)
	{
		this.x = x;
	}

	public int getY()
	{
		return y;
	}

	public void setY(int y)
	{
		this.y = y;
	}

	public int getZ()
	{
		return z;
	}

	public void setZ(int z)
	{
		this.z = z;
	}

	public long getExpirationDate()
	{
		return expirationDate;
	}

	public void setExpirationDate(long expirationDate)
	{
		this.expirationDate = expirationDate;
	}
	
	public static boolean wasPlaced(Location loc)
	{
		return TT.getPlugin().getSQLLiteHandler().exist(SQLiteHandler.Type.PLACEDBLOCKS,
				"`world` = ? AND `x` = ? AND `y` = ? AND `z` = ?",
				loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
	}
	
	public static void delete(Location loc)
	{
		TT.getPlugin().getSQLLiteHandler().deleteData(SQLiteHandler.Type.PLACEDBLOCKS,
				"`world` = ? AND `x` = ? AND `y` = ? AND `z` = ?",
				loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
	}

	@Override
	public boolean create(Connection conn, String tablename)
	{
		try
		{
			String sql = "INSERT INTO `" + tablename
					+ "`(`world`, `x`, `y`, `z`, `expiration_date`) " 
					+ "VALUES(?, ?, ?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setString(1, getWorld());
	        ps.setInt(2, getX());
	        ps.setInt(3, getY());
	        ps.setInt(4, getZ());
	        ps.setLong(5, getExpirationDate());
	        int i = ps.executeUpdate();
	        MysqlHandler.addRows(MysqlHandler.QueryType.INSERT, i);
	        return true;
		} catch (SQLException e)
		{
			this.log(Level.WARNING, "SQLException! Could not create a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return false;
	}

	@Override
	public boolean update(Connection conn, String tablename, String whereColumn, Object... whereObject)
	{
		try
		{
			String sql = "UPDATE `" + tablename
				+ "` SET `world` = ?, `x` = ?, `y` = ?, `z` = ?,"
				+ " `expiration_date` = ?" 
				+ " WHERE "+whereColumn;
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, getWorld());
		    ps.setInt(2, getX());
		    ps.setInt(3, getY());
		    ps.setInt(4, getZ());
		    ps.setLong(5, getExpirationDate());
			int i = 6;
			for(Object o : whereObject)
			{
				ps.setObject(i, o);
				i++;
			}			
			int u = ps.executeUpdate();
			MysqlHandler.addRows(MysqlHandler.QueryType.UPDATE, u);
			return true;
		} catch (SQLException e)
		{
			this.log(Level.WARNING, "SQLException! Could not update a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return false;
	}

	@Override
	public ArrayList<Object> get(Connection conn, String tablename, String orderby, String limit, String whereColumn, Object... whereObject)
	{
		try
		{
			String sql = "SELECT * FROM `" + tablename
				+ "` WHERE "+whereColumn+" ORDER BY "+orderby+limit;
			PreparedStatement ps = conn.prepareStatement(sql);
			int i = 1;
			for(Object o : whereObject)
			{
				ps.setObject(i, o);
				i++;
			}
			
			ResultSet rs = ps.executeQuery();
			MysqlHandler.addRows(MysqlHandler.QueryType.READ, rs.getMetaData().getColumnCount());
			ArrayList<Object> al = new ArrayList<>();
			while (rs.next()) 
			{
				al.add(new PlacedBlock(rs.getInt("id"),
						rs.getString("world"),
						rs.getInt("x"),
						rs.getInt("y"),
						rs.getInt("z"),
						rs.getLong("expiration_date")));
			}
			return al;
		} catch (SQLException e)
		{
			this.log(Level.WARNING, "SQLException! Could not get a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return new ArrayList<>();
	}
	
	public static ArrayList<PlacedBlock> convert(ArrayList<Object> arrayList)
	{
		ArrayList<PlacedBlock> l = new ArrayList<>();
		for(Object o : arrayList)
		{
			if(o instanceof PlacedBlock)
			{
				l.add((PlacedBlock	) o);
			}
		}
		return l;
	}
}
