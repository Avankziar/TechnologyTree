package main.java.me.avankziar.tt.spigot.gui.listener;

import java.io.IOException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.gui.events.UpperGuiClickEvent;
import main.java.me.avankziar.tt.spigot.gui.handler.GuiFunctionHandler;
import main.java.me.avankziar.tt.spigot.gui.objects.ClickFunctionType;
import main.java.me.avankziar.tt.spigot.gui.objects.ClickType;
import main.java.me.avankziar.tt.spigot.gui.objects.GuiType;
import main.java.me.avankziar.tt.spigot.handler.GuiHandler;
import main.java.me.avankziar.tt.spigot.objects.PlayerAssociatedType;

public class UpperListener implements Listener
{
	private TT plugin;
	
	public UpperListener(TT plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onUpperGui(final UpperGuiClickEvent event) throws IOException
	{
		if(!event.getPluginName().equals(plugin.pluginName))
		{
			TT.log.info("UpperListener 0"); //REMOVEME
			return;
		}
		if(!(event.getEvent().getWhoClicked() instanceof Player))
		{
			TT.log.info("UpperListener 1"); //REMOVEME
			return;
		}
		final Player player = (Player) event.getEvent().getWhoClicked();
		GuiType guit = null;
		try
		{
			guit = GuiType.valueOf(event.getInventoryIdentifier());
		} catch(Exception e)
		{
			TT.log.info("UpperListener 2 : "+event.getInventoryIdentifier()); //REMOVEME
			return;
		}
		final GuiType gt = guit;
		ClickType ct = getClickFunctionType(event.getEvent().getClick(), event.getEvent().getHotbarButton());
		if(ct == null)
		{
			TT.log.info("UpperListener 3 : "+event.getEvent().getClick().toString()+" | "+event.getEvent().getHotbarButton()); //REMOVEME
			return;
		}
		ClickFunctionType clft = null;
		try
		{
			clft = ClickFunctionType.valueOf(event.getFunction(ct));
		} catch(Exception e)
		{
			TT.log.info("UpperListener 4 : "+event.getFunction(ct)); //REMOVEME
			return;
		}
		if(clft == null)
		{
			TT.log.info("UpperListener 5"); //REMOVEME
			return;
		}
		PlayerAssociatedType paty = null;
		if(event.getValuesString().containsKey(GuiHandler.PAT))
		{
			paty = PlayerAssociatedType.valueOf(event.getValuesString().get(GuiHandler.PAT));
		}
		String macat = null;
		String sucat = null;
		String techn = null;
		if(event.getValuesString().containsKey(GuiHandler.MAINCATEGORY))
		{
			macat = event.getValuesString().get(GuiHandler.MAINCATEGORY);
		} else if(event.getValuesString().containsKey(GuiHandler.SUBCATEGORY))
		{
			sucat = event.getValuesString().get(GuiHandler.SUBCATEGORY);
		} else if(event.getValuesString().containsKey(GuiHandler.TECHNOLOGY))
		{
			techn = event.getValuesString().get(GuiHandler.TECHNOLOGY);
		}
		final ClickFunctionType cft = clft;
		final String mcat = macat;
		final String scat = sucat;
		final String tech = techn;
		final PlayerAssociatedType pat = paty;
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				GuiFunctionHandler.doClickFunktion(gt, cft, player, event.getEvent().getClickedInventory(), event.getSettingsLevel(),
						mcat, scat, tech, pat);
			}
		}.runTaskAsynchronously(plugin);
	}
	
	private ClickType getClickFunctionType(org.bukkit.event.inventory.ClickType ct, int hotbarButton)
	{
		switch(ct)
		{
		default: return null;
		case LEFT: return ClickType.LEFT;
		case RIGHT: return ClickType.RIGHT;
		case DROP: return ClickType.DROP;
		case SHIFT_LEFT: return ClickType.SHIFT_LEFT;
		case SHIFT_RIGHT: return ClickType.SHIFT_RIGHT;
		case CONTROL_DROP: return ClickType.CTRL_DROP;
		case NUMBER_KEY:
			if(hotbarButton < 0)
			{
				return null;
			}
			int i = hotbarButton+1;
			switch(i)
			{
			default: return null;
			case 1: return ClickType.NUMPAD_1;
			case 2: return ClickType.NUMPAD_2;
			case 3: return ClickType.NUMPAD_3;
			case 4: return ClickType.NUMPAD_4;
			case 5: return ClickType.NUMPAD_5;
			case 6: return ClickType.NUMPAD_6;
			case 7: return ClickType.NUMPAD_7;
			case 8: return ClickType.NUMPAD_8;
			case 9: return ClickType.NUMPAD_9;
			}
		}
	}
}