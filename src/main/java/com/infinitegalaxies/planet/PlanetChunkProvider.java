package com.infinitegalaxies.planet;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
    private final PlanetTraits base;

    public PlanetChunkProvider(World world, PlanetTraits t) {
        this.world = world;
        this.base = t;
    }

    protected PlanetTraits traitsFor(int cx, int cz) {
        return base;
    }

    private static long mix(long h) {
        h = (h ^ (h >>> 33)) * 0xff51afd7ed558ccdL;
        h = (h ^ (h >>> 33)) * 0xc4ceb9fe1a85ec53L;
        return h ^ (h >>> 33);
    }

    private static double h2(long seed, int x, int z, int o) {
        long h = mix(seed ^ (x * 341873128712L) ^ (z * 132897987541L) ^ (o * 0x9E3779B97F4A7C15L));
        return (h & 0xFFFFFFL) / 16777216.0;
    }

    private static double h3(long seed, int x, int y, int z, int o) {
        long h = mix(
            seed ^ (x * 341873128712L) ^ (z * 132897987541L) ^ (y * 0x632BE59BD9B4E019L)
                ^ (o * 0x9E3779B97F4A7C15L));
        return (h & 0xFFFFFFL) / 16777216.0;
    }

    private static double n2(long seed, double x, double z, int o) {
        int xi = (int) Math.floor(x);
        int zi = (int) Math.floor(z);
        double fx = x - xi;
        double fz = z - zi;
        fx = fx * fx * (3 - 2 * fx);
        fz = fz * fz * (3 - 2 * fz);
        double a = h2(seed, xi, zi, o);
        double b = h2(seed, xi + 1, zi, o);
        double c = h2(seed, xi, zi + 1, o);
        double d = h2(seed, xi + 1, zi + 1, o);
        double t1 = a + (b - a) * fx;
        double t2 = c + (d - c) * fx;
        return t1 + (t2 - t1) * fz;
    }

    private static double n3(long seed, double x, double y, double z, int o) {
        int xi = (int) Math.floor(x);
        int yi = (int) Math.floor(y);
        int zi = (int) Math.floor(z);
        double fx = x - xi;
        double fy = y - yi;
        double fz = z - zi;
        fx = fx * fx * (3 - 2 * fx);
        fy = fy * fy * (3 - 2 * fy);
        fz = fz * fz * (3 - 2 * fz);
        double a = h3(seed, xi, yi, zi, o);
        double b = h3(seed, xi + 1, yi, zi, o);
        double c = h3(seed, xi, yi, zi + 1, o);
        double d = h3(seed, xi + 1, yi, zi + 1, o);
        double e = h3(seed, xi, yi + 1, zi, o);
        double f = h3(seed, xi + 1, yi + 1, zi, o);
        double g = h3(seed, xi, yi + 1, zi + 1, o);
        double h = h3(seed, xi + 1, yi + 1, zi + 1, o);
        double l1 = (a + (b - a) * fx);
        double l2 = (c + (d - c) * fx);
        double l3 = (e + (f - e) * fx);
        double l4 = (g + (h - g) * fx);
        double m1 = l1 + (l2 - l1) * fz;
        double m2 = l3 + (l4 - l3) * fz;
        return m1 + (m2 - m1) * fy;
    }

    private static double fbm(long seed, double x, double z, int oct, double freq, int off) {
        double sum = 0;
        double amp = 1;
        double norm = 0;
        for (int i = 0; i < oct; i++) {
            sum += amp * n2(seed, x * freq, z * freq, off + i);
            norm += amp;
            amp *= 0.5;
            freq *= 2;
        }
        return sum / norm;
    }

    private static boolean cave(long seed, int x, int y, int z) {
        double a = n3(seed, x * 0.045, y * 0.07, z * 0.045, 91);
        if (Math.abs(a - 0.5) > 0.05) return false;
        double b = n3(seed, x * 0.045, y * 0.07, z * 0.045, 92);
        return Math.abs(b - 0.5) <= 0.05;
    }

    private static int height(PlanetTraits T, int wx, int wz) {
        double f = 1.0 / T.scale;
        double n = fbm(T.seed, wx, wz, 4, f, 0);
        double h;
        switch (T.shape) {
            case 1: {
                double rr = 1 - Math.abs(2 * fbm(T.seed, wx, wz, 3, f * 0.8, 20) - 1);
                h = T.baseHeight + (n - 0.5) * T.amplitude + rr * rr * T.amplitude * 2.2;
                break;
            }
            case 2: {
                double raw = T.baseHeight + (n - 0.5) * 2 * T.amplitude * 1.4;
                h = Math.round(raw / 7.0) * 7.0;
                break;
            }
            case 3: {
                double c = Math.abs(fbm(T.seed, wx, wz, 3, f * 1.2, 30) - 0.5);
                h = T.baseHeight + 14 + (n - 0.5) * T.amplitude * 0.5;
                double depth = (0.07 - c) / 0.07;
                if (depth > 0) h -= depth * 55;
                break;
            }
            case 4: {
                h = T.baseHeight + (n - 0.5) * T.amplitude * 0.6;
                double sp = fbm(T.seed, wx, wz, 2, f * 3.0, 40);
                if (sp > 0.62) h += (sp - 0.62) * 380;
                break;
            }
            case 5:
                h = T.baseHeight - 10 + (n - 0.4) * T.amplitude * 2.2;
                break;
            case 6:
                h = T.baseHeight + (n - 0.5) * T.amplitude * 0.5;
                break;
            default:
                h = T.baseHeight + (n - 0.5) * 2 * T.amplitude;
                break;
        }
        return Math.max(4, Math.min(230, (int) h));
    }

    private void islands(PlanetTraits T, int cx, int cz, Block[] blocks, byte[] metas) {
        int cell = 48;
        int x0 = cx * 16;
        int z0 = cz * 16;
        int gx0 = Math.floorDiv(x0 - 24, cell);
        int gx1 = Math.floorDiv(x0 + 40, cell);
        int gz0 = Math.floorDiv(z0 - 24, cell);
        int gz1 = Math.floorDiv(z0 + 40, cell);
        for (int gx = gx0; gx <= gx1; gx++) {
            for (int gz = gz0; gz <= gz1; gz++) {
                if (h2(T.seed, gx, gz, 77) < 0.35) continue;
                double ix = gx * cell + h2(T.seed, gx, gz, 78) * cell;
                double iz = gz * cell + h2(T.seed, gx, gz, 79) * cell;
                int iy = 100 + (int) (h2(T.seed, gx, gz, 80) * 80);
                double rx = 7 + h2(T.seed, gx, gz, 81) * 11;
                double rz = 7 + h2(T.seed, gx, gz, 82) * 11;
                int depth = 5 + (int) (h2(T.seed, gx, gz, 83) * 10);
                for (int lx = 0; lx < 16; lx++) {
                    for (int lz = 0; lz < 16; lz++) {
                        double dx = (x0 + lx - ix) / rx;
                        double dz = (z0 + lz - iz) / rz;
                        double d2 = dx * dx + dz * dz;
                        if (d2 >= 1) continue;
                        double fq = Math.sqrt(1 - d2);
                        int topY = iy + (int) (fq * 3);
                        int botY = iy - (int) (fq * depth);
                        int idx = (lx * 16 + lz) * 256;
                        for (int y = botY; y <= topY && y < 255; y++) {
                            if (y <= 0) continue;
                            if (y == topY) {
                                blocks[idx + y] = T.top;
                                metas[idx + y] = (byte) T.topMeta;
                            } else if (y >= topY - 3) {
                                blocks[idx + y] = T.filler;
                                metas[idx + y] = (byte) T.fillerMeta;
                            } else {
                                blocks[idx + y] = T.stone;
                                metas[idx + y] = 0;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public Chunk provideChunk(int cx, int cz) {
        PlanetTraits T = traitsFor(cx, cz);
        Block[] blocks = new Block[65536];
        byte[] metas = new byte[65536];
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int wx = cx * 16 + x;
                int wz = cz * 16 + z;
                int h = height(T, wx, wz);
                int idx = (x * 16 + z) * 256;
                blocks[idx] = Blocks.bedrock;
                for (int y = 1; y <= h; y++) {
                    if (y == h) {
                        blocks[idx + y] = T.top;
                        metas[idx + y] = (byte) T.topMeta;
                    } else if (y >= h - 3) {
                        blocks[idx + y] = T.filler;
                        metas[idx + y] = (byte) T.fillerMeta;
                    } else {
                        blocks[idx + y] = T.stone;
                    }
                }
                if (T.caves) {
                    for (int y = 5; y < h - 4 && y < 200; y++) {
                        if (cave(T.seed, wx, y, wz)) {
                            blocks[idx + y] = y <= 10 ? Blocks.lava : null;
                            metas[idx + y] = 0;
                        }
                    }
                }
                if (T.ocean) {
                    for (int y = h + 1; y <= T.seaLevel && y < 255; y++) blocks[idx + y] = T.liquid;
                }
            }
        }
        if (T.shape == 6) islands(T, cx, cz, blocks, metas);
        Chunk chunk = new Chunk(world, blocks, metas, cx, cz);
        byte[] biomes = chunk.getBiomeArray();
        Arrays.fill(biomes, (byte) T.biome.biomeID);
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
    public void populate(IChunkProvider provider, int cx, int cz) {
        PlanetTraits T = traitsFor(cx, cz);
        Random rand = new Random(T.seed * 31L + cx * 341873128712L + cz * 132897987541L);
        int bx = cx * 16;
        int bz = cz * 16;
        PlanetFeatures.ores(world, rand, T, bx, bz);
        if (T.caves && T.dungeons) PlanetFeatures.dungeons(world, rand, bx, bz);
        PlanetFeatures.vegetation(world, rand, T, bx, bz);
        PlanetFeatures.structures(world, rand, T, bx, bz);
    }

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
