package main.java.me.avankziar.tt.spigot.gui.handler;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.database.MysqlHandler;
import main.java.me.avankziar.tt.spigot.gui.objects.ClickFunctionType;
import main.java.me.avankziar.tt.spigot.gui.objects.GuiType;
import main.java.me.avankziar.tt.spigot.gui.objects.SettingsLevel;
import main.java.me.avankziar.tt.spigot.handler.CatTechHandler;
import main.java.me.avankziar.tt.spigot.handler.GuiHandler;
import main.java.me.avankziar.tt.spigot.objects.mysql.PlayerData;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.MainCategory;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.SubCategory;

public class GuiFunctionHandler
{
	private static TT plugin = TT.getPlugin();
	
	public static void doClickFunktion(GuiType guiType, ClickFunctionType cft, Player player,
			Inventory openInv, SettingsLevel settingsLevel,
			String mcat, String scat, String tech)
	{
		switch(cft)
		{
		default: return;
		case ADMINISTRATION_SETTINGSLEVEL_SETTO_ADVANCED: switchSettingsLevel(player, openInv, SettingsLevel.ADVANCED); break;
		case ADMINISTRATION_SETTINGSLEVEL_SETTO_BASE: switchSettingsLevel(player, openInv, SettingsLevel.BASE); break;
		case ADMINISTRATION_SETTINGSLEVEL_SETTO_EXPERT: switchSettingsLevel(player, openInv, SettingsLevel.EXPERT); break;
		case ADMINISTRATION_SETTINGSLEVEL_SETTO_MASTER: switchSettingsLevel(player, openInv, SettingsLevel.MASTER); break;
		case START_SYNCMESSAGE: syncMessage(player, openInv, settingsLevel); break;
		case RETURN_TOSTART: returnToStart(player, openInv, settingsLevel); break;
		case RETURN_TOMAINCATEGORY: returnToMainCategory(player, openInv, settingsLevel, mcat); break;
		case RETURN_TOSUBCATEGORY: returnToSubCategory(player, openInv, settingsLevel, scat); break;
		case MAINCATEGORYS_SUBCATEGORYS_SOLO:
		case MAINCATEGORYS_SUBCATEGORYS_GROUP:
		case MAINCATEGORYS_SUBCATEGORYS_GLOBAL:
		case SUBCATEGORYS_TECHNOLOGYS_SOLO:
		case SUBCATEGORYS_TECHNOLOGYS_GROUP:
		case SUBCATEGORYS_TECHNOLOGYS_GLOBAL:
		case INFO_TECHNOLOGY:
		case RESEARCH_TECHNOLOGY:
		}	
	}
	
	private static void switchSettingsLevel(Player player, Inventory inv, SettingsLevel settingsLevel)
	{
		PlayerData pd = (PlayerData) plugin.getMysqlHandler().getData(MysqlHandler.Type.PLAYERDATA,
				"`player_uuid` = ?", player.getUniqueId().toString());
		pd.setLastSettingLevel(settingsLevel);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.PLAYERDATA, pd, "`player_uuid` = ?", player.getUniqueId().toString());
		GuiHandler.openStart(player, settingsLevel, inv, false);
	}
	
	private static void syncMessage(Player player, Inventory inv, SettingsLevel settingsLevel)
	{
		PlayerData pd = (PlayerData) plugin.getMysqlHandler().getData(MysqlHandler.Type.PLAYERDATA,
				"`player_uuid` = ?", player.getUniqueId().toString());
		pd.setShowSyncMessage(!pd.isShowSyncMessage());
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.PLAYERDATA, pd, "`player_uuid` = ?", player.getUniqueId().toString());
		GuiHandler.openStart(player, settingsLevel, inv, false);
	}
	
	private static void returnToStart(Player player, Inventory inv, SettingsLevel settingsLevel)
	{
		GuiHandler.openStart(player, settingsLevel, inv, false);
	}
	
	private static void returnToMainCategory(Player player, Inventory inv, SettingsLevel settingsLevel, String mcat)
	{
		if(mcat == null)
		{
			return;
		}
		MainCategory mc = CatTechHandler.mainCategoryMapSolo.get(mcat);
		if(mc == null)
		{
			mc = CatTechHandler.mainCategoryMapGroup.get(mcat);
			if(mc == null)
			{
				mc = CatTechHandler.mainCategoryMapGlobal.get(mcat);
			}
		}
		if(mc == null)
		{
			returnToStart(player, inv, settingsLevel);
			return;
		}
		GuiHandler.openMCat(player, settingsLevel, inv, false, mc);
	}
	
	private static void returnToSubCategory(Player player, Inventory inv, SettingsLevel settingsLevel, String scat)
	{
		if(scat == null)
		{
			return;
		}
		SubCategory sc = CatTechHandler.subCategoryMapSolo.get(scat);
		if(sc == null)
		{
			sc = CatTechHandler.subCategoryMapGroup.get(scat);
			if(sc == null)
			{
				sc = CatTechHandler.subCategoryMapGlobal.get(scat);
			}
		}
		if(sc == null)
		{
			returnToStart(player, inv, settingsLevel);
			return;
		}
		GuiHandler.openSCat(player, settingsLevel, inv, false, sc);
	}
}