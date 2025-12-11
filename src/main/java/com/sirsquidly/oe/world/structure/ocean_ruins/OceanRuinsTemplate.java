package com.sirsquidly.oe.world.structure.ocean_ruins;

import com.sirsquidly.oe.world.structure.base.ModStructureTemplate;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.Random;

public class OceanRuinsTemplate extends ModStructureTemplate {

    public OceanRuinsTemplate(TemplateManager manager, String type, BlockPos pos, Rotation rot, int distance, boolean overWriteIn) {
        super(manager, type, pos,distance, rot, overWriteIn);
    }

    public OceanRuinsTemplate() {

    }



    // This handles data blocks that you put down. You can do some pretty awesome stuff with this such as generating smaller structures or
    // decoration pieces inside of a structure. Handle loot tables and whatnot. This is the bread and butter of adding to the structure
    //
    @Override
    protected void handleDataMarker(String function, BlockPos pos, World world, Random rand, StructureBoundingBox sbb) {
        //adds the boss to the structure


        //just an example
        //if(function.startsWith("boss")) {
         //   EntityNamelessChampion champion = new EntityNamelessChampion(world);
          //  champion.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
          //  world.spawnEntity(champion);
          //  world.setBlockToAir(pos);
      //  }
    }

    //The folder name in assets/oe/structures/
    @Override
    public String templateLocation() {
        return "ocean_ruins";
    }
}
