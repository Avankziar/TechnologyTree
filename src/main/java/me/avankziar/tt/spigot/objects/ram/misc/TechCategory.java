package main.java.me.avankziar.tt.spigot.objects.ram.misc;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import main.java.me.avankziar.tt.spigot.cmdtree.BaseConstructor;
import main.java.me.avankziar.tt.spigot.ifh.ItemGenerator;
import main.java.me.avankziar.tt.spigot.objects.PlayerAssociatedType;

public class TechCategory
{	
	private String internName;
	private String displayName;
	
	private PlayerAssociatedType playerAssociatedType;
	
	private int guiSlot;
	private boolean useFixGuiSlot;
	
	private List<String> seeRequirementConditionQuery;
	private boolean seeRequirementShowDifferentItemIfYouNormallyDontSeeIt;
	
	public TechCategory(String internName, String displayName, 
			PlayerAssociatedType playerAssociatedType,
			int guiSlot, boolean useFixGuiSlot,
			List<String> seeRequirementConditionQuery, boolean seeRequirementShowDifferentItemIfYouNormallyDontSeeIt)
	{
		setInternName(internName);
		setDisplayName(displayName);
		setPlayerAssociatedType(playerAssociatedType);
		setGuiSlot(guiSlot);
		setUseFixGuiSlot(useFixGuiSlot);
		setSeeRequirementConditionQuery(seeRequirementConditionQuery);
		setSeeRequirementShowDifferentItemIfYouNormallyDontSeeIt(seeRequirementShowDifferentItemIfYouNormallyDontSeeIt);
	}

	public String getInternName()
	{
		return internName;
	}

	public void setInternName(String internName)
	{
		this.internName = internName;
	}

	public String getDisplayName()
	{
		return displayName;
	}

	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}

	public PlayerAssociatedType getPlayerAssociatedType()
	{
		return playerAssociatedType;
	}

	public void setPlayerAssociatedType(PlayerAssociatedType playerAssociatedType)
	{
		this.playerAssociatedType = playerAssociatedType;
	}

	public int getGuiSlot()
	{
		return guiSlot;
	}

	public void setGuiSlot(int guiSlot)
	{
		this.guiSlot = guiSlot;
	}

	public boolean isUseFixGuiSlot()
	{
		return useFixGuiSlot;
	}

	public void setUseFixGuiSlot(boolean useFixGuiSlot)
	{
		this.useFixGuiSlot = useFixGuiSlot;
	}

	public List<String> getSeeRequirementConditionQuery()
	{
		return seeRequirementConditionQuery;
	}

	public void setSeeRequirementConditionQuery(List<String> seeRequirementConditionQuery)
	{
		this.seeRequirementConditionQuery = seeRequirementConditionQuery;
	}

	public boolean isSeeRequirementShowDifferentItemIfYouNormallyDontSeeIt()
	{
		return seeRequirementShowDifferentItemIfYouNormallyDontSeeIt;
	}

	public void setSeeRequirementShowDifferentItemIfYouNormallyDontSeeIt(
			boolean seeRequirementShowDifferentItemIfYouNormallyDontSeeIt)
	{
		this.seeRequirementShowDifferentItemIfYouNormallyDontSeeIt = seeRequirementShowDifferentItemIfYouNormallyDontSeeIt;
	}

	public ItemStack getSeeRequirementItemIfYouCanSeeIt(Player player)
	{
		return new ItemGenerator().generateItem(player,
				BaseConstructor.getPlugin().getYamlHandler().getTechnologies().get(this.getInternName()),
				"RequirementToSee.ItemIfYouCanSee", 0);
	}

	public ItemStack getSeeRequirementItemIfYouCannotSeeIt(Player player)
	{
		return new ItemGenerator().generateItem(player,
				BaseConstructor.getPlugin().getYamlHandler().getTechnologies().get(this.getInternName()),
				"RequirementToSee.ItemIfYouCannotSee", 0);
	}
}