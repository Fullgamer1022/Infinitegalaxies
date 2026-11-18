package com.infinitegalaxies.net;

import net.minecraft.entity.player.EntityPlayerMP;

import com.infinitegalaxies.planet.TravelService;

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
            TravelService.enqueue(p, msg.name);
            return null;
        }
    }
}
