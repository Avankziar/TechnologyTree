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

public class TechnologyPoll implements MysqlHandable
{
	private int id;
	private UUID playerUUID;
	private String choosen_Technology;
	private boolean processedInRepayment;
	private String global_Choosen_Technology;
	
	public TechnologyPoll()
	{
		
	}
	
	public TechnologyPoll(int id, UUID playerUUID, String choosen_Technology, boolean processedInRepayment,
			String global_Choosen_Technology)
	{
		setId(id);
		setPlayerUUID(playerUUID);
		setChoosen_Technology(choosen_Technology);
		setProcessedInRepayment(processedInRepayment);
		setGlobal_Choosen_Technology(global_Choosen_Technology);
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

	@Override
	public boolean create(Connection conn, String tablename)
	{
		try
		{
			String sql = "INSERT INTO `" + tablename
					+ "`(`player_uuid`, `choosen_technology`, "
					+ "`processed_in_repayment`, `global_choosen_technology`) " 
					+ "VALUES(?, ?, "
					+ "?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setString(1, getPlayerUUID().toString());
	        ps.setString(2, getChoosen_Technology());
	        ps.setBoolean(3, isProcessedInRepayment());
	        ps.setString(4, getGlobal_Choosen_Technology());
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
				+ "` SET `player_uuid` = ?, `choosen_technology` = ?, `processed_in_repayment` = ?"
				+ " WHERE "+whereColumn;
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, getPlayerUUID().toString());
	        ps.setString(2, getChoosen_Technology());
	        ps.setBoolean(3, isProcessedInRepayment());
	        ps.setString(4, getGlobal_Choosen_Technology());
			int i = 5;
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
				al.add(new TechnologyPoll(rs.getInt("id"),
						UUID.fromString(rs.getString("player_uuid")),
						rs.getString("choosen_technology"),
						rs.getBoolean("processed_in_repayment"),
						rs.getString("global_choosen_technology")));
			}
			return al;
		} catch (SQLException e)
		{
			this.log(Level.WARNING, "SQLException! Could not get a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return new ArrayList<>();
	}
	
	public static ArrayList<TechnologyPoll> convert(ArrayList<Object> arrayList)
	{
		ArrayList<TechnologyPoll> l = new ArrayList<>();
		for(Object o : arrayList)
		{
			if(o instanceof TechnologyPoll)
			{
				l.add((TechnologyPoll) o);
			}
		}
		return l;
	}
}