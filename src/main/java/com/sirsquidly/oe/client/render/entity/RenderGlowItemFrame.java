package com.sirsquidly.oe.client.render.entity;

import javax.annotation.Nonnull;

import com.sirsquidly.oe.entity.item.EntityGlowItemFrame;
import com.sirsquidly.oe.init.OEItems;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderItemFrame;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemCompass;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.client.event.RenderItemInFrameEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderGlowItemFrame extends RenderItemFrame
{
	private static final ResourceLocation MAP_BACKGROUND_TEXTURES = new ResourceLocation("textures/map/map_background.png");
	private final Minecraft mc = Minecraft.getMinecraft();
	protected final RenderItem itemRenderer;
	
	public RenderGlowItemFrame(RenderManager renderManagerIn)
	{
		super(renderManagerIn, Minecraft.getMinecraft().getRenderItem());
		itemRenderer = Minecraft.getMinecraft().getRenderItem();
	}

	@Override
	public void doRender(EntityItemFrame entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		EntityGlowItemFrame glowItemFrame = (EntityGlowItemFrame) entity;
		GlStateManager.pushMatrix();
        BlockPos blockpos = entity.getHangingPosition();
        double d0 = (double)blockpos.getX() - entity.posX + x;
        double d1 = (double)blockpos.getY() - entity.posY + y;
        double d2 = (double)blockpos.getZ() - entity.posZ + z;
        GlStateManager.translate(d0 + 0.5D, d1 + 0.5D, d2 + 0.5D);
        
        if (glowItemFrame.extFacingDirection.getAxis() == EnumFacing.Axis.Y)
        {
        	GlStateManager.rotate(glowItemFrame.extFacingDirection == EnumFacing.DOWN ? -90.0F : 90.0F, 1.0F, 0.0F, 0.0F);
        	if (glowItemFrame.extFacingDirection == EnumFacing.UP) GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        }
        else
        { GlStateManager.rotate(180.0F - entity.rotationYaw, 0.0F, 1.0F, 0.0F); }
        
        this.renderManager.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.pushMatrix();
        GlStateManager.translate(-0.5F, -0.5F, -0.5F);

		if (this.renderOutlines)
        {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }

		renderModel(glowItemFrame, mc);

		if (this.renderOutlines)
        {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.popMatrix();
        GlStateManager.translate(0.0F, 0.0F, 0.4375F);
		renderItem(entity);
		GlStateManager.popMatrix();
		/** A switch between a name rendering either at the Quark name positions, or in the future vanilla position. */
		double nameYOffset = ConfigHandler.item.glowItemFrame.glowItemFrameQuarkNamePosition ? glowItemFrame.extFacingDirection == EnumFacing.DOWN ? 0.75D : 0.25D : 0.0D;
		renderName(entity, x + glowItemFrame.extFacingDirection.getXOffset() * 0.3F, y - (nameYOffset), z + glowItemFrame.extFacingDirection.getZOffset() * 0.3F);
	}

	protected void renderModel(EntityGlowItemFrame entity, Minecraft mc)
	{
		/**
		 * Why yes, I am rendering the Glow Item Frame as an Item. I gave up trying to understand how to use ModelResourceLocation, because I value
		 * my time and sanity.
		 */
		GlStateManager.translate(0.5F, 0.5F, 0.5F);
		
		GlStateManager.pushAttrib();
		RenderHelper.enableStandardItemLighting();
		
		ItemStack trident = new ItemStack(OEItems.GLOW_ITEM_FRAME);
		NBTTagCompound nbt = trident.hasTagCompound() ? trident.getTagCompound() : new NBTTagCompound();

	    ItemStack displayStack = entity.getDisplayedItem();
	    
	    if(!displayStack.isEmpty() && displayStack.getItem() instanceof ItemMap && Items.FILLED_MAP.getMapData(displayStack, mc.world) != null)
	    { nbt.setInteger("map", 1); }
	    else { nbt.setInteger("normal", 1); }
	    
	    trident.setTagCompound(nbt);
	    
		itemRenderer.renderItem(trident, ItemCameraTransforms.TransformType.FIXED);
		RenderHelper.disableStandardItemLighting();
		GlStateManager.popAttrib();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityItemFrame entity)
	{ return null; }

	private void renderItem(EntityItemFrame itemFrame)
	{
		EntityGlowItemFrame glowItemFrame = (EntityGlowItemFrame) itemFrame;
		ItemStack itemstack = itemFrame.getDisplayedItem();

		if(!itemstack.isEmpty())
		{
			GlStateManager.pushMatrix();
			GlStateManager.disableLighting();
			
			int i = itemstack.getItem() instanceof ItemMap ? itemFrame.getRotation() % 4 * 2 : itemFrame.getRotation();
			
			GlStateManager.rotate(i * 360.0F / 8.0F, 0.0F, 0.0F, 1.0F);

			RenderItemInFrameEvent event = new RenderItemInFrameEvent(itemFrame, this);
			if (!MinecraftForge.EVENT_BUS.post(event))
			{
				if(itemstack.getItem() instanceof ItemMap)
				{
					renderManager.renderEngine.bindTexture(MAP_BACKGROUND_TEXTURES);
					GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
					float f = 0.0078125F;
					GlStateManager.scale(f, f, f);
					GlStateManager.translate(-64.0F, -64.0F, 0.0F);
					MapData mapdata = ((ItemMap) itemstack.getItem()).getMapData(itemstack, itemFrame.world);
					GlStateManager.translate(0.0F, 0.0F, -1.0F);

					if(mapdata != null)
						mc.entityRenderer.getMapItemRenderer().renderMap(mapdata, true);
				}
				else
				{
					/** If it's a compass on the ceiling, mirror it so the needle points the correct direction. */
					if (itemstack.getItem() instanceof ItemCompass && glowItemFrame.extFacingDirection == EnumFacing.DOWN) GlStateManager.rotate(-180.0F, 0.0F, 1.0F, 0.0F);
					
					renderItemStack(itemFrame, itemstack);
				}
			}

			GlStateManager.enableLighting();
			GlStateManager.popMatrix();
		}
	}

	protected void renderItemStack(EntityItemFrame itemFrame, ItemStack stack)
	{
		GlStateManager.scale(0.5F, 0.5F, 0.5F);
		if (stack.getItem() instanceof ItemBlock && ((EntityGlowItemFrame) itemFrame).extFacingDirection.getAxis() == Axis.Y) GlStateManager.rotate(-90F, 1F, 0F, 0F);
		
		GlStateManager.pushAttrib();
		RenderHelper.enableStandardItemLighting();
		int j = 15728880 % 65536;
        int k = 15728880 / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j, k);
		itemRenderer.renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
		
		RenderHelper.disableStandardItemLighting();
		GlStateManager.popAttrib();
	}

	@Override
	protected void renderName(@Nonnull EntityItemFrame entity, double x, double y, double z)
	{
		if(Minecraft.isGuiEnabled() && !entity.getDisplayedItem().isEmpty() && entity.getDisplayedItem().hasDisplayName() && renderManager.pointedEntity == entity)
		{
			double d0 = entity.getDistanceSq(renderManager.renderViewEntity);
			float f = entity.isSneaking() ? 32.0F : 64.0F;

			if(d0 < f * f)
			{
				String s = entity.getDisplayedItem().getDisplayName();
				renderLivingLabel(entity, s, x, y, z, 64);
			}
		}
	}
}