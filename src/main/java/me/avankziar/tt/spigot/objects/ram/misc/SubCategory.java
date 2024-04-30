package main.java.me.avankziar.tt.spigot.objects.ram.misc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import main.java.me.avankziar.tt.spigot.cmdtree.BaseConstructor;
import main.java.me.avankziar.tt.spigot.handler.CatTechHandler;
import main.java.me.avankziar.tt.spigot.handler.GuiHandler;
import main.java.me.avankziar.tt.spigot.objects.PlayerAssociatedType;

public class SubCategory extends TechCategory
{
	private String overlyingCategory;
	private String overlyingCategoryDisplayname;
	
	public SubCategory(String internName, String displayName, PlayerAssociatedType playerAssociatedType, String groupAssociatedPermission,
			int guiSlot,
			List<String> seeRequirementConditionQuery, boolean seeRequirementShowDifferentItemIfYouNormallyDontSeeIt,
			String overlyingCategory)
	{
		super(internName, displayName, playerAssociatedType, guiSlot,
				seeRequirementConditionQuery, seeRequirementShowDifferentItemIfYouNormallyDontSeeIt);
		setOverlyingCategory(overlyingCategory);
		if(overlyingCategory != null)
		{
			LinkedHashMap<String, MainCategory> map = new LinkedHashMap<>();
			switch(playerAssociatedType)
			{
			case SOLO: map = CatTechHandler.mainCategoryMapSolo; break;
			case GROUP: map = CatTechHandler.mainCategoryMapGroup; break;
			case GLOBAL: map = CatTechHandler.mainCategoryMapGlobal; break;
			}
			List<String> list = CatTechHandler.mainCategoryMapGlobal.keySet()
					.stream().filter(x -> x.equals(overlyingCategory)).collect(Collectors.toList());
			if(list.size() > 0)
			{
				MainCategory mcat = CatTechHandler.mainCategoryMapGlobal.get(list.get(0));
				if(mcat != null)
				{
					setOverlyingCategoryDisplayname(mcat.getDisplayName());
				}
			}
		}
	}

	public String getOverlyingCategory()
	{
		return overlyingCategory;
	}

	public void setOverlyingCategory(String overlyingCategory)
	{
		this.overlyingCategory = overlyingCategory;
	}
	
	public String getOverlyingCategoryDisplayname()
	{
		return overlyingCategoryDisplayname;
	}

	public void setOverlyingCategoryDisplayname(String overlyingCategoryDisplayname)
	{
		this.overlyingCategoryDisplayname = overlyingCategoryDisplayname;
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