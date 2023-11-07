package main.java.me.avankziar.tt.spigot.gui.listener;

import java.io.IOException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.gui.events.UpperGuiClickEvent;
import main.java.me.avankziar.tt.spigot.gui.handler.GuiFunctionHandler;
import main.java.me.avankziar.tt.spigot.gui.objects.ClickFunctionType;
import main.java.me.avankziar.tt.spigot.gui.objects.ClickType;
import main.java.me.avankziar.tt.spigot.gui.objects.GuiType;
import main.java.me.avankziar.tt.spigot.handler.GuiHandler;

public class UpperListener implements Listener
{
	private TT plugin;
	
	public UpperListener(TT plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onUpperGui(UpperGuiClickEvent event) throws IOException
	{
		if(!event.getPluginName().equals(plugin.pluginName))
		{
			return;
		}
		if(!(event.getEvent().getWhoClicked() instanceof Player))
		{
			return;
		}
		Player player = (Player) event.getEvent().getWhoClicked();
		GuiType gt = null;
		try
		{
			gt = GuiType.valueOf(event.getInventoryIdentifier());
		} catch(Exception e)
		{
			return;
		}
		ClickType ct = getClickFunctionType(event.getEvent().getClick(), event.getEvent().getHotbarButton());
		if(ct == null)
		{
			return;
		}
		ClickFunctionType cft = null;
		try
		{
			cft = ClickFunctionType.valueOf(event.getFunction(ct));
		} catch(Exception e)
		{
			return;
		}
		if(cft == null)
		{
			return;
		}
		String mcat = null;
		String scat = null;
		String tech = null;
		if(event.getValuesString().containsKey(GuiHandler.MAINCATEGORY))
		{
			mcat = event.getValuesString().get(GuiHandler.MAINCATEGORY);
		} else if(event.getValuesString().containsKey(GuiHandler.SUBCATEGORY))
		{
			scat = event.getValuesString().get(GuiHandler.SUBCATEGORY);
		} else if(event.getValuesString().containsKey(GuiHandler.TECHNOLOGY))
		{
			tech = event.getValuesString().get(GuiHandler.TECHNOLOGY);
		}
		GuiFunctionHandler.doClickFunktion(gt, cft, player, event.getEvent().getClickedInventory(), event.getSettingsLevel(),
				mcat, scat, tech);
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