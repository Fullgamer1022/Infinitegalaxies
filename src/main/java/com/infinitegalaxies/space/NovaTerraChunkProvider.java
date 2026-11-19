package com.infinitegalaxies.space;

import net.minecraft.world.World;

import com.infinitegalaxies.planet.PlanetChunkProvider;
import com.infinitegalaxies.planet.PlanetTraits;

public class NovaTerraChunkProvider extends PlanetChunkProvider {

    public NovaTerraChunkProvider(World world, long seed, boolean features) {
        super(world, Celestials.NOVA_TRAITS);
    }

    @Override
    protected PlanetTraits traitsFor(int cx, int cz) {
        return Regions.traitsOfChunk(cx, cz);
    }
}
