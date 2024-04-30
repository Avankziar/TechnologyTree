package main.java.me.avankziar.tt.spigot.objects.ram.misc;

public class SwitchMode
{
	public String name;
	public boolean ttexp = false;
	public boolean vanillaexp = false;
	public boolean money = false;
	public boolean drops = false;
	public boolean cmd = false;
	public String permission = null;
	
	public SwitchMode(String name, boolean ttexp, boolean vanillaexp, boolean money, boolean drops, boolean cmd, String permission) 
	{
		this.name = name;
		this.ttexp = ttexp;
		this.vanillaexp = vanillaexp;
		this.money = money;
		this.drops = drops;
		this.cmd = cmd;
		this.permission = permission;
	}
	
	public SwitchMode(String name) 
	{
		this.name = name;
	}
}