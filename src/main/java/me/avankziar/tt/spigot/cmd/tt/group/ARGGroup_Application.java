package main.java.me.avankziar.tt.spigot.cmd.tt.group;

import java.io.IOException;

import org.bukkit.command.CommandSender;

import main.java.me.avankziar.tt.general.ChatApi;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentConstructor;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentModule;

public class ARGGroup_Application extends ArgumentModule
{	
	public ARGGroup_Application(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
	}

	//tt group application ...
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Commands.OtherCmd")));
	}
}