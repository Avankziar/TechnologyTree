package main.java.me.avankziar.tt.spigot.cmd.tt.group;

import java.io.IOException;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.tt.general.ChatApi;
import main.java.me.avankziar.tt.spigot.assistance.Utility;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentConstructor;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentModule;
import main.java.me.avankziar.tt.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.tt.spigot.handler.GroupHandler;
import main.java.me.avankziar.tt.spigot.handler.GroupHandler.Position;
import main.java.me.avankziar.tt.spigot.modifiervalueentry.Bypass.Permission;
import main.java.me.avankziar.tt.spigot.modifiervalueentry.ModifierValueEntry;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupData;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupPlayerAffiliation;

public class ARGGroup_SetGrandMaster extends ArgumentModule
{	
	public ARGGroup_SetGrandMaster(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
	}

	//tt group setgrandmaster <playername> [groupname]
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		boolean bypass = ModifierValueEntry.hasPermission(player, Permission.GROUP_SETGRANDMASTER);
		String p2 = args[2];
		UUID uuid = Utility.convertNameToUUID(p2);
		if(uuid == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoPlayerExist")));
			return;
		}
		String gn = null;
		if(args.length >= 4)
		{
			gn = args[3];
		}
		GroupData gd = gn == null ? GroupHandler.getGroup(player) : GroupHandler.getGroup(gn);
		if(gd == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.GroupDontExist")
					.replace("%group%", gn == null ? "N.A." : gn)));
			return;
		}
		GroupPlayerAffiliation gpa2 = GroupHandler.getAffiliateGroup(uuid, gd.getGroupName());
		if(!bypass)
		{
			if(!GroupHandler.isInGroup(player.getUniqueId()))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.YouAreInNoGroup")));
				return;
			}
			GroupPlayerAffiliation gpa = GroupHandler.getAffiliateGroup(player.getUniqueId(), gd.getGroupName());
			if(gpa == null)
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.YouAreInNoGroup")));
				return;
			}			
			if(!player.getUniqueId().toString().equals(gd.getGrandmasterUUID().toString()))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang()
						.getString("Commands.Group.SetGrandmaster.AlreadyGrandmaster")));
				return;
			}
			if(gpa2.getPlayerUUID().toString().equals(gpa.getPlayerUUID().toString()))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang()
						.getString("Commands.Group.SetGrandmaster.AlreadyGrandmaster")));
				return;
			}
		}
		if(gpa2 != null && !gpa2.getGroupName().equals(gd.getGroupName()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.PlayerIsInAGroup")
					.replace("%player%", p2)));
			return;
		}
		String txt = ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.SetGrandmaster.Set")
				.replace("%player%", p2)
				.replace("%group%", gd.getGroupName()));
		gd.setGrandmasterUUID(uuid);
		if(gpa2 == null)
		{
			plugin.getMysqlHandler().deleteData(Type.GROUP_PLAYERAFFILIATION, "`player_uuid` = ?", player.getUniqueId().toString());
			GroupHandler.createAffiliateGroup(player.getUniqueId(), gd.getGroupName(), Position.MASTER, true);
		} else
		{
			gpa2.setAsGrandmaster();
			GroupHandler.updatePlayerAffiliation(gpa2);
		}
		GroupHandler.sendMembersText(gd.getGroupName(), txt, player.getUniqueId(), uuid);
		GroupHandler.updateGroup(gd);
	}
}