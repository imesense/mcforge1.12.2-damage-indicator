package org.imesense.damageindicator.DITextures;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.world.World;

import net.minecraftforge.common.config.Configuration;

import org.imesense.damageindicator.DamageIndicatorMod;

public class JarSkinRegistration extends AbstractSkin
{
    File file;

    private static void checkEntry(JarEntry jEntry)
    {
        if (jEntry.getName().contains("defaultskins") && jEntry.getName().contains("skin.cfg"))
        {
            String thisSkin = jEntry.getName().substring(0, jEntry.getName().lastIndexOf("/"));
            if (!thisSkin.startsWith("/"))
            {
                thisSkin = "/" + thisSkin;
            }
            if (!thisSkin.endsWith("/"))
            {
                thisSkin = thisSkin + "/";
            }
            if (!AbstractSkin.AVAILABLESKINS.contains(thisSkin))
            {
                AbstractSkin.AVAILABLESKINS.add(thisSkin);
            }
        }
    }

    private static void giveDebuggingInfo(Object test)
    {
    }

    public static void scanJarForSkins(Class clazz)
    {
        try
        {
            URL url = clazz.getResource("/assets");
            if (url != null)
            {
                Object test = url.openConnection();
                if (test instanceof JarURLConnection)
                {
                    JarURLConnection juc = (JarURLConnection) test;
                    juc.setUseCaches(false);
                    juc.setDoInput(true);
                    juc.setDoOutput(false);
                    juc.setAllowUserInteraction(true);
                    juc.connect();
                    Enumeration jEnum = juc.getJarFile().entries();
                    while (jEnum.hasMoreElements())
                    {
                        try
                        {
                            checkEntry((JarEntry) jEnum.nextElement());
                        }
                        catch (Exception e)
                        {
                        }
                    }
                }
                else if (!World.class.getName().endsWith("World"))
                {
                    giveDebuggingInfo(test);
                }
            }
        }
        catch (Exception e2)
        {
        }
        if (!AbstractSkin.AVAILABLESKINS.contains("/assets/defaultskins/default/"))
        {
            AbstractSkin.AVAILABLESKINS.add("/assets/defaultskins/default/");
        }
        if (!AbstractSkin.AVAILABLESKINS.contains("/assets/defaultskins/wowlike/"))
        {
            AbstractSkin.AVAILABLESKINS.add("/assets/defaultskins/wowlike/");
        }
        if (!AbstractSkin.AVAILABLESKINS.contains("/assets/defaultskins/minimal/"))
        {
            AbstractSkin.AVAILABLESKINS.add("/assets/defaultskins/minimal/");
        }
    }

    public JarSkinRegistration(String skinName)
    {
        setInternalName(skinName);
        setSkinValue(EnumSkinPart.FRAMENAME, skinName + "DIFrameSkin.png");
        setSkinValue(EnumSkinPart.TYPEICONSNAME, skinName + "DITypeIcons.png");
        setSkinValue(EnumSkinPart.DAMAGENAME, skinName + "damage.png");
        setSkinValue(EnumSkinPart.HEALTHNAME, skinName + "health.png");
        setSkinValue(EnumSkinPart.BACKGROUNDNAME, skinName + "background.png");
        setSkinValue(EnumSkinPart.NAMEPLATENAME, skinName + "NamePlate.png");
        setSkinValue(EnumSkinPart.LEFTPOTIONNAME, skinName + "leftPotions.png");
        setSkinValue(EnumSkinPart.RIGHTPOTIONNAME, skinName + "rightPotions.png");
        setSkinValue(EnumSkinPart.CENTERPOTIONNAME, skinName + "centerPotions.png");
        try
        {
            this.file = File.createTempFile("skin", ".tmp");
            try
            {
                if (this.file.exists() && !this.file.delete())
                {
                    this.file.deleteOnExit();
                    this.file = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".skin.tmp");
                }
                if (!this.file.createNewFile())
                {
                }
                URL url = Minecraft.class.getResource(skinName + "skin.cfg");
                InputStream cfg = url.openStream();
                FileOutputStream fos = new FileOutputStream(this.file);
                for (int bite = cfg.read(); bite != -1; bite = cfg.read())
                {
                    fos.write(bite);
                }
                fos.flush();
                fos.close();
                cfg.close();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        catch (Exception ex2)
        {
            ex2.printStackTrace();
        }
    }

    @Override
    public void loadConfig()
    {
        loadConfig(new Configuration(this.file));
        this.file.deleteOnExit();
    }

    private DynamicTexture checkAndReload(EnumSkinPart enumID, EnumSkinPart enumName)
    {
        DynamicTexture ret = (DynamicTexture) getSkinValue(enumID);
        if (ret == null)
        {
            try
            {
                String tmp = (String) getSkinValue(enumName);
                ret = setupTexture(fixDim(ImageIO.read(DamageIndicatorMod.class.getResourceAsStream(tmp))), enumID);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        return ret;
    }

    @Override
    public final void loadSkin()
    {
        setSkinValue(EnumSkinPart.FRAMEID, checkAndReload(EnumSkinPart.FRAMEID, EnumSkinPart.FRAMENAME));
        setSkinValue(EnumSkinPart.TYPEICONSID, checkAndReload(EnumSkinPart.TYPEICONSID, EnumSkinPart.TYPEICONSNAME));
        setSkinValue(EnumSkinPart.DAMAGEID, checkAndReload(EnumSkinPart.DAMAGEID, EnumSkinPart.DAMAGENAME));
        setSkinValue(EnumSkinPart.HEALTHID, checkAndReload(EnumSkinPart.HEALTHID, EnumSkinPart.HEALTHNAME));
        setSkinValue(EnumSkinPart.BACKGROUNDID, checkAndReload(EnumSkinPart.BACKGROUNDID, EnumSkinPart.BACKGROUNDNAME));
        setSkinValue(EnumSkinPart.NAMEPLATEID, checkAndReload(EnumSkinPart.NAMEPLATEID, EnumSkinPart.NAMEPLATENAME));
        setSkinValue(EnumSkinPart.LEFTPOTIONID, checkAndReload(EnumSkinPart.LEFTPOTIONID, EnumSkinPart.LEFTPOTIONNAME));
        setSkinValue(EnumSkinPart.RIGHTPOTIONID, checkAndReload(EnumSkinPart.RIGHTPOTIONID, EnumSkinPart.RIGHTPOTIONNAME));
        setSkinValue(EnumSkinPart.CENTERPOTIONID, checkAndReload(EnumSkinPart.CENTERPOTIONID, EnumSkinPart.CENTERPOTIONNAME));
        loadConfig();
    }
}
