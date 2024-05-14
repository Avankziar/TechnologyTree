package main.java.me.avankziar.tt.spigot.cmd.tt.info;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

import main.java.me.avankziar.ifh.general.economy.account.AccountCategory;
import main.java.me.avankziar.ifh.general.economy.currency.CurrencyType;
import main.java.me.avankziar.ifh.spigot.economy.account.Account;
import main.java.me.avankziar.ifh.spigot.economy.currency.EconomyCurrency;
import main.java.me.avankziar.tt.general.ChatApi;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentConstructor;
import main.java.me.avankziar.tt.spigot.cmdtree.ArgumentModule;
import main.java.me.avankziar.tt.spigot.handler.PlayerHandler;
import main.java.me.avankziar.tt.spigot.objects.EventType;
import main.java.me.avankziar.tt.spigot.objects.RewardType;
import main.java.me.avankziar.tt.spigot.objects.ToolType;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.SimpleUnlockedInteraction;

public class ARGInfo_Payment extends ArgumentModule
{	
	public ARGInfo_Payment(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
	}

	//tt info payment [material/entitytype] [tooltype]
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		Material mat = null;
		EntityType ent = null;
		ToolType tool = null;
		if(args.length >= 4)
		{
			try
			{
				tool = ToolType.valueOf(args[3]);
			} catch(Exception e)
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("")));
				return;
			}
		}
		if(args.length >= 3)
		{
			//Argument
			try
			{
				mat = Material.valueOf(args[2]);
			} catch(Exception e)
			{
				try
				{
					ent = EntityType.valueOf(args[2]);
				} catch(Exception e1)
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("")));
					return;
				}
			}
		} else if(player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInMainHand().getType() != Material.AIR)
		{
			//Inventory
			mat = player.getInventory().getItemInMainHand().getType();
		} else
		{
			//In Sight
			RayTraceResult rtr = player.getWorld().rayTrace(player.getEyeLocation(), player.getEyeLocation().getDirection(), 10.0,
									FluidCollisionMode.NEVER, false, 0.0, null);
			if(rtr == null)
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("")));
				return;
			} else if(rtr.getHitEntity() != null)
			{
				ent = rtr.getHitEntity().getType();
			} else if(rtr.getHitBlock() != null)
			{
				mat = rtr.getHitBlock().getType();
			}
		}
		if(ent != null)
		{
			entity(player, ent, tool);
		} else if(mat != null)
		{
			material(player, mat, tool);
		}
	}
	
	public static LinkedHashMap<ToolType, LinkedHashMap<Material, LinkedHashMap<EventType, Double>>> fakeRewardPlayer(EventType[] et, ToolType tool, Material material)
	{
		LinkedHashMap<ToolType, LinkedHashMap<Material, LinkedHashMap<EventType, Double>>> mapI = new LinkedHashMap<>();
		LinkedHashMap<Material, LinkedHashMap<EventType, Double>> mapII = new LinkedHashMap<>();
		LinkedHashMap<EventType, Double> mapIII = new LinkedHashMap<>();
		if(mapII.containsKey(material))
		{
			mapIII = mapII.get(material);
		}
		double d = 1.0;
		for(EventType eventType : et)
		{
			if(mapIII.containsKey(eventType))
			{
				d = d + mapIII.get(eventType);
			}
			mapIII.put(eventType, d);
		}
		mapII.put(material, mapIII);
		if(tool == null)
		{
			for(ToolType t : ToolType.values())
			{
				mapI.put(t, mapII);
			}
		} else
		{
			mapI.put(tool, mapII);
		}
		return mapI;
	}
	
	public static LinkedHashMap<ToolType, LinkedHashMap<EntityType, LinkedHashMap<EventType, Double>>> fakeRewardPlayer(EventType[] et, ToolType tool, EntityType entityType)
	{
		LinkedHashMap<ToolType, LinkedHashMap<EntityType, LinkedHashMap<EventType, Double>>> mapI = new LinkedHashMap<>();
		LinkedHashMap<EntityType, LinkedHashMap<EventType, Double>> mapII = new LinkedHashMap<>();
		LinkedHashMap<EventType, Double> mapIII = new LinkedHashMap<>();
		if(mapII.containsKey(entityType))
		{
			mapIII = mapII.get(entityType);
		}
		double d = 1.0;
		for(EventType eventType : et)
		{
			if(mapIII.containsKey(eventType))
			{
				d = d + mapIII.get(eventType);
			}
			mapIII.put(eventType, d);
		}
		mapII.put(entityType, mapIII);
		if(tool == null)
		{
			for(ToolType t : ToolType.values())
			{
				mapI.put(t, mapII);
			}
		} else
		{
			mapI.put(tool, mapII);
		}
		return mapI;
	}
	
	private void entity(Player player, EntityType ent, ToolType tool)
	{
		UUID uuid = player.getUniqueId();
		ArrayList<ToolType> tools = new ArrayList<>(Arrays.asList(ToolType.values()));
		if(tool != null)
		{
			tools = new ArrayList<>(Arrays.asList(tool));
		}
		ArrayList<String> tx = new ArrayList<>();
		tx.add(plugin.getYamlHandler().getLang().getString("Commands.Info.Payment.Headline")
				.replace("%value%", plugin.getEnumTl() != null ? plugin.getEnumTl().getLocalization(ent) : ent.toString()));
		tx.add(plugin.getYamlHandler().getLang().getString("Commands.Info.Payment.Info"));
		final LinkedHashMap<RewardType, LinkedHashMap<EventType, Double>> externBooster = PlayerHandler.externBoosterMap.get(uuid);
		final LinkedHashMap<EventType, Double> exBoTTExp = externBooster.get(RewardType.TECHNOLOGYTREE_EXP);
		final LinkedHashMap<EventType, Double> exBoVExp = externBooster.get(RewardType.VANILLA_EXP);
		final LinkedHashMap<EventType, Double> exBoMoney = externBooster.get(RewardType.MONEY);
		final LinkedHashMap<EventType, Double> exBoCmd = externBooster.get(RewardType.COMMAND);
		LinkedHashMap<ToolType, LinkedHashMap<EntityType, LinkedHashMap<EventType, Double>>> matMap = fakeRewardPlayer(EventType.values(), tool, ent);
		for(Entry<ToolType, LinkedHashMap<EntityType, LinkedHashMap<EventType, Double>>> entry0 : matMap.entrySet())
		{
			if(!tools.contains(entry0.getKey()))
			{
				continue;
			}
			for(Entry<EntityType, LinkedHashMap<EventType, Double>> entry : matMap.get(tool).entrySet())
			{
				if(ent != entry.getKey())
				{
					continue;
				}
				for(Entry<EventType, Double> entryII : entry.getValue().entrySet())
				{
					EventType et = entryII.getKey();
					tx.add(entry0.getKey().toString()+" => "+plugin.getYamlHandler().getLang().getString("Events."+et.toString()));
					double amount = entryII.getValue();
					double exBTTExp = exBoTTExp != null ? (exBoTTExp.get(et) != null ? exBoTTExp.get(et).doubleValue() : 1.0) : 1.0;
					double exBVExp = exBoVExp != null ? (exBoVExp.get(et) != null ? exBoVExp.get(et).doubleValue() : 1.0) : 1.0;
					double exBMoney = exBoMoney != null ? (exBoMoney.get(et) != null ? exBoMoney.get(et).doubleValue() : 1.0) : 1.0;
					double exBCmd = exBoCmd != null ? (exBoCmd.get(et) != null ? exBoCmd.get(et).doubleValue() : 1.0) : 1.0;
					if(PlayerHandler.entityTypeInteractionMap.containsKey(uuid)
							&& PlayerHandler.entityTypeInteractionMap.get(uuid).containsKey(tool)
							&& PlayerHandler.entityTypeInteractionMap.get(uuid).get(tool).containsKey(ent)
							&& PlayerHandler.entityTypeInteractionMap.get(uuid).get(tool).get(ent).containsKey(et))
						
					{
						SimpleUnlockedInteraction sui = PlayerHandler.entityTypeInteractionMap.get(uuid).get(tool).get(ent).get(et);
						if(sui == null)
						{
							continue;
						}
						double toGiveTTExp = sui.getTechnologyExperience() * amount * exBTTExp;
						tx.add(plugin.getYamlHandler().getLang().getString("Commands.Info.Payment.TTExp")
								.replace("%value%", String.valueOf(sui.getTechnologyExperience()))
								.replace("%externbooster%", String.valueOf(exBTTExp))
								.replace("%total%", String.valueOf(toGiveTTExp)));
						double toGiveVanillaExp = sui.getVanillaExperience() * amount * exBVExp;
						tx.add(plugin.getYamlHandler().getLang().getString("Commands.Info.Payment.VanillaExp")
								.replace("%value%", String.valueOf(sui.getVanillaExperience()))
								.replace("%externbooster%", String.valueOf(exBVExp))
								.replace("%total%", String.valueOf(toGiveVanillaExp)));
						for(Entry<String, Double> s : sui.getMoneyMap().entrySet())
						{
							double moneyAmount = s.getValue() * amount * exBMoney;
							String value = "";
							String total = "";
							if("vault".equals(s.getKey()))
							{
								value = plugin.getVaultEco() != null ? s.getValue()+" "+plugin.getVaultEco().currencyNamePlural() : s.getValue()+" Vault";
								total = plugin.getVaultEco() != null ? moneyAmount+" "+plugin.getVaultEco().currencyNamePlural() : moneyAmount+" Vault";
							} else
							{
								EconomyCurrency ec = null;
								if(s.getKey().equalsIgnoreCase("default"))
								{
									ec = plugin.getIFHEco().getDefaultCurrency(CurrencyType.DIGITAL);
								} else
								{
									ec = plugin.getIFHEco().getCurrency(s.getKey());
								}
								Account ac = plugin.getIFHEco().getDefaultAccount(uuid, AccountCategory.MAIN, ec);
								value = plugin.getIFHEco().format(s.getValue(), ec,
										plugin.getIFHEco().getDefaultGradationQuantity(ac.getCurrency()), plugin.getIFHEco().getDefaultDecimalPlaces(ac.getCurrency()),
										plugin.getIFHEco().getDefaultUseSIPrefix(ac.getCurrency()), plugin.getIFHEco().getDefaultUseSymbol(ac.getCurrency()),
										plugin.getIFHEco().getDefaultThousandSeperator(ac.getCurrency()), plugin.getIFHEco().getDefaultDecimalSeperator(ac.getCurrency()));
								total = plugin.getIFHEco().format(moneyAmount, ec,
										plugin.getIFHEco().getDefaultGradationQuantity(ac.getCurrency()), plugin.getIFHEco().getDefaultDecimalPlaces(ac.getCurrency()),
										plugin.getIFHEco().getDefaultUseSIPrefix(ac.getCurrency()), plugin.getIFHEco().getDefaultUseSymbol(ac.getCurrency()),
										plugin.getIFHEco().getDefaultThousandSeperator(ac.getCurrency()), plugin.getIFHEco().getDefaultDecimalSeperator(ac.getCurrency()));
							}
							tx.add(plugin.getYamlHandler().getLang().getString("Commands.Info.Payment.VanillaExp")
									.replace("%value%", value)
									.replace("%externbooster%", String.valueOf(exBMoney))
									.replace("%total%", total));
						}
						for(Entry<String, Double> s : sui.getCommandMap().entrySet())
						{
							double cmdAmount = s.getValue() * amount * exBCmd;
							tx.add(plugin.getYamlHandler().getLang().getString("Commands.Info.Payment.VanillaExp")
									.replace("%cmd%", s.getKey())
									.replace("%value%", String.valueOf(s.getValue()))
									.replace("%externbooster%", String.valueOf(exBCmd))
									.replace("%total%", String.valueOf(cmdAmount)));
						}						
					}
				}
			}
		}
		for(String s : tx)
		{
			player.sendMessage(ChatApi.tl(s));
		}
	}
	
	private void material(Player player, Material mat, ToolType tool)
	{
		UUID uuid = player.getUniqueId();
		ArrayList<ToolType> tools = new ArrayList<>(Arrays.asList(ToolType.values()));
		if(tool != null)
		{
			tools = new ArrayList<>(Arrays.asList(tool));
		}
		ArrayList<String> tx = new ArrayList<>();
		tx.add(plugin.getYamlHandler().getLang().getString("Commands.Info.Payment.Headline")
				.replace("%value%", plugin.getEnumTl() != null ? plugin.getEnumTl().getLocalization(mat) : mat.toString()));
		tx.add(plugin.getYamlHandler().getLang().getString("Commands.Info.Payment.Info"));
		final LinkedHashMap<RewardType, LinkedHashMap<EventType, Double>> externBooster = PlayerHandler.externBoosterMap.get(uuid);
		final LinkedHashMap<EventType, Double> exBoTTExp = externBooster.get(RewardType.TECHNOLOGYTREE_EXP);
		final LinkedHashMap<EventType, Double> exBoVExp = externBooster.get(RewardType.VANILLA_EXP);
		final LinkedHashMap<EventType, Double> exBoMoney = externBooster.get(RewardType.MONEY);
		final LinkedHashMap<EventType, Double> exBoCmd = externBooster.get(RewardType.COMMAND);
		LinkedHashMap<ToolType, LinkedHashMap<Material, LinkedHashMap<EventType, Double>>> matMap = fakeRewardPlayer(EventType.values(), tool, mat);
		for(Entry<ToolType, LinkedHashMap<Material, LinkedHashMap<EventType, Double>>> entry0 : matMap.entrySet())
		{
			if(!tools.contains(entry0.getKey()))
			{
				continue;
			}
			for(Entry<Material, LinkedHashMap<EventType, Double>> entry : matMap.get(tool).entrySet())
			{
				if(mat != entry.getKey())
				{
					continue;
				}
				for(Entry<EventType, Double> entryII : entry.getValue().entrySet())
				{
					EventType et = entryII.getKey();
					tx.add(entry0.getKey().toString()+" => "+plugin.getYamlHandler().getLang().getString("Events."+et.toString()));
					double amount = entryII.getValue();
					double exBTTExp = exBoTTExp != null ? (exBoTTExp.get(et) != null ? exBoTTExp.get(et).doubleValue() : 1.0) : 1.0;
					double exBVExp = exBoVExp != null ? (exBoVExp.get(et) != null ? exBoVExp.get(et).doubleValue() : 1.0) : 1.0;
					double exBMoney = exBoMoney != null ? (exBoMoney.get(et) != null ? exBoMoney.get(et).doubleValue() : 1.0) : 1.0;
					double exBCmd = exBoCmd != null ? (exBoCmd.get(et) != null ? exBoCmd.get(et).doubleValue() : 1.0) : 1.0;
					if(PlayerHandler.materialInteractionMap.containsKey(uuid)
							&& PlayerHandler.materialInteractionMap.get(uuid).containsKey(tool)
							&& PlayerHandler.materialInteractionMap.get(uuid).get(tool).containsKey(mat)
							&& PlayerHandler.materialInteractionMap.get(uuid).get(tool).get(mat).containsKey(et))
						
					{
						SimpleUnlockedInteraction sui = PlayerHandler.materialInteractionMap.get(uuid).get(tool).get(mat).get(et);
						if(sui == null)
						{
							continue;
						}
						double toGiveTTExp = sui.getTechnologyExperience() * amount * exBTTExp;
						tx.add(plugin.getYamlHandler().getLang().getString("Commands.Info.Payment.TTExp")
								.replace("%value%", String.valueOf(sui.getTechnologyExperience()))
								.replace("%externbooster%", String.valueOf(exBTTExp))
								.replace("%total%", String.valueOf(toGiveTTExp)));
						double toGiveVanillaExp = sui.getVanillaExperience() * amount * exBVExp;
						tx.add(plugin.getYamlHandler().getLang().getString("Commands.Info.Payment.VanillaExp")
								.replace("%value%", String.valueOf(sui.getVanillaExperience()))
								.replace("%externbooster%", String.valueOf(exBVExp))
								.replace("%total%", String.valueOf(toGiveVanillaExp)));
						for(Entry<String, Double> s : sui.getMoneyMap().entrySet())
						{
							double moneyAmount = s.getValue() * amount * exBMoney;
							String value = "";
							String total = "";
							if("vault".equals(s.getKey()))
							{
								value = plugin.getVaultEco() != null ? s.getValue()+" "+plugin.getVaultEco().currencyNamePlural() : s.getValue()+" Vault";
								total = plugin.getVaultEco() != null ? moneyAmount+" "+plugin.getVaultEco().currencyNamePlural() : moneyAmount+" Vault";
							} else
							{
								EconomyCurrency ec = null;
								if(s.getKey().equalsIgnoreCase("default"))
								{
									ec = plugin.getIFHEco().getDefaultCurrency(CurrencyType.DIGITAL);
								} else
								{
									ec = plugin.getIFHEco().getCurrency(s.getKey());
								}
								Account ac = plugin.getIFHEco().getDefaultAccount(uuid, AccountCategory.MAIN, ec);
								value = plugin.getIFHEco().format(s.getValue(), ec,
										plugin.getIFHEco().getDefaultGradationQuantity(ac.getCurrency()), plugin.getIFHEco().getDefaultDecimalPlaces(ac.getCurrency()),
										plugin.getIFHEco().getDefaultUseSIPrefix(ac.getCurrency()), plugin.getIFHEco().getDefaultUseSymbol(ac.getCurrency()),
										plugin.getIFHEco().getDefaultThousandSeperator(ac.getCurrency()), plugin.getIFHEco().getDefaultDecimalSeperator(ac.getCurrency()));
								total = plugin.getIFHEco().format(moneyAmount, ec,
										plugin.getIFHEco().getDefaultGradationQuantity(ac.getCurrency()), plugin.getIFHEco().getDefaultDecimalPlaces(ac.getCurrency()),
										plugin.getIFHEco().getDefaultUseSIPrefix(ac.getCurrency()), plugin.getIFHEco().getDefaultUseSymbol(ac.getCurrency()),
										plugin.getIFHEco().getDefaultThousandSeperator(ac.getCurrency()), plugin.getIFHEco().getDefaultDecimalSeperator(ac.getCurrency()));
							}
							tx.add(plugin.getYamlHandler().getLang().getString("Commands.Info.Payment.VanillaExp")
									.replace("%value%", value)
									.replace("%externbooster%", String.valueOf(exBMoney))
									.replace("%total%", total));
						}
						for(Entry<String, Double> s : sui.getCommandMap().entrySet())
						{
							double cmdAmount = s.getValue() * amount * exBCmd;
							tx.add(plugin.getYamlHandler().getLang().getString("Commands.Info.Payment.VanillaExp")
									.replace("%cmd%", s.getKey())
									.replace("%value%", String.valueOf(s.getValue()))
									.replace("%externbooster%", String.valueOf(exBCmd))
									.replace("%total%", String.valueOf(cmdAmount)));
						}						
					}
				}
			}
		}
		for(String s : tx)
		{
			player.sendMessage(ChatApi.tl(s));
		}
	}
}