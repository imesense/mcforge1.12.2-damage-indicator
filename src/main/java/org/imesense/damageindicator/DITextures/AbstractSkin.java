package org.imesense.damageindicator.DITextures;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;

import net.minecraftforge.common.config.Configuration;

import org.imesense.damageindicator.DamageIndicatorMod;
import org.imesense.damageindicator.DamageIndicatorsMod.configuration.DIConfig;

public abstract class AbstractSkin
{
    private static String lastSkinUsed;
    private final EnumMap<EnumSkinPart, Object> skinMap = new EnumMap<>(EnumSkinPart.class);
    public static final List<String> AVAILABLESKINS = new ArrayList();
    public static final Minecraft MCINSTANCE = Minecraft.getMinecraft();
    public static final Map<String, AbstractSkin> SKINS = new HashMap();

    public abstract void loadConfig();

    public abstract void loadSkin();

    public static BufferedImage fixDim(BufferedImage nonpoweroftwo)
    {
        BufferedImage resized;
        int width = nonpoweroftwo.getWidth();
        int scaledwidth = width;
        if (!isPowerOfTwoFast(width))
        {
            scaledwidth = upperPowerOfTwo(width);
        }
        int height = nonpoweroftwo.getHeight();
        int scaledheight = height;
        if (!isPowerOfTwoFast(height))
        {
            scaledheight = upperPowerOfTwo(height);
        }
        if (width == scaledwidth && height == scaledheight)
        {
            resized = nonpoweroftwo;
        }
        else
        {
            try
            {
                resized = new BufferedImage(scaledwidth, scaledheight, nonpoweroftwo.getType());
            }
            catch (Throwable th)
            {
                resized = new BufferedImage(scaledwidth, scaledheight, 5);
            }
            Graphics2D graphics = resized.createGraphics();
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.drawImage(nonpoweroftwo, 0, 0, scaledwidth, scaledheight, 0, 0, width, height, (ImageObserver) null);
            graphics.dispose();
        }
        return resized;
    }

    public static AbstractSkin getActiveSkin()
    {
        return setSkin(DIConfig.selectedSkin);
    }

    public static String getAuthor(String internalName)
    {
        return (String) SKINS.get(internalName).getSkinValue(EnumSkinPart.CONFIGAUTHOR);
    }

    public static String getSkinName(String internalName)
    {
        return (String) SKINS.get(internalName).getSkinValue(EnumSkinPart.CONFIGDISPLAYNM);
    }

    public static void init()
    {
        AbstractSkin jarSkinRegistration;
        JarSkinRegistration.scanJarForSkins(DamageIndicatorMod.class);
        File file = new File(Minecraft.getMinecraft().gameDir, "CustomDISkins");
        file.mkdirs();
        FileSkinRegistration.scanFilesForSkins(file);
        for (String s : AVAILABLESKINS)
        {
            if (s.startsWith("file:"))
            {
                jarSkinRegistration = new FileSkinRegistration(s);
            }
            else
            {
                jarSkinRegistration = new JarSkinRegistration(s);
            }
            AbstractSkin skin = jarSkinRegistration;
            releaseCurrentTextures();
            skin.loadSkin();
            SKINS.put(s, skin);
        }
        if (!AVAILABLESKINS.contains(DIConfig.selectedSkin))
        {
            DIConfig.selectedSkin = "/assets/defaultskins/default/";
            setSkin(DIConfig.selectedSkin);
        }
        releaseCurrentTextures();
        getActiveSkin().loadSkin();
    }

    private static boolean isPowerOfTwoFast(int num)
    {
        return num != 0 && (num & (num - 1)) == 0;
    }

    public static void refreshSkin()
    {
        releaseCurrentTextures();
        getActiveSkin().loadSkin();
    }

    private static void releaseCurrentTextures()
    {
        AbstractSkin lastSkin = SKINS.get(lastSkinUsed);
        if (lastSkin != null)
        {
            Iterator it = EnumSet.allOf(EnumSkinPart.class).iterator();
            while (it.hasNext())
            {
                EnumSkinPart esp = (EnumSkinPart) it.next();
                if (esp.name().endsWith("ID"))
                {
                    if (((DynamicTexture) lastSkin.skinMap.get(esp)) != null)
                    {
                        GL11.glDeleteTextures(((DynamicTexture) lastSkin.skinMap.get(esp)).getGlTextureId());
                    }
                    lastSkin.skinMap.put(esp, (EnumSkinPart) null);
                }
            }
        }
    }

    public static AbstractSkin setSkin(String skin)
    {
        if (lastSkinUsed != null && !lastSkinUsed.equals(skin))
        {
            releaseCurrentTextures();
            if (SKINS.containsKey(skin))
            {
                SKINS.get(skin).loadSkin();
            }
        }
        if (!SKINS.containsKey(skin))
        {
            try
            {
                if (skin.startsWith("file:"))
                {
                    SKINS.put(skin, new FileSkinRegistration(skin));
                }
                else
                {
                    SKINS.put(skin, new JarSkinRegistration(skin));
                }
                SKINS.get(skin).loadSkin();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        lastSkinUsed = skin;
        return SKINS.get(skin);
    }

    private static int upperPowerOfTwo(int num)
    {
        int newnum = num - 1;
        int newnum2 = newnum | (newnum >> 1);
        int newnum3 = newnum2 | (newnum2 >> 2);
        int newnum4 = newnum3 | (newnum3 >> 4);
        int newnum5 = newnum4 | (newnum4 >> 8);
        return (newnum5 | (newnum5 >> 16)) + 1;
    }

    public AbstractSkin()
    {
        Iterator it = EnumSet.allOf(EnumSkinPart.class).iterator();
        while (it.hasNext())
        {
            EnumSkinPart esp = (EnumSkinPart) it.next();
            this.skinMap.put(esp, esp.getConfigDefault());
        }
    }

    public final void bindTexture(EnumSkinPart enumSkinPart)
    {
        ((DynamicTexture) this.skinMap.get(enumSkinPart)).updateDynamicTexture();
    }

    public final String getInternalName()
    {
        return (String) this.skinMap.get(EnumSkinPart.INTERNAL);
    }

    public final Object getSkinValue(EnumSkinPart enumSkinPart)
    {
        return this.skinMap.get(enumSkinPart);
    }

    public final void loadConfig(Configuration config)
    {
        String strCat;
        config.load();
        Iterator it = EnumSet.allOf(EnumSkinPart.class).iterator();
        while (it.hasNext())
        {
            EnumSkinPart enumSkinPart = (EnumSkinPart) it.next();
            String spName = enumSkinPart.name();
            String strKey = (String) enumSkinPart.getExtended();
            if (strKey != null)
            {
                Object defaultVal = enumSkinPart.getConfigDefault();
                if (spName.endsWith("WIDTH") || spName.endsWith("HEIGHT"))
                {
                    strCat = "Skin config.Sizes";
                }
                else if (spName.endsWith("X") || spName.endsWith("Y") || spName.endsWith("OFFSET"))
                {
                    strCat = "Skin config.Positions";
                }
                else if (spName.contains("CONFIGTEXTEXT"))
                {
                    strCat = "Skin config.TextSettings";
                }
                else
                {
                    strCat = "Skin config.Info";
                }
                if (defaultVal instanceof Integer)
                {
                    this.skinMap.put(enumSkinPart, Integer.valueOf(config.get(strCat, strKey, ((Integer) defaultVal).intValue()).getInt(((Integer) defaultVal).intValue())));
                }
                else
                {
                    this.skinMap.put( enumSkinPart, config.get(strCat, strKey, (String) defaultVal).getString());
                }
            }
        }
        this.skinMap.put(EnumSkinPart.ORDERING, populateOrdering(config));
        config.save();
    }

    private Ordering[] populateOrdering(Configuration config)
    {
        Ordering[] ordering = new Ordering[9];
        ordering[config.get("Skin config.Ordering", "HealthBarOrder", 3).getInt(3) - 1] = Ordering.HEALTHBAR;
        ordering[config.get("Skin config.Ordering", "FrameOrder", 5).getInt(5) - 1] = Ordering.FRAME;
        ordering[config.get("Skin config.Ordering", "BackgroundOrder", 1).getInt(1) - 1] = Ordering.BACKGROUND;
        ordering[config.get("Skin config.Ordering", "NamePlateOrder", 4).getInt(4) - 1] = Ordering.NAMEPLATE;
        ordering[config.get("Skin config.Ordering", "MobPreviewOrder", 2).getInt(2) - 1] = Ordering.MOBPREVIEW;
        ordering[config.get("Skin config.Ordering", "MobTypeOrder", 6).getInt(6) - 1] = Ordering.MOBTYPES;
        ordering[config.get("Skin config.Ordering", "PotionBoxOrder", 7).getInt(7) - 1] = Ordering.POTIONS;
        ordering[config.get("Skin config.Ordering", "HealthBarTextOrder", 8).getInt(8) - 1] = Ordering.HEALTHTEXT;
        ordering[config.get("Skin config.Ordering", "NamePlateTextOrder", 9).getInt(9) - 1] = Ordering.NAMETEXT;
        return ordering;
    }

    public final void setInternalName(String newInternalName)
    {
        this.skinMap.put(EnumSkinPart.INTERNAL, newInternalName);
    }

    public final void setSkinValue(EnumSkinPart enumSkinPart, Object value)
    {
        this.skinMap.put(enumSkinPart, value);
    }

    public final DynamicTexture setupTexture(BufferedImage bufImg, EnumSkinPart uniqueName)
    {
        DynamicTexture check = (DynamicTexture) this.skinMap.get(uniqueName);
        if (check == null)
        {
            check = new DynamicTexture(bufImg);
            this.skinMap.put(uniqueName, check);
        }
        else
        {
            bufImg.getRGB(0, 0, bufImg.getWidth(), bufImg.getHeight(), check.getTextureData(), 0, bufImg.getWidth());
        }
        return check;
    }
}
