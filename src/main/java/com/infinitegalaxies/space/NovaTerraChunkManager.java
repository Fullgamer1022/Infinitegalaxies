package com.infinitegalaxies.space;

import net.minecraft.world.biome.BiomeGenBase;

import micdoodle8.mods.galacticraft.api.prefab.world.gen.WorldChunkManagerSpace;

public class NovaTerraChunkManager extends WorldChunkManagerSpace {

    @Override
    public BiomeGenBase getBiome() {
        return Celestials.NOVA_TRAITS.biome;
    }
}
