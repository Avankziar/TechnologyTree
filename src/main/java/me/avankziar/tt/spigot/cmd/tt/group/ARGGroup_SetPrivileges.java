package main.java.me.avankziar.tt.spigot.cmd.tt.group;

import java.io.IOException;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.ifh.general.assistance.ChatApi;
import main.java.me.avankziar.tt.spigot.assistance.Utility;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentConstructor;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentModule;
import main.java.me.avankziar.tt.spigot.handler.GroupHandler;
import main.java.me.avankziar.tt.spigot.handler.GroupHandler.Position;
import main.java.me.avankziar.tt.spigot.objects.GroupPrivilege;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupData;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupPlayerAffiliation;

public class ARGGroup_SetPrivileges extends ArgumentModule
{
	public ARGGroup_SetPrivileges(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
	}

	//tt group setprivileges <playername> <groupprivilege>
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
		String p2 = args[2];
		UUID uuid = Utility.convertNameToUUID(p2);
		if(uuid == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoPlayerExist")));
			return;
		}
		GroupPlayerAffiliation gpa2 = GroupHandler.getAffiliateGroup(uuid, gd.getGroupName());
		if(gpa2 == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.PlayerIsInNoGroup")));
			return;
		}
		GroupPrivilege gpvl = null;
		try
		{
			gpvl = GroupPrivilege.valueOf(args[3]);
		} catch(Exception e)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.SetPrivileges.NoPrivilege")));
			return;
		}
		GroupPlayerAffiliation gpa = GroupHandler.getAffiliateGroup(player.getUniqueId(), gd.getGroupName());
		if(gpa == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.YouAreInNoGroup")));
			return;
		}
		if(player.getUniqueId().toString().equals(gd.getGrandmasterUUID().toString())
				&& uuid.toString().equals(gd.getGrandmasterUUID().toString()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.SetPrivileges.YouAreGrandmaster")));
			return;
		}
		if(!player.getUniqueId().toString().equals(gd.getGrandmasterUUID().toString()))
		{
			if(gpa.getRank().getRank() >= gpa2.getRank().getRank())
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang()
						.getString("Commands.Group.SetPrivileges.YourGroupRankIsLowerThanTheOther")
						.replace("%rank%", GroupHandler.getPositionLocale(gpa2.getRank())))
						.replace("%prank%", GroupHandler.getPositionLocale(gpa.getRank())));
				return;
			}
			if(gpvl.getCausalRank() < gpa.getRank().getRank())
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang()
						.getString("Commands.Group.SetPrivileges.YourGroupRankDontMatchTheNeededRank")
						.replace("%rank%", GroupHandler.getPositionLocale(Position.getPosition(gpvl.getCausalRank())))
						.replace("%prank%", GroupHandler.getPositionLocale(gpa.getRank()))));
				return;
			}
			if(!gpvl.getCausalBoolean(gpa))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang()
						.getString("Commands.Group.SetPrivileges.YouDontHaveThePrivilegeYourself")));
				return;
			}
		}
		if(gpvl.getCausalBoolean(gpa2))
		{
			String txt = plugin.getYamlHandler().getLang()
					.getString("Commands.Group.SetPrivileges.Remove."+gpvl.toString())
					.replace("%player%", p2);
			player.sendMessage(ChatApi.tl(txt));
			GroupHandler.sendMemberText(uuid, txt);
		} else
		{
			String txt = plugin.getYamlHandler().getLang()
					.getString("Commands.Group.SetPrivileges.Apply."+gpvl.toString())
					.replace("%player%", p2);
			player.sendMessage(ChatApi.tl(txt));
			GroupHandler.sendMemberText(uuid, txt);
		}
		switch(gpvl)
		{
		case CAN_SETINDIVIDUAL_DAILY_UPKEEP: gpa2.setCanSetIndividualDailyUpkeep(!gpvl.getCausalBoolean(gpa2)); break;
		case CAN_KICK: gpa2.setCanKick(!gpvl.getCausalBoolean(gpa2)); break;
		case CAN_SETDEFAULT_DAILY_UPKEEP: gpa2.setCanSetDefaultDailyUpkeep(!gpvl.getCausalBoolean(gpa2)); break;
		case CAN_PROMOTE: gpa2.setCanPromote(!gpvl.getCausalBoolean(gpa2)); break;
		case CAN_DEMOTE: gpa2.setCanDemote(!gpvl.getCausalBoolean(gpa2)); break;
		case CAN_INCREASE_LEVEL: gpa2.setCanIncreaseLevel(!gpvl.getCausalBoolean(gpa2)); break;
		case CAN_RESEARCH: gpa2.setCanResearch(!gpvl.getCausalBoolean(gpa2)); break;
		case CAN_SENDINVITE: gpa2.setCanInvite(!gpvl.getCausalBoolean(gpa2)); break;
		case CAN_ACCEPT_APPLICATION: gpa2.setCanAcceptApplication(!gpvl.getCausalBoolean(gpa2)); break;
		default: break;
		}
		GroupHandler.updatePlayerAffiliation(gpa2);
	}
}