package com.sirsquidly.oe.client.gui;

import com.sirsquidly.oe.Main;
import com.sirsquidly.oe.inventory.ContainerPrismarinePot;
import com.sirsquidly.oe.tileentity.TilePrismarinePot;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiPrismarinePot extends GuiContainer
{
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(Main.MOD_ID, "textures/gui/prismarine_pot.png");

    private final InventoryPlayer playerInventory;
    public TilePrismarinePot tilePrismarinePot;

    public GuiPrismarinePot(EntityPlayer player, TilePrismarinePot te)
    {
        super(new ContainerPrismarinePot(player, te));
        this.playerInventory = player.inventory;
        this.tilePrismarinePot = te;
        this.ySize = 150;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
    	String s = this.tilePrismarinePot.getDisplayName().getUnformattedText();
        this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
    }
}