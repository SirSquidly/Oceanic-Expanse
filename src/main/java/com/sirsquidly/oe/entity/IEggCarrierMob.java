package com.sirsquidly.oe.entity;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/** 
 * An interface used by any mob that mates, holds an egg, then goes and drops it off.
 * 
 * This exists so EntityAIMateCarryEgg and EntityAIMateDepositEgg don't need to be hard-programmed, and can instead be left pretty flexible.
 */
public interface IEggCarrierMob
{
	boolean isCarryingEgg();
	void setCarryingEgg(boolean bool);
	
	/** 
     * If the Egg can be placed at the given position.
     */
	boolean canLayEgg(World world, BlockPos pos);
	
	
	/** 
     * Places the egg into the world.
     * 
     * Runs at the end of EntityAIMateDepositEgg
     */
	void placeEgg(World world, BlockPos pos);
}