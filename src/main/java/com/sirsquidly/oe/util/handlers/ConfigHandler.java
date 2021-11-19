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
		@Config.LangKey("oe.config.trident")
	    public configTrident trident = new configTrident();
		
		public static class configTrident
		{
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
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.enchant.impalingDamage")
		    @Config.Comment("Increases Impaling damage by this per level")
		    public float impalingDamage = 2.5F;
			
			@RequiresMcRestart
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
			@RequiresMcRestart
		    @Config.LangKey("oe.config.enchant.channelingWaterCheck")
		    @Config.Comment("Prevents Lightning if the target is in Water")
		    public boolean waterCheck = true;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.enchant.channelingLavaCheck")
		    @Config.Comment("Prevents Lightning if the target is in Lava")
		    public boolean lavaCheck = true;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.enchant.channelingInvert")
		    @Config.Comment("Inverts the Channeling Whitelist into a Blacklist")
		    public boolean invertLightning = false;
			
			@RequiresMcRestart
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