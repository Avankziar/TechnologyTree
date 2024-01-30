package main.java.me.avankziar.tt.spigot.cmd.tt.group;

import java.io.IOException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.ifh.general.assistance.ChatApi;
import main.java.me.avankziar.tt.spigot.assistance.MatchApi;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentConstructor;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentModule;
import main.java.me.avankziar.tt.spigot.handler.GroupHandler;
import main.java.me.avankziar.tt.spigot.handler.PlayerHandler;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupData;
import main.java.me.avankziar.tt.spigot.objects.mysql.PlayerData;

public class ARGGroup_Donate extends ArgumentModule
{	
	public ARGGroup_Donate(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
	}

	//tt group donate <amount(Double)> [groupname]
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		if(!MatchApi.isDouble(args[2]))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoDouble")
					.replace("%value%", args[2])));
			return;
		}
		double donate = Double.valueOf(args[2]);
		if(!MatchApi.isPositivNumber(donate))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("IsNegativ")
					.replace("%value%", args[2])));
			return;
		}
		PlayerData pd = PlayerHandler.getPlayer(player.getUniqueId());
		if(pd.getActualTTExp() < donate)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("GuiHandler.Technology.NotEnoughTTExp")
					.replace("%value%", args[2])));
			return;
		}
		String gn = null;
		if(args.length == 4)
		{
			gn = args[3];
		}
		GroupData gd = gn == null ? GroupHandler.getGroup(player) : GroupHandler.getGroup(gn);
		if(gd == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.GroupDontExist")
					.replace("%group%", "N.A.")));
			return;
		}
		pd.setActualTTExp(pd.getActualTTExp()-donate);
		PlayerHandler.updatePlayer(pd);
		String txt = plugin.getYamlHandler().getLang().getString("Commands.Group.Donate.Donated")
				.replace("%player%", player.getName())
				.replace("%group%", gd.getGroupName())
				.replace("%ttexp%", String.valueOf(donate));
		GroupHandler.sendMembersText(gd.getGroupName(), txt, player.getUniqueId());
	}
}