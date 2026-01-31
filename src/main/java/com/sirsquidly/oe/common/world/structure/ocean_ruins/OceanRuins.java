package com.sirsquidly.oe.common.world.structure.ocean_ruins;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.List;

/**
 * This class is where the structure logic itself is handled and where you can do things such as generating other parts or smaller bits with your own methods
 * I'll include some of my own in here and essential stuff and usages from OceanRuinsTemplate
 */
public class OceanRuins
{
    private List<StructureComponent> components;
    private World world;
    private TemplateManager manager;

    //If you plan on having ocean ruins spawn multiple different structures, you can make this a config option
    // and then put it in an if statement for seeing if the structure can continue to generate
    private int SIZE = 1;

    public OceanRuins(World worldIn, TemplateManager template, List<StructureComponent> components) {
        this.world = worldIn;
        this.manager = template;
        this.components = components;
    }

    //the starter piece to the structure, you can change a lot of this around to what you want
    public void startBuilding(BlockPos pos, Rotation rot, String name)
    {
        OceanRuinsTemplate template = new OceanRuinsTemplate(manager, name, pos, rot, 0, true);
        components.add(template);
        System.out.println("Generated Ocean Ruins At" + pos);
        //Also where you'll call the next part in your generation if you wish to generate more structure files
      //  generateAdditionalStructureExample(template, pos, rot);
    }


    private boolean generateAdditionalStructureExample(OceanRuinsTemplate parent, BlockPos pos, Rotation rot) {

        OceanRuinsTemplate straightPiece = addAdjustedPiece(parent, pos, "random_tile", rot);

        if(straightPiece.getDistance() > SIZE || straightPiece.isCollidingExcParent(manager, parent, components)) {
            //generate something else or return false
            return false;
        }
        //this adds the piece to the whole structure
        components.add(straightPiece);
        return true;
    }

    /**
     * Adds a new piece, with the previous template a reference for position and
     * rotation
     */
    private OceanRuinsTemplate addAdjustedPiece(OceanRuinsTemplate parent, BlockPos pos, String type, Rotation rot) {
        OceanRuinsTemplate newTemplate = new OceanRuinsTemplate(manager, type, parent.getTemplatePosition(), rot, parent.getDistance() + 1, true);
        BlockPos blockpos = parent.getTemplate().calculateConnectedPos(parent.getPlacementSettings(), pos, newTemplate.getPlacementSettings(), BlockPos.ORIGIN);
        newTemplate.offset(blockpos.getX(), blockpos.getY(), blockpos.getZ());
        adjustAndCenter(parent, newTemplate, rot);
        return newTemplate;
    }


    /**
     * Centers a template to line up on the x, and in the center with z
     */
    private void adjustAndCenter(OceanRuinsTemplate parent, OceanRuinsTemplate child, Rotation rot)
    {
        BlockPos adjustedPos = new BlockPos(parent.getTemplate().getSize().getX(), 0, (parent.getTemplate().getSize().getZ() - child.getTemplate().getSize().getZ()) / 2f)
                .rotate(rot);
        child.offset(adjustedPos.getX(), adjustedPos.getY(), adjustedPos.getZ());
    }
}