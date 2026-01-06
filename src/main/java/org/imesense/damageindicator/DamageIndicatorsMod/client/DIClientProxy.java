package DamageIndicatorsMod.client;

import DITextures.JarSkinRegistration;
import DamageIndicatorsMod.DIMod;
import DamageIndicatorsMod.configuration.DIConfig;
import DamageIndicatorsMod.core.DIEventBus;
import DamageIndicatorsMod.core.Tools;
import DamageIndicatorsMod.rendering.DIWordParticles;
import DamageIndicatorsMod.server.DIProxy;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
/* loaded from: input.jar:DamageIndicatorsMod/client/DIClientProxy.class */
public class DIClientProxy extends DIProxy {

    /* renamed from: kb */
    public static KeyBinding f0kb;
    int wordParticle = 1051414;

    @Override // DamageIndicatorsMod.server.DIProxy
    public void register() {
        super.register();
        DIEventBus seh = new DIEventBus();
        MinecraftForge.EVENT_BUS.register(seh);
        Tools.getInstance().RegisterRenders();
        JarSkinRegistration.init();
        Minecraft.func_71410_x().field_71452_i.func_178929_a(this.wordParticle, particleID, worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, p_178902_15_ -> {
            DIWordParticles customParticle = new DIWordParticles(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
            if (p_178902_15_[0] == 1) {
                customParticle.shouldOnTop = true;
            }
            return customParticle;
        });
    }

    @Override // DamageIndicatorsMod.server.DIProxy
    public void doCritical(Entity target) {
        int shouldbeseen = 0;
        if (Minecraft.func_71410_x().field_71439_g.func_70685_l(target)) {
            shouldbeseen = 1;
        } else if (Minecraft.func_71410_x().func_71356_B()) {
            shouldbeseen = DIConfig.mainInstance().alwaysRender ? 1 : 0;
        }
        if (target != Minecraft.func_71410_x().field_71439_g || Minecraft.func_71410_x().field_71474_y.field_74320_O != 0) {
            Minecraft.func_71410_x().field_71452_i.func_178927_a(this.wordParticle, target.field_70165_t, target.field_70163_u + target.field_70131_O, target.field_70161_v, 0.001d, 0.05f * DIConfig.mainInstance().BounceStrength, 0.001d, new int[]{shouldbeseen});
        }
    }

    @Override // DamageIndicatorsMod.server.DIProxy
    public EntityPlayer getPlayer() {
        return Minecraft.func_71410_x().field_71439_g;
    }

    @Override // DamageIndicatorsMod.server.DIProxy
    public void trysendmessage() {
        try {
            for (ModContainer modContainer : Loader.instance().getModList()) {
                this.dimod = modContainer;
                if (this.dimod != null && this.dimod.getName().equals("Damage Indicators")) {
                    break;
                }
            }
            System.out.println(this.dimod.getMetadata().version);
            new Thread(new Runnable() { // from class: DamageIndicatorsMod.client.DIClientProxy.1
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        InputStreamReader fr = new InputStreamReader(new URL("http://voidswrath.com/release/DamageIndicatorMod.txt").openStream(), "UTF-8");
                        BufferedReader br = new BufferedReader(fr);
                        br.readLine().trim();
                        while (true) {
                            String nextDonater = br.readLine();
                            if (nextDonater == null) {
                                break;
                            }
                            String nextDonater2 = nextDonater.trim();
                            if (!nextDonater2.isEmpty()) {
                                DIMod.donators.add(nextDonater2.toLowerCase());
                            }
                        }
                        br.close();
                        fr.close();
                    } catch (Throwable ex) {
                        ex.printStackTrace();
                        if (DIConfig.mainInstance().checkForUpdates >= 2) {
                            DIMod.s_sUpdateMessage = "Damage Indicators was unable to check for updates!";
                        }
                    }
                    if (DIConfig.mainInstance().checkForUpdates == 0) {
                        DIMod.s_sUpdateMessage = null;
                    }
                }
            }).start();
        } catch (Throwable ex) {
            ex.printStackTrace();
            if (DIConfig.mainInstance().checkForUpdates >= 2) {
                DIMod.s_sUpdateMessage = "Damage Indicators was unable to check for updates!";
            }
        }
    }
}
