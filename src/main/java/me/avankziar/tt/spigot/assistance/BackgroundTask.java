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

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.database.MysqlHandler;
import main.java.me.avankziar.tt.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.tt.spigot.handler.CatTechHandler;
import main.java.me.avankziar.tt.spigot.handler.PlayerHandler;
import main.java.me.avankziar.tt.spigot.objects.PlayerAssociatedType;
import main.java.me.avankziar.tt.spigot.objects.mysql.TechnologyPoll;
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
		return true;
	}
	
	private void doDeleteExpiringTechnologies()
	{
		//TODO
	}
	
	private void doTechnologyPoll()
	{
		if(plugin.getYamlHandler().getConfig().getBoolean("Do.TechnologyPoll.ProcessPollOnMainServer"))
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
					if(pat == PlayerAssociatedType.SOLO)
					{
						continue;
					}
					inProcess = false;
					processPoll(pat);
					inProcess = true;
					return;
				}
			}
		}.runTaskTimerAsynchronously(plugin, 20L*20, 30L*20);
	}
	
	private static void processPoll(PlayerAssociatedType pat)
	{
		ArrayList<TechnologyPoll> tpar = TechnologyPoll.convert(plugin.getMysqlHandler().getFullList(MysqlHandler.Type.TECHNOLOGYPOLL,
				"`id`", "`processed_in_repayment = ?`", false));
		LinkedHashMap<String, Integer> countMap = new LinkedHashMap<>();
		int participants = tpar.size();
		for(TechnologyPoll tp : tpar)
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
			plugin.getMysqlHandler().truncate(Type.TECHNOLOGYPOLL);
			return;
		}
		int max = 0;
		String choosenTech = "";
		for(Entry<String, Integer> e : countMap.entrySet())
		{
			if(max < e.getValue())
			{
				max = e.getValue();
				choosenTech = e.getKey();
			}
		}
		Technology globalChoosen = CatTechHandler.getTechnology(choosenTech, PlayerAssociatedType.GLOBAL);
		int researchlevel = PlayerHandler.researchGlobalTechnology(globalChoosen, true);
		int share = 1/participants;
		for(TechnologyPoll tp : tpar)
		{
			Player pcp = Bukkit.getPlayer(tp.getPlayerUUID());
			if(pcp == null)
			{
				tp.setProcessedInRepayment(true);
				tp.setGlobal_Choosen_Technology(choosenTech);
				plugin.getMysqlHandler().updateData(Type.TECHNOLOGYPOLL, tp,
						"`player_uuid` = ? AND `choosen_technology` = ? AND `global_choosen_technology` = ?",
						tp.getPlayerUUID().toString(), tp.getChoosen_Technology(), "/");
				continue;
			}
			Technology playerChoosen = CatTechHandler.getTechnology(choosenTech, PlayerAssociatedType.GLOBAL);
			PlayerHandler.repaymentGlobalTechnology(pcp, playerChoosen, researchlevel);
			PlayerHandler.payTechnology(pcp, globalChoosen, share);
		}
		//For Schleife für jeden Spieler, welcher online ist, repayment der techkosten anteilig für alle Wähler
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
}
