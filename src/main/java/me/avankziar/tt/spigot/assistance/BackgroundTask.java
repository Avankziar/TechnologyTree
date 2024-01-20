package main.java.me.avankziar.tt.spigot.assistance;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.ifh.general.assistance.ChatApi;
import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.database.MysqlHandler;
import main.java.me.avankziar.tt.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.tt.spigot.database.SQLiteHandler;
import main.java.me.avankziar.tt.spigot.handler.CatTechHandler;
import main.java.me.avankziar.tt.spigot.handler.EntryQueryStatusHandler;
import main.java.me.avankziar.tt.spigot.handler.PlayerHandler;
import main.java.me.avankziar.tt.spigot.objects.EntryQueryType;
import main.java.me.avankziar.tt.spigot.objects.PlayerAssociatedType;
import main.java.me.avankziar.tt.spigot.objects.mysql.GlobalEntryQueryStatus;
import main.java.me.avankziar.tt.spigot.objects.mysql.GlobalTechnologyPoll;
import main.java.me.avankziar.tt.spigot.objects.mysql.UpdateTech;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.Technology;

public class BackgroundTask
{
	private static TT plugin;
	
	public BackgroundTask(TT plugin)
	{
		BackgroundTask.plugin = plugin;
		initBackgroundTask(); 
	}
	
	public boolean initBackgroundTask()
	{
		doDeleteExpiringTechnologies();
		doTechnologyPoll();
		doDeleteExpirePlacedBlocks();
		doUpdatePlayer();
		//ADDME DailyUpkeep for Groups
		return true;
	}
	
	private void doDeleteExpiringTechnologies()
	{
		new BukkitRunnable()
		{
			
			@Override
			public void run()
			{
				long now = System.currentTimeMillis();
				
				plugin.getMysqlHandler().deleteData(Type.SOLO_ENTRYQUERYSTATUS,
						"`duration_until_expiration` < ?",
						now);
				
				ArrayList<GlobalEntryQueryStatus> geqsa = GlobalEntryQueryStatus.convert(
						plugin.getMysqlHandler().getFullList(Type.GLOBAL_ENTRYQUERYSTATUS,
								"`id` ASC",
								"`duration_until_expiration` < ?",
								now));
				for(GlobalEntryQueryStatus geqs : geqsa)
				{
					plugin.getMysqlHandler().deleteData(Type.GLOBAL_TECHNOLOGYPOLL, "`global_choosen_technology_id` = ?", geqs.getId());
					plugin.getMysqlHandler().deleteData(Type.GLOBAL_ENTRYQUERYSTATUS, "`id` = ?", geqs.getId());
				}
			}
		}.runTaskTimerAsynchronously(plugin, 60*20L,
				plugin.getYamlHandler().getConfig().getLong("Do.DeleteExpireTechnologies.TaskRunInMinutes", 5)*60*20L);
	}
	
	private void doTechnologyPoll()
	{
		if(!plugin.getYamlHandler().getConfig().getBoolean("Do.TechnologyPoll.ProcessPollOnMainServer"))
		{
			return;
		}
		List<String> timesList = plugin.getYamlHandler().getConfig().getStringList("Do.TechnologyPoll.DaysOfTheMonth_ToProcessThePoll");
		new BukkitRunnable()
		{
			boolean inProcess = false;
			@Override
			public void run()
			{
				if(inProcess)
				{
					return;
				}
				LocalDateTime ldt = LocalDateTime.now();
				for(String l : timesList)
				{
					String[] split = l.split(":");
					if(split.length != 3 && split.length != 4)
					{
						continue;
					}
					String[] split2 = split[1].split("-");
					if(split2.length != 2)
					{
						continue;
					}
					LocalTime lt = LocalTime.of(Integer.valueOf(split2[0]), Integer.valueOf(split2[1]));
					if(ldt.getHour() != lt.getHour() && ldt.getMinute() != lt.getMinute())
					{
						continue;
					}
					if(split.length == 3)
					{
						int dayOfMonth = Integer.valueOf(split[2]);
						if(ldt.getDayOfMonth() != dayOfMonth)
						{
							continue;
						}
					} else if(split.length == 4)
					{
						DayOfWeek dayOfWeek = DayOfWeek.valueOf(split[2]);
						if(dayOfWeek != ldt.getDayOfWeek())
						{
							continue;
						}
						LocalDate than = getNthOfMonth(getDayOfWeekAndMonth(split[3]), dayOfWeek, ldt.getMonthValue(), ldt.getYear());
						if(ldt.getDayOfMonth() != than.getDayOfMonth())
						{
							continue;
						}
					}
					PlayerAssociatedType pat = PlayerAssociatedType.valueOf(split[0]);
					if(pat == PlayerAssociatedType.GLOBAL)
					{
						inProcess = false;
						processPoll(pat);
						inProcess = true;
					}
					return;
				}
			}
		}.runTaskTimerAsynchronously(plugin, 20L*20, 30L*20);
	}
	
	private static void processPoll(PlayerAssociatedType pat)
	{
		//Cancel if no one has voted for a tech
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.GLOBAL_TECHNOLOGYPOLL, "`processed_in_poll` = ?", false))
		{
			ArrayList<String> l = new ArrayList<>();
			l.add(plugin.getYamlHandler().getLang().getString("BackgroundTask.PollEvaluation.Headline"));
			l.add(plugin.getYamlHandler().getLang().getString("BackgroundTask.PollEvaluation.NoVoteExist"));
			l.add(plugin.getYamlHandler().getLang().getString("BackgroundTask.PollEvaluation.Bottomline"));
			if(plugin.getMessageToBungee() != null)
			{
				plugin.getMessageToBungee().sendMessage(l.toArray(new String[l.size()]));
			} else
			{
				for(Player player : Bukkit.getOnlinePlayers())
				{
					for(String s : l)
					{
						player.sendMessage(ChatApi.tl(s));
					}
				}
			}
			return;
		}
		//Call all, which are NOT processed in poll
		ArrayList<GlobalTechnologyPoll> tpar = GlobalTechnologyPoll.convert(
				plugin.getMysqlHandler().getFullList(MysqlHandler.Type.GLOBAL_TECHNOLOGYPOLL,
				"`id`", "`processed_in_poll = ?`", false));
		LinkedHashMap<String, Integer> countMap = new LinkedHashMap<>();
		int participants = tpar.size();
		for(GlobalTechnologyPoll tp : tpar)
		{
			int c = 0;
			if(countMap.containsKey(tp.getChoosen_Technology()))
			{
				c = countMap.get(tp.getChoosen_Technology());
			}
			countMap.put(tp.getChoosen_Technology(), c+1);
		}
		if(countMap.isEmpty())
		{
			return;
		}
		int max = 0;
		String globalChoosenTech = "";
		for(Entry<String, Integer> e : countMap.entrySet())
		{
			if(max < e.getValue())
			{
				max = e.getValue();
				globalChoosenTech = e.getKey();
			}
		}
		Technology globalChoosen = CatTechHandler.getTechnology(globalChoosenTech, PlayerAssociatedType.GLOBAL);
		int globalResearchlevel = PlayerHandler.researchGlobalTechnology(globalChoosen, true);
		GlobalEntryQueryStatus geqs = EntryQueryStatusHandler.getGlobalEntryHighestResearchLevel(globalChoosen, EntryQueryType.TECHNOLOGY);
		double share = 1/participants;
		for(GlobalTechnologyPoll gtp : tpar)
		{
			Player pcp = Bukkit.getPlayer(gtp.getPlayerUUID());
			gtp.setChoosen_Technology_Researchlevel(globalResearchlevel);
			gtp.setGlobal_Choosen_Technology(globalChoosenTech);
			gtp.setGlobal_Choosen_Technology_ID(geqs.getId());
			gtp.setProcessedInPoll(true);
			gtp.setTotal_Participants(participants);
			if(pcp == null)
			{
				plugin.getMysqlHandler().updateData(Type.GLOBAL_TECHNOLOGYPOLL, gtp, "`id` = ?", gtp.getId());
				continue;
			}
			gtp.setProcessedInRepayment(true);
			plugin.getMysqlHandler().updateData(Type.GLOBAL_TECHNOLOGYPOLL, gtp, "`id` = ?", gtp.getId());
			Technology playerChoosen = CatTechHandler.getTechnology(globalChoosenTech, PlayerAssociatedType.GLOBAL);
			PlayerHandler.repaymentGlobalTechnology(pcp, playerChoosen, globalResearchlevel);
			PlayerHandler.payTechnology(pcp, globalChoosen, share);
		}
		if(plugin.getBungeeOnlinePlayers() != null)
		{
			for(UUID uuid : plugin.getBungeeOnlinePlayers().getBungeeOnlinePlayers().keySet())
			{
				Player player = Bukkit.getPlayer(uuid);
				if(player == null)
				{
					UpdateTech ut = new UpdateTech(0, uuid, PlayerAssociatedType.GLOBAL, globalChoosenTech, geqs.getId(), globalResearchlevel);
					plugin.getMysqlHandler().create(Type.UPDATE_TECH, ut);
					continue;
				}
				PlayerHandler.doUpdate(player, globalChoosen, geqs.getId(), globalResearchlevel);
			}
		} else
		{
			for(Player player : Bukkit.getOnlinePlayers())
			{
				PlayerHandler.doUpdate(player, globalChoosen, geqs.getId(), globalResearchlevel);
			}
		}
	}
	
	private static void doUpdatePlayer()
	{
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				if(!plugin.getMysqlHandler().exist(Type.UPDATE_TECH, "`id` > ?", 0))
				{
					return;
				}
				ArrayList<UpdateTech> l = UpdateTech.convert(plugin.getMysqlHandler().getFullList(Type.UPDATE_TECH, "`id` ASC", "`id` > ?", 0));
				for(UpdateTech u : l)
				{
					Player player = Bukkit.getPlayer(u.getUUID());
					if(player == null)
					{
						continue;
					}
					Technology t = CatTechHandler.getTechnology(u.getTechnology(), u.getPlayerAssociatedType());
					PlayerHandler.doUpdate(player, t, u.getGlobalTechnologyPollID(), u.getResearchLevel());
					plugin.getMysqlHandler().deleteData(Type.UPDATE_TECH, "`id` = ?", u.getId());
				}
			}
		}.runTaskTimerAsynchronously(plugin, 0, 20L*15);
	}
	
	private static int getDayOfWeekAndMonth(String text)
	{
		switch(text)
		{
		default: return 1;
		case "FIRST": return 1;
		case "SECOND": return 2;
		case "THIRD": return 3;
		case "FOURTH": return 4;
		case "FIFTH": return 5;
		}
	}
	
	private static LocalDate getNthOfMonth(int type, DayOfWeek dayOfWeek, int month, int year)
	{
	    return LocalDate.now().withMonth(month).withYear(year).with(TemporalAdjusters.dayOfWeekInMonth(type, dayOfWeek));
	}
	
	private void doDeleteExpirePlacedBlocks()
	{
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				plugin.getSQLLiteHandler().deleteData(SQLiteHandler.Type.PLACEDBLOCKS, "`expiration_date` > ?", System.currentTimeMillis());
			}
		}.runTaskTimerAsynchronously(plugin, 20L, 
				plugin.getYamlHandler().getConfig().getLong("Do.DeleteExpirePlacedBlocks.TaskRunInMinutes", 5)*60*20L);
	}
}