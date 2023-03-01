package main.java.me.avankziar.tt.spigot.objects.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import main.java.me.avankziar.tt.spigot.database.MysqlHandable;
import main.java.me.avankziar.tt.spigot.database.MysqlHandler;

public class EntryQueryStatus implements MysqlHandable
{
	public enum EntryQueryType
	{
		MAIN_CATEGORY,
		SUB_CATEGORY,
		TECHNOLOGY;
	}
	
	public enum StatusType
	{
		CANNOT_SEE_IT,
		CAN_SEE_IT,
		CAN_RESEARCH_IT,
		HAVE_RESEARCHED_IT;
	}
	
	private int id;
	private String internName;
	private UUID playerUUID;
	private EntryQueryType entryQueryType;
	private StatusType statusType;
	
	
	public EntryQueryStatus(){}
	
	public EntryQueryStatus(int id, String internName, UUID playerUUID, EntryQueryType entryQueryType, StatusType statusType)
	{
		setId(id);
		setInternName(internName);
		setPlayerUUID(playerUUID);
		setEntryQueryType(entryQueryType);
		setStatusType(statusType);
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

	public StatusType getStatusType()
	{
		return statusType;
	}

	public void setStatusType(StatusType statusType)
	{
		this.statusType = statusType;
	}
	
	@Override //FIXME
	public boolean create(Connection conn, String tablename)
	{
		try
		{
			String sql = "INSERT INTO `" + tablename
					+ "`(`player_uuid`, `player_name`, `balance`, `bankaccountlist`,"
					+ " `moneyplayerflow`, `moneybankflow`, `generalmessage`, `pendinginvite`, `frozen`) " 
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setString(1, getUUID().toString());
	        ps.setString(2, getName());
	        ps.setDouble(3, getBalance());
	        ps.setString(4, String.join(";", getBankAccountNumber()));
	        ps.setBoolean(5, isMoneyBankFlow());
	        ps.setBoolean(6, isMoneyPlayerFlow());
	        ps.setBoolean(7, isGeneralMessage());
	        ps.setString(8, getPendingInvite());
	        ps.setBoolean(9, isFrozen());
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
				+ "` SET `player_uuid` = ?, `player_name` = ?, `balance` = ?,"
				+ " `bankaccountlist` = ?, `moneyplayerflow` = ?, `moneybankflow` = ?, `generalmessage` = ?,"
				+ " `pendinginvite` = ?, `frozen` = ?" 
				+ " WHERE "+whereColumn;
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, getUUID().toString());
			ps.setString(2, getName());
			ps.setDouble(3, getBalance());
			ps.setString(4, String.join(";", getBankAccountNumber()));
			ps.setBoolean(5, isMoneyPlayerFlow());
			ps.setBoolean(6, isMoneyBankFlow());
			ps.setBoolean(7, isGeneralMessage());
			ps.setString(8, getPendingInvite());
			ps.setBoolean(9, isFrozen());
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
				String bankacc = rs.getString("bankaccountlist");
				List<String> lists = new ArrayList<>();
				if(bankacc != null)
				{
					lists = Arrays.asList(rs.getString("bankaccountlist").split(";"));
				}
				al.add(new PlayerData(rs.getInt("id"),
						UUID.fromString(rs.getString("player_uuid")),
						rs.getString("player_name"),
						rs.getDouble("balance"),
						lists,
						rs.getBoolean("moneyplayerflow"),
						rs.getBoolean("moneybankflow"),
						rs.getBoolean("generalmessage"),
						rs.getString("pendinginvite"),
						rs.getBoolean("frozen")));
			}
			return al;
		} catch (SQLException e)
		{
			this.log(Level.WARNING, "SQLException! Could not get a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return new ArrayList<>();
	}
	
	public static ArrayList<EntryQueryStatus> convert(ArrayList<Object> arrayList)
	{
		ArrayList<EntryQueryStatus> l = new ArrayList<>();
		for(Object o : arrayList)
		{
			if(o instanceof EntryQueryStatus)
			{
				l.add((EntryQueryStatus) o);
			}
		}
		return l;
	}
}