package com.sirsquidly.oe;

import java.io.File;
import java.util.Random;

import com.sirsquidly.oe.blocks.BlockDoubleUnderwater;
import com.sirsquidly.oe.common.CreativeTab;
import com.sirsquidly.oe.entity.ai.EntityAIStompTurtleEgg;
import com.sirsquidly.oe.init.OEBlocks;
import com.sirsquidly.oe.proxy.CommonProxy;
import com.sirsquidly.oe.util.handlers.ConfigHandler;
import com.sirsquidly.oe.util.handlers.RegistryHandler;
import com.sirsquidly.oe.util.Reference;

import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

@Mod.EventBusSubscriber
@Mod(modid = Main.MOD_ID, name = Main.NAME, version = Main.VERSION)
public class Main {

	public static File config;
	
	public static final String MOD_ID = "oe";
	public static final String NAME = "Oceanic Expanse";
	public static final String VERSION = "1.0";
	public static final String ACCEPTED_VERSIONS = "[1.12.2,)";
	public static final String CLIENT_PROXY_CLASS = "com.sirsquidly.oe.proxy.ClientProxy";
	public static final String COMMON_PROXY_CLASS = "com.sirsquidly.oe.proxy.CommonProxy";
	
	public static CreativeTabs OCEANEXPTAB = new CreativeTab("oceanictab");
	
	@Instance 
	public static Main instance;
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
	public static CommonProxy proxy;
	
	@EventHandler
	public void PreInit(FMLPreInitializationEvent event)
	{
		RegistryHandler.preInitRegisteries(event);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.initRegistries(event);
	}
	
	@EventHandler
	public void PostInit(FMLPostInitializationEvent event)
	{}
	
	/** This feels hacky. Am I a hack? Probably. **/
	@SubscribeEvent
	public static void onBonemeal(BonemealEvent event) {
		IBlockState state = event.getBlock();
		World world = event.getWorld();
		BlockPos pos = event.getPos();

	if(world.getBlockState(pos.up()).getBlock() == Blocks.WATER && world.getBlockState(pos.up(2)).getBlock() == Blocks.WATER && state.isFullCube())
	{ growSeaGrass(event); }
	}
	
	public static void growSeaGrass(BonemealEvent event)
    {
		World world = event.getWorld();
		Random rand = world.rand;
        BlockPos blockpos = event.getPos().up();

        for (int i = 0; i < 128; ++i)
        {
            BlockPos blockpos1 = blockpos;
            int j = 0;

            while (true)
            {
                if (j >= i / 16)
                {
                    if (world.getBlockState(blockpos1.down()).isNormalCube() && world.getBlockState(blockpos1.up()).getBlock() == Blocks.WATER)
                    {
                    	if (world.getBlockState(blockpos1).getBlock() == Blocks.WATER)
                    	{
                    		world.setBlockState(blockpos1, OEBlocks.SEAGRASS.getDefaultState());
                        }
                    	if (world.getBlockState(blockpos1).getBlock() == OEBlocks.SEAGRASS)
                    	{
                    		if (rand.nextInt(10) == 0)
                    		{
                    			if (OEBlocks.TALL_SEAGRASS.canPlaceBlockAt(world, blockpos1))
                    	        {
                    	        	((BlockDoubleUnderwater) OEBlocks.TALL_SEAGRASS).placeAt(world, blockpos1, 2);
                    	        }	
                    		}
                        }
                    }
                	break;
                }
                blockpos1 = blockpos1.add(rand.nextInt(3) - 1, (rand.nextInt(3) - 1) * rand.nextInt(3) / 2, rand.nextInt(3) - 1);

                if (!(world.getBlockState(blockpos1.down()).isNormalCube()) || world.getBlockState(blockpos1).isNormalCube())
                {
                    break;
                }
                
                ++j;
            }
        }
    event.setResult(Result.ALLOW);
    }
	
	@SubscribeEvent
	public static void spawnEvent(EntityJoinWorldEvent event)
	{
		if(ConfigHandler.zombiesTrample && event.getEntity() instanceof EntityZombie)
		{
			EntityZombie zombie  = (EntityZombie)event.getEntity();			

			zombie.tasks.addTask(3, new EntityAIStompTurtleEgg(zombie, 1.0D));
		}
	}
}