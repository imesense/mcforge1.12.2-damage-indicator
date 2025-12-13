package org.imesense.damageindicator.DamageIndicatorsMod.server;

import org.imesense.damageindicator.DamageIndicatorMod;
import org.imesense.damageindicator.DamageIndicatorsMod.configuration.DIConfig;
import org.imesense.damageindicator.DamageIndicatorsMod.core.DIPermissions;
import org.imesense.damageindicator.DamageIndicatorsMod.core.DIPotionEffects;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
/* loaded from: input.jar:DamageIndicatorsMod/server/ServerEventHandler.class */
public class ServerEventHandler {
    public static Map<String, Map<UUID, Long>> potionTimers = new HashMap();

    public static void sendServerSettings(EntityPlayer player) {
        if (player instanceof EntityPlayerMP) {
            byte toggles = (byte) (0 + (!DIConfig.mainInstance().portraitEnabled ? 2 : 0));
            DamageIndicatorMod.network.sendTo(new DIPermissions((byte) (((byte) (toggles + (!DIConfig.mainInstance().enablePotionEffects ? (byte) 4 : (byte) 0))) + (!DIConfig.mainInstance().popOffsEnabled ? (byte) 8 : (byte) 0))), (EntityPlayerMP) player);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            sendServerSettings(event.player);
        }
    }

    @SubscribeEvent
    public void livingEvent(LivingEvent.LivingUpdateEvent evt) {
        Collection<?> potionEffects;
        EntityLivingBase el = evt.getEntityLiving();
        if (DIConfig.mainInstance().enablePotionEffects && evt.getEntityLiving() != null && (potionEffects = el.getActivePotionEffects()) != null && !potionEffects.isEmpty()) {
            int offset = MathHelper.floor(DIConfig.mainInstance().packetrange / 2.0f);
            AxisAlignedBB aabb = new AxisAlignedBB(el.posX - offset, el.posY - offset, el.posZ - offset, el.posX + offset, el.posY + offset, el.posZ + offset);
            List<EntityPlayer> players = el.world.getEntitiesWithinAABB(EntityPlayer.class, aabb);
            if (players != null && !players.isEmpty()) {
                for (EntityPlayer entityPlayer : players) {
                    if (potionTimers.get(entityPlayer.getName()) == null) {
                        potionTimers.put(entityPlayer.getName(), new WeakHashMap());
                    }
                    Map<UUID, Long> potioneffectstimer = potionTimers.get(entityPlayer.getName());
                    if (!potioneffectstimer.containsKey(el.getPersistentID()) || System.currentTimeMillis() - potioneffectstimer.get(el.getPersistentID()).longValue() > 1000) {
                        if (entityPlayer instanceof EntityPlayerMP) {
                            DamageIndicatorMod.network.sendTo(new DIPotionEffects(el, getFormattedPotionEffects(el)), (EntityPlayerMP) entityPlayer);
                            potioneffectstimer.put(el.getPersistentID(), Long.valueOf(System.currentTimeMillis()));
                        }
                    }
                }
            }
        }
    }

    public List<PotionEffect> getFormattedPotionEffects(EntityLivingBase el) {
        List<PotionEffect> effects = new ArrayList<>();
        if (el.getActivePotionEffects() != null && el.getActivePotionEffects().size() > 0) {
            effects.addAll(el.getActivePotionEffects());
        }
        return effects;
    }
}
