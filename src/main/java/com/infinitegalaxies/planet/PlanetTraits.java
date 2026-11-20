package com.infinitegalaxies.planet;

import java.util.Locale;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;

public final class PlanetTraits {

    public static final String[] SHAPES = { "Colinas", "Montanhas", "Terracos", "Canions", "Agulhas",
        "Arquipelago", "Ilhas Flutuantes" };

    public final String name;
    public final long seed;
    public final int dimId;
    public final String type;
    public final Block top;
    public final int topMeta;
    public final Block filler;
    public final int fillerMeta;
    public final Block stone;
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
    public final int shape;
    public final boolean caves;
    public final boolean dungeons;
    public final int treeStyle;
    public final int treeStyle2;
    public final int treeDensity;
    public final Block logBlock;
    public final int logMeta;
    public final Block leafBlock;
    public final int leafMeta;
    public final int groundCover;
    public final int structA;
    public final int structB;
    public final int structEvery;
    public final int oreRichness;
    public final Block exoticOre;
    public final int radiation;

    public static long seedOf(String key) {
        long h = 0xcbf29ce484222325L;
        for (int i = 0; i < key.length(); i++) {
            h ^= key.charAt(i);
            h *= 0x100000001b3L;
        }
        return h;
    }

    private static float clamp(float v) {
        return v < 0F ? 0F : (v > 1F ? 1F : v);
    }

    public PlanetTraits(String name, long seed, int dimId) {
        this.name = name;
        this.seed = seed;
        this.dimId = dimId;
        Random r = new Random(seed);
        r.nextInt();
        r.nextInt();
        int theme = themeOf(seed);

        Block t = Blocks.grass;
        int tm = 0;
        Block f = Blocks.dirt;
        int fm = 0;
        Block s = Blocks.stone;
        Block l = Blocks.water;
        BiomeGenBase b = BiomeGenBase.plains;
        String ty = "Temperado";
        float fr = 0.7F;
        float fg = 0.85F;
        float fb = 1.0F;
        boolean oc = r.nextInt(100) < 55;
        int w = r.nextInt(6);
        Block lg = w < 4 ? Blocks.log : Blocks.log2;
        int lgm = w < 4 ? w : w - 4;
        Block lf = w < 4 ? Blocks.leaves : Blocks.leaves2;
        int lfm = lgm;
        int ts = r.nextInt(4);
        int ts2 = ts;
        int dens = 3 + r.nextInt(7);
        int cover = 0;
        int forced = 0;
        int shapeBias = -1;

        switch (theme) {
            case 1:
                ty = "Deserto";
                t = Blocks.sand;
                tm = r.nextInt(2);
                f = Blocks.sandstone;
                if (r.nextBoolean()) s = Blocks.sandstone;
                b = BiomeGenBase.desert;
                fr = 0.95F;
                fg = 0.8F;
                fb = 0.55F;
                oc = false;
                ts = r.nextBoolean() ? 4 : 2;
                ts2 = 7;
                lg = Blocks.log2;
                lgm = 0;
                lf = Blocks.leaves2;
                lfm = 0;
                dens = 2 + r.nextInt(5);
                cover = 2;
                forced = 5;
                break;
            case 2:
                ty = "Gelado";
                t = Blocks.snow;
                f = r.nextBoolean() ? Blocks.packed_ice : Blocks.dirt;
                if (r.nextInt(3) == 0) s = Blocks.packed_ice;
                b = BiomeGenBase.icePlains;
                fr = 0.8F;
                fg = 0.9F;
                fb = 1.0F;
                ts = 1;
                ts2 = 1;
                lg = Blocks.log;
                lgm = 1;
                lf = Blocks.leaves;
                lfm = 1;
                forced = 6;
                break;
            case 3:
                ty = "Vulcanico";
                t = Blocks.netherrack;
                f = Blocks.netherrack;
                l = Blocks.lava;
                b = BiomeGenBase.hell;
                oc = true;
                fr = 0.5F;
                fg = 0.15F;
                fb = 0.1F;
                ts = 4;
                ts2 = 4;
                lg = Blocks.log2;
                lgm = 1;
                dens = 1 + r.nextInt(3);
                forced = 4;
                break;
            case 4:
                ty = "Argiloso";
                t = Blocks.stained_hardened_clay;
                tm = r.nextInt(16);
                f = Blocks.stained_hardened_clay;
                fm = r.nextInt(16);
                s = Blocks.hardened_clay;
                b = BiomeGenBase.mesa;
                fr = 0.85F;
                fg = 0.55F;
                fb = 0.4F;
                ts = 2;
                ts2 = 4;
                lg = Blocks.log2;
                lgm = 0;
                lf = Blocks.leaves2;
                lfm = 0;
                cover = 2;
                forced = 3;
                shapeBias = 2;
                dens = 1 + r.nextInt(4);
                break;
            case 5:
                ty = "Fungico";
                t = Blocks.mycelium;
                b = BiomeGenBase.mushroomIsland;
                fr = 0.6F;
                fg = 0.4F;
                fb = 0.7F;
                ts = 6;
                ts2 = 6;
                dens = 4 + r.nextInt(6);
                cover = 3;
                forced = 8;
                break;
            case 6:
                ty = "Rochoso";
                t = r.nextBoolean() ? Blocks.stone : Blocks.gravel;
                f = Blocks.stone;
                b = BiomeGenBase.extremeHills;
                fr = 0.6F;
                fg = 0.6F;
                fb = 0.65F;
                ts = 4;
                ts2 = 1;
                dens = 1 + r.nextInt(4);
                forced = 1;
                shapeBias = 1;
                break;
            case 7:
                ty = "Cristalino";
                t = Blocks.quartz_block;
                f = Blocks.quartz_block;
                fr = 0.85F;
                fg = 0.9F;
                fb = 1.0F;
                ts = 5;
                ts2 = 5;
                lg = Blocks.quartz_block;
                lgm = 0;
                lf = Blocks.glass;
                lfm = 0;
                dens = 3 + r.nextInt(6);
                forced = 6;
                break;
            case 8:
                ty = "Sombrio";
                t = Blocks.soul_sand;
                f = Blocks.gravel;
                b = BiomeGenBase.swampland;
                fr = 0.25F;
                fg = 0.15F;
                fb = 0.35F;
                ts = 4;
                ts2 = 4;
                lg = Blocks.log2;
                lgm = 1;
                dens = 3 + r.nextInt(6);
                forced = 2;
                if (r.nextInt(4) == 0) l = Blocks.lava;
                break;
            case 9:
                ty = "Selva";
                b = BiomeGenBase.jungle;
                fr = 0.55F;
                fg = 0.85F;
                fb = 0.7F;
                lg = Blocks.log;
                lgm = 3;
                lf = Blocks.leaves;
                lfm = 3;
                ts = 3;
                ts2 = 0;
                dens = 7 + r.nextInt(7);
                cover = 1;
                forced = 1;
                break;
            case 10:
                ty = "Pantano";
                b = BiomeGenBase.swampland;
                fr = 0.45F;
                fg = 0.55F;
                fb = 0.4F;
                oc = true;
                lg = Blocks.log;
                lgm = 0;
                lf = Blocks.leaves;
                lfm = 0;
                ts = 2;
                ts2 = 0;
                dens = 4 + r.nextInt(5);
                cover = 1;
                forced = 7;
                break;
            case 11:
                ty = "Etereo";
                t = Blocks.end_stone;
                f = Blocks.end_stone;
                s = Blocks.end_stone;
                b = BiomeGenBase.sky;
                fr = 0.5F;
                fg = 0.35F;
                fb = 0.6F;
                oc = false;
                ts = 5;
                ts2 = 5;
                lg = Blocks.obsidian;
                lgm = 0;
                lf = Blocks.glowstone;
                lfm = 0;
                dens = 2 + r.nextInt(5);
                forced = 2;
                break;
            default:
                ty = "Temperado";
                b = r.nextBoolean() ? BiomeGenBase.plains : BiomeGenBase.forest;
                cover = 1;
                break;
        }

        int rad = 0;
        if (theme == 12) {
            ty = "Devastado";
            t = ModLookup.block("hbm", "tile.waste_earth", Blocks.gravel);
            tm = 0;
            f = ModLookup.block("hbm", "tile.dirt_dead", Blocks.dirt);
            fm = 0;
            s = Blocks.stone;
            l = Blocks.water;
            b = BiomeGenBase.desert;
            oc = false;
            fr = 0.42F;
            fg = 0.38F;
            fb = 0.32F;
            ts = 4;
            ts2 = 4;
            Block wl = ModLookup.block("hbm", "tile.waste_log", null);
            lg = wl != null ? wl : Blocks.log2;
            lgm = wl != null ? 0 : 1;
            cover = 0;
            dens = 6 + r.nextInt(8);
            forced = 4;
            rad = 3 + r.nextInt(30);
        }

        int sh = r.nextInt(7);
        if (shapeBias >= 0 && r.nextBoolean()) sh = shapeBias;
        double amp = 6 + r.nextInt(26);
        if (sh == 1) amp = 28 + r.nextInt(30);
        if (sh == 3) amp = 18 + r.nextInt(20);
        if (sh == 5) oc = true;
        int base = 58 + r.nextInt(25);
        int sea = base + r.nextInt(5) - 2;
        if (sh == 5) sea = base;

        int sa = forced > 0 ? forced : 1 + r.nextInt(8);
        int sb = 1 + r.nextInt(8);
        while (sb == sa) sb = 1 + r.nextInt(8);

        Block[] ex = { Blocks.emerald_ore, Blocks.quartz_ore, Blocks.lapis_ore, Blocks.glowstone,
            Blocks.diamond_ore };

        this.type = ty + " - " + SHAPES[sh];
        this.top = t;
        this.topMeta = tm;
        this.filler = f;
        this.fillerMeta = fm;
        this.stone = s;
        this.liquid = l;
        this.biome = b;
        this.baseHeight = base;
        this.seaLevel = sea;
        this.amplitude = amp;
        this.scale = 40 + r.nextInt(100);
        this.ocean = oc;
        this.gravity = 0.35F + r.nextFloat() * 1.1F;
        this.fogR = clamp(fr + (r.nextFloat() - 0.5F) * 0.16F);
        this.fogG = clamp(fg + (r.nextFloat() - 0.5F) * 0.16F);
        this.fogB = clamp(fb + (r.nextFloat() - 0.5F) * 0.16F);
        this.shape = sh;
        this.caves = r.nextInt(100) < 75;
        this.dungeons = r.nextInt(100) < 60;
        this.treeStyle = ts;
        this.treeStyle2 = ts2;
        this.treeDensity = dens;
        this.logBlock = lg;
        this.logMeta = lgm;
        this.leafBlock = lf;
        this.leafMeta = lfm;
        this.groundCover = cover;
        this.structA = sa;
        this.structB = sb;
        this.structEvery = 5 + r.nextInt(12);
        this.oreRichness = 1 + r.nextInt(3);
        this.exoticOre = pickOre(ex, r);
        this.radiation = rad;
    }

    public static int themeOf(long seed) {
        Random q = new Random(seed);
        q.nextInt();
        q.nextInt();
        return q.nextInt(13);
    }

    private static Block pickOre(Block[] ex, Random r) {
        java.util.List<Block> l = new java.util.ArrayList<Block>(java.util.Arrays.asList(ex));
        String[] mod = { "ore_uranium", "ore_thorium", "ore_lead", "ore_titanium", "ore_beryllium", "ore_fluorite", "ore_sulfur" };
        for (String n : mod) {
            Block b = ModLookup.block("hbm", "tile." + n, null);
            if (b != null) l.add(b);
        }
        return l.get(r.nextInt(l.size()));
    }

    public String describe() {
        return "Planeta " + name + " [" + type + "] gravidade " + String.format(Locale.ROOT, "%.2f", gravity) + "g";
    }
}
