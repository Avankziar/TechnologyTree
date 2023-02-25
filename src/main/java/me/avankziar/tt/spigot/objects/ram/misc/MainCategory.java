package main.java.me.avankziar.tt.spigot.objects.ram.misc;

import java.util.List;

import main.java.me.avankziar.tt.spigot.objects.PlayerAssociatedType;

public class MainCategory extends TechCategory
{	
	public MainCategory(String internName, String displayName, PlayerAssociatedType playerAssociatedType, String groupAssociatedPermission,
			int guiSlot, boolean useFixGuiSlot,
			List<String> seeRequirementConditionQuery, boolean seeRequirementShowDifferentItemIfYouNormallyDontSeeIt)
	{
		super(internName, displayName, playerAssociatedType, groupAssociatedPermission ,guiSlot, useFixGuiSlot,
				seeRequirementConditionQuery, seeRequirementShowDifferentItemIfYouNormallyDontSeeIt);
	}
}
