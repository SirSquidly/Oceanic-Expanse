package com.sirsquidly.oe.util.handlers;

import java.util.List;

import net.minecraft.util.ResourceLocation;

import com.google.common.collect.Lists;
import com.sirsquidly.oe.Main;

/**
 * 	This is to break part arrays in the config for use in other areas of the code.
 * 
 *  I break it up in this class so that I don't have to break the config arrays every time I want to use them.
 */
public class ConfigArrayHandler
{
	/** Entities that drown to become a different entity. */
	public static List<ResourceLocation> DROWNCONVERTFROM = Lists.<ResourceLocation>newArrayList();
	/** Entities created by the former (DROWNCONVERTFROM) drowning. */
	public static List<ResourceLocation> DROWNCONVERTTO = Lists.<ResourceLocation>newArrayList();
	
	public static void breakupConfigArrays()
	{
		for(String S : ConfigHandler.vanillaTweak.drownConverting.drownConversionsList)
		{
			String[] split = S.split("=");
			
			if (DROWNCONVERTFROM.contains(new ResourceLocation(split[0])))
			{
				Main.logger.error(split[0] + " has multiple drowned conversions set in the config! Only the first listed will be used!");
			}
			else if (split.length != 2)
			{
				if (split.length == 1)
					Main.logger.error(split[0] + " is missing a drowned conversion! Skipping...");
				else
					Main.logger.error(S + " is improperly written!");
			}
			else
			{
				DROWNCONVERTFROM.add(new ResourceLocation(split[0]));
				DROWNCONVERTTO.add(new ResourceLocation(split[1]));	
			}
		}
	}
}