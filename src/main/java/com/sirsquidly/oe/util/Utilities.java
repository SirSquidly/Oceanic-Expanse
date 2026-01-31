package com.sirsquidly.oe.util;

import com.sirsquidly.oe.common.items.ItemSpawnBucket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.ItemStack;

/** Rename and reorganize later. */
public class Utilities
{

    public static boolean spawnBucketContainsAnyEntity(ItemStack stack, @SuppressWarnings("rawtypes") Class<?>... entityClasses)
    {
        if (!(stack.getItem() instanceof ItemSpawnBucket)) return false;

        ItemSpawnBucket bucket = (ItemSpawnBucket)stack.getItem();
        Class<? extends Entity> containedClass = EntityList.getClass(bucket.getNamedIdFrom(stack));

        for (Class<?> entityClass : entityClasses)
        {
            if (entityClass.isAssignableFrom(containedClass)) return true;
        }
        return false;
    }
}