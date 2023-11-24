package main.java.me.avankziar.tt.spigot.handler;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.ifh.general.economy.account.AccountCategory;
import main.java.me.avankziar.ifh.general.economy.action.EconomyAction;
import main.java.me.avankziar.ifh.general.economy.currency.CurrencyType;
import main.java.me.avankziar.ifh.spigot.economy.account.Account;
import main.java.me.avankziar.ifh.spigot.economy.currency.EconomyCurrency;
import main.java.me.avankziar.tt.general.ChatApi;
import main.java.me.avankziar.tt.spigot.TT;
import main.java.me.avankziar.tt.spigot.assistance.Experience;
import main.java.me.avankziar.tt.spigot.cmdtree.BaseConstructor;
import main.java.me.avankziar.tt.spigot.event.PostRewardEvent;
import main.java.me.avankziar.tt.spigot.handler.RecipeHandler.RecipeType;
import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.RewardType;
import main.java.me.avankziar.tt.spigot.objects.ToolType;
import main.java.me.avankziar.tt.spigot.objects.mysql.PlayerData;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.RewardSummary;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.SimpleDropChance;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.SimpleUnlockedInteraction;

public class RewardHandler
{
	private static TT plugin = BaseConstructor.getPlugin();
	
	public static LinkedHashMap<UUID, LinkedHashMap<ToolType, LinkedHashMap<Material, LinkedHashMap<EventType, Double>>>> rewardMaterialMap = new LinkedHashMap<>();
	public static LinkedHashMap<UUID, LinkedHashMap<ToolType, LinkedHashMap<EntityType, LinkedHashMap<EventType, Double>>>> rewardEntityTypeMap = new LinkedHashMap<>();
	
	public static void doRewardJoinTask(Player player, long period)
	{
		new BukkitRunnable()
		{
			final UUID uuid = player.getUniqueId();
			@Override
			public void run()
			{
				if(Bukkit.getPlayer(uuid) == null)
				{
					cancel();
					return;
				}
				doRewardTask(uuid);
			}
		}.runTaskTimerAsynchronously(plugin, 0, period);
	}
	
	public static void doRewardOfflinePlayerTask()
	{
		if(!new ConfigHandler().rewardPayoutForOfflinePlayerActive())
		{
			return;
		}
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				ArrayList<UUID> alreadyUsed = new ArrayList<>();
				for(UUID uuid : rewardMaterialMap.keySet())
				{
					if(Bukkit.getPlayer(uuid) != null)
					{
						continue;
					}
					alreadyUsed.add(uuid);
					doRewardTask(uuid);
				}
				for(UUID uuid : rewardEntityTypeMap.keySet())
				{
					if(Bukkit.getPlayer(uuid) != null)
					{
						continue;
					}
					if(alreadyUsed.contains(uuid))
					{
						continue;
					}
					doRewardTask(uuid);
				}
			}
		}.runTaskTimerAsynchronously(plugin, 0, new ConfigHandler().rewardPayoutRepetitionRateForOfflinePlayer());
		
	}
	
	public static void doRewardTask(UUID uuid)
	{
		double toGiveTTExp = 0;
		double toGiveVanillaExp = 0;
		LinkedHashMap<String, Double> toGiveMoneyMap = new LinkedHashMap<>();
		LinkedHashMap<String, Double> toGiveCommandMap = new LinkedHashMap<>();
		final LinkedHashMap<ToolType, LinkedHashMap<Material, LinkedHashMap<EventType, Double>>> matMap = rewardMaterialMap.get(uuid);
		rewardMaterialMap.remove(uuid);
		final LinkedHashMap<ToolType, LinkedHashMap<EntityType, LinkedHashMap<EventType, Double>>> entityMap = rewardEntityTypeMap.get(uuid);
		rewardEntityTypeMap.remove(uuid);
		ArrayList<RewardSummary> rewardSummaryList = new ArrayList<>();
		if(matMap == null && entityMap == null)
		{
			TT.log.info("RewardHandler matMap == null && entityMap == null"); //REMOVEME
			return;
		}
		if(matMap != null)
		{
			for(Entry<ToolType, LinkedHashMap<Material, LinkedHashMap<EventType, Double>>> entry0 : matMap.entrySet())
			{
				ToolType tool = entry0.getKey();
				for(Entry<Material, LinkedHashMap<EventType, Double>> entry : matMap.get(tool).entrySet())
				{
					Material mat = entry.getKey();
					for(Entry<EventType, Double> entryII : entry.getValue().entrySet())
					{
						EventType et = entryII.getKey();
						double amount = entryII.getValue();
						if(PlayerHandler.materialInteractionMap.containsKey(uuid)
								&& PlayerHandler.materialInteractionMap.get(uuid).containsKey(tool)
								&& PlayerHandler.materialInteractionMap.get(uuid).get(tool).containsKey(mat)
								&& PlayerHandler.materialInteractionMap.get(uuid).get(tool).get(mat).containsKey(et))
							
						{
							SimpleUnlockedInteraction sui = PlayerHandler.materialInteractionMap.get(uuid).get(tool).get(mat).get(et);
							if(sui == null)
							{
								TT.log.info("RewardHandler sui == null"); //REMOVEME
								continue;
							}
							toGiveTTExp = toGiveTTExp + sui.getTechnologyExperience() * amount;
							toGiveVanillaExp = toGiveVanillaExp + sui.getVanillaExperience() * amount;
							for(Entry<String, Double> s : sui.getMoneyMap().entrySet())
							{
								TT.log.info("RewardHandler sui.MoneyMap: "+s.getKey()); //REMOVEME
								double moneyAmount = 0;
								if(toGiveMoneyMap.containsKey(s.getKey()))
								{
									moneyAmount = toGiveMoneyMap.get(s.getKey());
								}
								moneyAmount = moneyAmount + s.getValue() * amount;
								toGiveMoneyMap.put(s.getKey(), moneyAmount);
							}
							for(Entry<String, Double> s : sui.getCommandMap().entrySet())
							{
								double cmdAmount = 0;
								if(toGiveCommandMap.containsKey(s.getKey()))
								{
									cmdAmount = toGiveCommandMap.get(s.getKey());
								}
								cmdAmount = cmdAmount + s.getValue() * amount;
								toGiveCommandMap.put(s.getKey(), cmdAmount);
							}
							RewardSummary rs = new RewardSummary(et, mat, null, amount, toGiveVanillaExp, toGiveTTExp, toGiveMoneyMap, toGiveCommandMap);
							rewardSummaryList.add(rs);
						}
					}
				}
			}
		}
		if(entityMap != null)
		{
			for(Entry<ToolType, LinkedHashMap<EntityType, LinkedHashMap<EventType, Double>>> entry0 : entityMap.entrySet())
			{
				ToolType tool = entry0.getKey();
				for(Entry<EntityType, LinkedHashMap<EventType, Double>> entry : entityMap.get(tool).entrySet())
				{
					EntityType ent = entry.getKey();
					for(Entry<EventType, Double> entryII : entry.getValue().entrySet())
					{
						EventType et = entryII.getKey();
						double amount = entryII.getValue();
						if(PlayerHandler.entityTypeInteractionMap.containsKey(uuid)
								&& PlayerHandler.entityTypeInteractionMap.get(uuid).containsKey(tool)
								&& PlayerHandler.entityTypeInteractionMap.get(uuid).get(tool).containsKey(ent)
								&& PlayerHandler.entityTypeInteractionMap.get(uuid).get(tool).get(ent).containsKey(et))
							
						{
							SimpleUnlockedInteraction sui = PlayerHandler.entityTypeInteractionMap.get(uuid).get(tool).get(ent).get(et);
							if(sui == null)
							{
								TT.log.info("RewardHandler sui == null"); //REMOVEME
								continue;
							}
							toGiveTTExp = toGiveTTExp + sui.getTechnologyExperience() * amount;
							toGiveVanillaExp = toGiveVanillaExp + sui.getVanillaExperience() * amount;
							for(Entry<String, Double> s : sui.getMoneyMap().entrySet())
							{
								double moneyAmount = 0;
								if(toGiveMoneyMap.containsKey(s.getKey()))
								{
									moneyAmount = toGiveMoneyMap.get(s.getKey());
								}
								moneyAmount = moneyAmount + s.getValue() * amount;
								toGiveMoneyMap.put(s.getKey(), moneyAmount);
							}
							for(Entry<String, Double> s : sui.getCommandMap().entrySet())
							{
								double cmdAmount = 0;
								if(toGiveCommandMap.containsKey(s.getKey()))
								{
									cmdAmount = toGiveCommandMap.get(s.getKey());
								}
								cmdAmount = cmdAmount + s.getValue() * amount;
								toGiveCommandMap.put(s.getKey(), cmdAmount);
							}
							RewardSummary rs = new RewardSummary(et, null, ent, amount, toGiveVanillaExp, toGiveTTExp, toGiveMoneyMap, toGiveCommandMap);
							rewardSummaryList.add(rs);
						}
					}
				}
			}				
		}
		PlayerData pd = PlayerHandler.getPlayer(uuid);
		String playername = pd.getName();
		pd.setActualTTExp(pd.getActualTTExp() + toGiveTTExp);
		pd.setTotalReceivedTTExp(pd.getTotalReceivedTTExp() + toGiveTTExp);
		if(Bukkit.getPlayer(uuid) != null)
		{
			Experience.changeExp(Bukkit.getPlayer(uuid), (int) toGiveVanillaExp, false);
		} else
		{
			pd.setVanillaExpStillToBeObtained(pd.getVanillaExpStillToBeObtained() + (int) toGiveVanillaExp);
		}
		PlayerHandler.updatePlayer(pd);
		String toGiveMoney = "";
		for(Entry<String, Double> e : toGiveMoneyMap.entrySet())
		{
			TT.log.info("RewardHandler toGiveMoneyMap: "+e.getKey()); //REMOVEME
			if(e.getKey().equalsIgnoreCase("vault") && plugin.getVaultEco() != null)
			{
				plugin.getVaultEco().depositPlayer(Bukkit.getOfflinePlayer(uuid), e.getValue());
				toGiveMoney = e.getValue()+" "+plugin.getVaultEco().currencyNamePlural();
				continue;
			} else if(e.getKey().equalsIgnoreCase("vault") && plugin.getVaultEco() == null)
			{
				continue;
			}
			EconomyCurrency ec = null;
			if(e.getKey().equalsIgnoreCase("default"))
			{
				ec = plugin.getIFHEco().getDefaultCurrency(CurrencyType.DIGITAL);
			} else
			{
				ec = plugin.getIFHEco().getCurrency(e.getKey());
			}
			if(ec == null)
			{
				TT.log.info("RewardHandler ec == null : >"+e.getKey()+"<"); //REMOVEME
				continue;
			}
			Account ac = plugin.getIFHEco().getDefaultAccount(uuid, AccountCategory.JOB, ec);
			if(ac == null)
			{
				ac = plugin.getIFHEco().getDefaultAccount(uuid, AccountCategory.MAIN, ec);
				if(ac == null)
				{
					TT.log.info("RewardHandler ac == null"); //REMOVEME
					continue;
				}
			}
			Account tax = plugin.getIFHEco().getDefaultAccount(uuid, AccountCategory.TAX, ec);
			double taxation = new ConfigHandler().rewardPayoutTaxInPercent();
			EconomyAction ea = plugin.getIFHEco().deposit(ac, e.getValue(), taxation, true, tax);
			toGiveMoney = plugin.getIFHEco().format(ea.getDepositAmount(), ec);
		}
		for(Entry<String, Double> e : toGiveCommandMap.entrySet())
		{
			String[] split = e.getKey().split(":");
			if(split.length != 2)
			{
				TT.log.info("RewardHandler split.length != 2"); //REMOVEME
				continue;
			}
			if("spigot".equalsIgnoreCase(split[0]))
			{
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), split[1]
								.replace("%player%", playername)
								.replace("%value%", String.valueOf(e.getValue())));
			} else if("bungee".equalsIgnoreCase(split[0]) && plugin.getCommandToBungee() != null)
			{
				plugin.getCommandToBungee().executeAsConsole(split[1]
						.replace("%player%", playername)
						.replace("%value%", String.valueOf(e.getValue())));
			}
		}
		if(Bukkit.getPlayer(uuid) != null && pd.isShowRewardMessage())
		{
			Bukkit.getPlayer(uuid).sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Reward.TaskMsg")
					.replace("%ttexp%", String.valueOf(toGiveTTExp))
					.replace("%vaexp%", String.valueOf(toGiveVanillaExp))
					.replace("%money%", toGiveMoney)
					.replace("%times%", LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.systemDefault())
									   .format(DateTimeFormatter.ofPattern("HH:mm:ss")))));
		}
		PostRewardEvent postRewardEvent = new PostRewardEvent(uuid, playername, rewardSummaryList);
		Bukkit.getPluginManager().callEvent(postRewardEvent);
	}
	
	public static void rewardPlayer(UUID uuid, EventType eventType, ToolType tool, Material material, EntityType entityType, double amount)
	{
		if(material != null)
		{
			LinkedHashMap<ToolType, LinkedHashMap<Material, LinkedHashMap<EventType, Double>>> mapI = new LinkedHashMap<>();
			if(rewardMaterialMap.containsKey(uuid))
			{
				mapI = rewardMaterialMap.get(uuid);
			}
			LinkedHashMap<Material, LinkedHashMap<EventType, Double>> mapII = new LinkedHashMap<>();
			if(mapI.containsKey(tool))
			{
				mapII = mapI.get(tool);
			}
			LinkedHashMap<EventType, Double> mapIII = new LinkedHashMap<>();
			if(mapII.containsKey(material))
			{
				mapIII = mapII.get(material);
			}
			double d = amount;
			if(mapIII.containsKey(eventType))
			{
				d = d + mapIII.get(eventType);
			}
			mapIII.put(eventType, d);
			mapII.put(material, mapIII);
			mapI.put(tool, mapII);
			rewardMaterialMap.put(uuid, mapI);
		} else if(entityType != null)
		{
			LinkedHashMap<ToolType, LinkedHashMap<EntityType, LinkedHashMap<EventType, Double>>> mapI = new LinkedHashMap<>();
			if(rewardEntityTypeMap.containsKey(uuid))
			{
				mapI = rewardEntityTypeMap.get(uuid);
			}
			LinkedHashMap<EntityType, LinkedHashMap<EventType, Double>> mapII = new LinkedHashMap<>();
			if(mapI.containsKey(tool))
			{
				mapII = mapI.get(tool);
			}
			LinkedHashMap<EventType, Double> mapIII = new LinkedHashMap<>();
			if(mapII.containsKey(entityType))
			{
				mapIII = mapII.get(entityType);
			}
			double d = amount;
			if(mapIII.containsKey(eventType))
			{
				d = d + mapIII.get(eventType);
			}
			mapIII.put(eventType, d);
			mapII.put(entityType, mapIII);
			mapI.put(tool, mapII);
			rewardEntityTypeMap.put(uuid, mapI);
		}
	}
	
	public static boolean canAccessInteraction(Player player, ToolType toolType, EventType eventType, Material material, EntityType entityType) //Return true, if player cannot maniplulate a Block/Material
	{
		if(player == null || eventType == null)
		{
			return false;
		}
		UUID uuid = player.getUniqueId();
		if(material != null)
		{
			if(PlayerHandler.materialInteractionMap.containsKey(uuid)
					&& PlayerHandler.materialInteractionMap.get(uuid).containsKey(toolType)
					&& PlayerHandler.materialInteractionMap.get(uuid).get(toolType).containsKey(material)
					&& PlayerHandler.materialInteractionMap.get(uuid).get(toolType).get(material).containsKey(eventType))
			{
				boolean b = PlayerHandler.materialInteractionMap.get(uuid).get(toolType).get(material).get(eventType).isCanAccess();
				return b; /*? b : plugin.getValueEntry().getBooleanValueEntry(uuid, 
							CatTechHandler.getValueEntry(RewardType.ACCESS, eventType, material, entityType),
							plugin.getServername(), player.getWorld().getName());*/ //TODO Checken, warum das null gibt
			}
		} else if(entityType != null)
		{
			if(PlayerHandler.entityTypeInteractionMap.containsKey(uuid)
					&& PlayerHandler.entityTypeInteractionMap.get(uuid).containsKey(toolType)
					&& PlayerHandler.entityTypeInteractionMap.get(uuid).get(toolType).containsKey(entityType)
					&& PlayerHandler.entityTypeInteractionMap.get(uuid).get(toolType).get(entityType).containsKey(eventType))
			{
				boolean b = PlayerHandler.entityTypeInteractionMap.get(uuid).get(toolType).get(entityType).get(eventType).isCanAccess();
				return b ? b : plugin.getValueEntry().getBooleanValueEntry(uuid, 
						CatTechHandler.getValueEntry(RewardType.ACCESS, eventType, material, entityType),
						plugin.getServername(), player.getWorld().getName());
			}
		}
		return false;
	}
	
	public static boolean canAccessRecipe(UUID uuid, RecipeType rt, String key)
	{
		if(PlayerHandler.recipeMap.containsKey(uuid)
				&& PlayerHandler.recipeMap.get(uuid).containsKey(rt)
				&& PlayerHandler.recipeMap.get(uuid).get(rt).contains(key))
		{
			return true;
		}
		return false;
	}
	
	public static boolean useTTDropMechanicCalculation(Player player)
	{
		if(player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR)
		{
			return false;
		}
		if(!plugin.getYamlHandler().getConfig().getBoolean("Do.Drops.UsePluginDropsCalculation", false))
		{
			return false;
		}
		if(plugin.getYamlHandler().getConfig().getStringList("Do.Drops.DoNotUsePluginDropsCalculationWorlds").contains(player.getWorld().getName()))
		{
			return false;
		}
		//TODO Do Config.WorldList And Worldguard
		return true;
	}
	
	public static ArrayList<ItemStack> getDrops(Player player,
			EventType eventType, ToolType toolType, Material material, EntityType entityType)
	{
		//https://minecraft.fandom.com/wiki/Fortune
		boolean breakingThroughVanillaDropBarrier = plugin.getYamlHandler().getConfig().getBoolean("Do.Drops.BreakingThroughVanillaDropBarrier", true);
		boolean silkTouch = (player.getInventory().getItemInMainHand() != null &&
				player.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.SILK_TOUCH) > 0);
		final ToolType tool = toolType != null ? toolType : ToolType.getHandToolType(player);
		boolean playerUsedTools = ToolType.isTool(tool);
		if(!playerUsedTools)
		{
			silkTouch = false;
		}
		//https://minecraft.fandom.com/wiki/Luck
		int lucklevel = (player.getPotionEffect(PotionEffectType.LUCK) == null 
						? -1 
						: player.getPotionEffect(PotionEffectType.LUCK).getAmplifier()) + 1;
		UUID uuid = player.getUniqueId();
		ArrayList<ItemStack> list = new ArrayList<>();
		if(material != null)
		{
			//https://minecraft.fandom.com/wiki/Fortune
			int fortunelevel = -1;
			if(playerUsedTools && player.getInventory().getItemInMainHand() != null &&
					player.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) > 0)
			{
				fortunelevel = player.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
			}
			if(silkTouch)
			{
				if(PlayerHandler.materialSilkTouchDropMap.containsKey(uuid)
						&& PlayerHandler.materialSilkTouchDropMap.get(uuid).containsKey(tool)
						&& PlayerHandler.materialSilkTouchDropMap.get(uuid).get(tool).containsKey(material)
						&& PlayerHandler.materialSilkTouchDropMap.get(uuid).get(tool).get(material).containsKey(eventType))
				{
					for(SimpleDropChance sdc : PlayerHandler.materialSilkTouchDropMap.get(uuid).get(tool).get(material).get(eventType).values())
					{
						ItemStack is = getSingleDrops(player, sdc, fortunelevel, lucklevel, 
								breakingThroughVanillaDropBarrier, eventType, material, entityType);
						list.add(is);
					}
				}
			} else
			{
				if(PlayerHandler.materialDropMap.containsKey(uuid)
						&& PlayerHandler.materialDropMap.get(uuid).containsKey(tool)
						&& PlayerHandler.materialDropMap.get(uuid).get(tool).containsKey(material)
						&& PlayerHandler.materialDropMap.get(uuid).get(tool).get(material).containsKey(eventType))
				{
					for(SimpleDropChance sdc : PlayerHandler.materialDropMap.get(uuid).get(tool).get(material).get(eventType).values())
					{
						ItemStack is = getSingleDrops(player, sdc, fortunelevel, lucklevel,
								breakingThroughVanillaDropBarrier, eventType, material, entityType);
						list.add(is);
					}
				}
			}
		} else if(entityType != null)
		{
			int lootlevel = 0;
			if(eventType != EventType.FISHING)
			{
				if(playerUsedTools && player.getInventory().getItemInMainHand() != null &&
						player.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS) > 0)
				{
					lootlevel = player.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);
				}
			} else
			{
				if(player.getInventory().getItemInMainHand() != null &&
						player.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LUCK) > 0)
				{
					lootlevel = player.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LUCK);
				}
			}
			if(silkTouch)
			{
				if(PlayerHandler.entityTypeSilkTouchDropMap.containsKey(uuid)
						&& PlayerHandler.entityTypeSilkTouchDropMap.get(uuid).containsKey(tool)
						&& PlayerHandler.entityTypeSilkTouchDropMap.get(uuid).get(tool).containsKey(entityType)
						&& PlayerHandler.entityTypeSilkTouchDropMap.get(uuid).get(tool).get(entityType).containsKey(eventType))
				{
					for(SimpleDropChance sdc : PlayerHandler.entityTypeSilkTouchDropMap.get(uuid).get(tool).get(entityType).get(eventType).values())
					{
						ItemStack is = getSingleDrops(player, sdc, lootlevel, lucklevel,
								breakingThroughVanillaDropBarrier, eventType, material, entityType);
						list.add(is);
					}
				}
			} else
			{
				if(PlayerHandler.entityTypeDropMap.containsKey(uuid)
						&& PlayerHandler.entityTypeDropMap.get(uuid).containsKey(tool)
						&& PlayerHandler.entityTypeDropMap.get(uuid).get(tool).containsKey(entityType)
						&& PlayerHandler.entityTypeDropMap.get(uuid).get(tool).get(entityType).containsKey(eventType))
				{
					for(SimpleDropChance sdc : PlayerHandler.entityTypeDropMap.get(uuid).get(tool).get(entityType).get(eventType).values())
					{
						ItemStack is = getSingleDrops(player, sdc, lootlevel, lucklevel,
								breakingThroughVanillaDropBarrier, eventType, material, entityType);
						list.add(is);
					}
				}
			}
		}
		return list;
	}
	
	private static ItemStack getSingleDrops(Player player, SimpleDropChance sdc,
			int fortunelootlevel, int potionlucklevel,
			boolean breakingThroughVanillaDropBarrier,
			EventType eventType, Material material, EntityType entityType)
	{
		int i = 0;
		//int j = 0;
		Map<Integer, Double> sortedMap = 
	    	     sdc.getAmountToDropChance().entrySet().stream()
	    	    .sorted(Entry.comparingByKey())
	    	    .collect(Collectors.toMap(Entry::getKey, Entry::getValue,
	    	                              (e1, e2) -> e1, LinkedHashMap::new));
		for(Entry<Integer, Double> e : sortedMap.entrySet())
		{
			double chance = getChance(e.getValue(), fortunelootlevel, potionlucklevel);//-j*0.125;
			double r = new Random().nextDouble();
			//j++;
			if(r < chance)
			{
				i = e.getKey();
			} else
			{
				break;
			}
		}
		/*if(plugin.getModifier() != null)
		{
			i = (int) plugin.getModifier().getResult(player.getUniqueId(), (double) i,
					CatTechHandler.getModifier(RewardType.DROPS, eventType, material, entityType),
					plugin.getServername(), player.getWorld().getName());
		}*/ //TODO schauen ob es funktioniert.
		ItemStack is = sdc.getItem(player, i);
		if(!breakingThroughVanillaDropBarrier)
		{
			is.setAmount(getVanillaDropBarrier(is.getType(), i));
		}
		return is;
	}
	
	private static double getChance(double c, int fortunelootlevel, int potionlucklevel)
	{
		double chance = c;
		double fll = 0;
		double pll = 0;
		if(fortunelootlevel > 0 && potionlucklevel > 0)
		{
			fll = (1 / (1.0/((double)fortunelootlevel+2.0)+((double)fortunelootlevel+1.0)/2.0));
			pll = (1 / (1.0/((double)potionlucklevel+2.0) +((double)potionlucklevel+1.0)/2.0));
			chance = chance * (1 + (fll + pll));
			System.out.println("fll = " + fll + " | pll = " + pll + " >>> 1 + (fll + pll) = " + (1 + (fll + pll)));
		} else if(fortunelootlevel > 0)
		{
			fll = (1.0/((double)fortunelootlevel+2.0)+((double)fortunelootlevel+1.0)/2.0);
			chance = chance * (1 + (1 / fll));
			System.out.println("fll = " + fll + " >>> (1 + (1 / fll) = " + ((1 + (1 / fll))));
		} else if(potionlucklevel > 0)
		{
			pll = (1.0/((double)potionlucklevel+2.0) +((double)potionlucklevel+1.0)/2.0);
			chance = chance * (1 + (1 / pll));
			System.out.println("pll = " + pll + " >>> (0.42 + (1 / fll) = " + ((1 + (1 / pll))));
		}
		
		return chance;
	}
	
	public static void main(String[] args) //TestVersuch (in Eclipse) einer Rechnung um ein Stetigs Dropwachstum zu berechnen
    {
		/*HashMap<String, Double> map = new HashMap<>();
		map.put("techlev", (double) 0);
		map.put("techacq", (double) 0);
		map.put("solototaltech", (double) 0);
		map.put("globaltotaltech", (double) 0);
		
		double ttexp = new MathFormulaParser().parse("100 * techlev + 50 * techacq + 25 * solototaltech", map);
		System.out.println("ttexp : "+ttexp);*/
		
		boolean breakingThroughVanillaDropBarrier = false;
		int fortunelootlevel = 3;
		int potionlucklevel = 1;
		/*double lostExtraPercent = 0.25;
		if(fortunelootlevel > 0 && potionlucklevel > 0)
		{
			lostExtraPercent = 0.25
					* ( (1.0/((double)fortunelootlevel+2.0) ) + ( ((double)fortunelootlevel+1.0)/2.0) )
					* ( (1.0/((double)potionlucklevel+2.0) )  + ( ((double)potionlucklevel+1.0)/2.0) ); 
		} else if(fortunelootlevel > 0)
		{
			lostExtraPercent = 0.25 * (1.0/((double)fortunelootlevel+2.0)+((double)fortunelootlevel+1.0)/2.0);
		} else if(potionlucklevel > 0)
		{
			lostExtraPercent = 0.25 * (1.0/((double)potionlucklevel+2.0)+((double)potionlucklevel*2)/2.0);
		}
		System.out.println("lostExtraPercent = " + lostExtraPercent);*/
		int i = 0;
		LinkedHashMap<Integer, Double> map = new LinkedHashMap<>();
		map.put(2, 0.7);
		map.put(1, 0.9);
		map.put(4, 0.3);
		map.put(3, 0.5);
		Map<Integer, Double> sortedMap = 
	    	    map.entrySet().stream()
	    	    .sorted(Entry.comparingByKey())
	    	    .collect(Collectors.toMap(Entry::getKey, Entry::getValue,
	    	                              (e1, e2) -> e1, LinkedHashMap::new));
		int j = 0;
		int k = sortedMap.entrySet().size();
		for(Entry<Integer, Double> e : sortedMap.entrySet())
		{
			double chance = getChance(e.getValue(), fortunelootlevel, potionlucklevel)-j*0.125;
			double r = new Random().nextDouble();
			j++;
			System.out.println("NR: i = " + (i+1) + " | " + chance + " > " + r + " = " + (chance > r));
			if(r < chance)
			{
				i = e.getKey();
			} else
			{
				break;
			}
			if(j == k && i == e.getKey() && !breakingThroughVanillaDropBarrier)
			{
				//Ende der Map, maximal erreichte Dropzahl. Weiterführung in einer Schleife zur Erhöhung der Dropzahl, solange der Spieler glück hat
				double groundchance = chance - chance * 0.25;
				while(true)
				{
					groundchance = groundchance - groundchance; // * lostExtraPercent;
					double newchance = getChance(groundchance, fortunelootlevel, potionlucklevel);
					System.out.println("LOOP: i = " + (i+1) + " | chance = " + chance + " | gc = "+groundchance);
					if(newchance >= 1.0 || new Random().nextDouble() < newchance)
					{
						i++;
					} else
					{
						break;
					}
					if(i >= 10) 
					{
						break;
					}
				}
			}
		}
    }
	
	private static int getVanillaDropBarrier(Material material, int i)
	{
		switch(material)
		{
		default:
			return i;
		case PRISMARINE_CRYSTALS:
			return 3;
		case PRISMARINE_SHARD:
		case GLOWSTONE_DUST:
		case COAL:
		case DIAMOND:
		case EMERALD:
		case RAW_IRON:
		case RAW_GOLD:
		case QUARTZ:
			return 4;
		case SWEET_BERRIES:
		case BEETROOT_SEEDS:
			return 6;
		case NETHER_WART:
		case WHEAT_SEEDS:
			return 7;
		case REDSTONE:
			return 8;
		case MELON_SLICE:
			return 9;
		case AMETHYST_SHARD:
			return 16;
		case RAW_COPPER:
			return 20;
		case GOLD_NUGGET:
			return 24;
		case LAPIS_LAZULI:
			return 36;
		}
	}
}