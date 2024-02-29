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

public class GroupData implements MysqlHandable
{
	private int id;
	private String groupName;
	private UUID grandmasterUUID;
	private long creationTime;
	private String displayDescription;
	private double groupTechExp;
	private int groupLevel;
	
	private int maxAmountPlayer;
	private int maxAmountMaster;
	private int maxAmountVice;
	private int maxAmountCouncilMember;
	private int maxAmountMember;
	
	private int groupCounterFailedUpkeep;
	
	private double defaultGroupTechExpDailyUpkeep_Master;
	private double defaultGroupTechExpDailyUpkeep_Vice;
	private double defaultGroupTechExpDailyUpkeep_CouncilMember;
	private double defaultGroupTechExpDailyUpkeep_Member;
	private double defaultGroupTechExpDailyUpkeep_Adept;
	
	public GroupData()
	{}
	
	public GroupData(int id, String groupName, UUID grandmasterUUID, long creationTime, String displayDescription, double groupTechExp, int groupLevel,
			int maxAmountPlayer, int maxAmountMaster, int maxAmountVice, int maxAmountCouncilMember, int maxAmountMember,
			int groupCounterFailedUpkeep, double defaultGroupTechExpDailyUpkeep_Master, double defaultGroupTechExpDailyUpkeep_Vice,
			double defaultGroupTechExpDailyUpkeep_CouncilMember, double defaultGroupTechExpDailyUpkeep_Member,
			double defaultGroupTechExpDailyUpkeep_Adpet)
	{
		setId(id);
		setGroupName(groupName);
		setGrandmasterUUID(grandmasterUUID);
		setCreationTime(creationTime);
		setDisplayDescription(displayDescription);
		setGroupTechExp(groupTechExp);
		setGroupLevel(groupLevel);
		setMaxAmountPlayer(maxAmountPlayer);
		setMaxAmountMaster(maxAmountMaster);
		setMaxAmountVice(maxAmountVice);
		setMaxAmountCouncilMember(maxAmountCouncilMember);
		setMaxAmountMember(maxAmountMember);
		setGroupCounterFailedUpkeep(groupCounterFailedUpkeep);
		setDefaultGroupTechExpDailyUpkeep_Master(defaultGroupTechExpDailyUpkeep_Master);
		setDefaultGroupTechExpDailyUpkeep_Vice(defaultGroupTechExpDailyUpkeep_Vice);
		setDefaultGroupTechExpDailyUpkeep_CouncilMember(defaultGroupTechExpDailyUpkeep_CouncilMember);
		setDefaultGroupTechExpDailyUpkeep_Member(defaultGroupTechExpDailyUpkeep_Member);
		setDefaultGroupTechExpDailyUpkeep_Adept(defaultGroupTechExpDailyUpkeep_Adpet);
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

	public UUID getGrandmasterUUID()
	{
		return grandmasterUUID;
	}

	public void setGrandmasterUUID(UUID grandmasterUUID)
	{
		this.grandmasterUUID = grandmasterUUID;
	}

	public long getCreationTime()
	{
		return creationTime;
	}

	public void setCreationTime(long creationTime)
	{
		this.creationTime = creationTime;
	}

	public String getDisplayDescription()
	{
		return displayDescription;
	}

	public void setDisplayDescription(String displayDescription)
	{
		this.displayDescription = displayDescription;
	}

	public double getGroupTechExp()
	{
		return groupTechExp;
	}

	public void setGroupTechExp(double groupTechExp)
	{
		this.groupTechExp = groupTechExp;
	}

	public int getGroupLevel()
	{
		return groupLevel;
	}

	public void setGroupLevel(int groupLevel)
	{
		this.groupLevel = groupLevel;
	}

	public int getMaxAmountPlayer()
	{
		return maxAmountPlayer;
	}

	public void setMaxAmountPlayer(int maxAmountPlayer)
	{
		this.maxAmountPlayer = maxAmountPlayer;
	}

	public int getMaxAmountMaster()
	{
		return maxAmountMaster;
	}

	public void setMaxAmountMaster(int maxAmountMaster)
	{
		this.maxAmountMaster = maxAmountMaster;
	}

	public int getMaxAmountVice()
	{
		return maxAmountVice;
	}

	public void setMaxAmountVice(int maxAmountVice)
	{
		this.maxAmountVice = maxAmountVice;
	}

	public int getMaxAmountCouncilMember()
	{
		return maxAmountCouncilMember;
	}

	public void setMaxAmountCouncilMember(int maxAmountCouncilMember)
	{
		this.maxAmountCouncilMember = maxAmountCouncilMember;
	}

	public int getMaxAmountMember()
	{
		return maxAmountMember;
	}

	public void setMaxAmountMember(int maxAmountMember)
	{
		this.maxAmountMember = maxAmountMember;
	}

	public int getGroupCounterFailedUpkeep()
	{
		return groupCounterFailedUpkeep;
	}

	public void setGroupCounterFailedUpkeep(int groupCounterFailedUpkeep)
	{
		this.groupCounterFailedUpkeep = groupCounterFailedUpkeep;
	}

	public double getDefaultGroupTechExpDailyUpkeep_Master()
	{
		return defaultGroupTechExpDailyUpkeep_Master;
	}

	public void setDefaultGroupTechExpDailyUpkeep_Master(double defaultGroupTechExpDailyUpkeep_Master)
	{
		this.defaultGroupTechExpDailyUpkeep_Master = defaultGroupTechExpDailyUpkeep_Master;
	}

	public double getDefaultGroupTechExpDailyUpkeep_Vice()
	{
		return defaultGroupTechExpDailyUpkeep_Vice;
	}

	public void setDefaultGroupTechExpDailyUpkeep_Vice(double defaultGroupTechExpDailyUpkeep_Vice)
	{
		this.defaultGroupTechExpDailyUpkeep_Vice = defaultGroupTechExpDailyUpkeep_Vice;
	}

	public double getDefaultGroupTechExpDailyUpkeep_CouncilMember()
	{
		return defaultGroupTechExpDailyUpkeep_CouncilMember;
	}

	public void setDefaultGroupTechExpDailyUpkeep_CouncilMember(double defaultGroupTechExpDailyUpkeep_CouncilMember)
	{
		this.defaultGroupTechExpDailyUpkeep_CouncilMember = defaultGroupTechExpDailyUpkeep_CouncilMember;
	}

	public double getDefaultGroupTechExpDailyUpkeep_Member()
	{
		return defaultGroupTechExpDailyUpkeep_Member;
	}

	public void setDefaultGroupTechExpDailyUpkeep_Member(double defaultGroupTechExpDailyUpkeep_Member)
	{
		this.defaultGroupTechExpDailyUpkeep_Member = defaultGroupTechExpDailyUpkeep_Member;
	}

	public double getDefaultGroupTechExpDailyUpkeep_Adept()
	{
		return defaultGroupTechExpDailyUpkeep_Adept;
	}

	public void setDefaultGroupTechExpDailyUpkeep_Adept(double defaultGroupTechExpDailyUpkeep_Adept)
	{
		this.defaultGroupTechExpDailyUpkeep_Adept = defaultGroupTechExpDailyUpkeep_Adept;
	}
	
	public double getDefaultGroupTechExpDailyUpkeep(GroupHandler.Position pos)
	{
		switch(pos)
		{
		case MASTER: return getDefaultGroupTechExpDailyUpkeep_Master();
		case VICE: return getDefaultGroupTechExpDailyUpkeep_Vice();
		case COUNCILMEMBER: return getDefaultGroupTechExpDailyUpkeep_CouncilMember();
		case MEMBER: return getDefaultGroupTechExpDailyUpkeep_Member();
		case ADEPT: return getDefaultGroupTechExpDailyUpkeep_Adept();
		default: break;
		}
		return 0;
	}
	
	@Override
	public boolean create(Connection conn, String tablename)
	{
		try
		{
			String sql = "INSERT INTO `" + tablename
					+ "`(`group_name`, `grandmaster_uuid`, `creation_time`, `display_descrition`, `group_tech_exp`, `group_level`,"
					+ " `max_amount_player`, `max_amount_master`, `max_amount_vice`, `max_amount_councilmember`, `max_amount_member`, `max_amount_adept`,"
					+ " `group_counter_failed_upkeep`, `default_group_tech_exp_daily_upkeep_master`, `default_group_tech_exp_daily_upkeep_vice`,"
					+ " `default_group_tech_exp_daily_upkeep_councilmember`, `default_group_tech_exp_daily_upkeep_member`, `default_group_tech_exp_daily_upkeep_adept`) " 
					+ "VALUES(?, ?, ?, ?, ?, ?,"
					+ " ?, ?, ?, ?, ?, ?,"
					+ " ?, ?, ?,"
					+ " ?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setString(1, getGroupName());
	        ps.setString(2, getGrandmasterUUID().toString());
	        ps.setLong(3, getCreationTime());
	        ps.setString(4, getDisplayDescription());
	        ps.setDouble(5, getGroupTechExp());
	        ps.setInt(6, getGroupLevel());
	        ps.setInt(7, getMaxAmountPlayer());
	        ps.setInt(8, getMaxAmountMaster());
	        ps.setInt(9, getMaxAmountVice());
	        ps.setInt(10, getMaxAmountCouncilMember());
	        ps.setInt(11, getMaxAmountMember());
	        ps.setInt(12, getGroupCounterFailedUpkeep());
	        ps.setDouble(13, getDefaultGroupTechExpDailyUpkeep_Master());
	        ps.setDouble(14, getDefaultGroupTechExpDailyUpkeep_Vice());
	        ps.setDouble(15, getDefaultGroupTechExpDailyUpkeep_CouncilMember());
	        ps.setDouble(16, getDefaultGroupTechExpDailyUpkeep_Member());
	        ps.setDouble(17, getDefaultGroupTechExpDailyUpkeep_Adept());
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
				+ "` SET `group_name` = ?, `grandmaster_uuid` = ?, `creation_time` = ?, `display_descrition` = ?, `group_tech_exp` = ?, `group_level` = ?,"
				+ " `max_amount_player` = ?, `max_amount_master` = ?, `max_amount_vice` = ?, `max_amount_councilmember` = ?, `max_amount_member` = ?,"
				+ " `group_counter_failed_upkeep` = ?, `default_group_tech_exp_daily_upkeep_master` = ?, `default_group_tech_exp_daily_upkeep_vice` = ?,"
				+ " `default_group_tech_exp_daily_upkeep_councilmember` = ?, `default_group_tech_exp_daily_upkeep_member` = ?, `default_group_tech_exp_daily_upkeep_adept` = ?"
				+ " WHERE "+whereColumn;
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, getGroupName());
	        ps.setString(2, getGrandmasterUUID().toString());
	        ps.setLong(3, getCreationTime());
	        ps.setString(4, getDisplayDescription());
	        ps.setDouble(5, getGroupTechExp());
	        ps.setInt(6, getGroupLevel());
	        ps.setInt(7, getMaxAmountPlayer());
	        ps.setInt(8, getMaxAmountMaster());
	        ps.setInt(9, getMaxAmountVice());
	        ps.setInt(10, getMaxAmountCouncilMember());
	        ps.setInt(11, getMaxAmountMember());
	        ps.setInt(12, getGroupCounterFailedUpkeep());
	        ps.setDouble(13, getDefaultGroupTechExpDailyUpkeep_Master());
	        ps.setDouble(14, getDefaultGroupTechExpDailyUpkeep_Vice());
	        ps.setDouble(15, getDefaultGroupTechExpDailyUpkeep_CouncilMember());
	        ps.setDouble(16, getDefaultGroupTechExpDailyUpkeep_Member());
	        ps.setDouble(17, getDefaultGroupTechExpDailyUpkeep_Adept());
			int i = 18;
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
				al.add(new GroupData(rs.getInt("id"),
						rs.getString("group_name"),
						UUID.fromString(rs.getString("grandmaster_uuid")),
						rs.getLong("creation_time"),
						rs.getString("display_descrition"),
						rs.getDouble("group_tech_exp"),
						rs.getInt("group_level"),
						rs.getInt("max_amount_player"),
						rs.getInt("max_amount_master"),
						rs.getInt("max_amount_vice"),
						rs.getInt("max_amount_councilmember"),
						rs.getInt("max_amount_member"),
						rs.getInt("group_counter_failed_upkeep"),
						rs.getDouble("default_group_tech_exp_daily_upkeep_master"),
						rs.getDouble("default_group_tech_exp_daily_upkeep_vice"),
						rs.getDouble("default_group_tech_exp_daily_upkeep_councilmember"),
						rs.getDouble("default_group_tech_exp_daily_upkeep_member"),
						rs.getDouble("default_group_tech_exp_daily_upkeep_adept")));
			}
			return al;
		} catch (SQLException e)
		{
			this.log(Level.WARNING, "SQLException! Could not get a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return new ArrayList<>();
	}
	
	public static ArrayList<GroupData> convert(ArrayList<Object> arrayList)
	{
		ArrayList<GroupData> l = new ArrayList<>();
		for(Object o : arrayList)
		{
			if(o instanceof GroupData)
			{
				l.add((GroupData) o);
			}
		}
		return l;
	}
}