package main.java.me.avankziar.tt.spigot.cmd.tt.group;

import java.io.IOException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.tt.general.ChatApi;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentConstructor;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentModule;
import main.java.me.avankziar.tt.spigot.handler.GroupHandler;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupData;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupPlayerAffiliation;

public class ARGGroup_SetDescription extends ArgumentModule
{	
	public ARGGroup_SetDescription(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
	}

	//tt group setdescription <words...>
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
		StringBuilder sb = new StringBuilder();
		for(int i = 2; i < args.length; i++)
		{
			if(i != 1 && i < args.length)
			{
				sb.append(" ");
			}
			sb.append(args[i]);
		}
		String desc = sb.toString();
		if(!player.getUniqueId().toString().equals(gd.getGrandmasterUUID().toString()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.SetDescription.OnlyGrandmasterCanSetNewDescription")));
			return;
		}
		gd.setDisplayDescription(desc);
		GroupHandler.updateGroup(gd);
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.SetDescription.Set")));
		player.sendMessage(ChatApi.tl(desc));
	}
}