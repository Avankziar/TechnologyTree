package main.java.me.avankziar.tt.spigot.handler;

import java.lang.reflect.Field;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import main.java.me.avankziar.ifh.general.economy.account.AccountCategory;
import main.java.me.avankziar.ifh.general.economy.currency.CurrencyType;
import main.java.me.avankziar.ifh.general.math.MathFormulaParser;
import main.java.me.avankziar.ifh.spigot.economy.account.Account;
import main.java.me.avankziar.tt.general.ChatApi;
import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.assistance.Utility;
import main.java.me.avankziar.tt.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.tt.spigot.gui.GUIApi;
import main.java.me.avankziar.tt.spigot.gui.events.ClickFunction;
import main.java.me.avankziar.tt.spigot.gui.objects.ClickFunctionType;
import main.java.me.avankziar.tt.spigot.gui.objects.ClickType;
import main.java.me.avankziar.tt.spigot.gui.objects.GuiType;
import main.java.me.avankziar.tt.spigot.gui.objects.SettingsLevel;
import main.java.me.avankziar.tt.spigot.modifiervalueentry.ModifierValueEntry;
import main.java.me.avankziar.tt.spigot.objects.PlayerAssociatedType;
import main.java.me.avankziar.tt.spigot.objects.mysql.SoloEntryQueryStatus;
import main.java.me.avankziar.tt.spigot.objects.mysql.SoloEntryQueryStatus.EntryQueryType;
import main.java.me.avankziar.tt.spigot.objects.mysql.SoloEntryQueryStatus.StatusType;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.MainCategory;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.SubCategory;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.TechCategory;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.Technology;

public class GuiHandler
{
	private static TT plugin = TT.getPlugin();
	public static String CATEGORY = "category";
	public static String TECHNOLOGY = "technology";
	
	public static void openCatOrTech(Player player, GuiType gt, TechCategory tc, PlayerAssociatedType pat, SettingsLevel st, boolean closeInv)
	{
		String title = "";
		switch(gt)
		{
		case START:
			title = plugin.getYamlHandler().getLang().getString("GuiHandler.Main.Title"); break;
		case MAIN_CATEGORY:
			title = plugin.getYamlHandler().getLang().getString("GuiHandler.MainCategorys.Title"); break;
		case SUB_CATEGORY:
			title = plugin.getYamlHandler().getLang().getString("GuiHandler.MainCategorysSubCategorys.Title")
					.replace("%maincat%", tc.getDisplayName()); break;
		case TECHNOLOGY:
			title = plugin.getYamlHandler().getLang().getString("GuiHandler.SubCategorysTechnologys.Title")
			.replace("%subcat%", tc.getDisplayName()); break;
		}
		GUIApi gui = new GUIApi(plugin.pluginName, gt.toString(), null, 6, title, st);
		openGui(tc, pat, player, gt, gui, st, closeInv);
	}
	
	/*public static void openAdministration(SignShop ssh, Player player, SettingsLevel settingsLevel, Inventory inv, boolean closeInv)
	{
		GuiType gt = GuiType.ADMINISTRATION;
		GUIApi gui = new GUIApi(plugin.pluginName, inv, gt.toString(), 
				settingsLevel == null ? SettingsLevel.BASE : settingsLevel);
		SignShop ssh2 = (SignShop) plugin.getMysqlHandler().getData(MysqlHandler.Type.SIGNSHOP, "`id` = ?", ssh.getId());
		openGui(ssh2, player, gt, gui, settingsLevel, closeInv);
	}
	
	public static void openShop(SignShop ssh, Player player, SettingsLevel settingsLevel, boolean closeInv)
	{
		GuiType gt = GuiType.SHOP;
		GUIApi gui = new GUIApi(plugin.pluginName, gt.toString(), null, 6, "Shop "+ssh.getSignShopName(), settingsLevel);
		SignShop ssh2 = (SignShop) plugin.getMysqlHandler().getData(MysqlHandler.Type.SIGNSHOP, "`id` = ?", ssh.getId());
		openGui(ssh2, player, gt, gui, settingsLevel, closeInv);
	}
	
	public static void openShop(SignShop ssh, Player player, SettingsLevel settingsLevel, Inventory inv, boolean closeInv)
	{
		GuiType gt = GuiType.SHOP;
		GUIApi gui = new GUIApi(plugin.pluginName, inv, gt.toString(), settingsLevel);
		SignShop ssh2 = (SignShop) plugin.getMysqlHandler().getData(MysqlHandler.Type.SIGNSHOP, "`id` = ?", ssh.getId());
		openGui(ssh2, player, gt, gui, settingsLevel, closeInv);
	}
	
	public static void openInputInfo(SignShop ssh, Player player, SettingsLevel settingsLevel, boolean closeInv)
	{
		GuiType gt = GuiType.ITEM_INPUT;
		GUIApi gui = new GUIApi(plugin.pluginName, gt.toString(), null, 6, "Shop:"+String.valueOf(ssh.getId()), settingsLevel);
		SignShop ssh2 = (SignShop) plugin.getMysqlHandler().getData(MysqlHandler.Type.SIGNSHOP, "`id` = ?", ssh.getId());
		openGui(ssh2, player, gt, gui, settingsLevel, closeInv);
	}
	
	public static void openKeyOrNumInput(SignShop ssh, Player player, GuiType gt, SettingsLevel settingsLevel, String keyboardOrNumpad, boolean closeInv)
	{
		GUIApi gui = new GUIApi(plugin.pluginName, gt.toString(), null, 6, ssh.getSignShopName()+keyboardOrNumpad, settingsLevel);
		SignShop ssh2 = (SignShop) plugin.getMysqlHandler().getData(MysqlHandler.Type.SIGNSHOP, "`id` = ?", ssh.getId());
		openGui(ssh2, player, gt, gui, settingsLevel, closeInv);
	}*/
	
	private static void openGui(TechCategory tcat, PlayerAssociatedType pat, Player player, GuiType gt, GUIApi gui, SettingsLevel settingsLevel, boolean closeInv)
	{
		YamlConfiguration y = plugin.getYamlHandler().getGui(gt);
		for(int i = 0; i < 54; i++)
		{
			if(y.get(i+".Material") == null && y.get(i+".Material."+settingsLevel.toString()) == null)
			{
				continue;
			}			
			SettingsLevel itemSL = SettingsLevel.valueOf(y.getString(i+".SettingLevel"));
			if(y.get(i+".SettingLevel") == null)
			{
				itemSL = SettingsLevel.NOLEVEL;
			}
			if(settingsLevel.getOrdinal() < itemSL.getOrdinal())
			{
				continue;
			}
			if(y.get(i+".Permission") != null)
			{
				if(!ModifierValueEntry.hasPermission(player, y.getString(i+".Permission")))
				{
					continue;
				}
			}
			if(y.get(i+".IFHDepend") != null)
			{
				if(y.getBoolean(i+".IFHDepend"))
				{
					if(plugin.getIFHEco() == null)
					{
						continue;
					}
				}
			}
			Material mat = null;
			ItemStack is = null;
			if(y.get(i+".Material."+settingsLevel.toString()) != null)
			{
				mat = Material.valueOf(y.getString(i+".Material."+settingsLevel.toString()));
				if(mat == Material.PLAYER_HEAD && y.getString(i+"."+settingsLevel.toString()+".PlayerHeadTexture") != null)
				{
					is = getSkull(y.getString(i+"."+settingsLevel.getName()+".PlayerHeadTexture"));
				}
			} else
			{
				try
				{
					mat = Material.valueOf(y.getString(i+".Material"));
					if(mat == Material.PLAYER_HEAD && y.getString(i+".HeadTexture") != null)
					{
						is = getSkull(y.getString(i+".HeadTexture"));
					}
				} catch(Exception e)
				{
					continue;
				}
			}
			String playername = null;
			int amount = 1;
			if(y.get(i+".Amount") != null)
			{
				amount = y.getInt(i+".Amount");
			}
			ArrayList<String> lore = null;
			if(y.get(i+".Lore."+settingsLevel.toString()) != null)
			{
				lore = (ArrayList<String>) y.getStringList(i+".Lore."+settingsLevel.toString());
			} else
			{
				if(y.get(i+".Lore") != null)
				{
					lore = (ArrayList<String>) y.getStringList(i+".Lore");
				}
			}
			if(lore != null)
			{
				lore = (ArrayList<String>) getLorePlaceHolder(player, tcat, null, lore, playername);
			}
			String displayname = y.get(i+".Displayname") != null 
					? y.getString(i+".Displayname") : is.getType().toString();
			displayname = getStringPlaceHolder(player, tcat, null, displayname, playername);
			if(is == null)
			{
				is = new ItemStack(mat, amount);
			} else
			{
				is.setAmount(amount);
			}
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(displayname);
			if(lore != null)
			{
				im.setLore(lore);
			}
			is.setItemMeta(im);
			LinkedHashMap<String, Entry<GUIApi.Type, Object>> map = new LinkedHashMap<>();
			map.put(CATEGORY, new AbstractMap.SimpleEntry<GUIApi.Type, Object>(GUIApi.Type.STRING,
					tcat != null ? tcat.getInternName() : ""));
			gui.add(i, is, settingsLevel, true, map, getClickFunction(y, String.valueOf(i)));
		}
		switch(gt)
		{
		case START:
			break;
		case MAIN_CATEGORY:
			for(Entry<Integer, MainCategory> e : CatTechHandler.playerAssocMainCategoryMap.get(pat).entrySet())
			{
				int i = e.getKey();
				LinkedHashMap<ItemStack, Boolean> isb = PlayerHandler.canSeeOrResearch_ForGUI(player, player.getUniqueId(), e.getValue(), null, null);
				for(Entry<ItemStack, Boolean> ee : isb.entrySet())
				{
					if(ee.getValue() != null && ee.getValue() == false)
					{
						continue;
					}
					ItemStack is = ee.getKey();
					LinkedHashMap<String, Entry<GUIApi.Type, Object>> map = new LinkedHashMap<>();
					ArrayList<ClickFunction> ctar = new ArrayList<>();
					ClickFunctionType cft = ClickFunctionType.MAINCATEGORYS_SUBCATEGORYS_SOLO;
					ctar.add(new ClickFunction(ClickType.LEFT, cft));
					ctar.add(new ClickFunction(ClickType.RIGHT, cft));
					gui.add(i, is, settingsLevel, true, map, ctar.toArray(new ClickFunction[ctar.size()]));
				}
			}
			break;
		case SUB_CATEGORY:
			LinkedHashMap<Integer, SubCategory> subcmap = null;
			switch(tcat.getPlayerAssociatedType())
			{
			case GLOBAL:
				subcmap = CatTechHandler.mainCategorySubCategoryMapGlobal.get(tcat.getInternName()); break;
			/*case GROUP:
				subcmap = CatTechHandler.mainCategorySubCategoryMapGroup.get(tcat.getInternName()); break;*/
			case SOLO:
				subcmap = CatTechHandler.mainCategorySubCategoryMapSolo.get(tcat.getInternName()); break;
			}
			for(Entry<Integer, SubCategory> e : subcmap.entrySet())
			{
				int i = e.getKey();
				LinkedHashMap<ItemStack, Boolean> isb = PlayerHandler.canSeeOrResearch_ForGUI(player, player.getUniqueId(), null, e.getValue(), null);
				for(Entry<ItemStack, Boolean> ee : isb.entrySet())
				{
					if(ee.getValue() != null && ee.getValue() == false)
					{
						continue;
					}
					ItemStack is = ee.getKey();
					LinkedHashMap<String, Entry<GUIApi.Type, Object>> map = new LinkedHashMap<>();
					ArrayList<ClickFunction> ctar = new ArrayList<>();
					ClickFunctionType cft = ClickFunctionType.SUBCATEGORYS_TECHNOLOGYS_SOLO;
					ctar.add(new ClickFunction(ClickType.LEFT, cft));
					ctar.add(new ClickFunction(ClickType.RIGHT, cft));
					gui.add(i, is, settingsLevel, true, map, ctar.toArray(new ClickFunction[ctar.size()]));
				}
			}
			break;
		case TECHNOLOGY:
			LinkedHashMap<Integer, Technology> techmap = null;
			switch(tcat.getPlayerAssociatedType())
			{
			case GLOBAL:
				techmap = CatTechHandler.subCategoryTechnologyMapGlobal.get(tcat.getInternName()); break;
			/*case GROUP:
				techmap = CatTechHandler.subCategoryTechnologyMapGroup.get(tcat.getInternName()); break;*/
			case SOLO:
				techmap = CatTechHandler.subCategoryTechnologyMapSolo.get(tcat.getInternName()); break;
			}
			for(Entry<Integer, Technology> e : techmap.entrySet())
			{
				int i = e.getKey();
				LinkedHashMap<ItemStack, Boolean> isb = PlayerHandler.canSeeOrResearch_ForGUI(player, player.getUniqueId(), null, null, e.getValue());
				for(Entry<ItemStack, Boolean> ee : isb.entrySet())
				{
					ItemStack is = ee.getKey();
					LinkedHashMap<String, Entry<GUIApi.Type, Object>> map = new LinkedHashMap<>();
					map.put(TECHNOLOGY, new AbstractMap.SimpleEntry<GUIApi.Type, Object>(GUIApi.Type.STRING,
							e.getValue().getInternName()));
					ArrayList<ClickFunction> ctar = new ArrayList<>();
					if(ee.getValue() != null && ee.getValue() == true)
					{
						ClickFunctionType cft = ClickFunctionType.RESEARCH_TECHNOLOGY;
						ctar.add(new ClickFunction(ClickType.LEFT, cft));
						ctar.add(new ClickFunction(ClickType.RIGHT, cft));
					}
					gui.add(i, is, settingsLevel, true, map, ctar.toArray(new ClickFunction[ctar.size()]));
				}
			}
			break;
		}
		if(closeInv)
		{
			player.closeInventory();
		}
		gui.open(player, gt);
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack getSkull(String url) 
	{
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
        if (url == null || url.isEmpty())
            return skull;
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = org.apache.commons.codec.binary.Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try {
            profileField = skullMeta.getClass().getDeclaredField("profile");
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        profileField.setAccessible(true);
        try {
            profileField.set(skullMeta, profile);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        skull.setItemMeta(skullMeta);
        return skull;
    }
	
	private static List<String> getLorePlaceHolder(Player player, TechCategory tcat, Technology t, List<String> lore, String playername)
	{
		List<String> list = new ArrayList<>();
		for(String s : lore)
		{
			String a = getStringPlaceHolder(player, tcat, t, s, playername);
			if(plugin.getIFHEco() != null)
			{
				Account ac = plugin.getIFHEco().getDefaultAccount(player.getUniqueId(), AccountCategory.MAIN,
						plugin.getIFHEco().getDefaultCurrency(CurrencyType.DIGITAL));
				int dg = ac == null ? 0 : plugin.getIFHEco().getDefaultGradationQuantity(ac.getCurrency());
				boolean useSI = ac == null ? false : plugin.getIFHEco().getDefaultUseSIPrefix(ac.getCurrency());
				boolean useSy = ac == null ? false : plugin.getIFHEco().getDefaultUseSymbol(ac.getCurrency());
				String ts = ac == null ? "." : plugin.getIFHEco().getDefaultThousandSeperator(ac.getCurrency());
				String ds = ac == null ? "," : plugin.getIFHEco().getDefaultDecimalSeperator(ac.getCurrency());
				a = getStringPlaceHolderIFH(player, t, a, ac, dg, useSI, useSy, ts, ds, playername);
			} else
			{
				a = getStringPlaceHolderVault(player, t, a, playername);
			}
			list.add(a);
		}
		return list;
	}
	
	private static List<PotionEffect> getBasePotion(PotionData pd, int pv) //pv PotionVariation, 1 Normal, 2 Splash, 3 Linger
	{
		PotionType pt = pd.getType();
		boolean ex = pd.isExtended();
		List<PotionEffect> list = new ArrayList<>();
		int amp = pd.isUpgraded() ? 1 : 0;
		int dur = 0;
		switch(pt)
		{
		case AWKWARD:
		case MUNDANE:
		case UNCRAFTABLE:
		case WATER:
		case THICK:
			break;
		case INVISIBILITY:
		case NIGHT_VISION:
		case FIRE_RESISTANCE:
		case WATER_BREATHING:
			if(amp == 0 && !ex && pv == 1) {dur = 3*60*20;}
			else if(amp == 0 && ex && pv == 1) {dur = 8*60*20;}
			
			else if(amp == 0 && !ex && pv == 2) {dur = 3*60*20;}
			else if(amp == 0 && ex && pv == 2) {dur = 8*60*20;}
			
			else if(amp == 0 && !ex && pv == 3) {dur = 45*20;}
			else if(amp == 0 && ex && pv == 3) {dur = 2*60*20;}
			
			else if(amp == 0 && !ex && pv == 4) {dur = 22*20;}
			else if(amp == 0 && ex && pv == 4) {dur = 60*20;}
			list.add(pt.getEffectType().createEffect(dur, amp));
			break;
		case INSTANT_DAMAGE:
		case INSTANT_HEAL:
			list.add(pt.getEffectType().createEffect(10, amp));
			break;
		case JUMP:
		case SPEED:
		case STRENGTH:
			if(amp == 0 && !ex && pv == 1) {dur = 3*60*20;}
			else if(amp == 0 && ex && pv == 1) {dur = 8*60*20;}
			else if(amp == 1 && !ex && pv == 1) {dur = 90*20;}
			
			else if(amp == 0 && !ex && pv == 2) {dur = 3*60*20;}
			else if(amp == 0 && ex && pv == 2) {dur = 8*60*20;}
			else if(amp == 1 && !ex && pv == 2) {dur = 90*20;}
			
			else if(amp == 0 && !ex && pv == 3) {dur = 45*20;}
			else if(amp == 0 && ex && pv == 3) {dur = 2*60*20;}
			else if(amp == 1 && !ex && pv == 3) {dur = 22*20;}
			
			else if(amp == 0 && !ex && pv == 4) {dur = 22*20;}
			else if(amp == 0 && ex && pv == 4) {dur = 60*20;}
			else if(amp == 1 && !ex && pv == 4) {dur = 11*20;}
			list.add(pt.getEffectType().createEffect(dur, amp));
			break;
		case POISON:
		case REGEN:
			if(amp == 0 && !ex && pv == 1) {dur = 90*20;}
			else if(amp == 0 && ex && pv == 1) {dur = 4*60*20;}
			else if(amp == 1 && !ex && pv == 1) {dur = 22*20;}
			
			else if(amp == 0 && !ex && pv == 2) {dur = 90*20;}
			else if(amp == 0 && ex && pv == 2) {dur = 4*60*20;}
			else if(amp == 1 && !ex && pv == 2) {dur = 22*20;}
			
			else if(amp == 0 && !ex && pv == 3) {dur = 45*20;}
			else if(amp == 0 && ex && pv == 3) {dur = 2*60*20;}
			else if(amp == 1 && !ex && pv == 3) {dur = 22*20;}
			
			else if(amp == 0 && !ex && pv == 3) {dur = 5*20;}
			else if(amp == 0 && ex && pv == 3) {dur = 11*20;}
			else if(amp == 1 && !ex && pv == 3) {dur = 2*20;}
			list.add(pt.getEffectType().createEffect(dur, amp));
			break;
		case SLOW_FALLING:
		case WEAKNESS:
			if(amp == 0 && !ex && pv == 1) {dur = 90*20;}
			else if(amp == 0 && ex && pv == 1) {dur = 4*60*20;}
			
			else if(amp == 0 && !ex && pv == 2) {dur = 90*20;}
			else if(amp == 0 && ex && pv == 2) {dur = 4*60*20;}
			
			else if(amp == 0 && !ex && pv == 3) {dur = 22*20;}
			else if(amp == 0 && ex && pv == 3) {dur = 60*20;}
			
			else if(amp == 0 && !ex && pv == 3) {dur = 11*20;}
			else if(amp == 0 && ex && pv == 3) {dur = 30*20;}
			list.add(pt.getEffectType().createEffect(dur, amp));
			break;
		case SLOWNESS:
			amp = pd.isUpgraded() ? 3 : 0;
			if(amp == 0 && !ex && pv == 1) {dur = 90*20;}
			else if(amp == 0 && ex && pv == 1) {dur = 4*60*20;}
			else if(amp == 3 && !ex && pv == 1) {dur = 20*20;}
			
			else if(amp == 0 && !ex && pv == 2) {dur = 90*20;}
			else if(amp == 0 && ex && pv == 2) {dur = 4*60*20;}
			else if(amp == 3 && !ex && pv == 2) {dur = 20*20;}
			
			else if(amp == 0 && !ex && pv == 3) {dur = 22*20;}
			else if(amp == 0 && ex && pv == 3) {dur = 60*20;}
			else if(amp == 3 && !ex && pv == 3) {dur = 5*20;}
			
			else if(amp == 0 && !ex && pv == 3) {dur = 11*20;}
			else if(amp == 0 && ex && pv == 3) {dur = 30*20;}
			else if(amp == 3 && !ex && pv == 3) {dur = 2*20;}
			list.add(pt.getEffectType().createEffect(dur, amp));
			break;
		case TURTLE_MASTER:
			amp = pd.isUpgraded() ? 5 : 3;
			if(amp == 3 && !ex && pv == 1) {dur = 20*20;}
			else if(amp == 3 && ex && pv == 1) {dur = 40*20;}
			else if(amp == 5 && !ex && pv == 1) {dur = 20*20;}
			
			else if(amp == 3 && !ex && pv == 2) {dur = 20*20;}
			else if(amp == 3 && ex && pv == 2) {dur = 40*20;}
			else if(amp == 5 && !ex && pv == 2) {dur = 20*20;}
			
			else if(amp == 3 && !ex && pv == 3) {dur = 5*20;}
			else if(amp == 3 && ex && pv == 3) {dur = 10*20;}
			else if(amp == 5 && !ex && pv == 3) {dur = 5*20;}
			
			else if(amp == 3 && !ex && pv == 4) {dur = 2*20;}
			else if(amp == 3 && ex && pv == 4) {dur = 5*20;}
			else if(amp == 5 && !ex && pv == 4) {dur = 2*20;}
			list.add(new PotionEffect(PotionEffectType.SLOW, dur, amp));
			amp = pd.isUpgraded() ? 3 : 2;
			if(amp == 2 && !ex && pv == 1) {dur = 20*20;}
			else if(amp == 2 && ex && pv == 1) {dur = 40*20;}
			else if(amp == 3 && !ex && pv == 1) {dur = 20*20;}
			
			else if(amp == 2 && !ex && pv == 2) {dur = 20*20;}
			else if(amp == 2 && ex && pv == 2) {dur = 40*20;}
			else if(amp == 3 && !ex && pv == 2) {dur = 20*20;}
			
			else if(amp == 2 && !ex && pv == 3) {dur = 5*20;}
			else if(amp == 2 && ex && pv == 3) {dur = 10*20;}
			else if(amp == 3 && !ex && pv == 3) {dur = 5*20;}
			
			else if(amp == 2 && !ex && pv == 4) {dur = 2*20;}
			else if(amp == 2 && ex && pv == 4) {dur = 5*20;}
			else if(amp == 3 && !ex && pv == 4) {dur = 2*20;}
			list.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, dur, amp));
			break;
		case LUCK:
			if(amp == 0 && !ex && pv == 1) {dur = 5*60*20;}
			
			else if(amp == 0 && !ex && pv == 2) {dur = 5*60*20;}
			
			else if(amp == 0 && !ex && pv == 3) {dur = 75*20;}
			
			else if(amp == 0 && !ex && pv == 4) {dur = 37*20;}
			list.add(pt.getEffectType().createEffect(dur, amp));
			break;
		}
		return list;
	}
	
	private static String getPotionColor(PotionEffect pe)
	{
		String color = "";
		if(pe.getType() == PotionEffectType.ABSORPTION || pe.getType() == PotionEffectType.CONDUIT_POWER
				|| pe.getType() == PotionEffectType.DAMAGE_RESISTANCE || pe.getType() == PotionEffectType.DOLPHINS_GRACE
				|| pe.getType() == PotionEffectType.FAST_DIGGING || pe.getType() == PotionEffectType.FIRE_RESISTANCE
				|| pe.getType() == PotionEffectType.HEAL || pe.getType() == PotionEffectType.HEALTH_BOOST
				|| pe.getType() == PotionEffectType.HERO_OF_THE_VILLAGE || pe.getType() == PotionEffectType.INCREASE_DAMAGE
				|| pe.getType() == PotionEffectType.INVISIBILITY || pe.getType() == PotionEffectType.JUMP
				|| pe.getType() == PotionEffectType.LUCK || pe.getType() == PotionEffectType.NIGHT_VISION
				|| pe.getType() == PotionEffectType.REGENERATION || pe.getType() == PotionEffectType.SATURATION
				|| pe.getType() == PotionEffectType.SLOW_FALLING || pe.getType() == PotionEffectType.SPEED
				|| pe.getType() == PotionEffectType.WATER_BREATHING)
		{
			color = "&9";
		} else if(pe.getType() == PotionEffectType.BAD_OMEN || pe.getType() == PotionEffectType.BLINDNESS
				|| pe.getType() == PotionEffectType.CONFUSION || pe.getType() == PotionEffectType.DARKNESS
				|| pe.getType() == PotionEffectType.HARM || pe.getType() == PotionEffectType.HUNGER
				|| pe.getType() == PotionEffectType.LEVITATION || pe.getType() == PotionEffectType.POISON
				|| pe.getType() == PotionEffectType.SLOW || pe.getType() == PotionEffectType.SLOW_DIGGING
				|| pe.getType() == PotionEffectType.SLOW_FALLING || pe.getType() == PotionEffectType.UNLUCK
				|| pe.getType() == PotionEffectType.WEAKNESS || pe.getType() == PotionEffectType.WITHER)
		{
			color = "&c";
		} else if(pe.getType() == PotionEffectType.GLOWING)
		{
			color = "&7";
		}
		return color;
	}
	
	private static int getMaxDamage(Material material)
	{
		int damage = 0;
		switch(material)
		{
		case WOODEN_AXE: //Fallthrough
		case WOODEN_HOE:
		case WOODEN_PICKAXE:
		case WOODEN_SHOVEL:
		case WOODEN_SWORD:
			damage = 60;
			break;
		case LEATHER_BOOTS:
			damage = 65;
			break;
		case LEATHER_CHESTPLATE:
			damage = 80;
			break;
		case LEATHER_HELMET:
			damage = 55;
			break;
		case LEATHER_LEGGINGS:
			damage = 75;
			break;
		case STONE_AXE:
		case STONE_HOE:
		case STONE_PICKAXE:
		case STONE_SHOVEL:
		case STONE_SWORD:
			damage = 132;
			break;
		case CHAINMAIL_BOOTS:
			damage = 196;
			break;
		case CHAINMAIL_CHESTPLATE:
			damage = 241;
			break;
		case CHAINMAIL_HELMET:
			damage = 166;
			break;
		case CHAINMAIL_LEGGINGS:
			damage = 226;
			break;
		case GOLDEN_AXE:
		case GOLDEN_HOE:
		case GOLDEN_PICKAXE:
		case GOLDEN_SHOVEL:
		case GOLDEN_SWORD:
			damage = 33;
			break;
		case GOLDEN_BOOTS:
			damage = 91;
			break;
		case GOLDEN_CHESTPLATE:
			damage = 112;
			break;
		case GOLDEN_HELMET:
			damage = 77;
			break;
		case GOLDEN_LEGGINGS:
			damage = 105;
			break;
		case IRON_AXE:
		case IRON_HOE:
		case IRON_PICKAXE:
		case IRON_SHOVEL:
		case IRON_SWORD:
			damage = 251;
			break;
		case IRON_BOOTS:
			damage = 195;
			break;
		case IRON_CHESTPLATE:
			damage = 40;
			break;
		case IRON_HELMET:
			damage = 165;
			break;
		case IRON_LEGGINGS:
			damage = 225;
			break;
		case DIAMOND_AXE:
		case DIAMOND_HOE:
		case DIAMOND_PICKAXE:
		case DIAMOND_SHOVEL:
		case DIAMOND_SWORD:
			damage = 1562;
			break;
		case DIAMOND_BOOTS:
			damage = 429;
			break;
		case DIAMOND_CHESTPLATE:
			damage = 528;
			break;
		case DIAMOND_HELMET:
			damage = 363;
			break;
		case DIAMOND_LEGGINGS:
			damage = 495;
			break;
		case NETHERITE_AXE:
		case NETHERITE_HOE:
		case NETHERITE_PICKAXE:
		case NETHERITE_SHOVEL:
		case NETHERITE_SWORD:
			damage = 2031;
			break;
		case NETHERITE_BOOTS:
			damage = 482;
			break;
		case NETHERITE_CHESTPLATE:
			damage = 592;
			break;
		case NETHERITE_HELMET:
			damage = 408;
			break;
		case NETHERITE_LEGGINGS:
			damage = 556;
			break;
		case SHIELD:
			damage = 337;
			break;
		case TURTLE_HELMET:
			damage = 276;
			break;
		case TRIDENT:
			damage = 251;
			break;
		case FISHING_ROD:
			damage = 65;
			break;
		case CARROT_ON_A_STICK:
			damage = 26;
			break;
		case WARPED_FUNGUS_ON_A_STICK:
			damage = 100;
			break;
		case ELYTRA:
			damage = 432;
			break;
		case SHEARS:
			damage = 238;
			break;
		case BOW:
			damage = 385;
			break;
		case CROSSBOW:
			damage = 326;
			break;
		case FLINT_AND_STEEL:
			damage = 65;
			break;
		default:
			damage = 0;
			break;
		}
		return damage;
	}
	
	//thanks https://stackoverflow.com/questions/12967896/converting-integers-to-roman-numerals-java
	private static String IntegerToRomanNumeral(int input) 
	{
	    if (input < 1 || input > 3999)
	        return String.valueOf(input);
	    String s = "";
	    while (input >= 1000) {
	        s += "M";
	        input -= 1000;        }
	    while (input >= 900) {
	        s += "CM";
	        input -= 900;
	    }
	    while (input >= 500) {
	        s += "D";
	        input -= 500;
	    }
	    while (input >= 400) {
	        s += "CD";
	        input -= 400;
	    }
	    while (input >= 100) {
	        s += "C";
	        input -= 100;
	    }
	    while (input >= 90) {
	        s += "XC";
	        input -= 90;
	    }
	    while (input >= 50) {
	        s += "L";
	        input -= 50;
	    }
	    while (input >= 40) {
	        s += "XL";
	        input -= 40;
	    }
	    while (input >= 10) {
	        s += "X";
	        input -= 10;
	    }
	    while (input >= 9) {
	        s += "IX";
	        input -= 9;
	    }
	    while (input >= 5) {
	        s += "V";
	        input -= 5;
	    }
	    while (input >= 4) {
	        s += "IV";
	        input -= 4;
	    }
	    while (input >= 1) {
	        s += "I";
	        input -= 1;
	    }    
	    return s;
	}
	
	private static String getStringPlaceHolder(Player player, TechCategory tcat, Technology t, String text, String playername)
	{
		String s = text;
		if(text.contains("%player%"))
		{
			s = s.replace("%player%", player.getName());
		}
		return ChatApi.tl(s);
	}
	
	private static String getStringPlaceHolderIFH(Player player, Technology t, String text,
			Account ac, int dg, boolean useSI, boolean useSy, String ts, String ds, String playername)
	{
		String s = text;
		/*if(text.contains("%accountname%"))
		{
			s = s.replace("%accountname%", (ac == null || ac.getID() == 0) ? "/" : ac.getAccountName());
		}*/
		SoloEntryQueryStatus eqs = (SoloEntryQueryStatus) plugin.getMysqlHandler().getData(Type.SOLOENTRYQUERYSTATUS,
				"`player_uuid` = ? AND `intern_name` = ? AND `entry_query_type` = ?",
				player.getUniqueId().toString(), t.getInternName(), EntryQueryType.TECHNOLOGY.toString());
		int techLevel = eqs == null ? 1 : eqs.getResearchLevel();
		int acquiredTech = plugin.getMysqlHandler().getCount(Type.SOLOENTRYQUERYSTATUS,
				"`player_uuid` = ? AND `entry_query_type` = ? AND `status_type` = ?",
				player.getUniqueId().toString(), EntryQueryType.TECHNOLOGY.toString(), StatusType.HAVE_RESEARCHED_IT);
		HashMap<String, Double> map = new HashMap<>();
		map.put("techlv", Double.valueOf(techLevel));
		map.put("techaq", Double.valueOf(acquiredTech));
		if(text.contains("%techcostmoney%"))
		{
			int moneyFrac = 0;
			double money = 0.0;
			if(t != null)
			{
				money = new MathFormulaParser().parse(t.getCostMoney(), map);
				moneyFrac = String.valueOf(money).split("\\.")[1].length();
			}
			s = s.replace("%techcostmoney%", t == null ? "/" : 
				plugin.getIFHEco().format(money, ac.getCurrency(), dg, moneyFrac, useSI, useSy, ts, ds));
		}
		return ChatApi.tl(s);
	}
	
	private static String getStringPlaceHolderVault(Player player, Technology t, String text, String playername)
	{
		String s = text;
		SoloEntryQueryStatus eqs = (SoloEntryQueryStatus) plugin.getMysqlHandler().getData(Type.SOLOENTRYQUERYSTATUS,
				"`player_uuid` = ? AND `intern_name` = ? AND `entry_query_type` = ?",
				player.getUniqueId().toString(), t.getInternName(), EntryQueryType.TECHNOLOGY.toString());
		int techLevel = eqs == null ? 1 : eqs.getResearchLevel();
		int acquiredTech = plugin.getMysqlHandler().getCount(Type.SOLOENTRYQUERYSTATUS,
				"`player_uuid` = ? AND `entry_query_type` = ? AND `status_type` = ?",
				player.getUniqueId().toString(), EntryQueryType.TECHNOLOGY.toString(), StatusType.HAVE_RESEARCHED_IT);
		HashMap<String, Double> map = new HashMap<>();
		map.put("techlv", Double.valueOf(techLevel));
		map.put("techaq", Double.valueOf(acquiredTech));
		if(text.contains("%techcostmoney%"))
		{
			double money = 0.0;
			if(t != null)
			{
				money = new MathFormulaParser().parse(t.getCostMoney(), map);
			}
			s = s.replace("%techcostmoney%%", t == null ? "/" : 
				String.valueOf(money)+" "+ plugin.getVaultEco().currencyNamePlural());
		}
		return ChatApi.tl(s);
	}
	
	private static String getBoolean(boolean boo)
	{
		return boo ? plugin.getYamlHandler().getLang().getString("IsTrue") : plugin.getYamlHandler().getLang().getString("IsFalse");
	}
	
	private static ClickFunction[] getClickFunction(YamlConfiguration y, String pathBase)
	{
		ArrayList<ClickFunction> ctar = new ArrayList<>();
		List<ClickType> list = new ArrayList<ClickType>(EnumSet.allOf(ClickType.class));
		for(ClickType ct : list)
		{
			if(y.get(pathBase+".ClickFunction."+ct.toString()) == null)
			{
				continue;
			}
			ClickFunctionType cft = null;
			try
			{
				cft = ClickFunctionType.valueOf(y.getString(pathBase+".ClickFunction."+ct.toString()));
			} catch(Exception e)
			{
				continue;
			}
			ctar.add(new ClickFunction(ct, cft));
		}
		return ctar.toArray(new ClickFunction[ctar.size()]);
	}
	
	/*public static String getSpawnEggType(Material mat)
	{
		String s = "";
		switch(mat)
		{
		default: break;
		case ALLAY_SPAWN_EGG:
		case AXOLOTL_SPAWN_EGG:
		case BAT_SPAWN_EGG:
		case BEE_SPAWN_EGG:
		case BLAZE_SPAWN_EGG:
		case CAT_SPAWN_EGG:
		case CAVE_SPIDER_SPAWN_EGG:
		case CHICKEN_SPAWN_EGG:
		case COD_SPAWN_EGG:
		case COW_SPAWN_EGG:
		case CREEPER_SPAWN_EGG:
		case DOLPHIN_SPAWN_EGG:
		case DONKEY_SPAWN_EGG:
		case DROWNED_SPAWN_EGG:
		case ELDER_GUARDIAN_SPAWN_EGG:
		case ENDERMAN_SPAWN_EGG:
		case ENDERMITE_SPAWN_EGG:
		case EVOKER_SPAWN_EGG:
		case FOX_SPAWN_EGG:
		case FROG_SPAWN_EGG:
		case FROGSPAWN:
		case GHAST_SPAWN_EGG:
		case GLOW_SQUID_SPAWN_EGG:
		case GOAT_SPAWN_EGG:
		case GUARDIAN_SPAWN_EGG:
		case HOGLIN_SPAWN_EGG:
		case HORSE_SPAWN_EGG:
		case HUSK_SPAWN_EGG:
		case LLAMA_SPAWN_EGG:
		case MAGMA_CUBE_SPAWN_EGG:
		case MOOSHROOM_SPAWN_EGG:
		case MULE_SPAWN_EGG:
		case OCELOT_SPAWN_EGG:
		case PANDA_SPAWN_EGG:
		case PARROT_SPAWN_EGG:
		case PHANTOM_SPAWN_EGG:
		case PIG_SPAWN_EGG:
		case PIGLIN_BRUTE_SPAWN_EGG:
		case PIGLIN_SPAWN_EGG:
		case PILLAGER_SPAWN_EGG:
		case POLAR_BEAR_SPAWN_EGG:
		case PUFFERFISH_SPAWN_EGG:
		case RABBIT_SPAWN_EGG:
		case RAVAGER_SPAWN_EGG:
		case SALMON_SPAWN_EGG:
		case SHEEP_SPAWN_EGG:
		case SHULKER_SPAWN_EGG:
		case SILVERFISH_SPAWN_EGG:
		case SKELETON_HORSE_SPAWN_EGG:
		case SKELETON_SPAWN_EGG:
		case SLIME_SPAWN_EGG:
		case SPIDER_SPAWN_EGG:
		case SQUID_SPAWN_EGG:
		case STRAY_SPAWN_EGG:
		case STRIDER_SPAWN_EGG:
		case TADPOLE_SPAWN_EGG:
		case TRADER_LLAMA_SPAWN_EGG:
		case TROPICAL_FISH_SPAWN_EGG:
		case TURTLE_SPAWN_EGG:
		case VEX_SPAWN_EGG:
		case VILLAGER_SPAWN_EGG:
		case VINDICATOR_SPAWN_EGG:
		case WANDERING_TRADER_SPAWN_EGG:
		case WARDEN_SPAWN_EGG:
		case WITCH_SPAWN_EGG:
		case WITHER_SKELETON_SPAWN_EGG:
		case WOLF_SPAWN_EGG:
		case ZOGLIN_SPAWN_EGG:
		case ZOMBIE_HORSE_SPAWN_EGG:
		case ZOMBIE_SPAWN_EGG:
		case ZOMBIE_VILLAGER_SPAWN_EGG:
		case ZOMBIFIED_PIGLIN_SPAWN_EGG:
			s = (plugin.getEnumTl() != null 
				? SaLE.getPlugin().getEnumTl().getLocalization(mat)
				: mat.toString()); break;
		}
		return s;
	}*/
}