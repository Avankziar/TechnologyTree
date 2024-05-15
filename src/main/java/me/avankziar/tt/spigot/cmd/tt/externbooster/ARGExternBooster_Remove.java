package main.java.me.avankziar.tt.spigot.cmd.tt.externbooster;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.ifh.general.assistance.ChatApi;
import main.java.me.avankziar.tt.spigot.assistance.MatchApi;
import main.java.me.avankziar.tt.spigot.assistance.Utility;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentConstructor;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentModule;
import main.java.me.avankziar.tt.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.tt.spigot.handler.GroupHandler;
import main.java.me.avankziar.tt.spigot.objects.mysql.ExternBooster;
import main.java.me.avankziar.tt.spigot.objects.mysql.GroupPlayerAffiliation;

public class ARGExternBooster_Remove extends ArgumentModule
{	
	public ARGExternBooster_Remove(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
	}

	//tt externbooster remove <id>
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		String id = args[2];
		if(!MatchApi.isInteger(id))
		{
			sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoNumber").replace("%value%", id)));
			return;
		}
		if(!MatchApi.isPositivNumber(Integer.parseInt(id)))
		{
			sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("IsNegativ").replace("%value%", id)));
			return;
		}
		ExternBooster ex = (ExternBooster) plugin.getMysqlHandler().getData(Type.EXTERN_BOOSTER, "`id` = ?", id);
		if(ex == null)
		{
			sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.ExternBooster.Remove.NoExist")
					.replace("%id%", id)));
			return;
		}
		sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.ExternBooster.Remove.Removed")
				.replace("%id%", id)
				.replace("%name%", ex.getName())));
		update(ex);
	}
	
	private void update(ExternBooster ex)
	{
		boolean allUpdate = false;
		ArrayList<UUID> playerUUIDToUpdate = new ArrayList<>();
		switch(ex.getPlayerAssociatedType())
		{
		case GLOBAL:
			allUpdate = true;
			break;
		case GROUP:
			if(ex.getGroupname() == null)
			{
				break;
			}
			ArrayList<GroupPlayerAffiliation> gpas = GroupHandler.getAllAffiliateGroup(ex.getGroupname());
			if(gpas == null || gpas.size() <= 0)
			{
				break;
			}
			for(GroupPlayerAffiliation gpa : gpas)
			{
				if(playerUUIDToUpdate.contains(gpa.getPlayerUUID()))
				{
					continue;
				}
				playerUUIDToUpdate.add(gpa.getPlayerUUID());
			}
			break;
		case SOLO:
			if(ex.getPlayerUUIDText() == null || playerUUIDToUpdate.contains(ex.getPlayerUUID()))
			{
				break;
			}
			playerUUIDToUpdate.add(ex.getPlayerUUID());
			break;
		}
		String booster = "!~booster~!"+ex.getId()+"!-!"+ex.getName();
		plugin.getMysqlHandler().deleteData(Type.EXTERN_BOOSTER, "`id` = ?", ex.getId());
		if(allUpdate)
		{
			ArrayList<UUID> uuids = new ArrayList<>();
			if(plugin.getBungeeOnlinePlayers() != null)
			{
				for(UUID uuid : plugin.getBungeeOnlinePlayers().getBungeeOnlinePlayers().keySet())
				{
					uuids.add(uuid);
				}
				Utility.toUpdate(playerUUIDToUpdate, booster);
			} else
			{
				for(Player p : Bukkit.getOnlinePlayers())
				{
					uuids.add(p.getUniqueId());
				}
				Utility.toUpdate(playerUUIDToUpdate, booster);
			}
		} else
		{
			Utility.toUpdate(playerUUIDToUpdate, booster);
		}
	}
}