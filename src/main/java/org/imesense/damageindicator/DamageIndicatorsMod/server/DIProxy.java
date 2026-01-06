package DamageIndicatorsMod.server;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
/* loaded from: input.jar:DamageIndicatorsMod/server/DIProxy.class */
public class DIProxy {
    public ModContainer dimod;

    public void register() {
        ServerEventHandler seh = new ServerEventHandler();
        MinecraftForge.EVENT_BUS.register(seh);
        if (this.dimod == null) {
            for (ModContainer modContainer : Loader.instance().getModList()) {
                this.dimod = modContainer;
                if (this.dimod != null && this.dimod.getName().equals("Damage Indicators")) {
                    return;
                }
            }
        }
    }

    public void doCritical(Entity target) {
    }

    public void trysendmessage() {
    }

    public EntityPlayer getPlayer() {
        return null;
    }
}
