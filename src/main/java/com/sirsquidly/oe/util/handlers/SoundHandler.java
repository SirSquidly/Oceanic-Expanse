package com.sirsquidly.oe.util.handlers;

import com.sirsquidly.oe.util.Reference;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class SoundHandler 
{
	public static SoundEvent ENTITY_DROWNED_AMBIENT;
	public static SoundEvent ENTITY_DROWNED_HURT;
	public static SoundEvent ENTITY_DROWNED_DEATH;
	public static SoundEvent ENTITY_DROWNED_THROW;
	public static SoundEvent COCONUT_HIT;
	public static SoundEvent BLOCK_CONDUIT_ACTIVATE;
	public static SoundEvent BLOCK_CONDUIT_BEAT;
	public static SoundEvent BLOCK_CONDUIT_AMBIENT;
	public static SoundEvent BLOCK_CONDUIT_ATTACK;
	public static SoundEvent BLOCK_CONDUIT_DEACTIVATE;
	public static SoundEvent ITEM_CONCH_BLOW1;
	public static SoundEvent ITEM_CONCH_BLOW2;
	public static SoundEvent ITEM_CONCH_BLOW3;
	public static SoundEvent ITEM_CONCH_BLOW4;
	public static SoundEvent ENTITY_TRIDENT_SHOOT;
	public static SoundEvent ENTITY_TRIDENT_HIT;
	public static SoundEvent ENTITY_TRIDENT_HIT_ENTITY;
	
	public static void registerSounds()
	{
		ENTITY_DROWNED_AMBIENT = registerSound("entity.drowned.drowned_ambient");
		ENTITY_DROWNED_HURT = registerSound("entity.drowned.drowned_hurt");
		ENTITY_DROWNED_DEATH = registerSound("entity.drowned.drowned_death");
		ENTITY_DROWNED_THROW = registerSound("entity.drowned.drowned_throw");
		COCONUT_HIT = registerSound("block.coconut.coconut_hit");
		ITEM_CONCH_BLOW1 = registerSound("item.conch.conch_blow1");
		ITEM_CONCH_BLOW2 = registerSound("item.conch.conch_blow2");
		ITEM_CONCH_BLOW3 = registerSound("item.conch.conch_blow3");
		ITEM_CONCH_BLOW4 = registerSound("item.conch.conch_blow4");
		ENTITY_TRIDENT_SHOOT = registerSound("item.trident.trident_throw");
		ENTITY_TRIDENT_HIT = registerSound("item.trident.trident_hit");
		ENTITY_TRIDENT_HIT_ENTITY = registerSound("item.trident.trident_hit_entity");
	}
	
	private static SoundEvent registerSound(String name)
	{
		ResourceLocation location = new ResourceLocation(Reference.MOD_ID, name);
		SoundEvent event = new SoundEvent(location);
		event.setRegistryName(name);
		ForgeRegistries.SOUND_EVENTS.register(event);
		return event;
	}
}