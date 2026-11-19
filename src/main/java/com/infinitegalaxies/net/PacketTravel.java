package com.infinitegalaxies.net;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

import com.infinitegalaxies.space.Destinations;
import com.infinitegalaxies.space.Regions;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class PacketTravel implements IMessage {

    public String name = "";

    public PacketTravel() {}

    public PacketTravel(String name) {
        this.name = name;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        name = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, name);
    }

    public static class Handler implements IMessageHandler<PacketTravel, IMessage> {

        @Override
        public IMessage onMessage(PacketTravel msg, MessageContext ctx) {
            EntityPlayerMP p = ctx.getServerHandler().playerEntity;
            String n = msg.name == null ? "" : msg.name.trim();
            if (n.isEmpty()) return null;
            if (n.length() > 40) n = n.substring(0, 40);
            Destinations.set(p, n);
            int[] c = Regions.cellOf(n);
            p.addChatMessage(
                new ChatComponentText(
                    "Destino definido: " + Regions.describe(n, Regions.traitsOfCell(c[0], c[1]))
                        + ". Lance o foguete e escolha o Destino Infinito no mapa."));
            return null;
        }
    }
}
