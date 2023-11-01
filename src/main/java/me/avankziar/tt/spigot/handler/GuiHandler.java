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

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.Player;
import org.bukkit.entity.TropicalFish;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.AxolotlBucketMeta;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.BookMeta.Generation;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.TropicalFishBucketMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import main.java.me.avankziar.ifh.general.economy.account.AccountCategory;
import main.java.me.avankziar.ifh.general.economy.currency.CurrencyType;
import main.java.me.avankziar.ifh.general.math.MathFormulaParser;
import main.java.me.avankziar.ifh.spigot.economy.account.Account;
import main.java.me.avankziar.tt.general.ChatApi;
import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.tt.spigot.gui.GUIApi;
import main.java.me.avankziar.tt.spigot.gui.events.ClickFunction;
import main.java.me.avankziar.tt.spigot.gui.objects.ClickFunctionType;
import main.java.me.avankziar.tt.spigot.gui.objects.ClickType;
import main.java.me.avankziar.tt.spigot.gui.objects.GuiType;
import main.java.me.avankziar.tt.spigot.gui.objects.SettingsLevel;
import main.java.me.avankziar.tt.spigot.modifiervalueentry.ModifierValueEntry;
import main.java.me.avankziar.tt.spigot.objects.EntryQueryType;
import main.java.me.avankziar.tt.spigot.objects.EntryStatusType;
import main.java.me.avankziar.tt.spigot.objects.PlayerAssociatedType;
import main.java.me.avankziar.tt.spigot.objects.mysql.GlobalEntryQueryStatus;
import main.java.me.avankziar.tt.spigot.objects.mysql.PlayerData;
import main.java.me.avankziar.tt.spigot.objects.mysql.SoloEntryQueryStatus;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.MainCategory;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.SubCategory;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.Technology;

public class GuiHandler
{
	private static TT plugin = TT.getPlugin();
	public static String MAINCATEGORY = "maincategory";
	public static String SUBCATEGORY = "subcategory";
	public static String TECHNOLOGY = "technology";
	
	public static void openCatOrTech(Player player, GuiType gt, MainCategory mcat, SubCategory scat,
			PlayerAssociatedType pat, SettingsLevel st, boolean closeInv)
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
					.replace("%maincat%", mcat != null ? mcat.getDisplayName() : "/"); break;
		case TECHNOLOGY:
			title = plugin.getYamlHandler().getLang().getString("GuiHandler.SubCategorysTechnologys.Title")
			.replace("%subcat%", scat != null ? scat.getDisplayName() : "/"); break;
		}
		GUIApi gui = new GUIApi(plugin.pluginName, gt.toString(), null, 6, title, st);
		openGui(mcat, scat, pat, player, gt, gui, st, closeInv);
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
	
	@SuppressWarnings("deprecation")
	public static ItemStack generateItem(YamlConfiguration y, String parentPath, int overrideAmount,
			MainCategory mcat, SubCategory scat, Technology t, PlayerAssociatedType pat, Player player)
	{
		if(y.get(parentPath+".Material") == null)
		{
			return null;
		}
		int amount = 1;
		if(y.get(parentPath+".Amount") != null)
		{
			amount = y.getInt(parentPath+".Amount");
		}
		if(overrideAmount > 0)
		{
			amount = overrideAmount;
		}
		Material mat = Material.valueOf(y.getString(parentPath+".Material"));
		ItemStack is = null;
		if(mat == Material.PLAYER_HEAD && y.get(parentPath+".HeadTexture") != null)
		{
			is = getSkull(y.getString(parentPath+".HeadTexture"), amount);
		} else
		{
			is = new ItemStack(mat, amount);
		}
		boolean papi = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null 
				&& Bukkit.getPluginManager().getPlugin("PlaceholderAPI").isEnabled();
		ItemMeta im = is.getItemMeta();
		if(y.get(parentPath+".Displayname") != null)
		{
			if(papi && player != null)
			{
				im.setDisplayName(ChatApi.tl(me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, y.getString(parentPath+".Displayname"))));
			} else
			{
				im.setDisplayName(ChatApi.tl(y.getString(parentPath+".Displayname")));
			}
		}
		if(y.get(parentPath+".CustomModelData") != null)
		{
			im.setCustomModelData(y.getInt(parentPath+".CustomModelData"));
		}
		if(y.get(parentPath+".ItemFlag") != null)
		{
			for(String s : y.getStringList(parentPath+".ItemFlag"))
			{
				try
				{
					im.addItemFlags(ItemFlag.valueOf(s));
				} catch(Exception e)
				{
					continue;
				}
			}
		}
		if(mat == Material.ENCHANTED_BOOK)
		{
			if(im instanceof EnchantmentStorageMeta)
			{
				EnchantmentStorageMeta esm = (EnchantmentStorageMeta) im;
				for(String s : y.getStringList(parentPath+".Enchantment"))
				{
					String[] split = s.split(";");
					if(split.length != 2)
					{
						continue;
					}					
					try
					{
						esm.addStoredEnchant(Enchantment.getByName(split[0]), Integer.parseInt(split[1]), true);
					} catch(Exception e)
					{
						continue;
					}
				}
				is.setItemMeta(esm);
				im = is.getItemMeta();
			}
		} else
		{
			if(y.get(parentPath+".Enchantment") != null)
			{
				for(String s : y.getStringList(parentPath+".Enchantment"))
				{
					String[] split = s.split(";");
					if(split.length != 2)
					{
						continue;
					}					
					try
					{
						im.addEnchant(Enchantment.getByName(split[0]), Integer.parseInt(split[1]), true);
					} catch(Exception e)
					{
						continue;
					}
				}
			}
		}
		if(y.get(parentPath+".Lore") != null)
		{
			ArrayList<String> lore = new ArrayList<>();
			for(String s : y.getStringList(parentPath+".Lore"))
			{
				String st = "";
				if(papi && player != null)
				{
					st = ChatApi.tl(me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, s));
				} else
				{
					st = ChatApi.tl(s);
				}
				lore.add(st);
			}
			lore = (ArrayList<String>) getLorePlaceHolder(player, mcat, scat, t, lore, player.getName());
			im.setLore(lore);
		}
		if(y.get(parentPath+".AxolotlBucket") != null && im instanceof AxolotlBucketMeta)
		{
			AxolotlBucketMeta imm = (AxolotlBucketMeta) im;
			try
			{
				imm.setVariant(Axolotl.Variant.valueOf(y.getString(parentPath+".AxolotlBucket")));
			} catch(Exception e)
			{
				imm.setVariant(Axolotl.Variant.BLUE);
			}
			is.setItemMeta(imm);
			im = is.getItemMeta();
		}
		if(y.get(parentPath+".Banner") != null && im instanceof BannerMeta)
		{
			BannerMeta imm = (BannerMeta) im;
			for(String s : y.getStringList(parentPath+".Banner"))
			{
				String[] split = s.split(";");
				if(split.length != 2)
				{
					continue;
				}
				try
				{
					imm.addPattern(new Pattern(DyeColor.valueOf(split[0]), PatternType.valueOf(split[1])));
				} catch(Exception e)
				{
					continue;
				}
			}
			is.setItemMeta(imm);
			im = is.getItemMeta();
		}
		if(im instanceof BookMeta)
		{
			BookMeta imm = (BookMeta) im;
			try
			{
				if(y.get(parentPath+".Book.Author") != null)
				{
					imm.setAuthor(y.getString(parentPath+".Book.Author"));
				}
				if(y.get(parentPath+".Book.Generation") != null)
				{
					imm.setGeneration(Generation.valueOf(y.getString(parentPath+".Book.Generation")));
				}
				if(y.get(parentPath+".Book.Title") != null)
				{
					imm.setTitle(ChatApi.tl(y.getString(parentPath+".Book.Title")));
				}
				is.setItemMeta(imm);
				im = is.getItemMeta();
			} catch(Exception e){}
		}
		if(y.get(parentPath+".Durability") != null && im instanceof Damageable)
		{
			Damageable imm = (Damageable) im;
			try
			{
				imm.setDamage(getMaxDamage(mat)-y.getInt(parentPath+".Durability"));
			} catch(Exception e)
			{
				imm.setDamage(0);
			}
			is.setItemMeta(imm);
			im = is.getItemMeta();
		}
		if(y.get(parentPath+".LeatherArmor.Color.Red") != null 
				&& y.get(parentPath+".LeatherArmor.Color.Green") != null 
				&& y.get(parentPath+".LeatherArmor.Color.Blue") != null 
				&& im instanceof LeatherArmorMeta)
		{
			LeatherArmorMeta imm = (LeatherArmorMeta) im;
			try
			{
				imm.setColor(Color.fromRGB(
						y.getInt(parentPath+".LeatherArmor.Color.Red"),
						y.getInt(parentPath+".LeatherArmor.Color.Green"),
						y.getInt(parentPath+".LeatherArmor.Color.Blue")));
				is.setItemMeta(imm);
				im = is.getItemMeta();
			} catch(Exception e){}
		}
		if(im instanceof PotionMeta)
		{
			PotionMeta imm = (PotionMeta) im;
			try
			{
				if(y.get(parentPath+".Potion.PotionEffectType") != null 
						&& y.get(parentPath+".Potion.Duration") != null 
						&& y.get(parentPath+".Potion.Amplifier") != null)
				{
					imm.addCustomEffect(new PotionEffect(
							PotionEffectType.getByName(y.getString(parentPath+".Potion.PotionEffectType")),
							y.getInt(parentPath+".Potion.Duration"),
							y.getInt(parentPath+".Potion.Amplifier")), true);
				}
				if(y.get(parentPath+".Potion.Color.Red") != null 
						&& y.get(parentPath+".Potion.Color.Green") != null 
						&& y.get(parentPath+".Potion.Color.Blue") != null)
				{
					imm.setColor(Color.fromRGB(
						y.getInt(parentPath+".Potion.Color.Red"),
						y.getInt(parentPath+".Potion.Color.Green"),
						y.getInt(parentPath+".Potion.Color.Blue")));
				}
				is.setItemMeta(imm);
				im = is.getItemMeta();
			} catch(Exception e){}
		}
		if(y.get(parentPath+".Repairable") != null && im instanceof Repairable)
		{
			Repairable imm = (Repairable) im;
			try
			{
				imm.setRepairCost(y.getInt(parentPath+".Repairable"));
				is.setItemMeta(imm);
				im = is.getItemMeta();
			} catch(Exception e){}
		}
		if(y.get(parentPath+".TropicalFishBucket.BodyColor") != null 
				&& y.get(parentPath+".TropicalFishBucket.Pattern") != null 
				&& y.get(parentPath+".TropicalFishBucket.PatternColor") != null 
				&& im instanceof TropicalFishBucketMeta)
		{
			TropicalFishBucketMeta imm = (TropicalFishBucketMeta) im;
			try
			{
				imm.setBodyColor(DyeColor.valueOf(y.getString(parentPath+".TropicalFishBucket.BodyColor")));
				imm.setPattern(TropicalFish.Pattern.valueOf(y.getString(parentPath+".TropicalFishBucket.Pattern")));
				imm.setPatternColor(DyeColor.valueOf(y.getString(parentPath+".TropicalFishBucket.PatternColor")));
				is.setItemMeta(imm);
				im = is.getItemMeta();
			} catch(Exception e){}
		}
		return is;
	}
	
	private static void openGui(MainCategory mcat, SubCategory scat, PlayerAssociatedType pat, Player player, GuiType gt, GUIApi gui,
			SettingsLevel settingsLevel, boolean closeInv)
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
					is = getSkull(y.getString(i+"."+settingsLevel.getName()+".PlayerHeadTexture"), 1);
				}
			} else
			{
				try
				{
					mat = Material.valueOf(y.getString(i+".Material"));
					if(mat == Material.PLAYER_HEAD && y.getString(i+".HeadTexture") != null)
					{
						is = getSkull(y.getString(i+".HeadTexture"), 1);
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
				lore = (ArrayList<String>) getLorePlaceHolder(player, mcat, scat, null, lore, playername);
			}
			String displayname = y.get(i+".Displayname") != null 
					? y.getString(i+".Displayname") 
					: (playername != null ? playername 
					: (TT.getPlugin().getEnumTl() != null
							  ? TT.getPlugin().getEnumTl().getLocalization(mat)
							  : is.getType().toString()));
			displayname = getStringPlaceHolder(player, mcat, scat, null, displayname, playername);
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
			switch(gt)
			{
			case START:
				LinkedHashMap<String, Entry<GUIApi.Type, Object>> stmap = new LinkedHashMap<>();
				gui.add(i, is, settingsLevel, true, stmap, getClickFunction(y, String.valueOf(i)));
				break;
			case MAIN_CATEGORY:
				break;
			case SUB_CATEGORY:
				LinkedHashMap<String, Entry<GUIApi.Type, Object>> smap = new LinkedHashMap<>();
				smap.put(MAINCATEGORY, new AbstractMap.SimpleEntry<GUIApi.Type, Object>(GUIApi.Type.STRING,
						mcat != null ? mcat.getInternName() : ""));
				gui.add(i, is, settingsLevel, true, smap, getClickFunction(y, String.valueOf(i)));
				break;
			case TECHNOLOGY:
				LinkedHashMap<String, Entry<GUIApi.Type, Object>> tmap = new LinkedHashMap<>();
				tmap.put(SUBCATEGORY, new AbstractMap.SimpleEntry<GUIApi.Type, Object>(GUIApi.Type.STRING,
						scat != null ? scat.getInternName() : ""));
				gui.add(i, is, settingsLevel, true, tmap, getClickFunction(y, String.valueOf(i)));
				break;
			}
		}
		switch(gt)
		{
		case START:
			break;
		case MAIN_CATEGORY:
			int mj = 0;
			for(Entry<Integer, MainCategory> ee : CatTechHandler.playerAssocMainCategoryMap.get(pat).entrySet())
			{
				if(mj >= 53)
				{
					break;
				}
				int ii = ee.getKey();
				LinkedHashMap<ItemStack, Boolean> isb = PlayerHandler.canSeeOrResearch_ForGUI(
														player, player.getUniqueId(), pat, ee.getValue(), null, null);
				if(isb == null)
				{
					mj++;
					continue;
				}
				for(Entry<ItemStack, Boolean> eee : isb.entrySet())
				{
					if(isb == null || (eee.getValue() != null && eee.getValue() == false))
					{
						continue;
					}
					ItemStack iss = eee.getKey();
					LinkedHashMap<String, Entry<GUIApi.Type, Object>> map = new LinkedHashMap<>();
					map.put(MAINCATEGORY, new AbstractMap.SimpleEntry<GUIApi.Type, Object>(GUIApi.Type.STRING,
							ee.getValue() != null ? ee.getValue().getInternName() : ""));
					ArrayList<ClickFunction> ctar = new ArrayList<>();
					ClickFunctionType cft = ClickFunctionType.MAINCATEGORYS_SUBCATEGORYS_SOLO;
					ctar.add(new ClickFunction(ClickType.LEFT, cft));
					ctar.add(new ClickFunction(ClickType.RIGHT, cft));
					gui.add(ii, iss, settingsLevel, true, map, ctar.toArray(new ClickFunction[ctar.size()]));
				}
				mj++;
			}
			break;
		case SUB_CATEGORY:
			LinkedHashMap<Integer, SubCategory> subcmap = null;
			switch(mcat.getPlayerAssociatedType())
			{
			case GLOBAL:
				subcmap = CatTechHandler.mainCategorySubCategoryMapGlobal.get(mcat.getInternName()); break;
			/*case GROUP:
				subcmap = CatTechHandler.mainCategorySubCategoryMapGroup.get(tcat.getInternName()); break;*/
			case SOLO:
				subcmap = CatTechHandler.mainCategorySubCategoryMapSolo.get(mcat.getInternName()); break;
			}
			int sj = 0;
			for(Entry<Integer, SubCategory> ee : subcmap.entrySet())
			{
				if(sj >= 53)
				{
					break;
				}
				int ii = ee.getKey();
				LinkedHashMap<ItemStack, Boolean> isb = PlayerHandler.canSeeOrResearch_ForGUI(
						player, player.getUniqueId(), pat, null, ee.getValue(), null);
				if(isb == null)
				{
					sj++;
				}
				for(Entry<ItemStack, Boolean> eee : isb.entrySet())
				{
					if(eee.getValue() != null && eee.getValue() == false)
					{
						continue;
					}
					ItemStack iss = eee.getKey();
					LinkedHashMap<String, Entry<GUIApi.Type, Object>> map = new LinkedHashMap<>();
					map.put(SUBCATEGORY, new AbstractMap.SimpleEntry<GUIApi.Type, Object>(GUIApi.Type.STRING,
							ee.getValue() != null ? ee.getValue().getInternName() : ""));
					ArrayList<ClickFunction> ctar = new ArrayList<>();
					ClickFunctionType cft = ClickFunctionType.SUBCATEGORYS_TECHNOLOGYS_SOLO;
					ctar.add(new ClickFunction(ClickType.LEFT, cft));
					ctar.add(new ClickFunction(ClickType.RIGHT, cft));
					gui.add(ii, iss, settingsLevel, true, map, ctar.toArray(new ClickFunction[ctar.size()]));
				}
				sj++;
			}
			break;
		case TECHNOLOGY:
			LinkedHashMap<Integer, Technology> techmap = null;
			switch(scat.getPlayerAssociatedType())
			{
			case GLOBAL:
				techmap = CatTechHandler.subCategoryTechnologyMapGlobal.get(scat.getInternName()); break;
			/*case GROUP:
				techmap = CatTechHandler.subCategoryTechnologyMapGroup.get(tcat.getInternName()); break;*/
			case SOLO:
				techmap = CatTechHandler.subCategoryTechnologyMapSolo.get(scat.getInternName()); break;
			}
			int tj = 0;
			for(Entry<Integer, Technology> ee : techmap.entrySet())
			{
				if(tj >= 53)
				{
					break;
				}
				int ii = ee.getKey();
				LinkedHashMap<ItemStack, Boolean> isb = PlayerHandler.canSeeOrResearch_ForGUI(
						player, player.getUniqueId(), pat, null, null, ee.getValue());
				if(isb == null)
				{
					tj++;
					continue;
				}
				for(Entry<ItemStack, Boolean> eee : isb.entrySet())
				{
					ItemStack iss = eee.getKey();
					LinkedHashMap<String, Entry<GUIApi.Type, Object>> map = new LinkedHashMap<>();
					map.put(TECHNOLOGY, new AbstractMap.SimpleEntry<GUIApi.Type, Object>(GUIApi.Type.STRING,
							ee.getValue().getInternName()));
					ArrayList<ClickFunction> ctar = new ArrayList<>();
					if(eee.getValue() != null && eee.getValue() == true)
					{
						ClickFunctionType cft = ClickFunctionType.RESEARCH_TECHNOLOGY;
						ctar.add(new ClickFunction(ClickType.LEFT, cft));
						ctar.add(new ClickFunction(ClickType.RIGHT, cft));
					}
					gui.add(ii, iss, settingsLevel, true, map, ctar.toArray(new ClickFunction[ctar.size()]));
				}
				tj++;
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
	public static ItemStack getSkull(String url, int amount) 
	{
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, amount, (short) 3);
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
	
	public static List<String> getLorePlaceHolder(Player player, MainCategory mcat, SubCategory scat, Technology t, List<String> lore, String playername)
	{
		List<String> list = new ArrayList<>();
		for(String s : lore)
		{
			String a = getStringPlaceHolder(player, mcat, scat, t, s, playername);
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
			list.add(ChatApi.tl(a));
		}
		return list;
	}
	
	private static String getStringPlaceHolder(Player player, MainCategory mcat, SubCategory scat, Technology t, String text, String playername)
	{
		String s = text;
		if(text.contains("%player%"))
		{
			s = s.replace("%player%", player.getName());
		}
		if(mcat != null)
		{
			//TODO
			/*
			 * 
			 */
		}
		if(scat != null)
		{
			//TODO
			/*
			 * 
			 */
		}
		if(t != null)
		{
			//TODO
			/**
			 * 
			 */
			if(text.contains("%rawcostttexp%") || text.contains("%costttexp%")
					|| text.contains("%rawcostvanillaexp%") || text.contains("%costvanillaexp%") 
					|| text.contains("%rawcostmoney%") || text.contains("%costmoney%")
					|| text.contains("%rawcostmaterial%"))
			{
				int techLevel = 0;
				int acquiredTech = 0;
				int totalSoloTechs = plugin.getMysqlHandler().getCount(Type.SOLOENTRYQUERYSTATUS,
						"`player_uuid` = ? AND `entry_query_type` = ? AND `status_type` = ?",
						player.getUniqueId().toString(), EntryQueryType.TECHNOLOGY.toString(), EntryStatusType.HAVE_RESEARCHED_IT.toString());
				int totalGlobalTechs = plugin.getMysqlHandler().getCount(Type.GLOBALENTRYQUERYSTATUS,
						"`entry_query_type` = ? AND `status_type` = ?",
						player.getUniqueId().toString(), EntryQueryType.TECHNOLOGY.toString(), EntryStatusType.HAVE_RESEARCHED_IT.toString());
				if(t.getPlayerAssociatedType() == PlayerAssociatedType.SOLO)
				{
					ArrayList<SoloEntryQueryStatus> eeqsList = SoloEntryQueryStatus.convert(plugin.getMysqlHandler().getList(Type.SOLOENTRYQUERYSTATUS,
							"`research_level` DESC", 0, 1,
							"`player_uuid` = ? AND `intern_name` = ? AND `entry_query_type` = ?",
							player.getUniqueId().toString(), t.getInternName(), EntryQueryType.TECHNOLOGY.toString()));
					SoloEntryQueryStatus eqs = eeqsList.size() == 0 ? null : eeqsList.get(0);
					techLevel = eqs == null ? 1 : eqs.getResearchLevel() + 1; //Tech which may to acquire
					acquiredTech = eqs == null ? 0 : techLevel; //Tech which was already acquire
				} else
				{
					ArrayList<GlobalEntryQueryStatus> eeqsList = GlobalEntryQueryStatus.convert(plugin.getMysqlHandler().getList(Type.GLOBALENTRYQUERYSTATUS,
							"`research_level` DESC", 0, 1,
							"`intern_name` = ? AND `entry_query_type` = ?",
							t.getInternName(), EntryQueryType.TECHNOLOGY.toString()));
					GlobalEntryQueryStatus eqs = eeqsList.size() == 0 ? null : eeqsList.get(0);
					techLevel = eqs == null ? 1 : eqs.getResearchLevel() + 1; //Tech which may to acquire
					acquiredTech = eqs == null ? 0 : techLevel; //Tech which was already acquire
				}
				PlayerData pd = PlayerHandler.getPlayer(player.getUniqueId());
				HashMap<String, Double> map = new HashMap<>();
				map.put("techlv", Double.valueOf(techLevel));
				map.put("techaq", Double.valueOf(acquiredTech));
				map.put("totalsolotech", Double.valueOf(totalSoloTechs));
				map.put("totalglobaltech", Double.valueOf(totalGlobalTechs));
				if(text.contains("%rawcostttexp%"))
				{
					double ttexp = 0;
					if(pd != null && !t.getCostTTExp().isEmpty())
					{
						ttexp =  new MathFormulaParser().parse(t.getCostTTExp(), map);
					}
					s = s.replace("%rawcostttexp%", String.valueOf(ttexp));
				}
				if(text.contains("%costttexp%"))
				{
					double ttexp = 0;
					if(pd != null && !t.getCostTTExp().isEmpty())
					{
						ttexp =  new MathFormulaParser().parse(t.getCostTTExp(), map);
					}
					s = s.replace("%costttexp%", String.valueOf(ttexp)+" TTExp");
				}
				if(text.contains("%rawcostvanillaexp%"))
				{
					int vexp = 0;
					if(!t.getCostVanillaExp().isEmpty())
					{
						vexp = (int) Math.floor(new MathFormulaParser().parse(t.getCostVanillaExp(), map));
					}
					s = s.replace("%rawcostvanillaexp%", String.valueOf(vexp));
				}
				if(text.contains("%costvanillaexp%"))
				{
					int vexp = 0;
					if(!t.getCostVanillaExp().isEmpty())
					{
						vexp = (int) Math.floor(new MathFormulaParser().parse(t.getCostVanillaExp(), map));
					}
					s = s.replace("%costvanillaexp%", String.valueOf(vexp)+" VanillaExp");
				}
				if(text.contains("%rawcostmoney%"))
				{
					double money = 0;
					if(!t.getCostMoney().isEmpty())
					{
						money = new MathFormulaParser().parse(t.getCostMoney(), map);
					}
					s = s.replace("%rawcostmoney%", String.valueOf(money));
				}
				if(text.contains("%costmoney%"))
				{
					double money = 0;
					if(!t.getCostMoney().isEmpty())
					{
						money = new MathFormulaParser().parse(t.getCostMoney(), map);
					}
					s = s.replace("%costmoney%", String.valueOf(money));
				}
				if(text.contains("%rawcostmaterial%"))
				{
					StringBuilder sb = new StringBuilder();
					int i = 0;
					int j = t.getCostMaterial().entrySet().size();
					for(Entry<Material, String> e : t.getCostMaterial().entrySet())
					{
						int material = (int) Math.floor(new MathFormulaParser().parse(e.getValue(), map));
						sb.append(material+"x "+  e.getKey().toString());
						if(i < j)
						{
							sb.append(", ");
						}
						i++;
					}
					s = s.replace("%rawcostmaterial%", sb.toString());
				}
				if(text.contains("%costmaterial%"))
				{
					StringBuilder sb = new StringBuilder();
					int i = 0;
					int j = t.getCostMaterial().entrySet().size();
					for(Entry<Material, String> e : t.getCostMaterial().entrySet())
					{
						int material = (int) Math.floor(new MathFormulaParser().parse(e.getValue(), map));
						sb.append(material+"x "+  TT.getPlugin().getEnumTl() != null
								  				? TT.getPlugin().getEnumTl().getLocalization(e.getKey())
								  				: e.getKey().toString());
						if(i < j)
						{
							sb.append(", ");
						}
						i++;
					}
					s = s.replace("%costmaterial%", sb.toString());
				}
			}
		}
		return s;
	}
	
	private static String getStringPlaceHolderIFH(Player player, Technology t, String text,
			Account ac, int dg, boolean useSI, boolean useSy, String ts, String ds, String playername)
	{
		String s = text;
		/*if(text.contains("%accountname%"))
		{
			s = s.replace("%accountname%", (ac == null || ac.getID() == 0) ? "/" : ac.getAccountName());
		}*/
		switch(t.getPlayerAssociatedType())
		{
		case SOLO:
			SoloEntryQueryStatus seqs = (SoloEntryQueryStatus) plugin.getMysqlHandler().getData(Type.SOLOENTRYQUERYSTATUS,
					"`player_uuid` = ? AND `intern_name` = ? AND `entry_query_type` = ?",
					player.getUniqueId().toString(), t.getInternName(), EntryQueryType.TECHNOLOGY.toString());
			int techLevel = seqs == null ? 1 : seqs.getResearchLevel() + 1; //Tech which may to acquire
			int acquiredTech = seqs == null ? 0 : techLevel; //Tech which was already acquire
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
			break;
		case GLOBAL:
		}
		
		return s;
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
				player.getUniqueId().toString(), EntryQueryType.TECHNOLOGY.toString(), EntryStatusType.HAVE_RESEARCHED_IT);
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
		return s;
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
	
	/*thanks https://stackoverflow.com/questions/12967896/converting-integers-to-roman-numerals-java
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
	}*/
	
	/*private static String getBoolean(boolean boo)
	{
		return boo ? plugin.getYamlHandler().getLang().getString("IsTrue") : plugin.getYamlHandler().getLang().getString("IsFalse");
	}*/
	
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
}