package com.sirsquidly.oe.util.handlers;

import com.sirsquidly.oe.client.gui.GuiPrismarinePot;
import com.sirsquidly.oe.inventory.ContainerPrismarinePot;
import com.sirsquidly.oe.tileentity.TilePrismarinePot;

import net.minecraft.entity.player.EntityPlayer;
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
			return new ContainerPrismarinePot(player, (TilePrismarinePot)world.getTileEntity(new BlockPos(x, y, z)));
		}
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
	{
		if(ID == 3)
		{
			return new GuiPrismarinePot(player, (TilePrismarinePot)world.getTileEntity(new BlockPos(x, y, z)));
		}
		return null;
	}
}
