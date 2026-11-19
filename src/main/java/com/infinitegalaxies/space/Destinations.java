package com.infinitegalaxies.space;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public final class Destinations {

    public static final String DEFAULT = "Nova Terra";
    private static final String TAG = "ig_destination";

    private Destinations() {}

    public static void set(EntityPlayer p, String name) {
        NBTTagCompound data = p.getEntityData();
        NBTTagCompound persisted = data.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
        persisted.setString(TAG, name);
        data.setTag(EntityPlayer.PERSISTED_NBT_TAG, persisted);
    }

    public static String get(EntityPlayer p) {
        NBTTagCompound persisted = p.getEntityData()
            .getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
        String s = persisted.getString(TAG);
        return (s == null || s.trim()
            .isEmpty()) ? DEFAULT : s;
    }
}
