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

    public static int[] rawCell(String name) {
        long h = PlanetTraits.seedOf(key(name));
        int cx = (int) Math.floorMod(h, 400L) - 200;
        int cz = (int) Math.floorMod(h >>> 20, 400L) - 200;
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

    private static int keywordTheme(String k) {
        if (k.contains("nuclear") || k.contains("radioativ") || k.contains("apocalip") || k.contains("atomic") || k.contains("devastad")) return 12;
        if (k.contains("deserto") || k.contains("desert")) return 1;
        if (k.contains("gelo") || k.contains("ice") || k.contains("frio")) return 2;
        if (k.contains("lava") || k.contains("vulcao") || k.contains("fogo")) return 3;
        if (k.contains("fungo") || k.contains("cogumelo")) return 5;
        if (k.contains("cristal")) return 7;
        if (k.contains("selva") || k.contains("jungle")) return 9;
        if (k.contains("ender") || k.contains("vazio")) return 11;
        return -1;
    }

    public static int[] cellOf(String name) {
        int want = keywordTheme(key(name));
        if (want < 0) return rawCell(name);
        for (int j = 0; j < 400; j++) {
            int[] c = rawCell(j == 0 ? name : name + "#" + j);
            long seed = PlanetTraits.seedOf("cell:" + c[0] + ":" + c[1]);
            if (PlanetTraits.themeOf(seed) == want) return c;
        }
        return rawCell(name);
    }

    public static double center(int cell) {
        return cell * (double) CELL + CELL / 2.0D;
    }

    public static String describe(String name, PlanetTraits t) {
        String water = t.ocean ? (t.liquid == Blocks.lava ? "mares de lava" : "oceanos") : "seco";
        return name.trim() + " [" + t.type + "] gravidade " + String.format(Locale.ROOT, "%.2f", t.gravity)
            + "g, " + water + (t.radiation > 0 ? " | RADIACAO " + t.radiation + " RAD/s - use traje de protecao" : "");
    }
}
