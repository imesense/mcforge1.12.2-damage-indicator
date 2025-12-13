package org.imesense.damageindicator.DamageIndicatorsMod.client;

import org.imesense.damageindicator.DITextures.JarSkinRegistration;
import org.imesense.damageindicator.DamageIndicatorMod;
import org.imesense.damageindicator.DamageIndicatorsMod.configuration.DIConfig;
import org.imesense.damageindicator.DamageIndicatorsMod.core.DIEventBus;
import org.imesense.damageindicator.DamageIndicatorsMod.core.Tools;
import org.imesense.damageindicator.DamageIndicatorsMod.rendering.DIWordParticles;
import org.imesense.damageindicator.DamageIndicatorsMod.server.DIProxy;
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
        Minecraft.getMinecraft().effectRenderer.registerParticle(this.wordParticle, (particleID, worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, parameters) -> {
            DIWordParticles customParticle = new DIWordParticles(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
            if (parameters[0] == 1) {
                customParticle.shouldOnTop = true;
            }
            return customParticle;
        });
    }

    @Override // DamageIndicatorsMod.server.DIProxy
    public void doCritical(Entity target) {
        int shouldbeseen = 0;
        if (Minecraft.getMinecraft().player.canEntityBeSeen(target)) {
            shouldbeseen = 1;
        } else if (Minecraft.getMinecraft().isSingleplayer()) {
            shouldbeseen = DIConfig.mainInstance().alwaysRender ? 1 : 0;
        }
        if (target != Minecraft.getMinecraft().player || Minecraft.getMinecraft().gameSettings.thirdPersonView != 0) {
            Minecraft.getMinecraft().effectRenderer.spawnEffectParticle(this.wordParticle, target.posX, target.posY + target.height, target.posZ, 0.001d, 0.05f * DIConfig.mainInstance().BounceStrength, 0.001d, new int[]{shouldbeseen});
        }
    }

    @Override // DamageIndicatorsMod.server.DIProxy
    public EntityPlayer getPlayer() {
        return Minecraft.getMinecraft().player;
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
                                DamageIndicatorMod.donators.add(nextDonater2.toLowerCase());
                            }
                        }
                        br.close();
                        fr.close();
                    } catch (Throwable ex) {
                        ex.printStackTrace();
                        if (DIConfig.mainInstance().checkForUpdates >= 2) {
                            DamageIndicatorMod.s_sUpdateMessage = "Damage Indicators was unable to check for updates!";
                        }
                    }
                    if (DIConfig.mainInstance().checkForUpdates == 0) {
                        DamageIndicatorMod.s_sUpdateMessage = null;
                    }
                }
            }).start();
        } catch (Throwable ex) {
            ex.printStackTrace();
            if (DIConfig.mainInstance().checkForUpdates >= 2) {
                DamageIndicatorMod.s_sUpdateMessage = "Damage Indicators was unable to check for updates!";
            }
        }
    }
}
