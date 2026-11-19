package com.infinitegalaxies.space;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.init.Blocks;

import com.infinitegalaxies.planet.PlanetTraits;

public final class Regions {

    public static final int CELL = 65536;
    private static final Map<Long, PlanetTraits> CACHE = new ConcurrentHashMap<Long, PlanetTraits>();

    private Regions() {}

    public static String key(String name) {
        return name.trim()
            .toLowerCase(Locale.ROOT);
    }

    public static int[] cellOf(String name) {
        long h = PlanetTraits.seedOf(key(name));
        int cx = (int) Math.floorMod(h, 1000L) - 500;
        int cz = (int) Math.floorMod(h >>> 20, 1000L) - 500;
        return new int[] { cx, cz };
    }

    public static PlanetTraits traitsOfCell(int cx, int cz) {
        long k = (((long) cx) << 32) ^ (cz & 0xffffffffL);
        PlanetTraits t = CACHE.get(k);
        if (t == null) {
            t = new PlanetTraits(
                "Mundo " + cx + "," + cz,
                PlanetTraits.seedOf("cell:" + cx + ":" + cz),
                Celestials.DIM_ID);
            CACHE.put(k, t);
        }
        return t;
    }

    public static PlanetTraits traitsOfChunk(int chunkX, int chunkZ) {
        return traitsOfCell(Math.floorDiv(chunkX * 16, CELL), Math.floorDiv(chunkZ * 16, CELL));
    }

    public static PlanetTraits traitsAt(double x, double z) {
        return traitsOfCell(Math.floorDiv((int) Math.floor(x), CELL), Math.floorDiv((int) Math.floor(z), CELL));
    }

    public static double center(int cell) {
        return cell * (double) CELL + CELL / 2.0D;
    }

    public static String describe(String name, PlanetTraits t) {
        String water = t.ocean ? (t.liquid == Blocks.lava ? "mares de lava" : "oceanos") : "seco";
        return name.trim() + " [" + t.type + "] gravidade " + String.format(Locale.ROOT, "%.2f", t.gravity)
            + "g, " + water;
    }
}
