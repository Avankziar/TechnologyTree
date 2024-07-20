package main.java.me.avankziar.tt.spigot.assistance;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.tt.general.ChatApi;
import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.database.MysqlHandler;
import main.java.me.avankziar.tt.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.tt.spigot.database.SQLiteHandler;
import main.java.me.avankziar.tt.spigot.handler.CatTechHandler;
import main.java.me.avankziar.tt.spigot.handler.EntryQueryStatusHandler;
import main.java.me.avankziar.tt.spigot.handler.GroupHandler;
import main.java.me.avankziar.tt.spigot.handler.PlayerHandler;
import main.java.me.avankziar.tt.spigot.handler.SwitchModeHandler;
import main.java.me.avankziar.tt.spigot.objects.EntryQueryType;
import main.java.me.avankziar.tt.spigot.objects.EntryStatusType;
import main.java.me.avankziar.tt.spigot.objects.PlayerAssociatedType;
import main.java.me.avankziar.tt.spigot.objects.mysql.ExternBooster;
import main.java.me.avankziar.tt.spigot.objects.mysql.GlobalEntryQueryStatus;
import main.java.me.avankziar.tt.spigot.objects.mysql.GlobalTechnologyPoll;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupData;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupEntryQueryStatus;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupPlayerAffiliation;
import main.java.me.avankziar.tt.spigot.objects.mysql.PlayerData;
import main.java.me.avankziar.tt.spigot.objects.mysql.SoloEntryQueryStatus;
import main.java.me.avankziar.tt.spigot.objects.mysql.UpdateTech;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.SwitchMode;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.Technology;

public class BackgroundTask
{
	private static TT plugin;
	public static boolean globalPollInProcess = false;
	
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
		doGroupDailyUpkeep();
		doCheckSwitchMode();
		doDeleteExpiringExternBooster();
		return true;
	}
	
	private void doDeleteExpiringTechnologies()
	{
		if(!plugin.getYamlHandler().getConfig().getBoolean("Do.DeleteExpireTechnologies.isMainServer", false))
		{
			return;
		}
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				long now = System.currentTimeMillis();
				ArrayList<GlobalEntryQueryStatus> geqsa = GlobalEntryQueryStatus.convert(
						plugin.getMysqlHandler().getFullList(Type.GLOBAL_ENTRYQUERYSTATUS,
								"`id` ASC",	"`duration_until_expiration` < ?", now));
				for(GlobalEntryQueryStatus geqs : geqsa)
				{
					plugin.getMysqlHandler().deleteData(Type.GLOBAL_TECHNOLOGYPOLL, "`global_choosen_technology_id` = ?", geqs.getId());
				}
				plugin.getMysqlHandler().deleteData(Type.GLOBAL_ENTRYQUERYSTATUS,
						"`duration_until_expiration` < ?", now);
				
				ArrayList<UUID> toUpdate = new ArrayList<>();
				for(SoloEntryQueryStatus e : SoloEntryQueryStatus.convert(plugin.getMysqlHandler().getFullList(Type.SOLO_ENTRYQUERYSTATUS,
						"`id` ASC", "`duration_until_expiration` < ?", now)))
				{
					if(!toUpdate.contains(e.getPlayerUUID()))
					{
						toUpdate.add(e.getPlayerUUID());
					}
				}
				plugin.getMysqlHandler().deleteData(Type.SOLO_ENTRYQUERYSTATUS,
						"`duration_until_expiration` < ?", now);
				for(GroupEntryQueryStatus e : GroupEntryQueryStatus.convert(plugin.getMysqlHandler().getFullList(Type.GROUP_ENTRYQUERYSTATUS,
						"`id` ASC", "`duration_until_expiration` < ?", now)))
				{
					for(GroupPlayerAffiliation g : GroupHandler.getAllAffiliateGroup(e.getGroupName()))
					{
						if(!toUpdate.contains(g.getPlayerUUID()))
						{
							toUpdate.add(g.getPlayerUUID());
						}
					}
				}
				plugin.getMysqlHandler().deleteData(Type.GROUP_ENTRYQUERYSTATUS,
						"`duration_until_expiration` < ?", now);
				if(geqsa.size() > 0)
				{
					if(plugin.getProxyOnlinePlayers() != null)
					{
						toUpdate(new ArrayList<UUID>(plugin.getProxyOnlinePlayers().getProxyOnlinePlayers().keySet()));
					} else
					{
						for(Player player : Bukkit.getOnlinePlayers())
						{
							PlayerHandler.quitPlayer(player.getUniqueId());
							PlayerHandler.joinPlayer(player);
						}
					}
				} else if(toUpdate.size() > 0)
				{
					toUpdate(toUpdate);
				}
			}
		}.runTaskTimerAsynchronously(plugin, 60*20L,
				plugin.getYamlHandler().getConfig().getLong("Do.DeleteExpireTechnologies.TaskRunInMinutes", 5)*60*20L);
	}
	
	private void toUpdate(ArrayList<UUID> uuids)
	{
		Utility.toUpdate(uuids, null);
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
			long lastTime = System.currentTimeMillis();
			@Override
			public void run()
			{
				long time = System.currentTimeMillis()-lastTime;
				if(globalPollInProcess || time < 1000*90)
				{
					return;
				}
				LocalDateTime ldt = LocalDateTime.now();
				for(String l : timesList)
				{
					String[] split = l.split(":");
					if(split.length != 2 && split.length != 3)
					{
						continue;
					}
					String[] split2 = split[0].split("-");
					if(split2.length != 2)
					{
						continue;
					}
					LocalTime lt = LocalTime.of(Integer.valueOf(split2[0]), Integer.valueOf(split2[1]));
					if(ldt.getHour() != lt.getHour() || ldt.getMinute() != lt.getMinute())
					{
						continue;
					}
					if(split.length == 2)
					{
						if(!MatchApi.isInteger(split[1]))
						{
							TT.log.warning("In the config.yml "+split[1]+" is NO number to match the the nth day of the month!");
							continue;
						}
						int dayOfMonth = Integer.valueOf(split[1]);
						if(ldt.getDayOfMonth() != dayOfMonth)
						{
							continue;
						}
					} else if(split.length == 3)
					{
						DayOfWeek dayOfWeek = DayOfWeek.valueOf(split[1]);
						if(dayOfWeek != ldt.getDayOfWeek())
						{
							continue;
						}
						LocalDate than = getNthOfMonth(getDayOfWeekAndMonth(split[2]), dayOfWeek, ldt.getMonthValue(), ldt.getYear());
						if(ldt.getDayOfMonth() != than.getDayOfMonth())
						{
							continue;
						}
					}
					globalPollInProcess = true;
					new BukkitRunnable()
					{
						@Override
						public void run()
						{
							lastTime = System.currentTimeMillis();
							processPoll();
							globalPollInProcess = false;
						}
					}.runTaskAsynchronously(plugin);
					return;
				}
			}
		}.runTaskTimer(plugin, 20L*5, 20L*15);
	}
	
	private static void processPoll()
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
				"`id` ASC", "`processed_in_poll` = ?", false));
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
		int globalResearchlevel = PlayerHandler.researchGlobalTechnology(globalChoosen);
		GlobalEntryQueryStatus geqs = EntryQueryStatusHandler.getGlobalEntryHighestResearchLevel(globalChoosen,
				EntryQueryType.TECHNOLOGY, EntryStatusType.HAVE_RESEARCHED_IT);
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
		if(plugin.getProxyOnlinePlayers() != null)
		{
			for(UUID uuid : plugin.getProxyOnlinePlayers().getProxyOnlinePlayers().keySet())
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
		ArrayList<String> l = new ArrayList<>();
		l.add(plugin.getYamlHandler().getLang().getString("BackgroundTask.PollEvaluation.Headline"));
		l.add(plugin.getYamlHandler().getLang().getString("BackgroundTask.PollEvaluation.Vote")
				.replace("%pcp%", String.valueOf(participants))
				.replace("%tech%", globalChoosen.getDisplayName())
				.replace("%lv%", String.valueOf(globalResearchlevel))
				);
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
	}
	
	private void doUpdatePlayer()
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
					if(u.getTechnology() == null)
					{
						PlayerHandler.quitPlayer(u.getUUID());
						PlayerHandler.joinPlayer(player);
						plugin.getMysqlHandler().deleteData(Type.UPDATE_TECH, "`id` = ?", u.getId());
						continue;
					} else if(u.getTechnology().startsWith("!~booster~!"))
					{
						String bo = u.getTechnology().replace("!~booster~!", "");
						String[] split = bo.split("!-!");
						if(split.length != 2)
						{
							plugin.getMysqlHandler().deleteData(Type.UPDATE_TECH, "`id` = ?", u.getId());
							continue;
						}
						String id = split[0];
						String name = split[1];
						ExternBooster exB = (ExternBooster) plugin.getMysqlHandler().getData(Type.EXTERN_BOOSTER, "`id` ASC",
								"`id` = ? AND `booster_name` = ?", id, name);
						if(exB == null)
						{
							Utility.toUpdate(new ArrayList<UUID>((ArrayList<UUID>) Arrays.asList(player.getUniqueId())), null);
							plugin.getMysqlHandler().deleteData(Type.UPDATE_TECH, "`id` = ?", u.getId());
							continue;
						} else
						{
							PlayerHandler.addExternBooster(player.getUniqueId(), exB);
							plugin.getMysqlHandler().deleteData(Type.UPDATE_TECH, "`id` = ?", u.getId());
							continue;
						}
					} else
					{
						Technology t = CatTechHandler.getTechnology(u.getTechnology(), u.getPlayerAssociatedType());
						PlayerHandler.doUpdate(player, t, u.getGlobalTechnologyPollID(), u.getResearchLevel());
						plugin.getMysqlHandler().deleteData(Type.UPDATE_TECH, "`id` = ?", u.getId());
					}
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
	
	private void doGroupDailyUpkeep()
	{
		if(!plugin.getYamlHandler().getConfig().getBoolean("Group.DailyUpkeep.Active", true))
		{
			return;
		}
		String time = plugin.getYamlHandler().getConfig().getString("Group.DailyUpkeep.Time", "11-00");
		String[] sp = time.split("-");
		if(sp.length != 2 || !MatchApi.isInteger(sp[0]) || !MatchApi.isInteger(sp[1])
				|| !MatchApi.isPositivNumber(Integer.valueOf(sp[0])) || !MatchApi.isPositivNumber(Integer.valueOf(sp[1])))
		{
			return;
		}
		new BukkitRunnable()
		{
			long lastTime = System.currentTimeMillis();
			LocalTime lt = LocalTime.of(Integer.valueOf(sp[0]), Integer.valueOf(sp[1]));
			boolean deleteIfNoMember = plugin.getYamlHandler().getConfig().getBoolean("Do.Group.IfGroupHasNoMemberDeleteIt", true);
			@Override
			public void run()
			{
				if(System.currentTimeMillis() - lastTime < 1000*60*2)
				{
					return;
				}
				LocalTime now = LocalTime.now();
				if(lt.getHour() != now.getHour() || lt.getMinute() != now.getMinute())
				{
					return;
				}
				ArrayList<GroupData> agd = GroupData.convert(plugin.getMysqlHandler().getFullList(Type.GROUP_DATA, "`id` ASC", "`id` > ?", 0));
				if(agd == null || agd.isEmpty())
				{
					return;
				}
				lastTime = System.currentTimeMillis();
				ArrayList<GroupData> toDeleteGroup = new ArrayList<>();
				for(int i = 0; i < agd.size(); i++)
				{
					GroupData gd = agd.get(i);
					ArrayList<GroupPlayerAffiliation> gpa = GroupPlayerAffiliation.convert(plugin.getMysqlHandler().getFullList(
							Type.GROUP_PLAYERAFFILIATION, "`id` ASC", "`group_name` = ? AND `rank_ordinal` < ?", gd.getGroupName(), 5));
					if(gpa == null || gpa.isEmpty())
					{
						if(deleteIfNoMember)
						{
							toDeleteGroup.add(gd);
							continue;
						}
					}
					double collectedUpkeep = 0;
					int failedUpkeep = 0;
					for(GroupPlayerAffiliation gp : gpa)
					{
						PlayerData pd = PlayerHandler.getPlayer(gp.getPlayerUUID());
						if(pd == null)
						{
							continue;
						}
						if(gp.getIndividualTechExpDailyUpkeep() > 0)
						{
							if(pd.getActualTTExp() < gp.getIndividualTechExpDailyUpkeep())
							{
								double lastTTExp = pd.getActualTTExp();
								collectedUpkeep += pd.getActualTTExp();
								pd.setActualTTExp(0);
								failedUpkeep++;
								GroupHandler.sendMemberText(gp.getPlayerUUID(),
										ChatApi.tl(plugin.getYamlHandler().getLang().getString("BackgroundTask.GroupDailyUpkeep.FailedCollectdUpkeep")
												.replace("%group%", gd.getGroupName())
												.replace("%exp%", String.valueOf(lastTTExp))
												.replace("%failedexp%", String.valueOf(gp.getIndividualTechExpDailyUpkeep()))));
							} else
							{
								collectedUpkeep += gp.getIndividualTechExpDailyUpkeep();
								pd.setActualTTExp(pd.getActualTTExp()-gp.getIndividualTechExpDailyUpkeep());
								GroupHandler.sendMemberText(gp.getPlayerUUID(),
										ChatApi.tl(plugin.getYamlHandler().getLang().getString("BackgroundTask.GroupDailyUpkeep.CollectedUpkeep")
												.replace("%group%", gd.getGroupName())
												.replace("%exp%", String.valueOf(gp.getIndividualTechExpDailyUpkeep()))));
							}
							PlayerHandler.updatePlayer(pd);
						}
					}
					int lvl = plugin.getYamlHandler().getConfig().getInt("Group.DailyUpkeep.ActiveFromLevel", 2);
					if(gd.getGroupLevel() < lvl)
					{
						gd.setGroupTechExp(gd.getGroupTechExp()+collectedUpkeep);
						GroupHandler.updateGroup(gd);
						GroupHandler.sendMembersText(gd.getGroupName(),
								ChatApi.tl(plugin.getYamlHandler().getLang().getString("BackgroundTask.GroupDailyUpkeep.NoUpkeep")
										.replace("%group%", gd.getGroupName())
										.replace("%lvl%", String.valueOf(lvl))));
						continue;
					}
					double upkeep = GroupHandler.calculateGroupDailyUpKeep(gd.getGroupName(), gd.getGroupLevel(), gpa.size());
					double c = collectedUpkeep;
					if(collectedUpkeep > upkeep)
					{
						collectedUpkeep -= upkeep;
						gd.setGroupTechExp(gd.getGroupTechExp()+collectedUpkeep);
						GroupHandler.sendMembersText(gd.getGroupName(), 
								ChatApi.tl(plugin.getYamlHandler().getLang().getString("BackgroundTask.GroupDailyUpkeep.AddedCollectedUpkeep")
										.replace("%group%", gd.getGroupName())
										.replace("%exp%", String.valueOf(c))
										.replace("%upkeep%", String.valueOf(upkeep))
										.replace("%failed%", String.valueOf(failedUpkeep))));
						if(gd.getGroupCounterFailedUpkeep() > 0)
						{
							gd.setGroupCounterFailedUpkeep(gd.getGroupCounterFailedUpkeep()-1);
							GroupHandler.sendMembersText(gd.getGroupName(), 
									ChatApi.tl(plugin.getYamlHandler().getLang().getString("BackgroundTask.GroupDailyUpkeep.FailedCounterMinusOne")
											.replace("%group%", gd.getGroupName())
											.replace("%failed%", String.valueOf(gd.getGroupCounterFailedUpkeep()))));
						}
					} else
					{
						upkeep -= collectedUpkeep;
						int maxfailed = plugin.getYamlHandler().getConfig().getInt("Group.DailyUpkeep.CounterFailedUpkeepToReduceGroupLevel", 7);
						if(gd.getGroupTechExp() > upkeep)
						{
							gd.setGroupTechExp(gd.getGroupTechExp()-upkeep);
							GroupHandler.sendMembersText(gd.getGroupName(), 
									ChatApi.tl(plugin.getYamlHandler().getLang().getString("BackgroundTask.GroupDailyUpkeep.AddedCollectedUpkeepButHaveEnoughAlready")
											.replace("%group%", gd.getGroupName())
											.replace("%exp%", String.valueOf(c))
											.replace("%upkeep%", String.valueOf(upkeep))
											.replace("%failed%", String.valueOf(failedUpkeep))));
							if(gd.getGroupCounterFailedUpkeep() > 0)
							{
								gd.setGroupCounterFailedUpkeep(gd.getGroupCounterFailedUpkeep()-1);
								GroupHandler.sendMembersText(gd.getGroupName(), 
										ChatApi.tl(plugin.getYamlHandler().getLang().getString("BackgroundTask.GroupDailyUpkeep.FailedCounterMinusOne")
												.replace("%group%", gd.getGroupName())
												.replace("%failed%", String.valueOf(gd.getGroupCounterFailedUpkeep()))));
							}
						} else
						{
							gd.setGroupTechExp(0);
							gd.setGroupCounterFailedUpkeep(gd.getGroupCounterFailedUpkeep()+1);
							GroupHandler.sendMembersText(gd.getGroupName(), 
									ChatApi.tl(plugin.getYamlHandler().getLang().getString("BackgroundTask.GroupDailyUpkeep.AddedCollectedUpkeepButHaveNotEnoughAlready")
											.replace("%group%", gd.getGroupName())
											.replace("%exp%", String.valueOf(c))
											.replace("%upkeep%", String.valueOf(upkeep))
											.replace("%failed%", String.valueOf(failedUpkeep))));
							GroupHandler.sendMembersText(gd.getGroupName(), 
									ChatApi.tl(plugin.getYamlHandler().getLang().getString("BackgroundTask.GroupDailyUpkeep.FailedCounterPlusOne")
											.replace("%group%", gd.getGroupName())
											.replace("%failed%", String.valueOf(gd.getGroupCounterFailedUpkeep()))
											.replace("%maxfailed%", String.valueOf(maxfailed))));
							if(gd.getGroupCounterFailedUpkeep() >= maxfailed)
							{
								gd.setGroupLevel(gd.getGroupLevel()-1);
								gd.setGroupCounterFailedUpkeep(0);
								GroupHandler.sendMembersText(gd.getGroupName(), 
										ChatApi.tl(plugin.getYamlHandler().getLang().getString("BackgroundTask.GroupDailyUpkeep.MaxFailedCounter")
												.replace("%group%", gd.getGroupName())
												.replace("%maxfailed%", String.valueOf(maxfailed))
												.replace("%prelvl%", String.valueOf(gd.getGroupLevel()+1))
												.replace("%postlvl%", String.valueOf(gd.getGroupLevel()))));
							}
						}
					}
					GroupHandler.updateGroup(gd);
				}
				if(toDeleteGroup.size() > 0)
				{
					int deletedGroups = toDeleteGroup.size();
					int deletedEntrys = 0;
					int deletedMembers = 0;
					for(int i = 0; i < toDeleteGroup.size(); i++)
					{
						GroupData gd = toDeleteGroup.get(i);
						plugin.getMysqlHandler().deleteData(Type.EXTERN_BOOSTER, "`group_name` = ?", gd.getGroupName());
						deletedMembers += plugin.getMysqlHandler().deleteData(Type.GROUP_PLAYERAFFILIATION, "`group_name` = ?", gd.getGroupName());
						deletedEntrys += plugin.getMysqlHandler().deleteData(Type.GROUP_ENTRYQUERYSTATUS, "`group_name` = ?", gd.getGroupName());
						plugin.getMysqlHandler().deleteData(Type.GROUP_DATA, "`group_name` = ?", gd.getGroupName());
					}
					TT.log.info("==========Deleted Groups==========");
					TT.log.info("Reason: Have No Members after checking the DailyUpkeep");
					TT.log.info("Deleted Groups: "+deletedGroups);
					TT.log.info("Deleted Entrys: "+deletedEntrys+" (Categorys and Techs)");
					TT.log.info("Deleted Members: "+deletedMembers+" (Applicants and Invitees)");
					TT.log.info("==================================");
				}
			}
		}.runTaskTimerAsynchronously(plugin, 0L, 20L*15);
	}
	
	private void doCheckSwitchMode()
	{
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				if(SwitchModeHandler.isActive)
				{
					for(Player player : Bukkit.getOnlinePlayers())
					{
						if(player == null)
						{
							continue;
						}
						PlayerData pd = PlayerHandler.getPlayer(player.getUniqueId());
						SwitchMode sw = SwitchModeHandler.getSwitchMode(pd.getSwitchMode());
						String oldsw = sw.name;
						if(sw.permission != null && !player.hasPermission(sw.permission))
						{
							sw = PlayerHandler.getSwitchMode(player);
							String newsw = sw.name;
							player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("PlayerHandler.SwitchMode.Switched")
									.replace("%old%", oldsw)
									.replace("%new%", newsw)));
						}
					}
				}				
			}
		}.runTaskTimerAsynchronously(plugin, 60*20L, plugin.getYamlHandler().getConfig().getInt("Do.SwitchMode.PermissionCheckUp", 5)*60*20L);
	}
	
	private void doDeleteExpiringExternBooster()
	{
		if(!plugin.getYamlHandler().getConfig().getBoolean("Do.DeleteExpireExternBooster.isMainServer", false))
		{
			return;
		}
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				long now = System.currentTimeMillis();
				ArrayList<ExternBooster> exb = ExternBooster.convert(plugin.getMysqlHandler().getFullList(Type.EXTERN_BOOSTER,
						"`id` ASC", "`expiry_date` < ?", now));
				if(exb.size() <= 0)
				{
					return;
				}
				boolean allUpdate = false;
				ArrayList<UUID> playerUUIDToUpdate = new ArrayList<>();
				for(ExternBooster ex : exb)
				{
					switch(ex.getPlayerAssociatedType())
					{
					case GLOBAL:
						allUpdate = true;
						break;
					case GROUP:
						if(ex.getGroupname() == null)
						{
							break;
						}
						ArrayList<GroupPlayerAffiliation> gpas = GroupHandler.getAllAffiliateGroup(ex.getGroupname());
						if(gpas == null || gpas.size() <= 0)
						{
							break;
						}
						for(GroupPlayerAffiliation gpa : gpas)
						{
							if(playerUUIDToUpdate.contains(gpa.getPlayerUUID()))
							{
								continue;
							}
							playerUUIDToUpdate.add(gpa.getPlayerUUID());
						}
						break;
					case SOLO:
						if(ex.getPlayerUUIDText() == null || playerUUIDToUpdate.contains(ex.getPlayerUUID()))
						{
							break;
						}
						playerUUIDToUpdate.add(ex.getPlayerUUID());
						break;
					}
					if(allUpdate)
					{
						plugin.getMysqlHandler().deleteData(Type.EXTERN_BOOSTER, "`expiry_date` = ?", now);
						break;
					} else
					{
						plugin.getMysqlHandler().deleteData(Type.EXTERN_BOOSTER, "`id` = ?", ex.getId());
					}
				}
				if(allUpdate)
				{
					ArrayList<UUID> uuids = new ArrayList<>();
					if(plugin.getProxyOnlinePlayers() != null)
					{
						for(UUID uuid : plugin.getProxyOnlinePlayers().getProxyOnlinePlayers().keySet())
						{
							uuids.add(uuid);
						}
						toUpdate(uuids);
					} else
					{
						for(Player p : Bukkit.getOnlinePlayers())
						{
							uuids.add(p.getUniqueId());
						}
						toUpdate(uuids);
					}
				} else
				{
					toUpdate(playerUUIDToUpdate);
				}
			}
		}.runTaskTimerAsynchronously(plugin, 60*20L,
				plugin.getYamlHandler().getConfig().getLong("Do.DeleteExpireExternBooster.TaskRunInMinutes", 5)*60*20L);
	}
}