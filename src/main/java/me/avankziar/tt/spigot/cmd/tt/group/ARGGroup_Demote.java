package main.java.me.avankziar.tt.spigot.cmd.tt.group;

import java.io.IOException;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.tt.general.ChatApi;
import main.java.me.avankziar.tt.spigot.assistance.Utility;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentConstructor;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentModule;
import main.java.me.avankziar.tt.spigot.handler.GroupHandler;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupData;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupPlayerAffiliation;

public class ARGGroup_Demote extends ArgumentModule
{	
	public ARGGroup_Demote(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
	}

	//tt group demote <playername> <rank>
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
		if(!gpa.isCanDemote())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.Privilege.CanNotDemote")));
			return;
		}
		GroupHandler.Position pos = null;
		try
		{
			pos = GroupHandler.Position.valueOf(args[3]);
		} catch(Exception e)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.Position.NoPosition")
					.replace("%rank%", args[3])));
			return;
		}
		if(uuid.toString().equals(gd.getGrandmasterUUID().toString()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Group.SetPrivileges.PlayerAreGrandmaster")));
			return;
		}
		if(gpa2.getPlayerUUID().toString().equals(gpa.getPlayerUUID().toString()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang()
					.getString("Commands.Group.Demote.YouCannotDemoteYourself")));
			return;
		}
		if(gpa2.getRank().getRank() >= pos.getRank())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.Demote.RankIsHigher")
					.replace("%player%", p2)));
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
		}
		gpa2.setRank(pos);
		gpa2.setIndividualTechExpDailyUpkeep(gd.getDefaultGroupTechExpDailyUpkeep(pos));
		GroupHandler.updatePlayerAffiliation(gpa2);
		String txt = plugin.getYamlHandler().getLang().getString("Commands.Group.Demote.Set")
				.replace("%player%", player.getName())
				.replace("%player2%", p2)
				.replace("%rank%", GroupHandler.getPositionLocale(pos));
		GroupHandler.sendMembersText(gd.getGroupName(), txt);
	}
}