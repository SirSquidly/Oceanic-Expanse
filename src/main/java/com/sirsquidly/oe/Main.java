package com.sirsquidly.oe;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sirsquidly.oe.common.CreativeTab;
import com.sirsquidly.oe.entity.item.EntityTrident;
import com.sirsquidly.oe.init.OEEnchants;
import com.sirsquidly.oe.init.OEItems;
import com.sirsquidly.oe.proxy.CommonProxy;

import net.minecraft.block.BlockDispenser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod.EventBusSubscriber
@Mod(modid = Main.MOD_ID, name = Main.NAME, version = Main.VERSION)
public class Main {

	public static File config;
	public static Logger logger = LogManager.getLogger(Main.MOD_ID);
	
	public static final String MOD_ID = "oe";
	public static final String NAME = "Oceanic Expanse";
	public static final String CONFIG_NAME = "oceanic_expanse";
	public static final String VERSION = "1.2.1";
	public static final String ACCEPTED_VERSIONS = "[1.12.2]";
	public static final String CLIENT_PROXY_CLASS = "com.sirsquidly.oe.proxy.ClientProxy";
	public static final String COMMON_PROXY_CLASS = "com.sirsquidly.oe.proxy.CommonProxy";
	
	public static CreativeTabs OCEANEXPTAB = new CreativeTab("oceanictab");
	public static final EnumAction SPEAR = EnumHelper.addAction("SPEAR");
	
	/** Moved to get initialized as soon as possible, due to weird mod conflicts otherwise. Commented out as I wish to experiment with it later, but it's not necessary.*/
	//public static final EnumCreatureType WATER_MONSTER = EnumHelper.addCreatureType("WATER_MONSTER", IMob.class, 70, Material.WATER, false, false);
	
	@Instance 
	public static Main instance;
	
	@SidedProxy(clientSide = Main.CLIENT_PROXY_CLASS, serverSide = Main.COMMON_PROXY_CLASS)
	public static CommonProxy proxy;
	
	@EventHandler
	public void PreInit(FMLPreInitializationEvent event)
	{
		proxy.preInitRegisteries(event);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.initRegistries(event);
	}
	
	@EventHandler
	public void PostInit(FMLPostInitializationEvent event)
	{
		proxy.postInitRegistries(event);

		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(OEItems.TRIDENT_ORIG, new BehaviorProjectileDispense()
        {
			
			public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
		    {
				float velocity = EnchantmentHelper.getEnchantmentLevel(OEEnchants.RIPTIDE, stack) > 0 ? EnchantmentHelper.getEnchantmentLevel(OEEnchants.RIPTIDE, stack) * 0.8F : 0.8F;
				
				
		        World world = source.getWorld();
		        IPosition iposition = BlockDispenser.getDispensePosition(source);
		        EnumFacing enumfacing = (EnumFacing)source.getBlockState().getValue(BlockDispenser.FACING);
		        IProjectile iprojectile = this.getProjectileEntity(world, iposition, stack);
                
                boolean flag = EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) != 0 || EnchantmentHelper.getEnchantmentLevel(OEEnchants.LOYALTY, stack) != 0;

		        iprojectile.shoot((double)enumfacing.getXOffset(), (double)((float)enumfacing.getYOffset() + 0.1F), (double)enumfacing.getZOffset(), velocity * 3.0F, this.getProjectileInaccuracy());
		        world.spawnEntity((Entity)iprojectile);
		        
		        if (!flag) 
		        {
		        	stack.shrink(1);
		        }
		        else
		        {
		        	damageTrident(stack, 1);
		        }
		        
		        return stack;
		    }
			
			public void damageTrident(ItemStack stack, int amount)
			{
				if (!stack.isItemStackDamageable()) return;
				
				if (stack.getItemDamage() < stack.getMaxDamage() - 1)
				{
					stack.setItemDamage(stack.getItemDamage() + amount);
				}
				else
				{
					stack.shrink(1);
				}
			}
			
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn)
            {
            	EntityTrident entitytrient = new EntityTrident(worldIn, position.getX(), position.getY(), position.getZ());
            	
            	entitytrient.setItem(stackIn);
            	
            	int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stackIn);

                if (j > 0)
                {
                	entitytrient.setDamage(entitytrient.getDamage() + (double)j * 0.5D + 0.5D);
                }
                
                if (EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stackIn) > 0)
                { entitytrient.setKnockbackStrength(EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stackIn)); }
                
                if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stackIn) > 0) entitytrient.setFire(100);

                if (EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stackIn) == 0 && EnchantmentHelper.getEnchantmentLevel(OEEnchants.LOYALTY, stackIn) == 0)
                {
                	entitytrient.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                }

                return entitytrient;
            }
        });
	}
}