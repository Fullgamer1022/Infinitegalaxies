package com.infinitegalaxies.planet;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;

import com.infinitegalaxies.space.Celestials;
import com.infinitegalaxies.space.Regions;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class GravityHandler {

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase e = event.entityLiving;
        if (e.dimension != Celestials.DIM_ID) return;
        PlanetTraits t = Regions.traitsAt(e.posX, e.posZ);
        if (e.onGround || e.isInWater() || e.isOnLadder()) return;
        if (e instanceof EntityPlayer && ((EntityPlayer) e).capabilities.isFlying) return;
        e.motionY += (1.0F - t.gravity) * 0.08D;
        if (t.gravity < 1.0F) e.fallDistance *= t.gravity;
    }
}
