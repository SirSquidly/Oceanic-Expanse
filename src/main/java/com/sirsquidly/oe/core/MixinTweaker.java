package com.sirsquidly.oe.core;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.fml.relauncher.CoreModManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.util.List;

public class MixinTweaker implements ITweaker {

    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.oe.json");
        CodeSource source = getClass().getProtectionDomain().getCodeSource();
        if (source != null) {
            URL location = source.getLocation();
            try {
                File file = new File(location.toURI());
                if (file.isFile()) {
                    CoreModManager.getIgnoredMods().remove(file.getName());
                }
                Logger logger = LogManager.getLogger();
                logger.warn("Oceanic Expanse Mixins Loaded!");
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else {
            Logger logger = LogManager.getLogger();
            logger.warn("No CodeSource, if this is not a development environment we might run into problems!");
            logger.warn(this.getClass().getProtectionDomain());
        }
    }

    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) { }

    @Override
    public String getLaunchTarget()
    {  return null; }

    @Override
    public String[] getLaunchArguments()
    { return new String[0]; }
}