package main.java.me.avankziar.tt.spigot.objects.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

import main.java.me.avankziar.tt.spigot.database.MysqlHandable;
import main.java.me.avankziar.tt.spigot.database.MysqlHandler;
import main.java.me.avankziar.tt.spigot.objects.EntryQueryType;
import main.java.me.avankziar.tt.spigot.objects.EntryStatusType;

public class SoloEntryQueryStatus implements MysqlHandable
{
	private int id;
	private String internName;
	private UUID playerUUID;
	
	private EntryQueryType entryQueryType;
	private EntryStatusType statusType;
	private int researchLevel;
	private long durationUntilExpiration;
	
	public SoloEntryQueryStatus()
	{
		
	}
	
	public SoloEntryQueryStatus(int id, String internName, UUID playerUUID,
			EntryQueryType entryQueryType, EntryStatusType statusType, int researchLevel,
			long durationUntilExpiration)
	{
		setId(id);
		setInternName(internName);
		setPlayerUUID(playerUUID);
		setEntryQueryType(entryQueryType);
		setStatusType(statusType);
		setResearchLevel(researchLevel);
		setDurationUntilExpiration(durationUntilExpiration);
	}
	
	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getInternName()
	{
		return internName;
	}

	public void setInternName(String internName)
	{
		this.internName = internName;
	}
	
	public UUID getPlayerUUID()
	{
		return playerUUID;
	}

	public void setPlayerUUID(UUID playerUUID)
	{
		this.playerUUID = playerUUID;
	}

	public EntryQueryType getEntryQueryType()
	{
		return entryQueryType;
	}

	public void setEntryQueryType(EntryQueryType entryQueryType)
	{
		this.entryQueryType = entryQueryType;
	}

	public EntryStatusType getStatusType()
	{
		return statusType;
	}

	public void setStatusType(EntryStatusType statusType)
	{
		this.statusType = statusType;
	}
	
	public int getResearchLevel()
	{
		return researchLevel;
	}

	public void setResearchLevel(int researchLevel)
	{
		this.researchLevel = researchLevel;
	}

	public long getDurationUntilExpiration()
	{
		return durationUntilExpiration;
	}

	public void setDurationUntilExpiration(long durationUntilExpiration)
	{
		this.durationUntilExpiration = durationUntilExpiration;
	}

	@Override
	public boolean create(Connection conn, String tablename)
	{
		try
		{
			String sql = "INSERT INTO `" + tablename
					+ "`(`intern_name`, `player_uuid`, `entry_query_type`, `status_type`, `research_level`, `duration_until_expiration`) " 
					+ "VALUES(?, ?, ?, ?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setString(1, getInternName());
	        ps.setString(2, getPlayerUUID().toString());
	        ps.setString(3, getEntryQueryType().toString());
	        ps.setString(4, getStatusType().toString());
	        ps.setInt(5, getResearchLevel());
	        ps.setLong(6, getDurationUntilExpiration());
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
				+ "` SET `intern_name` = ?, `player_uuid` = ?, `entry_query_type` = ?, `status_type` = ?, `research_level` = ?,"
				+ " `duration_until_expiration` = ?" 
				+ " WHERE "+whereColumn;
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, getInternName());
	        ps.setString(2, getPlayerUUID().toString());
	        ps.setString(3, getEntryQueryType().toString());
	        ps.setString(4, getStatusType().toString());
	        ps.setInt(5, getResearchLevel());
	        ps.setLong(6, getDurationUntilExpiration());
			int i = 7;
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
				al.add(new SoloEntryQueryStatus(rs.getInt("id"),
						rs.getString("intern_name"),
						UUID.fromString(rs.getString("player_uuid")),
						EntryQueryType.valueOf(rs.getString("entry_query_type")),
						EntryStatusType.valueOf(rs.getString("status_type")),
						rs.getInt("research_level"),
						rs.getLong("duration_until_expiration")));
			}
			return al;
		} catch (SQLException e)
		{
			this.log(Level.WARNING, "SQLException! Could not get a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return new ArrayList<>();
	}
	
	public static ArrayList<SoloEntryQueryStatus> convert(ArrayList<Object> arrayList)
	{
		ArrayList<SoloEntryQueryStatus> l = new ArrayList<>();
		for(Object o : arrayList)
		{
			if(o instanceof SoloEntryQueryStatus)
			{
				l.add((SoloEntryQueryStatus) o);
			}
		}
		return l;
	}
}