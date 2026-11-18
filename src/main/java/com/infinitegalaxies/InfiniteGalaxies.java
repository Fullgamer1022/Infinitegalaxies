package com.infinitegalaxies;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
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
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        LOG.info("Infinite Galaxies carregado junto com o Galacticraft!");
    }
}
