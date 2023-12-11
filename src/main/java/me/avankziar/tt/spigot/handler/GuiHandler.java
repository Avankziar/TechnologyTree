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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
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
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

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
import main.java.me.avankziar.tt.spigot.handler.BlockHandler.BlockType;
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
import net.md_5.bungee.api.ChatColor;

public class GuiHandler
{
	private static TT plugin = TT.getPlugin();
	public static String MAINCATEGORY = "maincategory";
	public static String SUBCATEGORY = "subcategory";
	public static String TECHNOLOGY = "technology";
	public static String PAT = "playerassociatedtype";
	
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
		GUIApi gui = new GUIApi(plugin.pluginName, gt.toString(), null, 6, ChatApi.tl(title), st);
		openGui(mcat, scat, pat, player, gt, gui, st, closeInv);
	}
	
	public static void openStart(Player player, SettingsLevel settingsLevel, Inventory inv, boolean closeInv)
	{
		GuiType gt = GuiType.START;
		GUIApi gui = new GUIApi(plugin.pluginName, inv, gt.toString(), 
				settingsLevel == null ? SettingsLevel.BASE : settingsLevel);
		openGui(null, null, null, player, gt, gui, settingsLevel, closeInv);
	}
	
	public static void openStartMCat(Player player, SettingsLevel settingsLevel, Inventory inv, boolean closeInv, PlayerAssociatedType pat)
	{
		GuiType gt = GuiType.MAIN_CATEGORY;
		GUIApi gui = new GUIApi(plugin.pluginName, inv, gt.toString(), 
				settingsLevel == null ? SettingsLevel.BASE : settingsLevel);
		openGui(null, null, pat, player, gt, gui, settingsLevel, closeInv);
	}
	
	public static void openMainCatSubCat(Player player, SettingsLevel settingsLevel, Inventory inv, boolean closeInv, MainCategory mcat)
	{
		GuiType gt = GuiType.SUB_CATEGORY;
		GUIApi gui = new GUIApi(plugin.pluginName, inv, gt.toString(), 
				settingsLevel == null ? SettingsLevel.BASE : settingsLevel);
		openGui(mcat, null, mcat.getPlayerAssociatedType(), player, gt, gui, settingsLevel, closeInv);
	}
	
	public static void openSubCTech(Player player, SettingsLevel settingsLevel, Inventory inv, boolean closeInv, SubCategory scat)
	{
		GuiType gt = GuiType.TECHNOLOGY;
		GUIApi gui = new GUIApi(plugin.pluginName, inv, gt.toString(), 
				settingsLevel == null ? SettingsLevel.BASE : settingsLevel);
		openGui(null, scat, scat.getPlayerAssociatedType(), player, gt, gui, settingsLevel, closeInv);
	}
	
	/*public static void openInputInfo(SignShop ssh, Player player, SettingsLevel settingsLevel, boolean closeInv)
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
		is.setItemMeta(im);
		if(y.get(parentPath+".ArmorMeta.TrimMaterial") != null 
				&& y.get(parentPath+".ArmorMeta.TrimPattern") != null 
				&& im instanceof ArmorMeta)
		{
			ArmorMeta ima = (ArmorMeta) im;
			try
			{
				ima.setTrim(new ArmorTrim(getTrimMaterial(y.getString(parentPath+".ArmorMeta.TrimMaterial")),
						getTrimPattern(y.getString(parentPath+".ArmorMeta.TrimPattern"))));
			} catch(Exception e)
			{
				ima.setTrim(new ArmorTrim(TrimMaterial.IRON, TrimPattern.WILD));
			}
			is.setItemMeta(ima);
			im = is.getItemMeta();
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
	
	@SuppressWarnings("deprecation")
	private static void openGui(MainCategory mcat, SubCategory scat, PlayerAssociatedType pat, Player player, GuiType gt, GUIApi gui,
			SettingsLevel settingsLevel, boolean closeInv)
	{
		switch(gt)
		{
		case START:
			break;
		case MAIN_CATEGORY:
			LinkedHashMap<String, MainCategory> maincmap = null;
			ClickFunctionType cftm = ClickFunctionType.MAINCATEGORYS_SUBCATEGORYS_SOLO;
			switch(pat)
			{
			case GLOBAL:
				maincmap = CatTechHandler.mainCategoryMapGlobal;
				cftm = ClickFunctionType.MAINCATEGORYS_SUBCATEGORYS_GLOBAL; break;
			case GROUP:
				maincmap = CatTechHandler.mainCategoryMapGroup;
				cftm = ClickFunctionType.MAINCATEGORYS_SUBCATEGORYS_GROUP; break;
			case SOLO:
				maincmap = CatTechHandler.mainCategoryMapSolo;
				cftm = ClickFunctionType.MAINCATEGORYS_SUBCATEGORYS_SOLO; break;
			}
			int mj = 0;
			for(Entry<String, MainCategory> ee : maincmap.entrySet())
			{
				if(mj >= 53)
				{
					break;
				}
				LinkedHashMap<ItemStack, Boolean> isb = PlayerHandler.canSeeOrResearch_ForGUI(
														player, player.getUniqueId(), pat, ee.getValue(), null, null);
				if(isb == null)
				{
					mj++;
					continue;
				}
				for(Entry<ItemStack, Boolean> eee : isb.entrySet())
				{
					ItemStack iss = eee.getKey();
					LinkedHashMap<String, Entry<GUIApi.Type, Object>> map = new LinkedHashMap<>();
					map.put(MAINCATEGORY, new AbstractMap.SimpleEntry<GUIApi.Type, Object>(GUIApi.Type.STRING,
							ee.getValue() != null ? ee.getValue().getInternName() : ""));
					map.put(PAT, new AbstractMap.SimpleEntry<GUIApi.Type, Object>(GUIApi.Type.STRING,
							ee.getValue() != null ? ee.getValue().getPlayerAssociatedType().toString() : ""));
					ArrayList<ClickFunction> ctar = new ArrayList<>();
					if(eee.getValue() != null && eee.getValue() == true)
					{
						ctar.add(new ClickFunction(ClickType.LEFT, cftm));
						ctar.add(new ClickFunction(ClickType.RIGHT, cftm));
					}
					gui.add(ee.getValue().getGuiSlot(), iss, settingsLevel, true, true, map, ctar.toArray(new ClickFunction[ctar.size()]));
				}
				mj++;
			}
			break;
		case SUB_CATEGORY:
			LinkedHashMap<Integer, SubCategory> subcmap = null;
			ClickFunctionType cfts = ClickFunctionType.SUBCATEGORYS_TECHNOLOGYS_SOLO;
			switch(mcat.getPlayerAssociatedType())
			{
			case GLOBAL:
				subcmap = CatTechHandler.mainCategorySubCategoryMapGlobal.get(mcat.getInternName());
				cfts = ClickFunctionType.SUBCATEGORYS_TECHNOLOGYS_GLOBAL; break;
			case GROUP:
				subcmap = CatTechHandler.mainCategorySubCategoryMapGroup.get(mcat.getInternName());
				cfts = ClickFunctionType.SUBCATEGORYS_TECHNOLOGYS_GROUP; break;
			case SOLO:
				subcmap = CatTechHandler.mainCategorySubCategoryMapSolo.get(mcat.getInternName());
				cfts = ClickFunctionType.SUBCATEGORYS_TECHNOLOGYS_SOLO; break;
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
					ItemStack iss = eee.getKey();
					LinkedHashMap<String, Entry<GUIApi.Type, Object>> map = new LinkedHashMap<>();
					map.put(SUBCATEGORY, new AbstractMap.SimpleEntry<GUIApi.Type, Object>(GUIApi.Type.STRING,
							ee.getValue() != null ? ee.getValue().getInternName() : ""));
					map.put(PAT, new AbstractMap.SimpleEntry<GUIApi.Type, Object>(GUIApi.Type.STRING,
							ee.getValue() != null ? ee.getValue().getPlayerAssociatedType().toString() : ""));
					ArrayList<ClickFunction> ctar = new ArrayList<>();
					if(eee.getValue() != null && eee.getValue() == true)
					{
						ctar.add(new ClickFunction(ClickType.LEFT, cfts));
						ctar.add(new ClickFunction(ClickType.RIGHT, cfts));
					}
					gui.add(ii, iss, settingsLevel, true, true, map, ctar.toArray(new ClickFunction[ctar.size()]));
				}
				sj++;
			}
			break;
		case TECHNOLOGY:
			LinkedHashMap<Integer, Technology> techmap = null;
			ClickFunctionType cft = null;
			switch(scat.getPlayerAssociatedType())
			{
			case GLOBAL:
				techmap = CatTechHandler.subCategoryTechnologyMapGlobal.get(scat.getInternName());
				cft = ClickFunctionType.RESEARCH_TECHNOLOGY_GLOBAL;	break;
			case GROUP:
				techmap = CatTechHandler.subCategoryTechnologyMapGroup.get(scat.getInternName());
				cft = ClickFunctionType.RESEARCH_TECHNOLOGY_GROUP; break;
			case SOLO:
				techmap = CatTechHandler.subCategoryTechnologyMapSolo.get(scat.getInternName());
				cft = ClickFunctionType.RESEARCH_TECHNOLOGY_SOLO; break;
			}
			int tj = 0;
			for(Entry<Integer, Technology> ee : techmap.entrySet())
			{
				if(tj >= 53)
				{
					TT.log.info("GuiHandler tj >= 53"); //REMOVEME
					break;
				}
				int ii = ee.getKey();
				LinkedHashMap<ItemStack, Boolean> isb = PlayerHandler.canSeeOrResearch_ForGUI(
						player, player.getUniqueId(), pat, null, null, ee.getValue());
				if((ii < 0 && ii > 53) || isb == null)
				{
					tj++;
					TT.log.info("GuiHandler (ii < 0 && ii > 53) || isb == null"); //REMOVEME
					continue;
				}
				for(Entry<ItemStack, Boolean> eee : isb.entrySet())
				{
					ItemStack iss = eee.getKey();
					LinkedHashMap<String, Entry<GUIApi.Type, Object>> map = new LinkedHashMap<>();
					map.put(TECHNOLOGY, new AbstractMap.SimpleEntry<GUIApi.Type, Object>(GUIApi.Type.STRING,
							ee.getValue().getInternName()));
					map.put(PAT, new AbstractMap.SimpleEntry<GUIApi.Type, Object>(GUIApi.Type.STRING,
							ee.getValue() != null ? ee.getValue().getPlayerAssociatedType().toString() : ""));
					ArrayList<ClickFunction> ctar = new ArrayList<>();
					if(eee.getValue() != null && eee.getValue() == true)
					{
						ctar.add(new ClickFunction(ClickType.LEFT, cft));
						ctar.add(new ClickFunction(ClickType.RIGHT, ClickFunctionType.INFO_TECHNOLOGY));
					} else if(eee.getValue() != null && eee.getValue() == false)
					{
						ctar.add(new ClickFunction(ClickType.RIGHT, ClickFunctionType.INFO_TECHNOLOGY));
					}
					gui.add(ii, iss, settingsLevel, true, true, map, ctar.toArray(new ClickFunction[ctar.size()]));
					TT.log.info("GuiHandler gui.add: ii:"+ii+" | iss.getType:"+iss.getType().toString()); //REMOVEME
				}
				tj++;
			}
			break;
		}
		boolean fillNotDefineGuiSlots = new ConfigHandler().fillNotDefineGuiSlots();
		Material filler = Material.valueOf(plugin.getConfig().getString("Do.Gui.FillerItemMaterial", "LIGHT_GRAY_STAINED_GLASS_PANE"));
		YamlConfiguration y = plugin.getYamlHandler().getGui(gt);
		for(int i = 0; i < 54; i++)
		{
			SettingsLevel itemSL = SettingsLevel.NOLEVEL;
			if(y.get(i+".SettingLevel") != null)
			{
				itemSL = SettingsLevel.valueOf(y.getString(i+".SettingLevel"));
			}
			if(y.get(i+".Material") == null 
					&& y.get(i+".Material."+settingsLevel.toString()) == null
					&& y.get(i+".Material."+itemSL.toString()) == null)
			{
				filler(gui, i, filler, fillNotDefineGuiSlots);
				continue;
			}
			if(settingsLevel.getOrdinal() < itemSL.getOrdinal())
			{
				filler(gui, i, filler, fillNotDefineGuiSlots);
				continue;
			}
			if(y.get(i+".Permission") != null)
			{
				if(!ModifierValueEntry.hasPermission(player, y.getString(i+".Permission")))
				{
					filler(gui, i, filler, fillNotDefineGuiSlots);
					continue;
				}
			}
			if(y.get(i+".IFHDepend") != null)
			{
				if(y.getBoolean(i+".IFHDepend"))
				{
					if(plugin.getIFHEco() == null)
					{
						filler(gui, i, filler, fillNotDefineGuiSlots);
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
			} else if(y.get(i+".Material."+itemSL.toString()) != null)
			{
				mat = Material.valueOf(y.getString(i+".Material."+itemSL.toString()));
				if(mat == Material.PLAYER_HEAD && y.getString(i+"."+itemSL.toString()+".PlayerHeadTexture") != null)
				{
					is = getSkull(y.getString(i+"."+itemSL.getName()+".PlayerHeadTexture"), 1);
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
					filler(gui, i, filler, fillNotDefineGuiSlots);
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
			displayname = getStringPlaceHolder(player, mcat, scat, null, displayname, playername, 0, 0);
			if(is == null)
			{
				is = new ItemStack(mat, amount);
			} else
			{
				is.setAmount(amount);
			}
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(ChatApi.tl(displayname));
			if(y.get(i+".ItemFlag") != null)
			{
				for(String s : y.getStringList(i+".ItemFlag"))
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
					for(String s : y.getStringList(i+".Enchantment"))
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
				if(y.get(i+".Enchantment") != null)
				{
					for(String s : y.getStringList(i+".Enchantment"))
					{
						String[] split = s.split(";");
						if(split.length != 2)
						{
							filler(gui, i, filler, fillNotDefineGuiSlots);
							continue;
						}					
						try
						{
							im.addEnchant(Enchantment.getByName(split[0]), Integer.parseInt(split[1]), true);
						} catch(Exception e)
						{
							filler(gui, i, filler, fillNotDefineGuiSlots);
							continue;
						}
					}
				}
			}
			if(lore != null)
			{
				im.setLore(lore);
			}
			is.setItemMeta(im);			
			switch(gt)
			{
			case START:
				LinkedHashMap<String, Entry<GUIApi.Type, Object>> stmap = new LinkedHashMap<>();
				ClickFunction[] cfts = getClickFunction(y, String.valueOf(i));
				for(ClickFunction c : cfts)
				{
					if(c.getFunction().equalsIgnoreCase(ClickFunctionType.START_MAINCATEGORYS_GLOBAL.toString()))
					{
						stmap.put(PAT, new AbstractMap.SimpleEntry<GUIApi.Type, Object>(GUIApi.Type.STRING,
								PlayerAssociatedType.GLOBAL.toString()));
						break;
					} else if(c.getFunction().equalsIgnoreCase(ClickFunctionType.START_MAINCATEGORYS_GROUP.toString()))
					{
						stmap.put(PAT, new AbstractMap.SimpleEntry<GUIApi.Type, Object>(GUIApi.Type.STRING,
								PlayerAssociatedType.GROUP.toString()));
						break;
					} else if(c.getFunction().equalsIgnoreCase(ClickFunctionType.START_MAINCATEGORYS_SOLO.toString()))
					{
						stmap.put(PAT, new AbstractMap.SimpleEntry<GUIApi.Type, Object>(GUIApi.Type.STRING,
								PlayerAssociatedType.SOLO.toString()));
						break;
					}
				}
				gui.add(i, is, settingsLevel, true, true, stmap, cfts);
				break;
			case MAIN_CATEGORY:
				LinkedHashMap<String, Entry<GUIApi.Type, Object>> map = new LinkedHashMap<>();
				map.put(PAT, new AbstractMap.SimpleEntry<GUIApi.Type, Object>(GUIApi.Type.STRING,
						pat.toString()));
				gui.add(i, is, settingsLevel, true, true, map, getClickFunction(y, String.valueOf(i)));
				break;
			case SUB_CATEGORY:
				LinkedHashMap<String, Entry<GUIApi.Type, Object>> smap = new LinkedHashMap<>();
				smap.put(PAT, new AbstractMap.SimpleEntry<GUIApi.Type, Object>(GUIApi.Type.STRING,
						pat.toString()));
				gui.add(i, is, settingsLevel, true, true, smap, getClickFunction(y, String.valueOf(i)));
				break;
			case TECHNOLOGY:
				LinkedHashMap<String, Entry<GUIApi.Type, Object>> tmap = new LinkedHashMap<>();
				tmap.put(MAINCATEGORY, new AbstractMap.SimpleEntry<GUIApi.Type, Object>(GUIApi.Type.STRING,
						scat != null ? scat.getOverlyingCategory() : ""));
				tmap.put(PAT, new AbstractMap.SimpleEntry<GUIApi.Type, Object>(GUIApi.Type.STRING,
						pat.toString()));
				gui.add(i, is, settingsLevel, true, true, tmap, getClickFunction(y, String.valueOf(i)));
				break;
			}
		}
		new BukkitRunnable()
		{
			
			@Override
			public void run()
			{
				if(closeInv)
				{
					player.closeInventory();
				}
				gui.open(player, gt);
			}
		}.runTask(plugin);
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
		int totalSoloTechs = plugin.getMysqlHandler().getCount(Type.SOLOENTRYQUERYSTATUS,
				"`player_uuid` = ? AND `entry_query_type` = ? AND `status_type` = ?",
				player.getUniqueId().toString(), EntryQueryType.TECHNOLOGY.toString(), EntryStatusType.HAVE_RESEARCHED_IT.toString());
		int totalGlobalTechs = plugin.getMysqlHandler().getCount(Type.GLOBALENTRYQUERYSTATUS,
				"`entry_query_type` = ? AND `status_type` = ?",
				EntryQueryType.TECHNOLOGY.toString(), EntryStatusType.HAVE_RESEARCHED_IT.toString());
		boolean papi = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null 
				&& Bukkit.getPluginManager().getPlugin("PlaceholderAPI").isEnabled();
		for(String s : lore)
		{
			String a = getStringPlaceHolder(player, mcat, scat, t, s, playername,
					totalSoloTechs, totalGlobalTechs);
			if(a == null)
			{
				continue;
			}
			if(plugin.getIFHEco() != null)
			{
				Account ac = plugin.getIFHEco().getDefaultAccount(player.getUniqueId(), AccountCategory.MAIN,
						plugin.getIFHEco().getDefaultCurrency(CurrencyType.DIGITAL));
				int dg = ac == null ? 0 : plugin.getIFHEco().getDefaultGradationQuantity(ac.getCurrency());
				boolean useSI = ac == null ? false : plugin.getIFHEco().getDefaultUseSIPrefix(ac.getCurrency());
				boolean useSy = ac == null ? false : plugin.getIFHEco().getDefaultUseSymbol(ac.getCurrency());
				String ts = ac == null ? "." : plugin.getIFHEco().getDefaultThousandSeperator(ac.getCurrency());
				String ds = ac == null ? "," : plugin.getIFHEco().getDefaultDecimalSeperator(ac.getCurrency());
				a = getStringPlaceHolderIFH(player, t, a, ac, dg, useSI, useSy, ts, ds, playername,
						totalSoloTechs, totalGlobalTechs);
				if(a == null)
				{
					continue;
				}
			} else
			{
				a = getStringPlaceHolderVault(player, t, a, playername,
						totalSoloTechs, totalGlobalTechs);
				if(a == null)
				{
					continue;
				}
			}
			if(papi)
			{
				a = ChatApi.tl(me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, a));
			}
			list.add(ChatApi.tl(a));
		}
		return list;
	}
	
	private static String getStringPlaceHolder(Player player, MainCategory mcat, SubCategory scat, Technology t, String text, String playername,
			int totalSoloTechs, int totalGlobalTechs)
	{
		String s = text;
		if(text.contains("%player%"))
		{
			s = s.replace("%player%", player.getName());
		}
		PlayerData pd = PlayerHandler.getPlayer(player.getUniqueId());
		if(text.contains("%syncmsg%"))
		{
			s = s.replace("%syncmsg%", pd.isShowSyncMessage() 
					? plugin.getYamlHandler().getLang().getString("IsTrue") : plugin.getYamlHandler().getLang().getString("IsFalse"));
		}
		if(text.contains("%rewardmsg%"))
		{
			s = s.replace("%rewardmsg%", pd.isShowRewardMessage() 
					? plugin.getYamlHandler().getLang().getString("IsTrue") : plugin.getYamlHandler().getLang().getString("IsFalse"));
		}
		if(text.contains("%freettexp%"))
		{
			s = s.replace("%freettexp%", String.valueOf(pd.getActualTTExp()));
		}
		if(text.contains("%allocatedttexp%"))
		{
			s = s.replace("%allocatedttexp%", String.valueOf((pd.getTotalReceivedTTExp()-pd.getActualTTExp())));
		}
		if(text.contains("%techhave%"))
		{
			int c = plugin.getMysqlHandler().getCount(Type.SOLOENTRYQUERYSTATUS,
						"`player_uuid` = ? AND `entry_query_type` = ? AND `status_type` = ?",
						player.getUniqueId().toString(), EntryQueryType.TECHNOLOGY.toString(), EntryStatusType.HAVE_RESEARCHED_IT.toString())
					+
					plugin.getMysqlHandler().getCount(Type.GLOBALENTRYQUERYSTATUS,
						"`entry_query_type` = ? AND `status_type` = ?",
						EntryQueryType.TECHNOLOGY.toString(), EntryStatusType.HAVE_RESEARCHED_IT.toString());
			s = s.replace("%techhave%",  String.valueOf(c));
		}
		if(text.contains("%techexist%"))
		{
			s = s.replace("%techexist%", String.valueOf(CatTechHandler.totalTech));
		}
		if(text.contains("%solotechhave%"))
		{
			int c = plugin.getMysqlHandler().getCount(Type.SOLOENTRYQUERYSTATUS,
					"`player_uuid` = ? AND `entry_query_type` = ? AND `status_type` = ?",
					player.getUniqueId().toString(), EntryQueryType.TECHNOLOGY.toString(), EntryStatusType.HAVE_RESEARCHED_IT.toString());
			s = s.replace("%solotechhave%", String.valueOf(c));
		}
		if(text.contains("%solotechexist%"))
		{
			s = s.replace("%solotechexist%", String.valueOf(CatTechHandler.totalSoloTech));
		}
		if(text.contains("%grouptechhave%"))
		{
			s = s.replace("%grouptechhave%", "0"); //TODO
		}
		if(text.contains("%grouptechexist%"))
		{
			s = s.replace("%grouptechexist%", String.valueOf(CatTechHandler.totalGroupTech));
		}
		if(text.contains("%globaltechhave%"))
		{
			int c = plugin.getMysqlHandler().getCount(Type.GLOBALENTRYQUERYSTATUS,
					"`entry_query_type` = ? AND `status_type` = ?",
					EntryQueryType.TECHNOLOGY.toString(), EntryStatusType.HAVE_RESEARCHED_IT.toString());
			s = s.replace("%globaltechhave%", String.valueOf(c));
		}
		if(text.contains("%globaltechexist%"))
		{
			s = s.replace("%globaltechexist%", String.valueOf(CatTechHandler.totalGlobalTech));
		}
		if(text.contains("%brewing_standhas%"))
		{
			BlockType bt = BlockType.BREWING_STAND;
			int c = plugin.getMysqlHandler().getCount(Type.REGISTEREDBLOCK,
					"`player_uuid` = ? AND `block_type` = ?",
					player.getUniqueId().toString(), bt.toString());
			s = s.replace("%brewing_standhas%", String.valueOf(c));
		}
		if(text.contains("%brewing_standmax%"))
		{
			BlockType bt = BlockType.BREWING_STAND;
			int c = BlockHandler.getMaxRegisteredBlocks(player, bt);
			s = s.replace("%brewing_standmax%", String.valueOf(c));
		}
		/*if(text.contains("%enchanting_tablehas%")) REMOVEME Do not needed
		{
			BlockType bt = BlockType.ENCHANTING_TABLE;
			int c = plugin.getMysqlHandler().getCount(Type.REGISTEREDBLOCK,
					"`player_uuid` = ? AND `block_type` = ?",
					player.getUniqueId().toString(), bt.toString());
			s = s.replace("%enchanting_tablehas%", String.valueOf(c));
		}
		if(text.contains("%enchanting_tablemax%"))
		{
			BlockType bt = BlockType.ENCHANTING_TABLE;
			int c = BlockHandler.getMaxRegisteredBlocks(player, bt);
			s = s.replace("%enchanting_tablemax%", String.valueOf(c));
		}*/
		if(text.contains("%campfirehas%"))
		{
			BlockType bt = BlockType.CAMPFIRE;
			int c = plugin.getMysqlHandler().getCount(Type.REGISTEREDBLOCK,
					"`player_uuid` = ? AND `block_type` = ?",
					player.getUniqueId().toString(), bt.toString());
			s = s.replace("%campfirehas%", String.valueOf(c));
		}
		if(text.contains("%campfiremax%"))
		{
			BlockType bt = BlockType.CAMPFIRE;
			int c = BlockHandler.getMaxRegisteredBlocks(player, bt);
			s = s.replace("%campfiremax%", String.valueOf(c));
		}
		if(text.contains("%furnacehas%"))
		{
			BlockType bt = BlockType.FURNACE;
			int c = plugin.getMysqlHandler().getCount(Type.REGISTEREDBLOCK,
					"`player_uuid` = ? AND `block_type` = ?",
					player.getUniqueId().toString(), bt.toString());
			s = s.replace("%furnacehas%", String.valueOf(c));
		}
		if(text.contains("%furnacemax%"))
		{
			BlockType bt = BlockType.FURNACE;
			int c = BlockHandler.getMaxRegisteredBlocks(player, bt);
			s = s.replace("%furnacemax%", String.valueOf(c));
		}
		if(text.contains("%blast_furnacehas%"))
		{
			BlockType bt = BlockType.BLASTFURNACE;
			int c = plugin.getMysqlHandler().getCount(Type.REGISTEREDBLOCK,
					"`player_uuid` = ? AND `block_type` = ?",
					player.getUniqueId().toString(), bt.toString());
			s = s.replace("%blast_furnacehas%", String.valueOf(c));
		}
		if(text.contains("%blast_furnacemax%"))
		{
			BlockType bt = BlockType.BLASTFURNACE;
			int c = BlockHandler.getMaxRegisteredBlocks(player, bt);
			s = s.replace("%blast_furnacemax%", String.valueOf(c));
		}
		if(text.contains("%smokerhas%"))
		{
			BlockType bt = BlockType.SMOKER;
			int c = plugin.getMysqlHandler().getCount(Type.REGISTEREDBLOCK,
					"`player_uuid` = ? AND `block_type` = ?",
					player.getUniqueId().toString(), bt.toString());
			s = s.replace("%smokerhas%", String.valueOf(c));
		}
		if(text.contains("%smokermax%"))
		{
			BlockType bt = BlockType.SMOKER;
			int c = BlockHandler.getMaxRegisteredBlocks(player, bt);
			s = s.replace("%smokermax%", String.valueOf(c));
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
			if(text.contains("%maxtechlev%"))
			{
				s = s.replace("%maxtechlev%", String.valueOf(t.getMaximalTechnologyLevelToResearch()));
			}
			if(text.contains("%acquiredtechlev%") || text.contains("%rawcostttexp%")
					|| text.contains("%rawcostttexp%") || text.contains("%costttexp%")
					|| text.contains("%rawcostvanillaexp%") || text.contains("%costvanillaexp%") 
					|| text.contains("%rawcostmoney%")
					|| text.contains("%rawcostmaterial%") || text.contains("%costmaterial%"))
			{
				int techLevel = 0;
				int acquiredTech = 0;
				if(t.getPlayerAssociatedType() == PlayerAssociatedType.SOLO)
				{
					ArrayList<SoloEntryQueryStatus> highestEntryResearchedList = SoloEntryQueryStatus.convert(plugin.getMysqlHandler().getList(Type.SOLOENTRYQUERYSTATUS,
							"`research_level` DESC", 0, 1,
							"`player_uuid` = ? AND `intern_name` = ? AND `entry_query_type` = ?",
							player.getUniqueId().toString(), t.getInternName(), EntryQueryType.TECHNOLOGY.toString()));
					SoloEntryQueryStatus eqs = highestEntryResearchedList.size() == 0 ? null : highestEntryResearchedList.get(0);
					techLevel = eqs == null ? 1 
							: (eqs.getStatusType() == EntryStatusType.HAVE_RESEARCHED_IT ? eqs.getResearchLevel() + 1 : eqs.getResearchLevel()); //Tech which may to acquire
					acquiredTech = eqs == null ? 0 : techLevel - 1; //Tech which was already acquire
				} else
				{
					ArrayList<GlobalEntryQueryStatus> highestEntryResearchedList = GlobalEntryQueryStatus.convert(plugin.getMysqlHandler().getList(Type.GLOBALENTRYQUERYSTATUS,
							"`research_level` DESC", 0, 1,
							"`intern_name` = ? AND `entry_query_type` = ?",
							t.getInternName(), EntryQueryType.TECHNOLOGY.toString()));
					GlobalEntryQueryStatus eqs = highestEntryResearchedList.size() == 0 ? null : highestEntryResearchedList.get(0);
					techLevel = eqs == null ? 1 
							: (eqs.getStatusType() == EntryStatusType.HAVE_RESEARCHED_IT ? eqs.getResearchLevel() + 1 : eqs.getResearchLevel()); //Tech which may to acquire
					acquiredTech = eqs == null ? 0 : techLevel - 1; //Tech which was already acquire
				}
				if(text.contains("%acquiredtechlev%"))
				{
					s = s.replace("%acquiredtechlev%", String.valueOf(acquiredTech));
				}
				HashMap<String, Double> map = new HashMap<>();
				map.put(PlayerHandler.TECHLEVEL, (double) techLevel);
				map.put(PlayerHandler.TECHACQUIRED, (double) acquiredTech);
				map.put(PlayerHandler.SOLOTOTALTECH, (double) totalSoloTechs);
				map.put(PlayerHandler.GLOBALTOTALTECH, (double) totalGlobalTechs);
				if(text.contains("%rawcostttexp%"))
				{
					if(t.getCostTTExp().get(techLevel) == null)
					{
						return s = s.replace("%rawcostttexp%", "0");
					}
					double ttexp = 0;
					if(pd != null && !t.getCostTTExp().isEmpty())
					{
						ttexp =  new MathFormulaParser().parse(t.getCostTTExp().get(techLevel), map);
						TT.log.info("costTTExp: | "+ttexp+" | "+t.getCostTTExp().get(techLevel));
					}
					s = s.replace("%rawcostttexp%", String.valueOf(ttexp));
				}
				if(text.contains("%costttexp%"))
				{
					if(t.getCostTTExp().get(techLevel) == null)
					{
						s = s.replace("%costttexp%", "0 TTExp");
					}
					double ttexp = 0;
					if(pd != null && !t.getCostTTExp().isEmpty())
					{
						ttexp = new MathFormulaParser().parse(t.getCostTTExp().get(techLevel), map);
						TT.log.info("costTTExp: | "+ttexp+" | "+t.getCostTTExp().get(techLevel));
					}
					s = s.replace("%costttexp%", String.valueOf(ttexp)+" TTExp");
				}
				if(text.contains("%rawcostvanillaexp%"))
				{
					if(t.getCostVanillaExp().get(techLevel) == null)
					{
						s = s.replace("%rawcostvanillaexp%", "0");
					}
					int vexp = 0;
					if(!t.getCostVanillaExp().isEmpty())
					{
						vexp = (int) Math.floor(new MathFormulaParser().parse(t.getCostVanillaExp().get(techLevel), map));
					}
					s = s.replace("%rawcostvanillaexp%", String.valueOf(vexp));
				}
				if(text.contains("%costvanillaexp%") && t.getCostVanillaExp().get(techLevel) != null)
				{
					if(t.getCostVanillaExp().get(techLevel) == null)
					{
						s = s.replace("%rawcostvanillaexp%", "0 VanillaExp");
					}
					int vexp = 0;
					if(!t.getCostVanillaExp().isEmpty())
					{
						vexp = (int) Math.floor(new MathFormulaParser().parse(t.getCostVanillaExp().get(techLevel), map));
					}
					s = s.replace("%costvanillaexp%", String.valueOf(vexp)+" VanillaExp");
				}
				if(text.contains("%rawcostmoney%"))
				{
					if(t.getCostMoney().get(techLevel) == null)
					{
						return null;
					}
					double money = 0;
					if(!t.getCostMoney().isEmpty())
					{
						money = new MathFormulaParser().parse(t.getCostMoney().get(techLevel), map);
					}
					s = s.replace("%rawcostmoney%", String.valueOf(money));
				}
				if(text.contains("%rawcostmaterial%"))
				{
					if(t.getCostMaterial().get(techLevel) == null)
					{
						return null;
					}
					StringBuilder sb = new StringBuilder();
					for(Entry<Material, String> e : t.getCostMaterial().get(techLevel).entrySet())
					{
						if(!sb.isEmpty())
						{
							sb.append(", ");
						}
						int material = Integer.parseInt(e.getValue());
						sb.append(material+"x "+ e.getKey().toString());
					}
					s = s.replace("%rawcostmaterial%", sb.toString());
				}
				if(text.contains("%costmaterial%"))
				{
					if(t.getCostMaterial().get(techLevel) == null)
					{
						return null;
					}
					StringBuilder sb = new StringBuilder();
					for(Entry<Material, String> e : t.getCostMaterial().get(techLevel).entrySet())
					{
						if(!sb.isEmpty())
						{
							sb.append(", ");
						}
						int material = Integer.parseInt(e.getValue());
						sb.append(material+"x "+ (TT.getPlugin().getEnumTl() != null
								  				? TT.getPlugin().getEnumTl().getLocalization(e.getKey())
								  				: e.getKey().toString()));
					}
					s = s.replace("%costmaterial%", sb.toString());
				}
			}
		}
		return s;
	}
	
	private static String getStringPlaceHolderIFH(Player player, Technology t, String text,
			Account ac, int dg, boolean useSI, boolean useSy, String ts, String ds, String playername,
			int totalSoloTechs, int totalGlobalTechs)
	{
		String s = text;
		/*if(text.contains("%accountname%"))
		{
			s = s.replace("%accountname%", (ac == null || ac.getID() == 0) ? "/" : ac.getAccountName());
		}*/
		if(t == null)
		{
			return s;
		}
		switch(t.getPlayerAssociatedType())
		{
		case SOLO:
			//TODO Das noch als Liste ändern und daher das Object herholen.
			SoloEntryQueryStatus seqs = (SoloEntryQueryStatus) plugin.getMysqlHandler().getData(Type.SOLOENTRYQUERYSTATUS,
					"`player_uuid` = ? AND `intern_name` = ? AND `entry_query_type` = ?",
					player.getUniqueId().toString(), t.getInternName(), EntryQueryType.TECHNOLOGY.toString());
			int techLevel = seqs == null ? 1 : seqs.getResearchLevel() + 1; //Tech which may to acquire
			int acquiredTech = seqs == null ? 0 : techLevel; //Tech which was already acquire
			HashMap<String, Double> map = new HashMap<>();
			map.put(PlayerHandler.TECHLEVEL, Double.valueOf(techLevel));
			map.put(PlayerHandler.TECHACQUIRED, Double.valueOf(acquiredTech));
			map.put(PlayerHandler.SOLOTOTALTECH, Double.valueOf(totalSoloTechs));
			map.put(PlayerHandler.GLOBALTOTALTECH, Double.valueOf(totalGlobalTechs));
			if(text.contains("%costmoney%"))
			{
				if(t.getCostMoney().get(techLevel) == null)
				{
					return null;
				}
				int moneyFrac = 0;
				double money = 0.0;
				if(t != null)
				{
					money = new MathFormulaParser().parse(t.getCostMoney().get(techLevel), map);
					moneyFrac = String.valueOf(money).split("\\.")[1].length();
				}
				s = s.replace("%costmoney%", t == null ? "/" : 
					ChatColor.stripColor(plugin.getIFHEco().format(money, ac.getCurrency(), dg, moneyFrac, useSI, useSy, ts, ds)));
			}
			break;
		case GROUP:
		case GLOBAL:
			//TODO noch machen
		}
		
		return s;
	}
	
	private static String getStringPlaceHolderVault(Player player, Technology t, String text, String playername,
			int totalSoloTechs, int totalGlobalTechs)
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
		map.put(PlayerHandler.TECHLEVEL, Double.valueOf(techLevel));
		map.put(PlayerHandler.TECHACQUIRED, Double.valueOf(acquiredTech));
		map.put(PlayerHandler.SOLOTOTALTECH, Double.valueOf(totalSoloTechs));
		map.put(PlayerHandler.GLOBALTOTALTECH, Double.valueOf(totalGlobalTechs));
		if(text.contains("%costmoney%"))
		{
			if(t.getCostMoney().get(techLevel) == null)
			{
				return null;
			}
			double money = 0.0;
			if(t != null)
			{
				money = new MathFormulaParser().parse(t.getCostMoney().get(techLevel), map);
			}
			s = s.replace("%costmoney%%", t == null ? "/" : 
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
	
	public static TrimMaterial getTrimMaterial(String s)
	{
		switch(s)
		{
		default:
			return TrimMaterial.IRON;
		case "AMETHYST":
			return TrimMaterial.AMETHYST;
		case "COPPER":
			return TrimMaterial.COPPER;
		case "DIAMOND":
			return TrimMaterial.DIAMOND;
		case "EMERALD":
			return TrimMaterial.EMERALD;
		case "GOLD":
			return TrimMaterial.GOLD;
		case "IRON":
			return TrimMaterial.IRON;
		case "LAPIS":
			return TrimMaterial.LAPIS;
		case "NETHERITE":
			return TrimMaterial.NETHERITE;
		case "QUARTZ":
			return TrimMaterial.QUARTZ;
		case "REDSTONE":
			return TrimMaterial.REDSTONE;
		}
	}
	
	public static TrimPattern getTrimPattern(String s)
	{
		switch(s)
		{
		default:
			return TrimPattern.WILD;
		case "COAST":
			return TrimPattern.COAST;
		case "DUNE":
			return TrimPattern.DUNE;
		case "EYE":
			return TrimPattern.EYE;
		case "HOST":
			return TrimPattern.HOST;
		case "RAISER":
			return TrimPattern.RAISER;
		case "RIB":
			return TrimPattern.RIB;
		case "SENTRY":
			return TrimPattern.SENTRY;
		case "SHAPER":
			return TrimPattern.SHAPER;
		case "SILENCE":
			return TrimPattern.SILENCE;
		case "SNOUT":
			return TrimPattern.SNOUT;
		case "SPIRE":
			return TrimPattern.SPIRE;
		case "TIDE":
			return TrimPattern.TIDE;
		case "VEX":
			return TrimPattern.VEX;
		case "WARD":
			return TrimPattern.WARD;
		case "WILD":
			return TrimPattern.WILD;
		}
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
	
	private static void filler(GUIApi gui, int i, Material mat, boolean fillNotDefineGuiSlots)
	{
		if(!fillNotDefineGuiSlots)
		{
			return;
		}
		ItemStack is = new ItemStack(mat, 1);
		ItemMeta im = is.getItemMeta();
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		im.addItemFlags(ItemFlag.HIDE_DESTROYS);
		im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		im.setDisplayName(ChatApi.tl("&0"));
		im.setLore(new ArrayList<>());
		is.setItemMeta(im);
		LinkedHashMap<String, Entry<GUIApi.Type, Object>> map = new LinkedHashMap<>();
		gui.add(i, is, SettingsLevel.NOLEVEL, true, false, map, new ClickFunction[0]);
	}
}