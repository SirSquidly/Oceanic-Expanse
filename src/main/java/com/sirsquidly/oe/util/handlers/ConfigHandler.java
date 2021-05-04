package com.sirsquidly.oe.util.handlers;

import com.sirsquidly.oe.util.Reference;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.RequiresMcRestart;

@Config(modid = Reference.MOD_ID)
@Config.LangKey("oe.config.title")
public class ConfigHandler 
{
	@RequiresMcRestart
    @Config.LangKey("oe.config.guSpiFallMultiplier")
    @Config.Comment("How much a Guardian Spike multiplies Fall Damage.")
    @Config.RangeDouble(min = 0, max = 9999)
    public static double guSpiFallMultiplier = 1.5;
	
	@RequiresMcRestart
    @Config.LangKey("oe.config.amountOnTrample")
    @Config.Comment("Amount of Turtle Eggs broken each time the trampleAI succeeds.")
    @Config.RangeInt(min = 0, max = 4)
    public static int amountOnTrample = 1;
	
	@RequiresMcRestart
    @Config.LangKey("oe.config.particlesOnFall")
    @Config.Comment("If egg shell particles spawn when fallen on. (0 = Never, 1 = By AI, 2 = Always)")
    @Config.RangeInt(min = 0, max = 2)
    public static int particlesOnFall = 2;
	
	@RequiresMcRestart
    @Config.LangKey("oe.config.puffOnTrample")
    @Config.Comment("If puff particles spawn when trampled. (0 = Never, 1 = By AI, 2 = Always)")
    @Config.RangeInt(min = 0, max = 2)
    public static int puffOnTrample = 2;
	
	@RequiresMcRestart
    @Config.LangKey("oe.config.zombiesTrample")
    @Config.Comment("If any mob extending Zombie (Zombie, Husks, Drowned, ect) are given the trampleAI")
    public static boolean zombiesTrample = true;
	
	@RequiresMcRestart
    @Config.LangKey("oe.config.coconutHitSound")
    @Config.Comment("If coconuts go Clonk when hitting an entity")
    public static boolean coconutHitSound = true;
	
	@RequiresMcRestart
    @Config.LangKey("oe.config.coconutFallBreak")
    @Config.Comment("How many blocks a Coconut must fall to break. (-1 = Disabled entirely)")
    @Config.RangeDouble(min = -1, max = 9999)
    public static double coconutFallBreak = 6.0;
	
	@RequiresMcRestart
    @Config.LangKey("oe.config.coconutFallDamage")
    @Config.Comment("How much damage per block added to a falling Coconut.")
    @Config.RangeDouble(min = -1, max = 9999)
    public static double coconutFallDamage = 1.0;
	
	@RequiresMcRestart
    @Config.LangKey("oe.config.coconutFallMaxDamage")
    @Config.Comment("How much max damage can a falling coconut deal")
	@Config.RangeInt(min = 0, max = 9999)
    public static int coconutFallMaxDamage = 19;
}