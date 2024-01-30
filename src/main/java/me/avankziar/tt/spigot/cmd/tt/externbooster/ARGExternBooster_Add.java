package main.java.me.avankziar.tt.spigot.cmd.tt.externbooster;

import java.io.IOException;

import org.bukkit.command.CommandSender;

import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentConstructor;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentModule;

public class ARGExternBooster_Add extends ArgumentModule
{	
	public ARGExternBooster_Add(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
	}

	//tt externbooster add <ExternBoosterName> <factor(double)> <time(00d-00H-00m)> <PlayerAssociatedType> [playername/group]
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		
	}
}