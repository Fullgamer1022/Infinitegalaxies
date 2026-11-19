package com.infinitegalaxies.space;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

public class Rescue {

    @SubscribeEvent
    public void onLogin(PlayerEvent.PlayerLoggedInEvent e) {
        if (!(e.player instanceof EntityPlayerMP)) return;
        EntityPlayerMP p = (EntityPlayerMP) e.player;
        if (p.dimension != Celestials.DIM_ID) return;
        if (Math.abs(p.posX) < 2.9E7D && Math.abs(p.posZ) < 2.9E7D) return;
        int[] c = Regions.cellOf(Destinations.get(p));
        double x = Regions.center(c[0]);
        double z = Regions.center(c[1]);
        WorldServer w = (WorldServer) p.worldObj;
        int y = w.getTopSolidOrLiquidBlock((int) x, (int) z);
        p.fallDistance = 0.0F;
        p.playerNetServerHandler.setPlayerLocation(x, y + 1.5D, z, p.rotationYaw, p.rotationPitch);
    }
}
