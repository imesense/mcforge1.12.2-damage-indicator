package org.imesense.damageindicator.DamageIndicatorsMod.core;

import org.imesense.damageindicator.DITextures.AbstractSkin;
import org.imesense.damageindicator.DITextures.EnumSkinPart;
import org.imesense.damageindicator.DamageIndicatorMod;
import org.imesense.damageindicator.DamageIndicatorsMod.client.DIClientProxy;
import org.imesense.damageindicator.DamageIndicatorsMod.configuration.DIConfig;
import org.imesense.damageindicator.DamageIndicatorsMod.gui.DIGuiTools;
import org.imesense.damageindicator.DamageIndicatorsMod.gui.RepositionGui;
import org.imesense.damageindicator.DamageIndicatorsMod.rendering.DIWordParticles;
import org.imesense.damageindicator.DamageIndicatorsMod.util.RaytraceUtil;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.opengl.GL11;
/* loaded from: input.jar:DamageIndicatorsMod/core/DIEventBus.class */
public class DIEventBus {
    double count = 5.0d;
    boolean skip = false;
    float test;
    static Entity last;
    public static Random rnd = new Random(1051414);
    public static HashMap<Integer, Integer> healths = new HashMap<>();
    public static Map<Integer, Collection<PotionEffect>> potionEffects = new HashMap();
    public static List<Integer> enemies = new ArrayList();
    public static int playerDim = 0;
    public static String playerName = "";
    public static String lastServer = "";
    public static String currentTexturePack = "";
    public static int dim = -2;
    public static int LastTargeted = 0;
    public static boolean searched = false;
    public static double tick = 0.0d;
    public static int updateSkip = 4;
    private static long time = -1;
    public static Map<String, Map<UUID, Long>> potionTimers = new HashMap();

    @SubscribeEvent
    public void arrowNook(LivingHurtEvent evt) {
        EntityArrow arrow;
        if (FMLCommonHandler.instance().getEffectiveSide().isClient() && evt.getEntityLiving() != null && (evt.getSource() instanceof EntityDamageSourceIndirect) && (evt.getSource().getImmediateSource() instanceof EntityArrow) && (arrow = (EntityArrow) evt.getSource().getImmediateSource()) != null && arrow.getIsCritical()) {
            DamageIndicatorMod.proxy.doCritical(evt.getEntityLiving());
        }
    }

    @SubscribeEvent
    public void onLivingUpdateEvent(LivingDeathEvent evt) {
    }

    @SubscribeEvent
    public void onLivingUpdateEvent(LivingEvent.LivingUpdateEvent evt) {
        EntityConfigurationEntry configentry = Tools.getInstance().getEntityMap().get(evt.getEntityLiving().getClass());
        if (configentry != null && configentry.DisableMob) {
            evt.getEntityLiving().setDead();
        }
    }

    private void updateHealth(EntityLivingBase el, int currentHealth) {
        int lastHealth;
        if (healths.containsKey(Integer.valueOf(el.getEntityId())) && (lastHealth = healths.get(Integer.valueOf(el.getEntityId())).intValue()) != currentHealth) {
            int damage = lastHealth - currentHealth;
            DIWordParticles customParticle = new DIWordParticles(Minecraft.getMinecraft().world, el.posX, el.posY + el.height, el.posZ, 0.001d, 0.05f * DIConfig.BounceStrength, 0.001d, damage);
            if (Minecraft.getMinecraft().player.canEntityBeSeen(el)) {
                customParticle.shouldOnTop = true;
            } else if (Minecraft.getMinecraft().isSingleplayer()) {
                customParticle.shouldOnTop = DIConfig.alwaysRender;
            }
            if (el != Minecraft.getMinecraft().player || Minecraft.getMinecraft().gameSettings.thirdPersonView != 0) {
                Minecraft.getMinecraft().effectRenderer.addEffect(customParticle);
            }
        }
        healths.put(Integer.valueOf(el.getEntityId()), Integer.valueOf(currentHealth));
    }

    @SubscribeEvent
    public void entityDeath(LivingDeathEvent evt) {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient() && DIConfig.popOffsEnabled) {
            updateHealth(evt.getEntityLiving(), 0);
        }
        Object entityID = Integer.valueOf(evt.getEntity().getEntityId());
        potionEffects.remove((Integer) entityID);
        healths.remove((Integer) entityID);
        enemies.remove((Integer) entityID);
    }

    @SubscribeEvent
    public void livingUpdate(RenderPlayerEvent.Pre evt) {
        EntityPlayer el = (EntityPlayer) evt.getEntityLiving();
        this.count -= evt.getPartialRenderTick();
        EntityPlayer p = evt.getEntityPlayer();
        if (p != null && p.world != null && (el instanceof EntityPlayer) && DamageIndicatorMod.donators.contains(el.getName().trim().toLowerCase()) && (el != p || Minecraft.getMinecraft().gameSettings.thirdPersonView != 0)) {
            if (el.getName().equals(Minecraft.getMinecraft().player.getName())) {
                el = Minecraft.getMinecraft().player;
            }
            if (!(el instanceof AbstractClientPlayer) || ((AbstractClientPlayer) el).getLocationCape() == null) {
            }
            if (this.count <= 0.0d) {
                this.count = rnd.nextDouble() * 10.0d;
                double darkness = rnd.nextDouble() * 0.333d;
                double red = Math.min(0.75d + darkness, 1.0d);
                double green = red * 0.75d;
                if (Minecraft.getMinecraft().inGameHasFocus) {
                    el.world.spawnParticle(EnumParticleTypes.REDSTONE, el.posX + ((rnd.nextDouble() * 1.25d) - 1.0d), el.posY + ((rnd.nextDouble() * 1.25d) - 1.5d), el.posZ + ((rnd.nextDouble() * 1.25d) - 1.0d), red, green, 0.0d, new int[0]);
                }
            }
        }
        if (evt.getEntity().isDead) {
            try {
                potionEffects.remove(Integer.valueOf(evt.getEntity().getEntityId()));
            } catch (Throwable th) {
            }
            try {
                healths.remove(Integer.valueOf(evt.getEntity().getEntityId()));
            } catch (Throwable th2) {
            }
            try {
                enemies.remove(Integer.valueOf(evt.getEntity().getEntityId()));
            } catch (Throwable th3) {
            }
        }
    }

    @SubscribeEvent
    public void attackEntity(AttackEntityEvent evt) {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT && evt.getEntityLiving() != null && evt.getEntityPlayer() != null) {
            boolean flag = (evt.getEntityPlayer().fallDistance <= 0.0f || evt.getEntityPlayer().onGround || evt.getEntityPlayer().isOnLadder() || evt.getEntityPlayer().isInWater() || evt.getEntityPlayer().getRidingEntity() != null) ? false : true;
            if (flag) {
                DamageIndicatorMod.proxy.doCritical(evt.getTarget());
            }
        }
    }

    @SubscribeEvent
    public void livingEvent(LivingEvent.LivingUpdateEvent evt) {
        EntityPlayerSP entityPlayerSP;
        if (!"".equals(DamageIndicatorMod.s_sUpdateMessage)) {
            if (FMLCommonHandler.instance().getSide().isServer()) {
                DamageIndicatorMod.logger.info(DamageIndicatorMod.s_sUpdateMessage);
                DamageIndicatorMod.s_sUpdateMessage = "";
            } else if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
                Minecraft.getMinecraft().player.sendMessage(new TextComponentString(DamageIndicatorMod.s_sUpdateMessage));
                DamageIndicatorMod.s_sUpdateMessage = "";
            }
        }
        EntityLivingBase el = evt.getEntityLiving();
        if (FMLCommonHandler.instance().getEffectiveSide().isClient() && (entityPlayerSP = Minecraft.getMinecraft().player) != null && ((EntityPlayer) entityPlayerSP).world != null && (el instanceof EntityPlayer) && DamageIndicatorMod.donators.contains(((EntityPlayer) el).getName().trim().toLowerCase()) && (el != entityPlayerSP || Minecraft.getMinecraft().gameSettings.thirdPersonView != 0)) {
            if (el.getName().equals(Minecraft.getMinecraft().player.getName())) {
                el = Minecraft.getMinecraft().player;
            }
            if (el instanceof AbstractClientPlayer) {
            }
            if (!this.skip) {
                this.skip = !this.skip;
                double darkness = rnd.nextDouble() * 0.333d;
                double red = Math.min(0.75d + darkness, 1.0d);
                double green = red * 0.75d;
                if (Minecraft.getMinecraft().inGameHasFocus) {
                    el.world.spawnParticle(EnumParticleTypes.REDSTONE, el.posX + ((rnd.nextDouble() * 1.25d) - 1.0d), el.posY + ((rnd.nextDouble() * 1.25d) - 1.5d), el.posZ + ((rnd.nextDouble() * 1.25d) - 1.0d), red, green, 0.0d, new int[0]);
                }
            }
        }
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            if (DIConfig.popOffsEnabled) {
                updateHealth(el, MathHelper.ceil(el.getHealth()));
            }
            if (evt.getEntity().isDead) {
                try {
                    potionEffects.remove(Integer.valueOf(evt.getEntity().getEntityId()));
                } catch (Throwable th) {
                }
                try {
                    healths.remove(Integer.valueOf(evt.getEntity().getEntityId()));
                } catch (Throwable th2) {
                }
                try {
                    enemies.remove(evt.getEntity().getEntityId());
                } catch (Throwable th3) {
                }
            }
        }
    }

    @SubscribeEvent
    public void changeDimension(EntityJoinWorldEvent evt) {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            try {
                if (evt.getEntity() == Minecraft.getMinecraft().player) {
                    potionEffects.clear();
                    healths.clear();
                    enemies.clear();
                    playerDim = Minecraft.getMinecraft().player.dimension;
                    playerName = Minecraft.getMinecraft().player.getName();
                }
            } catch (Throwable th) {
            }
        }
    }

    @SubscribeEvent
    public void mobHurtUs(LivingHurtEvent evt) {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient() && (evt.getEntity() instanceof EntityPlayer)) {
            EntityPlayer player = (EntityPlayer) evt.getEntity();
            if (player.getName().equals(playerName) && evt.getSource() != null && evt.getSource().getTrueSource() != null) {
                enemies.add(Integer.valueOf(evt.getSource().getTrueSource().getEntityId()));
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void rendergui(RenderGameOverlayEvent.Pre event) {
        EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
        if (entityPlayerSP != null && ((EntityPlayer) entityPlayerSP).world != null && DamageIndicatorMod.donators.contains(entityPlayerSP.getName().trim().toLowerCase()) && Minecraft.getMinecraft().gameSettings.thirdPersonView != 0) {
            if (entityPlayerSP instanceof AbstractClientPlayer) {
            }
            if (this.count <= 0.0d) {
                this.count = rnd.nextDouble() * 10.0d;
                double darkness = rnd.nextDouble() * 0.333d;
                double red = Math.min(0.75d + darkness, 1.0d);
                double green = red * 0.75d;
                if (Minecraft.getMinecraft().inGameHasFocus) {
                    ((EntityPlayer) entityPlayerSP).world.spawnParticle(EnumParticleTypes.REDSTONE, ((EntityPlayer) entityPlayerSP).posX + ((rnd.nextDouble() * 1.25d) - 1.0d), ((EntityPlayer) entityPlayerSP).posY + ((rnd.nextDouble() * 1.25d) - 1.5d), ((EntityPlayer) entityPlayerSP).posZ + ((rnd.nextDouble() * 1.25d) - 1.0d), red, green, 0.0d, new int[0]);
                }
            }
        }
        if (Minecraft.isGuiEnabled() && DIClientProxy.f0kb == null) {
            DIClientProxy.f0kb = new KeyBinding("key.portaitreposition", 52, "key.categories.ui");
            ClientRegistry.registerKeyBinding(DIClientProxy.f0kb);
            KeyBinding.resetKeyBindingArrayAndHash();
        }
        if (DIClientProxy.f0kb.isPressed()) {
            RepositionGui gui = new RepositionGui();
            Minecraft.getMinecraft().displayGuiScreen(gui);
        }
        boolean flag = DIConfig.alternateRenderingMethod && event.getType() == RenderGameOverlayEvent.ElementType.CHAT;
        if (!flag) {
            flag = event.getType() == RenderGameOverlayEvent.ElementType.PORTAL && !DIConfig.alternateRenderingMethod;
        }
        if (event.getType() == RenderGameOverlayEvent.ElementType.BOSSHEALTH && DIConfig.supressBossUI) {
            if (event.isCancelable()) {
                event.setCanceled(true);
            }
        } else if (flag && Minecraft.getMinecraft().player != null) {
            if (Minecraft.getMinecraft().gameSettings.hideGUI) {
                LastTargeted = 0;
            } else if (Minecraft.getMinecraft().gameSettings.showDebugInfo && DIConfig.DebugHidesWindow) {
                LastTargeted = 0;
            } else if (Minecraft.getMinecraft().currentScreen != null && !(Minecraft.getMinecraft().currentScreen instanceof GuiChat)) {
                LastTargeted = 0;
            } else {
                if (!searched) {
                    Tools.getInstance().giveUpdateInformation();
                    Tools.getInstance().scanforEntities();
                    searched = true;
                }
                try {
                    if (DIConfig.portraitEnabled && !DIPermissions.Handler.allDisabled && !DIPermissions.Handler.mouseOversDisabled) {
                        if (DIConfig.highCompatibilityMod) {
                            GL11.glPushAttrib(1048575);
                            GL11.glPushClientAttrib(-1);
                        }
                        updateMouseOversSkinned(0.5f);
                        if (DIConfig.highCompatibilityMod) {
                            GL11.glPopClientAttrib();
                            GL11.glPopAttrib();
                        }
                    }
                } catch (Throwable ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static void updateMouseOversSkinned(float ticks) {
        String Name;
        Entity tmp;
        if (time == -1) {
            time = System.nanoTime();
        }
        double elapsedTime = (System.nanoTime() - time) / 1.0E7d;
        time = System.nanoTime();
        if (Minecraft.getMinecraft().player != null) {
            EntityLivingBase el = null;
            int i = updateSkip;
            updateSkip = i - 1;
            if (i <= 0) {
                updateSkip = 4;
                el = RaytraceUtil.getClosestLivingEntity(Minecraft.getMinecraft().player, DIConfig.mouseoverRange);
                if (el != null && el.getHealth() <= 0.0f) {
                    el = null;
                }
            }
            if (Minecraft.getMinecraft().player.getName().contains("rich1051414") && Minecraft.getMinecraft().player.isSneaking() && (tmp = RaytraceUtil.getClosestEntity(Minecraft.getMinecraft().player, DIConfig.mouseoverRange)) != null && tmp != last) {
                last = tmp;
                Minecraft.getMinecraft().player.sendMessage(new TextComponentString(tmp.getClass().getName()));
                TextTransfer textTransfer = new TextTransfer();
                textTransfer.setClipboardContents(tmp.getClass().getName());
            }
            if (el != null) {
                Class<?> cls = el.getClass();
                EntityConfigurationEntry configentry = Tools.getInstance().getEntityMap().get(cls);
                if (configentry == null) {
                    Configuration configfile = EntityConfigurationEntry.getEntityConfiguration();
                    configentry = EntityConfigurationEntry.generateDefaultConfiguration(configfile, cls);
                    configentry.save();
                    Tools.getInstance().getEntityMap().put(cls, configentry);
                }
                if (configentry.IgnoreThisMob) {
                    el = null;
                } else {
                    LastTargeted = el.getEntityId();
                }
            }
            if (el == null) {
                if (LastTargeted == 0) {
                    return;
                }
                if (DIConfig.portraitLifetime != -1 && tick <= 0.0d) {
                    return;
                }
            }
            ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
            if (DIConfig.locX > scaledresolution.getScaledWidth() - 135) {
                DIConfig.locX = scaledresolution.getScaledWidth() - 135;
            }
            if (DIConfig.locY > scaledresolution.getScaledHeight() - 50) {
                DIConfig.locY = scaledresolution.getScaledHeight() - 50;
            }
            if (DIConfig.locX < 0) {
                DIConfig.locX = 0;
            }
            if (DIConfig.locY < 0) {
                DIConfig.locY = 0;
            }
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            if (el == null) {
                tick -= elapsedTime;
                try {
                    el = (EntityLivingBase) Minecraft.getMinecraft().world.getEntityByID(LastTargeted);
                } catch (Throwable th) {
                    el = null;
                }
                if (el == null) {
                    LastTargeted = 0;
                }
            } else {
                tick = DIConfig.portraitLifetime;
            }
            if (el == null) {
                return;
            }
            LastTargeted = el.getEntityId();
            EntityConfigurationEntry configentry2 = Tools.getInstance().getEntityMap().get(el.getClass());
            if (configentry2.maxHP == -1 || configentry2.eyeHeight == -1.0f) {
                configentry2.eyeHeight = el.getEyeHeight();
                configentry2.maxHP = MathHelper.floor(Math.ceil(el.getMaxHealth()));
            }
            if (configentry2.maxHP != MathHelper.floor(Math.ceil(el.getMaxHealth()))) {
                configentry2.maxHP = MathHelper.floor(Math.ceil(el.getMaxHealth()));
            }
            String Name2 = configentry2.NameOverride;
            if (el instanceof EntityPlayer) {
                Name2 = el.getName();
            }
            if (Name2 == null || "".equals(Name2)) {
                Name = el.getName();
                if (Name.endsWith(".name")) {
                    String Name3 = Name.replace(".name", "");
                    String Name4 = Name3.substring(Name3.lastIndexOf(".") + 1, Name3.length());
                    Name = Name4.substring(0, 1).toUpperCase() + Name4.substring(1, Name4.length());
                }
                if (el.isChild() && configentry2.AppendBaby) {
                    Name = "Baby " + Name;
                }
            } else if (el.isChild() && configentry2.AppendBaby) {
                Name = "§oBaby " + Name2;
            } else {
                Name = "§o" + Name2;
            }
            GL11.glPushMatrix();
            GL11.glTranslatef((1.0f - DIConfig.guiScale) * DIConfig.locX, (1.0f - DIConfig.guiScale) * DIConfig.locY, 0.0f);
            GL11.glScalef(DIConfig.guiScale, DIConfig.guiScale, DIConfig.guiScale);
            try {
                DIGuiTools.DrawPortraitSkinned(DIConfig.locX, DIConfig.locY, Name, MathHelper.ceil(el.getHealth()), MathHelper.ceil(el.getMaxHealth()), el);
                if (Calendar.getInstance().getWeekYear() + 3 > Calendar.getInstance().getWeeksInWeekYear()) {
                    FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
                    int intValue = ((Integer) AbstractSkin.getActiveSkin().getSkinValue(EnumSkinPart.CONFIGFRAMEY)).intValue() + 75;
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                }
            } catch (Throwable th2) {
            }
            GL11.glPopMatrix();
            OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
            GL11.glDisableClientState(32888);
            OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    public List<PotionEffect> getFormattedPotionEffects(EntityLivingBase el) {
        List<PotionEffect> effects = new ArrayList<>();
        if (el.getActivePotionEffects() != null && el.getActivePotionEffects().size() > 0) {
            effects.addAll(el.getActivePotionEffects());
        }
        return effects;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            DamageIndicatorMod.proxy.trysendmessage();
        }
    }
}
