package com.infinitegalaxies.space;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import com.infinitegalaxies.planet.PlanetTraits;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class Radiation {

    private static boolean tried = false;
    private static Field proxyField;
    private static Method setRad;
    private static Method getRad;
    private int ticks = 0;

    private static void init() {
        if (tried) return;
        tried = true;
        try {
            Class<?> mgr = Class.forName("com.hbm.handler.radiation.ChunkRadiationManager");
            proxyField = mgr.getField("proxy");
            Class<?> h = Class.forName("com.hbm.handler.radiation.ChunkRadiationHandler");
            setRad = h.getMethod("setRadiation", World.class, int.class, int.class, int.class, float.class);
            getRad = h.getMethod("getRadiation", World.class, int.class, int.class, int.class);
        } catch (Throwable e) {
            proxyField = null;
            setRad = null;
            getRad = null;
        }
    }

    private static boolean applyHbm(World w, int x, int y, int z, float target) {
        try {
            if (proxyField == null || setRad == null || getRad == null) return false;
            Object proxy = proxyField.get(null);
            if (proxy == null) return false;
            float cur = ((Float) getRad.invoke(proxy, w, x, y, z)).floatValue();
            if (cur < target) setRad.invoke(proxy, w, x, y, z, Float.valueOf(target));
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ServerTickEvent e) {
        if (e.phase != TickEvent.Phase.END) return;
        if (++ticks % 20 != 0) return;
        MinecraftServer s = MinecraftServer.getServer();
        if (s == null) return;
        init();
        for (Object o : s.getConfigurationManager().playerEntityList) {
            EntityPlayerMP p = (EntityPlayerMP) o;
            if (p.dimension != Celestials.DIM_ID) continue;
            PlanetTraits T = Regions.traitsAt(p.posX, p.posZ);
            if (T.radiation <= 0) continue;
            int x = (int) Math.floor(p.posX);
            int y = (int) Math.floor(p.posY);
            int z = (int) Math.floor(p.posZ);
            if (!applyHbm(p.worldObj, x, y, z, (float) T.radiation)) {
                p.addPotionEffect(new PotionEffect(Potion.poison.id, 60, 0));
                p.addPotionEffect(new PotionEffect(Potion.weakness.id, 60, 0));
            }
        }
    }
}
