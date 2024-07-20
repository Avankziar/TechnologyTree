package main.java.me.avankziar.tt.spigot.cmd.tt.group;

import java.io.IOException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.tt.general.ChatApi;
import main.java.me.avankziar.tt.spigot.assistance.MatchApi;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentConstructor;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentModule;
import main.java.me.avankziar.tt.spigot.handler.GroupHandler;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupData;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupPlayerAffiliation;

public class ARGGroup_SetDefaultUpkeep extends ArgumentModule
{	
	public ARGGroup_SetDefaultUpkeep(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
	}

	//tt group defaultupkeep <position> <upkeep(double)>
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		if(!GroupHandler.isInGroup(player.getUniqueId()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.YouAreInNoGroup")));
			return;
		}
		GroupData gd = GroupHandler.getGroup(player);
		if(gd == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.GroupDontExist")
					.replace("%group%", "N.A.")));
			return;
		}
		GroupPlayerAffiliation gpa = GroupHandler.getAffiliateGroup(player.getUniqueId(), gd.getGroupName());
		if(gpa == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.YouAreInNoGroup")));
			return;
		}
		if(!gpa.isCanSetDefaultDailyUpkeep())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.Privilege.CanNotSetIndividualUpKeep")));
			return;
		}
		if(!MatchApi.isDouble(args[3]))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoDouble")
					.replace("%value%", args[3])));
			return;
		}
		double upkeep = Double.valueOf(args[3]);
		if(!MatchApi.isPositivNumber(upkeep))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("IsNegativ")
					.replace("%value%", args[3])));
			return;
		}
		GroupHandler.Position pos = null;
		try
		{
			pos = GroupHandler.Position.valueOf(args[2]);
		} catch(Exception e)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.Position.NoPosition")
					.replace("%rank%", args[2])));
			return;
		}
		switch(pos)
		{
		case MASTER:
			gd.setDefaultGroupTechExpDailyUpkeep_Master(upkeep); break;
		case VICE: 
			gd.setDefaultGroupTechExpDailyUpkeep_Vice(upkeep); break;
		case COUNCILMEMBER: 
			gd.setDefaultGroupTechExpDailyUpkeep_CouncilMember(upkeep); break;
		case MEMBER: 
			gd.setDefaultGroupTechExpDailyUpkeep_Member(upkeep); break;
		case ADEPT:
			gd.setDefaultGroupTechExpDailyUpkeep_Adept(upkeep); break;
		default:
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.SetDefaultUpkeep.WrongPosition")
					.replace("%rank%", GroupHandler.getPositionLocale(pos))));
			return;
		}
		String txt = plugin.getYamlHandler().getLang().getString("Commands.Group.SetDefaultUpkeep.Set")
				.replace("%rank%", GroupHandler.getPositionLocale(pos))
				.replace("%upkeep%", String.valueOf(upkeep));
		GroupHandler.sendMembersText(gd.getGroupName(), txt, player.getUniqueId());
		GroupHandler.updateGroup(gd);
	}
}