package com.sirsquidly.oe.init;

import java.util.ArrayList;
import java.util.List;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.blocks.*;
import com.sirsquidly.oe.items.ItemBlockSeaPickle;
import com.sirsquidly.oe.items.ItemBlockSlab;
import com.sirsquidly.oe.util.Reference;
import com.sirsquidly.oe.util.handlers.ConfigHandler;
import com.sirsquidly.oe.util.handlers.SoundHandler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
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
		public static Block PALM_LOG = new BlockPalmLog();
		public static Block PALM_WOOD = new BlockPalmLog();
		public static Block PALM_LOG_STRIPPED = new BlockPalmLog();
		public static Block PALM_WOOD_STRIPPED = new BlockPalmLog();
		public static Block PALM_PLANKS = new BlockPalmPlanks();
		public static Block PALM_SLAB = new BlockOESlab(Material.WOOD, SoundType.WOOD, 2.0F, 5.0F, 20, 5);
		public static Block PALM_SLAB_D = new BlockOESlabDouble(PALM_SLAB, Material.WOOD, SoundType.WOOD, 2.0F, 5.0F);
		public static Block PALM_STAIRS = new BlockOEStairs(OEBlocks.PALM_PLANKS.getDefaultState(), 20, 5);
		public static Block PALM_FENCE = new BlockFence(Material.WOOD, MapColor.BROWN_STAINED_HARDENED_CLAY);
		public static Block PALM_FENCE_GATE = new BlockFenceGate(BlockPlanks.EnumType.SPRUCE);
		public static Block PALM_DOOR = new BlockPalmDoor();
		public static Block COCONUT_LEAVES = blockReadyForRegister(new BlockCoconutLeaves(), "coconut_leaves");
		public static Block COCONUT_LEAVES_FLOWERING = blockReadyForRegister(new BlockCoconutLeavesFlowering(), "coconut_leaves_flowering");
		public static Block COCONUT_SAPLING = blockReadyForRegister(new BlockPalmSapling(), "palm_sapling");
		
		public static Block BLUE_ICE = new BlockBlueIce();
		public static Block SEA_TURTLE_EGG = blockReadyForRegister(new BlockTurtleEgg(), "turtle_egg");
		public static Block CONDUIT = blockReadyForRegister(new BlockConduit(), "conduit");
		public static Block SEA_OATS = blockReadyForRegister(new BlockDoubleSeaOats(), "sea_oats");
		public static Block TUBE_SPONGE = blockReadyForRegister(new BlockTubeSponge(), "tube_sponge");
		public static Block SEASTAR = blockReadyForRegister(new BlockSeaStar(), "seastar");
		public static Block DULSE = blockReadyForRegister(new BlockDulse(), "dulse");
		public static Block DRIED_DULSE_BLOCK = blockReadyForRegister(new BlockDriedKelp(), "dried_dulse_block");
		public static Block UNDERWATER_TORCH = new BlockUnderwaterTorch();
		
		public static Block SHELL_SAND = blockReadyForRegister(new BlockShellSand(), "shell_sand");
		public static Block COQUINA = blockReadyForRegister(new BlockShellSand(), "coquina");
		public static Block WRACK = blockReadyForRegister(new BlockWrack(), "wrack");
		
		public static Block BLUE_CORAL_BLOCK = new BlockCoralFull(MapColor.BLUE, SoundHandler.CORAL);
		public static Block PINK_CORAL_BLOCK = new BlockCoralFull(MapColor.PINK, SoundHandler.CORAL);
		public static Block PURPLE_CORAL_BLOCK = new BlockCoralFull(MapColor.PURPLE, SoundHandler.CORAL);
		public static Block RED_CORAL_BLOCK = new BlockCoralFull(MapColor.RED, SoundHandler.CORAL);
		public static Block YELLOW_CORAL_BLOCK = new BlockCoralFull(MapColor.YELLOW, SoundHandler.CORAL);
		public static Block BLUE_CORAL_BLOCK_DEAD = new BlockCoralFull();
		public static Block PINK_CORAL_BLOCK_DEAD = new BlockCoralFull();
		public static Block PURPLE_CORAL_BLOCK_DEAD = new BlockCoralFull();
		public static Block RED_CORAL_BLOCK_DEAD = new BlockCoralFull();
		public static Block YELLOW_CORAL_BLOCK_DEAD = new BlockCoralFull();

		public static Block BLUE_CORAL_FAN = new BlockCoralFan(MapColor.BLUE, SoundHandler.CORAL);
		public static Block PINK_CORAL_FAN = new BlockCoralFan(MapColor.PINK, SoundHandler.CORAL);
		public static Block PURPLE_CORAL_FAN = new BlockCoralFan(MapColor.PURPLE, SoundHandler.CORAL);
		public static Block RED_CORAL_FAN = new BlockCoralFan(MapColor.RED, SoundHandler.CORAL);
		public static Block YELLOW_CORAL_FAN = new BlockCoralFan(MapColor.YELLOW, SoundHandler.CORAL);
		public static Block BLUE_CORAL_FAN_DEAD = new BlockCoralFan();
		public static Block PINK_CORAL_FAN_DEAD = new BlockCoralFan();
		public static Block PURPLE_CORAL_FAN_DEAD = new BlockCoralFan();
		public static Block RED_CORAL_FAN_DEAD = new BlockCoralFan();
		public static Block YELLOW_CORAL_FAN_DEAD = new BlockCoralFan();
		
		public static Block BLUE_CORAL = new BlockCoral(MapColor.BLUE, SoundHandler.CORAL);
		public static Block PINK_CORAL = new BlockCoral(MapColor.PINK, SoundHandler.CORAL);
		public static Block PURPLE_CORAL = new BlockCoral(MapColor.PURPLE, SoundHandler.CORAL);
		public static Block RED_CORAL = new BlockCoral(MapColor.RED, SoundHandler.CORAL);
		public static Block YELLOW_CORAL = new BlockCoral(MapColor.YELLOW, SoundHandler.CORAL);
		public static Block BLUE_CORAL_DEAD = new BlockCoral();
		public static Block PINK_CORAL_DEAD = new BlockCoral();
		public static Block PURPLE_CORAL_DEAD = new BlockCoral();
		public static Block RED_CORAL_DEAD = new BlockCoral();
		public static Block YELLOW_CORAL_DEAD = new BlockCoral();
		
		@SubscribeEvent
		public static void registerBlocks(RegistryEvent.Register<Block> event)
		{
			if (ConfigHandler.block.blueIce.enableBlueIce) blockReadyForRegister(BLUE_ICE, "blue_ice");
			
			if (ConfigHandler.block.guardianSpike.enableGuardianSpike) blockReadyForRegister(GUARDIAN_SPIKE, "guardian_spike");
			
			if (ConfigHandler.block.coralBlocks.enableCoralBlock)
			{
				blockReadyForRegister(BLUE_CORAL_BLOCK, "blue_coral_block");
				blockReadyForRegister(PINK_CORAL_BLOCK, "pink_coral_block");
				blockReadyForRegister(PURPLE_CORAL_BLOCK, "purple_coral_block");
				blockReadyForRegister(RED_CORAL_BLOCK, "red_coral_block");
				blockReadyForRegister(YELLOW_CORAL_BLOCK, "yellow_coral_block");
				
				blockReadyForRegister(BLUE_CORAL_BLOCK_DEAD, "blue_coral_block_dead");
				blockReadyForRegister(PINK_CORAL_BLOCK_DEAD, "pink_coral_block_dead");
				blockReadyForRegister(PURPLE_CORAL_BLOCK_DEAD, "purple_coral_block_dead");
				blockReadyForRegister(RED_CORAL_BLOCK_DEAD, "red_coral_block_dead");
				blockReadyForRegister(YELLOW_CORAL_BLOCK_DEAD, "yellow_coral_block_dead");
			}
			if (ConfigHandler.block.coralBlocks.enableCoralFan)
			{
				blockReadyForRegister(BLUE_CORAL_FAN, "blue_coral_fan");
				blockReadyForRegister(PINK_CORAL_FAN, "pink_coral_fan");
				blockReadyForRegister(PURPLE_CORAL_FAN, "purple_coral_fan");
				blockReadyForRegister(RED_CORAL_FAN, "red_coral_fan");
				blockReadyForRegister(YELLOW_CORAL_FAN, "yellow_coral_fan");
				
				blockReadyForRegister(BLUE_CORAL_FAN_DEAD, "blue_coral_fan_dead");
				blockReadyForRegister(PINK_CORAL_FAN_DEAD, "pink_coral_fan_dead");
				blockReadyForRegister(PURPLE_CORAL_FAN_DEAD, "purple_coral_fan_dead");
				blockReadyForRegister(RED_CORAL_FAN_DEAD, "red_coral_fan_dead");
				blockReadyForRegister(YELLOW_CORAL_FAN_DEAD, "yellow_coral_fan_dead");
			}
			if (ConfigHandler.block.coralBlocks.enableCoral)
			{
				blockReadyForRegister(BLUE_CORAL, "blue_coral");
				blockReadyForRegister(PINK_CORAL, "pink_coral");
				blockReadyForRegister(PURPLE_CORAL, "purple_coral");
				blockReadyForRegister(RED_CORAL, "red_coral");
				blockReadyForRegister(YELLOW_CORAL, "yellow_coral");
				
				blockReadyForRegister(BLUE_CORAL_DEAD, "blue_coral_dead");
				blockReadyForRegister(PINK_CORAL_DEAD, "pink_coral_dead");
				blockReadyForRegister(PURPLE_CORAL_DEAD, "purple_coral_dead");
				blockReadyForRegister(RED_CORAL_DEAD, "red_coral_dead");
				blockReadyForRegister(YELLOW_CORAL_DEAD, "yellow_coral_dead");
			}
			
			
			if (ConfigHandler.block.palmBlocks.enablePalmWoods)
			{
				blockReadyForRegister(PALM_LOG, "palm_log");
				blockReadyForRegister(PALM_WOOD, "palm_wood");
				if (ConfigHandler.block.palmBlocks.enablePalmStrippedWoods) blockReadyForRegister(PALM_LOG_STRIPPED, "palm_log_stripped");
				if (ConfigHandler.block.palmBlocks.enablePalmStrippedWoods) blockReadyForRegister(PALM_WOOD_STRIPPED, "palm_wood_stripped");
				blockReadyForRegister(PALM_PLANKS, "palm_planks");
				blockReadyForRegister(PALM_SLAB, "palm_slab");
				blockReadyForRegister(PALM_SLAB_D, "palm_slab_double", false);
				blockReadyForRegister(PALM_STAIRS, "palm_stairs");
				blockReadyForRegister(PALM_FENCE, "palm_fence"); PALM_FENCE.setHardness(2.0F).setResistance(5.0F);
				blockReadyForRegister(PALM_FENCE_GATE, "palm_fence_gate"); PALM_FENCE_GATE.setHardness(2.0F).setResistance(5.0F);
				blockReadyForRegister(PALM_DOOR, "palm_door");
			} 
			if (ConfigHandler.block.waterTorch.enableWaterTorch) blockReadyForRegister(UNDERWATER_TORCH, "underwater_torch");
			
			for (Block blocks : blockList) event.getRegistry().register(blocks);
		}
		
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) 
	{
		IForgeRegistry<Item> r = event.getRegistry();
		
		if (ConfigHandler.block.guardianSpike.enableGuardianSpike) registerItemBlock(r, GUARDIAN_SPIKE); itemBlockBlacklist.add(GUARDIAN_SPIKE);;
		
		registerItemBlock(r, new ItemBlockSeaPickle(SEA_PICKLE)); itemBlockBlacklist.add(SEA_PICKLE);
		registerItemBlock(r, new ItemBlockSeaPickle(SEA_TURTLE_EGG)); itemBlockBlacklist.add(SEA_TURTLE_EGG);
		
		if (ConfigHandler.block.palmBlocks.enablePalmWoods) registerItemBlock(r, new ItemBlockSlab(PALM_SLAB, (BlockSlab)PALM_SLAB, (BlockSlab)PALM_SLAB_D)); itemBlockBlacklist.add(PALM_SLAB);
		if (ConfigHandler.block.palmBlocks.enablePalmWoods) registerDoorItem(r, new ItemDoor(PALM_DOOR), PALM_DOOR); itemBlockBlacklist.add(PALM_DOOR);
		
		
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
			ModelLoader.setCustomStateMapper(b, new StateMap.Builder().ignore(BlockLiquid.LEVEL).ignore(BlockCoralFan.IN_WATER).ignore(BlockTubeSponge.SHEARED).build());
			
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