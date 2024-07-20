package main.java.me.avankziar.tt.spigot.cmd.tt.group;

import java.io.IOException;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.tt.general.ChatApi;
import main.java.me.avankziar.tt.spigot.assistance.MatchApi;
import main.java.me.avankziar.tt.spigot.assistance.Utility;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentConstructor;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentModule;
import main.java.me.avankziar.tt.spigot.handler.GroupHandler;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupData;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupPlayerAffiliation;

public class ARGGroup_SetIndividualUpkeep extends ArgumentModule
{	
	public ARGGroup_SetIndividualUpkeep(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
	}

	//tt group individualupkeep <playername> <upkeep(double)>
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
		GroupPlayerAffiliation gpa2 = GroupHandler.getAffiliateGroup(uuid, gd.getGroupName());
		if(gpa2 == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.PlayerIsInNoGroup")));
			return;
		}
		if(!gpa.isCanSetIndividualDailyUpkeep())
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
		gpa2.setIndividualTechExpDailyUpkeep(upkeep);
		GroupHandler.updatePlayerAffiliation(gpa2);
		String txt = plugin.getYamlHandler().getLang().getString("Commands.Group.SetIndividuellUpkeep.Set")
				.replace("%player%", player.getName())
				.replace("%player2%", p2)
				.replace("%upkeep%", String.valueOf(upkeep));
		GroupHandler.sendMembersText(gd.getGroupName(), txt, player.getUniqueId());
	}
}