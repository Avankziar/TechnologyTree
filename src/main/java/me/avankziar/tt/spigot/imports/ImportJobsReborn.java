package main.java.me.avankziar.tt.spigot.imports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.entity.Player;

import main.java.me.avankziar.ifh.general.assistance.ChatApi;
import main.java.me.avankziar.ifh.general.math.MathFormulaParser;
import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.handler.ConfigHandler;
import main.java.me.avankziar.tt.spigot.handler.PlayerHandler;
import main.java.me.avankziar.tt.spigot.objects.mysql.PlayerData;

public class ImportJobsReborn
{
	private static TT plugin = TT.getPlugin();
	
	public static void importJobsReborn(Player player, PlayerData pd, String formula)
	{
		int juserid = getJobsRebornUserID(player.getUniqueId());
		if(juserid == 0)
		{
			return;
		}
		LinkedHashMap<String, LinkedHashMap<Integer, Integer>> map = getJobsRebornData(juserid);
		if(map == null)
		{
			return;
		}
		double ttexp = convertJobInExp(map, formula);
		pd.setActualTTExp(pd.getActualTTExp() + ttexp);
		pd.setTotalReceivedTTExp(pd.getTotalReceivedTTExp() + ttexp);
		PlayerHandler.updatePlayer(pd);
		deleteJobsRebornUserID(player.getUniqueId());
		deleteJobsRebornData(juserid);
		player.sendMessage(ChatApi.tl(TT.getPlugin().getYamlHandler().getLang().getString("PlayerHandler.ImportFromJobsReborn")
				.replace("%ttexp%", String.valueOf(ttexp))));
	}
	
	private static int getJobsRebornUserID(UUID uuid)
	{
		int juserid = 0;
		PreparedStatement ps = null;
		ResultSet result = null;
		Connection conn = null;
		try
		{
			conn = plugin.getMysqlSetup().getConnection();
		} catch (SQLException e)
		{
			return 0;
		}
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `jobs_users` WHERE `player_uuid` = ?";
		        ps = conn.prepareStatement(sql);
		        ps.setString(1, uuid.toString());
		        result = ps.executeQuery();
		        while (result.next()) 
		        {
		        	juserid = result.getInt("id");
		        }
		        return juserid;
		    } catch (SQLException e) 
			{
				  TT.log.warning("Error: " + e.getMessage());
				  e.printStackTrace();
		    } finally 
			{
		    	try 
		    	{
		    		if (result != null) 
		    		{
		    			result.close();
		    		}
		    		if (ps != null) 
		    		{
		    			ps.close();
		    		}
		    	} catch (Exception e) {
		    		e.printStackTrace();
		    	}
		    }
		}
		return juserid;
	}
	
	private static void deleteJobsRebornUserID(UUID uuid)
	{
		PreparedStatement ps = null;
		ResultSet result = null;
		Connection conn = null;
		try
		{
			conn = plugin.getMysqlSetup().getConnection();
		} catch (SQLException e)
		{
			return;
		}
		if (conn != null) 
		{
			try 
			{			
				String sql = "Delete FROM `jobs_users` WHERE `player_uuid` = ?";
		        ps = conn.prepareStatement(sql);
		        ps.setString(1, uuid.toString());
		        ps.executeUpdate();
		    } catch (SQLException e) 
			{
				  TT.log.warning("Error: " + e.getMessage());
				  e.printStackTrace();
		    } finally 
			{
		    	try 
		    	{
		    		if (result != null) 
		    		{
		    			result.close();
		    		}
		    		if (ps != null) 
		    		{
		    			ps.close();
		    		}
		    	} catch (Exception e) {
		    		e.printStackTrace();
		    	}
		    }
		}
		return;
	}
	
	private static LinkedHashMap<String, LinkedHashMap<Integer, Integer>> getJobsRebornData(int userid)
	{
		LinkedHashMap<String, LinkedHashMap<Integer, Integer>> map = new LinkedHashMap<>();
		PreparedStatement ps = null;
		ResultSet result = null;
		Connection conn = null;
		try
		{
			conn = plugin.getMysqlSetup().getConnection();
		} catch (SQLException e)
		{
			return null;
		}
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `jobs_jobs` WHERE `userid` = ?";
		        ps = conn.prepareStatement(sql);
		        ps.setInt(1, userid);
		        result = ps.executeQuery();
		        while (result.next()) 
		        {
		        	String job = result.getString("job");
		        	int level = result.getInt("level");
		        	int expprogression = result.getInt("experience");
		        	LinkedHashMap<Integer, Integer> m = new LinkedHashMap<>();
		        	m.put(level, expprogression);
		        	map.put(job, m);
		        }
		    } catch (SQLException e) 
			{
				  TT.log.warning("Error: " + e.getMessage());
				  e.printStackTrace();
		    } finally 
			{
		    	try 
		    	{
		    		if (result != null) 
		    		{
		    			result.close();
		    		}
		    		if (ps != null) 
		    		{
		    			ps.close();
		    		}
		    	} catch (Exception e) {
		    		e.printStackTrace();
		    	}
		    }
		}
		return map;
	}
	
	private static void deleteJobsRebornData(int userid)
	{
		PreparedStatement ps = null;
		ResultSet result = null;
		Connection conn = null;
		try
		{
			conn = plugin.getMysqlSetup().getConnection();
		} catch (SQLException e)
		{
			return;
		}
		if (conn != null) 
		{
			try 
			{			
				String sql = "Delete FROM `jobs_jobs` WHERE `userid` = ?";
		        ps = conn.prepareStatement(sql);
		        ps.setInt(1, userid);
		        ps.executeUpdate();
		    } catch (SQLException e) 
			{
				  TT.log.warning("Error: " + e.getMessage());
				  e.printStackTrace();
		    } finally 
			{
		    	try 
		    	{
		    		if (result != null) 
		    		{
		    			result.close();
		    		}
		    		if (ps != null) 
		    		{
		    			ps.close();
		    		}
		    	} catch (Exception e) {
		    		e.printStackTrace();
		    	}
		    }
		}
		return;
	}
	
	private static double convertJobInExp(LinkedHashMap<String, LinkedHashMap<Integer, Integer>> m, String formula)
	{
		double ttexp = 0;
		int numjobs = m.size();
		int maxjobs = new ConfigHandler().jobsRebornImportMaxJobsPerPlayer();
		for(LinkedHashMap<Integer, Integer> v : m.values())
		{
			for(Entry<Integer, Integer> e : v.entrySet())
			{
				int level = e.getKey();
				int exp = e.getValue();
				for(int i = 1; i <= level; i++)
				{
					HashMap<String, Double> map = new HashMap<>();
					map.put("numjobs" , Integer.valueOf(numjobs).doubleValue());
					map.put("maxjobs" , Integer.valueOf(maxjobs).doubleValue());
					map.put("joblevel" , Integer.valueOf(level).doubleValue());
					ttexp = ttexp + new MathFormulaParser().parse(formula, map);
				}
				ttexp = ttexp + Integer.valueOf(exp).doubleValue();
			}
		}
		return ttexp;
	}
}