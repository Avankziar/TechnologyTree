package main.java.me.avankziar.tt.spigot.objects;

public enum EventType
{
	BREAKING, 
	BREEDING,
	BREWING,
	BUCKET_EMPTYING,
	BUCKET_FILLING,
	COLD_FORGING,
	COOKING,
	CRAFTING,
	DRYING,
	DYING,
	ENCHANTING,
	EXPLODING,
	FERTILIZING,
	FISHING,
	GRINDING,
	HARMING,
	HARVEST,
	IGNITING,
	INTERACT, //Such as, creat Pathways, stripped Logs, Cow milking
	ITEM_BREAKING,
	ITEM_CONSUME,
	KILLING,
	MELTING,
	PLACING,
	PREPARE_ITEMCRAFT, //Do not can be used to reward a player, only used in permit the player access to the event
	PREPARE_SMITHING, //Do not can be used to reward a player, only used in permit the player access to the event
	RENAMING,
	SHEARING,
	SHEEP_DYE,
	SMELTING,
	SMITHING,
	SMOKING,
	TAMING;
	
	//ADDME Possible new EventTypes
	/*
	 * EQUIP_ARMOR > PlayerInteractEvent > Possible New RewardType = Reward.UnlockableEquipment
	 * EQUIP_WEAPON > PlayerItemHeldEvent > Possible New RewardType = Reward.UnlockableEquipment
	 * EQUIP_TOOLS > PlayerItemHeldEvent > Possible New RewardType = Reward.UnlockableEquipment
	 * TRADING > InventoryClickEvent
	 * 		Info: It muss be prevent the event.getAction NOTHING/PLACE_ONE/PLACE_ALL/PLACE_SOME
	 * 		InventoryType must be MERCHANT, clicked SlotType must be RESULT
	 * 		ItemStack resultStack = event.getClickedInventory().getItem(2);
	 */
}