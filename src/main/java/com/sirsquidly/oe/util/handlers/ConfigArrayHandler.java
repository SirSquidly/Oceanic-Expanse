package com.sirsquidly.oe.util.handlers;

import java.util.List;
import java.util.function.BooleanSupplier;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.GameData;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.sirsquidly.oe.Main;

/**
 * 	This is to break part arrays in the config for use in other areas of the code.
 *
 *  I break it up in this class so that I don't have to break the config arrays every time I want to use them.
 */
public class ConfigArrayHandler
{
	/** Entities that drown to become a different entity. */
	public static List<ResourceLocation> DROWNCONVERTFROM = Lists.<ResourceLocation>newArrayList();
	/** Entities created by the former (DROWNCONVERTFROM) drowning. */
	public static List<ResourceLocation> DROWNCONVERTTO = Lists.<ResourceLocation>newArrayList();

	/** Entities that drown to become a different entity. */
	public static List<IBlockState> CRABDIGFROM = Lists.<IBlockState>newArrayList();
	/** Loot Tables pulled by the former (CRABDIGFROM). */
	public static List<ResourceLocation> CRABDIGTO = Lists.<ResourceLocation>newArrayList();

	/** Entities that have their swimming AI removed. */
	public static List<ResourceLocation> NOSWIM = Lists.<ResourceLocation>newArrayList();
	/** Entities that do not loose air when underwater. */
	public static List<ResourceLocation> NODROWN = Lists.<ResourceLocation>newArrayList();


	public static List<ResourceLocation> bucketableMobs = Lists.<ResourceLocation>newArrayList();
	public static List<ResourceLocation> pufferfishFriends = Lists.<ResourceLocation>newArrayList();
	public static List<ResourceLocation> babyTurtlePredators = Lists.<ResourceLocation>newArrayList();
	public static List<ResourceLocation> ridingBlacklist = Lists.<ResourceLocation>newArrayList();
	public static List<ResourceLocation> aquaticMobs = Lists.<ResourceLocation>newArrayList();

	public static void breakupConfigArrays()
	{
		for(String S : ConfigHandler.vanillaTweak.drownConverting.drownConversionsList)
		{
			String[] split = S.split("=");

			if (DROWNCONVERTFROM.contains(new ResourceLocation(split[0])))
			{
				Main.logger.error(split[0] + " has multiple drowned conversions set in the config! Only the first listed will be used!");
			}
			else if (split.length != 2)
			{
				if (split.length == 1)
					Main.logger.error(split[0] + " is missing a drowned conversion! Skipping...");
				else
					Main.logger.error(S + " is improperly written!");
			}
			else
			{
				ResourceLocation drownResourceFrom = getEntityFromString(split[0]);
				ResourceLocation drownResourceTo = getEntityFromString(split[1]);

				if(drownResourceFrom != null && drownResourceTo != null)
				{
					DROWNCONVERTFROM.add(drownResourceFrom);
					DROWNCONVERTTO.add(drownResourceTo);
				}
				else
				{
					Main.logger.error("Drowning Conversion Table: " + S + " is returning null for an Entity, please make sure both entity IDs are proeprly written/exist! Skipping...");
				}
			}
		}

		for(String S : ConfigHandler.entity.crab.crabDiggingList)
		{
			String[] split = S.split("=");

			if (getBlockFromString(split[0]) == null)
			{
				Main.logger.error(split[0] + " is not a proper block!");
			}
			else if (CRABDIGFROM.contains(new ResourceLocation(split[0])))
			{
				Main.logger.error(split[0] + " has multiple crab digging loot tables set in the config! Only the first listed will be used!");
			}
			else if (split.length != 2)
			{
				if (split.length == 1)
					Main.logger.error(split[0] + " is missing a loot table! Skipping...");
				else
					Main.logger.error(S + " is improperly written!");
			}
			else
			{
				CRABDIGFROM.add(getBlockFromString(split[0]));
				CRABDIGTO.add(new ResourceLocation(split[1]));
			}
		}

		for(String S : ConfigHandler.vanillaTweak.waterMechanics.noDrownList)
		{ NODROWN.add(new ResourceLocation(S)); }

		for(String S : ConfigHandler.vanillaTweak.waterMechanics.noSwimList)
		{ NOSWIM.add(new ResourceLocation(S)); }

		for(String S : ConfigHandler.item.spawnBucket.bucketableMobs)
		{ bucketableMobs.add(new ResourceLocation(S)); }

		for(String S : ConfigHandler.entity.pufferfish.pufferfishFriends)
		{ pufferfishFriends.add(new ResourceLocation(S)); }

		for(String S : ConfigHandler.entity.turtle.babyTurtlePredators)
		{ babyTurtlePredators.add(new ResourceLocation(S)); }

		for(String S : ConfigHandler.enchant.channeling.ridingBlacklist)
		{ ridingBlacklist.add(new ResourceLocation(S)); }

		for(String S : ConfigHandler.enchant.impaling.aquaticMobs)
		{ aquaticMobs.add(new ResourceLocation(S)); }
	}

	/**
	 * Rips up a String to return an IBlockState.
	 *
	 * Returns null if the string cannot be processed!
	 */
	@SuppressWarnings("deprecation")
	public static IBlockState getBlockFromString(String string)
	{
		String[] ripString = string.split(":");

		Block block = GameRegistry.findRegistry(Block.class).getValue(new ResourceLocation(ripString[0], ripString[1]));
		Integer meta = null;

		if(block == null || block == Blocks.AIR)
		{
			Main.logger.error("Could not find " + string + "!");
			return null;
		}
		if(ripString.length > 2)
		{
			meta = Integer.parseInt(ripString[2]);

			if(meta == -1)
			{ meta = null; }
		}

		return meta == null ? block.getDefaultState() : block.getStateFromMeta(meta);
	}

	/**
	 * A copy of `getBlockFromString`, but for Entities.
	 */
	public static ResourceLocation getEntityFromString(String string)
	{
		if(GameData.getEntityRegistry().containsKey(new ResourceLocation(string)))
		{
			Main.logger.debug("Found " + string + " in Entity Registry, this is good.");
			return new ResourceLocation(string);
		}
		else
		{
			Main.logger.error("Could not find " + string + "! Are they registered?");
			return null;
		}
	}

	/**
	 * A copy of `getBlockFromString`, but for ItemStacks.
	 */
	public static ItemStack getItemStackFromString(String string)
	{
		String[] ripString = string.split(":");

		Item item = GameRegistry.findRegistry(Item.class).getValue(new ResourceLocation(ripString[0], ripString[1]));
		Integer meta = null;

		if(item == null || item == Items.AIR)
		{
			Main.logger.error("Could not find" + string + "!");
			return null;
		}
		if(ripString.length > 2)
		{
			meta = Integer.parseInt(ripString[2]);

			if(meta == -1)
			{ meta = null; }
		}

		return new ItemStack(item, 1, meta);
	}

	/**
	 * Sets up custom crafting recipe conditions.
	 *
	 * Used in `_factories.json` in /recipes
	 */
	public static class ConfigProperty implements IConditionFactory
	{
		@Override
		public BooleanSupplier parse(JsonContext context, JsonObject json)
		{
			String prop = JsonUtils.getString(json, "prop");
			return () -> propertyEnabled(prop);
		}

		private static boolean propertyEnabled(String property)
		{
			switch(property)
			{
				case "enableBlueIce": return ConfigHandler.block.blueIce.enableBlueIce;
				case "conduitAllIngredients": return ConfigHandler.block.conduit.enableConduit & ConfigHandler.item.enableNautilusShell;
				case "enableCoquina": return ConfigHandler.block.coquina.enableCoquina;
				case "enableCoquinaBricks": return ConfigHandler.block.coquina.enableCoquinaBricks;
				case "enableCoquinaBrickWalls": return ConfigHandler.block.coquina.enableCoquinaBrickWalls;
				case "enableCoconut": return ConfigHandler.block.coconut.enableCoconut;
				case "enableDusle": return ConfigHandler.block.dulse.enableDulse;
				case "enablePalmWoods": return ConfigHandler.block.palmBlocks.enablePalmWoods;
				case "enablePalmStrippedWoods": return ConfigHandler.block.palmBlocks.enablePalmStrippedWoods;
				case "enableKelp": return ConfigHandler.block.enableKelp;
				case "enableWaterTorch": return ConfigHandler.block.waterTorch.enableWaterTorch;

				case "enableGlowItemFrames": return ConfigHandler.item.glowItemFrame.enableGlowItemFrame;
				case "tridentAllIngredients": return ConfigHandler.item.trident.enableTridentCrafting & ConfigHandler.item.trident.enableTrident & ConfigHandler.block.guardianSpike.enableGuardianSpike;
				case "turtleShellAllIngredients": return ConfigHandler.item.turtleShell.enableTurtleShell & ConfigHandler.item.enableTurtleScute;
			}

			throw new JsonSyntaxException("Invalid propertyname '" + property + "'");
		}
	}
}