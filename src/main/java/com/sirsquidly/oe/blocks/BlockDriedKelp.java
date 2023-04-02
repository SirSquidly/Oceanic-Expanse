package com.sirsquidly.oe.blocks;

import javax.annotation.Nullable;

import com.sirsquidly.oe.init.OEBlocks;
import com.sirsquidly.oe.init.OEItems;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDriedKelp extends Block
{
	public static final PropertyBool OPEN = PropertyBool.create("stringless");

	public BlockDriedKelp() {
		super(Material.WOOD);
		this.setSoundType(SoundType.PLANT);
		
        this.setDefaultState(this.blockState.getBaseState().withProperty(OPEN, false));
        setHardness(0.5f);
		setResistance(2.5f);
	}

	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
		return 20;
	}

	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {
		return 5;
	}

	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		ItemStack itemstack = playerIn.getHeldItem(hand);
		Item item = itemstack.getItem();

		if (!((Boolean)state.getValue(OPEN)).booleanValue() && (this == OEBlocks.DRIED_DULSE_BLOCK ? ConfigHandler.block.dulse.driedDulseShears : ConfigHandler.block.driedKelpShears))
        {
			if (item == Items.SHEARS)
	        {
				worldIn.setBlockState(pos, this.getDefaultState().withProperty(OPEN, true));
				worldIn.playSound((EntityPlayer)null, pos, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 1.0F, 1.0F);
	            return true;
	        }
        }
		return false;
    }
	
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
    {
		player.addStat(StatList.getBlockStats(this));
		player.addExhaustion(0.005F);
		
        if (((Boolean)state.getValue(OPEN)).booleanValue())
        {
            if (this == OEBlocks.DRIED_KELP_BLOCK) spawnAsEntity(worldIn, pos, new ItemStack(OEItems.DRIED_KELP, 9, 0));
            else if (this == OEBlocks.DRIED_DULSE_BLOCK) spawnAsEntity(worldIn, pos, new ItemStack(OEItems.DRIED_DULSE, 9, 0));
            else spawnAsEntity(worldIn, pos, new ItemStack(this, 1, 0));
        }
        else
        { spawnAsEntity(worldIn, pos, new ItemStack(this, 1, 0)); }
    }
	
	public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(OPEN, Boolean.valueOf((meta & 1) == 1));
    }

    public int getMetaFromState(IBlockState state)
    {
        return ((Boolean)state.getValue(OPEN)).booleanValue() ? 1 : 0;
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {OPEN});
    }
}
