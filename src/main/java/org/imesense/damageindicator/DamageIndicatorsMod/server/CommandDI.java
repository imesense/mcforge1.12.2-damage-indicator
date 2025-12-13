package org.imesense.damageindicator.DamageIndicatorsMod.server;

import net.minecraft.entity.player.EntityPlayerMP;
import org.imesense.damageindicator.DamageIndicatorsMod.configuration.DIConfig;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
/* loaded from: input.jar:DamageIndicatorsMod/server/CommandDI.class */
public class CommandDI extends CommandBase {
    public String getName() {
        return "direload";
    }

    public String getUsage(ICommandSender icommandsender) {
        return "commands.damageindicatorsmod.direload";
    }

    public void execute(MinecraftServer server, ICommandSender icommandsender, String[] astring) {
        if (icommandsender.getName().toLowerCase().equals("server")) {
            DIConfig.loadConfig(null);
            for (EntityPlayerMP player : server.getPlayerList().getPlayers()) {
                if (player != null) {
                    ServerEventHandler.sendServerSettings(player);
                }
            }
        } else if (FMLCommonHandler.instance().getSide().isClient()) {
            ((EntityPlayer) icommandsender).sendMessage(new TextComponentString("Configuration Reloading"));
            DIConfig.loadConfig(null);
        }
    }
}
