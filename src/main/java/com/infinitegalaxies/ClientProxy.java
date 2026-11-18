package com.infinitegalaxies;

import net.minecraft.client.Minecraft;

import com.infinitegalaxies.gui.GuiNavigation;

public class ClientProxy extends CommonProxy {

    @Override
    public void openNavigation() {
        Minecraft.getMinecraft()
            .displayGuiScreen(new GuiNavigation());
    }
}
