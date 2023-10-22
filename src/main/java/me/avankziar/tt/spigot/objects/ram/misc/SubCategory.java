package main.java.me.avankziar.tt.spigot.objects.ram.misc;

import java.util.List;

import main.java.me.avankziar.tt.spigot.objects.PlayerAssociatedType;

public class SubCategory extends TechCategory
{
	private String overlyingCategory;
	
	public SubCategory(String internName, String displayName, PlayerAssociatedType playerAssociatedType, String groupAssociatedPermission,
			int guiSlot, boolean useFixGuiSlot, 
			List<String> seeRequirementConditionQuery, boolean seeRequirementShowDifferentItemIfYouNormallyDontSeeIt,
			String overlyingCategory)
	{
		super(internName, displayName, playerAssociatedType, guiSlot, useFixGuiSlot,
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
}