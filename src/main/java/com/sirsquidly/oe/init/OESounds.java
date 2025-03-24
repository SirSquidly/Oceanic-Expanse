package com.sirsquidly.oe.init;

import java.util.ArrayList;
import java.util.List;

import com.sirsquidly.oe.Main;

import net.minecraft.block.SoundType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class OESounds 
{
	/** Contains every block ran through the 'blockReadyForRegister' function. So they all SHOULD be registered. */
	private static List<SoundEvent> soundList = new ArrayList<SoundEvent>();
	
	public static SoundEvent BLOCK_COCONUT_HIT = soundReadyForRegister("block.coconut.coconut_hit");
	
	public static SoundEvent BLOCK_CONDUIT_ACTIVATE = soundReadyForRegister("block.conduit.activate");
	public static SoundEvent BLOCK_CONDUIT_BEAT = soundReadyForRegister("block.conduit.ambient");
	public static SoundEvent BLOCK_CONDUIT_AMBIENT = soundReadyForRegister("block.conduit.ambient_short");
	public static SoundEvent BLOCK_CONDUIT_ATTACK = soundReadyForRegister("block.conduit.attack");
	public static SoundEvent BLOCK_CONDUIT_DEACTIVATE = soundReadyForRegister("block.conduit.deactivate");
	
	public static SoundEvent BLOCK_CORAL_BREAK = soundReadyForRegister("block.coral.break");
	public static SoundEvent BLOCK_CORAL_FALL = soundReadyForRegister("block.coral.fall");
	public static SoundEvent BLOCK_CORAL_HIT = soundReadyForRegister("block.coral.hit");
	public static SoundEvent BLOCK_CORAL_PLACE = soundReadyForRegister("block.coral.place");
	public static SoundEvent BLOCK_CORAL_STEP = soundReadyForRegister("block.coral.step");
	
	public static SoundType CORAL = new SoundType(1.0f, 1.0f, BLOCK_CORAL_BREAK, BLOCK_CORAL_STEP, BLOCK_CORAL_PLACE, BLOCK_CORAL_HIT, BLOCK_CORAL_FALL);

	public static SoundEvent BLOCK_TURTLE_EGG_CRACK = soundReadyForRegister("block.turtle_egg.crack");
	public static SoundEvent BLOCK_TURTLE_EGG_BREAK = soundReadyForRegister("block.turtle_egg.egg_break");
	public static SoundEvent BLOCK_TURTLE_EGG_STOMP = soundReadyForRegister("block.turtle_egg.fall");
	public static SoundEvent BLOCK_TURTLE_EGG_HATCH = soundReadyForRegister("block.turtle_egg.hatch");

	public static SoundEvent BLOCK_WET_GRASS_BREAK = soundReadyForRegister("block.wet_grass.break");
	public static SoundEvent BLOCK_WET_GRASS_FALL = soundReadyForRegister("block.wet_grass.fall");
	public static SoundEvent BLOCK_WET_GRASS_HIT = soundReadyForRegister("block.wet_grass.hit");
	public static SoundEvent BLOCK_WET_GRASS_PLACE = soundReadyForRegister("block.wet_grass.place");
	public static SoundEvent BLOCK_WET_GRASS_STEP = soundReadyForRegister("block.wet_grass.step");
	
	public static SoundType WET_GRASS = new SoundType(1.0f, 1.0f, BLOCK_WET_GRASS_BREAK, BLOCK_WET_GRASS_STEP, BLOCK_WET_GRASS_PLACE, BLOCK_WET_GRASS_HIT, BLOCK_WET_GRASS_FALL);
	
	public static SoundEvent ENTITY_CLAM_CLOSE = soundReadyForRegister("entity.clam.close");
	public static SoundEvent ENTITY_CLAM_DEATH = soundReadyForRegister("entity.clam.death");
	public static SoundEvent ENTITY_CLAM_HURT = soundReadyForRegister("entity.clam.hurt");
	public static SoundEvent ENTITY_CLAM_HURT_CLOSED = soundReadyForRegister("entity.clam.hurt_closed");
	public static SoundEvent ENTITY_CLAM_SHAKE = soundReadyForRegister("entity.clam.shake");
	public static SoundEvent ENTITY_CLAM_OPEN = soundReadyForRegister("entity.clam.open");
	public static SoundEvent ENTITY_CLAM_OPEN_LAND = soundReadyForRegister("entity.clam.open_land");
	
	public static SoundEvent ENTITY_COD_DEATH = soundReadyForRegister("entity.cod.death");
	public static SoundEvent ENTITY_COD_FLOP = soundReadyForRegister("entity.cod.flop");
	public static SoundEvent ENTITY_COD_HURT = soundReadyForRegister("entity.cod.hurt");
	
	public static SoundEvent ENTITY_CRAB_AMBIENT = soundReadyForRegister("entity.crab.ambient");
	public static SoundEvent ENTITY_CRAB_ANGRY = soundReadyForRegister("entity.crab.angry");
	public static SoundEvent ENTITY_CRAB_DEATH = soundReadyForRegister("entity.crab.death");
	public static SoundEvent ENTITY_CRAB_HURT = soundReadyForRegister("entity.crab.hurt");

	public static SoundEvent ENTITY_DOLPHIN_AMBIENT_WATER = soundReadyForRegister("entity.dolphin.ambient.water");
	public static SoundEvent ENTITY_DOLPHIN_HURT = soundReadyForRegister("entity.dolphin.hurt");

	public static SoundEvent ENTITY_DROWNED_AMBIENT = soundReadyForRegister("entity.drowned.ambient");
	public static SoundEvent ENTITY_DROWNED_HURT = soundReadyForRegister("entity.drowned.hurt");
	public static SoundEvent ENTITY_DROWNED_DEATH = soundReadyForRegister("entity.drowned.death");
	public static SoundEvent ENTITY_DROWNED_STEP = soundReadyForRegister("entity.drowned.step");
	public static SoundEvent ENTITY_DROWNED_THROW = soundReadyForRegister("entity.drowned.throw");
	
	// ** This sound is used when a fish doesn't specify a flop sound. */
	public static SoundEvent ENTITY_FISH_FLOP = soundReadyForRegister("entity.fish.flop");
	public static SoundEvent ENTITY_FISH_SWIM = soundReadyForRegister("entity.fish.swim");
	
	public static SoundEvent ENTITY_LOBSTER_ANGRY = soundReadyForRegister("entity.lobster.angry");
	public static SoundEvent ENTITY_LOBSTER_DEATH = soundReadyForRegister("entity.lobster.death");
	public static SoundEvent ENTITY_LOBSTER_HURT = soundReadyForRegister("entity.lobster.hurt");
	public static SoundEvent ENTITY_LOBSTER_MOLT = soundReadyForRegister("entity.lobster.molt");
	
	public static SoundEvent ENTITY_PUFFERFISH_DEATH = soundReadyForRegister("entity.pufferfish.death");
	public static SoundEvent ENTITY_PUFFERFISH_DEFLATE = soundReadyForRegister("entity.pufferfish.deflate");
	public static SoundEvent ENTITY_PUFFERFISH_FLOP = soundReadyForRegister("entity.pufferfish.flop");
	public static SoundEvent ENTITY_PUFFERFISH_HURT = soundReadyForRegister("entity.pufferfish.hurt");
	public static SoundEvent ENTITY_PUFFERFISH_INFLATE = soundReadyForRegister("entity.pufferfish.inflate");
	public static SoundEvent ENTITY_PUFFERFISH_STING = soundReadyForRegister("entity.pufferfish.sting");
	
	public static SoundEvent ENTITY_SALMON_DEATH = soundReadyForRegister("entity.salmon.death");
	public static SoundEvent ENTITY_SALMON_FLOP = soundReadyForRegister("entity.salmon.flop");
	public static SoundEvent ENTITY_SALMON_HURT = soundReadyForRegister("entity.salmon.hurt");
	public static SoundEvent ENTITY_SQUID_SQUIRT = soundReadyForRegister("entity.squid.squirt");
	
	public static SoundEvent ENTITY_TRIDENT_SHOOT = soundReadyForRegister("item.trident.throw");
	public static SoundEvent ENTITY_TRIDENT_IMPACT = soundReadyForRegister("item.trident.impact");
	public static SoundEvent ENTITY_TRIDENT_HIT = soundReadyForRegister("item.trident.pierce");
	public static SoundEvent ENTITY_TRIDENT_RETURN = soundReadyForRegister("item.trident.return");
	public static SoundEvent ENTITY_TRIDENT_RIPTIDE1 = soundReadyForRegister("item.trident.riptide1");
	public static SoundEvent ENTITY_TRIDENT_RIPTIDE2 = soundReadyForRegister("item.trident.riptide2");
	public static SoundEvent ENTITY_TRIDENT_RIPTIDE3 = soundReadyForRegister("item.trident.riptide3");
	public static SoundEvent ENTITY_TRIDENT_THUNDER = soundReadyForRegister("item.trident.thunder");
	
	public static SoundEvent ENTITY_TROPICAL_SLIME_DEATH = soundReadyForRegister("entity.tropical_slime.death");
	public static SoundEvent ENTITY_TROPICAL_SLIME_HURT = soundReadyForRegister("entity.tropical_slime.hurt");
	public static SoundEvent ENTITY_TROPICAL_SLIME_JUMP = soundReadyForRegister("entity.tropical_slime.jump");
	public static SoundEvent ENTITY_TROPICAL_SLIME_SQUISH = soundReadyForRegister("entity.tropical_slime.squish");
	
	public static SoundEvent ENTITY_TROPICAL_FISH_DEATH = soundReadyForRegister("entity.tropical_fish.death");
	public static SoundEvent ENTITY_TROPICAL_FISH_FLOP = soundReadyForRegister("entity.tropical_fish.flop");
	public static SoundEvent ENTITY_TROPICAL_FISH_HURT = soundReadyForRegister("entity.tropical_fish.hurt");
	
	public static SoundEvent ENTITY_TURTLE_AMBIENT = soundReadyForRegister("entity.turtle.ambient");
	public static SoundEvent ENTITY_TURTLE_BABY_HURT = soundReadyForRegister("entity.turtle.baby_hurt");
	public static SoundEvent ENTITY_TURTLE_HURT = soundReadyForRegister("entity.turtle.hurt");
	public static SoundEvent ENTITY_TURTLE_BABY_DEATH = soundReadyForRegister("entity.turtle.baby_death");
	public static SoundEvent ENTITY_TURTLE_DEATH = soundReadyForRegister("entity.turtle.death");
	public static SoundEvent ENTITY_TURTLE_BABY_STEP = soundReadyForRegister("entity.turtle.baby_step");
	public static SoundEvent ENTITY_TURTLE_STEP = soundReadyForRegister("entity.turtle.step");
	public static SoundEvent ENTITY_TURTLE_SWIM = soundReadyForRegister("entity.turtle.swim");

	public static SoundEvent ITEM_CONCH_BLOW1 = soundReadyForRegister("item.conch.conch_blow1");
	public static SoundEvent ITEM_CONCH_BLOW2 = soundReadyForRegister("item.conch.conch_blow2");
	public static SoundEvent ITEM_CONCH_BLOW3 = soundReadyForRegister("item.conch.conch_blow3");
	public static SoundEvent ITEM_CONCH_BLOW4 = soundReadyForRegister("item.conch.conch_blow4");
	public static SoundEvent ITEM_INK_SAC_SQUIRT = soundReadyForRegister("item.ink_sac.squirt");
	public static SoundEvent ITEM_SPAWN_BUCKET_EMPTY_FISH = soundReadyForRegister("item.spawn_bucket.empty_fish");
	public static SoundEvent ITEM_SPAWN_BUCKET_FILL_FISH = soundReadyForRegister("item.spawn_bucket.fill_fish");
	
	public static void registerSounds()
	{	
		for (SoundEvent sounds : soundList) ForgeRegistries.SOUND_EVENTS.register(sounds);
	}
	
	private static SoundEvent soundReadyForRegister(String name)
	{
		ResourceLocation location = new ResourceLocation(Main.MOD_ID, name);
		SoundEvent event = new SoundEvent(location);
		event.setRegistryName(name);
		soundList.add(event);
		
        return event;
    }
}