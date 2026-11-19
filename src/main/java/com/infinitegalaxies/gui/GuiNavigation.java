package com.infinitegalaxies.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import org.lwjgl.input.Keyboard;

import com.infinitegalaxies.InfiniteGalaxies;
import com.infinitegalaxies.net.PacketTravel;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiNavigation extends GuiScreen {

    private GuiTextField field;

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        int cx = this.width / 2;
        int cy = this.height / 2;
        field = new GuiTextField(this.fontRendererObj, cx - 100, cy - 10, 200, 20);
        field.setMaxStringLength(40);
        field.setFocused(true);
        this.buttonList.add(new GuiButton(0, cx - 100, cy + 20, 200, 20, "Definir destino"));
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void updateScreen() {
        field.updateCursorCounter();
    }

    @Override
    protected void keyTyped(char c, int code) {
        if (code == Keyboard.KEY_RETURN || code == Keyboard.KEY_NUMPADENTER) {
            send(field.getText());
        } else if (code == Keyboard.KEY_ESCAPE) {
            this.mc.displayGuiScreen(null);
        } else {
            field.textboxKeyTyped(c, code);
        }
    }

    @Override
    protected void mouseClicked(int x, int y, int b) {
        super.mouseClicked(x, y, b);
        field.mouseClicked(x, y, b);
    }

    @Override
    protected void actionPerformed(GuiButton b) {
        if (b.id == 0) send(field.getText());
        else if (b.id == 1) send("terra");
    }

    private void send(String s) {
        if (s == null || s.trim()
            .isEmpty()) return;
        InfiniteGalaxies.NET.sendToServer(new PacketTravel(s.trim()));
        this.mc.displayGuiScreen(null);
    }

    @Override
    public void drawScreen(int mx, int my, float pt) {
        drawDefaultBackground();
        drawCenteredString(this.fontRendererObj, "Console Estelar", this.width / 2, this.height / 2 - 60, 0xB388FF);
        drawCenteredString(
            this.fontRendererObj,
            "Escreva o destino do foguete (qualquer nome)",
            this.width / 2,
            this.height / 2 - 38,
            0xCCCCCC);
        field.drawTextBox();
        super.drawScreen(mx, my, pt);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
