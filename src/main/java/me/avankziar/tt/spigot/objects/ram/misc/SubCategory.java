package main.java.me.avankziar.tt.spigot.objects.ram.misc;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import main.java.me.avankziar.tt.spigot.cmdtree.BaseConstructor;
import main.java.me.avankziar.tt.spigot.handler.GuiHandler;
import main.java.me.avankziar.tt.spigot.objects.PlayerAssociatedType;

public class SubCategory extends TechCategory
{
	private String overlyingCategory;
	
	public SubCategory(String internName, String displayName, PlayerAssociatedType playerAssociatedType, String groupAssociatedPermission,
			int guiSlot,
			List<String> seeRequirementConditionQuery, boolean seeRequirementShowDifferentItemIfYouNormallyDontSeeIt,
			String overlyingCategory)
	{
		super(internName, displayName, playerAssociatedType, guiSlot,
				seeRequirementConditionQuery, seeRequirementShowDifferentItemIfYouNormallyDontSeeIt);
		setOverlyingCategory(overlyingCategory);
	}

	public String getOverlyingCategory()
	{
		return overlyingCategory;
	}

	public void setOverlyingCategory(String overlyingCategory)
	{
		this.overlyingCategory = overlyingCategory;
	}
	
	public ItemStack getSeeRequirementItemIfYouCanSeeIt(Player player)
	{
		return GuiHandler.generateItem(BaseConstructor.getPlugin().getYamlHandler().getSubCategories().get(this.getInternName()),
				"RequirementToSee.ItemIfYouCanSee", 0, null, this, null, getPlayerAssociatedType(), player);
	}

	public ItemStack getSeeRequirementItemIfYouCannotSeeIt(Player player)
	{
		return GuiHandler.generateItem(BaseConstructor.getPlugin().getYamlHandler().getSubCategories().get(this.getInternName()),
				"RequirementToSee.ItemIfYouCannotSee", 0, null, this, null, getPlayerAssociatedType(), player);
	}
}