package main.java.me.avankziar.tt.spigot.objects.ram.misc;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import main.java.me.avankziar.tt.spigot.cmdtree.BaseConstructor;
import main.java.me.avankziar.tt.spigot.handler.GuiHandler;
import main.java.me.avankziar.tt.spigot.objects.PlayerAssociatedType;

public class MainCategory extends TechCategory
{	
	public MainCategory(String internName, String displayName, PlayerAssociatedType playerAssociatedType, String groupAssociatedPermission,
			int guiSlot,
			List<String> seeRequirementConditionQuery, boolean seeRequirementShowDifferentItemIfYouNormallyDontSeeIt)
	{
		super(internName, displayName, playerAssociatedType ,guiSlot,
				seeRequirementConditionQuery, seeRequirementShowDifferentItemIfYouNormallyDontSeeIt);
	}
	
	public ItemStack getSeeRequirementItemIfYouCanSeeIt(Player player)
	{
		return GuiHandler.generateItem(BaseConstructor.getPlugin().getYamlHandler().getMainCategories().get(this.getInternName()),
				"RequirementToSee.ItemIfYouCanSee", 0, this, null, null, getPlayerAssociatedType(), player);
	}

	public ItemStack getSeeRequirementItemIfYouCannotSeeIt(Player player)
	{
		return GuiHandler.generateItem(BaseConstructor.getPlugin().getYamlHandler().getMainCategories().get(this.getInternName()),
				"RequirementToSee.ItemIfYouCannotSee", 0, this, null, null, getPlayerAssociatedType(), player);
	}
}
