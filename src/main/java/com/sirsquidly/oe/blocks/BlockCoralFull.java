package com.sirsquidly.oe.blocks;

import java.util.Random;

import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockCoralFull extends Block implements IChecksWater
{
	public static final PropertyBool IS_DEAD = PropertyBool.create("is_dead");

    /** This is used for storing the dead version of this coral, if there is one. */
    private Block deadVersion = null;

	public BlockCoralFull(MapColor blockMapColor, SoundType soundIn, Block deadVersionIn)
	{
		super(Material.ROCK, blockMapColor);
		this.setSoundType(soundIn);
        this.deadVersion = deadVersionIn;
		this.setDefaultState(this.blockState.getBaseState());		
		this.setHardness(1.5F);
		this.setResistance(6.5F);
		
		this.setTickRandomly(ConfigHandler.block.coralBlocks.coralBlockDryTicks != 0);
	}
	
	//** This just helps register dead coral faster */
	public BlockCoralFull()
	{ this(MapColor.GRAY, SoundType.STONE, null); }
	
    public int getMetaFromState(IBlockState state)
    {
    	return 0;
    }
	
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this);
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    { return this.deadVersion != null ? Item.getItemFromBlock(this.deadVersion) : super.getItemDropped(state, rand, fortune); }
    
    protected boolean canSilkHarvest() { return true; }
    /**
     * Handles the Coral Death
     */
    
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
    	if (!this.checkWater(worldIn, pos, state))
        {
    		worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
        }
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!this.checkWater(worldIn, pos, state))
        {
        	worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
        }
    }
    
    protected boolean checkWater(World worldIn, BlockPos pos, IBlockState state)
    {
    	if (ConfigHandler.block.coralBlocks.coralBlockDryTicks == 0) return true;
    	
        boolean flag = false;

        for (EnumFacing enumfacing : EnumFacing.values())
        {
        	BlockPos blockpos = pos.offset(enumfacing);
            if (isWaterHere(worldIn, blockpos))
            {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {}
    
    /** Should be ~6 Seconds **/
    public int tickRate(World worldIn)
    {
        return ConfigHandler.block.coralBlocks.coralBlockDryTicks;
    }
    
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        boolean flag = this.checkWater(worldIn, pos, state);

        if (!flag && this.deadVersion != null)
        { worldIn.setBlockState(pos, this.deadVersion.getDefaultState(), 2); }
    }
}