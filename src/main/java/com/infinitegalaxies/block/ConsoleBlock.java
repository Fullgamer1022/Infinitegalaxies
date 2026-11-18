package com.infinitegalaxies.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.infinitegalaxies.InfiniteGalaxies;

public class ConsoleBlock extends Block {

    public ConsoleBlock() {
        super(Material.iron);
        setBlockName("navigation_console");
        setBlockTextureName("infinitegalaxies:console");
        setHardness(3.0F);
        setResistance(10.0F);
        setStepSound(soundTypeMetal);
        setLightLevel(0.6F);
        setCreativeTab(CreativeTabs.tabMisc);
    }

    @Override
    public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer p, int side, float hx, float hy,
        float hz) {
        if (w.isRemote) InfiniteGalaxies.proxy.openNavigation();
        return true;
    }
}
