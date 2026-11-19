package com.infinitegalaxies;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import com.infinitegalaxies.gui.GuiNavigation;
import com.infinitegalaxies.planet.PlanetTraits;
import com.infinitegalaxies.space.Celestials;
import com.infinitegalaxies.space.Regions;

public class ClientProxy extends CommonProxy {

    @Override
    public void openNavigation() {
        Minecraft.getMinecraft()
            .displayGuiScreen(new GuiNavigation());
    }

    @Override
    public PlanetTraits viewerTraits() {
        EntityPlayer p = Minecraft.getMinecraft().thePlayer;
        if (p == null) return Celestials.NOVA_TRAITS;
        return Regions.traitsAt(p.posX, p.posZ);
    }
}
