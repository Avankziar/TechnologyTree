package main.java.me.avankziar.tt.spigot.cmd.tt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.tt.general.ChatApi;
import main.java.me.avankziar.tt.spigot.assistance.Utility;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentConstructor;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentModule;
import main.java.me.avankziar.tt.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.tt.spigot.handler.CatTechHandler;
import main.java.me.avankziar.tt.spigot.handler.EntryQueryStatusHandler;
import main.java.me.avankziar.tt.spigot.handler.GroupHandler;
import main.java.me.avankziar.tt.spigot.handler.PlayerHandler;
import main.java.me.avankziar.tt.spigot.handler.PlayerHandler.AcquireRespond;
import main.java.me.avankziar.tt.spigot.objects.EntryQueryType;
import main.java.me.avankziar.tt.spigot.objects.EntryStatusType;
import main.java.me.avankziar.tt.spigot.objects.PlayerAssociatedType;
import main.java.me.avankziar.tt.spigot.objects.mysql.GlobalEntryQueryStatus;
import main.java.me.avankziar.tt.spigot.objects.mysql.GlobalTechnologyPoll;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupData;
import main.java.me.avankziar.tt.spigot.objects.mysql.PlayerData;
import main.java.me.avankziar.tt.spigot.objects.mysql.UpdateTech;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.Technology;

public class ARGResearch extends ArgumentModule
{
	private static boolean cooldown = false;
	
	public ARGResearch(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
	}

	//tt research <Technology> [Groupname/Playername] 
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		if(cooldown)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Research")));
			return;
		}
		String tech = args[1];
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
		switch(t.getPlayerAssociatedType())
		{
		case GLOBAL:
			global(args, player, t);
		case GROUP:
			group(args, player, t); break;
		case SOLO:
			solo(args, player, t); break;
		}
	}
	
	private void global(String[] args, Player player, Technology t)
	{
		AcquireRespond ar = PlayerHandler.haveAlreadyResearched(player.getUniqueId(), t);
		switch(ar)
		{
		case CANNOT_BE_RESEARCH:
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("GuiHandler.Technology.CannotBeResearch")));
			break;
		case NOT_ENOUGH_TT_EXP:
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("GuiHandler.Technology.NotEnoughTTExp")));
			break;
		case NOT_ENOUGH_VANILLA_EXP:
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("GuiHandler.Technology.NotEnoughVanillaExp")));
			break;
		case NOT_ENOUGH_MONEY:
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("GuiHandler.Technology.NotEnoughMoney")));
			break;
		case NOT_ENOUGH_MATERIAL:
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("GuiHandler.Technology.NotEnoughMaterial")));
			break;
		case TECH_IS_SIMPLE_AND_ALREADY_RESEARCHED:
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("GuiHandler.Technology.IsSimpleAndAlreadyResearched")
					.replace("%tech%", t.getDisplayName())));
			break;
		case TECH_MAX_LEVEL_IS_REACHED:
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("GuiHandler.MaxResearchLevelAlreadyReached")
					.replace("%tech%", t.getDisplayName())));
			break;
		case CAN_BE_RESEARCHED:
			cooldown = true;
			int globalResearchlevel = PlayerHandler.researchGlobalTechnology(t);
			GlobalEntryQueryStatus geqs = EntryQueryStatusHandler.getGlobalEntryHighestResearchLevel(t,
					EntryQueryType.TECHNOLOGY, EntryStatusType.HAVE_RESEARCHED_IT);
			new BukkitRunnable()
			{
				int quantity = 25;
				int start = -25;
				@Override
				public void run()
				{
					start = start + 25;
					ArrayList<PlayerData> alpd = PlayerData.convert(plugin.getMysqlHandler().getList(
							Type.PLAYERDATA, "`id` ASC", start, quantity, "?", 1));
					if(alpd.isEmpty())
					{
						cancel();
						cooldown = false;
						return;
					}
					for(PlayerData pd : alpd)
					{
						GlobalTechnologyPoll gtp = new GlobalTechnologyPoll(0, pd.getUUID(), t.getInternName(),
								globalResearchlevel, true, true, t.getInternName(),
								geqs.getId(), 0);
						plugin.getMysqlHandler().create(Type.GLOBAL_TECHNOLOGYPOLL, gtp);
						if(plugin.getMessageToBungee() != null)
						{
							plugin.getMessageToBungee().sendMessage(
									plugin.getYamlHandler().getLang().getString("GuiHandler.Technology.TechnologyResearched")
										.replace("%level%", String.valueOf(globalResearchlevel))
										.replace("%tech%", t.getDisplayName()));
						}
						if(Bukkit.getPlayer(pd.getUUID()) != null)
						{
							PlayerHandler.doUpdate(player, t, geqs.getId(), globalResearchlevel);
							if(plugin.getMessageToBungee() == null)
							{
								Bukkit.getPlayer(pd.getUUID()).sendMessage(ChatApi.tl(
										plugin.getYamlHandler().getLang().getString("GuiHandler.Technology.TechnologyResearched")
										.replace("%level%", String.valueOf(globalResearchlevel))
										.replace("%tech%", t.getDisplayName())));
							}							
						} else if(plugin.getProxyOnlinePlayers() != null 
								&& plugin.getProxyOnlinePlayers().getProxyOnlinePlayers().keySet().contains(pd.getUUID()))
						{
							UpdateTech ut = new UpdateTech(0, pd.getUUID(), PlayerAssociatedType.GLOBAL, 
									t.getInternName(), geqs.getId(), globalResearchlevel);
							plugin.getMysqlHandler().create(Type.UPDATE_TECH, ut);
							
						}
					}
				}
			}.runTaskTimerAsynchronously(plugin, 0L, 75L);
		}
	}
	
	private void group(String[] args, Player player, Technology t)
	{
		if(args.length < 3)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Research.GroupnameNotFound")));
			return;
		}
		String gn = null;
		if(args.length >= 3)
		{
			gn = args[2];
		}
		GroupData gd = GroupHandler.getGroup(gn);
		if(gd == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.GroupDontExist")
					.replace("%group%", "N.A.")));
			return;
		}
		AcquireRespond ar = PlayerHandler.haveAlreadyResearched(player.getUniqueId(), t);
		switch(ar)
		{
		case CANNOT_BE_RESEARCH:
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("GuiHandler.Technology.CannotBeResearch")));
			break;
		case NOT_ENOUGH_TT_EXP:
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("GuiHandler.Technology.NotEnoughTTExp")));
			break;
		case NOT_ENOUGH_VANILLA_EXP:
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("GuiHandler.Technology.NotEnoughVanillaExp")));
			break;
		case NOT_ENOUGH_MONEY:
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("GuiHandler.Technology.NotEnoughMoney")));
			break;
		case NOT_ENOUGH_MATERIAL:
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("GuiHandler.Technology.NotEnoughMaterial")));
			break;
		case TECH_IS_SIMPLE_AND_ALREADY_RESEARCHED:
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("GuiHandler.Technology.IsSimpleAndAlreadyResearched")
					.replace("%tech%", t.getDisplayName())));
			break;
		case TECH_MAX_LEVEL_IS_REACHED:
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("GuiHandler.MaxResearchLevelAlreadyReached")
					.replace("%tech%", t.getDisplayName())));
			break;
		case CAN_BE_RESEARCHED:
			cooldown = true;
			int rlvl = PlayerHandler.researchGroupTechnology(player, t, true);
			GroupHandler.sendMembersText(gd.getGroupName(), plugin.getYamlHandler().getLang().getString("GuiHandler.Technology.TechnologyResearched")
					.replace("%level%", String.valueOf(rlvl))
					.replace("%tech%", t.getDisplayName()));
			cooldown = false;
		}
				
	}
	
	private void solo(String[] args, Player player, Technology t)
	{
		if(args.length < 3)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Research.PlayernameNotFound")));
			return;
		}
		String p2 = args[2];
		UUID uuid = Utility.convertNameToUUID(p2);
		if(uuid == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoPlayerExist")));
			return;
		}
		AcquireRespond ar = PlayerHandler.haveAlreadyResearched(player.getUniqueId(), t);
		switch(ar)
		{
		case CANNOT_BE_RESEARCH:
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("GuiHandler.Technology.CannotBeResearch")));
			break;
		case NOT_ENOUGH_TT_EXP:
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("GuiHandler.Technology.NotEnoughTTExp")));
			break;
		case NOT_ENOUGH_VANILLA_EXP:
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("GuiHandler.Technology.NotEnoughVanillaExp")));
			break;
		case NOT_ENOUGH_MONEY:
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("GuiHandler.Technology.NotEnoughMoney")));
			break;
		case NOT_ENOUGH_MATERIAL:
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("GuiHandler.Technology.NotEnoughMaterial")));
			break;
		case TECH_IS_SIMPLE_AND_ALREADY_RESEARCHED:
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("GuiHandler.Technology.IsSimpleAndAlreadyResearched")
					.replace("%tech%", t.getDisplayName())));
			break;
		case TECH_MAX_LEVEL_IS_REACHED:
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("GuiHandler.MaxResearchLevelAlreadyReached")
					.replace("%tech%", t.getDisplayName())));
			break;
		case CAN_BE_RESEARCHED:
			cooldown = true;
			int rlvl = PlayerHandler.researchSoloTechnology(player, t, true);
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("GuiHandler.Technology.TechnologyResearched")
					.replace("%level%", String.valueOf(rlvl))
					.replace("%tech%", t.getDisplayName())));
			cooldown = false;
		}
	}
}