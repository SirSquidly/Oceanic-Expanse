package com.sirsquidly.oe.init;

import java.util.ArrayList;
import java.util.List;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.blocks.*;
import com.sirsquidly.oe.items.ItemBlockSeaPickle;
import com.sirsquidly.oe.items.ItemBlockSlab;
import com.sirsquidly.oe.util.Reference;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDoor;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber(modid = Reference.MOD_ID)
public class OEBlocks 
{
		/** Contains every block ran through the 'blockReadyForRegister' function. So they all SHOULD be registered. */
		private static List<Block> blockList = new ArrayList<Block>();
		/** Records any blocks that have a unique itemBlock assigned, as the rest are automatically slapped with the default itemBlock.*/
		private static List<Block> itemBlockBlacklist = new ArrayList<Block>();
		/** Used for items settup here, and need to have models registered as such. */
		private static List<Item> blockDirectItemList = new ArrayList<Item>();

		public static Block GUARDIAN_SPIKE = new BlockGuardianSpike();
	
		public static Block SEA_PICKLE = blockReadyForRegister(new BlockSeaPickle(), "sea_pickle");
		
		public static Block SEAGRASS = blockReadyForRegister(new BlockUnderwaterGrass(), "seagrass");
		public static Block TALL_SEAGRASS = blockReadyForRegister(new BlockDoubleUnderwater(), "tall_seagrass");
		public static Block KELP = blockReadyForRegister(new BlockKelp(), "kelp_mid", false);
		public static Block KELP_TOP = blockReadyForRegister(new BlockTopKelp(), "kelp");
		public static Block DRIED_KELP_BLOCK = blockReadyForRegister(new BlockDriedKelp(), "dried_kelp_block");
		public static Block COCONUT = blockReadyForRegister(new BlockCoconut(), "coconut");
		public static Block PALM_LOG = blockReadyForRegister(new BlockPalmLog(), "palm_log");
		public static Block PALM_WOOD = blockReadyForRegister(new BlockPalmLog(), "palm_wood");
		public static Block PALM_PLANKS = blockReadyForRegister(new BlockPalmPlanks(), "palm_planks");
		public static Block PALM_SLAB = blockReadyForRegister(new BlockPalmSlab(), "palm_slab");
		public static Block PALM_SLAB_D = blockReadyForRegister(new BlockPalmSlabDouble(), "palm_slab_double", false);
		public static Block PALM_STAIRS = blockReadyForRegister(new BlockPalmStairs(), "palm_stairs");
		public static Block PALM_FENCE = blockReadyForRegister(new BlockFence(Material.WOOD, MapColor.BROWN_STAINED_HARDENED_CLAY), "palm_fence");
		public static Block PALM_FENCE_GATE = blockReadyForRegister(new BlockFenceGate(BlockPlanks.EnumType.SPRUCE), "palm_fence_gate");
		public static Block PALM_DOOR = blockReadyForRegister(new BlockPalmDoor(), "palm_door");
		public static Block COCONUT_LEAVES = blockReadyForRegister(new BlockCoconutLeaves(), "coconut_leaves");
		public static Block COCONUT_LEAVES_FLOWERING = blockReadyForRegister(new BlockCoconutLeavesFlowering(), "coconut_leaves_flowering");
		public static Block COCONUT_SAPLING = blockReadyForRegister(new BlockPalmSapling(), "palm_sapling");
		
		public static Block BLUE_ICE = blockReadyForRegister(new BlockBlueIce(), "blue_ice");
		public static Block SEA_TURTLE_EGG = blockReadyForRegister(new BlockTurtleEgg(), "turtle_egg");
		public static Block SEA_OATS = blockReadyForRegister(new BlockDoubleSeaOats(), "sea_oats");
		
		public static Block SHELL_SAND = blockReadyForRegister(new BlockShellSand(), "shell_sand");
		public static Block COQUINA = blockReadyForRegister(new BlockShellSand(), "coquina");
		public static Block WRACK = blockReadyForRegister(new BlockWrack(), "wrack");
		
		public static Block BLUE_CORAL_BLOCK = blockReadyForRegister(new BlockCoralFull(MapColor.BLUE), "blue_coral_block");
		public static Block BLUE_CORAL_BLOCK_DEAD = blockReadyForRegister(new BlockCoralFull(MapColor.GRAY), "blue_coral_block_dead");
		public static Block PINK_CORAL_BLOCK = blockReadyForRegister(new BlockCoralFull(MapColor.PINK), "pink_coral_block");
		public static Block PINK_CORAL_BLOCK_DEAD = blockReadyForRegister(new BlockCoralFull(MapColor.GRAY), "pink_coral_block_dead");
		public static Block PURPLE_CORAL_BLOCK = blockReadyForRegister(new BlockCoralFull(MapColor.PURPLE), "purple_coral_block");
		public static Block PURPLE_CORAL_BLOCK_DEAD = blockReadyForRegister(new BlockCoralFull(MapColor.GRAY), "purple_coral_block_dead");
		public static Block RED_CORAL_BLOCK = blockReadyForRegister(new BlockCoralFull(MapColor.RED), "red_coral_block");
		public static Block RED_CORAL_BLOCK_DEAD = blockReadyForRegister(new BlockCoralFull(MapColor.GRAY), "red_coral_block_dead");
		public static Block YELLOW_CORAL_BLOCK = blockReadyForRegister(new BlockCoralFull(MapColor.YELLOW), "yellow_coral_block");
		public static Block YELLOW_CORAL_BLOCK_DEAD = blockReadyForRegister(new BlockCoralFull(MapColor.GRAY), "yellow_coral_block_dead");

		public static Block BLUE_CORAL_FAN = blockReadyForRegister(new BlockCoralFan(MapColor.BLUE), "blue_coral_fan");
		public static Block BLUE_CORAL_FAN_DEAD = blockReadyForRegister(new BlockCoralFan(MapColor.GRAY), "blue_coral_fan_dead");
		public static Block PINK_CORAL_FAN = blockReadyForRegister(new BlockCoralFan(MapColor.PINK), "pink_coral_fan");
		public static Block PINK_CORAL_FAN_DEAD = blockReadyForRegister(new BlockCoralFan(MapColor.GRAY), "pink_coral_fan_dead");
		public static Block PURPLE_CORAL_FAN = blockReadyForRegister(new BlockCoralFan(MapColor.PURPLE), "purple_coral_fan");
		public static Block PURPLE_CORAL_FAN_DEAD = blockReadyForRegister(new BlockCoralFan(MapColor.GRAY), "purple_coral_fan_dead");
		public static Block RED_CORAL_FAN = blockReadyForRegister(new BlockCoralFan(MapColor.RED), "red_coral_fan");
		public static Block RED_CORAL_FAN_DEAD = blockReadyForRegister(new BlockCoralFan(MapColor.GRAY), "red_coral_fan_dead");
		public static Block YELLOW_CORAL_FAN = blockReadyForRegister(new BlockCoralFan(MapColor.YELLOW), "yellow_coral_fan");
		public static Block YELLOW_CORAL_FAN_DEAD = blockReadyForRegister(new BlockCoralFan(MapColor.GRAY), "yellow_coral_fan_dead");
		
		public static Block BLUE_CORAL = blockReadyForRegister(new BlockCoral(MapColor.BLUE), "blue_coral");
		public static Block BLUE_CORAL_DEAD = blockReadyForRegister(new BlockCoral(MapColor.GRAY), "blue_coral_dead");
		public static Block PINK_CORAL = blockReadyForRegister(new BlockCoral(MapColor.BLUE), "pink_coral");
		public static Block PINK_CORAL_DEAD = blockReadyForRegister(new BlockCoral(MapColor.GRAY), "pink_coral_dead");
		public static Block PURPLE_CORAL = blockReadyForRegister(new BlockCoral(MapColor.BLUE), "purple_coral");
		public static Block PURPLE_CORAL_DEAD = blockReadyForRegister(new BlockCoral(MapColor.GRAY), "purple_coral_dead");
		public static Block RED_CORAL = blockReadyForRegister(new BlockCoral(MapColor.BLUE), "red_coral");
		public static Block RED_CORAL_DEAD = blockReadyForRegister(new BlockCoral(MapColor.GRAY), "red_coral_dead");
		public static Block YELLOW_CORAL = blockReadyForRegister(new BlockCoral(MapColor.BLUE), "yellow_coral");
		public static Block YELLOW_CORAL_DEAD = blockReadyForRegister(new BlockCoral(MapColor.GRAY), "yellow_coral_dead");
		
		@SubscribeEvent
		public static void registerBlocks(RegistryEvent.Register<Block> event)
		{
			if (ConfigHandler.block.guardianSpike.enableGuardianSpike) blockReadyForRegister(GUARDIAN_SPIKE, "guardian_spike");
			
			for (Block blocks : blockList) event.getRegistry().register(blocks);
		}
		
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) 
	{
		IForgeRegistry<Item> r = event.getRegistry();
		
		if (ConfigHandler.block.guardianSpike.enableGuardianSpike) registerItemBlock(r, GUARDIAN_SPIKE); itemBlockBlacklist.add(GUARDIAN_SPIKE);;
		
		registerItemBlock(r, new ItemBlockSeaPickle(SEA_PICKLE)); itemBlockBlacklist.add(SEA_PICKLE);
		registerItemBlock(r, new ItemBlockSeaPickle(SEA_TURTLE_EGG)); itemBlockBlacklist.add(SEA_TURTLE_EGG);
		registerItemBlock(r, new ItemBlockSlab(PALM_SLAB, (BlockSlab)PALM_SLAB, (BlockSlab)PALM_SLAB_D)); itemBlockBlacklist.add(PALM_SLAB);
		
		registerDoorItem(r, new ItemDoor(PALM_DOOR), PALM_DOOR); itemBlockBlacklist.add(PALM_DOOR);
		
		
		/** As stated on itemBlockBlacklist, this registers anything NOT from the blacklist with a generic itemBlock.*/
		for (Block blocks : blockList) if (!(itemBlockBlacklist.contains(blocks))) { registerItemBlock(r, blocks);}
	}

	/** If blocks don't specify the 'addToTab' boolean, assume true.*/
	public static Block blockReadyForRegister(Block block, String name)
	{ return blockReadyForRegister(block, name, true);}
	
	/** Slaps the names to Blocks, and adds them to the blockList to be registered in 'registerBlocks'.*/
	public static Block blockReadyForRegister(Block block, String name, Boolean addToTab)
	{
		block.setUnlocalizedName(name);
		block.setRegistryName(name);
		
		if (addToTab) block.setCreativeTab(Main.OCEANEXPTAB);
		else block.setCreativeTab(null);
			
		blockList.add(block);

		return block;
	}
	
	public static ItemDoor registerDoorItem(IForgeRegistry<Item> r, ItemDoor itemDoor, Block block)
	{ 
		itemDoor.setUnlocalizedName(block.getUnlocalizedName());
		itemDoor.setRegistryName(block.getRegistryName());
		itemDoor.setCreativeTab(Main.OCEANEXPTAB);
		
		r.register(itemDoor);
		if (block == PALM_DOOR) ((BlockPalmDoor) block).setItem(itemDoor);
		
		blockDirectItemList.add(itemDoor);
		
		return itemDoor;
	}
	
	public static ItemBlock registerItemBlock(IForgeRegistry<Item> registry, Block block) 
	{ return registerItemBlock(registry, new ItemBlock(block)); }

	public static <T extends ItemBlock> T registerItemBlock(IForgeRegistry<Item> registry, T item) {
		Block block = item.getBlock();
		item.setUnlocalizedName(block.getUnlocalizedName());
		item.setRegistryName(block.getRegistryName());

		registry.register(item);

		return item;
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event)
	{
		for(Item i : blockDirectItemList)
		{
			ModelLoader.setCustomModelResourceLocation(i, 0, new ModelResourceLocation(i.getRegistryName(), "inventory"));
		}
		
		for(Block b : blockList)
		{
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(b), 0, new ModelResourceLocation(b.getRegistryName(), "inventory"));
			
			/** So many blocks are submergable, might as well set this for all **/
			ModelLoader.setCustomStateMapper(b, new StateMap.Builder().ignore(BlockLiquid.LEVEL).ignore(BlockCoralFan.IN_WATER).build());
			ModelLoader.setCustomStateMapper(OEBlocks.PALM_SLAB_D, new StateMap.Builder().ignore(BlockSlab.HALF).build());
			ModelLoader.setCustomStateMapper(OEBlocks.PALM_FENCE_GATE, new StateMap.Builder().ignore(BlockFenceGate.POWERED).build());
			ModelLoader.setCustomStateMapper(OEBlocks.PALM_DOOR, new StateMap.Builder().ignore(BlockDoor.POWERED).build());
			
			/** Sea Pickles actually use the IN_WATER check to change model, or RE-include it**/
			ModelLoader.setCustomStateMapper(OEBlocks.SEA_PICKLE, new StateMap.Builder().ignore(BlockLiquid.LEVEL).build());
		}
	}
	
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void registerBlockColourHandlers(final ColorHandlerEvent.Block event) 
	{
		final BlockColors blockColors = event.getBlockColors();
		final IBlockColor grassColourHandler = (state, blockAccess, pos, tintIndex) -> 
		{
			if (blockAccess != null && pos != null) {
				return BiomeColorHelper.getGrassColorAtPos(blockAccess, pos);
			}

			return ColorizerGrass.getGrassColor(1.0D, 1.0D);
		};
		blockColors.registerBlockColorHandler(grassColourHandler, OEBlocks.COCONUT_LEAVES);
		blockColors.registerBlockColorHandler(grassColourHandler, OEBlocks.COCONUT_LEAVES_FLOWERING);
	} 
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void registerItemColourHandlers(final ColorHandlerEvent.Item event) 
	{
		final BlockColors blockColors = event.getBlockColors();
		final ItemColors itemColors = event.getItemColors();

		final IItemColor itemBlockColourHandler = (stack, tintIndex) -> 
		{
			@SuppressWarnings("deprecation")
			final IBlockState state = ((ItemBlock) stack.getItem()).getBlock().getStateFromMeta(stack.getMetadata());
			return blockColors.colorMultiplier(state, null, null, tintIndex);
		};
		itemColors.registerItemColorHandler(itemBlockColourHandler, OEBlocks.COCONUT_LEAVES);
		itemColors.registerItemColorHandler(itemBlockColourHandler, OEBlocks.COCONUT_LEAVES_FLOWERING);
	}
}