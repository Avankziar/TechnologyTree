package main.java.me.avankziar.tt.spigot.handler;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.RewardType;

public class EnumHandler
{
	public static ArrayList<String> activeEvents = new ArrayList<>();
	
	public static void init()
	{
		activeEvents.clear();
		activeEvents = (ArrayList<String>) new ConfigHandler().activeEvents();
	}
	
	public static String getName(String e)
	{
		String[] s = e.split("_");
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < s.length; i++)
		{
			String a = s[i];
			sb.append(a.substring(0, 1)+a.substring(1).toLowerCase());
			if(i+1 < s.length)
			{
				sb.append("_");
			}
		}
		return sb.toString();
	}
	
	public static String getName(Material e)
	{
		return getName(e.toString());
	}
	
	public static String getName(EntityType e)
	{
		return getName(e.toString());
	}
	
	public static String getName(EventType e)
	{
		return getName(e.toString());
	}
	
	public static String getName(RewardType e)
	{
		return getName(e.toString());
	}
	
	public static boolean isEventActive(EventType eventType)
	{
		return activeEvents.contains(eventType.toString());
	}
}
