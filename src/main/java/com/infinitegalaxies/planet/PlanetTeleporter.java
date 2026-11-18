package com.infinitegalaxies.planet;

import net.minecraft.entity.Entity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class PlanetTeleporter extends Teleporter {

    private final WorldServer ws;
    private final boolean home;

    public PlanetTeleporter(WorldServer ws, boolean home) {
        super(ws);
        this.ws = ws;
        this.home = home;
    }

    @Override
    public void placeInPortal(Entity e, double x, double y, double z, float yaw) {
        int px = 0;
        int pz = 0;
        if (home) {
            ChunkCoordinates cc = ws.getSpawnPoint();
            px = cc.posX;
            pz = cc.posZ;
        }
        ws.getChunkFromBlockCoords(px, pz);
        int py = ws.getTopSolidOrLiquidBlock(px, pz);
        e.setLocationAndAngles(px + 0.5D, py + 1.0D, pz + 0.5D, e.rotationYaw, 0.0F);
        e.motionX = 0;
        e.motionY = 0;
        e.motionZ = 0;
    }
}
