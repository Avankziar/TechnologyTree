package main.java.me.avankziar.tt.spigot.handler;

import java.util.LinkedHashMap;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.SwitchMode;

public class SwitchModeHandler
{
	private static TT plugin;
	public static boolean isActive = false;
	public static LinkedHashMap<String, SwitchMode> switchMode = new LinkedHashMap<>();
	
	public static void init()
	{
		plugin = TT.getPlugin();
		if(!plugin.getYamlHandler().getConfig().getBoolean("SwitchMode.isActive", false))
		{
			return;
		}
		isActive = true;
		for(String s : plugin.getYamlHandler().getConfig().getStringList("SwitchMode.ModeList"))
		{
			String[] ss = s.split(":");
			if(ss.length < 2 || ss.length > 3)
			{
				continue;
			}
			if(ss[0].equals("null"))
			{
				continue;
			}
			SwitchMode swm = new SwitchMode(ss[0]);
			String[] sb = ss[1].split(",");
			for(String ssb : sb)
			{
				switch(ssb)
				{
				default:
					continue;
				case "ttexp":
					swm.ttexp = true; break;
				case "vanillaexp":
					swm.vanillaexp = true; break;
				case "money":
					swm.money = true; break;
				case "drops":
					swm.drops = true; break;
				case "cmd":
					swm.cmd = true; break;
				}
			}
			if(ss.length == 3)
			{
				swm.permission = ss[2];
			}
			SwitchModeHandler.switchMode.put(swm.name, swm);
		}
	}
	
	public static SwitchMode getSwitchMode(String switchMode)
	{
		SwitchMode swm = SwitchModeHandler.switchMode.get(switchMode);
		return swm == null ? new SwitchMode("null", false, false, false, false,false, null) : swm;
	}
}