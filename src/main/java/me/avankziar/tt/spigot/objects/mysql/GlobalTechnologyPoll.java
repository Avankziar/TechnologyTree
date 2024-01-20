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

public class GlobalTechnologyPoll implements MysqlHandable
{
	private int id;
	private UUID playerUUID;
	private String choosen_Technology;
	private int choosen_Technology_Researchlevel;  //Eigentlich global_choosen_Technology_Researchlevel, aka das RLvl vom GlobalchoosenTech
	private boolean processedInPoll;
	private boolean processedInRepayment;
	private String global_Choosen_Technology;
	private int global_Choosen_Technology_ID;
	private int total_Participants;
	
	public GlobalTechnologyPoll()
	{
		
	}
	
	public GlobalTechnologyPoll(int id, UUID playerUUID, String choosen_Technology, int choosen_Technology_Researchlevel, 
			boolean processedInPoll, boolean processedInRepayment,
			String global_Choosen_Technology, int global_Choosen_Technology_ID, int total_Participants)
	{
		setId(id);
		setPlayerUUID(playerUUID);
		setChoosen_Technology(choosen_Technology);
		setChoosen_Technology_Researchlevel(choosen_Technology_Researchlevel);
		setProcessedInPoll(processedInPoll);
		setProcessedInRepayment(processedInRepayment);
		setGlobal_Choosen_Technology(global_Choosen_Technology);
		setGlobal_Choosen_Technology_ID(global_Choosen_Technology_ID);
		setTotal_Participants(total_Participants);
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public UUID getPlayerUUID()
	{
		return playerUUID;
	}

	public void setPlayerUUID(UUID playerUUID)
	{
		this.playerUUID = playerUUID;
	}

	public String getChoosen_Technology()
	{
		return choosen_Technology;
	}

	public void setChoosen_Technology(String choosen_Technology)
	{
		this.choosen_Technology = choosen_Technology;
	}
	
	public int getChoosen_Technology_Researchlevel()
	{
		return choosen_Technology_Researchlevel;
	}

	public void setChoosen_Technology_Researchlevel(int choosen_Technology_Researchlevel)
	{
		this.choosen_Technology_Researchlevel = choosen_Technology_Researchlevel;
	}

	public boolean isProcessedInPoll()
	{
		return processedInPoll;
	}

	public void setProcessedInPoll(boolean processedInPoll)
	{
		this.processedInPoll = processedInPoll;
	}

	public boolean isProcessedInRepayment()
	{
		return processedInRepayment;
	}

	public void setProcessedInRepayment(boolean processedInRepayment)
	{
		this.processedInRepayment = processedInRepayment;
	}

	public String getGlobal_Choosen_Technology()
	{
		return global_Choosen_Technology;
	}

	public void setGlobal_Choosen_Technology(String global_Choosen_Technology)
	{
		this.global_Choosen_Technology = global_Choosen_Technology;
	}

	public int getGlobal_Choosen_Technology_ID()
	{
		return global_Choosen_Technology_ID;
	}

	public void setGlobal_Choosen_Technology_ID(int global_Choosen_Technology_ID)
	{
		this.global_Choosen_Technology_ID = global_Choosen_Technology_ID;
	}

	public int getTotal_Participants()
	{
		return total_Participants;
	}

	public void setTotal_Participants(int total_Participants)
	{
		this.total_Participants = total_Participants;
	}

	@Override
	public boolean create(Connection conn, String tablename)
	{
		try
		{
			String sql = "INSERT INTO `" + tablename
					+ "`(`player_uuid`, `choosen_technology`, `choosen_technology_researchlevel`, `processed_in_poll`, "
					+ "`processed_in_repayment`, `global_choosen_technology`, `global_choosen_technology_id`, `total_participants`) " 
					+ "VALUES(?, ?, ?"
					+ "?, ?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setString(1, getPlayerUUID().toString());
	        ps.setString(2, getChoosen_Technology());
	        ps.setInt(3, getChoosen_Technology_Researchlevel());
	        ps.setBoolean(4, isProcessedInPoll());
	        ps.setBoolean(5, isProcessedInRepayment());
	        ps.setString(6, getGlobal_Choosen_Technology());
	        ps.setInt(7, getGlobal_Choosen_Technology_ID());
	        ps.setInt(8, getTotal_Participants());
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
				+ "` SET `player_uuid` = ?, `choosen_technology` = ?, `choosen_technology_researchlevel`,"
				+ " `processed_in_poll` = ?, `processed_in_repayment` = ?,"
				+ " `global_choosen_technology` = ?, `global_choosen_technology_id` = ?, `total_participants` = ?"
				+ " WHERE "+whereColumn;
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, getPlayerUUID().toString());
	        ps.setString(2, getChoosen_Technology());
	        ps.setInt(3, getChoosen_Technology_Researchlevel());
	        ps.setBoolean(4, isProcessedInPoll());
	        ps.setBoolean(5, isProcessedInRepayment());
	        ps.setString(6, getGlobal_Choosen_Technology()); 
	        ps.setInt(7, getGlobal_Choosen_Technology_ID());
	        ps.setInt(8, getTotal_Participants());
			int i = 9;
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
				al.add(new GlobalTechnologyPoll(rs.getInt("id"),
						UUID.fromString(rs.getString("player_uuid")),
						rs.getString("choosen_technology"),
						rs.getInt("choosen_technology_researchlevel"),
						rs.getBoolean("processed_in_poll"),
						rs.getBoolean("processed_in_repayment"),
						rs.getString("global_choosen_technology"),
						rs.getInt("global_choosen_technology_id"),
						rs.getInt("total_participants")));
			}
			return al;
		} catch (SQLException e)
		{
			this.log(Level.WARNING, "SQLException! Could not get a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return new ArrayList<>();
	}
	
	public static ArrayList<GlobalTechnologyPoll> convert(ArrayList<Object> arrayList)
	{
		ArrayList<GlobalTechnologyPoll> l = new ArrayList<>();
		for(Object o : arrayList)
		{
			if(o instanceof GlobalTechnologyPoll)
			{
				l.add((GlobalTechnologyPoll) o);
			}
		}
		return l;
	}
}