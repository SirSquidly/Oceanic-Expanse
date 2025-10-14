package com.sirsquidly.oe.items;

import com.google.common.collect.Maps;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import com.sirsquidly.oe.util.handlers.ConfigHandler;

import java.util.Collection;
import java.util.LinkedHashMap;

public class ItemNautilusArmor extends Item {

    private final Material material;

    public ItemNautilusArmor(Material material) {
        this.material = material;
    }

    public String getName() {
        return material.getName();
    }

    public int getProtection() {
        return material.getProtection();
    }

    public ResourceLocation getTexture() {
        return material.getTexture();
    }

    public Material getMaterial() {
        return material;
    }

    public static class Material {

        private static final LinkedHashMap<String, Material> MATERIALS = Maps.newLinkedHashMap();

        public static void readFromConfig() {
            for (String string : ConfigHandler.item.nautilusArmor.nautilusArmourMaterials) {
                String[] split = string.split("-");
                int protection = 0;
                if (split.length > 1) try {
                    protection = Integer.getInteger(split[1]);
                } catch (Exception e) {}
                register(split[0], protection);
            }
        }

        public static Material register(String name, int protection) {
            Material material = new Material(name, protection);
            MATERIALS.put(name, material);
            return material;
        }

        public static Material get(String name) {
            return MATERIALS.get(name);
        }

        public static Collection<Material> getAll() {
            return MATERIALS.values();
        }

        private final String name;
        private final int protection;
        private final ResourceLocation texture;

        private Material(String name, int protection) {
            this.name = name;
            this.protection = protection;
            texture = new ResourceLocation("oe", "textures/entities/nautilus/armor/" + name + ".png");
        }

        public String getName() {
            return name;
        }

        public int getProtection() {
            return protection;
        }

        public ResourceLocation getTexture() {
            return texture;
        }

    }
}
