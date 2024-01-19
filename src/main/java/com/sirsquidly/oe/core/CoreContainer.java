package com.sirsquidly.oe.core;

import com.google.common.eventbus.EventBus;
import com.sirsquidly.oe.Main;

import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;

public class CoreContainer extends DummyModContainer
{
    public CoreContainer()
    {
        super(new ModMetadata());
        ModMetadata meta = this.getMetadata();
        meta.modId = "oe";
        meta.name = "Oceanic Expanse";
        meta.description = "Loads Mixin things for Oceanic Expanse";
        meta.version = Main.VERSION;
        meta.authorList.add("Sir Squidly");
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public boolean registerBus(EventBus bus, LoadController controller)
    {
        bus.register(this);
        return true;
    }
}