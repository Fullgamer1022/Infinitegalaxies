package com.infinitegalaxies.space;

import net.minecraft.world.World;

import com.infinitegalaxies.planet.PlanetChunkProvider;

public class NovaTerraChunkProvider extends PlanetChunkProvider {

    public NovaTerraChunkProvider(World world, long seed, boolean features) {
        super(world, Celestials.NOVA_TRAITS);
    }
}
