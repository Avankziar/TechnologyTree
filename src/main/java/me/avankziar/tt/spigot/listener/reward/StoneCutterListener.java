package main.java.me.avankziar.tt.spigot.listener.reward;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.StonecutterInventory;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.cmd.tt.ARGCheckEventAction;
import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.ItemHandler;
import main.java.me.avankziar.tt.spigot.handler.RecipeHandler;
import main.java.me.avankziar.tt.spigot.handler.RewardHandler;
import main.java.me.avankziar.tt.spigot.handler.RecipeHandler.RecipeType;
import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.ToolType;

public class StoneCutterListener implements Listener
{
	final private static EventType SC = EventType.STONECUTTING;

	@EventHandler
	public void onStoneCutter(InventoryClickEvent event)
	{
		if(event.getClickedInventory() == null
				|| event.getCurrentItem() == null
				|| !(event.getClickedInventory() instanceof StonecutterInventory)
				|| event.getSlotType() != SlotType.RESULT
				|| !EnumHandler.isEventActive(SC))
		{
			ARGCheckEventAction.checkEventAction((Player) event.getWhoClicked(), "STONECUTTING:RETURN",
					SC, ToolType.HAND, null, null, null);
			return;
		}
		final Material mat = event.getCurrentItem().getType();
		final ItemStack result = event.getCurrentItem().clone();
		final Player player = (Player) event.getWhoClicked();
		final int amount = result.getAmount();
		if(!RecipeHandler.hasAccessToRecipe(player.getUniqueId(), RecipeType.STONECUTTING, mat.toString()))
		{
			event.setCancelled(true);
			event.setCurrentItem(null);
			ARGCheckEventAction.checkEventAction(player, "STONECUTTING:CANNOTACCESS",
					SC, ToolType.HAND, mat, null, null);
			return;
		}
		if (event.getClick() == ClickType.SHIFT_LEFT 
				|| event.getClick() == ClickType.SHIFT_RIGHT 
				|| event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY)
		{
			CraftItemListener.oostDetectionAll(player, result, mat, result.getItemMeta());
		} else
		{
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					ARGCheckEventAction.checkEventAction(player, "STONECUTTING:REWARD",
							SC, ToolType.HAND, mat, null, null);
					for(ItemStack is : RewardHandler.getDrops(player, SC, ToolType.HAND, mat, null))
					{
						ItemHandler.dropItem(is, player, null);
					}
					RewardHandler.rewardPlayer(player.getUniqueId(), SC, ToolType.HAND, mat, null, amount);
				}
			}.runTaskAsynchronously(TT.getPlugin());
		}
	}
}