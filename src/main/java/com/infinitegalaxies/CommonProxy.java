package com.infinitegalaxies;

import com.infinitegalaxies.planet.PlanetTraits;
import com.infinitegalaxies.space.Celestials;

public class CommonProxy {

    public void openNavigation() {}

    public PlanetTraits viewerTraits() {
        return Celestials.NOVA_TRAITS;
    }
}
