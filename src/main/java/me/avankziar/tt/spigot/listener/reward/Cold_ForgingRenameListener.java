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
import main.java.me.avankziar.tt.spigot.objects.ToolType;

public class Cold_ForgingRenameListener implements Listener
{
	final private static EventType RN = EventType.RENAMING;
	final private static EventType CF = EventType.COLD_FORGING;
	@EventHandler
	public void onAnvil(InventoryClickEvent event)
	{
		if(event.isCancelled()
				|| !(event.getClickedInventory() instanceof AnvilInventory)
				|| event.getClickedInventory() == null
				|| event.getClickedInventory().getType() != InventoryType.ANVIL
				|| event.getSlotType() != SlotType.RESULT
				|| !(event.getWhoClicked() instanceof Player)
				|| event.getWhoClicked().getGameMode() == GameMode.CREATIVE
				|| event.getWhoClicked().getGameMode() == GameMode.SPECTATOR
				)
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
			if(!EnumHandler.isEventActive(RN))
			{
				return;
			}
			for(ItemStack is : RewardHandler.getDrops(player, RN, ToolType.ALL, result.getType(), null))
			{
				Item it = player.getWorld().dropItem(player.getLocation(), is);
				ItemHandler.addItemToTask(it, player.getUniqueId());
			}
			RewardHandler.rewardPlayer(player.getUniqueId(), RN, ToolType.ALL, result.getType(), null, 1);
		} else
		{
			if(!EnumHandler.isEventActive(CF))
			{
				return;
			}
			for(ItemStack is : RewardHandler.getDrops(player, CF, ToolType.ALL, result.getType(), null))
			{
				Item it = player.getWorld().dropItem(player.getLocation(), is);
				ItemHandler.addItemToTask(it, player.getUniqueId());
			}
			RewardHandler.rewardPlayer(player.getUniqueId(), CF, ToolType.ALL, result.getType(), null, 1);
		}
	}
}