package com.infinitegalaxies.planet;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.WorldServer;

public class CommandPlanet extends CommandBase {

    @Override
    public String getCommandName() {
        return "planeta";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/planeta <nome do planeta>   ou   /planeta volta";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        EntityPlayerMP player = getCommandSenderAsPlayer(sender);
        if (args.length == 0) throw new WrongUsageException(getCommandUsage(sender));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            if (i > 0) sb.append(' ');
            sb.append(args[i]);
        }
        String name = sb.toString();
        MinecraftServer server = MinecraftServer.getServer();

        if (name.equalsIgnoreCase("volta")) {
            if (player.dimension == 0) {
                player.addChatMessage(new ChatComponentText("Voce ja esta na Terra."));
                return;
            }
            WorldServer overworld = server.worldServerForDimension(0);
            server.getConfigurationManager()
                .transferPlayerToDimension(player, 0, new PlanetTeleporter(overworld, true));
            return;
        }

        PlanetTraits t = PlanetRegistry.getOrCreate(name, true);
        if (player.dimension == t.dimId) {
            player.addChatMessage(new ChatComponentText("Voce ja esta nesse planeta."));
            return;
        }
        WorldServer ws = server.worldServerForDimension(t.dimId);
        server.getConfigurationManager()
            .transferPlayerToDimension(player, t.dimId, new PlanetTeleporter(ws, false));
        player.addChatMessage(new ChatComponentText(t.describe()));
    }
}
