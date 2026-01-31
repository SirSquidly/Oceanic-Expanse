package com.sirsquidly.oe.init;

import com.sirsquidly.oe.common.world.structure.ocean_ruins.OceanRuinsTemplate;
import com.sirsquidly.oe.common.world.structure.ocean_ruins.WorldGenOceanRuins;
import net.minecraft.world.gen.structure.MapGenStructureIO;

//handles registration
public class OEStructures {

    public static void handleStructureRegistries()
    {
        //Ocean Ruins
        MapGenStructureIO.registerStructure(WorldGenOceanRuins.Start.class, "OceanRuins");
        MapGenStructureIO.registerStructureComponent(OceanRuinsTemplate.class, "ORP");
    }
}
