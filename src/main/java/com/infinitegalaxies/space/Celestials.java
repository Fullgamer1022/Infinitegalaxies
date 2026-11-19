package com.infinitegalaxies.space;

import net.minecraft.util.ResourceLocation;

import com.infinitegalaxies.planet.PlanetTraits;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.api.galaxies.SolarSystem;
import micdoodle8.mods.galacticraft.api.galaxies.Star;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.dimension.TeleportTypeMoon;

public class Celestials {

    public static final int DIM_ID = 5151;

    public static SolarSystem system;
    public static Planet novaTerra;
    public static PlanetTraits NOVA_TRAITS;

    private static ResourceLocation icon(String name) {
        return new ResourceLocation("infinitegalaxies", "textures/gui/celestialbodies/" + name + ".png");
    }

    public static void register() {
        NOVA_TRAITS = new PlanetTraits("Nova Terra", PlanetTraits.seedOf("nova terra"), DIM_ID);

        system = new SolarSystem("infiniteSystem", "milkyWay");
        Star star = new Star("infiniteStar");
        star.setParentSolarSystem(system);
        star.setBodyIcon(icon("star"));
        system.setMainStar(star);
        system.setMapPosition(new Vector3(-1.5F, 1.2F, 0.0F));
        GalaxyRegistry.registerSolarSystem(system);

        GalacticraftRegistry.registerTeleportType(NovaTerraProvider.class, new TeleportTypeMoon());

        novaTerra = new Planet("novaTerra");
        novaTerra.setParentSolarSystem(system);
        novaTerra.setRingColorRGB(0.3F, 0.7F, 0.4F);
        novaTerra.setPhaseShift(1.0F);
        novaTerra.setRelativeDistanceFromCenter(new CelestialBody.ScalableDistance(1.0F, 1.0F));
        novaTerra.setRelativeOrbitTime(1.0F);
        novaTerra.setBodyIcon(icon("nova_terra"));
        novaTerra.setDimensionInfo(DIM_ID, NovaTerraProvider.class);
        novaTerra.setTierRequired(1);
        GalaxyRegistry.registerPlanet(novaTerra);
    }
}
