package com.infinitegalaxies.planet;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import cpw.mods.fml.common.registry.GameRegistry;

public final class ModLookup {

    private ModLookup() {}

    public static Block block(String mod, String name, Block fallback) {
        try {
            Block b = GameRegistry.findBlock(mod, name);
            return b != null ? b : fallback;
        } catch (Throwable e) {
            return fallback;
        }
    }

    public static Item item(String mod, String name) {
        try {
            return GameRegistry.findItem(mod, name);
        } catch (Throwable e) {
            return null;
        }
    }
}
