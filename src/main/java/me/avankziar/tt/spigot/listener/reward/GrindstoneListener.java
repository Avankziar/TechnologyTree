package main.java.me.avankziar.tt.spigot.listener.reward;

import org.bukkit.GameMode;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.GrindstoneInventory;
import org.bukkit.inventory.ItemStack;

import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.ItemHandler;
import main.java.me.avankziar.tt.spigot.handler.RewardHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;

public class GrindstoneListener implements Listener
{
	@EventHandler
	public void onGrindstone(InventoryClickEvent event)
	{
		if(event.isCancelled()
				|| !(event.getWhoClicked() instanceof Player)
				|| event.getWhoClicked().getGameMode() == GameMode.CREATIVE
				|| event.getWhoClicked().getGameMode() == GameMode.SPECTATOR
				|| event.getClickedInventory() == null
				|| event.getSlotType() != SlotType.RESULT
				|| event.getClickedInventory().getType() != InventoryType.GRINDSTONE
				|| !(event.getClickedInventory() instanceof GrindstoneInventory)
				|| !EnumHandler.isEventActive(EventType.GRINDING))
		{
			return;
		}
		Player player = (Player) event.getWhoClicked();
		GrindstoneInventory ai = (GrindstoneInventory) event.getClickedInventory();
		ItemStack result = ai.getContents()[2];
		for(ItemStack is : RewardHandler.getDrops(player, EventType.GRINDING, result.getType(), null, false))
		{
			Item it = player.getWorld().dropItem(player.getLocation(), is);
			ItemHandler.addItemToTask(it, player.getUniqueId());
		}
		RewardHandler.rewardPlayer(player.getUniqueId(), EventType.GRINDING, result.getType(), null, 1);
	}
}