package com.sirsquidly.oe.util.handlers;

import com.sirsquidly.oe.util.Reference;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class SoundHandler 
{
	public static SoundEvent SNOW_GLOBE_SHAKE;
	public static SoundEvent ENTITY_DROWNED_AMBIENT;
	public static SoundEvent ENTITY_DROWNED_HURT;
	public static SoundEvent ENTITY_DROWNED_DEATH;
	public static SoundEvent COCONUT_HIT;
	
	public static void registerSounds()
	{
		SNOW_GLOBE_SHAKE = registerSound("snow_globe_shake");
		ENTITY_DROWNED_AMBIENT = registerSound("drowned_ambient");
		ENTITY_DROWNED_HURT = registerSound("drowned_hurt");
		ENTITY_DROWNED_DEATH = registerSound("drowned_death");
		COCONUT_HIT = registerSound("coconut_hit");
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