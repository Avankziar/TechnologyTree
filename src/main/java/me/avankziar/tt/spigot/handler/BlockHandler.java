package main.java.me.avankziar.tt.spigot.handler;

import java.util.LinkedHashMap;
import java.util.UUID;

import org.bukkit.Location;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.cmdtree.BaseConstructor;

public class BlockHandler
{
	public enum BlockType
	{
		BLASTFURNACE, CAMPFIRE, FURNACE;
	}
	
	private static TT plugin = BaseConstructor.getPlugin();
	
	public static LinkedHashMap<UUID, String> atTheMomentAccessBlockMap = new LinkedHashMap<>(); //Blöcke die man gerade angeklickt hat., BlockType, Loc als String
	
	public static String getLocationText(Location l)
	{
		return l.getWorld().getName()
				+":"+l.getBlockX()
				+":"+l.getBlockY()
				+":"+l.getBlockZ();
	}
}