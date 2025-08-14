package com.sirsquidly.oe.blocks.tileentity;

import net.minecraft.tileentity.TileEntitySkull;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TilePickledSkull extends TileEntitySkull
{
	/** Current Comparator Output */
	public int currCompOutput;
	public int prevCompOutput;
	/** Tracks if the Intestines should be out */
	private boolean intestinesOut;
	
	public void update()
    {
		if (this.world.getTotalWorldTime() % 20L == 0L)
		{
			if (this.world.isRainingAt(this.pos))
	        {
	            if (this.world.isThundering())
	            {
	            	updatePowerAndVisual(7);
	            }
	            else
	            {
	            	updatePowerAndVisual(3);
	            }
	        }
			else
			{
				updatePowerAndVisual(0);
			}
			
			if ( prevCompOutput != currCompOutput )
			{
				this.markDirty();
				prevCompOutput = currCompOutput;
			}
		}
    }
	
	 public void updatePowerAndVisual(int the)
	 { 
		 currCompOutput = the;
		 this.intestinesOut = the > 0;
	 }
	
	
	@SideOnly(Side.CLIENT)
    public boolean getIntestinesOut()
    { return this.intestinesOut; }
}