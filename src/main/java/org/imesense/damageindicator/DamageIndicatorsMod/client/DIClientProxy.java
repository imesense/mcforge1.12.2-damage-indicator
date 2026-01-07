package org.imesense.damageindicator.DamageIndicatorsMod.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

import org.imesense.damageindicator.DITextures.JarSkinRegistration;
import org.imesense.damageindicator.DamageIndicatorsMod.configuration.DIConfig;
import org.imesense.damageindicator.DamageIndicatorsMod.core.DIEventBus;
import org.imesense.damageindicator.DamageIndicatorsMod.core.Tools;
import org.imesense.damageindicator.DamageIndicatorsMod.rendering.DIWordParticles;
import org.imesense.damageindicator.DamageIndicatorsMod.server.DIProxy;

public class DIClientProxy extends DIProxy
{
    public static KeyBinding keyBinding;
    int wordParticle = 1051414;

    @Override
    public void register()
    {
        super.register();
        DIEventBus seh = new DIEventBus();
        MinecraftForge.EVENT_BUS.register(seh);
        Tools.getInstance().RegisterRenders();
        JarSkinRegistration.init();
        Minecraft.getMinecraft().effectRenderer.registerParticle(this.wordParticle, (particleID, worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, parameters) ->
        {
            DIWordParticles customParticle = new DIWordParticles(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
            if (parameters.length > 0 && parameters[0] == 1)
            {
                customParticle.shouldOnTop = true;
            }
            return customParticle;
        });
    }

    @Override
    public void doCritical(Entity target)
    {
        int shouldbeseen = 0;
        if (Minecraft.getMinecraft().player.canEntityBeSeen(target))
        {
            shouldbeseen = 1;
        }
        else if (Minecraft.getMinecraft().isSingleplayer())
        {
            shouldbeseen = DIConfig.alwaysRender ? 1 : 0;
        }
        if (target != Minecraft.getMinecraft().player || Minecraft.getMinecraft().gameSettings.thirdPersonView != 0)
        {
            Minecraft.getMinecraft().effectRenderer.spawnEffectParticle(this.wordParticle, target.posX, target.posY + target.height, target.posZ, 0.001d, 0.05f * DIConfig.BounceStrength, 0.001d, shouldbeseen);
        }
    }

    @Override // DamageIndicatorsMod.server.DIProxy
    public EntityPlayer getPlayer()
    {
        return Minecraft.getMinecraft().player;
    }

    @Override // DamageIndicatorsMod.server.DIProxy
    public void trysendmessage()
    {
    }
}
