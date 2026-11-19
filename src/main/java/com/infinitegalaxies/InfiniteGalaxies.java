package com.infinitegalaxies;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.infinitegalaxies.block.ConsoleBlock;
import com.infinitegalaxies.net.PacketTravel;
import com.infinitegalaxies.planet.GravityHandler;
import com.infinitegalaxies.space.Celestials;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(
    modid = InfiniteGalaxies.MODID,
    name = "Infinite Galaxies",
    version = "0.0.1",
    acceptedMinecraftVersions = "[1.7.10]",
    dependencies = "required-after:GalacticraftCore")
public class InfiniteGalaxies {

    public static final String MODID = "infinitegalaxies";
    public static final Logger LOG = LogManager.getLogger(MODID);

    @SidedProxy(
        clientSide = "com.infinitegalaxies.ClientProxy",
        serverSide = "com.infinitegalaxies.CommonProxy")
    public static CommonProxy proxy;

    public static SimpleNetworkWrapper NET;
    public static Item testItem;
    public static Block consoleBlock;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        testItem = new Item().setUnlocalizedName("test_item")
            .setTextureName(MODID + ":test_item")
            .setCreativeTab(CreativeTabs.tabMisc);
        GameRegistry.registerItem(testItem, "test_item");

        consoleBlock = new ConsoleBlock();
        GameRegistry.registerBlock(consoleBlock, "navigation_console");
        GameRegistry.addRecipe(
            new ItemStack(consoleBlock),
            "OOO",
            "OEO",
            "OOO",
            'O',
            Blocks.obsidian,
            'E',
            Items.ender_eye);

        NET = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
        NET.registerMessage(PacketTravel.Handler.class, PacketTravel.class, 0, Side.SERVER);

        MinecraftForge.EVENT_BUS.register(new GravityHandler());
        cpw.mods.fml.common.FMLCommonHandler.instance().bus().register(new com.infinitegalaxies.space.Rescue());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Celestials.register();
        LOG.info("Infinite Galaxies carregado junto com o Galacticraft!");
    }
}
