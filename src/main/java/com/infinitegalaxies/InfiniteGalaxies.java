package com.infinitegalaxies;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.infinitegalaxies.planet.CommandPlanet;
import com.infinitegalaxies.planet.GravityHandler;
import com.infinitegalaxies.planet.PlanetRegistry;
import com.infinitegalaxies.planet.PlanetWorldProvider;
import com.infinitegalaxies.space.Celestials;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(
    modid = InfiniteGalaxies.MODID,
    name = "Infinite Galaxies",
    version = "0.0.1",
    acceptedMinecraftVersions = "[1.7.10]",
    dependencies = "required-after:GalacticraftCore")
public class InfiniteGalaxies {

    public static final String MODID = "infinitegalaxies";
    public static final Logger LOG = LogManager.getLogger(MODID);

    public static Item testItem;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        testItem = new Item().setUnlocalizedName("test_item")
            .setTextureName(MODID + ":test_item")
            .setCreativeTab(CreativeTabs.tabMisc);
        GameRegistry.registerItem(testItem, "test_item");
        DimensionManager.registerProviderType(PlanetRegistry.PROVIDER_ID, PlanetWorldProvider.class, false);
        MinecraftForge.EVENT_BUS.register(new GravityHandler());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Celestials.register();
        LOG.info("Infinite Galaxies carregado junto com o Galacticraft!");
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        PlanetRegistry.loadAll();
        event.registerServerCommand(new CommandPlanet());
    }

    @Mod.EventHandler
    public void serverStopped(FMLServerStoppedEvent event) {
        PlanetRegistry.clear();
    }
}
