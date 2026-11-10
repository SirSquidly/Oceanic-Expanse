package com.sirsquidly.oe.asm;

import com.sirsquidly.oe.Main;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.GlobalProperties;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.TransformerExclusions({ "com.sirsquidly.oe.asm" })
public class oceanicExpansePlugin implements IFMLLoadingPlugin
{
    @Override
    public String[] getASMTransformerClass() { return null; }

    @Override
    public String getModContainerClass() { return null; }

    @Override
    public String getSetupClass() { return null; }

    @Override
    public void injectData(Map<String, Object> data)
    {
        try
        {
            if (GlobalProperties.get(GlobalProperties.Keys.INIT) == null) { MixinBootstrap.init(); }
            MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
        }
        catch (Throwable t)
        { Main.logger.fatal(t); }
    }

    @Override
    public String getAccessTransformerClass() { return null; }
}