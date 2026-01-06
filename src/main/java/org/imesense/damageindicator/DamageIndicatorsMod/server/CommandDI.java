package DamageIndicatorsMod.server;

import DamageIndicatorsMod.configuration.DIConfig;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
/* loaded from: input.jar:DamageIndicatorsMod/server/CommandDI.class */
public class CommandDI extends CommandBase {
    public String func_71517_b() {
        return "direload";
    }

    public String func_71518_a(ICommandSender icommandsender) {
        return "commands.damageindicatorsmod.direload";
    }

    public void func_184881_a(MinecraftServer server, ICommandSender icommandsender, String[] astring) {
        if (icommandsender.func_70005_c_().toLowerCase().equals("server")) {
            DIConfig.loadConfig(null);
            for (EntityPlayer player : server.func_184103_al()) {
                if (player != null) {
                    ServerEventHandler.sendServerSettings(player);
                }
            }
        } else if (FMLCommonHandler.instance().getSide().isClient()) {
            ((EntityPlayer) icommandsender).func_145747_a(new TextComponentString("Configuration Reloading"));
            DIConfig.loadConfig(null);
        }
    }
}
