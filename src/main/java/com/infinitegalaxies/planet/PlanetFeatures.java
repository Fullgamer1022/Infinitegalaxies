package com.infinitegalaxies.planet;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.feature.WorldGenMinable;

public final class PlanetFeatures {

    private PlanetFeatures() {}

    private static void set(World w, int x, int y, int z, Block b, int meta) {
        if (y > 0 && y < 255) w.setBlock(x, y, z, b, meta, 2);
    }

    private static void set(World w, int x, int y, int z, Block b) {
        set(w, x, y, z, b, 0);
    }

    private static boolean free(World w, int x, int y, int z) {
        Material m = w.getBlock(x, y, z)
            .getMaterial();
        return m == Material.air || m == Material.leaves || m == Material.plants || m == Material.vine;
    }

    private static void leaf(World w, int x, int y, int z, Block b, int meta) {
        if (free(w, x, y, z)) set(w, x, y, z, b, meta);
    }

    private static int ground(World w, int x, int z) {
        return w.getHeightValue(x, z) - 1;
    }

    private static void foundation(World w, int x0, int z0, int x1, int z1, int y, Block b) {
        for (int x = x0; x <= x1; x++) {
            for (int z = z0; z <= z1; z++) {
                for (int yy = y; yy > y - 14 && yy > 1; yy--) {
                    if (w.getBlock(x, yy, z)
                        .getMaterial()
                        .isSolid()) break;
                    set(w, x, yy, z, b);
                }
            }
        }
    }

    private static void chest(World w, Random r, int x, int y, int z) {
        set(w, x, y, z, Blocks.chest, 2);
        Object te = w.getTileEntity(x, y, z);
        if (te instanceof TileEntityChest) {
            TileEntityChest c = (TileEntityChest) te;
            Item[] loot = { Items.iron_ingot, Items.gold_ingot, Items.diamond, Items.emerald, Items.ender_pearl,
                Items.golden_apple, Items.bread, Items.arrow, Items.redstone, Items.bone, Items.coal,
                Items.saddle, Items.blaze_rod };
            int n = 4 + r.nextInt(5);
            for (int i = 0; i < n; i++) {
                c.setInventorySlotContents(
                    r.nextInt(27),
                    new ItemStack(loot[r.nextInt(loot.length)], 1 + r.nextInt(4)));
            }
        }
    }

    // ---------------------------------------------------------------- minerios e masmorras

    private static void vein(World w, Random r, Block ore, int size, int tries, int minY, int maxY, int bx, int bz,
        Block target) {
        for (int i = 0; i < tries; i++) {
            int x = bx + r.nextInt(16);
            int y = minY + r.nextInt(Math.max(1, maxY - minY));
            int z = bz + r.nextInt(16);
            new WorldGenMinable(ore, size, target).generate(w, r, x, y, z);
        }
    }

    static void ores(World w, Random r, PlanetTraits T, int bx, int bz) {
        int k = T.oreRichness;
        Block s = T.stone;
        vein(w, r, Blocks.coal_ore, 17, 12, 2, 120, bx, bz, s);
        vein(w, r, Blocks.iron_ore, 9, 12, 2, 70, bx, bz, s);
        vein(w, r, Blocks.gold_ore, 9, k + 1, 2, 32, bx, bz, s);
        vein(w, r, Blocks.redstone_ore, 8, 4 + k, 2, 20, bx, bz, s);
        vein(w, r, Blocks.diamond_ore, 8, k, 2, 20, bx, bz, s);
        vein(w, r, Blocks.lapis_ore, 7, k, 2, 34, bx, bz, s);
        vein(w, r, T.exoticOre, 8, 3 * k, 4, 70, bx, bz, s);
    }

    static void dungeons(World w, Random r, int bx, int bz) {
        for (int i = 0; i < 6; i++) {
            int x = bx + r.nextInt(16) + 8;
            int y = 8 + r.nextInt(60);
            int z = bz + r.nextInt(16) + 8;
            new WorldGenDungeons().generate(w, r, x, y, z);
        }
    }

    // ---------------------------------------------------------------- vegetacao

    static void vegetation(World w, Random r, PlanetTraits T, int bx, int bz) {
        for (int i = 0; i < T.treeDensity; i++) {
            int x = bx + 8 + r.nextInt(16);
            int z = bz + 8 + r.nextInt(16);
            int g = ground(w, x, z);
            Block gb = w.getBlock(x, g, z);
            if (gb.getMaterial()
                .isLiquid()
                || !gb.getMaterial()
                    .isSolid())
                continue;
            int style = r.nextInt(100) < 70 ? T.treeStyle : T.treeStyle2;
            if (style == 7 && gb != Blocks.sand) continue;
            tree(w, r, x, g + 1, z, T, style);
        }
        if (T.groundCover > 0) {
            for (int i = 0; i < 24; i++) {
                int x = bx + 8 + r.nextInt(16);
                int z = bz + 8 + r.nextInt(16);
                int g = ground(w, x, z);
                Block gb = w.getBlock(x, g, z);
                if (!w.isAirBlock(x, g + 1, z)) continue;
                if (T.groundCover == 1 && gb == Blocks.grass) {
                    int k = r.nextInt(100);
                    if (k < 80) set(w, x, g + 1, z, Blocks.tallgrass, 1);
                    else set(w, x, g + 1, z, k < 90 ? Blocks.red_flower : Blocks.yellow_flower, 0);
                } else if (T.groundCover == 2
                    && (gb == Blocks.sand || gb == Blocks.hardened_clay || gb == Blocks.stained_hardened_clay)) {
                    set(w, x, g + 1, z, Blocks.deadbush, 0);
                } else if (T.groundCover == 3 && gb == Blocks.mycelium) {
                    set(w, x, g + 1, z, Blocks.brown_mushroom, 0);
                }
            }
        }
    }

    private static void tree(World w, Random r, int x, int y, int z, PlanetTraits T, int style) {
        Block lg = T.logBlock;
        int lgm = T.logMeta;
        Block lf = T.leafBlock;
        int lm = (lf == Blocks.leaves || lf == Blocks.leaves2) ? (T.leafMeta | 4) : T.leafMeta;
        switch (style) {
            case 0: {
                int h = 4 + r.nextInt(4);
                for (int k = 0; k < h; k++) set(w, x, y + k, z, lg, lgm);
                for (int dy = -1; dy <= 2; dy++) {
                    int rr = dy >= 2 ? 1 : 2;
                    for (int dx = -rr; dx <= rr; dx++) {
                        for (int dz = -rr; dz <= rr; dz++) {
                            if (Math.abs(dx) == rr && Math.abs(dz) == rr && r.nextBoolean()) continue;
                            leaf(w, x + dx, y + h + dy, z + dz, lf, lm);
                        }
                    }
                }
                break;
            }
            case 1: {
                int h = 7 + r.nextInt(6);
                for (int k = 0; k < h; k++) set(w, x, y + k, z, lg, lgm);
                for (int dy = 2; dy <= h + 1; dy++) {
                    int rad = dy == h + 1 ? 0 : (int) Math.ceil((h + 2 - dy) * 0.4);
                    for (int dx = -rad; dx <= rad; dx++) {
                        for (int dz = -rad; dz <= rad; dz++) {
                            if (dx * dx + dz * dz > rad * rad + 1) continue;
                            leaf(w, x + dx, y + dy, z + dz, lf, lm);
                        }
                    }
                }
                break;
            }
            case 2: {
                int h = 5 + r.nextInt(5);
                int ox = 0;
                int oz = 0;
                for (int k = 0; k < h; k++) {
                    if (k == h / 2) {
                        ox = r.nextInt(3) - 1;
                        oz = r.nextInt(3) - 1;
                    }
                    set(w, x + ox, y + k, z + oz, lg, lgm);
                }
                int tx = x + ox;
                int tz = z + oz;
                int ty = y + h;
                for (int dx = -3; dx <= 3; dx++) {
                    for (int dz = -3; dz <= 3; dz++) {
                        int d2 = dx * dx + dz * dz;
                        if (d2 <= 9) leaf(w, tx + dx, ty, tz + dz, lf, lm);
                        if (d2 <= 4) leaf(w, tx + dx, ty + 1, tz + dz, lf, lm);
                        if (d2 >= 5 && d2 <= 9 && r.nextBoolean()) leaf(w, tx + dx, ty - 1, tz + dz, lf, lm);
                    }
                }
                break;
            }
            case 3: {
                int h = 14 + r.nextInt(12);
                for (int k = 0; k < h; k++) {
                    set(w, x, y + k, z, lg, lgm);
                    set(w, x + 1, y + k, z, lg, lgm);
                    set(w, x, y + k, z + 1, lg, lgm);
                    set(w, x + 1, y + k, z + 1, lg, lgm);
                }
                int cy = y + h;
                for (int dx = -6; dx <= 7; dx++) {
                    for (int dy = -5; dy <= 5; dy++) {
                        for (int dz = -6; dz <= 7; dz++) {
                            double ax = dx - 0.5;
                            double az = dz - 0.5;
                            if (ax * ax + dy * dy + az * az <= 28) leaf(w, x + dx, cy + dy, z + dz, lf, lm);
                            else if (dy == -3 && ax * ax + az * az <= 36 && r.nextInt(100) < 55)
                                leaf(w, x + dx, cy + dy, z + dz, lf, lm);
                        }
                    }
                }
                break;
            }
            case 4: {
                int h = 4 + r.nextInt(6);
                for (int k = 0; k < h; k++) set(w, x, y + k, z, lg, lgm);
                int nb = 2 + r.nextInt(2);
                for (int b = 0; b < nb; b++) {
                    int by = y + 2 + r.nextInt(Math.max(1, h - 2));
                    int dx = r.nextInt(3) - 1;
                    int dz = r.nextInt(3) - 1;
                    if (dx == 0 && dz == 0) dx = 1;
                    int len = 1 + r.nextInt(3);
                    for (int s = 1; s <= len; s++) {
                        set(w, x + dx * s, by + (s > 2 ? 1 : 0), z + dz * s, lg, lgm | (dx != 0 ? 4 : 8));
                    }
                }
                break;
            }
            case 5: {
                int h = 4 + r.nextInt(6);
                for (int k = 0; k < h; k++) set(w, x, y + k, z, lg, lgm);
                for (int dx = -2; dx <= 2; dx++) {
                    for (int dy = -2; dy <= 2; dy++) {
                        for (int dz = -2; dz <= 2; dz++) {
                            if (dx * dx + dy * dy + dz * dz <= 5 && r.nextInt(100) < 65)
                                leaf(w, x + dx, y + h + dy, z + dz, lf, lm);
                        }
                    }
                }
                for (int k = 0; k < 3; k++) leaf(w, x, y + h + 3 + k, z, lf, lm);
                break;
            }
            case 6: {
                Block cap = r.nextBoolean() ? Blocks.brown_mushroom_block : Blocks.red_mushroom_block;
                int h = 4 + r.nextInt(5);
                for (int k = 0; k < h; k++) set(w, x, y + k, z, cap, 10);
                int top = y + h;
                for (int dx = -3; dx <= 3; dx++) {
                    for (int dz = -3; dz <= 3; dz++) {
                        int d2 = dx * dx + dz * dz;
                        if (d2 <= 10) set(w, x + dx, top, z + dz, cap, 14);
                        if (d2 >= 6 && d2 <= 12) set(w, x + dx, top - 1, z + dz, cap, 14);
                        if (d2 <= 4) set(w, x + dx, top + 1, z + dz, cap, 14);
                    }
                }
                break;
            }
            case 7: {
                int h = 1 + r.nextInt(3);
                for (int k = 0; k < h; k++) set(w, x, y + k, z, Blocks.cactus, 0);
                break;
            }
            default:
                break;
        }
    }

    // ---------------------------------------------------------------- estruturas

    static void structures(World w, Random r, PlanetTraits T, int bx, int bz) {
        if (T.radiation > 0) {
            NuclearFeatures.populate(w, r, T, bx, bz);
            return;
        }
        if (r.nextInt(T.structEvery) != 0) return;
        int x = bx + 8 + r.nextInt(8);
        int z = bz + 8 + r.nextInt(8);
        if (w.getBlock(x, ground(w, x, z), z)
            .getMaterial()
            .isLiquid())
            return;
        int kind = r.nextBoolean() ? T.structA : T.structB;
        switch (kind) {
            case 1:
                tower(w, r, x, z);
                break;
            case 2:
                obelisk(w, r, x, z);
                break;
            case 3:
                arch(w, r, x, z, T);
                break;
            case 4:
                crater(w, r, x, z, T);
                break;
            case 5:
                pyramid(w, r, x, z);
                break;
            case 6:
                crystals(w, r, x, z);
                break;
            case 7:
                new WorldGenLakes(T.liquid).generate(w, r, x, ground(w, x, z) + 5, z);
                break;
            default:
                henge(w, r, x, z);
                break;
        }
    }

    private static void tower(World w, Random r, int x, int z) {
        int y0 = ground(w, x, z) + 1;
        int S = 7;
        int H = 10 + r.nextInt(8);
        foundation(w, x, z, x + S - 1, z + S - 1, y0 - 1, Blocks.cobblestone);
        for (int dy = 0; dy < H; dy++) {
            for (int dx = 0; dx < S; dx++) {
                for (int dz = 0; dz < S; dz++) {
                    boolean wall = dx == 0 || dz == 0 || dx == S - 1 || dz == S - 1;
                    int px = x + dx;
                    int py = y0 + dy;
                    int pz = z + dz;
                    if (!wall) {
                        if (dx == 1 && dz == 3 && dy >= 1) set(w, px, py, pz, Blocks.ladder, 5);
                        else set(w, px, py, pz, dy % 5 == 0 ? Blocks.planks : Blocks.air);
                        continue;
                    }
                    if (dy > H * 0.6 && r.nextInt(100) < (int) ((dy - H * 0.6) * 12)) {
                        set(w, px, py, pz, Blocks.air);
                        continue;
                    }
                    boolean window = dy % 5 == 3
                        && ((dx == 3 && (dz == 0 || dz == S - 1)) || (dz == 3 && (dx == 0 || dx == S - 1)));
                    boolean door = dy < 2 && dz == 0 && dx == 3;
                    if (window || door) {
                        set(w, px, py, pz, Blocks.air);
                        continue;
                    }
                    int k = r.nextInt(10);
                    if (k < 6) set(w, px, py, pz, Blocks.stonebrick, r.nextInt(3));
                    else if (k < 8) set(w, px, py, pz, Blocks.mossy_cobblestone);
                    else set(w, px, py, pz, Blocks.cobblestone);
                }
            }
        }
        chest(w, r, x + 2, y0 + 1, z + 2);
    }

    private static void obelisk(World w, Random r, int x, int z) {
        int y0 = ground(w, x, z) + 1;
        int H = 14 + r.nextInt(18);
        foundation(w, x - 2, z - 2, x + 2, z + 2, y0 - 1, Blocks.stonebrick);
        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) set(w, x + dx, y0, z + dz, Blocks.stonebrick, 3);
        }
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) set(w, x + dx, y0 + 1, z + dz, Blocks.stonebrick, 0);
        }
        for (int dy = 2; dy <= H; dy++) set(w, x, y0 + dy, z, Blocks.obsidian);
        set(w, x, y0 + H + 1, z, Blocks.glowstone);
        set(w, x + 1, y0 + H - 2, z, Blocks.glowstone);
        set(w, x - 1, y0 + H - 2, z, Blocks.glowstone);
        set(w, x, y0 + H - 2, z + 1, Blocks.glowstone);
        set(w, x, y0 + H - 2, z - 1, Blocks.glowstone);
    }

    private static Block archMat(PlanetTraits T) {
        if (T.top == Blocks.sand) return Blocks.sandstone;
        if (T.top == Blocks.snow) return Blocks.packed_ice;
        if (T.top == Blocks.end_stone) return Blocks.end_stone;
        if (T.top == Blocks.netherrack) return Blocks.nether_brick;
        return Blocks.stonebrick;
    }

    private static void arch(World w, Random r, int x, int z, PlanetTraits T) {
        int y0 = ground(w, x, z) + 1;
        int H = 9 + r.nextInt(5);
        int gap = 7;
        Block m = archMat(T);
        foundation(w, x, z, x + 1, z + 1, y0 - 1, m);
        foundation(w, x + gap + 2, z, x + gap + 3, z + 1, y0 - 1, m);
        for (int dy = 0; dy <= H; dy++) {
            for (int a = 0; a < 2; a++) {
                for (int b = 0; b < 2; b++) {
                    set(w, x + a, y0 + dy, z + b, m);
                    set(w, x + gap + 2 + a, y0 + dy, z + b, m);
                }
            }
        }
        for (int dx = 0; dx <= gap + 3; dx++) {
            set(w, x + dx, y0 + H + 1, z, m);
            set(w, x + dx, y0 + H + 1, z + 1, m);
        }
        for (int b = 0; b < 2; b++) {
            set(w, x + 2, y0 + H, z + b, m);
            set(w, x + gap + 1, y0 + H, z + b, m);
        }
    }

    private static void crater(World w, Random r, int x, int z, PlanetTraits T) {
        int R = 7 + r.nextInt(9);
        int D = R / 2 + 2;
        int y0 = ground(w, x, z);
        for (int dx = -R - 2; dx <= R + 2; dx++) {
            for (int dz = -R - 2; dz <= R + 2; dz++) {
                double d = Math.sqrt(dx * dx + dz * dz);
                int px = x + dx;
                int pz = z + dz;
                int sy = ground(w, px, pz);
                if (d <= R) {
                    int ny = y0 - (int) (D * (1.0 - (d / R) * (d / R)));
                    for (int yy = sy; yy > ny; yy--) set(w, px, yy, pz, Blocks.air);
                    if (d < R * 0.5) set(w, px, ny, pz, Blocks.lava);
                    else if (d < R * 0.7) set(w, px, ny, pz, Blocks.obsidian);
                    else set(w, px, ny, pz, T.top, T.topMeta);
                } else if (d <= R + 2) {
                    set(w, px, sy + 1, pz, T.filler, T.fillerMeta);
                }
            }
        }
    }

    private static void pyramid(World w, Random r, int x, int z) {
        int half = 10;
        int y0 = ground(w, x, z) + 1;
        foundation(w, x - half, z - half, x + half, z + half, y0 - 1, Blocks.sandstone);
        for (int k = 0; k <= 10; k++) {
            int hf = half - k;
            for (int dx = -hf; dx <= hf; dx++) {
                for (int dz = -hf; dz <= hf; dz++) {
                    boolean chamber = k >= 1 && k <= 4 && Math.abs(dx) <= 3 && Math.abs(dz) <= 3 && hf >= 4;
                    if (chamber) {
                        set(w, x + dx, y0 + k, z + dz, Blocks.air);
                        continue;
                    }
                    set(w, x + dx, y0 + k, z + dz, Blocks.sandstone, k % 4 == 3 ? 2 : 0);
                }
            }
        }
        for (int k = 1; k <= 2; k++) {
            for (int dz = 4; dz <= 10; dz++) set(w, x, y0 + k, z + dz, Blocks.air);
        }
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                set(w, x + dx, y0 - 1, z + dz, Blocks.tnt);
                set(w, x + dx, y0 - 2, z + dz, Blocks.tnt);
            }
        }
        set(w, x, y0 + 1, z, Blocks.stone_pressure_plate);
        chest(w, r, x + 2, y0 + 1, z - 2);
        chest(w, r, x - 2, y0 + 1, z - 2);
    }

    private static void crystals(World w, Random r, int x, int z) {
        Block[] mats = { Blocks.glass, Blocks.quartz_block, Blocks.glowstone, Blocks.packed_ice, Blocks.obsidian };
        Block m = mats[r.nextInt(mats.length)];
        int n = 6 + r.nextInt(7);
        for (int i = 0; i < n; i++) {
            int sx = x + r.nextInt(9) - 4;
            int sz = z + r.nextInt(9) - 4;
            int sy = ground(w, sx, sz) + 1;
            int h = 4 + r.nextInt(11);
            int dirx = r.nextInt(3) - 1;
            int dirz = r.nextInt(3) - 1;
            int lx = 0;
            int lz = 0;
            for (int k = 0; k < h; k++) {
                if (k > 0 && k % 3 == 0) {
                    lx += dirx;
                    lz += dirz;
                }
                set(w, sx + lx, sy + k, sz + lz, m);
            }
        }
    }

    private static void henge(World w, Random r, int x, int z) {
        int n = 8 + r.nextInt(5);
        int R = 9;
        for (int i = 0; i < n; i++) {
            double a = 2 * Math.PI * i / n;
            int px = x + (int) Math.round(Math.cos(a) * R);
            int pz = z + (int) Math.round(Math.sin(a) * R);
            int y0 = ground(w, px, pz) + 1;
            int h = 5 + r.nextInt(3);
            foundation(w, px, pz, px, pz, y0 - 1, Blocks.stonebrick);
            for (int dy = 0; dy < h; dy++) set(w, px, y0 + dy, pz, Blocks.stonebrick, r.nextInt(3));
        }
        int y0 = ground(w, x, z) + 1;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) set(w, x + dx, y0, z + dz, Blocks.stonebrick, 3);
        }
        set(w, x, y0 + 1, z, Blocks.glowstone);
        chest(w, r, x + 1, y0 + 1, z);
    }
}
