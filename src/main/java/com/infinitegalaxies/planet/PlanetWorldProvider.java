package com.infinitegalaxies.planet;

import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.IChunkProvider;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PlanetWorldProvider extends WorldProvider {

    private PlanetTraits traits() {
        PlanetTraits t = PlanetRegistry.byDim(this.dimensionId);
        return t != null ? t : PlanetRegistry.DEFAULT;
    }

    @Override
    protected void registerWorldChunkManager() {
        this.worldChunkMgr = new WorldChunkManagerHell(traits().biome, 0.0F);
    }

    @Override
    public IChunkProvider createChunkGenerator() {
        return new PlanetChunkProvider(this.worldObj, traits());
    }

    @Override
    public String getDimensionName() {
        return traits().name;
    }

    @Override
    public boolean canRespawnHere() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Vec3 getFogColor(float a, float b) {
        PlanetTraits t = traits();
        return Vec3.createVectorHelper(t.fogR, t.fogG, t.fogB);
    }
}
