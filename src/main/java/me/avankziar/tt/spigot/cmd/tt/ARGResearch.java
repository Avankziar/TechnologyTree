package main.java.me.avankziar.tt.spigot.cmd.tt;

import java.io.IOException;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.ifh.general.assistance.ChatApi;
import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.assistance.Utility;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentConstructor;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentModule;
import main.java.me.avankziar.tt.spigot.handler.CatTechHandler;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.Technology;
import net.md_5.bungee.api.chat.ClickEvent;

public class ARGResearch extends ArgumentModule
{	
	public ARGResearch(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
	}

	//tt research <Technology> [Groupname/Playername] 
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		String tech = args[1];
		Technology t = CatTechHandler.technologyMapSolo.get(tech);
		if(t == null)
		{
			t = CatTechHandler.technologyMapGlobal.get(tech);
			if(t == null)
			{
				t = CatTechHandler.technologyMapGroup.get(tech);
			}
		}
		if(t == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.TechInfo.TechNotFound")));
			return;
		}
		switch(t.getPlayerAssociatedType())
		{
		case GLOBAL:
		case GROUP:
		case SOLO:
			if(args.length < 3)
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.TechInfo.TechNotFound")));
				return;
			}
			String p2 = args[2];
			UUID uuid = Utility.convertNameToUUID(p2);
			if(uuid == null)
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoPlayerExist")));
				return;
			}
		}
		
		
		
		
	}
}