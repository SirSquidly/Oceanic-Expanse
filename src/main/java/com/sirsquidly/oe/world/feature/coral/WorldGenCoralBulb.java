package com.sirsquidly.oe.world.feature.coral;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenCoralBulb extends CoralGen
{
	/** The chance for any block to not generate.*/
	private double failChance = 0.1F;
	
	public WorldGenCoralBulb(int type) 
	{
		super();
		this.coralType = type;
	}

	public boolean generate(World worldIn, Random rand, BlockPos pos)
    {
		if (this.coralType == 0)
		{
			this.coralType = rand.nextInt(5)+1;
		}

		int ranDwnShft = rand.nextInt(2) + 1;
		
		int len = rand.nextInt(3) + 3;
	    int hei = rand.nextInt(3) + 3;
	    int wid = rand.nextInt(3) + 3;
	    
	    for (int h1 = 0; h1 <= len; h1++)
	    {
	    	for (int i1 = 0; i1 <= hei; i1++)
	    	{
	    		for (int j1 = 0; j1 <= wid; j1++)
		    	{
	    			BlockPos tPos = new BlockPos(pos.getX() + h1, pos.getY() + i1, pos.getZ() + j1);
	    			
	    			//** Cuts off all edges on the X axis.**/
	    			if ((h1 != 0 || h1 == 0 && i1 != 0 && j1 != 0 && i1 != hei && j1 != wid) && (h1 != len || h1 == len && i1 != 0 && j1 != 0 && h1 == len && i1 != hei && j1 != wid))
	    			{
	    				//** Cuts off remaining edges.**/
	    				if ((i1 != 0 || i1 == 0 && j1 != 0 && j1 != wid) && (i1 != hei || i1 == hei && j1 != 0 && j1 != wid))
		    			{
	    					//** Used as to make it not generate anything on the inside. I'm currently too lazy to invert this check, so we are relying on the Else here!**/
	    					if (h1 != 0 && i1 != 0 && j1 != 0 && h1 != len && i1 != hei && j1 != wid)
	    					{}
	    					else if (rand.nextFloat() > failChance)
	    					{
	    						placeCoralBlockAt(worldIn, tPos.down(ranDwnShft), coralType);
	    					}
		    				
		    			}
	    			}
		    	}	
	    	}
	    }
		return true;
    }
}