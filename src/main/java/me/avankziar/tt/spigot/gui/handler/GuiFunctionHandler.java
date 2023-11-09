package main.java.me.avankziar.tt.spigot.gui.handler;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import main.java.me.avankziar.tt.general.ChatApi;
import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.assistance.TimeHandler;
import main.java.me.avankziar.tt.spigot.database.MysqlHandler;
import main.java.me.avankziar.tt.spigot.gui.objects.ClickFunctionType;
import main.java.me.avankziar.tt.spigot.gui.objects.GuiType;
import main.java.me.avankziar.tt.spigot.gui.objects.SettingsLevel;
import main.java.me.avankziar.tt.spigot.handler.CatTechHandler;
import main.java.me.avankziar.tt.spigot.handler.GuiHandler;
import main.java.me.avankziar.tt.spigot.handler.PlayerHandler;
import main.java.me.avankziar.tt.spigot.handler.PlayerHandler.AcquireRespond;
import main.java.me.avankziar.tt.spigot.objects.PlayerAssociatedType;
import main.java.me.avankziar.tt.spigot.objects.TechnologyType;
import main.java.me.avankziar.tt.spigot.objects.mysql.PlayerData;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.MainCategory;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.SubCategory;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.Technology;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class GuiFunctionHandler
{
	private static TT plugin = TT.getPlugin();
	
	public static void doClickFunktion(GuiType guiType, ClickFunctionType cft, Player player,
			Inventory openInv, SettingsLevel settingsLevel,
			String mcat, String scat, String tech, PlayerAssociatedType pat)
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
		case RETURN_TOMAINCATEGORY: returnToMainCategory(player, openInv, settingsLevel, mcat, pat); break;
		case RETURN_TOSUBCATEGORY: returnToSubCategory(player, openInv, settingsLevel, scat, pat); break;
		case MAINCATEGORYS_SUBCATEGORYS_SOLO: fromStartToMainCat(player, openInv, settingsLevel, mcat, PlayerAssociatedType.SOLO); break;
		case MAINCATEGORYS_SUBCATEGORYS_GROUP: break; //TODO
		case MAINCATEGORYS_SUBCATEGORYS_GLOBAL: fromStartToMainCat(player, openInv, settingsLevel, mcat, PlayerAssociatedType.GLOBAL); break;
		case SUBCATEGORYS_TECHNOLOGYS_SOLO: fromMainCatToSubCat(player, openInv, settingsLevel, scat, PlayerAssociatedType.SOLO); break;
		case SUBCATEGORYS_TECHNOLOGYS_GROUP: break; //TODO
		case SUBCATEGORYS_TECHNOLOGYS_GLOBAL: fromMainCatToSubCat(player, openInv, settingsLevel, scat, PlayerAssociatedType.GLOBAL); break;
		case INFO_TECHNOLOGY:
		case RESEARCH_TECHNOLOGY_SOLO: researchTechnologySolo(player, openInv, settingsLevel, tech, pat);
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
	
	private static void returnToMainCategory(Player player, Inventory inv, SettingsLevel settingsLevel, String mCat, PlayerAssociatedType pat)
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
			case GLOBAL: mc = CatTechHandler.mainCategoryMapGlobal.get(mCat); break;
			}
		}		
		if(mc == null)
		{
			returnToStart(player, inv, settingsLevel);
			return;
		}
		GuiHandler.openMCat(player, settingsLevel, inv, false, mc);
	}
	
	private static void returnToSubCategory(Player player, Inventory inv, SettingsLevel settingsLevel, String sCat, PlayerAssociatedType pat)
	{
		if(sCat == null)
		{
			return;
		}
		SubCategory sc = null;
		if(pat == null)
		{
			sc = CatTechHandler.subCategoryMapSolo.get(sCat);
			if(sc == null)
			{
				sc = CatTechHandler.subCategoryMapGroup.get(sCat);
				if(sc == null)
				{
					sc = CatTechHandler.subCategoryMapGlobal.get(sCat);
				}
			}
		} else
		{
			switch(pat)
			{
			case SOLO: sc = CatTechHandler.subCategoryMapSolo.get(sCat); break;
			case GLOBAL: sc = CatTechHandler.subCategoryMapGlobal.get(sCat); break;
			}
		}
		if(sc == null)
		{
			returnToStart(player, inv, settingsLevel);
			return;
		}
		GuiHandler.openSCat(player, settingsLevel, inv, false, sc);
	}
	
	private static void fromStartToMainCat(Player player, Inventory inv, SettingsLevel settingsLevel, String mCat, PlayerAssociatedType pat)
	{
		MainCategory mc = null;
		switch(pat)
		{
		case SOLO: mc = CatTechHandler.mainCategoryMapSolo.get(mCat); break;
		case GLOBAL: mc = CatTechHandler.mainCategoryMapGlobal.get(mCat); break;
		}
		if(mc == null)
		{
			return;
		}
		GuiHandler.openMCat(player, settingsLevel, inv, false, mc);
	}
	
	private static void fromMainCatToSubCat(Player player, Inventory inv, SettingsLevel settingsLevel, String sCat, PlayerAssociatedType pat)
	{
		SubCategory sc = null;
		switch(pat)
		{
		case SOLO: sc = CatTechHandler.subCategoryMapSolo.get(sCat); break;
		case GLOBAL: sc = CatTechHandler.subCategoryMapGlobal.get(sCat); break;
		}
		if(sc == null)
		{
			return;
		}
		GuiHandler.openSCat(player, settingsLevel, inv, false, sc);
	}
	
	private static void infoTechnology(Player player, String tech, PlayerAssociatedType pat)
	{
		Technology t = null;
		switch(pat)
		{
		case SOLO:
			t = CatTechHandler.technologyMapSolo.get(tech); break;
		case GLOBAL:
			t = CatTechHandler.technologyMapGlobal.get(tech); break;
		}//GuiHandler.
		YamlConfiguration y = plugin.getYamlHandler().getLang();
		String path = "GuiHandler.Technology.Info.";
		ArrayList<BaseComponent> albc = new ArrayList<>();
		TextComponent tx = ChatApi.hoverEvent(y.getString(path+"Headline").replace("%tech%", t.getDisplayName()),
				Action.SHOW_TEXT,
				y.getString(path+"Internname").replace("%name%", t.getInternName())+"~!~"+
				y.getString(path+"OverlyingSubCategory").replace("%name%", t.getOverlyingSubCategory())
				);
		albc.add(tx);
		tx = ChatApi.tctl(y.getString(path+"MaxTechLvlToResearchAndGuiSlot")
				.replace("%lvl%", String.valueOf((t.getTechnologyType() == TechnologyType.MULTIPLE ? t.getMaximalTechnologyLevelToResearch() : 1)))
				.replace("%slot%", String.valueOf(t.getGuiSlot()))
				);
		albc.add(tx);
		tx = ChatApi.tctl(y.getString(path+"PlayerAssociatedTypeAndTechType")
				.replace("%pat%", t.getPlayerAssociatedType().toString())
				.replace("%ttype%", t.getTechnologyType().toString())
				);
		albc.add(tx);
		//GenerelleInfos
		if(t.getTechnologyType() == TechnologyType.BOOSTER)
		{
			tx = ChatApi.tctl(y.getString(path+"BoosterExpireTimes")
					.replace("%exp%", TimeHandler.getRepeatingTime(t.getIfBoosterDurationUntilExpiration(), "yyyy-dd-HH:mm")));
			albc.add(tx);
		}
		if(pat == PlayerAssociatedType.GLOBAL)
		{
			tx = ChatApi.hoverEvent(y.getString(path+"GlobalTechPollParticipants.Info"),
					Action.SHOW_TEXT,
					y.getString(path+"GlobalTechPollParticipants.Interaction")
					.replace("%v%", String.valueOf(t.getForUninvolvedPollParticipants_RewardUnlockableInteractionsInPercent()))
					+"~!~"+
					y.getString(path+"GlobalTechPollParticipants.Recipe")
					.replace("%v%", String.valueOf(t.getForUninvolvedPollParticipants_RewardRecipesInPercent()))
					+"~!~"+
					y.getString(path+"GlobalTechPollParticipants.DropChance")
					.replace("%v%", String.valueOf(t.getForUninvolvedPollParticipants_RewardDropChancesInPercent()))
					+"~!~"+
					y.getString(path+"GlobalTechPollParticipants.SilkTouchDropChance")
					.replace("%v%", String.valueOf(t.getForUninvolvedPollParticipants_RewardSilkTouchDropChancesInPercent()))
					+"~!~"+
					y.getString(path+"GlobalTechPollParticipants.Commands")
					.replace("%v%", String.valueOf(t.getForUninvolvedPollParticipants_RewardCommandsInPercent()))
					+"~!~"+
					y.getString(path+"GlobalTechPollParticipants.Items")
					.replace("%v%", String.valueOf(t.getForUninvolvedPollParticipants_RewardItemsInPercent()))
					+"~!~"+
					y.getString(path+"GlobalTechPollParticipants.Modifier")
					.replace("%v%", String.valueOf(t.getForUninvolvedPollParticipants_RewardModifiersInPercent()))
					+"~!~"+
					y.getString(path+"GlobalTechPollParticipants.ValueEntry")
					.replace("%v%", String.valueOf(t.getForUninvolvedPollParticipants_RewardValueEntryInPercent())));
			albc.add(tx);
		}
		StringBuilder costTTExp = new StringBuilder();
		StringBuilder costVExp = new StringBuilder();
		StringBuilder costMoney = new StringBuilder();
		StringBuilder costMaterial = new StringBuilder();
		for(int i = 1; i <= t.getMaximalTechnologyLevelToResearch(); i++)
		{
			if(t.getCostTTExp().containsKey(i))
			{
				if(!costTTExp.isEmpty())
				{
					costTTExp.append("~!~");
				}
				costTTExp.append(y.getString(path+"Technology.Info.Lvl.CostTTExpHover").replace("%v%", t.getCostTTExp().get(i)));
			}
			if(t.getCostVanillaExp().containsKey(i))
			{
				if(!costVExp.isEmpty())
				{
					costVExp.append("~!~");
				}
				costVExp.append(y.getString(path+"Technology.Info.Lvl.CostVExpHover").replace("%v%", t.getCostVanillaExp().get(i)));
			}
			if(t.getCostMoney().containsKey(i))
			{
				if(!costMoney.isEmpty())
				{
					costMoney.append("~!~");
				}
				costMoney.append(y.getString(path+"Technology.Info.Lvl.CostMooneyHover").replace("%v%", t.getCostMoney().get(i)));
			}
			if(t.getCostMaterial().containsKey(i))
			{
				if(!costMaterial.isEmpty())
				{
					costMaterial.append("~!~");
				}
				costMaterial.append(y.getString(path+"Technology.Info.Lvl.CostMaterialHover"));
				int j = 0;
				for(Entry<Material, String> e : t.getCostMaterial().get(i).entrySet())
				{
					if(j + 1 <  t.getCostMaterial().get(i).size())
					{
						costMaterial.append("~!~");
					}
					costMaterial.append("&f"+e.getValue()+"x "+TT.getPlugin().getEnumTl() != null
							  									? TT.getPlugin().getEnumTl().getLocalization(e.getKey())
							  									: e.getKey().toString());
				}
			}
			
		}
		albc.add(ChatApi.hoverEvent(y.getString(path+"Technology.Info.Lvl.CostTTExp"), Action.SHOW_TEXT, costTTExp.toString()));
		albc.add(ChatApi.hoverEvent(y.getString(path+"Technology.Info.Lvl.CostVExp"), Action.SHOW_TEXT, costVExp.toString()));
		albc.add(ChatApi.hoverEvent(y.getString(path+"Technology.Info.Lvl.CostMoney"), Action.SHOW_TEXT, costMoney.toString()));
		albc.add(ChatApi.hoverEvent(y.getString(path+"Technology.Info.Lvl.CostMaterial"), Action.SHOW_TEXT, costMaterial.toString()));
		//Levelbezogene Dinge
		t.getResearchRequirementConditionQuery();
		t.getRewardCommandList();
		t.getRewardDropChances();
		t.getRewardItemList();
		t.getRewardModifierList();
		t.getRewardRecipes();
		t.getRewardSilkTouchDropChances();
		t.getRewardUnlockableInteractions();
		t.getRewardValueEntryList();
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
			PlayerHandler.payTechnology(player, t, 100);
			int rlvl = PlayerHandler.researchSoloTechnology(player, t, false);
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("GuiHandler.Technology.TechnologyResearched")
					.replace("%level%", String.valueOf(rlvl))
					.replace("%tech%", t.getDisplayName())));
			fromMainCatToSubCat(player, inv, settingsLevel, t.getOverlyingSubCategory(), pat);
			break;
		}
	}
}