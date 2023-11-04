package main.java.me.avankziar.tt.spigot.objects;

import org.bukkit.Material;

public enum ToolType
{
	ALL, //All Items
	UNDEFINE, //All other Items in the Hand
	HAND,
	SHOVEL, WOODEN_SHOVEL, STONE_SHOVEL, IRON_SHOVEL, GOLDEN_SHOVEL, DIAMOND_SHOVEL, NETHERITE_SHOVEL,
	HOE, WOODEN_HOE, STONE_HOE, IRON_HOE, GOLDEN_HOE, DIAMOND_HOE, NETHERITE_HOE,
	AXE, WOODEN_AXE, STONE_AXE, IRON_AXE, GOLDEN_AXE, DIAMOND_AXE, NETHERITE_AXE,
	PICKAXE, WOODEN_PICKAXE, STONE_PICKAXE, IRON_PICKAXE, GOLDEN_PICKAXE, DIAMOND_PICKAXE, NETHERITE_PICKAXE,
	SWORD, WOODEN_SWORD, STONE_SWORD, IRON_SWORD, GOLDEN_SWORD, DIAMOND_SWORD, NETHERITE_SWORD,
	BOW,
	;
	
	public static ToolType getToolType(Material material)
	{
		if(material == null || material == Material.AIR)
		{
			return ToolType.HAND;
		}
		try
		{
			ToolType tt = ToolType.valueOf(material.toString());
			return tt;
		} catch(Exception e)
		{
			return ToolType.UNDEFINE;
		}
	}
	
	public static ToolType[] getComplementToolFormType(ToolType toolType)
	{
		switch(toolType)
		{
		default:
			return new ToolType[] {toolType};
		case SHOVEL:
			return new ToolType[] {WOODEN_SHOVEL, STONE_SHOVEL, IRON_SHOVEL, GOLDEN_SHOVEL, DIAMOND_SHOVEL, NETHERITE_SHOVEL};
		case HOE:
			return new ToolType[] {WOODEN_HOE, STONE_HOE, IRON_HOE, GOLDEN_HOE, DIAMOND_HOE, NETHERITE_HOE};
		case AXE:
			return new ToolType[] {WOODEN_AXE, STONE_AXE, IRON_AXE, GOLDEN_AXE, DIAMOND_AXE, NETHERITE_AXE};
		case PICKAXE:
			return new ToolType[] {WOODEN_PICKAXE, STONE_PICKAXE, IRON_PICKAXE, GOLDEN_PICKAXE, DIAMOND_PICKAXE, NETHERITE_PICKAXE};
		case SWORD:
			return new ToolType[] {WOODEN_SWORD, STONE_SWORD, IRON_SWORD, GOLDEN_SWORD, DIAMOND_SWORD, NETHERITE_SWORD};
		}
	}
}