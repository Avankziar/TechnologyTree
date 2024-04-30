package main.java.me.avankziar.tt.spigot.cmd.tt;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.tt.general.ChatApi;
import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.assistance.TimeHandler;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentConstructor;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentModule;
import main.java.me.avankziar.tt.spigot.cmdtree.CommandExecuteType;
import main.java.me.avankziar.tt.spigot.cmdtree.CommandSuggest;
import main.java.me.avankziar.tt.spigot.database.MysqlHandler;
import main.java.me.avankziar.tt.spigot.handler.ConfigHandler;
import main.java.me.avankziar.tt.spigot.handler.PlayerHandler;
import main.java.me.avankziar.tt.spigot.handler.SwitchModeHandler;
import main.java.me.avankziar.tt.spigot.modifiervalueentry.Bypass;
import main.java.me.avankziar.tt.spigot.modifiervalueentry.Bypass.Permission;
import main.java.me.avankziar.tt.spigot.objects.mysql.PlayerData;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.SwitchMode;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ARGSwitchMode extends ArgumentModule
{	
	public ARGSwitchMode(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
	}

	//tt switchmode [switchmode|null] [playername|-a]
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		if(!SwitchModeHandler.isActive)
		{
			sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.SwitchMode.NotActive")));
			return;
		}
		if(args.length == 1)
		{
			switchmodelist(sender);
			return;
		}
		//Wenn [playername] == -a ist, sollen alle Spieler in der Datenbank auf diesen SwitchMode gesetzt werden.
		if(args.length == 2)
		{
			//If switchmode is 'null', than the switchmode will be reset and the first switchmode who has the player has permission for will be picked.
			if(!(sender instanceof Player))
			{
				plugin.getLogger().info("Cmd is only for Player!");
				return;
			}
			Player player = (Player) sender;
			PlayerData pd = PlayerHandler.getPlayer(player.getUniqueId());
			if(pd.getSwitchModeCooldown() > System.currentTimeMillis())
			{
				String time = TimeHandler.getDateTime(pd.getSwitchModeCooldown());
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.SwitchMode.OnCooldown")
						.replace("%time%", time)));
				return;
			}
			SwitchMode sm = null;
			if("null".equals(args[1]))
			{
				sm = PlayerHandler.getSwitchMode(player);
			} else
			{
				sm = SwitchModeHandler.getSwitchMode(args[1]);
			}
			if(sm == null || "null".equals(sm.name))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.SwitchMode.NoFound")));
				return;
			}
			if(pd.getSwitchMode().equals(sm.name))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.SwitchMode.HaveAlready")));
				return;
			}
			if(sm.permission != null)
			{
				if(!player.hasPermission(sm.permission))
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.SwitchMode.NoRights")));
					return;
				}
			}
			pd.setSwitchMode(sm.name);
			long expi = System.currentTimeMillis();
			if(player.hasPermission(new ConfigHandler().getSwitchModeCooldownVIPPermission()))
			{
				expi += TimeHandler.getTiming(new ConfigHandler().getSwitchModeCooldownVIP());
			} else
			{
				expi += TimeHandler.getTiming(new ConfigHandler().getSwitchModeCooldownNormalPlayer());
			}
			pd.setSwitchModeCooldown(expi);
			PlayerHandler.updatePlayer(pd);
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.SwitchMode.Set").replace("%name%", sm.name)));
			return;
		}
		if(args.length == 3)
		{
			if(!sender.hasPermission(Bypass.get(Permission.SWITCHMODE_OTHERPLAYER)))
			{
				sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoPermission")));
				return;
			}
			String swm = args[1];
			if("-a".equals(args[2]) && "null".equals(swm))
			{
				sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.SwitchMode.NotSwitchModeNullForAll")));
				return;
			}
			SwitchMode sm = SwitchModeHandler.getSwitchMode(swm);
			if(sm == null || "null".equals(sm.name))
			{
				sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.SwitchMode.NoFound")));
				return;
			}
			if("-a".equals(args[2]))
			{
				updateAll(swm);
				sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.SwitchMode.SetAll")
						.replace("%name%", sm.name)));
				return;
			}
			String othername = args[2];
			PlayerData pd = PlayerHandler.getPlayer(othername);
			if(pd == null)
			{
				sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoPlayerExist")));
				return;
			}
			pd.setSwitchMode(sm.name);
			PlayerHandler.updatePlayer(pd);
			sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.SwitchMode.SetPlayer")
					.replace("%player%", othername)
					.replace("%name%", sm.name)));
			return;
		}
	}
	
	private void switchmodelist(CommandSender sender)
	{
		if(!(sender instanceof Player))
		{
			plugin.getLogger().info("Cmd is only for Player!");
			return;
		}
		Player player = (Player) sender;
		if(plugin.getYamlHandler().getConfig().get("SwitchMode.ModeList") == null 
				|| plugin.getYamlHandler().getConfig().getStringList("SwitchMode.ModeList").isEmpty())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.SwitchMode.NoSwitchMode")));
			return;
		}
		ArrayList<BaseComponent> al = new ArrayList<>();
		for(SwitchMode sm : SwitchModeHandler.switchMode.values())
		{
			if(sm.permission != null)
			{
				if(!player.hasPermission(sm.permission))
				{
					continue;
				}
			}
			if(!al.isEmpty())
			{
				al.add(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Commands.SwitchMode.Comma")));
			}
			StringBuilder sb = new StringBuilder();
			sb.append(plugin.getYamlHandler().getLang().getString("Commands.SwitchMode.Type.Headline"));
			if(sm.ttexp || sm.vanillaexp || sm.money || sm.drops || sm.cmd)
			{
				sb.append("~!~");
			}
			if(sm.ttexp)
			{
				sb.append(plugin.getYamlHandler().getLang().getString("Commands.SwitchMode.Type.ttexp"));
				if(sm.vanillaexp || sm.money || sm.drops || sm.cmd)
				{
					sb.append("~!~");
				}
			}
			if(sm.vanillaexp)
			{
				sb.append(plugin.getYamlHandler().getLang().getString("Commands.SwitchMode.Type.vanillaexp"));
				if(sm.money || sm.drops || sm.cmd)
				{
					sb.append("~!~");
				}
			}
			if(sm.money)
			{
				sb.append(plugin.getYamlHandler().getLang().getString("Commands.SwitchMode.Type.money"));
				if(sm.drops || sm.cmd)
				{
					sb.append("~!~");
				}
			}
			if(sm.drops)
			{
				sb.append(plugin.getYamlHandler().getLang().getString("Commands.SwitchMode.Type.drops"));
				if(sm.cmd)
				{
					sb.append("~!~");
				}
			}
			if(sm.cmd)
			{
				sb.append(plugin.getYamlHandler().getLang().getString("Commands.SwitchMode.Type.cmd"));
			}
			al.add(ChatApi.apiChat(plugin.getYamlHandler().getLang().getString("Commands.SwitchMode.Name").replace("%name%", sm.name),
					ClickEvent.Action.SUGGEST_COMMAND, CommandSuggest.get(CommandExecuteType.TT_SWITCHMODE)+sm.name,
					HoverEvent.Action.SHOW_TEXT, sb.toString()));
		}
		PlayerData pd = PlayerHandler.getPlayer(player.getUniqueId());
		TextComponent tx = ChatApi.tc("");
		tx.setExtra(al);
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.SwitchMode.ListHeadline")));
		player.spigot().sendMessage(tx);
		if(pd.getSwitchModeCooldown() > System.currentTimeMillis())
		{
			String time = TimeHandler.getDateTime(pd.getSwitchModeCooldown());
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.SwitchMode.OnCooldown")
					.replace("%time%", time)));
		}
		
	}
	
	public boolean updateAll(String switchMode)
	{
		try (Connection conn = plugin.getMysqlSetup().getConnection();)
		{
			String sql = "UPDATE `" + MysqlHandler.Type.PLAYERDATA.getValue()
				+ "` SET `switchmode` = ?"
				+ " WHERE 1";
			PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setString(1, switchMode);
			int u = ps.executeUpdate();
			MysqlHandler.addRows(MysqlHandler.QueryType.UPDATE, u);
			return true;
		} catch (SQLException e)
		{
			TT.log.log(Level.WARNING, "SQLException! Could not update a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return false;
	}
}