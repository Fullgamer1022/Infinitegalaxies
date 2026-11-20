package com.infinitegalaxies.planet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;

public final class NuclearFeatures {

    private NuclearFeatures() {}

    private static Block hb(String n, Block fb) {
        return ModLookup.block("hbm", "tile." + n, fb);
    }

    private static void set(World w, int x, int y, int z, Block b, int meta) {
        if (y > 0 && y < 255) w.setBlock(x, y, z, b, meta, 2);
    }

    private static int ground(World w, int x, int z) {
        return w.getHeightValue(x, z) - 1;
    }

    static void populate(World w, Random r, PlanetTraits T, int bx, int bz) {
        Block fallout = hb("fallout", null);
        Block trin = hb("waste_trinitite", Blocks.glass);
        Block earth = hb("waste_earth", Blocks.gravel);
        for (int i = 0; i < 30; i++) {
            int x = bx + 8 + r.nextInt(16);
            int z = bz + 8 + r.nextInt(16);
            int g = ground(w, x, z);
            Block gb = w.getBlock(x, g, z);
            if (!gb.getMaterial()
                .isSolid() || !w.isAirBlock(x, g + 1, z))
                continue;
            int k = r.nextInt(100);
            if (k < 50 && fallout != null) set(w, x, g + 1, z, fallout, 0);
            else if (k >= 50 && k < 70) set(w, x, g, z, trin, 0);
            else if (k >= 70 && k < 80) set(w, x, g, z, earth, 0);
        }
        if (r.nextInt(3) == 0) crater(w, r, bx + 8 + r.nextInt(8), bz + 8 + r.nextInt(8), trin, earth);
        if (r.nextInt(4) == 0) ruin(w, r, bx + 8 + r.nextInt(6), bz + 8 + r.nextInt(6));
        Block u = hb("ore_uranium", null);
        if (u != null) {
            for (int i = 0; i < 3; i++) {
                new WorldGenMinable(u, 6, T.stone).generate(w, r, bx + r.nextInt(16), 12 + r.nextInt(50), bz + r.nextInt(16));
            }
        }
    }

    private static void crater(World w, Random r, int x, int z, Block floor, Block rim) {
        int R = 6 + r.nextInt(7);
        int D = R / 2 + 3;
        int y0 = ground(w, x, z);
        for (int dx = -R - 2; dx <= R + 2; dx++) {
            for (int dz = -R - 2; dz <= R + 2; dz++) {
                double d = Math.sqrt(dx * dx + dz * dz);
                int px = x + dx;
                int pz = z + dz;
                int sy = ground(w, px, pz);
                if (d <= R) {
                    int ny = y0 - (int) (D * (1.0 - (d / R) * (d / R)));
                    for (int yy = sy; yy > ny; yy--) set(w, px, yy, pz, Blocks.air, 0);
                    if (d < R * 0.25) set(w, px, ny, pz, Blocks.lava, 0);
                    else set(w, px, ny, pz, floor, 0);
                } else if (d <= R + 2) {
                    set(w, px, sy + 1, pz, rim, 0);
                }
            }
        }
    }

    private static void ruin(World w, Random r, int x, int z) {
        Block[] mats = { hb("brick_concrete", Blocks.stonebrick), hb("brick_concrete_cracked", Blocks.stonebrick),
            hb("brick_concrete_broken", Blocks.cobblestone), hb("brick_concrete_mossy", Blocks.mossy_cobblestone) };
        int S = 9;
        int H = 5 + r.nextInt(5);
        int y0 = ground(w, x, z) + 1;
        for (int dx = 0; dx < S; dx++) {
            for (int dz = 0; dz < S; dz++) {
                for (int yy = y0 - 1; yy > y0 - 10 && yy > 1; yy--) {
                    if (w.getBlock(x + dx, yy, z + dz)
                        .getMaterial()
                        .isSolid())
                        break;
                    set(w, x + dx, yy, z + dz, mats[0], 0);
                }
            }
        }
        for (int dy = 0; dy < H; dy++) {
            for (int dx = 0; dx < S; dx++) {
                for (int dz = 0; dz < S; dz++) {
                    boolean wall = dx == 0 || dz == 0 || dx == S - 1 || dz == S - 1;
                    int px = x + dx;
                    int py = y0 + dy;
                    int pz = z + dz;
                    if (!wall) {
                        set(w, px, py, pz, dy == 0 ? mats[0] : Blocks.air, 0);
                        continue;
                    }
                    boolean door = dy < 2 && dz == 0 && dx == S / 2;
                    boolean window = dy == 2 && (dx % 3 == 1 || dz % 3 == 1);
                    boolean broken = dy > 1 && r.nextInt(100) < 18 + dy * 6;
                    if (door || window || broken) {
                        set(w, px, py, pz, Blocks.air, 0);
                        continue;
                    }
                    set(w, px, py, pz, mats[r.nextInt(mats.length)], 0);
                }
            }
        }
        loot(w, r, x + S / 2, y0 + 1, z + S / 2);
    }

    private static void loot(World w, Random r, int x, int y, int z) {
        set(w, x, y, z, Blocks.chest, 2);
        Object te = w.getTileEntity(x, y, z);
        if (!(te instanceof TileEntityChest)) return;
        TileEntityChest c = (TileEntityChest) te;
        String[] special = { "geiger_counter", "dosimeter", "hazmat_helmet", "hazmat_plate", "hazmat_legs",
            "hazmat_boots", "pill_iodine", "radaway", "radx", "gas_mask", "ingot_uranium", "ingot_lead",
            "scrap_nuclear", "ingot_steel", "plate_steel" };
        List<Item> pool = new ArrayList<Item>();
        for (String n : special) {
            Item it = ModLookup.item("hbm", "item." + n);
            if (it != null) pool.add(it);
        }
        pool.add(Items.iron_ingot);
        pool.add(Items.bread);
        pool.add(Items.coal);
        Item geiger = ModLookup.item("hbm", "item.geiger_counter");
        if (geiger != null) c.setInventorySlotContents(0, new ItemStack(geiger, 1));
        int n = 5 + r.nextInt(5);
        for (int i = 0; i < n; i++) {
            Item it = pool.get(r.nextInt(pool.size()));
            int cnt = it.getItemStackLimit() > 1 ? 1 + r.nextInt(3) : 1;
            c.setInventorySlotContents(1 + r.nextInt(26), new ItemStack(it, cnt));
        }
    }
}
