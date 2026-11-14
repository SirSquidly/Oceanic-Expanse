package com.sirsquidly.oe.util.handlers;

import com.sirsquidly.oe.client.gui.GuiPrismarinePot;
import com.sirsquidly.oe.inventory.ContainerPrismarinePot;
import com.sirsquidly.oe.blocks.tileentity.TilePrismarinePot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
	{
		if(ID == 3)
		{
			TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
			
			if (tile instanceof TilePrismarinePot)
			{ return new ContainerPrismarinePot(player, (TilePrismarinePot)tile); }
		}
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
	{
		if(ID == 3)
		{
			TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
			
			if (tile instanceof TilePrismarinePot)
			{ return new GuiPrismarinePot(player, (TilePrismarinePot)tile); }
		}
		return null;
	}
}
