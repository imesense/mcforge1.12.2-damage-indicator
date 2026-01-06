package DamageIndicatorsMod.server;

import DamageIndicatorsMod.DIMod;
import DamageIndicatorsMod.configuration.DIConfig;
import DamageIndicatorsMod.core.DIPermissions;
import DamageIndicatorsMod.core.DIPotionEffects;
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
            DIMod.network.sendTo(new DIPermissions((byte) (((byte) (toggles + (!DIConfig.mainInstance().enablePotionEffects ? (byte) 4 : (byte) 0))) + (!DIConfig.mainInstance().popOffsEnabled ? (byte) 8 : (byte) 0))), (EntityPlayerMP) player);
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
        if (DIConfig.mainInstance().enablePotionEffects && evt.getEntityLiving() != null && (potionEffects = el.func_70651_bq()) != null && !potionEffects.isEmpty()) {
            int offset = MathHelper.func_76141_d(DIConfig.mainInstance().packetrange / 2.0f);
            AxisAlignedBB aabb = new AxisAlignedBB(el.field_70165_t - offset, el.field_70163_u - offset, el.field_70161_v - offset, el.field_70165_t + offset, el.field_70163_u + offset, el.field_70161_v + offset);
            List<EntityPlayer> players = el.field_70170_p.func_72872_a(EntityPlayer.class, aabb);
            if (players != null && !players.isEmpty()) {
                for (EntityPlayer entityPlayer : players) {
                    if (potionTimers.get(entityPlayer.func_70005_c_()) == null) {
                        potionTimers.put(entityPlayer.func_70005_c_(), new WeakHashMap());
                    }
                    Map<UUID, Long> potioneffectstimer = potionTimers.get(entityPlayer.func_70005_c_());
                    if (!potioneffectstimer.containsKey(el.getPersistentID()) || System.currentTimeMillis() - potioneffectstimer.get(el.getPersistentID()).longValue() > 1000) {
                        if (entityPlayer instanceof EntityPlayerMP) {
                            DIMod.network.sendTo(new DIPotionEffects(el, getFormattedPotionEffects(el)), (EntityPlayerMP) entityPlayer);
                            potioneffectstimer.put(el.getPersistentID(), Long.valueOf(System.currentTimeMillis()));
                        }
                    }
                }
            }
        }
    }

    public List<PotionEffect> getFormattedPotionEffects(EntityLivingBase el) {
        List<PotionEffect> effects = new ArrayList<>();
        if (el.func_70651_bq() != null && el.func_70651_bq().size() > 0) {
            effects.addAll(el.func_70651_bq());
        }
        return effects;
    }
}
