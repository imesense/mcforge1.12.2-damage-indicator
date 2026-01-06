package DamageIndicatorsMod.core;

import DamageIndicatorsMod.DIMod;
import DamageIndicatorsMod.configuration.DIConfig;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
/* loaded from: input.jar:DamageIndicatorsMod/core/Tools.class */
public class Tools {
    private static HashMap<Class, EntityConfigurationEntry> entityMap = new HashMap<>();
    public static int timeTillFlush = 500;
    private static Tools instance;

    /* renamed from: mc */
    private Minecraft f1mc = Minecraft.func_71410_x();
    public List<Object[]> unloadedEntities = new ArrayList();
    boolean lasttimefailed = false;

    public static Tools getInstance() {
        if (instance == null) {
            instance = new Tools();
        }
        return instance;
    }

    public void checkIfLoaded() {
    }

    public BufferedImage doFilter(BufferedImage src) throws OutOfMemoryError, Throwable {
        int upScaleDim = MathHelper.func_76141_d(src.getWidth() * DIConfig.mainInstance().ScaleFilter);
        BufferedImage dst = new BufferedImage(upScaleDim, upScaleDim, src.getType());
        AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(DIConfig.mainInstance().ScaleFilter, DIConfig.mainInstance().ScaleFilter), DIConfig.mainInstance().hints);
        ato.filter(src, dst);
        return dst;
    }

    public HashMap<Class, EntityConfigurationEntry> getEntityMap() {
        if (entityMap.isEmpty()) {
            scanforEntities();
        }
        return entityMap;
    }

    public void giveUpdateInformation() {
        if (DIMod.s_sUpdateMessage == null) {
            DIMod.s_sUpdateMessage = "Damage Indicators was unable to check for updates.";
        }
        if (!"".equals(DIMod.s_sUpdateMessage) && this.f1mc.field_71439_g != null) {
            this.f1mc.field_71439_g.func_145747_a(new TextComponentString(DIMod.s_sUpdateMessage));
            DIMod.s_sUpdateMessage = "";
        }
    }

    public void registerCommands() {
    }

    public void RegisterRenders() {
        scanforEntities();
        MinecraftForge.EVENT_BUS.register(DITicker.instance);
    }

    public static Map<Class<? extends Entity>, String> getEntityList() {
        Map<Class<? extends Entity>, String> ret = new HashMap<>();
        for (ResourceLocation rl : EntityList.func_180124_b()) {
            ret.put(EntityList.getClass(rl), EntityList.func_191302_a(rl));
        }
        ret.put(EntityOtherPlayerMP.class, "OtherPlayers");
        return ret;
    }

    public void scanforEntities() {
        searchMapForEntities(getEntityList());
    }

    private void searchMapForEntities(Map theMap) {
        Configuration config = EntityConfigurationEntry.getEntityConfiguration();
        this.lasttimefailed = false;
        Set<Class> set = theMap.keySet();
        for (Class entry : set) {
            if (entry != null) {
                try {
                    if (EntityLiving.class.isAssignableFrom(entry)) {
                        entityMap.put(entry, EntityConfigurationEntry.generateDefaultConfiguration(config, entry));
                    }
                } catch (Throwable th) {
                }
            }
        }
        config.save();
    }
}
