package main.java.me.avankziar.tt.spigot.listener.reward;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.cmd.tt.ARGCheckEventAction;
import main.java.me.avankziar.tt.spigot.handler.EnumHandler;
import main.java.me.avankziar.tt.spigot.handler.ItemHandler;
import main.java.me.avankziar.tt.spigot.handler.RewardHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.ToolType;

public class CraftItemListener implements Listener
{
	final private static EventType CR = EventType.CRAFTING;
	
	@EventHandler //InventoryClickEvent
	public void onCrafItem(CraftItemEvent event)
	{
		if(event.isCancelled()
				|| !(event.getWhoClicked() instanceof Player)
				|| event.getSlotType() != SlotType.RESULT
				|| event.getClickedInventory() == null
				|| (event.getClickedInventory().getType() != InventoryType.WORKBENCH && event.getClickedInventory().getType() != InventoryType.CRAFTING)
				|| event.getWhoClicked().getGameMode() == GameMode.CREATIVE
				|| event.getWhoClicked().getGameMode() == GameMode.SPECTATOR
				|| event.getCurrentItem() == null
				|| !EnumHandler.isEventActive(CR))
		{
			ARGCheckEventAction.checkEventAction((Player) event.getWhoClicked(), "CRAFTING:RETURN1",
					CR, ToolType.HAND, null, null, Material.AIR);
			return;
		}
		final ItemStack result = event.getCurrentItem().clone();
		if(result == null) //If the result item is null, deny all and return.
		{
			ARGCheckEventAction.checkEventAction((Player) event.getWhoClicked(), "CRAFTING:RESULTNULL1",
					CR, ToolType.HAND, null, null, Material.AIR);
			event.setResult(Result.DENY);
			event.setCurrentItem(null);
			event.setCancelled(true);
			return;
		}
		final int amount = result.getAmount();
		final Material mat = result.getType();
		final Player player = (Player) event.getWhoClicked();
		if (event.getClick() == ClickType.SHIFT_LEFT 
				|| event.getClick() == ClickType.SHIFT_RIGHT 
				|| event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY)
		{
			postDetectionAll(player, result, mat, result.getItemMeta());
		} else
		{
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					ARGCheckEventAction.checkEventAction(player, "CRAFTING:REWARD1",
							CR, ToolType.HAND, null, null, Material.AIR);
					for(ItemStack is : RewardHandler.getDrops(player, CR, ToolType.HAND, mat, null))
					{
						ItemHandler.dropItem(is, player, null);
					}
					RewardHandler.rewardPlayer(player.getUniqueId(), CR, ToolType.HAND, mat, null, amount);
				}
			}.runTaskAsynchronously(TT.getPlugin());
		}
	}
	
	public static void postDetectionAll(final Player player,
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
				preInvcount = preInvcount + is.getAmount();
			}
		}
		final int preCount = preInvcount;
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				final ItemStack[] postInv = player.getInventory().getContents();
				int newCount = 0;
				
				for (ItemStack post : postInv)
				{
					if(isSameItem(premat, premeta, post))
					{
						newCount = newCount + post.getAmount();
					}
				}
				int newItemsCount = newCount-preCount;
				if (newItemsCount < 0)
				{
			        return;
				}
				new BukkitRunnable()
				{
					@Override
					public void run()
					{
						ARGCheckEventAction.checkEventAction(player, "CRAFTING:REWARD2",
								CR, ToolType.HAND, null, null, Material.AIR);
						for(int i = 0; i <= newItemsCount; i++)
						{
							for(ItemStack is : RewardHandler.getDrops(player, CR, ToolType.HAND, premat, null))
							{
								ItemHandler.dropItem(is, player, null);
							}
						}
						RewardHandler.rewardPlayer(player.getUniqueId(), CR, ToolType.HAND, premat, null, newItemsCount);
					}
				}.runTaskAsynchronously(TT.getPlugin());
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