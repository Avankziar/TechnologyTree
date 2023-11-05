package main.java.me.avankziar.tt.spigot.objects;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public enum ToolType
{
	ALL, //All Items
	//UNDEFINE, //All other Items in the Hand
	HAND,
	//SHOVEL, 
	WOODEN_SHOVEL, STONE_SHOVEL, IRON_SHOVEL, GOLDEN_SHOVEL, DIAMOND_SHOVEL, NETHERITE_SHOVEL,
	//HOE, 
	WOODEN_HOE, STONE_HOE, IRON_HOE, GOLDEN_HOE, DIAMOND_HOE, NETHERITE_HOE,
	//AXE, 
	WOODEN_AXE, STONE_AXE, IRON_AXE, GOLDEN_AXE, DIAMOND_AXE, NETHERITE_AXE,
	//PICKAXE, 
	WOODEN_PICKAXE, STONE_PICKAXE, IRON_PICKAXE, GOLDEN_PICKAXE, DIAMOND_PICKAXE, NETHERITE_PICKAXE,
	//SWORD, 
	WOODEN_SWORD, STONE_SWORD, IRON_SWORD, GOLDEN_SWORD, DIAMOND_SWORD, NETHERITE_SWORD,
	BOW,
	SHEARS,
	FISHING_ROD
	;
	
	public static ToolType getHandToolType(Player player)
	{
		if(player == null 
				|| player.getInventory().getItemInMainHand() == null 
				|| player.getInventory().getItemInMainHand().getType() == Material.AIR)
		{
			return ToolType.HAND;
		}
		try
		{
			ToolType tt = ToolType.valueOf(player.getInventory().getItemInMainHand().getType().toString());
			return tt;
		} catch(Exception e)
		{
			return ToolType.ALL;
		}
	}
	
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
			return ToolType.ALL;
		}
	}
	
	/*public static ToolType[] getComplementToolFormType(ToolType toolType)
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
	}*/
	
	public static boolean isTool(ToolType tool)
	{
		switch(tool)
		{
		default:
			return false;
		//case AXE:
		case BOW:
		case DIAMOND_AXE:
		case DIAMOND_HOE:
		case DIAMOND_PICKAXE:
		case DIAMOND_SHOVEL:
		case DIAMOND_SWORD:
		case FISHING_ROD:
		case GOLDEN_AXE:
		case GOLDEN_HOE:
		case GOLDEN_PICKAXE:
		case GOLDEN_SHOVEL:
		case GOLDEN_SWORD:
		//case HOE:
		case IRON_AXE:
		case IRON_HOE:
		case IRON_PICKAXE:
		case IRON_SHOVEL:
		case IRON_SWORD:
		case NETHERITE_AXE:
		case NETHERITE_HOE:
		case NETHERITE_PICKAXE:
		case NETHERITE_SHOVEL:
		case NETHERITE_SWORD:
		//case PICKAXE:
		case SHEARS:
		//case SHOVEL:
		case STONE_AXE:
		case STONE_HOE:
		case STONE_PICKAXE:
		case STONE_SHOVEL:
		case STONE_SWORD:
		//case SWORD:
		case WOODEN_AXE:
		case WOODEN_HOE:
		case WOODEN_PICKAXE:
		case WOODEN_SHOVEL:
		case WOODEN_SWORD:
			return true;
		}
	}
}