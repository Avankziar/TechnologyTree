package main.java.me.avankziar.tt.spigot.cmd.tt.group;

import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.tt.general.ChatApi;
import main.java.me.avankziar.tt.spigot.assistance.Utility;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentConstructor;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentModule;
import main.java.me.avankziar.tt.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.tt.spigot.handler.GroupHandler;
import main.java.me.avankziar.tt.spigot.handler.PlayerHandler;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupData;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupPlayerAffiliation;

public class ARGGroup_Kick extends ArgumentModule
{
	public ARGGroup_Kick(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
	}

	//tt group kick <playername>
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
		if(!gpa.isCanKick())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.Privilege.CanNotKick")));
			return;
		}
		if(player.getUniqueId().toString().equals(uuid.toString()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.Kick.YouCannotKickYourself")));
			return;
		}
		if(!player.getUniqueId().toString().equals(gd.getGrandmasterUUID().toString()))
		{
			if(gpa.getRank().getRank() >= gpa2.getRank().getRank())
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang()
						.getString("Commands.Group.Kick.YourGroupRankIsLowerThanTheOther")
						.replace("%rank%", GroupHandler.getPositionLocale(gpa2.getRank())))
						.replace("%prank%", GroupHandler.getPositionLocale(gpa.getRank())));
				return;
			}
		}
		String txt = plugin.getYamlHandler().getLang().getString("Commands.Group.Kick.KickedPlayer")
				.replace("%player1%", player.getName())
				.replace("%player2%", p2);
		GroupHandler.sendMembersText(gd.getGroupName(), txt);
		plugin.getMysqlHandler().deleteData(Type.GROUP_PLAYERAFFILIATION, "`id` = ?", gpa2.getId());
		if(Bukkit.getPlayer(uuid) != null)
		{
			PlayerHandler.quitPlayer(uuid);
			PlayerHandler.joinPlayer(Bukkit.getPlayer(uuid));
		}
	}
}