package com.sirsquidly.oe.util.handlers;

import com.sirsquidly.oe.util.Reference;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Reference.MOD_ID)
@Config.LangKey("oe.config.title")
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class ConfigHandler 
{
	@Config.LangKey("oe.config.worldGen")
	@Config.Comment("Config related to World Gen")
    public static configWorldGen worldGen = new configWorldGen();
	
	public static class configWorldGen
	{
		
		
		@RequiresMcRestart
		@Config.LangKey("oe.config.worldGen.kelpForest")
	    public configKelpForest kelpForest = new configKelpForest();
		
		public static class configKelpForest
		{
			@RequiresMcRestart
		    @Config.LangKey("oe.config.worldGen.enableKelpForest")
		    @Config.Comment("If Kelp Forests should be enabled")
		    public boolean enableKelpForest = true;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.worldGen.kelpConnective")
		    @Config.Comment("The noise scale on the Kelp Forest. Smaller numbers make more interconnected noodles, while larger numbers make seperate clusters.")
		    public double kelpConnective = 0.2;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.worldGen.kelpSpread")
		    @Config.Comment("Adjusts the cutoff for the noise. Smaller numbers make the Kelp Forest more spread.")
		    public double kelpSpread = 0.1;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.worldGen.kelpDensity")
		    @Config.Comment("The chance for Kelp to be placed in the forest")
		    public double kelpDensity = 0.2;
		}
		
		@RequiresMcRestart
		@Config.LangKey("oe.config.worldGen.frozenOcean")
	    public configFrozenOcean frozenOcean = new configFrozenOcean();
		
		public static class configFrozenOcean
		{
			@RequiresMcRestart
		    @Config.LangKey("oe.config.worldGen.enableIceBerg")
		    @Config.Comment("If Icebergs should be enabled")
		    public boolean enableIcebergs = true;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.worldGen.enableIceSheet")
		    @Config.Comment("If Ice Sheets should be enabled")
		    public boolean enableIceSheet = true;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.worldGen.iceSheetSpread")
		    @Config.Comment("Adjusts the cutoff for the noise. Smaller numbers make the Ice Sheets more spread.")
		    public double iceSheetSpread = 0.3;
		}
			
	}
	
	@Config.LangKey("oe.config.block")
	@Config.Comment("Config related to Blocks")
    public static configBlock block = new configBlock();
	
	public static class configBlock
	{
		@RequiresMcRestart
	    @Config.LangKey("oe.config.block.driedKelpShears")
	    @Config.Comment("If the string on Dried Kelp can be removed using Shears")
	    public boolean driedKelpShears = true;
		
		@Config.LangKey("oe.config.block.turtleEggs")
	    public configTurtleEgg turtleEgg = new configTurtleEgg();
		
		public static class configTurtleEgg
		{
			@RequiresMcRestart
		    @Config.LangKey("oe.config.block.amountOnTrample")
		    @Config.Comment("Amount of Turtle Eggs broken each time the trampleAI succeeds.")
		    @Config.RangeInt(min = 0, max = 4)
		    public int amountOnTrample = 1;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.block.particlesOnFall")
		    @Config.Comment("If egg shell particles spawn when fallen on. (0 = Never, 1 = By AI, 2 = Always)")
		    @Config.RangeInt(min = 0, max = 2)
		    public int particlesOnFall = 2;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.block.puffOnTrample")
		    @Config.Comment("If puff particles spawn when trampled. (0 = Never, 1 = By AI, 2 = Always)")
		    @Config.RangeInt(min = 0, max = 2)
		    public int puffOnTrample = 2;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.block.zombiesTrample")
		    @Config.Comment("If any mob extending Zombie (Zombie, Husks, Drowned, ect) are given the trampleAI")
		    public boolean zombiesTrample = true;
	    }
		
		@Config.LangKey("oe.config.block.guardianSpike")
	    public configGuardianSpike guardianSpike = new configGuardianSpike();
		
		public static class configGuardianSpike
		{
			@RequiresMcRestart
		    @Config.LangKey("oe.config.block.enableGuardianSpike")
		    @Config.Comment("If the Guardian Spike is enabled")
		    public boolean enableGuardianSpike = true;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.block.guardianSpikeFallMultiplier")
		    @Config.Comment("How much a Guardian Spike multiplies Fall Damage.")
		    @Config.RangeDouble(min = 0, max = 9999)
		    public double guSpiFallMultiplier = 1.5;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.block.guardianSpikeDropChance")
		    @Config.Comment("The percent chance a Guardian drops a spike.")
		    @Config.RangeDouble(min = 0, max = 100)
		    public double guardianSpikeDropChance = 1;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.block.guardianSpikeLooting")
		    @Config.Comment("The percent increase per level of looting to the Guardian.")
		    @Config.RangeDouble(min = 0, max = 100)
		    public double guardianSpikeLooting = 0.5;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.block.guardianSpikeElderDropChance")
		    @Config.Comment("The percent chance an Elder Guardian drops a spike.")
		    @Config.RangeDouble(min = 0, max = 100)
		    public double guardianSpikeElderDropChance = 100;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.block.guardianSpikeElderLooting")
		    @Config.Comment("The percent increase per level of looting to the Elder Guardian.")
		    @Config.RangeDouble(min = 0, max = 100)
		    public double guardianSpikeElderLooting = 0;
	    }
		
		@Config.LangKey("oe.config.block.coconut")
	    public configCoconut coconut = new configCoconut();
		
		public static class configCoconut
		{
			@RequiresMcRestart
		    @Config.LangKey("oe.config.block.coconutHitSound")
		    @Config.Comment("If coconuts go Clonk when hitting an entity")
		    public boolean coconutHitSound = true;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.block.coconutFallBreak")
		    @Config.Comment("How many blocks a Coconut must fall to break. (-1 = Disabled entirely)")
		    @Config.RangeDouble(min = -1, max = 9999)
		    public double coconutFallBreak = 6.0;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.block.coconutFallDamage")
		    @Config.Comment("How much damage per block added to a falling Coconut.")
		    @Config.RangeDouble(min = -1, max = 9999)
		    public double coconutFallDamage = 1.0;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.block.coconutFallMaxDamage")
		    @Config.Comment("How much max damage can a falling coconut deal")
			@Config.RangeInt(min = 0, max = 9999)
		    public int coconutFallMaxDamage = 19;
	    }
	}
	
	@Config.LangKey("oe.config.item")
	@Config.Comment("Config related to Items, Tools, ect")
    public static configItem item = new configItem();
	
	public static class configItem
	{
		
		@RequiresMcRestart
	    @Config.LangKey("oe.config.coconutFallMaxDamage")
	    @Config.Comment("The cooldown between uses of the Conch")
		@Config.RangeInt(min = 0, max = 9999)
	    public int conchCooldown = 60;
		
		@RequiresMcRestart
		@Config.LangKey("oe.config.spawnBucket")
	    public configSpawnBucket spawnBucket = new configSpawnBucket();
		
		public static class configSpawnBucket
		{
			@Config.LangKey("oe.config.item.spawnBucketMobs")
		    @Config.Comment("Mobs that can be bucketed")
		    public String[] bucketableMobs = 
		{
				 	"minecraft:squid",
		            "minecraft:guardian",
				 	"minecraft:elder_guardian",
				 	"oe:cod",
				 	"oe:salmon",
				 	"oe:pufferfish",
				 	"oe:turtle",
				 	"oe:glow_squid",
				 	"oe:crab",
				 	"oe:drowned",
				 	"oe:pickled"
		    };
	    }
		
		@RequiresMcRestart
		@Config.LangKey("oe.config.trident")
	    public configTrident trident = new configTrident();
		
		public static class configTrident
		{
			@RequiresMcRestart
		    @Config.LangKey("oe.config.item.enableTrident")
		    @Config.Comment("If Tridents are enabled")
		    public boolean enableTrident = true;
			
		    @Config.LangKey("oe.config.item.tridentDamage")
		    @Config.Comment("The Attack Damage of the Trident")
		    public int tridentDamage = 9;
			
		    @Config.LangKey("oe.config.item.tridentThrowDamage")
		    @Config.Comment("The Attack Damage of the Thrown Trident")
		    public int tridentThrowDamage = 8;
			
		    @Config.LangKey("oe.config.item.tridentAttackSpeed")
		    @Config.Comment("The Attack Speed of the Trident")
		    public double tridentSpeed = 1.1;
			
		    @RequiresMcRestart
		    @Config.LangKey("oe.config.item.tridentDurability")
		    @Config.Comment("The Trident's Durability (note, displays as this -1 in-game, as 0 counts as a point)")
		    public int tridentDurability = 250;
		    
			@RequiresMcRestart
		    @Config.LangKey("oe.config.item.loyaltyVoidReturn")
		    @Config.Comment("Loyalty Tridents return when in the void")
		    public boolean loyaltyVoidReturn = true;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.item.loyaltyVoidReturnLevel")
		    @Config.Comment("Loyalty Tridents return when below this y-level. (Requires void return to be enabled))")
		    public int loyaltyVoidReturnLevel = -15;
	    }
	}
	
	@Config.LangKey("oe.config.entity")
	@Config.Comment("Config related to Entities")
    public static configEntity entity = new configEntity();
	
	public static class configEntity
	{
		@RequiresMcRestart
		@Config.LangKey("oe.config.entity.glowSquid")
	    public configGlowSquid glowSquid = new configGlowSquid();
		
		public static class configGlowSquid
		{
		    @Config.LangKey("oe.config.entity.glowSquidBodyBright")
		    @Config.Comment("Minimum brightness of Glow Squids. 15 is Full Bright")
		    public int glowSquidBodyBright = 4;
			
		    @RequiresMcRestart
		    @Config.LangKey("oe.config.entity.glowSquidLayer")
		    @Config.Comment("If Glow Squids should have a 2ed layer for brightness")
		    public boolean glowSquidLayer = true;
		    
		    @Config.LangKey("oe.config.entity.glowSquidLayerBright")
		    @Config.Comment("Brightness of a Glow Squids' second layer. 15 is Full Bright")
		    public int glowSquidLayerBright = 11;
	    }
		
		@RequiresMcRestart
		@Config.LangKey("oe.config.entity.drowned")
	    public configDrowned drowned = new configDrowned();
		
		public static class configDrowned
		{
			@RequiresMcRestart
		    @Config.LangKey("oe.config.entity.enableDrowned")
		    @Config.Comment("If Drowned should be enabled")
		    public boolean enableDrowned = true;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.entity.drownedArmorSpawning")
		    @Config.Comment("If Drowned can spawn wearing Armor the same way normal Zombies do.")
		    public boolean drownedArmorSpawning = true;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.entity.drownedTridentSpawnChance")
		    @Config.Comment("The percent chance a Drowned spawns holding a Trident.")
		    @Config.RangeDouble(min = 0, max = 100)
		    public double drownedTridentSpawnChance = 6.25;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.entity.drownedRodSpawnChance")
		    @Config.Comment("The percent chance a Drowned spawns holding a Fishing Rod. Note this will always run after the Trident check, so this will never replace a Trident spawn.")
		    @Config.RangeDouble(min = 0, max = 100)
		    public double drownedRodSpawnChance = 3.75;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.entity.drownedNautilusSpawnChance")
		    @Config.Comment("The percent chance a Drowned spawns holding a Nautilus Shell in its offhand.")
		    @Config.RangeDouble(min = 0, max = 100)
		    public double drownedNautilusSpawnChance = 3;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.entity.drownedTridentMeleeRange")
		    @Config.Comment("The distance (in blocks) in which a Trident Drowned will use melee rather than ranged attacks.")
		    @Config.RangeDouble(min = 0, max = 100)
		    public double drownedTridentMeleeRange = 3;
			
		    @RequiresMcRestart
		    @Config.LangKey("oe.config.entity.drownedGlowLayer")
		    @Config.Comment("If Drowned should have a 2ed layer for brightness")
		    public boolean drownedGlowLayer = true;
	    }
	}
	
	
	@Config.LangKey("oe.config.enchant")
	@Config.Comment("Config related to Enchantments")
    public static configEnchantment enchant = new configEnchantment();
	
	public static class configEnchantment
	{
		@Config.LangKey("oe.config.enchant.impaling")
		@Config.Comment("Config for Impaling and Water Jet")
	    public configImpaling impaling = new configImpaling();
		
		public static class configImpaling
		{
			@RequiresMcRestart
		    @Config.LangKey("oe.config.enchant.enableWaterJet")
		    @Config.Comment("Adds the Water Jet enchantment, which functions like Bedrock Edition's Impaling (Bonus damage to wet mobs). (0 = Disabled, 1 = Enabled, 2 = Disable, and make Impaling use this behavior instead.)")
		    public int enableWaterJet = 1;
			
		    @Config.LangKey("oe.config.enchant.impalingDamage")
		    @Config.Comment("Increases Impaling damage by this per level")
		    public float impalingDamage = 2.5F;
			
		    @Config.LangKey("oe.config.enchant.waterJetDamage")
		    @Config.Comment("Increases Water Jet damage by this per level")
		    public float waterJetDamage = 1.0F;
			
			@Config.LangKey("oe.config.enchant.impalingMobs")
		    @Config.Comment("Mobs affected by the Impaling enchantment")
		    public String[] aquaticMobs = {
				 	"minecraft:squid",
		            "minecraft:guardian",
				 	"minecraft:elder_guardian",
				 	"oe:cod",
				 	"oe:salmon",
				 	"oe:pufferfish",
				 	"oe:turtle",
				 	"oe:glow_squid",
				 	"oe:crab",
				 	"oe:drowned",
				 	"oe:pickled"
		    };
		}
		
		@Config.LangKey("oe.config.enchant.channeling")
		@Config.Comment("Config for Channeling")
	    public configChanneling channeling = new configChanneling();
		
		public static class configChanneling
		{
		    @Config.LangKey("oe.config.enchant.channelingWaterCheck")
		    @Config.Comment("Prevents Lightning if the target is in Water")
		    public boolean waterCheck = true;
			
		    @Config.LangKey("oe.config.enchant.channelingLavaCheck")
		    @Config.Comment("Prevents Lightning if the target is in Lava")
		    public boolean lavaCheck = true;
			
		    @Config.LangKey("oe.config.enchant.channelingInvert")
		    @Config.Comment("Inverts the Channeling Whitelist into a Blacklist")
		    public boolean invertLightning = false;
			
			@Config.LangKey("oe.config.enchant.channelingBlocks")
		    @Config.Comment("Blocks that Channeling Tridents strike Lighting on when hit")
		    public String[] lightningRodWhitelist = {
		            "minecraft:iron_bars"
		    };
			
			@Config.LangKey("oe.config.enchant.channelingRidingBlacklist")
		    @Config.Comment("Prevents Lightning if the target is riding any of these entities")
		    public String[] ridingBlacklist = {
				 	"minecraft:boat",
		            "minecraft:minecart"
		    };
		}
	}
	
	@Config.LangKey("oe.config.vanillaTweak")
	@Config.Comment("Config for any direct tweaks or changes to Vanilla content")
    public static configvanillaTweak vanillaTweak = new configvanillaTweak();
	
	public static class configvanillaTweak
	{
		@Config.LangKey("oe.config.vanillaTweak.waterLighting")
	    @Config.Comment("Alters how much light Water and Flowing Water block to 1, instead of 3. Automatically disabled as not only do I predict some MASSIVE incompatabilities, but this will require every water block to be updated. Every single one. ")
	    public boolean waterTweak = false;
		
		@Config.LangKey("oe.config.vanillaTweak.squidFlop")
	    @Config.Comment("Allows Squids to flop about on land like fish do.")
	    public boolean squidFlop = true;
		
		@Config.LangKey("oe.config.vanillaTweak.squidPush")
	    @Config.Comment("Allows the Player to shift right-click a Squid to push it a bit. Done as a workaround for players who want to help squids, but can't due to their buggy movement.")
	    public boolean squidPush = true;
	}
	
	@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
    public static class ConfigSyncHandler
    {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
        {
            if(event.getModID().equals(Reference.MOD_ID))
            {
                ConfigManager.sync(Reference.MOD_ID, Config.Type.INSTANCE);
            }
        }
    }
}