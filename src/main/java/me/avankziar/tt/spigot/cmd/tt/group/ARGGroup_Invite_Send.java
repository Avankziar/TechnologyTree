package main.java.me.avankziar.tt.spigot.cmd.tt.group;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.tt.general.ChatApi;
import main.java.me.avankziar.tt.spigot.assistance.Utility;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentConstructor;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentModule;
import main.java.me.avankziar.tt.spigot.cmdtree.CommandExecuteType;
import main.java.me.avankziar.tt.spigot.cmdtree.CommandSuggest;
import main.java.me.avankziar.tt.spigot.handler.GroupHandler;
import main.java.me.avankziar.tt.spigot.handler.GroupHandler.Position;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupData;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class ARGGroup_Invite_Send extends ArgumentModule
{	
	public ARGGroup_Invite_Send(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
	}

	//tt group invite send <playername>
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		if(!GroupHandler.isInGroup(player.getUniqueId()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.YouAreInNoGroup")));
			return;
		}
		if(!GroupHandler.getAffiliateGroup(player).isCanInvite())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.Privilege.CanNotInvite")));
			return;
		}
		int gdta = GroupHandler.getGroupMemberTotalAmount(player);
		int gda = GroupHandler.getGroupMemberAmount(player);
		if(gdta <= gda)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.GroupHasToManyMember")));
			return;
		}
		String p2 = args[3];
		UUID uuid = Utility.convertNameToUUID(p2);
		if(uuid == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoPlayerExist")));
			return;
		}
		if(GroupHandler.isInGroup(uuid))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.PlayerIsInAGroup")
					.replace("%player%", p2)));
			return;
		}
		GroupData gd = GroupHandler.getGroup(player);
		GroupHandler.createAffiliateGroup(uuid, gd.getGroupName(), Position.INVITEE, false);
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.Invite.Send.SendToInviter")
				.replace("%player%", p2)));
		TextComponent tx = ChatApi.clickEvent(plugin.getYamlHandler().getLang().getString("Commands.Group.Invite.Send.SendToInvitee")
							.replace("%player%", player.getName())
							.replace("%group%", gd.getGroupName()),
							Action.SUGGEST_COMMAND, CommandSuggest.get(CommandExecuteType.TT_GROUP_INVITE_ACCEPT) + gd.getGroupName());
		if(Bukkit.getPlayer(uuid) != null)
		{
			Bukkit.getPlayer(uuid).spigot().sendMessage(tx);
		} else
		{
			if(plugin.getProxyOnlinePlayers() != null && plugin.getBaseComponentToBungee() != null)
			{
				if(plugin.getProxyOnlinePlayers().isProxyOnline(uuid))
				{
					ArrayList<ArrayList<BaseComponent>> listInList = new ArrayList<>();
					ArrayList<BaseComponent> list = new ArrayList<>();
					list.add(tx);
					listInList.add(list);
					plugin.getBaseComponentToBungee().sendMessage(uuid, listInList);
				}
			} 
		}
	}
}