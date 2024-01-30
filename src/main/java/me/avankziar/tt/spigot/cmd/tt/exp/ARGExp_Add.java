package main.java.me.avankziar.tt.spigot.cmd.tt.exp;

import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.ifh.general.assistance.ChatApi;
import main.java.me.avankziar.tt.spigot.assistance.MatchApi;
import main.java.me.avankziar.tt.spigot.assistance.Utility;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentConstructor;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentModule;
import main.java.me.avankziar.tt.spigot.handler.PlayerHandler;
import main.java.me.avankziar.tt.spigot.objects.mysql.PlayerData;

public class ARGExp_Add extends ArgumentModule
{	
	public ARGExp_Add(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
	}

	//tt exp add <playername> <double>
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		String p2 = args[2];
		UUID uuid = Utility.convertNameToUUID(p2);
		if(uuid == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoPlayerExist")));
			return;
		}
		String d = args[3];
		if(!MatchApi.isDouble(d))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoDouble")
					.replace("%value%", d)));
			return;
		}
		double exp = Double.valueOf(d);
		PlayerData pd = PlayerHandler.getPlayer(uuid);
		String txt = null;
		if(exp < 0)
		{
			if(pd.getActualTTExp()+exp < 0)
			{
				txt = ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Exp.ToZero")
						.replace("%player%", p2)
						.replace("%value%", String.valueOf(exp*-1))
						.replace("%value2%", String.valueOf(pd.getActualTTExp()))
						);
				pd.setActualTTExp(0);
			} else
			{
				pd.setActualTTExp(pd.getActualTTExp()+exp);
				txt = ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Exp.Reduce")
						.replace("%player%", p2)
						.replace("%value%", String.valueOf(exp))
						.replace("%value2%", String.valueOf(pd.getActualTTExp()))
						);
			}
		} else
		{
			pd.setActualTTExp(pd.getActualTTExp()+exp);
			pd.setTotalReceivedTTExp(pd.getTotalReceivedTTExp()+exp);
			txt = ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Exp.Add")
					.replace("%player%", p2)
					.replace("%value%", String.valueOf(exp))
					.replace("%value2%", String.valueOf(pd.getActualTTExp()))
					);
		}
		player.sendMessage(txt);
		if(Bukkit.getPlayer(uuid) != null)
		{
			Bukkit.getPlayer(uuid).sendMessage(txt);
		} else
		{
			if(plugin.getMessageToBungee() != null)
			{
				plugin.getMessageToBungee().sendMessage(uuid, txt);
			}
		}
		PlayerHandler.updatePlayer(pd);
	}
}