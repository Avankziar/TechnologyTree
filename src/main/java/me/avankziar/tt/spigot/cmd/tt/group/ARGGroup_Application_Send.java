package main.java.me.avankziar.tt.spigot.cmd.tt.group;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.tt.general.ChatApi;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentConstructor;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentModule;
import main.java.me.avankziar.tt.spigot.handler.GroupHandler;
import main.java.me.avankziar.tt.spigot.handler.GroupHandler.Position;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupData;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupPlayerAffiliation;

public class ARGGroup_Application_Send extends ArgumentModule
{	
	public ARGGroup_Application_Send(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
	}

	//tt group application send <groupname>
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		if(GroupHandler.isInGroup(player.getUniqueId()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.YouAreInAGroup")));
			return;
		}
		String gn = args[3];
		GroupData gd = GroupHandler.getGroup(gn);
		if(gd == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.GroupDontExist")
					.replace("%group%", gn)));
			return;
		}
		GroupHandler.createAffiliateGroup(player.getUniqueId(), gd.getGroupName(), Position.APPLICANT, false);
		String txt = ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.Application.Send.Applicated")
				.replace("%player%", player.getName())
				.replace("%group%", gd.getGroupName()));
		ArrayList<UUID> l = new ArrayList<>();
		for(GroupPlayerAffiliation ga : GroupHandler.getAllAffiliateGroup(gd.getGroupName()))
		{
			Player m = Bukkit.getPlayer(ga.getPlayerUUID());
			if(m != null)
			{
				m.sendMessage(txt);
			} else
			{
				l.add(ga.getPlayerUUID());
			}
		}
		if(plugin.getMessageToBungee() != null && !l.isEmpty())
		{
			plugin.getMessageToBungee().sendMessage(l, txt);
		}
	}
}