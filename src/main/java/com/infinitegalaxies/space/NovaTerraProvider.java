package com.infinitegalaxies.space;

import com.infinitegalaxies.planet.PlanetTraits;

import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.prefab.world.gen.WorldProviderSpace;
import micdoodle8.mods.galacticraft.api.vector.Vector3;

public class NovaTerraProvider extends WorldProviderSpace {

    private PlanetTraits t() {
        return Celestials.NOVA_TRAITS;
    }

    @Override
    public Vector3 getFogColor() {
        return new Vector3(t().fogR, t().fogG, t().fogB);
    }

    @Override
    public Vector3 getSkyColor() {
        return new Vector3(t().fogR * 0.6D, t().fogG * 0.6D, t().fogB * 0.8D);
    }

    @Override
    public boolean canRainOrSnow() {
        return false;
    }

    @Override
    public boolean hasSunset() {
        return true;
    }

    @Override
    public long getDayLength() {
        return 24000L;
    }

    @Override
    public Class getChunkProviderClass() {
        return NovaTerraChunkProvider.class;
    }

    @Override
    public Class getWorldChunkManagerClass() {
        return NovaTerraChunkManager.class;
    }

    @Override
    public float getGravity() {
        return 0.08F * t().gravity;
    }

    @Override
    public double getMeteorFrequency() {
        return 0.0D;
    }

    @Override
    public double getFuelUsageMultiplier() {
        return 1.0D;
    }

    @Override
    public boolean canSpaceshipTierPass(int tier) {
        return tier >= 1;
    }

    @Override
    public float getFallDamageModifier() {
        return t().gravity;
    }

    @Override
    public float getSoundVolReductionAmount() {
        return 1.0F;
    }

    @Override
    public float getThermalLevelModifier() {
        return 0.0F;
    }

    @Override
    public float getWindLevel() {
        return 0.0F;
    }

    @Override
    public boolean hasBreathableAtmosphere() {
        return true;
    }

    @Override
    public CelestialBody getCelestialBody() {
        return Celestials.novaTerra;
    }
}
