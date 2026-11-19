package com.infinitegalaxies.planet;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

public class PlanetChunkProvider implements IChunkProvider {

    private final World world;
    protected PlanetTraits t;

    public PlanetChunkProvider(World world, PlanetTraits t) {
        this.world = world;
        this.t = t;
    }

    protected PlanetTraits traitsFor(int cx, int cz) {
        return t;
    }

    private double hash(int x, int z, int o) {
        long h = t.seed ^ (x * 341873128712L) ^ (z * 132897987541L) ^ (o * 0x9E3779B97F4A7C15L);
        h = (h ^ (h >>> 33)) * 0xff51afd7ed558ccdL;
        h ^= h >>> 33;
        h *= 0xc4ceb9fe1a85ec53L;
        h ^= h >>> 33;
        return (h & 0xFFFFFFL) / (double) 0x1000000;
    }

    private double valueNoise(double x, double z, int o) {
        int xi = (int) Math.floor(x);
        int zi = (int) Math.floor(z);
        double fx = x - xi;
        double fz = z - zi;
        fx = fx * fx * (3 - 2 * fx);
        fz = fz * fz * (3 - 2 * fz);
        double a = hash(xi, zi, o);
        double b = hash(xi + 1, zi, o);
        double c = hash(xi, zi + 1, o);
        double d = hash(xi + 1, zi + 1, o);
        double top = a + (b - a) * fx;
        double bottom = c + (d - c) * fx;
        return top + (bottom - top) * fz;
    }

    private int height(int wx, int wz) {
        double sum = 0;
        double amp = 1;
        double norm = 0;
        double freq = 1.0 / t.scale;
        for (int o = 0; o < 4; o++) {
            sum += amp * valueNoise(wx * freq, wz * freq, o);
            norm += amp;
            amp *= 0.5;
            freq *= 2;
        }
        double n = sum / norm;
        int h = (int) (t.baseHeight + (n - 0.5) * 2 * t.amplitude);
        return Math.max(2, Math.min(250, h));
    }

    @Override
    public Chunk provideChunk(int cx, int cz) {
        this.t = traitsFor(cx, cz);
        Block[] blocks = new Block[65536];
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int h = height(cx * 16 + x, cz * 16 + z);
                int base = (x * 16 + z) * 256;
                blocks[base] = Blocks.bedrock;
                for (int y = 1; y <= h; y++) {
                    Block b;
                    if (y == h) b = t.top;
                    else if (y >= h - 3) b = t.filler;
                    else b = Blocks.stone;
                    blocks[base + y] = b;
                }
                if (t.ocean) {
                    for (int y = h + 1; y <= t.seaLevel && y < 256; y++) blocks[base + y] = t.liquid;
                }
            }
        }
        Chunk chunk = new Chunk(world, blocks, new byte[65536], cx, cz);
        byte[] biomes = chunk.getBiomeArray();
        Arrays.fill(biomes, (byte) t.biome.biomeID);
        chunk.generateSkylightMap();
        return chunk;
    }

    @Override
    public Chunk loadChunk(int cx, int cz) {
        return provideChunk(cx, cz);
    }

    @Override
    public boolean chunkExists(int cx, int cz) {
        return true;
    }

    @Override
    public void populate(IChunkProvider provider, int cx, int cz) {}

    @Override
    public boolean saveChunks(boolean all, IProgressUpdate progress) {
        return true;
    }

    @Override
    public boolean unloadQueuedChunks() {
        return false;
    }

    @Override
    public boolean canSave() {
        return true;
    }

    @Override
    public String makeString() {
        return "InfiniteGalaxiesPlanet";
    }

    @Override
    public List getPossibleCreatures(EnumCreatureType type, int x, int y, int z) {
        return world.getBiomeGenForCoords(x, z)
            .getSpawnableList(type);
    }

    @Override
    public ChunkPosition func_147416_a(World w, String s, int x, int y, int z) {
        return null;
    }

    @Override
    public int getLoadedChunkCount() {
        return 0;
    }

    @Override
    public void recreateStructures(int cx, int cz) {}

    @Override
    public void saveExtraData() {}
}
