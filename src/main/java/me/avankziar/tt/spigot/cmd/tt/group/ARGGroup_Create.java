package main.java.me.avankziar.tt.spigot.cmd.tt.group;

import java.io.IOException;
import java.util.HashMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.ifh.general.assistance.ChatApi;
import main.java.me.avankziar.ifh.general.math.MathFormulaParser;
import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentConstructor;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentModule;
import main.java.me.avankziar.tt.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.tt.spigot.handler.GroupHandler;
import main.java.me.avankziar.tt.spigot.handler.PlayerHandler;
import main.java.me.avankziar.tt.spigot.handler.GroupHandler.Position;
import main.java.me.avankziar.tt.spigot.modifiervalueentry.ModifierValueEntry;
import main.java.me.avankziar.tt.spigot.modifiervalueentry.Bypass.Permission;
import main.java.me.avankziar.tt.spigot.objects.mysql.PlayerData;

public class ARGGroup_Create extends ArgumentModule
{
	private TT plugin;
	
	public ARGGroup_Create(TT plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	//tt group create <Groupname>
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		if(GroupHandler.isInGroup(player.getUniqueId()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.YouAreInAGroup")));
			return;
		}
		String gn = args[2];
		if(plugin.getMysqlHandler().exist(Type.GROUP_DATA, "`group_name` = ?", gn))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.Create.GroupNameAlreadyExist")
					.replace("%name%", gn)));
			return;
		}
		String ttExpCost = plugin.getYamlHandler().getConfig().getString("Group.Creation.Cost.TTExp");
		HashMap<String, Double> map = new HashMap<>();
		map.put(PlayerHandler.SOLORESEARCHEDTOTALTECH, Double.valueOf(PlayerHandler.getTotalResearchSoloTech(player)));
		map.put(PlayerHandler.GLOBALRESEARCHEDTOTALTECH, Double.valueOf(PlayerHandler.getTotalResearchGlobalTech()));
		map.put(PlayerHandler.GROUPTOTALAMOUNT, Double.valueOf(GroupHandler.getTotalGroupAmount()));
		double ttExp = 0;
		if(!ModifierValueEntry.hasPermission(player, Permission.GROUP_CREATE))
		{
			ttExp = new MathFormulaParser().parse(ttExpCost, map);
		}
		if(ttExp <= 0)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.Create.GroupCreatedBypass")));
		} else
		{
			PlayerData pd = PlayerHandler.getPlayer(player.getUniqueId());	
			if(ttExp > pd.getActualTTExp())
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.Create.NotEnoughTTExp")
						.replace("%cost%", String.valueOf(ttExp))));
				return;
			}
			pd.setActualTTExp(pd.getActualTTExp()-ttExp);
			PlayerHandler.updatePlayer(pd);
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.Group.Create.GroupCreated")));
		}
		GroupHandler.createGroup(player, gn);
		GroupHandler.createAffiliateGroup(player.getUniqueId(), ttExpCost, Position.MASTER, true);
	}
}