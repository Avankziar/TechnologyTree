package main.java.me.avankziar.tt.spigot.listener.reward;

import org.bukkit.GameMode;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.ItemHandler;
import main.java.me.avankziar.tt.spigot.handler.RewardHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;

public class AnvilListener implements Listener
{	
	@EventHandler
	public void onAnvil(InventoryClickEvent event)
	{
		if(event.isCancelled()
				|| !(event.getWhoClicked() instanceof Player)
				|| event.getWhoClicked().getGameMode() == GameMode.CREATIVE
				|| event.getWhoClicked().getGameMode() == GameMode.SPECTATOR
				|| event.getClickedInventory() == null
				|| event.getSlotType() != SlotType.RESULT
				|| event.getClickedInventory().getType() != InventoryType.ANVIL
				|| !(event.getClickedInventory() instanceof AnvilInventory))
		{
			return;
		}
		Player player = (Player) event.getWhoClicked();
		AnvilInventory ai = (AnvilInventory) event.getClickedInventory();
		ItemStack base = ai.getContents()[0];
		ItemStack add = ai.getContents()[1];
		ItemStack result = ai.getContents()[2];
		if(add == null
				&& base.hasItemMeta()
				&& (	(base.getItemMeta().hasDisplayName()
						&& !base.getItemMeta().getDisplayName().equals(ai.getRenameText())
						)
						|| !base.getItemMeta().hasDisplayName())
				)
		{
			if(!EnumHandler.isEventActive(EventType.RENAMING))
			{
				return;
			}
			for(ItemStack is : RewardHandler.getDrops(player, EventType.RENAMING, result.getType(), null, false))
			{
				Item it = player.getWorld().dropItem(player.getLocation(), is);
				ItemHandler.addItemToTask(it, player.getUniqueId());
			}
			RewardHandler.rewardPlayer(player.getUniqueId(), EventType.RENAMING, result.getType(), null, 1);
		} else
		{
			if(!EnumHandler.isEventActive(EventType.COLD_FORGING))
			{
				return;
			}
			for(ItemStack is : RewardHandler.getDrops(player, EventType.COLD_FORGING, result.getType(), null, false))
			{
				Item it = player.getWorld().dropItem(player.getLocation(), is);
				ItemHandler.addItemToTask(it, player.getUniqueId());
			}
			RewardHandler.rewardPlayer(player.getUniqueId(), EventType.COLD_FORGING, result.getType(), null, 1);
		}
	}
}