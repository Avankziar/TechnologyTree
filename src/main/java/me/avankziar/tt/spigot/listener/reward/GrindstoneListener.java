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
import main.java.me.avankziar.tt.spigot.objects.ToolType;

public class GrindstoneListener implements Listener
{
	final private static EventType GR = EventType.GRINDING;
	@EventHandler
	public void onGrindstone(InventoryClickEvent event)
	{
		if(event.isCancelled()
				|| event.getClickedInventory() == null
				|| event.getClickedInventory().getType() != InventoryType.GRINDSTONE
				|| !(event.getClickedInventory() instanceof GrindstoneInventory)
				|| !(event.getWhoClicked() instanceof Player)
				|| event.getWhoClicked().getGameMode() == GameMode.CREATIVE
				|| event.getWhoClicked().getGameMode() == GameMode.SPECTATOR
				|| event.getSlotType() != SlotType.RESULT
				|| !EnumHandler.isEventActive(GR))
		{
			return;
		}
		Player player = (Player) event.getWhoClicked();
		GrindstoneInventory ai = (GrindstoneInventory) event.getClickedInventory();
		ItemStack result = ai.getContents()[2];
		for(ItemStack is : RewardHandler.getDrops(player, GR, ToolType.ALL, result.getType(), null))
		{
			Item it = player.getWorld().dropItem(player.getLocation(), is);
			ItemHandler.addItemToTask(it, player.getUniqueId());
		}
		RewardHandler.rewardPlayer(player.getUniqueId(), GR, ToolType.ALL, result.getType(), null, 1);
	}
}