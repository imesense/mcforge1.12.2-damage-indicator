package DamageIndicatorsMod.core;

import DITextures.AbstractSkin;
import DITextures.EnumSkinPart;
import DamageIndicatorsMod.DIMod;
import DamageIndicatorsMod.client.DIClientProxy;
import DamageIndicatorsMod.configuration.DIConfig;
import DamageIndicatorsMod.core.DIPermissions;
import DamageIndicatorsMod.gui.DIGuiTools;
import DamageIndicatorsMod.gui.RepositionGui;
import DamageIndicatorsMod.rendering.DIWordParticles;
import DamageIndicatorsMod.util.RaytraceUtil;
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
        if (FMLCommonHandler.instance().getEffectiveSide().isClient() && evt.getEntityLiving() != null && (evt.getSource() instanceof EntityDamageSourceIndirect) && (evt.getSource().func_76364_f() instanceof EntityArrow) && (arrow = evt.getSource().func_76364_f()) != null && arrow.func_70241_g()) {
            DIMod.proxy.doCritical(evt.getEntityLiving());
        }
    }

    @SubscribeEvent
    public void onLivingUpdateEvent(LivingDeathEvent evt) {
    }

    @SubscribeEvent
    public void onLivingUpdateEvent(LivingEvent.LivingUpdateEvent evt) {
        EntityConfigurationEntry configentry = Tools.getInstance().getEntityMap().get(evt.getEntityLiving().getClass());
        if (configentry != null && configentry.DisableMob) {
            evt.getEntityLiving().func_70106_y();
        }
    }

    private void updateHealth(EntityLivingBase el, int currentHealth) {
        int lastHealth;
        if (healths.containsKey(Integer.valueOf(el.func_145782_y())) && (lastHealth = healths.get(Integer.valueOf(el.func_145782_y())).intValue()) != currentHealth) {
            int damage = lastHealth - currentHealth;
            DIWordParticles customParticle = new DIWordParticles(Minecraft.func_71410_x().field_71441_e, el.field_70165_t, el.field_70163_u + el.field_70131_O, el.field_70161_v, 0.001d, 0.05f * DIConfig.mainInstance().BounceStrength, 0.001d, damage);
            if (Minecraft.func_71410_x().field_71439_g.func_70685_l(el)) {
                customParticle.shouldOnTop = true;
            } else if (Minecraft.func_71410_x().func_71356_B()) {
                customParticle.shouldOnTop = DIConfig.mainInstance().alwaysRender;
            }
            if (el != Minecraft.func_71410_x().field_71439_g || Minecraft.func_71410_x().field_71474_y.field_74320_O != 0) {
                Minecraft.func_71410_x().field_71452_i.func_78873_a(customParticle);
            }
        }
        healths.put(Integer.valueOf(el.func_145782_y()), Integer.valueOf(currentHealth));
    }

    @SubscribeEvent
    public void entityDeath(LivingDeathEvent evt) {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient() && DIConfig.mainInstance().popOffsEnabled) {
            updateHealth(evt.getEntityLiving(), 0);
        }
        Object entityID = Integer.valueOf(evt.getEntity().func_145782_y());
        potionEffects.remove((Integer) entityID);
        healths.remove((Integer) entityID);
        enemies.remove((Integer) entityID);
    }

    @SubscribeEvent
    public void livingUpdate(RenderPlayerEvent.Pre evt) {
        EntityPlayer el = evt.getEntityLiving();
        this.count -= evt.getPartialRenderTick();
        EntityPlayer p = evt.getEntityPlayer();
        if (p != null && p.field_70170_p != null && (el instanceof EntityPlayer) && DIMod.donators.contains(el.func_70005_c_().trim().toLowerCase()) && (el != p || Minecraft.func_71410_x().field_71474_y.field_74320_O != 0)) {
            if (el.func_70005_c_().equals(Minecraft.func_71410_x().field_71439_g.func_70005_c_())) {
                el = Minecraft.func_71410_x().field_71439_g;
            }
            if (!(el instanceof AbstractClientPlayer) || el.func_110303_q() == null) {
            }
            if (this.count <= 0.0d) {
                this.count = rnd.nextDouble() * 10.0d;
                double darkness = rnd.nextDouble() * 0.333d;
                double red = Math.min(0.75d + darkness, 1.0d);
                double green = red * 0.75d;
                if (Minecraft.func_71410_x().field_71415_G) {
                    el.field_70170_p.func_175688_a(EnumParticleTypes.REDSTONE, el.field_70165_t + ((rnd.nextDouble() * 1.25d) - 1.0d), el.field_70163_u + ((rnd.nextDouble() * 1.25d) - 1.5d), el.field_70161_v + ((rnd.nextDouble() * 1.25d) - 1.0d), red, green, 0.0d, new int[0]);
                }
            }
        }
        if (evt.getEntity().field_70128_L) {
            try {
                potionEffects.remove(Integer.valueOf(evt.getEntity().func_145782_y()));
            } catch (Throwable th) {
            }
            try {
                healths.remove(Integer.valueOf(evt.getEntity().func_145782_y()));
            } catch (Throwable th2) {
            }
            try {
                enemies.remove(Integer.valueOf(evt.getEntity().func_145782_y()));
            } catch (Throwable th3) {
            }
        }
    }

    @SubscribeEvent
    public void attackEntity(AttackEntityEvent evt) {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT && evt.getEntityLiving() != null && evt.getEntityPlayer() != null) {
            boolean flag = (evt.getEntityPlayer().field_70143_R <= 0.0f || evt.getEntityPlayer().field_70122_E || evt.getEntityPlayer().func_70617_f_() || evt.getEntityPlayer().func_70090_H() || evt.getEntityPlayer().func_184187_bx() != null) ? false : true;
            if (flag) {
                DIMod.proxy.doCritical(evt.getTarget());
            }
        }
    }

    @SubscribeEvent
    public void livingEvent(LivingEvent.LivingUpdateEvent evt) {
        EntityPlayerSP entityPlayerSP;
        if (!"".equals(DIMod.s_sUpdateMessage)) {
            if (FMLCommonHandler.instance().getSide().isServer()) {
                DIMod.log.info(DIMod.s_sUpdateMessage);
                DIMod.s_sUpdateMessage = "";
            } else if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
                Minecraft.func_71410_x().field_71439_g.func_145747_a(new TextComponentString(DIMod.s_sUpdateMessage));
                DIMod.s_sUpdateMessage = "";
            }
        }
        EntityLivingBase el = evt.getEntityLiving();
        if (FMLCommonHandler.instance().getEffectiveSide().isClient() && (entityPlayerSP = Minecraft.func_71410_x().field_71439_g) != null && ((EntityPlayer) entityPlayerSP).field_70170_p != null && (el instanceof EntityPlayer) && DIMod.donators.contains(((EntityPlayer) el).func_70005_c_().trim().toLowerCase()) && (el != entityPlayerSP || Minecraft.func_71410_x().field_71474_y.field_74320_O != 0)) {
            if (el.func_70005_c_().equals(Minecraft.func_71410_x().field_71439_g.func_70005_c_())) {
                el = Minecraft.func_71410_x().field_71439_g;
            }
            if (el instanceof AbstractClientPlayer) {
            }
            if (!this.skip) {
                this.skip = !this.skip;
                double darkness = rnd.nextDouble() * 0.333d;
                double red = Math.min(0.75d + darkness, 1.0d);
                double green = red * 0.75d;
                if (Minecraft.func_71410_x().field_71415_G) {
                    el.field_70170_p.func_175688_a(EnumParticleTypes.REDSTONE, el.field_70165_t + ((rnd.nextDouble() * 1.25d) - 1.0d), el.field_70163_u + ((rnd.nextDouble() * 1.25d) - 1.5d), el.field_70161_v + ((rnd.nextDouble() * 1.25d) - 1.0d), red, green, 0.0d, new int[0]);
                }
            }
        }
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            if (DIConfig.mainInstance().popOffsEnabled) {
                updateHealth(el, MathHelper.func_76123_f(el.func_110143_aJ()));
            }
            if (evt.getEntity().field_70128_L) {
                try {
                    potionEffects.remove(Integer.valueOf(evt.getEntity().func_145782_y()));
                } catch (Throwable th) {
                }
                try {
                    healths.remove(Integer.valueOf(evt.getEntity().func_145782_y()));
                } catch (Throwable th2) {
                }
                try {
                    enemies.remove(evt.getEntity().func_145782_y());
                } catch (Throwable th3) {
                }
            }
        }
    }

    @SubscribeEvent
    public void changeDimension(EntityJoinWorldEvent evt) {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            try {
                if (evt.getEntity() == Minecraft.func_71410_x().field_71439_g) {
                    potionEffects.clear();
                    healths.clear();
                    enemies.clear();
                    playerDim = Minecraft.func_71410_x().field_71439_g.field_71093_bK;
                    playerName = Minecraft.func_71410_x().field_71439_g.func_70005_c_();
                }
            } catch (Throwable th) {
            }
        }
    }

    @SubscribeEvent
    public void mobHurtUs(LivingHurtEvent evt) {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient() && (evt.getEntity() instanceof EntityPlayer)) {
            EntityPlayer player = evt.getEntity();
            if (player.func_70005_c_().equals(playerName) && evt.getSource() != null && evt.getSource().func_76346_g() != null) {
                enemies.add(Integer.valueOf(evt.getSource().func_76346_g().func_145782_y()));
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void rendergui(RenderGameOverlayEvent.Pre event) {
        EntityPlayerSP entityPlayerSP = Minecraft.func_71410_x().field_71439_g;
        if (entityPlayerSP != null && ((EntityPlayer) entityPlayerSP).field_70170_p != null && DIMod.donators.contains(entityPlayerSP.func_70005_c_().trim().toLowerCase()) && Minecraft.func_71410_x().field_71474_y.field_74320_O != 0) {
            if (entityPlayerSP instanceof AbstractClientPlayer) {
            }
            if (this.count <= 0.0d) {
                this.count = rnd.nextDouble() * 10.0d;
                double darkness = rnd.nextDouble() * 0.333d;
                double red = Math.min(0.75d + darkness, 1.0d);
                double green = red * 0.75d;
                if (Minecraft.func_71410_x().field_71415_G) {
                    ((EntityPlayer) entityPlayerSP).field_70170_p.func_175688_a(EnumParticleTypes.REDSTONE, ((EntityPlayer) entityPlayerSP).field_70165_t + ((rnd.nextDouble() * 1.25d) - 1.0d), ((EntityPlayer) entityPlayerSP).field_70163_u + ((rnd.nextDouble() * 1.25d) - 1.5d), ((EntityPlayer) entityPlayerSP).field_70161_v + ((rnd.nextDouble() * 1.25d) - 1.0d), red, green, 0.0d, new int[0]);
                }
            }
        }
        if (Minecraft.func_71382_s() && DIClientProxy.f0kb == null) {
            DIClientProxy.f0kb = new KeyBinding("key.portaitreposition", 52, "key.categories.ui");
            ClientRegistry.registerKeyBinding(DIClientProxy.f0kb);
            KeyBinding.func_74508_b();
        }
        if (DIClientProxy.f0kb.func_151468_f()) {
            RepositionGui gui = new RepositionGui();
            Minecraft.func_71410_x().func_147108_a(gui);
        }
        boolean flag = DIConfig.mainInstance().alternateRenderingMethod && event.getType() == RenderGameOverlayEvent.ElementType.CHAT;
        if (!flag) {
            flag = event.getType() == RenderGameOverlayEvent.ElementType.PORTAL && !DIConfig.mainInstance().alternateRenderingMethod;
        }
        if (event.getType() == RenderGameOverlayEvent.ElementType.BOSSHEALTH && DIConfig.mainInstance().supressBossUI) {
            if (event.isCancelable()) {
                event.setCanceled(true);
            }
        } else if (flag && Minecraft.func_71410_x().field_71439_g != null) {
            if (Minecraft.func_71410_x().field_71474_y.field_74319_N) {
                LastTargeted = 0;
            } else if (Minecraft.func_71410_x().field_71474_y.field_74330_P && DIConfig.mainInstance().DebugHidesWindow) {
                LastTargeted = 0;
            } else if (Minecraft.func_71410_x().field_71462_r != null && !(Minecraft.func_71410_x().field_71462_r instanceof GuiChat)) {
                LastTargeted = 0;
            } else {
                if (!searched) {
                    Tools.getInstance().giveUpdateInformation();
                    Tools.getInstance().scanforEntities();
                    searched = true;
                }
                try {
                    if (DIConfig.mainInstance().portraitEnabled && !DIPermissions.Handler.allDisabled && !DIPermissions.Handler.mouseOversDisabled) {
                        if (DIConfig.mainInstance().highCompatibilityMod) {
                            GL11.glPushAttrib(1048575);
                            GL11.glPushClientAttrib(-1);
                        }
                        updateMouseOversSkinned(0.5f);
                        if (DIConfig.mainInstance().highCompatibilityMod) {
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
        if (Minecraft.func_71410_x().field_71439_g != null) {
            EntityLivingBase el = null;
            int i = updateSkip;
            updateSkip = i - 1;
            if (i <= 0) {
                updateSkip = 4;
                el = RaytraceUtil.getClosestLivingEntity(Minecraft.func_71410_x().field_71439_g, DIConfig.mainInstance().mouseoverRange);
                if (el != null && el.func_110143_aJ() <= 0.0f) {
                    el = null;
                }
            }
            if (Minecraft.func_71410_x().field_71439_g.func_70005_c_().contains("rich1051414") && Minecraft.func_71410_x().field_71439_g.func_70093_af() && (tmp = RaytraceUtil.getClosestEntity(Minecraft.func_71410_x().field_71439_g, DIConfig.mainInstance().mouseoverRange)) != null && tmp != last) {
                last = tmp;
                Minecraft.func_71410_x().field_71439_g.func_145747_a(new TextComponentString(tmp.getClass().getName()));
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
                    LastTargeted = el.func_145782_y();
                }
            }
            if (el == null) {
                if (LastTargeted == 0) {
                    return;
                }
                if (DIConfig.mainInstance().portraitLifetime != -1 && tick <= 0.0d) {
                    return;
                }
            }
            ScaledResolution scaledresolution = new ScaledResolution(Minecraft.func_71410_x());
            if (DIConfig.mainInstance().locX > scaledresolution.func_78326_a() - 135) {
                DIConfig.mainInstance().locX = scaledresolution.func_78326_a() - 135;
            }
            if (DIConfig.mainInstance().locY > scaledresolution.func_78328_b() - 50) {
                DIConfig.mainInstance().locY = scaledresolution.func_78328_b() - 50;
            }
            if (DIConfig.mainInstance().locX < 0) {
                DIConfig.mainInstance().locX = 0;
            }
            if (DIConfig.mainInstance().locY < 0) {
                DIConfig.mainInstance().locY = 0;
            }
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            if (el == null) {
                tick -= elapsedTime;
                try {
                    el = (EntityLivingBase) Minecraft.func_71410_x().field_71441_e.func_73045_a(LastTargeted);
                } catch (Throwable th) {
                    el = null;
                }
                if (el == null) {
                    LastTargeted = 0;
                }
            } else {
                tick = DIConfig.mainInstance().portraitLifetime;
            }
            if (el == null) {
                return;
            }
            LastTargeted = el.func_145782_y();
            EntityConfigurationEntry configentry2 = Tools.getInstance().getEntityMap().get(el.getClass());
            if (configentry2.maxHP == -1 || configentry2.eyeHeight == -1.0f) {
                configentry2.eyeHeight = el.func_70047_e();
                configentry2.maxHP = MathHelper.func_76128_c(Math.ceil(el.func_110138_aP()));
            }
            if (configentry2.maxHP != MathHelper.func_76128_c(Math.ceil(el.func_110138_aP()))) {
                configentry2.maxHP = MathHelper.func_76128_c(Math.ceil(el.func_110138_aP()));
            }
            String Name2 = configentry2.NameOverride;
            if (el instanceof EntityPlayer) {
                Name2 = el.func_70005_c_();
            }
            if (Name2 == null || "".equals(Name2)) {
                Name = el.func_70005_c_();
                if (Name.endsWith(".name")) {
                    String Name3 = Name.replace(".name", "");
                    String Name4 = Name3.substring(Name3.lastIndexOf(".") + 1, Name3.length());
                    Name = Name4.substring(0, 1).toUpperCase() + Name4.substring(1, Name4.length());
                }
                if (el.func_70631_g_() && configentry2.AppendBaby) {
                    Name = "Baby " + Name;
                }
            } else if (el.func_70631_g_() && configentry2.AppendBaby) {
                Name = "§oBaby " + Name2;
            } else {
                Name = "§o" + Name2;
            }
            GL11.glPushMatrix();
            GL11.glTranslatef((1.0f - DIConfig.mainInstance().guiScale) * DIConfig.mainInstance().locX, (1.0f - DIConfig.mainInstance().guiScale) * DIConfig.mainInstance().locY, 0.0f);
            GL11.glScalef(DIConfig.mainInstance().guiScale, DIConfig.mainInstance().guiScale, DIConfig.mainInstance().guiScale);
            try {
                DIGuiTools.DrawPortraitSkinned(DIConfig.mainInstance().locX, DIConfig.mainInstance().locY, Name, MathHelper.func_76123_f(el.func_110143_aJ()), MathHelper.func_76123_f(el.func_110138_aP()), el);
                if (Calendar.getInstance().getWeekYear() + 3 > Calendar.getInstance().getWeeksInWeekYear()) {
                    FontRenderer fontRenderer = Minecraft.func_71410_x().field_71466_p;
                    int intValue = ((Integer) AbstractSkin.getActiveSkin().getSkinValue(EnumSkinPart.CONFIGFRAMEY)).intValue() + 75;
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                }
            } catch (Throwable th2) {
            }
            GL11.glPopMatrix();
            OpenGlHelper.func_77472_b(OpenGlHelper.field_77476_b);
            GL11.glDisableClientState(32888);
            OpenGlHelper.func_77472_b(OpenGlHelper.field_77478_a);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    public List<PotionEffect> getFormattedPotionEffects(EntityLivingBase el) {
        List<PotionEffect> effects = new ArrayList<>();
        if (el.func_70651_bq() != null && el.func_70651_bq().size() > 0) {
            effects.addAll(el.func_70651_bq());
        }
        return effects;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            DIMod.proxy.trysendmessage();
        }
    }
}
