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
		@Config.LangKey("oe.config.worldGen.coquinaOutcrop")
	    public configCoquinaOutcrop coquinaOutcrop = new configCoquinaOutcrop();
		
		public static class configCoquinaOutcrop
		{
			@RequiresMcRestart
		    @Config.LangKey("oe.config.worldGen.enableCoquinaOutcrops")
		    @Config.Comment("If Coquina Outcrops should be enabled")
		    public boolean enableCoquinaOutcrops = true;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.worldGen.coquinaOutcropTriesPerChunk")
		    @Config.Comment("Coquina Outcrops tries per chunk to generate.")
		    public int coquinaOutcropTriesPerChunk = 1;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.worldGen.coquinaOutcropChancePerChunk")
		    @Config.Comment("Coquina Outcrops chance to generate, per try. 1 / this number")
		    public int coquinaOutcropChancePerChunk = 3;
		}
		
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
		    @Config.LangKey("oe.config.worldGen.enableFrozenOcean")
		    @Config.Comment("If Frozen Oceans should be enabled")
		    public boolean enableFrozenOcean = true;
			
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
			
			@RequiresMcRestart
			@Config.LangKey("oe.config.worldGen.frozenSeafloor")
		    public configFrozenSeafloor frozenSeafloor = new configFrozenSeafloor();
			
			public static class configFrozenSeafloor
			{
				@RequiresMcRestart
			    @Config.LangKey("oe.config.worldGen.enableRockDecor")
			    @Config.Comment("If Granite Chunks on the Frozen Ocean seafloor should be enabled")
			    public boolean enableRockDecor = true;
				
				@RequiresMcRestart
			    @Config.LangKey("oe.config.worldGen.seastarTriesPerChunk")
			    @Config.Comment("Seastar tries per chunk to generate.")
			    public int seastarTriesPerChunk = 6;
				
				@RequiresMcRestart
			    @Config.LangKey("oe.config.worldGen.seastarChancePerChunk")
			    @Config.Comment("Seastar chance to generate, per try. 1 / this number")
			    public int seastarChancePerChunk = 4;
				
				@RequiresMcRestart
			    @Config.LangKey("oe.config.worldGen.tubeSpongeTriesPerChunk")
			    @Config.Comment("Tube Sponge tries per chunk to generate.")
			    public int tubeSpongeTriesPerChunk = 1;
				
				@RequiresMcRestart
			    @Config.LangKey("oe.config.worldGen.tubeSpongeChancePerChunk")
			    @Config.Comment("Tube Sponge chance to generate, per try. 1 / this number")
			    public int tubeSpongeChancePerChunk = 3;
				
				@RequiresMcRestart
			    @Config.LangKey("oe.config.worldGen.dusleTriesPerChunk")
			    @Config.Comment("Dulse tries per chunk to generate.")
			    public int dusleTriesPerChunk = 2;
				
				@RequiresMcRestart
			    @Config.LangKey("oe.config.worldGen.dulseChancePerChunk")
			    @Config.Comment("Dulse chance to generate, per try. 1 / this number")
			    public int dulseChancePerChunk = 3;
				
				@RequiresMcRestart
			    @Config.LangKey("oe.config.worldGen.blueIceTriesPerChunk")
			    @Config.Comment("Blue Ice tries per chunk to generate.")
			    public int blueIceTriesPerChunk = 8;
				
				@RequiresMcRestart
			    @Config.LangKey("oe.config.worldGen.blueIceChancePerChunk")
			    @Config.Comment("Blue Ice chance to generate, per try. 1 / this number")
			    public int blueIceChancePerChunk = 2;
			}
		}
		
		@RequiresMcRestart
		@Config.LangKey("oe.config.worldGen.warmOcean")
	    public configWarmOcean warmOcean = new configWarmOcean();
		
		public static class configWarmOcean
		{
			@RequiresMcRestart
			@Config.LangKey("oe.config.worldGen.coralReef")
		    public configCoralReef coralReef = new configCoralReef();
			
			public static class configCoralReef
			{
				@RequiresMcRestart
			    @Config.LangKey("oe.config.worldGen.enableCoralReef")
			    @Config.Comment("If Coral Reefs should be enabled. Requires Warm Oceans.")
			    public boolean enableCoralReef = true;
				
				@RequiresMcRestart
			    @Config.LangKey("oe.config.worldGen.enableCoralStalk")
			    @Config.Comment("Enable Coral Stalks. The tree ones.")
			    public boolean enableCoralStalk= true;
				
				@RequiresMcRestart
			    @Config.LangKey("oe.config.worldGen.enableCoralBranch")
			    @Config.Comment("Enable Coral Branch/Claws. The sideways ones.")
			    public boolean enableCoralBranch= true;
				
				@RequiresMcRestart
			    @Config.LangKey("oe.config.worldGen.enableCoralBulb")
			    @Config.Comment("Enable Coral Bulbs/Mushrooms. The round ones.")
			    public boolean enableCoralBulb= true;
			}
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.worldGen.enableWarmOcean")
		    @Config.Comment("If Warm Oceans should be enabled")
		    public boolean enableWarmOcean = true;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.worldGen.seaPickleTriesPerChunk")
		    @Config.Comment("The Sea Pickles' tries per chunk to generate.")
		    public int seaPickleTriesPerChunk = 1;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.worldGen.seaPickleChancePerChunk")
		    @Config.Comment("The Sea Pickles' chance to generate, per try. 1 / this number")
		    public int seaPickleChancePerChunk = 6;
		}
		
		@RequiresMcRestart
		@Config.LangKey("oe.config.worldGen.palmTree")
	    public configPalmTree palmTree = new configPalmTree();
		
		public static class configPalmTree
		{
			@RequiresMcRestart
		    @Config.LangKey("oe.config.worldGen.enablePalmTrees")
		    @Config.Comment("If Palm Trees should be enabled")
		    public boolean enablePalmTrees = true;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.worldGen.palmTreeTriesPerChunk")
		    @Config.Comment("The Palm Trees' tries per chunk to generate.")
		    public int palmTreeTriesPerChunk = 3;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.worldGen.palmTreeChancePerChunk")
		    @Config.Comment("The Palm Trees' chance to generate, per try. 1 / this number")
		    public int palmTreeChancePerChunk = 8;
		}
		
		@RequiresMcRestart
		@Config.LangKey("oe.config.worldGen.shellPatch")
	    public configShellPatch shellPatch = new configShellPatch();
		
		public static class configShellPatch
		{
			@RequiresMcRestart
		    @Config.LangKey("oe.config.worldGen.enableShellPatch")
		    @Config.Comment("If Shell Patches should Generate")
		    public boolean enableShellPatch = true;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.worldGen.shellPatchTriesPerChunk")
		    @Config.Comment("The Shell Patches' tries per chunk to generate.")
		    public int shellPatchTriesPerChunk = 2;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.worldGen.shellPatchChancePerChunk")
		    @Config.Comment("The Shell Patches' chance to generate, per try. 1 / this number")
		    public int shellPatchChancePerChunk = 6;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.worldGen.shellPatchAboveSeaLevel")
		    @Config.Comment("Blocks above Sea Level that Shell Patches can be placed.")
		    public int shellPatchAboveSeaLevel = 2;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.worldGen.shellPatchSeaStarChance")
		    @Config.Comment("Chance for a Sea Star to be placed atop a generated Shelly Sand.")
		    public int shellPatchSeaStarChance = 20;
		}
		
		@RequiresMcRestart
		@Config.LangKey("oe.config.worldGen.shipwreck")
	    public configShipwreck shipwreck = new configShipwreck();
		
		public static class configShipwreck
		{
			@RequiresMcRestart
		    @Config.LangKey("oe.config.worldGen.enableShipwrecks")
		    @Config.Comment("If Shipwrecks should be enabled")
		    public boolean enableShipwrecks = true;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.worldGen.enableShipwreckDrowned")
		    @Config.Comment("Makes Drowned spawn in groups in Shipwrecks when generated. There's 1 group per chest, making more complete shipwrecks more challenging.")
		    public boolean enableShipwreckDrowned = true;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.worldGen.shipwreckChancePerChunk")
		    @Config.Comment("The Shipwreck's chance to generate, per try. 1 / this number")
		    public int shipwreckChancePerChunk = 300;
		}
	}
	
	@Config.LangKey("oe.config.block")
	@Config.Comment("Config related to Blocks")
    public static configBlock block = new configBlock();
	
	public static class configBlock
	{
		
		@Config.LangKey("oe.config.block.blueIce")
	    public configBlueIce blueIce = new configBlueIce();
		
		public static class configBlueIce
		{
			@RequiresMcRestart
		    @Config.LangKey("oe.config.block.enableBlueIce")
		    @Config.Comment("If Blue Ice is enabled")
		    public boolean enableBlueIce = true;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.block.blueIceLight")
		    @Config.Comment("How much Light Blue Ice gives off")
		    @Config.RangeDouble(min = 0, max = 16)
		    public double blueIceLight = 0;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.block.blueIceSlipperiness")
		    @Config.Comment("The slipperiness of Blue Ice")
		    @Config.RangeDouble(min = 0.6, max = 10)
		    public double blueIceSlipperiness = 0.989;
	    }
		
		@Config.LangKey("oe.config.block.conduit")
	    public configConduit conduit = new configConduit();
		
		public static class configConduit
		{
			@RequiresMcRestart
		    @Config.LangKey("oe.config.block.enableConduit")
		    @Config.Comment("Enables the Conduit. Wait, why would you disable this?! It took me forever to make!")
		    public boolean enableConduit = true;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.block.enableConduitPulse")
		    @Config.Comment("Enables the pulsing animation for the Conduit Eye/Heart.")
		    public boolean enableConduitPulse = true;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.block.conduitLight")
		    @Config.Comment("How much Light the Conduit emits")
		    @Config.RangeDouble(min = 0, max = 16)
		    public double conduitLight = 14;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.block.conduitParticles")
		    @Config.Comment("The particles used by the Conduit when active or Attacking. (0 = None, 1 = Bedrock Runes, 2 = Java Eyes/Nautilus, 3 = Both, 4 = Yellow Bolt)")
		    @Config.RangeInt(min = 0, max = 18)
		    public double conduitParticles = 3;
			
			@Config.LangKey("oe.config.block.conduitFrameBlocks")
		    @Config.Comment("Blocks accepted by the Conduit as Frame Blocks.")
		    public String[] conduitFrameBlocks = {
		            "minecraft:prismarine",
					"minecraft:sea_lantern"
		    };
	    }
		
		@Config.LangKey("oe.config.block.coralBlocks")
	    public configCoralBlocks coralBlocks = new configCoralBlocks();
		
		public static class configCoralBlocks
		{
			@RequiresMcRestart
		    @Config.LangKey("oe.config.block.enableCoralBlock")
		    @Config.Comment("Enables Coral Blocks")
		    public boolean enableCoralBlock = true;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.block.enableCoralFan")
		    @Config.Comment("Enables Coral Fans")
		    public boolean enableCoralFan = true;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.block.enableCoral")
		    @Config.Comment("Enabled Coral")
		    public boolean enableCoral = true;
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
		
		@RequiresMcRestart
	    @Config.LangKey("oe.config.block.driedKelpShears")
	    @Config.Comment("If the string on Dried Kelp can be removed using Shears")
	    public boolean driedKelpShears = true;
		
		@Config.LangKey("oe.config.block.palmBlocks")
	    public configPalmBlocks palmBlocks = new configPalmBlocks();
		
		public static class configPalmBlocks
		{
			@RequiresMcRestart
		    @Config.LangKey("oe.config.block.enablePalmWoods")
		    @Config.Comment("If Palm Woods are enabled. Note this doesn't disable Palm Trees, they will generate using Jungle Wood.")
		    public boolean enablePalmWoods = true;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.block.enablePalmStripping")
		    @Config.Comment("Adds the ability to strip Palm woods by using an Axe on it.")
		    public boolean enablePalmStripping = true;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.block.enablePalmStrippedWoods")
		    @Config.Comment("If Stripped Palm Woods are enabled. Note this also disables Palm wood stripping.")
		    public boolean enablePalmStrippedWoods = true;
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
		    public double guSpiFallMultiplier = 2.0;
			
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
		
		@Config.LangKey("oe.config.block.waterTorch")
	    public configUnderwaterTorch waterTorch = new configUnderwaterTorch();
		
		public static class configUnderwaterTorch
		{
			@RequiresMcRestart
		    @Config.LangKey("oe.config.block.enableWaterTorch")
		    @Config.Comment("If coconuts go Clonk when hitting an entity")
		    public boolean enableWaterTorch = true;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.block.waterTorchLight")
		    @Config.Comment("How much Light the Underwater Torch gives off")
		    @Config.RangeDouble(min = 0, max = 16)
		    public double waterTorchLight = 14;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.block.waterTorchParticles")
		    @Config.Comment("The particles of the Underwater Torch. (0 = None, 1 = Glow Squid Glow, 2 = Firework Sparks, 3 = Both)")
		    @Config.RangeDouble(min = 0, max = 3)
		    public double waterTorchParticles = 1;
	    }
	}
	
	@Config.LangKey("oe.config.item")
	@Config.Comment("Config related to Items, Tools, ect")
    public static configItem item = new configItem();
	
	public static class configItem
	{
		
		
		
		@RequiresMcRestart
		@Config.LangKey("oe.config.item.conch")
	    public configConch conch = new configConch();
		
		public static class configConch
		{
			@RequiresMcRestart
		    @Config.LangKey("oe.config.item.conchCooldown")
		    @Config.Comment("The default cooldown between uses of the Conch. Can be altered per-conch using the 'Cooldown' NBT tag.")
			@Config.RangeInt(min = 0, max = 9999)
		    public int conchCooldown = 60;
		
			@RequiresMcRestart
		    @Config.LangKey("oe.config.item.conchSoundDistance")
		    @Config.Comment("The attenuation distance of the Conch, how far away it's heard from.")
			@Config.RangeInt(min = 0, max = 9999)
		    public int conchSoundDistance = 256;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.item.conchDisplayHorn")
		    @Config.Comment("Adds description text to the Conch to tell which Horn sound is attached to it. Only for the default horn sounds.")
		    public boolean conchDisplayHorn = true;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.item.conchDisplayRawSound")
		    @Config.Comment("Adds the attached sound's registry name to the Conch's description. Overridden by 'conchDisplayHorn' if the sound is a horn sound.")
		    public boolean conchDisplayRawSound = true;
		}
		
		@RequiresMcRestart
		@Config.LangKey("oe.config.item.spawnBucket")
	    public configSpawnBucket spawnBucket = new configSpawnBucket();
		
		public static class configSpawnBucket
		{
			@Config.LangKey("oe.config.item.spawnBucketMobs")
		    @Config.Comment("Mobs that can be bucketed. These will also appear in Creative Tabs.")
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
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.item.enableAllBucketsCreative")
		    @Config.Comment("Makes every possible spawn bucket appear in the creative search tab. Warning, this is equal to every spawn egg, and most won't have proper textures!")
		    public boolean enableAllBucketsCreative = false;
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
		
		
		@RequiresMcRestart
		@Config.LangKey("oe.config.turtleShell")
	    public configTurtleShell turtleShell = new configTurtleShell();
		
		public static class configTurtleShell
		{
			@RequiresMcRestart
		    @Config.LangKey("oe.config.item.enableTurtleShell")
		    @Config.Comment("If Turtle Shells are enabled")
		    public boolean enableTurtleShell= true;
			
		    @RequiresMcRestart
		    @Config.LangKey("oe.config.item.turtleShellEffectLength")
		    @Config.Comment("How long (in seconds) Water Breathing is given.")
		    public int turtleShellEffectLength = 10;
		    
			@RequiresMcRestart
		    @Config.LangKey("oe.config.item.enableTurtleShellDesc")
		    @Config.Comment("Turtle Shell has a description.")
		    public boolean enableTurtleShellDesc = true;
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
		@Config.LangKey("oe.config.entity.pufferfish")
	    public configPufferfish pufferfish = new configPufferfish();
		
		public static class configPufferfish
		{
			@RequiresMcRestart
		    @Config.LangKey("oe.config.entity.enablePufferfish")
		    @Config.Comment("If Pufferfish should be enabled")
		    public boolean enablePufferfish = true;
		    
			@RequiresMcRestart
		    @Config.LangKey("oe.config.entity.enablePufferfishInflateOnHit")
		    @Config.Comment("Pufferfish instantly inflate to full when hurt")
		    public boolean enablePufferfishInflateOnHit = true;
			
		    @Config.LangKey("oe.config.item.pufferfishFriends")
		    @Config.Comment("Mobs that don't scare the Pufferfish.")
		    public String[] pufferfishFriends = 
			{
				 	"minecraft:squid",
				 	"oe:cod",
				 	"oe:salmon",
				 	"oe:pufferfish",
				 	"oe:turtle",
				 	"oe:glow_squid",
				 	"oe:clam"
		    };
		    
		    @Config.LangKey("oe.config.entity.pufferfishPoisonLength")
		    @Config.Comment("How long the Pufferfish Poison lasts.")
		    public int pufferfishPoisonLength = 6;
		    
		    @Config.LangKey("oe.config.entity.pufferfishPoisonAmplifier")
		    @Config.Comment("The amplifier of the Pufferfish Poison.")
		    public int pufferfishPoisonAmplifier = 1;
		}
		
		@RequiresMcRestart
		@Config.LangKey("oe.config.entity.salmon")
	    public configSalmon salmon = new configSalmon();
		
		public static class configSalmon
		{
			@RequiresMcRestart
		    @Config.LangKey("oe.config.entity.enableSalmon")
		    @Config.Comment("If Slamon should be enabled")
		    public boolean enableSalmon = true;
			
		    @RequiresMcRestart
		    @Config.LangKey("oe.config.entity.salmonSizeVarience")
		    @Config.Comment("If Salmon can spawn in a variety of sizes.")
		    public boolean salmonSizeVarience = true;
		    
		    @RequiresMcRestart
		    @Config.LangKey("oe.config.entity.salmonFeedingGrowth")
		    @Config.Comment("When using breeding items on Salmon, they grow in size. This is a joke feature.")
		    public boolean salmonFeedingGrowth = false;
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
			
		    @Config.LangKey("oe.config.entity.enableDrownedSwimAnims")
		    @Config.Comment("Enables unique animations for Drowned in water, inspired by MC Dungeons.")
		    public boolean enableDrownedSwimAnims = true;
			
			@RequiresMcRestart
		    @Config.LangKey("oe.config.entity.enableDrownedStepup")
		    @Config.Comment("If Drowned keep a full block step-up outside of water.")
		    public boolean enableDrownedStepup = false;
			
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
				 	"oe:clam",
				 	"oe:cod",
				 	"oe:crab",
				 	"oe:drowned",
				 	"oe:glow_squid",
				 	"oe:pickled",
				 	"oe:pufferfish",
				 	"oe:salmon",
				 	"oe:turtle"
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
	
	@Config.LangKey("oe.config.effect")
	@Config.Comment("Config related to Potion Effects")
    public static configEffects effect = new configEffects();
	
	public static class configEffects
	{
		@RequiresMcRestart
		@Config.LangKey("oe.config.effect.descent")
	    public configDescent descent = new configDescent();
		
		public static class configDescent
		{
		    @RequiresMcRestart
		    @Config.LangKey("oe.config.effect.descentFallPull")
		    @Config.Comment("How much Descent pulls the player when falling. Is multiplied per amplifier, and the current falling speed.")
		    public double descentFallPull = 0.05;
		    
		    @RequiresMcRestart
		    @Config.LangKey("oe.config.effect.descentWaterPull")
		    @Config.Comment("How much Descent pulls the player when in water. Is multiplied per amplifier, and the current sinking speed.")
		    public double descentWaterPull = 0.05;
		    
		    @RequiresMcRestart
		    @Config.LangKey("oe.config.effect.descentAqAcWaterPull")
		    @Config.Comment("How much Descent pulls the player when in water, WITH Aqua Acrobatics installed. Uses a different formula, but will use the original if set to 0.")
		    public double descentAqAcWaterPull = 0.07;
	    }
	}
	
	
	@Config.LangKey("oe.config.vanillaTweak")
	@Config.Comment("Config for any direct tweaks or changes to Vanilla content")
    public static configVanillaTweak vanillaTweak = new configVanillaTweak();
	
	public static class configVanillaTweak
	{
		@RequiresMcRestart
		@Config.LangKey("oe.config.vanillaTweak.waterBonemeal")
	    public configWaterBonemeal waterBonemeal = new configWaterBonemeal();
		
		public static class configWaterBonemeal
		{
		    @RequiresMcRestart
		    @Config.LangKey("oe.config.vanillaTweak.enableWaterBonemeal")
		    @Config.Comment("Enables Underwater Bonemealing.")
		    public boolean enableWaterBonemeal = true;
		    
		    @RequiresMcRestart
		    @Config.LangKey("oe.config.vanillaTweak.waterBonemealCoralChance")
		    @Config.Comment("The Coral chance to be placed if it passes the waterBonemealCoralGen check. Else, places Seagrass. 1 / this number.")
		    @Config.RangeInt(min = 0, max = 9999)
		    public int waterBonemealCoralChance = 5;
		    
		    @RequiresMcRestart
		    @Config.LangKey("oe.config.vanillaTweak.waterBonemealCoralGen")
		    @Config.Comment("Makes Coral generate when using underwater bonemeal near Coral Blocks. (0 = Disable, 1 = Generate only on top, 2 = Generate around any side)")
		    @Config.RangeInt(min = 0, max = 2)
		    public int waterBonemealCoralGen = 2;
	    }
		
		@Config.LangKey("oe.config.vanillaTweak.waterLighting")
	    @Config.Comment("Alters how much light Water and Flowing Water block. Default water is 3. Automatically disabled as not only do I predict some MASSIVE incompatabilities, but this will require every water block to be updated. Every single one. ")
	    public int waterTweak = 3;
		
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