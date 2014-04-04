package alexndr.SimpleOres.api.helpers;

import java.util.ArrayList;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;

import com.google.common.collect.Lists;

import cpw.mods.fml.common.ICraftingHandler;
import cpw.mods.fml.common.IPickupNotifier;

public class StatTriggersHelper implements ICraftingHandler, IPickupNotifier
{
	private static final StatTriggersHelper statTriggers = new StatTriggersHelper();
	
	private static ArrayList<ItemStack> craftingItems = Lists.newArrayList();
	private static ArrayList<Achievement> craftingAchievs = Lists.newArrayList();
	private static ArrayList<ItemStack> smeltingItems = Lists.newArrayList();
	private static ArrayList<Achievement> smeltingAchievs = Lists.newArrayList();
	private static ArrayList<ItemStack> pickupItems = Lists.newArrayList();
	private static ArrayList<Achievement> pickupAchievs = Lists.newArrayList();
	
	public static final StatTriggersHelper statTriggers()
	{
		return statTriggers;
	}
	
	/**
	 * Add a Crafting Grid trigger for an Achievement.
	 * 
	 * @param itemTakenFromCrafting The ItemStack that you want to trigger the Achievement when it is taken from the Crafting Grid output slot.
	 * @param achievementToTrigger The Achievement that you want to trigger.
	 */
	public void addCraftingTrigger(ItemStack itemstackTakenFromCrafting, Achievement achievementToTrigger)
	{
		this.craftingItems.add(itemstackTakenFromCrafting);
		this.craftingAchievs.add(achievementToTrigger);
		LogHelper.verboseInfo("StatTriggersHelper: Total number of crafting triggers registered = " + craftingAchievs.size());
	}
	
	/**
	 * Add a Furnace trigger for an Achievement.
	 * 
	 * @param itemTakenFromSmelting The ItemStack that you want to trigger the Achievement when it is taken from the Furnace output slot.
	 * @param achievementToTrigger The Achievement that you want to trigger.
	 */
	public void addSmeltingTrigger(ItemStack itemstackTakenFromSmelting, Achievement achievementToTrigger)
	{
		this.smeltingItems.add(itemstackTakenFromSmelting);
		this.smeltingAchievs.add(achievementToTrigger);
		LogHelper.verboseInfo("StatTriggersHelper: Total number of smelting triggers registered = " + smeltingAchievs.size());
	}
	
	/**
	 * Add an item pickup trigger for an Achievement.
	 * 
	 * @param itemstackPickedUp The ItemStack that you want to trigger the Achievement when it is picked up.
	 * @param achievementToTrigger The Achievement that you want to trigger.
	 */
	public void addPickupTrigger(ItemStack itemstackPickedUp, Achievement achievementToTrigger)
	{
		this.pickupItems.add(itemstackPickedUp);
		this.pickupAchievs.add(achievementToTrigger);
		LogHelper.verboseInfo("StatTriggersHelper: Total number of pickup triggers registered = " + pickupAchievs.size());
	}

	@Override
	public void onCrafting(EntityPlayer player, ItemStack item, IInventory craftMatrix) 
	{
		if(craftingItems.size() == craftingAchievs.size() && craftingItems.size() != 0)
		{
			for(int i = 0; i < craftingItems.size(); i ++)
			{
				if(item.itemID == craftingItems.get(i).itemID)
				{
					player.addStat(craftingAchievs.get(i), 1);
				}
			}
		}
	}

	@Override
	public void onSmelting(EntityPlayer player, ItemStack item) 
	{
		if(smeltingItems.size() == smeltingAchievs.size() && smeltingItems.size() != 0)
		{
			for(int i = 0; i < smeltingItems.size(); i ++)
			{
				if(item.itemID == smeltingItems.get(i).itemID)
				{
					player.addStat(smeltingAchievs.get(i), 1);
				}
			}
		}
	}

	@Override
	public void notifyPickup(EntityItem item, EntityPlayer player)
	{
		if(pickupItems.size() == pickupAchievs.size() && pickupItems.size() != 0)
		{
			for(int i = 0; i < pickupItems.size(); i++)
			{
				if(item.getEntityItem().itemID == pickupItems.get(i).itemID)
				{
					player.addStat(pickupAchievs.get(i), 1);
				}
			}
		}
	}
}
