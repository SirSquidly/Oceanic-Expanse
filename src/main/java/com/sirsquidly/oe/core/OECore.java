package com.sirsquidly.oe.core;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import zone.rong.mixinbooter.IEarlyMixinLoader;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

@IFMLLoadingPlugin.Name("OECore")
@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.SortingIndex(Integer.MIN_VALUE)
public class OECore implements IFMLLoadingPlugin, IEarlyMixinLoader 
{
	@Override
    public List<String> getMixinConfigs()
    { return Lists.newArrayList( "mixins.oe.json" ); }

	@Override
	public String[] getASMTransformerClass() { return new String[0]; }

	@Override
	public String getModContainerClass()
	{ return null; }

	@Override
	public String getSetupClass() { return null; }

	@Override
	public void injectData(Map<String, Object> data) { }

	@Override
	public String getAccessTransformerClass() { return null; }
}