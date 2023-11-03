package main.java.me.avankziar.tt.spigot.listener.reward;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.ItemHandler;
import main.java.me.avankziar.tt.spigot.handler.RewardHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;

public class CraftItemListener implements Listener
{
	final private static EventType CR = EventType.CRAFTING;
	@EventHandler
	public void onSmithItem(InventoryClickEvent event) //TODO Checken ob es nicht besser CraftItemEvent sein soll (Unwahrscheinlich)
	{
		if(event.isCancelled()
				|| !(event.getWhoClicked() instanceof Player)
				|| event.getSlotType() != SlotType.RESULT
				|| event.getClickedInventory() == null
				|| (event.getClickedInventory().getType() != InventoryType.CRAFTING && event.getClickedInventory().getType() != InventoryType.PLAYER)
				|| event.getWhoClicked().getGameMode() == GameMode.CREATIVE
				|| event.getWhoClicked().getGameMode() == GameMode.SPECTATOR
				|| event.getCurrentItem() == null
				|| !EnumHandler.isEventActive(CR))
		{
			return;
		}
		final ItemStack result = event.getCurrentItem().clone();
		if(result == null) //If the result item is null, deny all and return.
		{
			event.setResult(Result.DENY);
			event.setCurrentItem(null);
			event.setCancelled(true);
			return;
		}
		Player player = (Player) event.getWhoClicked();
		if (event.getClick() == ClickType.SHIFT_LEFT 
				|| event.getClick() == ClickType.SHIFT_RIGHT 
				|| event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY)
		{
			schedulePostDetectionAll(player, result, result.getType(), result.getItemMeta());
		} else
		{
			for(ItemStack is : RewardHandler.getDrops(player, CR, result.getType(), null, false))
			{
				Item it = player.getWorld().dropItem(player.getLocation(), is);
				ItemHandler.addItemToTask(it, player.getUniqueId());
			}
			RewardHandler.rewardPlayer(player.getUniqueId(), CR, result.getType(), null, 1);
		}
	}
	
	public void schedulePostDetectionAll(final Player player,
			final ItemStack pr, final Material premat, final ItemMeta premeta)
	{ //Da final ItemStack pr null zurücklierfert, holen wir die nicht null Material und ItemMeta.
		
		final ItemStack[] preInv = player.getInventory().getContents();
		for (int i = 0; i < preInv.length; i++) //als Sicherung erstellen wir vom PreInv eine Kopie
		{
			preInv[i] = preInv[i] != null ? preInv[i].clone() : null;
		}
		
		int preInvcount = 0;

		for(ItemStack is : preInv)
		{
			if(isSameItem(premat, premeta, is))
			{
				preInvcount++;
			}
		}
		
		final int preCount = preInvcount;

		new BukkitRunnable()
		{

			@Override
			public void run()
			{
				final ItemStack[] postInv = player.getInventory().getContents();
				int newItemsCount = 0;
				
				for (ItemStack post : postInv)
				{
					if(isSameItem(premat, premeta, post))
					{
						newItemsCount++;
					}
				}
				
				newItemsCount = newItemsCount-preCount;
				
				if (newItemsCount > 0)
				{
					
			        return;
				}
			}
		}.runTaskLater(TT.getPlugin(), 1L);
	}
	
	public static boolean isSameItem(Material mat, ItemMeta meta, ItemStack other)
	{
        if(other == null
        		|| mat != other.getType())
        {
        	return false;
        }
        if((other.getItemMeta() == null && meta != null)
        		|| other.getItemMeta() != null && meta == null)
        {
        	return false;
        }
        if(other.getItemMeta() != null && (meta != null))
        {
        	if(!meta.toString().equals(other.getItemMeta().toString()))
            {
            	return false;
            }
        }
        return true;
    }
}