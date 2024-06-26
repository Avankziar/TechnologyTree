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
import main.java.me.avankziar.tt.spigot.gui.objects.SettingsLevel;

public class PlayerData implements MysqlHandable
{
	private int id;
	private UUID uuid;
	private String name;
	private boolean showSyncMessage; //Nachricht, welche beim Login angezeigt wird, wenn die Tech etc. synchronisiert werden.
	private boolean showRewardMessage; //Nachricht, welche bei der Bearbeitung der Belohnung angezeigt wird.
	private double actualTTExp; //TTExp die man gerade hat.
	private double totalReceivedTTExp; //TTExp die man insgesamt gehabt hat.
	private int vanillaExpStillToBeObtained; //Wenn man nicht online ist und doch Vanilla Exp bekommt.
	private SettingsLevel lastSettingLevel;
	private String switchMode; //SwitchMode from SwitchModeHandler;
	private long switchModeCooldown; //Cooldown when a new SwitchMode can be picked.
	
	public PlayerData(){}
	
	public PlayerData(int id, UUID uuid, String name, boolean showSyncMessage, boolean showRewardMessage,
			double actualTTExp, double totalReceivedTTExp,
			int vanillaExpStillToBeObtained, SettingsLevel lastSettingLevel, String switchMode, long switchModeCooldown)
			
	{
		setId(id);
		setUUID(uuid);
		setName(name);
		setShowSyncMessage(showSyncMessage);
		setShowRewardMessage(showRewardMessage);
		setActualTTExp(actualTTExp);
		setTotalReceivedTTExp(totalReceivedTTExp);
		setVanillaExpStillToBeObtained(vanillaExpStillToBeObtained);
		setLastSettingLevel(lastSettingLevel);
		setSwitchMode(switchMode);
		setSwitchModeCooldown(switchModeCooldown);
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
		return uuid;
	}

	public void setUUID(UUID uuid)
	{
		this.uuid = uuid;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public boolean isShowSyncMessage()
	{
		return showSyncMessage;
	}

	public void setShowSyncMessage(boolean showSyncMessage)
	{
		this.showSyncMessage = showSyncMessage;
	}

	public boolean isShowRewardMessage()
	{
		return showRewardMessage;
	}

	public void setShowRewardMessage(boolean showRewardMessage)
	{
		this.showRewardMessage = showRewardMessage;
	}

	public double getActualTTExp()
	{
		return actualTTExp;
	}

	public void setActualTTExp(double actualTTExp)
	{
		this.actualTTExp = actualTTExp;
	}

	public double getTotalReceivedTTExp()
	{
		return totalReceivedTTExp;
	}

	public void setTotalReceivedTTExp(double totalReceivedTTExp)
	{
		this.totalReceivedTTExp = totalReceivedTTExp;
	}

	public int getVanillaExpStillToBeObtained()
	{
		return vanillaExpStillToBeObtained;
	}

	public void setVanillaExpStillToBeObtained(int vanillaExpStillToBeObtained)
	{
		this.vanillaExpStillToBeObtained = vanillaExpStillToBeObtained;
	}

	public SettingsLevel getLastSettingLevel()
	{
		return lastSettingLevel;
	}

	public void setLastSettingLevel(SettingsLevel lastSettingLevel)
	{
		this.lastSettingLevel = lastSettingLevel;
	}

	public String getSwitchMode()
	{
		return switchMode;
	}

	public void setSwitchMode(String switchMode)
	{
		this.switchMode = switchMode;
	}

	public long getSwitchModeCooldown()
	{
		return switchModeCooldown;
	}

	public void setSwitchModeCooldown(long switchModeCooldown)
	{
		this.switchModeCooldown = switchModeCooldown;
	}

	@Override
	public boolean create(Connection conn, String tablename)
	{
		try
		{
			String sql = "INSERT INTO `" + tablename
					+ "`(`player_uuid`, `player_name`, `show_sync_msg`, `show_reward_msg`,"
					+ " `ttexp_actual`, `ttexp_total_received`, `vanilla_exp_still_to_be_obtained`, `lastSettingLevel`,"
					+ " `switchmode`, `switchmode_cooldown`) " 
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setString(1, getUUID().toString());
	        ps.setString(2, getName());
	        ps.setBoolean(3, isShowSyncMessage());
	        ps.setBoolean(4, isShowRewardMessage());
	        ps.setDouble(5, getActualTTExp());
	        ps.setDouble(6, getTotalReceivedTTExp());
	        ps.setInt(7, getVanillaExpStillToBeObtained());
	        ps.setString(8, getLastSettingLevel().toString());
	        ps.setString(9, getSwitchMode());
	        ps.setLong(10, getSwitchModeCooldown());
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
				+ "` SET `player_uuid` = ?, `player_name` = ?, `show_sync_msg` = ?, `show_reward_msg` = ?,"
				+ " `ttexp_actual` = ?, `ttexp_total_received` = ?, `vanilla_exp_still_to_be_obtained` = ?, `lastSettingLevel` = ?,"
				+ " `switchmode` = ?, `switchmode_cooldown` = ?"
				+ " WHERE "+whereColumn;
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, getUUID().toString());
		    ps.setString(2, getName());
		    ps.setBoolean(3, isShowSyncMessage());
		    ps.setBoolean(4, isShowRewardMessage());
	        ps.setDouble(5, getActualTTExp());
	        ps.setDouble(6, getTotalReceivedTTExp());
	        ps.setInt(7, getVanillaExpStillToBeObtained());
	        ps.setString(8, getLastSettingLevel().toString());
	        ps.setString(9, getSwitchMode());
	        ps.setLong(10, getSwitchModeCooldown());
			int i = 11;
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
				al.add(new PlayerData(rs.getInt("id"),
						UUID.fromString(rs.getString("player_uuid")),
						rs.getString("player_name"),
						rs.getBoolean("show_sync_msg"),
						rs.getBoolean("show_reward_msg"),
						rs.getDouble("ttexp_actual"),
						rs.getDouble("ttexp_total_received"),
						rs.getInt("vanilla_exp_still_to_be_obtained"),
						SettingsLevel.valueOf(rs.getString("lastSettingLevel")),
						rs.getString("switchmode"),
						rs.getLong("switchmode_cooldown")));
			}
			return al;
		} catch (SQLException e)
		{
			this.log(Level.WARNING, "SQLException! Could not get a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return new ArrayList<>();
	}
	
	public static ArrayList<PlayerData> convert(ArrayList<Object> arrayList)
	{
		ArrayList<PlayerData> l = new ArrayList<>();
		for(Object o : arrayList)
		{
			if(o instanceof PlayerData)
			{
				l.add((PlayerData) o);
			}
		}
		return l;
	}
}