package main.java.me.avankziar.tt.spigot.gui.handler;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import main.java.me.avankziar.tt.general.ChatApi;
import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.cmd.tt.ARGTechInfo;
import main.java.me.avankziar.tt.spigot.database.MysqlHandler;
import main.java.me.avankziar.tt.spigot.gui.objects.ClickFunctionType;
import main.java.me.avankziar.tt.spigot.gui.objects.GuiType;
import main.java.me.avankziar.tt.spigot.gui.objects.SettingsLevel;
import main.java.me.avankziar.tt.spigot.handler.CatTechHandler;
import main.java.me.avankziar.tt.spigot.handler.GuiHandler;
import main.java.me.avankziar.tt.spigot.handler.PlayerHandler;
import main.java.me.avankziar.tt.spigot.handler.PlayerHandler.AcquireRespond;
import main.java.me.avankziar.tt.spigot.objects.PlayerAssociatedType;
import main.java.me.avankziar.tt.spigot.objects.mysql.PlayerData;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.MainCategory;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.SubCategory;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.Technology;

public class GuiFunctionHandler
{
	private static TT plugin = TT.getPlugin();
	
	public static void doClickFunktion(GuiType guiType, ClickFunctionType cft, Player player,
			Inventory openInv, SettingsLevel settingsLevel,
			String mcat, String scat, String tech, PlayerAssociatedType pat)
	{
		/*TT.log.info("=========================="); //REMOVEME
		TT.log.info("guitype : "+guiType.toString());
		TT.log.info("cft : "+cft.toString());
		TT.log.info("pat != null : "+(pat != null ? pat.toString(): "/"));
		TT.log.info("mcat != null : "+(mcat != null ? mcat : "/"));
		TT.log.info("scat != null : "+(scat != null ? scat : "/"));
		TT.log.info("tech != null : "+(tech != null ? tech : "/"));*/
		switch(cft)
		{
		default: 
			return;
		case ADMINISTRATION_SETTINGSLEVEL_SETTO_ADVANCED: switchSettingsLevel(player, openInv, SettingsLevel.ADVANCED); break;
		case ADMINISTRATION_SETTINGSLEVEL_SETTO_BASE: switchSettingsLevel(player, openInv, SettingsLevel.BASE); break;
		case ADMINISTRATION_SETTINGSLEVEL_SETTO_EXPERT: switchSettingsLevel(player, openInv, SettingsLevel.EXPERT); break;
		case ADMINISTRATION_SETTINGSLEVEL_SETTO_MASTER: switchSettingsLevel(player, openInv, SettingsLevel.MASTER); break;
		case START_SYNCMESSAGE: syncMessage(player, openInv, settingsLevel); break;
		case START_REWARDMESSAGE: rewardMessage(player, openInv, settingsLevel); break;
		case RETURN_TOSTART: returnToStart(player, openInv, settingsLevel); break;
		case RETURN_TOMAINCATEGORY: returnToMainCategory(player, openInv, settingsLevel, pat); break;
		case RETURN_TOSUBCATEGORY: returnToSubCategory(player, openInv, settingsLevel, mcat, pat); break;
		case START_MAINCATEGORYS_SOLO: fromStartToMainCat(player, openInv, settingsLevel, PlayerAssociatedType.SOLO); break;
		case START_MAINCATEGORYS_GROUP: break; //TODO
		case START_MAINCATEGORYS_GLOBAL: fromStartToMainCat(player, openInv, settingsLevel, PlayerAssociatedType.GLOBAL); break;
		case MAINCATEGORYS_SUBCATEGORYS_SOLO: fromMainCatToSubCat(player, openInv, settingsLevel, mcat, PlayerAssociatedType.SOLO); break;
		case MAINCATEGORYS_SUBCATEGORYS_GROUP: break; //TODO
		case MAINCATEGORYS_SUBCATEGORYS_GLOBAL: fromMainCatToSubCat(player, openInv, settingsLevel, mcat, PlayerAssociatedType.GLOBAL); break;
		case SUBCATEGORYS_TECHNOLOGYS_SOLO: fromSubCatToTechs(player, openInv, settingsLevel, scat, PlayerAssociatedType.SOLO); break;
		case SUBCATEGORYS_TECHNOLOGYS_GROUP: break; //TODO
		case SUBCATEGORYS_TECHNOLOGYS_GLOBAL: fromSubCatToTechs(player, openInv, settingsLevel, scat, PlayerAssociatedType.GLOBAL); break;
		case INFO_TECHNOLOGY: infoTechnology(player, tech, pat); break;
		case RESEARCH_TECHNOLOGY_SOLO: researchTechnologySolo(player, openInv, settingsLevel, tech, pat); break;
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
	
	private static void rewardMessage(Player player, Inventory inv, SettingsLevel settingsLevel)
	{
		PlayerData pd = (PlayerData) plugin.getMysqlHandler().getData(MysqlHandler.Type.PLAYERDATA,
				"`player_uuid` = ?", player.getUniqueId().toString());
		pd.setShowRewardMessage(!pd.isShowRewardMessage());
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.PLAYERDATA, pd, "`player_uuid` = ?", player.getUniqueId().toString());
		GuiHandler.openStart(player, settingsLevel, inv, false);
	}
	
	private static void returnToStart(Player player, Inventory inv, SettingsLevel settingsLevel)
	{
		GuiHandler.openStart(player, settingsLevel, inv, false);
	}
	
	private static void returnToMainCategory(Player player, Inventory inv, SettingsLevel settingsLevel, PlayerAssociatedType pat)
	{
		GuiHandler.openStartMCat(player, settingsLevel, inv, false, pat);
	}
	
	private static void returnToSubCategory(Player player, Inventory inv, SettingsLevel settingsLevel, String mCat, PlayerAssociatedType pat)
	{
		if(mCat == null)
		{
			return;
		}
		MainCategory mc = null;
		if(pat == null)
		{
			mc = CatTechHandler.mainCategoryMapSolo.get(mCat);
			if(mc == null)
			{
				mc = CatTechHandler.mainCategoryMapGroup.get(mCat);
				if(mc == null)
				{
					mc = CatTechHandler.mainCategoryMapGlobal.get(mCat);
				}
			}
		} else
		{
			switch(pat)
			{
			case SOLO: mc = CatTechHandler.mainCategoryMapSolo.get(mCat); break;
			case GROUP: mc = CatTechHandler.mainCategoryMapGroup.get(mCat); break;
			case GLOBAL: mc = CatTechHandler.mainCategoryMapGlobal.get(mCat); break;
			}
		}
		if(mc == null)
		{
			returnToStart(player, inv, settingsLevel);
			return;
		}
		GuiHandler.openMainCatSubCat(player, settingsLevel, inv, false, mc);
	}
	
	private static void fromStartToMainCat(Player player, Inventory inv, SettingsLevel settingsLevel, PlayerAssociatedType pat)
	{
		GuiHandler.openStartMCat(player, settingsLevel, inv, false, pat);
	}
	
	private static void fromMainCatToSubCat(Player player, Inventory inv, SettingsLevel settingsLevel, String mCat, PlayerAssociatedType pat)
	{
		MainCategory mc = null;
		switch(pat)
		{
		case SOLO: mc = CatTechHandler.mainCategoryMapSolo.get(mCat); break;
		case GROUP: mc = CatTechHandler.mainCategoryMapGroup.get(mCat); break;
		case GLOBAL: mc = CatTechHandler.mainCategoryMapGlobal.get(mCat); break;
		}
		if(mc == null)
		{
			return;
		}
		GuiHandler.openMainCatSubCat(player, settingsLevel, inv, false, mc);
	}
	
	private static void fromSubCatToTechs(Player player, Inventory inv, SettingsLevel settingsLevel, String sCat, PlayerAssociatedType pat)
	{
		SubCategory sc = null;
		switch(pat)
		{
		case SOLO: sc = CatTechHandler.subCategoryMapSolo.get(sCat); break;
		case GROUP: sc = CatTechHandler.subCategoryMapGroup.get(sCat); break;
		case GLOBAL: sc = CatTechHandler.subCategoryMapGlobal.get(sCat); break;
		}
		if(sc == null)
		{
			return;
		}
		GuiHandler.openSubCTech(player, settingsLevel, inv, false, sc);
	}
	
	private static void infoTechnology(Player player, String tech, PlayerAssociatedType pat)
	{
		Technology t = null;
		switch(pat)
		{
		case SOLO: t = CatTechHandler.technologyMapSolo.get(tech); break;
		case GROUP: t = CatTechHandler.technologyMapGroup.get(tech); break;
		case GLOBAL: t = CatTechHandler.technologyMapGlobal.get(tech); break;
		}
		ARGTechInfo.techInfo(player, t, null);
	}
	
	private static void researchTechnologySolo(Player player, Inventory inv, SettingsLevel settingsLevel, String tech, PlayerAssociatedType pat)
	{
		Technology t = CatTechHandler.technologyMapSolo.get(tech);
		if(t == null)
		{
			return;
		}
		AcquireRespond ar = PlayerHandler.haveAlreadyResearched(player, t);
		switch(ar)
		{
		case NOT_ENOUGH_TT_EXP:
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("GuiHandler.Technology.NotEnoughTTExp")));
			break;
		case NOT_ENOUGH_VANILLA_EXP:
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("GuiHandler.Technology.NotEnoughVanillaExp")));
			break;
		case NOT_ENOUGH_MONEY:
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("GuiHandler.Technology.NotEnoughMoney")));
			break;
		case NOT_ENOUGH_MATERIAL:
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("GuiHandler.Technology.NotEnoughMaterial")));
			break;
		case TECH_IS_SIMPLE_AND_ALREADY_RESEARCHED:
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("GuiHandler.Technology.IsSimpleAndAlreadyResearched")
					.replace("%tech%", t.getDisplayName())));
			break;
		case TECH_MAX_LEVEL_IS_REACHED:
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("GuiHandler.MaxResearchLevelAlreadyReached")
					.replace("%tech%", t.getDisplayName())));
			break;
		case CAN_BE_RESEARCHED:
			AcquireRespond arII = PlayerHandler.payTechnology(player, t, 1.0);
			switch(arII)
			{
			case NOT_ENOUGH_TT_EXP:
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("GuiHandler.Technology.NotEnoughTTExp")));
				break;
			case NOT_ENOUGH_VANILLA_EXP:
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("GuiHandler.Technology.NotEnoughVanillaExp")));
				break;
			case NOT_ENOUGH_MONEY:
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("GuiHandler.Technology.NotEnoughMoney")));
				break;
			case NOT_ENOUGH_MATERIAL:
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("GuiHandler.Technology.NotEnoughMaterial")));
				break;
			case TECH_IS_SIMPLE_AND_ALREADY_RESEARCHED:
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("GuiHandler.Technology.IsSimpleAndAlreadyResearched")
						.replace("%tech%", t.getDisplayName())));
				break;
			case TECH_MAX_LEVEL_IS_REACHED:
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("GuiHandler.MaxResearchLevelAlreadyReached")
						.replace("%tech%", t.getDisplayName())));
				break;
			case CAN_BE_RESEARCHED:
				int rlvl = PlayerHandler.researchSoloTechnology(player, t, true);
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("GuiHandler.Technology.TechnologyResearched")
						.replace("%level%", String.valueOf(rlvl))
						.replace("%tech%", t.getDisplayName())));
				fromSubCatToTechs(player, inv, settingsLevel, t.getOverlyingSubCategory(), pat);
				break;
			}
			break;
		}
	}
}