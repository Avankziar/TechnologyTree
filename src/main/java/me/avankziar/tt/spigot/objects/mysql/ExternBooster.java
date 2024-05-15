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
import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.PlayerAssociatedType;
import main.java.me.avankziar.tt.spigot.objects.RewardType;

public class ExternBooster implements MysqlHandable
{
	private int id;
	private String name;
	private EventType eventType;
	private PlayerAssociatedType playerAssociatedType;
	private RewardType rewardType;
	private double factor;
	private long expiryDate;
	private String permission;
	private String playerUUID;
	private String groupname;

	public ExternBooster()
	{
		
	}
	
	public ExternBooster(int id, String name, EventType eventType, PlayerAssociatedType playerAssociatedType, RewardType rewardType,
			double factor, long expiryDate, String permission, String playerUUID, String groupname)
	{
		setId(id);
		setName(name);
		setEventType(eventType);
		setPlayerAssociatedType(playerAssociatedType);
		setRewardType(rewardType);
		setFactor(factor);
		setExpiryDate(expiryDate);
		setPermission(permission);
		setPlayerUUID(playerUUID);
		setGroupname(groupname);
	}
	
	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public EventType getEventType()
	{
		return eventType;
	}

	public void setEventType(EventType eventType)
	{
		this.eventType = eventType;
	}

	public PlayerAssociatedType getPlayerAssociatedType()
	{
		return playerAssociatedType;
	}

	public void setPlayerAssociatedType(PlayerAssociatedType playerAssociatedType)
	{
		this.playerAssociatedType = playerAssociatedType;
	}

	public RewardType getRewardType()
	{
		return rewardType;
	}

	public void setRewardType(RewardType rewardType)
	{
		this.rewardType = rewardType;
	}

	public double getFactor()
	{
		return factor;
	}

	public void setFactor(double factor)
	{
		this.factor = factor;
	}

	public long getExpiryDate()
	{
		return expiryDate;
	}

	public void setExpiryDate(long expiryDate)
	{
		this.expiryDate = expiryDate;
	}

	public String getPermission()
	{
		return permission;
	}

	public void setPermission(String permission)
	{
		this.permission = permission;
	}

	public UUID getPlayerUUID()
	{
		return playerUUID != null ? UUID.fromString(playerUUID) : null;
	}
	
	public String getPlayerUUIDText()
	{
		return playerUUID;
	}

	public void setPlayerUUID(String playerUUID)
	{
		this.playerUUID = playerUUID;
	}

	public String getGroupname()
	{
		return groupname;
	}

	public void setGroupname(String groupname)
	{
		this.groupname = groupname;
	}

	@Override
	public boolean create(Connection conn, String tablename)
	{
		try
		{
			String sql = "INSERT INTO `" + tablename
					+ "`(`booster_name`, `event_type`, `player_associated_type`, `reward_type`, `factor`,"
					+ " `expiry_date`, `permission`, `player_uuid`, `group_name`) " 
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setString(1, getName());
	        ps.setString(2, getEventType().toString());
	        ps.setString(3, getPlayerAssociatedType().toString());
	        ps.setString(4, getRewardType().toString());
	        ps.setDouble(5, getFactor());
	        ps.setLong(6, getExpiryDate());
	        ps.setString(7, getPermission());
	        ps.setString(8, getPlayerUUIDText());
	        ps.setString(9, getGroupname());
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
				+ "` SET `booster_name` = ?, `event_type` = ?, `player_associated_type` = ?, `reward_type` = ?, `factor` = ?,"
				+ " `expiry_date` = ?, `permission` = ?, `player_uuid` = ?, `group_name` = ?"
				+ " WHERE "+whereColumn;
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, getName());
	        ps.setString(2, getEventType().toString());
	        ps.setString(3, getPlayerAssociatedType().toString());
	        ps.setString(4, getRewardType().toString());
	        ps.setDouble(5, getFactor());
	        ps.setLong(6, getExpiryDate());
	        ps.setString(7, getPermission());
	        ps.setString(8, getPlayerUUIDText());
	        ps.setString(9, getGroupname());
			int i = 10;
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
				al.add(new ExternBooster(rs.getInt("id"),
						rs.getString("booster_name"),
						EventType.valueOf(rs.getString("event_type")),
						PlayerAssociatedType.valueOf(rs.getString("player_associated_type")),
						RewardType.valueOf(rs.getString("reward_type")),
						rs.getDouble("factor"),
						rs.getLong("expiry_date"),
						rs.getString("permission"),
						rs.getString("player_uuid"),
						rs.getString("group_name")));
			}
			return al;
		} catch (SQLException e)
		{
			this.log(Level.WARNING, "SQLException! Could not get a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return new ArrayList<>();
	}
	
	public static ArrayList<ExternBooster> convert(ArrayList<Object> arrayList)
	{
		ArrayList<ExternBooster> l = new ArrayList<>();
		for(Object o : arrayList)
		{
			if(o instanceof ExternBooster)
			{
				l.add((ExternBooster) o);
			}
		}
		return l;
	}
}
