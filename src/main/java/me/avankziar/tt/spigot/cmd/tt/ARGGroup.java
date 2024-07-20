package main.java.me.avankziar.tt.spigot.cmd.tt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.tt.general.ChatApi;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentConstructor;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentModule;
import main.java.me.avankziar.tt.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.tt.spigot.handler.GroupHandler;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupData;

public class ARGGroup extends ArgumentModule
{	
	public ARGGroup(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
	}

	//tt group ... 
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		int groupamount = GroupHandler.getTotalGroupAmount();
		int memberamount = GroupHandler.getTotalGroupPlayerAffiliate();
		ArrayList<Integer> numa = new ArrayList<>(); 
		for(GroupData gd : GroupData.convert(plugin.getMysqlHandler().getFullList(Type.GROUP_DATA, "`id` ASC", "`id` > ?", 1)))
		{
			numa.add(GroupHandler.getGroupMemberAmount(gd.getGroupName()));
		}
		double average = groupamount == 0 ? 0 : memberamount/groupamount;
		double median = getMedian(numa.toArray(new Integer[numa.size()]));
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.Groups.Headline")));
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.Groups.GroupAmount")
				.replace("%amount%", String.valueOf(groupamount))));
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.Groups.PlayerAmount")
				.replace("%amount%", String.valueOf(memberamount))));
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.Groups.Median")
				.replace("%average%", String.valueOf(average))
				.replace("%median%", String.valueOf(median))));
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.Groups.Bottomline")));
		
	}
	
	public double getMedian(Integer[] numArray)
	{
		Arrays.sort(numArray);
		double median;
		if(numArray.length == 0)
		{
			median = 0;
		} else if (numArray.length % 2 == 0)
		{
		    median = ((double)numArray[numArray.length/2] + (double)numArray[numArray.length/2 - 1])/2;
		} else
		{
			median = (double) numArray[numArray.length/2];
		}
	
		return median;
	}
}