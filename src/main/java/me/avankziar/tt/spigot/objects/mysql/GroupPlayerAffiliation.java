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
import main.java.me.avankziar.tt.spigot.handler.GroupHandler;
import main.java.me.avankziar.tt.spigot.handler.GroupHandler.Position;

public class GroupPlayerAffiliation implements MysqlHandable
{
	private int id;
	private String groupName;
	private UUID playerUUID;
	private GroupHandler.Position rank;
	
	private double individualTechExpDailyUpkeep;
	
	private boolean canResearch;
	private boolean canKick;
	private boolean canInvite;
	private boolean canAcceptApplication;
	private boolean canPromote;
	private boolean canDemote;
	private boolean canIncreaseLevel;
	private boolean canSetDefaultDailyUpkeep;
	private boolean canSetIndividualDailyUpkeep;
	
	public GroupPlayerAffiliation()
	{}
	
	public GroupPlayerAffiliation(int id, String groupName, UUID playerUUID, GroupHandler.Position rank, double individualTechExpDailyUpkeep,
			boolean canResearch, boolean canKick, boolean canInvite, boolean canAcceptApplication, boolean canPromote, boolean canDemote,
			boolean canIncreaseLevel, boolean canSetDefaultDailyUpkeep, boolean canSetIndividualDailyUpkeep)
	{
		setId(id);
		setGroupName(groupName);
		setPlayerUUID(playerUUID);
		setRank(rank);
		setIndividualTechExpDailyUpkeep(individualTechExpDailyUpkeep);
		setCanResearch(canResearch);
		setCanKick(canKick);
		setCanInvite(canInvite);
		setCanAcceptApplication(canAcceptApplication);
		setCanPromote(canPromote);
		setCanDemote(canDemote);
		setCanIncreaseLevel(canIncreaseLevel);
		setCanSetDefaultDailyUpkeep(canSetDefaultDailyUpkeep);
		setCanSetIndividualDailyUpkeep(canSetIndividualDailyUpkeep);
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getGroupName()
	{
		return groupName;
	}

	public void setGroupName(String groupName)
	{
		this.groupName = groupName;
	}

	public UUID getPlayerUUID()
	{
		return playerUUID;
	}

	public void setPlayerUUID(UUID playerUUID)
	{
		this.playerUUID = playerUUID;
	}

	public GroupHandler.Position getRank()
	{
		return rank;
	}

	public void setRank(GroupHandler.Position rank)
	{
		this.rank = rank;
	}

	public double getIndividualTechExpDailyUpkeep()
	{
		return individualTechExpDailyUpkeep;
	}

	public void setIndividualTechExpDailyUpkeep(double individualTechExpDailyUpkeep)
	{
		this.individualTechExpDailyUpkeep = individualTechExpDailyUpkeep;
	}

	public boolean isCanResearch()
	{
		return canResearch;
	}

	public void setCanResearch(boolean canResearch)
	{
		this.canResearch = canResearch;
	}

	public boolean isCanKick()
	{
		return canKick;
	}

	public void setCanKick(boolean canKick)
	{
		this.canKick = canKick;
	}

	public boolean isCanInvite()
	{
		return canInvite;
	}

	public void setCanInvite(boolean canInvite)
	{
		this.canInvite = canInvite;
	}

	public boolean isCanAcceptApplication()
	{
		return canAcceptApplication;
	}

	public void setCanAcceptApplication(boolean canAcceptApplication)
	{
		this.canAcceptApplication = canAcceptApplication;
	}

	public boolean isCanPromote()
	{
		return canPromote;
	}

	public void setCanPromote(boolean canPromote)
	{
		this.canPromote = canPromote;
	}

	public boolean isCanDemote()
	{
		return canDemote;
	}

	public void setCanDemote(boolean canDemote)
	{
		this.canDemote = canDemote;
	}

	public boolean isCanIncreaseLevel()
	{
		return canIncreaseLevel;
	}

	public void setCanIncreaseLevel(boolean canIncreaseLevel)
	{
		this.canIncreaseLevel = canIncreaseLevel;
	}

	public boolean isCanSetDefaultDailyUpkeep()
	{
		return canSetDefaultDailyUpkeep;
	}

	public void setCanSetDefaultDailyUpkeep(boolean canSetDefaultDailyUpkeep)
	{
		this.canSetDefaultDailyUpkeep = canSetDefaultDailyUpkeep;
	}

	public boolean isCanSetIndividualDailyUpkeep()
	{
		return canSetIndividualDailyUpkeep;
	}

	public void setCanSetIndividualDailyUpkeep(boolean canSetIndividualDailyUpkeep)
	{
		this.canSetIndividualDailyUpkeep = canSetIndividualDailyUpkeep;
	}
	
	public void setAsGrandmaster()
	{
		setRank(Position.MASTER);
		setIndividualTechExpDailyUpkeep(GroupHandler.getDefaultUpKeep(Position.MASTER));
		setCanResearch(true);
		setCanKick(true);
		setCanInvite(true);
		setCanAcceptApplication(true);
		setCanPromote(true);
		setCanDemote(true);
		setCanIncreaseLevel(true);
		setCanSetDefaultDailyUpkeep(true);
		setCanSetIndividualDailyUpkeep(true);
	}

	@Override
	public boolean create(Connection conn, String tablename)
	{
		try
		{
			String sql = "INSERT INTO `" + tablename
					+ "`(`group_name`, `player_uuid`, `ranks`, `rank_ordinal`, `individual_tech_exp_daily_upkeep`,"
					+ " `can_research`, `can_kick`, `can_invite`, `can_acceptapplication`, `can_promote`, `can_demote`,"
					+ " `can_increaselevel`, `can_setdefault_daily_upkeep`, `can_setindividual_daily_upkeep`) " 
					+ "VALUES(?, ?, ?, ?, ?,"
					+ "?, ?, ?, ?, ?, ?,"
					+ "?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setString(1, getGroupName());
	        ps.setString(2, getPlayerUUID().toString());
	        ps.setString(3, getRank().toString());
	        ps.setInt(4, getRank().getRank());
	        ps.setDouble(5, getIndividualTechExpDailyUpkeep());
	        ps.setBoolean(6, isCanResearch());
	        ps.setBoolean(7, isCanKick());
	        ps.setBoolean(8, isCanInvite());
	        ps.setBoolean(9, isCanAcceptApplication());
	        ps.setBoolean(10, isCanPromote());
	        ps.setBoolean(11, isCanDemote());
	        ps.setBoolean(12, isCanIncreaseLevel());
	        ps.setBoolean(13, isCanSetDefaultDailyUpkeep());
	        ps.setBoolean(14, isCanSetIndividualDailyUpkeep());
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
				+ "` SET `group_name` = ?, `player_uuid` = ?, `ranks` = ?, `rank_ordinal` = ?, `individual_tech_exp_daily_upkeep` = ?,"
				+ " `can_research` = ?, `can_kick` = ?, `can_invite` = ?, `can_acceptapplication` = ?, `can_promote` = ?, `can_demote` = ?,"
				+ " `can_increaselevel` = ?, `can_setdefault_daily_upkeep` = ?, `can_setindividual_daily_upkeep` = ?" 
				+ " WHERE "+whereColumn;
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, getGroupName());
	        ps.setString(2, getPlayerUUID().toString());
	        ps.setString(3, getRank().toString());
	        ps.setInt(4, getRank().getRank());
	        ps.setDouble(5, getIndividualTechExpDailyUpkeep());
	        ps.setBoolean(6, isCanResearch());
	        ps.setBoolean(7, isCanKick());
	        ps.setBoolean(8, isCanInvite());
	        ps.setBoolean(9, isCanAcceptApplication());
	        ps.setBoolean(10, isCanPromote());
	        ps.setBoolean(11, isCanDemote());
	        ps.setBoolean(12, isCanIncreaseLevel());
	        ps.setBoolean(13, isCanSetDefaultDailyUpkeep());
	        ps.setBoolean(14, isCanSetIndividualDailyUpkeep());
			int i = 15;
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
				al.add(new GroupPlayerAffiliation(rs.getInt("id"),
						rs.getString("group_name"),
						UUID.fromString(rs.getString("player_uuid")),
						GroupHandler.Position.valueOf(rs.getString("ranks")),
						rs.getDouble("individual_tech_exp_daily_upkeep"),
						rs.getBoolean("can_research"),
						rs.getBoolean("can_kick"),
						rs.getBoolean("can_invite"),
						rs.getBoolean("can_acceptapplication"),
						rs.getBoolean("can_promote"),
						rs.getBoolean("can_demote"),
						rs.getBoolean("can_increaselevel"),
						rs.getBoolean("can_setdefault_daily_upkeep"),
						rs.getBoolean("can_setindividual_daily_upkeep")));
			}
			return al;
		} catch (SQLException e)
		{
			this.log(Level.WARNING, "SQLException! Could not get a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return new ArrayList<>();
	}
	
	public static ArrayList<GroupPlayerAffiliation> convert(ArrayList<Object> arrayList)
	{
		ArrayList<GroupPlayerAffiliation> l = new ArrayList<>();
		for(Object o : arrayList)
		{
			if(o instanceof GroupPlayerAffiliation)
			{
				l.add((GroupPlayerAffiliation) o);
			}
		}
		return l;
	}
}