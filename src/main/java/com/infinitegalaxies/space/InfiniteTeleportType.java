package com.infinitegalaxies.space;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import com.infinitegalaxies.planet.PlanetTraits;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.ITeleportType;
import micdoodle8.mods.galacticraft.core.dimension.TeleportTypeMoon;

public class InfiniteTeleportType implements ITeleportType {

    private final ITeleportType moon = new TeleportTypeMoon();

    private int[] cell(EntityPlayerMP p) {
        return Regions.cellOf(Destinations.get(p));
    }

    @Override
    public boolean useParachute() {
        return moon.useParachute();
    }

    @Override
    public Vector3 getPlayerSpawnLocation(WorldServer w, EntityPlayerMP p) {
        Vector3 b = moon.getPlayerSpawnLocation(w, p);
        int[] c = cell(p);
        return new Vector3(Regions.center(c[0]), b.y, Regions.center(c[1]));
    }

    @Override
    public Vector3 getEntitySpawnLocation(WorldServer w, Entity e) {
        return moon.getEntitySpawnLocation(w, e);
    }

    @Override
    public Vector3 getParaChestSpawnLocation(WorldServer w, EntityPlayerMP p, Random rand) {
        Vector3 b = moon.getParaChestSpawnLocation(w, p, rand);
        int[] c = cell(p);
        return new Vector3(
            Regions.center(c[0]) + rand.nextInt(20) - 10,
            b.y,
            Regions.center(c[1]) + rand.nextInt(20) - 10);
    }

    @Override
    public void onSpaceDimensionChanged(World w, EntityPlayerMP p, boolean ridingAutoRocket) {
        moon.onSpaceDimensionChanged(w, p, ridingAutoRocket);
        if (p != null) {
            int[] c = cell(p);
            PlanetTraits t = Regions.traitsOfCell(c[0], c[1]);
            p.addChatMessage(new ChatComponentText(Regions.describe(Destinations.get(p), t)));
        }
    }

    @Override
    public void setupAdventureSpawn(EntityPlayerMP p) {
        moon.setupAdventureSpawn(p);
    }
}
