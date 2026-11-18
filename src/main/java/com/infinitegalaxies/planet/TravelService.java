package com.infinitegalaxies.planet;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.WorldServer;

import com.infinitegalaxies.InfiniteGalaxies;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class TravelService {

    private static class Request {

        final EntityPlayerMP player;
        final String name;

        Request(EntityPlayerMP player, String name) {
            this.player = player;
            this.name = name;
        }
    }

    private static final Queue<Request> QUEUE = new ConcurrentLinkedQueue<Request>();

    public static void enqueue(EntityPlayerMP p, String name) {
        QUEUE.add(new Request(p, name));
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent e) {
        if (e.phase != TickEvent.Phase.END) return;
        Request r;
        while ((r = QUEUE.poll()) != null) {
            try {
                travel(r.player, r.name);
            } catch (Exception ex) {
                InfiniteGalaxies.LOG.error("Falha na viagem", ex);
            }
        }
    }

    private static void travel(EntityPlayerMP p, String raw) {
        if (raw == null) return;
        String n = raw.trim();
        if (n.length() > 40) n = n.substring(0, 40);
        if (n.isEmpty()) return;
        MinecraftServer server = MinecraftServer.getServer();
        String k = PlanetRegistry.key(n);

        if (k.equals("terra") || k.equals("volta") || k.equals("earth")) {
            if (p.dimension == 0) {
                p.addChatMessage(new ChatComponentText("Voce ja esta na Terra."));
                return;
            }
            WorldServer overworld = server.worldServerForDimension(0);
            server.getConfigurationManager()
                .transferPlayerToDimension(p, 0, new PlanetTeleporter(overworld, true));
            return;
        }

        PlanetTraits t = PlanetRegistry.getOrCreate(n, true);
        if (p.dimension == t.dimId) {
            p.addChatMessage(new ChatComponentText("Voce ja esta nesse mundo."));
            return;
        }
        WorldServer ws = server.worldServerForDimension(t.dimId);
        server.getConfigurationManager()
            .transferPlayerToDimension(p, t.dimId, new PlanetTeleporter(ws, false));

        Item consoleItem = Item.getItemFromBlock(InfiniteGalaxies.consoleBlock);
        if (!p.inventory.hasItem(consoleItem)) {
            p.inventory.addItemStackToInventory(new ItemStack(consoleItem));
        }
        p.addChatMessage(new ChatComponentText(t.describe()));
    }
}
