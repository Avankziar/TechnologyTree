package main.java.me.avankziar.tt.spigot.cmd.tt.group;

import java.io.IOException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.ifh.general.assistance.ChatApi;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentConstructor;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentModule;
import main.java.me.avankziar.tt.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.tt.spigot.handler.GroupHandler;
import main.java.me.avankziar.tt.spigot.handler.PlayerHandler;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupData;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupPlayerAffiliation;

public class ARGGroup_Leave extends ArgumentModule
{	
	public ARGGroup_Leave(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
	}

	//tt group leave [confirm]
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
		boolean confirm = false;
		if(args.length >= 3)
		{
			if(args[2].equalsIgnoreCase("confirm") || args[2].equalsIgnoreCase("bestätigen"))
			{
				confirm = true;
			}
		}
		if(!confirm)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.Leave.NotConfirm")
					.replace("%cmd%", argumentConstructor.getSuggestion())));
			return;
		}
		if(player.getUniqueId().toString().equals(gd.getGrandmasterUUID().toString()))
		{
			int members = GroupHandler.getGroupMemberAmount(player);
			if(members > 1)
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.Leave.NotLastMember")));
				return;
			}
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.Leave.GroupClose")));
			plugin.getMysqlHandler().deleteData(Type.EXTERN_BOOSTER, "`group_name` = ?", gd.getGroupName());
			plugin.getMysqlHandler().deleteData(Type.GROUP_PLAYERAFFILIATION, "`id` = ?", gpa.getId());
			plugin.getMysqlHandler().deleteData(Type.GROUP_DATA, "`id` = ?", gd.getId());
		} else
		{
			String txt = plugin.getYamlHandler().getLang().getString("Commands.Group.Leave.PlayerLeave")
					.replace("%player%", player.getName());
			GroupHandler.sendMembersText(gd.getGroupName(), txt);
			plugin.getMysqlHandler().deleteData(Type.GROUP_PLAYERAFFILIATION, "`id` = ?", gpa.getId());
			PlayerHandler.quitPlayer(player.getUniqueId());
			PlayerHandler.joinPlayer(player);
		}
	}
}