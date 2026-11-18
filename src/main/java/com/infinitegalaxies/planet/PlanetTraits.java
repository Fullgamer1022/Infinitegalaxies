package com.infinitegalaxies.planet;

import java.util.Locale;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;

public final class PlanetTraits {

    public final String name;
    public final long seed;
    public final int dimId;
    public final String type;
    public final Block top;
    public final Block filler;
    public final Block liquid;
    public final BiomeGenBase biome;
    public final int baseHeight;
    public final int seaLevel;
    public final double amplitude;
    public final double scale;
    public final boolean ocean;
    public final float gravity;
    public final float fogR;
    public final float fogG;
    public final float fogB;

    public static long seedOf(String key) {
        long h = 0xcbf29ce484222325L;
        for (int i = 0; i < key.length(); i++) {
            h ^= key.charAt(i);
            h *= 0x100000001b3L;
        }
        return h;
    }

    public PlanetTraits(String name, long seed, int dimId) {
        this.name = name;
        this.seed = seed;
        this.dimId = dimId;
        Random r = new Random(seed);
        int theme = r.nextInt(7);
        Block t;
        Block f;
        Block l = Blocks.water;
        BiomeGenBase b;
        String ty;
        float fr;
        float fg;
        float fb;
        boolean oc = r.nextInt(100) < 55;
        switch (theme) {
            case 0:
                ty = "Temperado";
                t = Blocks.grass;
                f = Blocks.dirt;
                b = BiomeGenBase.plains;
                fr = 0.75F;
                fg = 0.85F;
                fb = 1.0F;
                break;
            case 1:
                ty = "Deserto";
                t = Blocks.sand;
                f = Blocks.sandstone;
                b = BiomeGenBase.desert;
                fr = 0.95F;
                fg = 0.8F;
                fb = 0.55F;
                oc = false;
                break;
            case 2:
                ty = "Gelado";
                t = Blocks.snow;
                f = Blocks.packed_ice;
                b = BiomeGenBase.icePlains;
                fr = 0.8F;
                fg = 0.9F;
                fb = 1.0F;
                break;
            case 3:
                ty = "Vulcanico";
                t = Blocks.gravel;
                f = Blocks.stone;
                l = Blocks.lava;
                b = BiomeGenBase.hell;
                fr = 0.5F;
                fg = 0.15F;
                fb = 0.1F;
                oc = true;
                break;
            case 4:
                ty = "Argiloso";
                t = Blocks.hardened_clay;
                f = Blocks.clay;
                b = BiomeGenBase.mesa;
                fr = 0.85F;
                fg = 0.55F;
                fb = 0.4F;
                break;
            case 5:
                ty = "Fungico";
                t = Blocks.mycelium;
                f = Blocks.dirt;
                b = BiomeGenBase.mushroomIsland;
                fr = 0.6F;
                fg = 0.4F;
                fb = 0.7F;
                break;
            default:
                ty = "Rochoso";
                t = Blocks.stone;
                f = Blocks.stone;
                b = BiomeGenBase.extremeHills;
                fr = 0.6F;
                fg = 0.6F;
                fb = 0.65F;
                break;
        }
        this.type = ty;
        this.top = t;
        this.filler = f;
        this.liquid = l;
        this.biome = b;
        this.ocean = oc;
        this.gravity = 0.3F + r.nextFloat() * 1.2F;
        this.baseHeight = 56 + r.nextInt(30);
        this.amplitude = 6 + r.nextInt(30);
        this.scale = 40 + r.nextInt(100);
        this.seaLevel = this.baseHeight + r.nextInt(5) - 2;
        this.fogR = clamp(fr + (r.nextFloat() - 0.5F) * 0.16F);
        this.fogG = clamp(fg + (r.nextFloat() - 0.5F) * 0.16F);
        this.fogB = clamp(fb + (r.nextFloat() - 0.5F) * 0.16F);
    }

    private static float clamp(float v) {
        return v < 0F ? 0F : (v > 1F ? 1F : v);
    }

    public String describe() {
        return "Planeta " + name + " [" + type + "] gravidade " + String.format(Locale.ROOT, "%.2f", gravity)
            + "g, " + (ocean ? (liquid == Blocks.lava ? "mares de lava" : "oceanos") : "seco");
    }
}
