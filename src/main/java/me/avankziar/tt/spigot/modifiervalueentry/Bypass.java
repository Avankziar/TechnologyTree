package main.java.me.avankziar.tt.spigot.modifiervalueentry;

import java.util.LinkedHashMap;

import main.java.me.avankziar.tt.spigot.cmdtree.BaseConstructor;

public class Bypass
{
	public enum Permission
	{
		//Here Condition and BypassPermission.
		SEE_MAIN_CATEGORYS,
		SEE_SUB_CATEGORYS,
		GROUP_CREATE,
		GROUP_INCREASELEVEL,
		GROUP_SETGRANDMASTER,
		GROUP_INFO;
		
		public String getValueLable()
		{
			return BaseConstructor.getPlugin().pluginName.toLowerCase()+"-"+this.toString().toLowerCase();
		}
	}
	
	private static LinkedHashMap<Bypass.Permission, String> mapPerm = new LinkedHashMap<>();
	
	public static void set(Bypass.Permission bypass, String perm)
	{
		mapPerm.put(bypass, perm);
	}
	
	public static String get(Bypass.Permission bypass)
	{
		return mapPerm.get(bypass);
	}
	
	public enum Counter
	{
		//Here BonusMalus and CountPermission Things
		REGISTER_BLOCK_(true);
		
		private boolean forPermission;
		
		Counter()
		{
			this.forPermission = true;
		}
		
		Counter(boolean forPermission)
		{
			this.forPermission = forPermission;
		}
	
		public boolean forPermission()
		{
			return this.forPermission;
		}
		
		public String getModification()
		{
			return BaseConstructor.getPlugin().pluginName.toLowerCase()+"-"+this.toString().toLowerCase();
		}
	}
	
	private static LinkedHashMap<Bypass.Counter, String> mapCount = new LinkedHashMap<>();
	
	public static void set(Bypass.Counter bypass, String perm)
	{
		mapCount.put(bypass, perm);
	}
	
	public static String get(Bypass.Counter bypass)
	{
		return mapCount.get(bypass);
	}
}