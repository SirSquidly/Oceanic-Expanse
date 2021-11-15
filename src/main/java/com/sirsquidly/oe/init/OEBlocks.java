package com.sirsquidly.oe.init;

import java.util.ArrayList;
import java.util.List;

import com.sirsquidly.oe.blocks.*;
import com.sirsquidly.oe.items.ItemBlockSeaPickle;
import com.sirsquidly.oe.util.Reference;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
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
	public static Block SEA_PICKLE, SEAGRASS, TALL_SEAGRASS, KELP, KELP_TOP,
	DRIED_KELP_BLOCK, COCONUT, COCONUT_LEAVES, COCONUT_LEAVES_FLOWERING, COCONUT_SAPLING, BLUE_CORAL_BLOCK, 
	BLUE_CORAL_BLOCK_DEAD, PINK_CORAL_BLOCK, PINK_CORAL_BLOCK_DEAD, PURPLE_CORAL_BLOCK, PURPLE_CORAL_BLOCK_DEAD,
	RED_CORAL_BLOCK, RED_CORAL_BLOCK_DEAD, YELLOW_CORAL_BLOCK, YELLOW_CORAL_BLOCK_DEAD, BLUE_CORAL_FAN, 
	BLUE_CORAL_FAN_DEAD, PINK_CORAL_FAN, PINK_CORAL_FAN_DEAD, PURPLE_CORAL_FAN, PURPLE_CORAL_FAN_DEAD, RED_CORAL_FAN,
	RED_CORAL_FAN_DEAD, YELLOW_CORAL_FAN, YELLOW_CORAL_FAN_DEAD, BLUE_CORAL, BLUE_CORAL_DEAD, PINK_CORAL,
	PINK_CORAL_DEAD, PURPLE_CORAL, PURPLE_CORAL_DEAD, RED_CORAL, RED_CORAL_DEAD, YELLOW_CORAL, YELLOW_CORAL_DEAD,
	SEA_TURTLE_EGG, BLUE_ICE, SHELL_SAND, WRACK, SEA_OATS;
	private static List<Block> blockList = new ArrayList<Block>();
	static List<Item> itemBlockList = new ArrayList<>();

	public static final Block GUARDIAN_SPIKE = registerBlock(new BlockGuardianSpike(), "guardian_spike");
	
	static {
		SEA_PICKLE = registerBlock(new BlockSeaPickle(), "sea_pickle", new ItemBlockSeaPickle(SEA_PICKLE));
		
		SEAGRASS = registerBlock(new BlockUnderwaterGrass(), "seagrass");
		TALL_SEAGRASS = registerBlock(new BlockDoubleUnderwater(), "tall_seagrass");
		KELP = registerBlock(new BlockKelp(), "kelp_mid");
		KELP_TOP = registerBlock(new BlockTopKelp(), "kelp");
		DRIED_KELP_BLOCK = registerBlock(new BlockDriedKelp(), "dried_kelp_block");
		COCONUT = registerBlock(new BlockCoconut(), "coconut");
		COCONUT_LEAVES = registerBlock(new BlockCoconutLeaves(), "coconut_leaves");
		COCONUT_LEAVES_FLOWERING = registerBlock(new BlockCoconutLeavesFlowering(), "coconut_leaves_flowering");
		COCONUT_SAPLING = registerBlock(new BlockCoconutSapling(), "coconut_sapling");
		
		BLUE_ICE = registerBlock(new BlockBlueIce(), "blue_ice");
		SEA_TURTLE_EGG = registerBlock(new BlockTurtleEgg(), "turtle_egg");
		SEA_OATS = registerBlock(new BlockDoubleSeaOats(), "sea_oats");
		
		SHELL_SAND = registerBlock(new BlockShellSand(), "shell_sand");
		WRACK = registerBlock(new BlockWrack(), "wrack");
		
		BLUE_CORAL_BLOCK = registerBlock(new BlockCoralFull(MapColor.BLUE), "blue_coral_block");
		BLUE_CORAL_BLOCK_DEAD = registerBlock(new BlockCoralFull(MapColor.GRAY), "blue_coral_block_dead");
		PINK_CORAL_BLOCK = registerBlock(new BlockCoralFull(MapColor.PINK), "pink_coral_block");
		PINK_CORAL_BLOCK_DEAD = registerBlock(new BlockCoralFull(MapColor.GRAY), "pink_coral_block_dead");
		PURPLE_CORAL_BLOCK = registerBlock(new BlockCoralFull(MapColor.PURPLE), "purple_coral_block");
		PURPLE_CORAL_BLOCK_DEAD = registerBlock(new BlockCoralFull(MapColor.GRAY), "purple_coral_block_dead");
		RED_CORAL_BLOCK = registerBlock(new BlockCoralFull(MapColor.RED), "red_coral_block");
		RED_CORAL_BLOCK_DEAD = registerBlock(new BlockCoralFull(MapColor.GRAY), "red_coral_block_dead");
		YELLOW_CORAL_BLOCK = registerBlock(new BlockCoralFull(MapColor.YELLOW), "yellow_coral_block");
		YELLOW_CORAL_BLOCK_DEAD = registerBlock(new BlockCoralFull(MapColor.GRAY), "yellow_coral_block_dead");

		BLUE_CORAL_FAN = registerBlock(new BlockCoralFan(MapColor.BLUE), "blue_coral_fan");
		BLUE_CORAL_FAN_DEAD = registerBlock(new BlockCoralFan(MapColor.GRAY), "blue_coral_fan_dead");
		PINK_CORAL_FAN = registerBlock(new BlockCoralFan(MapColor.PINK), "pink_coral_fan");
		PINK_CORAL_FAN_DEAD = registerBlock(new BlockCoralFan(MapColor.GRAY), "pink_coral_fan_dead");
		PURPLE_CORAL_FAN = registerBlock(new BlockCoralFan(MapColor.PURPLE), "purple_coral_fan");
		PURPLE_CORAL_FAN_DEAD = registerBlock(new BlockCoralFan(MapColor.GRAY), "purple_coral_fan_dead");
		RED_CORAL_FAN = registerBlock(new BlockCoralFan(MapColor.RED), "red_coral_fan");
		RED_CORAL_FAN_DEAD = registerBlock(new BlockCoralFan(MapColor.GRAY), "red_coral_fan_dead");
		YELLOW_CORAL_FAN = registerBlock(new BlockCoralFan(MapColor.YELLOW), "yellow_coral_fan");
		YELLOW_CORAL_FAN_DEAD = registerBlock(new BlockCoralFan(MapColor.GRAY), "yellow_coral_fan_dead");
		
		BLUE_CORAL = registerBlock(new BlockCoral(MapColor.BLUE), "blue_coral");
		BLUE_CORAL_DEAD = registerBlock(new BlockCoral(MapColor.GRAY), "blue_coral_dead");
		PINK_CORAL = registerBlock(new BlockCoral(MapColor.BLUE), "pink_coral");
		PINK_CORAL_DEAD = registerBlock(new BlockCoral(MapColor.GRAY), "pink_coral_dead");
		PURPLE_CORAL = registerBlock(new BlockCoral(MapColor.BLUE), "purple_coral");
		PURPLE_CORAL_DEAD = registerBlock(new BlockCoral(MapColor.GRAY), "purple_coral_dead");
		RED_CORAL = registerBlock(new BlockCoral(MapColor.BLUE), "red_coral");
		RED_CORAL_DEAD = registerBlock(new BlockCoral(MapColor.GRAY), "red_coral_dead");
		YELLOW_CORAL = registerBlock(new BlockCoral(MapColor.BLUE), "yellow_coral");
		YELLOW_CORAL_DEAD = registerBlock(new BlockCoral(MapColor.GRAY), "yellow_coral_dead");
		};

		@SubscribeEvent
		public static void registerBlocks(RegistryEvent.Register<Block> event)
		{
			if (ConfigHandler.block.guardianSpike.enableGuardianSpike == true)
			{ event.getRegistry().registerAll(GUARDIAN_SPIKE); }
			
			for (Block blocks : blockList) event.getRegistry().register(blocks);
		}
		
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> r = event.getRegistry();
		
		if (ConfigHandler.block.guardianSpike.enableGuardianSpike == true)
		{ registerItemBlock(r, GUARDIAN_SPIKE); }
		
		registerItemBlock(r, new ItemBlockSeaPickle(SEA_PICKLE));
		registerItemBlock(r, SEAGRASS);
		registerItemBlock(r, TALL_SEAGRASS);
		registerItemBlock(r, KELP);
		registerItemBlock(r, KELP_TOP);
		registerItemBlock(r, DRIED_KELP_BLOCK);
		registerItemBlock(r, COCONUT);
		registerItemBlock(r, COCONUT_LEAVES);
		registerItemBlock(r, COCONUT_LEAVES_FLOWERING);
		registerItemBlock(r, COCONUT_SAPLING);
		registerItemBlock(r, SEA_TURTLE_EGG);
		registerItemBlock(r, SHELL_SAND);
		registerItemBlock(r, WRACK);
		registerItemBlock(r, SEA_OATS);
		registerItemBlock(r, BLUE_ICE);
		registerItemBlock(r, BLUE_CORAL_BLOCK);
		registerItemBlock(r, BLUE_CORAL_BLOCK_DEAD);
		registerItemBlock(r, PINK_CORAL_BLOCK);
		registerItemBlock(r, PINK_CORAL_BLOCK_DEAD);
		registerItemBlock(r, PURPLE_CORAL_BLOCK);
		registerItemBlock(r, PURPLE_CORAL_BLOCK_DEAD);
		registerItemBlock(r, RED_CORAL_BLOCK);
		registerItemBlock(r, RED_CORAL_BLOCK_DEAD);
		registerItemBlock(r, YELLOW_CORAL_BLOCK);
		registerItemBlock(r, YELLOW_CORAL_BLOCK_DEAD);
		registerItemBlock(r, BLUE_CORAL_FAN);
		registerItemBlock(r, BLUE_CORAL_FAN_DEAD);
		registerItemBlock(r, PINK_CORAL_FAN);
		registerItemBlock(r, PINK_CORAL_FAN_DEAD);
		registerItemBlock(r, PURPLE_CORAL_FAN);
		registerItemBlock(r, PURPLE_CORAL_FAN_DEAD);
		registerItemBlock(r, RED_CORAL_FAN);
		registerItemBlock(r, RED_CORAL_FAN_DEAD);
		registerItemBlock(r, YELLOW_CORAL_FAN);
		registerItemBlock(r, YELLOW_CORAL_FAN_DEAD);
		registerItemBlock(r, BLUE_CORAL);
		registerItemBlock(r, BLUE_CORAL_DEAD);
		registerItemBlock(r, PINK_CORAL);
		registerItemBlock(r, PINK_CORAL_DEAD);
		registerItemBlock(r, PURPLE_CORAL);
		registerItemBlock(r, PURPLE_CORAL_DEAD);
		registerItemBlock(r, RED_CORAL);
		registerItemBlock(r, RED_CORAL_DEAD);
		registerItemBlock(r, YELLOW_CORAL);
		registerItemBlock(r, YELLOW_CORAL_DEAD);
	}
	
	public static Block registerBlock(Block block, String name) 
	{ return registerBlock(block, name, new ItemBlock(block)); }

	public static Block registerBlock(Block block, String name, ItemBlock item) {
		block.setUnlocalizedName(name);
		block.setRegistryName(name);
		
		blockList.add(block);

		return block;
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
		for(Block b : blockList)
		{
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(b), 0, new ModelResourceLocation(b.getRegistryName(), "inventory"));
			
			/** So many blocks are submergable, might as well set this for all **/
			ModelLoader.setCustomStateMapper(b, new StateMap.Builder().ignore(BlockLiquid.LEVEL).ignore(BlockCoralFan.IN_WATER).build());
			
			/** Sea Pickles actually use the IN_WATER **/
			ModelLoader.setCustomStateMapper(OEBlocks.SEA_PICKLE, new StateMap.Builder().ignore(BlockLiquid.LEVEL).build());
		}
		/**
		//Very hacky, setup a better method later
		Main.proxy.registerItemVariantModel(Item.getItemFromBlock(OEBlocks.BLUE_CORAL_BLOCK), OEBlocks.BLUE_CORAL_BLOCK.getRegistryName() + "_dead", 1);
		Main.proxy.registerItemVariantModel(Item.getItemFromBlock(OEBlocks.PINK_CORAL_BLOCK), OEBlocks.PINK_CORAL_BLOCK.getRegistryName() + "_dead", 1);
		Main.proxy.registerItemVariantModel(Item.getItemFromBlock(OEBlocks.PURPLE_CORAL_BLOCK), OEBlocks.PURPLE_CORAL_BLOCK.getRegistryName() + "_dead", 1);
		Main.proxy.registerItemVariantModel(Item.getItemFromBlock(OEBlocks.RED_CORAL_BLOCK), OEBlocks.RED_CORAL_BLOCK.getRegistryName() + "_dead", 1);
		Main.proxy.registerItemVariantModel(Item.getItemFromBlock(OEBlocks.YELLOW_CORAL_BLOCK), OEBlocks.YELLOW_CORAL_BLOCK.getRegistryName() + "_dead", 1);
		**/
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