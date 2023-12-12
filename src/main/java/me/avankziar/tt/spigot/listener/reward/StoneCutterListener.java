package main.java.me.avankziar.tt.spigot.listener.reward;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.StonecutterInventory;

import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;

public class StoneCutterListener implements Listener
{
	final private static EventType SC = EventType.HARMING;

	@EventHandler
	public void onStoneCutter(InventoryClickEvent event)
	{
		if(event.getClickedInventory() == null
				|| !(event.getClickedInventory() instanceof StonecutterInventory)
				|| !EnumHandler.isEventActive(SC))
		{
			return;
		}
	}

}
