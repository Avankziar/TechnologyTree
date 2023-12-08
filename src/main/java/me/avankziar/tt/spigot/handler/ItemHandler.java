package main.java.me.avankziar.tt.spigot.handler;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.cmdtree.BaseConstructor;

public class ItemHandler
{
	private static TT plugin = BaseConstructor.getPlugin();
	
	public static void dropItem(ItemStack is, Player player, Location loc)
	{
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				Item it = player.getWorld().dropItem(loc != null ? loc : player.getLocation(), is);
				ItemHandler.addItemToTask(it, player.getUniqueId());
			}
		}.runTask(plugin);
	}
	
	private static void addItemToTask(Item item, UUID owner)
	{
		item.setOwner(owner);
		final UUID uuid = item.getUniqueId();
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				new BukkitRunnable()
				{
					@Override
					public void run()
					{
						Entity e = Bukkit.getEntity(uuid);
						if(e == null || !(e instanceof Item))
						{
							return;
						}
						Item i = (Item) e;
						i.setOwner(null);
					}
				}.runTask(plugin);
			}
		}.runTaskLaterAsynchronously(plugin, new ConfigHandler().loseDropItemOwnershipAfterTime()*20L);
	}
}