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
import main.java.me.avankziar.tt.spigot.objects.PlayerAssociatedType;

public class UpdateTech implements MysqlHandable
{
	private int id;
	private UUID uUID;
	private PlayerAssociatedType playerAssociatedType;
	private String technology;
	private int globalTechnologyPollID;
	private int researchLevel;
	
	public UpdateTech()
	{}
	
	public UpdateTech(int id, UUID uUID, PlayerAssociatedType playerAssociatedType, String technology, int globalTechnologyPollID,
			int researchLevel)
	{
		setId(id);
		setUUID(uUID);
		setPlayerAssociatedType(playerAssociatedType);
		setTechnology(technology);
		setGlobalTechnologyPollID(globalTechnologyPollID);
		setResearchLevel(researchLevel);
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public UUID getUUID()
	{
		return uUID;
	}

	public void setUUID(UUID uUID)
	{
		this.uUID = uUID;
	}

	public PlayerAssociatedType getPlayerAssociatedType()
	{
		return playerAssociatedType;
	}

	public void setPlayerAssociatedType(PlayerAssociatedType playerAssociatedType)
	{
		this.playerAssociatedType = playerAssociatedType;
	}

	public String getTechnology()
	{
		return technology;
	}

	public void setTechnology(String technology)
	{
		this.technology = technology;
	}

	public int getGlobalTechnologyPollID()
	{
		return globalTechnologyPollID;
	}

	public void setGlobalTechnologyPollID(int globalTechnologyPollID)
	{
		this.globalTechnologyPollID = globalTechnologyPollID;
	}

	public int getResearchLevel()
	{
		return researchLevel;
	}

	public void setResearchLevel(int researchLevel)
	{
		this.researchLevel = researchLevel;
	}
	
	@Override
	public boolean create(Connection conn, String tablename)
	{
		try
		{
			String sql = "INSERT INTO `" + tablename
					+ "`(`player_uuid`, `player_associated_type`, `technology`, `global_technology_poll_id`, `research_level`) " 
					+ "VALUES(?, ?, ?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setString(1, getUUID().toString());
	        ps.setString(2, getPlayerAssociatedType().toString());
	        ps.setString(3, getTechnology());
	        ps.setInt(4, getGlobalTechnologyPollID());
	        ps.setInt(5, getResearchLevel());
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
				+ "` SET `player_uuid` = ?, `player_associated_type` = ?, `technology` = ?, `global_technology_poll_id` = ?, `research_level` = ?"
				+ " WHERE "+whereColumn;
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, getUUID().toString());
	        ps.setString(2, getPlayerAssociatedType().toString());
	        ps.setString(3, getTechnology());
	        ps.setInt(4, getGlobalTechnologyPollID());
	        ps.setInt(5, getResearchLevel());
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
				al.add(new UpdateTech(rs.getInt("id"),
						UUID.fromString(rs.getString("player_uuid")),
						PlayerAssociatedType.valueOf(rs.getString("player_associated_type")),
						rs.getString("technology"),
						rs.getInt("global_technology_poll_id"),
						rs.getInt("research_level")));
			}
			return al;
		} catch (SQLException e)
		{
			this.log(Level.WARNING, "SQLException! Could not get a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return new ArrayList<>();
	}
	
	public static ArrayList<UpdateTech> convert(ArrayList<Object> arrayList)
	{
		ArrayList<UpdateTech> l = new ArrayList<>();
		for(Object o : arrayList)
		{
			if(o instanceof UpdateTech)
			{
				l.add((UpdateTech) o);
			}
		}
		return l;
	}
}