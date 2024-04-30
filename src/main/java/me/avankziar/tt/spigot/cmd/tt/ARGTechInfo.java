package main.java.me.avankziar.tt.spigot.cmd.tt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.ifh.general.math.MathFormulaParser;
import main.java.me.avankziar.tt.general.ChatApi;
import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.assistance.MatchApi;
import main.java.me.avankziar.tt.spigot.assistance.TimeHandler;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentConstructor;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentModule;
import main.java.me.avankziar.tt.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.tt.spigot.handler.CatTechHandler;
import main.java.me.avankziar.tt.spigot.handler.GroupHandler;
import main.java.me.avankziar.tt.spigot.handler.PlayerHandler;
import main.java.me.avankziar.tt.spigot.handler.RecipeHandler.RecipeType;
import main.java.me.avankziar.tt.spigot.objects.EntryQueryType;
import main.java.me.avankziar.tt.spigot.objects.EntryStatusType;
import main.java.me.avankziar.tt.spigot.objects.PlayerAssociatedType;
import main.java.me.avankziar.tt.spigot.objects.TechnologyType;
import main.java.me.avankziar.tt.spigot.objects.mysql.GlobalEntryQueryStatus;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupData;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupEntryQueryStatus;
import main.java.me.avankziar.tt.spigot.objects.mysql.SoloEntryQueryStatus;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.DropChance;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.Technology;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.UnlockableInteraction;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class ARGTechInfo extends ArgumentModule
{	
	public ARGTechInfo(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
	}

	//tt techinfo <Technologie> [Level, welches man anschauen möchte]
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		String tech = args[1];
		Integer level = null;
		if(args.length >= 3 && MatchApi.isInteger(args[2]))
		{
			level = Integer.parseInt(args[2]);
		}
		Integer lvl = level;
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				Technology t = CatTechHandler.technologyMapSolo.get(tech);
				if(t == null)
				{
					t = CatTechHandler.technologyMapGlobal.get(tech);
					if(t == null)
					{
						t = CatTechHandler.technologyMapGroup.get(tech);
					}
				}
				if(t == null)
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.TechInfo.TechNotFound")));
					return;
				}
				techInfo(player, t, lvl);
			}
		}.runTaskAsynchronously(plugin);
		
	}
	
	public static void techInfo(Player player, Technology t, Integer lvl)
	{
		int techLevel = 0;
		int acquiredTech = 0;
		if(lvl != null)
		{
			techLevel = lvl;
			acquiredTech = lvl-1;
		} else
		{
			switch(t.getPlayerAssociatedType())
			{
			case SOLO:
				ArrayList<SoloEntryQueryStatus> seqsList = SoloEntryQueryStatus.convert(plugin.getMysqlHandler().getList(Type.SOLO_ENTRYQUERYSTATUS,
						"`research_level` DESC", 0, 1,
						"`player_uuid` = ? AND `intern_name` = ? AND `entry_query_type` = ?",
						player.getUniqueId().toString(), t.getInternName(), EntryQueryType.TECHNOLOGY.toString()));
				SoloEntryQueryStatus seqs = seqsList.size() == 0 ? null : seqsList.get(0);
				techLevel = seqs == null ? 1 : 
					(seqs.getStatusType() == EntryStatusType.HAVE_RESEARCHED_IT 
					? seqs.getResearchLevel() + 1 : seqs.getResearchLevel()); //Tech which may to acquire
				acquiredTech = seqs == null ? 0 : 
					(seqs.getStatusType() == EntryStatusType.HAVE_RESEARCHED_IT 
					? seqs.getResearchLevel() : seqs.getResearchLevel() - 1); //Tech which was already acquire
				break;
			case GROUP:
				GroupData gd = GroupHandler.getGroup(player);
				String gn = "";
				if(gd != null)
				{
					gn = gd.getGroupName();
				}
				ArrayList<GroupEntryQueryStatus> greqsList = GroupEntryQueryStatus.convert(plugin.getMysqlHandler().getList(Type.GROUP_ENTRYQUERYSTATUS,
						"`research_level` DESC", 0, 1,
						"`group_name` = ? AND `intern_name` = ? AND `entry_query_type` = ?",
						gn, t.getInternName(), EntryQueryType.TECHNOLOGY.toString()));
				GroupEntryQueryStatus greqs = greqsList.size() == 0 ? null : greqsList.get(0);
				techLevel = greqs == null ? 1 : 
					(greqs.getStatusType() == EntryStatusType.HAVE_RESEARCHED_IT 
					? greqs.getResearchLevel() + 1 : greqs.getResearchLevel()); //Tech which may to acquire
				acquiredTech = greqs == null ? 0 :
					(greqs.getStatusType() == EntryStatusType.HAVE_RESEARCHED_IT 
					? greqs.getResearchLevel() : greqs.getResearchLevel() - 1); //Tech which was already acquire
				break;
			case GLOBAL:
				ArrayList<GlobalEntryQueryStatus> geqsList = GlobalEntryQueryStatus.convert(plugin.getMysqlHandler().getList(Type.GLOBAL_ENTRYQUERYSTATUS,
						"`research_level` DESC", 0, 1,
						"`intern_name` = ? AND `entry_query_type` = ?",
						t.getInternName(), EntryQueryType.TECHNOLOGY.toString()));
				GlobalEntryQueryStatus geqs = geqsList.size() == 0 ? null : geqsList.get(0);
				techLevel = geqs == null ? 1 :
					(geqs.getStatusType() == EntryStatusType.HAVE_RESEARCHED_IT 
					? geqs.getResearchLevel() + 1 : geqs.getResearchLevel()); //Tech which may to acquire
				acquiredTech = geqs == null ? 0 : 
					(geqs.getStatusType() == EntryStatusType.HAVE_RESEARCHED_IT 
					? geqs.getResearchLevel() : geqs.getResearchLevel() - 1); //Tech which was already acquire
				break;
			}
		}
		if(techLevel > t.getMaximalTechnologyLevelToResearch())
		{
			techLevel = t.getMaximalTechnologyLevelToResearch();
		}
		HashMap<String, Double> map = new HashMap<>();
		map.put(PlayerHandler.TECHLEVEL, Double.valueOf(techLevel));
		map.put(PlayerHandler.TECHACQUIRED, Double.valueOf(acquiredTech));
		map.put(PlayerHandler.SOLO_RESEARCHED_TOTALTECH, Double.valueOf(PlayerHandler.getTotalResearchSoloTech(player.getUniqueId())));
		map.put(PlayerHandler.GLOBAL_RESEARCHED_TOTALTECH, Double.valueOf(PlayerHandler.getTotalResearchGlobalTech()));
		YamlConfiguration y = plugin.getYamlHandler().getLang();
		String path = "GuiHandler.Technology.Info.";
		ArrayList<BaseComponent> albc = new ArrayList<>();
		TextComponent tx = ChatApi.hoverEvent(y.getString(path+"Headline").replace("%tech%", t.getDisplayName()),
				Action.SHOW_TEXT,
				y.getString(path+"Internname").replace("%name%", t.getInternName())+"~!~"+
				y.getString(path+"OverlyingSubCategory").replace("%name%", t.getOverlyingSubCategory())
				);
		albc.add(tx);
		tx = ChatApi.tctl(y.getString(path+"PlayerAssociatedTypeAndTechType")
				.replace("%pat%", t.getPlayerAssociatedType().toString())
				.replace("%ttype%", t.getTechnologyType().toString())
				);
		albc.add(tx);
		tx = ChatApi.tctl(y.getString(path+"MaxTechLvlToResearchAndGuiSlot")
				.replace("%lvl%", String.valueOf((t.getTechnologyType() == TechnologyType.MULTIPLE ? t.getMaximalTechnologyLevelToResearch() : 1)))
				.replace("%slot%", String.valueOf(t.getGuiSlot()))
				);
		albc.add(tx);
		if(lvl == null)
		{
			tx = ChatApi.tctl(y.getString(path+"LevelResearched")
					.replace("%lvl%", String.valueOf(acquiredTech)));
		} else
		{
			tx = ChatApi.tctl(y.getString(path+"LevelDisplayed")
					.replace("%lvl%", String.valueOf(acquiredTech)));
		}
		albc.add(tx);		
		if(t.getTechnologyType() == TechnologyType.BOOSTER)
		{
			tx = ChatApi.tctl(y.getString(path+"BoosterExpireTimes")
					.replace("%exp%", TimeHandler.getRepeatingTime(t.getIfBoosterDurationUntilExpiration(), "yyyy-dd-HH:mm")));
			albc.add(tx);
		}
		if(t.getPlayerAssociatedType() == PlayerAssociatedType.GLOBAL)
		{
			tx = ChatApi.hoverEvent(y.getString(path+"GlobalTechPollParticipants.Info"),
				Action.SHOW_TEXT,
				y.getString(path+"GlobalTechPollParticipants.Interaction")
				.replace("%v%", String.valueOf(t.getForUninvolvedPollParticipants_RewardUnlockableInteractionsInPercent()))
				+"~!~"+
				y.getString(path+"GlobalTechPollParticipants.Recipe")
				.replace("%v%", String.valueOf(t.getForUninvolvedPollParticipants_RewardRecipesInPercent()))
				+"~!~"+
				y.getString(path+"GlobalTechPollParticipants.DropChance")
				.replace("%v%", String.valueOf(t.getForUninvolvedPollParticipants_RewardDropChancesInPercent()))
				+"~!~"+
				y.getString(path+"GlobalTechPollParticipants.SilkTouchDropChance")
				.replace("%v%", String.valueOf(t.getForUninvolvedPollParticipants_RewardSilkTouchDropChancesInPercent()))
				+"~!~"+
				y.getString(path+"GlobalTechPollParticipants.Commands")
				.replace("%v%", String.valueOf(t.getForUninvolvedPollParticipants_RewardCommandsInPercent()))
				+"~!~"+
				y.getString(path+"GlobalTechPollParticipants.Items")
				.replace("%v%", String.valueOf(t.getForUninvolvedPollParticipants_RewardItemsInPercent()))
				+"~!~"+
				y.getString(path+"GlobalTechPollParticipants.Modifier")
				.replace("%v%", String.valueOf(t.getForUninvolvedPollParticipants_RewardModifiersInPercent()))
				+"~!~"+
				y.getString(path+"GlobalTechPollParticipants.ValueEntry")
				.replace("%v%", String.valueOf(t.getForUninvolvedPollParticipants_RewardValueEntryInPercent())));
			albc.add(tx);
		}
		if(t.getCostTTExp().containsKey(techLevel))
		{
			double ttexp =  new MathFormulaParser().parse(t.getCostTTExp().get(techLevel), map);
			albc.add(ChatApi.tctl(y.getString(path+"Lvl.CostTTExp")
					.replace("%f%", t.getCostTTExp().get(techLevel))
					.replace("%v%", String.valueOf(ttexp))));
		}
		if(t.getCostVanillaExp().containsKey(techLevel))
		{
			double vexp = (int) Math.floor(new MathFormulaParser().parse(t.getCostVanillaExp().get(techLevel), map));
			albc.add(ChatApi.tctl(y.getString(path+"Lvl.CostVExp")
					.replace("%f%", t.getCostVanillaExp().get(techLevel))
					.replace("%v%", String.valueOf(vexp))));
		}
		if(t.getCostMoney().containsKey(techLevel))
		{
			double money = new MathFormulaParser().parse(t.getCostMoney().get(techLevel), map);
			albc.add(ChatApi.tctl(y.getString(path+"Lvl.CostMoney")
					.replace("%f%", t.getCostMoney().get(techLevel))
					.replace("%v%", String.valueOf(money))));
		}
		if(t.getCostMaterial().containsKey(techLevel))
		{
			albc.add(ChatApi.tctl(y.getString(path+"Lvl.CostMaterial")));
			for(Entry<Material, String> e : t.getCostMaterial().get(techLevel).entrySet())
			{
				albc.add(ChatApi.tctl("&e"+e.getValue()+"x "+(TT.getPlugin().getEnumTl() != null
						  									? TT.getPlugin().getEnumTl().getLocalization(e.getKey())
						  									: e.getKey().toString())));
			}
		}
		if(t.getResearchRequirementConditionQuery().containsKey(techLevel))
		{
			albc.add(ChatApi.tctl(y.getString(path+"Lvl.ResearchRequirementConditionQuery")));
			for(String s : t.getResearchRequirementConditionQuery().get(techLevel))
			{
				if(s.startsWith("event"))
				{
					continue;
				}
				albc.add(ChatApi.tctl("◦ "+s));
			}
		}
		if(t.getRewardUnlockableInteractions().containsKey(techLevel))
		{
			albc.add(ChatApi.tctl(y.getString(path+"Lvl.RewardInteraction")));
			for(UnlockableInteraction ui : t.getRewardUnlockableInteractions().get(techLevel))
			{
				albc.add(ChatApi.tctl("◦ "+ui.getEventType().toString()+":"+
						(ui.getEventMaterial() != null ? ui.getEventMaterial().toString() : "/")+":"+
						(ui.getEventEntityType() != null ? ui.getEventEntityType().toString() : "/")));
				albc.add(ChatApi.tctl("  "+ui.getToolType().toString()+" | "+(ui.isCanAccess() ? "CanAccess="+ui.isCanAccess() : "")));
				albc.add(ChatApi.tctl("  "+ui.getTechnologyExperience()+" TTExp | "+ui.getVanillaExperience()+" VanillaExp"));
				for(Entry<String, Double> e : ui.getMoneyMap().entrySet())
				{
					albc.add(ChatApi.tctl("  "+e.getKey()+"="+e.getValue()));
				}
				for(Entry<String, Double> e : ui.getCommandMap().entrySet())
				{
					albc.add(ChatApi.tctl("  "+e.getKey()+"="+e.getValue()));
				}
			}
		}
		if(t.getRewardRecipes().containsKey(techLevel))
		{
			albc.add(ChatApi.tctl(y.getString(path+"Lvl.RewardRecipe")));
			for(Entry<RecipeType, ArrayList<String>> e : t.getRewardRecipes().get(techLevel).entrySet())
			{
				albc.add(ChatApi.tctl("◦ "+e.getKey().toString()+":"));
				for(int j = 0; j < e.getValue().size(); j++)
				{
					albc.add(ChatApi.tctl("◦◦ "+e.getValue().get(j)));
				}
			}
		}
		if(t.getRewardDropChances().containsKey(techLevel))
		{
			albc.add(ChatApi.tctl(y.getString(path+"Lvl.RewardDropChance")));
			for(DropChance s : t.getRewardDropChances().get(techLevel))
			{
				albc.add(ChatApi.tctl("◦ "+s.getEventType().toString()
						+"&f:"+s.getToolType().toString()
						+"&f:"+(s.getEventMaterial() != null ? s.getEventMaterial().toString() : "X")
						+"&f:"+(s.getEventEntityType() != null ? s.getEventEntityType().toString() : "X")
						+"&f:"+s.getToDropItem()
						+"&f:"+s.getToDropItemAmount()
						+"&f:"+(s.getDropChance()*100)+" %"));
			}
		}
		if(t.getRewardSilkTouchDropChances().containsKey(techLevel))
		{
			albc.add(ChatApi.tctl(y.getString(path+"Lvl.RewardSilkTouchDropChance")));
			for(DropChance s : t.getRewardSilkTouchDropChances().get(techLevel))
			{
				albc.add(ChatApi.tctl("◦ "+s.getEventType().toString()
						+"&f:"+s.getToolType().toString()
						+"&f:"+(s.getEventMaterial() != null ? s.getEventMaterial().toString() : "X")
						+"&f:"+(s.getEventEntityType() != null ? s.getEventEntityType().toString() : "X")
						+"&f:"+s.getToDropItem()
						+"&f:"+s.getToDropItemAmount()
						+"&f:"+(s.getDropChance()*100)+" %"));
			}
		}
		if(t.getRewardCommandList().containsKey(techLevel))
		{
			albc.add(ChatApi.tctl(y.getString(path+"Lvl.RewardCommand")));
			for(String s : t.getRewardCommandList().get(techLevel))
			{
				albc.add(ChatApi.tctl("◦ "+s));
			}
		}
		if(t.getRewardItemList().containsKey(techLevel))
		{
			albc.add(ChatApi.tctl(y.getString(path+"Lvl.RewardItem")));
			for(String s : t.getRewardItemList().get(techLevel))
			{
				albc.add(ChatApi.tctl("◦ "+s));
			}
		}
		if(t.getRewardModifierList().containsKey(techLevel))
		{
			albc.add(ChatApi.tctl(y.getString(path+"Lvl.RewardModifier")));
			for(String s : t.getRewardModifierList().get(techLevel))
			{
				albc.add(ChatApi.tctl("◦ "+s));
			}
		}
		if(t.getRewardValueEntryList().containsKey(techLevel))
		{
			albc.add(ChatApi.tctl(y.getString(path+"Lvl.RewardValueEntry")));
			for(String s : t.getRewardValueEntryList().get(techLevel))
			{
				albc.add(ChatApi.tctl("◦ "+s));
			}
		}
		for(BaseComponent bc : albc)
		{
			player.spigot().sendMessage(bc);
		}
	}
}