package DamageIndicatorsMod.core;

import DamageIndicatorsMod.DIMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
/* loaded from: input.jar:DamageIndicatorsMod/core/DIPermissions.class */
public class DIPermissions implements IMessage {
    byte message;

    public DIPermissions() {
    }

    public DIPermissions(byte message) {
        this.message = message;
    }

    public void fromBytes(ByteBuf buf) {
        try {
            this.message = buf.readByte();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    public void toBytes(ByteBuf buf) {
        try {
            buf.writeByte(this.message);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    /* loaded from: input.jar:DamageIndicatorsMod/core/DIPermissions$Handler.class */
    public static class Handler implements IMessageHandler<DIPermissions, IMessage> {
        public static boolean allDisabled = false;
        public static boolean mouseOversDisabled = false;
        public static boolean potionEffectsDisabled = false;
        public static boolean popOffsDisabled = false;

        public DIPermissions onMessage(DIPermissions message, MessageContext ctx) {
            processPermissions(DIMod.proxy.getPlayer(), (byte) 0);
            return null;
        }

        public static void processPermissions(EntityPlayer player, byte toggles) {
            allDisabled = (toggles & 1) != 0;
            mouseOversDisabled = (toggles & 2) != 0;
            potionEffectsDisabled = (toggles & 4) != 0;
            popOffsDisabled = (toggles & 8) != 0;
            if (mouseOversDisabled || allDisabled) {
                player.func_145747_a(new TextComponentString("[DamageIndicators] §4Server has disabled mouseovers."));
            } else {
                player.func_145747_a(new TextComponentString("[DamageIndicators] §2Mouseovers enabled."));
            }
            if (potionEffectsDisabled || allDisabled) {
                player.func_145747_a(new TextComponentString("[DamageIndicators] §4Server has disabled potion effects."));
            } else {
                player.func_145747_a(new TextComponentString("[DamageIndicators] §2Potion Effects enabled."));
            }
            if (popOffsDisabled || allDisabled) {
                player.func_145747_a(new TextComponentString("[DamageIndicators] §4Server has disabled damage popoffs."));
            } else {
                player.func_145747_a(new TextComponentString("[DamageIndicators] §2Popoffs enabled."));
            }
        }
    }
}
