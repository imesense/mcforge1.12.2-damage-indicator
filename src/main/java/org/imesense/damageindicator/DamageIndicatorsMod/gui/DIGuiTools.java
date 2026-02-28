package org.imesense.damageindicator.DamageIndicatorsMod.gui;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.MathHelper;

import net.minecraftforge.common.config.Configuration;

import org.imesense.damageindicator.DITextures.AbstractSkin;
import org.imesense.damageindicator.DITextures.EnumSkinPart;
import org.imesense.damageindicator.DITextures.JarSkinRegistration;
import org.imesense.damageindicator.DITextures.Ordering;
import org.imesense.damageindicator.DamageIndicatorsMod.configuration.DIConfig;
import org.imesense.damageindicator.DamageIndicatorsMod.core.DIEventBus;
import org.imesense.damageindicator.DamageIndicatorsMod.core.EntityConfigurationEntry;
import org.imesense.damageindicator.DamageIndicatorsMod.core.Tools;

public class DIGuiTools extends GuiIngame
{
    public static Long offset;
    private static ScaledResolution scaledresolution;
    public static DynamicTexture inventoryPNG;
    public static DynamicTexture widgetsPNG;
    public static Field foundField = null;
    public static DIGuiTools instance = new DIGuiTools(Minecraft.getMinecraft());
    public static Map<Class, Integer> mobRenderLists = new HashMap();
    public static int opt = 0;
    public static double rotationCounter = 0.0d;
    public static boolean skinned = true;

    private static final Minecraft f2mc = Minecraft.getMinecraft();

    public static void addVertexWithUV(double x, double y, double z, double u, double v)
    {
        GL11.glTexCoord2d(u, v);
        GL11.glVertex3d(x, y, z);
    }

    public static void addVertex(double x, double y, double z)
    {
        GL11.glVertex3d(x, y, z);
    }

    public static void drawBackground(AbstractSkin skin, int locX, int locY)
    {
        int backgroundWidth = ((Integer) skin.getSkinValue(EnumSkinPart.CONFIGBACKGROUNDWIDTH)).intValue();
        int backgroundHeight = ((Integer) skin.getSkinValue(EnumSkinPart.CONFIGBACKGROUNDHEIGHT)).intValue();
        int backgroundX = locX + ((Integer) skin.getSkinValue(EnumSkinPart.CONFIGBACKGROUNDX)).intValue();
        int backgroundY = locY + ((Integer) skin.getSkinValue(EnumSkinPart.CONFIGBACKGROUNDY)).intValue();
        skin.bindTexture(EnumSkinPart.BACKGROUNDID);
        GL11.glBegin(7);
        addVertexWithUV(backgroundX, backgroundY + backgroundHeight, 0.0d, 0.0d, 1.0d);
        addVertexWithUV(backgroundX + backgroundWidth, backgroundY + backgroundHeight, 0.0d, 1.0d, 1.0d);
        addVertexWithUV(backgroundX + backgroundWidth, backgroundY, 0.0d, 1.0d, 0.0d);
        addVertexWithUV(backgroundX, backgroundY, 0.0d, 0.0d, 0.0d);
        GL11.glEnd();
    }

    public static void drawFrame(AbstractSkin skin, int locX, int locY)
    {
        skin.bindTexture(EnumSkinPart.FRAMEID);
        int adjx = locX + ((Integer) skin.getSkinValue(EnumSkinPart.CONFIGFRAMEX)).intValue();
        int adjy = locY + ((Integer) skin.getSkinValue(EnumSkinPart.CONFIGFRAMEY)).intValue();
        int backgroundWidth = ((Integer) skin.getSkinValue(EnumSkinPart.CONFIGFRAMEWIDTH)).intValue();
        int backgroundHeight = ((Integer) skin.getSkinValue(EnumSkinPart.CONFIGFRAMEHEIGHT)).intValue();
        GL11.glBegin(7);
        addVertexWithUV(adjx, adjy + backgroundHeight, 0.0d, 0.0d, 1.0d);
        addVertexWithUV(adjx + backgroundWidth, adjy + backgroundHeight, 0.0d, 1.0d, 1.0d);
        addVertexWithUV(adjx + backgroundWidth, adjy, 0.0d, 1.0d, 0.0d);
        addVertexWithUV(adjx, adjy, 0.0d, 0.0d, 0.0d);
        GL11.glEnd();
    }

    private static void drawGradientRect(int par1, int par2, int par3, int par4, int par5, int par6, float zLevel)
    {
        float var7 = ((par5 >> 24) & 255) / 255.0f;
        float var8 = ((par5 >> 16) & 255) / 255.0f;
        float var9 = ((par5 >> 8) & 255) / 255.0f;
        float var10 = (par5 & 255) / 255.0f;
        float var11 = ((par6 >> 24) & 255) / 255.0f;
        float var12 = ((par6 >> 16) & 255) / 255.0f;
        float var13 = ((par6 >> 8) & 255) / 255.0f;
        float var14 = (par6 & 255) / 255.0f;
        GL11.glDisable(3553);
        GL11.glDisable(3008);
        GL11.glShadeModel(7425);
        Tessellator var15 = Tessellator.getInstance();
        GL11.glBegin(7);
        GL11.glColor4d(var8, var9, var10, var7);
        addVertex(par3, par2, zLevel);
        addVertex(par1, par2, zLevel);
        GL11.glColor4d(var12, var13, var14, var11);
        addVertex(par1, par4, zLevel);
        addVertex(par3, par4, zLevel);
        var15.draw();
        GL11.glShadeModel(7424);
        GL11.glEnable(3008);
        GL11.glEnable(3553);
    }

    public static void drawHealthBar(AbstractSkin skin, int locX, int locY, int health, int maxHealth, int entityID)
    {
        float healthbarwidth;
        int health2 = Math.min(health, maxHealth);
        int healthBarWidth = ((Integer) skin.getSkinValue(EnumSkinPart.CONFIGHEALTHBARWIDTH)).intValue();
        int healthBarHeight = ((Integer) skin.getSkinValue(EnumSkinPart.CONFIGHEALTHBARHEIGHT)).intValue();
        int healthBarX = ((Integer) skin.getSkinValue(EnumSkinPart.CONFIGHEALTHBARX)).intValue();
        int healthBarY = ((Integer) skin.getSkinValue(EnumSkinPart.CONFIGHEALTHBARY)).intValue();
        skin.bindTexture(EnumSkinPart.DAMAGEID);
        GL11.glBegin(7);
        addVertexWithUV(locX + healthBarX, locY + healthBarY + healthBarHeight, 0.0d, health2 / maxHealth, 1.0d);
        addVertexWithUV(locX + healthBarX + healthBarWidth, locY + healthBarY + healthBarHeight, 0.0d, 1.0d, 1.0d);
        addVertexWithUV(locX + healthBarX + healthBarWidth, locY + healthBarY, 0.0d, 1.0d, 0.0d);
        addVertexWithUV(locX + healthBarX, locY + healthBarY, 0.0d, health2 / maxHealth, 0.0d);
        GL11.glEnd();
        if (health2 < maxHealth)
        {
            float f = healthBarWidth * ((health2 * 1.0f) / (maxHealth * 1.0f));
            healthbarwidth = f;
            if (f < 0.0f)
            {
                healthbarwidth = 0.0f;
            }
        }
        else
        {
            healthbarwidth = healthBarWidth;
            EntityConfigurationEntry.maxHealthOverride.put(Integer.valueOf(entityID), Integer.valueOf(health2));
        }
        float tmp = health2 / maxHealth;
        skin.bindTexture(EnumSkinPart.HEALTHID);
        GL11.glBegin(7);
        addVertexWithUV(locX + healthBarX, locY + healthBarY + healthBarHeight, 0.0d, 0.0d, 1.0d);
        addVertexWithUV(locX + healthBarX + healthbarwidth, locY + healthBarY + healthBarHeight, 0.0d, tmp, 1.0d);
        addVertexWithUV(locX + healthBarX + healthbarwidth, locY + healthBarY, 0.0d, tmp, 0.0d);
        addVertexWithUV(locX + healthBarX, locY + healthBarY, 0.0d, 0.0d, 0.0d);
        GL11.glEnd();
    }

    public static void drawHealthText(AbstractSkin skin, int locX, int locY, int health, int maxHealth)
    {
        try
        {
            String Health = health + "/" + maxHealth;
            if (health > maxHealth)
            {
                Health = health + "/" + health;
            }
            int healthBarWidth = ((Integer) skin.getSkinValue(EnumSkinPart.CONFIGHEALTHBARWIDTH)).intValue();
            int healthBarHeight = ((Integer) skin.getSkinValue(EnumSkinPart.CONFIGHEALTHBARHEIGHT)).intValue();
            int healthBarX = ((Integer) skin.getSkinValue(EnumSkinPart.CONFIGHEALTHBARX)).intValue();
            int healthBarY = ((Integer) skin.getSkinValue(EnumSkinPart.CONFIGHEALTHBARY)).intValue();
            int packedRGB = Integer.parseInt("FFFFFF", 16);
            try
            {
                packedRGB = Integer.parseInt((String) skin.getSkinValue(EnumSkinPart.CONFIGTEXTEXTHEALTHCOLOR), 16);
            }
            catch (Exception e)
            {
            }
            if (f2mc.fontRenderer.FONT_HEIGHT + 2 > healthBarHeight)
            {
                GL11.glPushMatrix();
                try
                {
                    GL11.glTranslatef(locX + healthBarX + ((healthBarWidth - (f2mc.fontRenderer.getStringWidth(Health) * 0.7f)) / 2.0f), (((locY + healthBarY) + healthBarHeight) - (f2mc.fontRenderer.FONT_HEIGHT * 0.7f)) - 0.5f, 0.0f);
                    GL11.glScalef(0.7f, 0.7f, 1.0f);
                    f2mc.fontRenderer.drawStringWithShadow(Health, 0.0f, 0.0f, packedRGB);
                }
                catch (Throwable th)
                {
                }
                GL11.glPopMatrix();
            }
            else
            {
                try
                {
                    f2mc.fontRenderer.drawStringWithShadow(Health, locX + healthBarX + ((healthBarWidth - f2mc.fontRenderer.getStringWidth(Health)) / 2), locY + healthBarY + ((healthBarHeight - f2mc.fontRenderer.FONT_HEIGHT) / 2), packedRGB);
                }
                catch (Throwable th2)
                {
                }
            }
            GL11.glColor4d(1.0d, 1.0d, 1.0d, 1.0d);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
        catch (Throwable ex)
        {
            ex.printStackTrace();
        }
    }

    public static void drawMobPreview(EntityLivingBase el, AbstractSkin skin, int locX, int locY)
    {
        GL11.glPushAttrib(8192);
        int backgroundWidth = ((Integer) skin.getSkinValue(EnumSkinPart.CONFIGBACKGROUNDWIDTH)).intValue();
        int backgroundHeight = ((Integer) skin.getSkinValue(EnumSkinPart.CONFIGBACKGROUNDHEIGHT)).intValue();
        int MobPreviewOffsetX = ((Integer) skin.getSkinValue(EnumSkinPart.CONFIGMOBPREVIEWX)).intValue();
        int MobPreviewOffsetY = ((Integer) skin.getSkinValue(EnumSkinPart.CONFIGMOBPREVIEWY)).intValue();
        GL11.glEnable(3089);
        try
        {
            int ex = MathHelper.floor((locX + MobPreviewOffsetX) * scaledresolution.getScaleFactor());
            int boxWidth = MathHelper.floor(backgroundWidth * scaledresolution.getScaleFactor());
            int boxHeight = MathHelper.floor(backgroundHeight * scaledresolution.getScaleFactor());
            int boxLocY = MathHelper.floor((locY + MobPreviewOffsetY) * scaledresolution.getScaleFactor());
            if (!(f2mc.currentScreen instanceof AdvancedGui))
            {
                boxWidth = (int) (boxWidth * DIConfig.guiScale);
                boxHeight = (int) (boxHeight * DIConfig.guiScale);
            }
            GL11.glScissor(ex, (Minecraft.getMinecraft().displayHeight - boxLocY) - boxHeight, boxWidth, boxHeight);
            drawTargettedMobPreview(el, locX + MobPreviewOffsetX, locY + MobPreviewOffsetY);
        }
        catch (Throwable var15)
        {
            var15.printStackTrace();
        }
        GL11.glDisable(3089);
        GL11.glPopAttrib();
    }

    public static void drawMobTypes(EntityLivingBase el, AbstractSkin skin, int locX, int locY)
    {
        float glTexX;
        if (DIEventBus.enemies.contains(Integer.valueOf(el.getEntityId())) || (el instanceof IMob))
        {
            GL11.glColor4f(1.0f, 0.0f, 0.0f, 0.6f);
            GL11.glColor4d(1.0d, 0.0d, 0.0d, 0.6000000238418579d);
        }
        else
        {
            GL11.glColor4f(0.0f, 1.0f, 0.0f, 0.6f);
            GL11.glColor4d(0.0d, 1.0d, 0.0d, 0.6000000238418579d);
        }
        if (!el.isNonBoss())
        {
            glTexX = 4.0f * 0.2f;
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.6f);
            GL11.glColor4d(1.0d, 1.0d, 1.0d, 0.6000000238418579d);
        }
        else
        {
            glTexX = (el.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD || el.isEntityUndead()) ? 0.0f * 0.2f : el.getCreatureAttribute() == EnumCreatureAttribute.ARTHROPOD ? 3.0f * 0.2f : ((el instanceof EntityPlayer) || (el instanceof EntityWitch) || (el instanceof EntityVillager) || (el instanceof EntityIronGolem)) ? 2.0f * 0.2f : 1.0f * 0.2f;
        }
        float adjX = locX + ((Integer) skin.getSkinValue(EnumSkinPart.CONFIGMOBTYPEX)).intValue();
        float adjY = locY + ((Integer) skin.getSkinValue(EnumSkinPart.CONFIGMOBTYPEY)).intValue();
        skin.bindTexture(EnumSkinPart.TYPEICONSID);
        GL11.glBegin(7);
        addVertexWithUV(adjX, adjY, 0.0d, glTexX, 0.0d);
        addVertexWithUV(adjX, adjY + ((Integer) skin.getSkinValue(EnumSkinPart.CONFIGMOBTYPEHEIGHT)).intValue(), 0.0d, glTexX, 1.0d);
        addVertexWithUV(adjX + ((Integer) skin.getSkinValue(EnumSkinPart.CONFIGMOBTYPEWIDTH)).intValue(), adjY + ((Integer) skin.getSkinValue(EnumSkinPart.CONFIGMOBTYPEHEIGHT)).intValue(), 0.0d, glTexX + 0.2f, 1.0d);
        addVertexWithUV(adjX + ((Integer) skin.getSkinValue(EnumSkinPart.CONFIGMOBTYPEWIDTH)).intValue(), adjY, 0.0d, glTexX + 0.2f, 0.0d);
        GL11.glEnd();
    }

    public static void drawNamePlate(AbstractSkin skin, int locX, int locY)
    {
        int NamePlateWidth = ((Integer) skin.getSkinValue(EnumSkinPart.CONFIGNAMEPLATEWIDTH)).intValue();
        int NamePlateHeight = ((Integer) skin.getSkinValue(EnumSkinPart.CONFIGNAMEPLATEHEIGHT)).intValue();
        int NamePlateX = ((Integer) skin.getSkinValue(EnumSkinPart.CONFIGNAMEPLATEX)).intValue();
        int NamePlateY = ((Integer) skin.getSkinValue(EnumSkinPart.CONFIGNAMEPLATEY)).intValue();
        skin.bindTexture(EnumSkinPart.NAMEPLATEID);
        GL11.glBegin(7);
        addVertexWithUV(locX + NamePlateX, locY + NamePlateY, 0.0d, 0.0d, 0.0d);
        addVertexWithUV(locX + NamePlateX, locY + NamePlateY + NamePlateHeight, 0.0d, 0.0d, 1.0d);
        addVertexWithUV(locX + NamePlateX + NamePlateWidth, locY + NamePlateY + NamePlateHeight, 0.0d, 1.0d, 1.0d);
        addVertexWithUV(locX + NamePlateX + NamePlateWidth, locY + NamePlateY, 0.0d, 1.0d, 0.0d);
        GL11.glEnd();
    }

    public static void drawNameText(AbstractSkin skin, String Name, int locX, int locY)
    {
        int NamePlateWidth = ((Integer) skin.getSkinValue(EnumSkinPart.CONFIGNAMEPLATEWIDTH)).intValue();
        int NamePlateHeight = ((Integer) skin.getSkinValue(EnumSkinPart.CONFIGNAMEPLATEHEIGHT)).intValue();
        int NamePlateX = ((Integer) skin.getSkinValue(EnumSkinPart.CONFIGNAMEPLATEX)).intValue();
        int NamePlateY = ((Integer) skin.getSkinValue(EnumSkinPart.CONFIGNAMEPLATEY)).intValue();
        int packedRGB = Integer.parseInt("FFFFFF", 16);
        try
        {
            packedRGB = Integer.parseInt((String) skin.getSkinValue(EnumSkinPart.CONFIGTEXTEXTNAMECOLOR), 16);
        }
        catch (Exception var10)
        {
            var10.printStackTrace();
        }
        f2mc.fontRenderer.drawStringWithShadow(Name, locX + NamePlateX + ((NamePlateWidth - f2mc.fontRenderer.getStringWidth(Name)) / 2), locY + NamePlateY + ((NamePlateHeight - f2mc.fontRenderer.FONT_HEIGHT) / 2), packedRGB);
        GL11.glColor4d(1.0d, 1.0d, 1.0d, 1.0d);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public static void DrawPortraitSkinned(int locX, int locY, String Name, int health, int maxHealth, EntityLivingBase el)
    {
        scaledresolution = new ScaledResolution(f2mc);
        int depthzfun = GL11.glGetInteger(2932);
        boolean depthTest = GL11.glGetBoolean(2929);
        boolean blend = GL11.glGetBoolean(3042);
        try
        {
            AbstractSkin ex = AbstractSkin.getActiveSkin();
            Ordering[] ordering = (Ordering[]) ex.getSkinValue(EnumSkinPart.ORDERING);
            for (Ordering element : ordering)
            {
                GL11.glPushMatrix();
                GL11.glDepthFunc(519);
                if (element != Ordering.MOBPREVIEW)
                {
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                }
                else
                {
                    GL11.glDepthFunc(515);
                }
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 0.003662109f);
                GL11.glEnable(3553);
                GL11.glDisable(3042);
                GL11.glDepthMask(true);
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glEnable(3553);
                GL11.glEnable(2929);
                GL11.glDisable(2896);
                GL11.glBlendFunc(770, 771);
                GL11.glEnable(3042);
                GL11.glEnable(3008);
                boolean drawMobAndBackground = ((Integer) AbstractSkin.getActiveSkin().getSkinValue(EnumSkinPart.CONFIGBACKGROUNDWIDTH)).intValue() != 0;
                switch (element)
                {
                    case BACKGROUND:
                        if (drawMobAndBackground)
                        {
                            drawBackground(ex, locX, locY);
                            break;
                        }
                        break;
                    case MOBPREVIEW:
                        if (drawMobAndBackground && el.getHealth() > 0.0f)
                        {
                            drawMobPreview(el, ex, locX, locY);
                            break;
                        }
                        break;
                    case NAMEPLATE:
                        drawNamePlate(ex, locX, locY);
                        break;
                    case HEALTHBAR:
                        drawHealthBar(ex, locX, locY, health, maxHealth, el != null ? el.getEntityId() : -1);
                        break;
                    case FRAME:
                        drawFrame(ex, locX, locY);
                        break;
                    case MOBTYPES:
                        drawMobTypes(el, ex, locX, locY);
                        break;
                    case POTIONS:
                        drawPotionBoxes(el);
                        break;
                    case HEALTHTEXT:
                        drawHealthText(ex, locX, locY, health, maxHealth);
                        break;
                    case NAMETEXT:
                        drawNameText(ex, Name, locX, locY);
                        break;
                }
                GL11.glPopMatrix();
            }
        }
        catch (Throwable var26)
        {
            var26.printStackTrace();
        }
        GL11.glDepthFunc(515);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glDepthFunc(depthzfun);
        if (depthTest)
        {
            GL11.glEnable(2929);
        }
        else
        {
            GL11.glDisable(2929);
        }
        if (blend)
        {
            GL11.glEnable(3042);
        }
        else
        {
            GL11.glDisable(3042);
        }
        GL11.glClear(256);
    }

    public static void drawPotionBoxes(EntityLivingBase el)
    {
        Potion potion;
        if (inventoryPNG == null)
        {
            try
            {
                BufferedImage skin = ImageIO.read(Minecraft.class.getResourceAsStream("/assets/minecraft/textures/gui/container/inventory.png"));
                inventoryPNG = new DynamicTexture(skin);
            }
            catch (Throwable var25)
            {
                var25.printStackTrace();
            }
        }
        AbstractSkin var27 = JarSkinRegistration.getActiveSkin();
        int PotionBoxSidesWidth = ((Integer) var27.getSkinValue(EnumSkinPart.CONFIGPOTIONBOXWIDTH)).intValue();
        int PotionBoxHeight = ((Integer) var27.getSkinValue(EnumSkinPart.CONFIGPOTIONBOXHEIGHT)).intValue();
        int PotionBoxOffsetX = ((Integer) var27.getSkinValue(EnumSkinPart.CONFIGPOTIONBOXX)).intValue();
        int PotionBoxOffsetY = ((Integer) var27.getSkinValue(EnumSkinPart.CONFIGPOTIONBOXY)).intValue();
        try
        {
            boolean ex = false;
            if (DIEventBus.potionEffects.get(Integer.valueOf(el.getEntityId())) != null && !DIEventBus.potionEffects.get(Integer.valueOf(el.getEntityId())).isEmpty())
            {
                int position = 0;
                if (DIEventBus.potionEffects.containsKey(Integer.valueOf(el.getEntityId())))
                {
                    for (PotionEffect adjy : DIEventBus.potionEffects.get(Integer.valueOf(el.getEntityId())))
                    {
                        int Duration = adjy.getDuration();
                        if (Duration > 0 && (potion = adjy.getPotion()) != null && potion.hasStatusIcon() && Duration > 10)
                        {
                            GL11.glPushMatrix();
                            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                            GL11.glColor4d(1.0d, 1.0d, 1.0d, 1.0d);
                            if (!ex)
                            {
                                ex = true;
                                int adjx1 = DIConfig.locX + PotionBoxOffsetX;
                                int adjy1 = DIConfig.locY + PotionBoxOffsetY;
                                var27.bindTexture(EnumSkinPart.LEFTPOTIONID);
                                GL11.glBegin(7);
                                addVertexWithUV(adjx1, adjy1, 0.0d, 0.0d, 0.0d);
                                addVertexWithUV(adjx1, adjy1 + PotionBoxHeight, 0.0d, 0.0d, 1.0d);
                                addVertexWithUV(adjx1 + PotionBoxSidesWidth, adjy1 + PotionBoxHeight, 0.0d, 1.0d, 1.0d);
                                addVertexWithUV(adjx1 + PotionBoxSidesWidth, adjy1, 0.0d, 1.0d, 0.0d);
                                GL11.glEnd();
                            }
                            int adjx12 = DIConfig.locX + PotionBoxOffsetX + (position * 20) + PotionBoxSidesWidth;
                            int adjy12 = DIConfig.locY + PotionBoxOffsetY;
                            var27.bindTexture(EnumSkinPart.CENTERPOTIONID);
                            GL11.glBegin(7);
                            addVertexWithUV(adjx12, adjy12, 0.0d, 0.0d, 0.0d);
                            addVertexWithUV(adjx12, adjy12 + PotionBoxHeight, 0.0d, 0.0d, 1.0d);
                            addVertexWithUV(adjx12 + 20, adjy12 + PotionBoxHeight, 0.0d, 1.0d, 1.0d);
                            addVertexWithUV(adjx12 + 20, adjy12, 0.0d, 1.0d, 0.0d);
                            GL11.glEnd();
                            int iconIndex = potion.getStatusIconIndex();
                            String formattedtime = Potion.getPotionDurationString(adjy, 1.0f);
                            int posx = DIConfig.locX + PotionBoxOffsetX + (position * 20) + PotionBoxSidesWidth + 2;
                            int posy = DIConfig.locY + PotionBoxOffsetY + 2;
                            int ioffx = (0 + (iconIndex % 8)) * 18;
                            int ioffy = ((0 + (iconIndex / 8)) * 18) + 198;
                            int width = PotionBoxHeight - 4;
                            inventoryPNG.updateDynamicTexture();
                            instance.drawTexturedModalRect(posx, posy, ioffx, ioffy, width, width);
                            try
                            {
                                GL11.glTranslatef(((((DIConfig.locX + PotionBoxOffsetX) + (position * 20)) + PotionBoxSidesWidth) + 13) - (f2mc.fontRenderer.getStringWidth(formattedtime) / 2), ((DIConfig.locY + PotionBoxOffsetY) + PotionBoxHeight) - (f2mc.fontRenderer.FONT_HEIGHT * 0.815f), 0.1f);
                                GL11.glScalef(0.815f, 0.815f, 0.815f);
                                f2mc.fontRenderer.drawStringWithShadow(formattedtime, 0.0f, 0.0f, new Color(1.0f, 1.0f, 0.5f, 1.0f).getRGB());
                                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                                GL11.glColor4d(1.0d, 1.0d, 1.0d, 1.0d);
                            }
                            catch (Throwable th)
                            {
                            }
                            GL11.glPopMatrix();
                            position++;
                        }
                    }
                    if (ex)
                    {
                        int var28 = DIConfig.locX + PotionBoxOffsetX + (position * 20) + PotionBoxSidesWidth;
                        int var29 = DIConfig.locY + PotionBoxOffsetY;
                        var27.bindTexture(EnumSkinPart.RIGHTPOTIONID);
                        GL11.glBegin(7);
                        addVertexWithUV(var28, var29, 0.0d, 0.0d, 0.0d);
                        addVertexWithUV(var28, var29 + PotionBoxHeight, 0.0d, 0.0d, 1.0d);
                        addVertexWithUV(var28 + PotionBoxSidesWidth, var29 + PotionBoxHeight, 0.0d, 1.0d, 1.0d);
                        addVertexWithUV(var28 + PotionBoxSidesWidth, var29, 0.0d, 1.0d, 0.0d);
                        GL11.glEnd();
                    }
                }
            }
        }
        catch (Throwable var26)
        {
            var26.printStackTrace();
        }
    }

    public static void drawTargettedMobPreview(EntityLivingBase el, int locX, int locY)
    {
        Class entityclass = el.getClass();
        EntityConfigurationEntry configentry = Tools.getInstance().getEntityMap().get(entityclass);
        if (configentry == null)
        {
            Configuration configfile = EntityConfigurationEntry.getEntityConfiguration();
            configentry = EntityConfigurationEntry.generateDefaultConfiguration(configfile, entityclass);
            configentry.save();
            Tools.getInstance().getEntityMap().put(entityclass, configentry);
        }
        GL11.glPushMatrix();
        try
        {
            if (el == Minecraft.getMinecraft().player)
            {
                GL11.glTranslatef(locX + 25 + configentry.XOffset, ((locY + 52) + configentry.YOffset) - 30.0f, 1.0f);
            }
            else
            {
                GL11.glTranslatef(locX + 25 + configentry.XOffset, locY + 52 + configentry.YOffset, 1.0f);
            }
            GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
            float ex = (3.0f - el.getEyeHeight()) * configentry.EntitySizeScaling;
            float finalScale = configentry.ScaleFactor + (configentry.ScaleFactor * ex);
            if (el.isChild())
            {
                finalScale = (configentry.ScaleFactor + (configentry.ScaleFactor * ex)) * configentry.BabyScaleFactor;
            }
            GL11.glScalef(finalScale * 0.85f, finalScale * 0.85f, 0.1f);
            if (DIConfig.lockPosition)
            {
                int hurt = el.hurtTime;
                float ex1 = el.prevRenderYawOffset;
                el.hurtTime = 0;
                el.prevRenderYawOffset = el.renderYawOffset - 360.0f;
                GL11.glRotatef(el.renderYawOffset - 360.0f, 0.0f, 1.0f, 0.0f);
                GL11.glRotatef(-30.0f, 0.0f, 1.0f, 0.0f);
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glPushMatrix();
                renderEntity(el);
                GL11.glPopMatrix();
                el.prevRenderYawOffset = ex1;
                el.hurtTime = hurt;
            }
            else
            {
                int hurt2 = el.hurtTime;
                el.hurtTime = 0;
                GL11.glRotatef(180.0f - Minecraft.getMinecraft().player.rotationYaw, 0.0f, -1.0f, 0.0f);
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glPushMatrix();
                try
                {
                    renderEntity(el);
                }
                catch (Throwable th)
                {
                }
                GL11.glPopMatrix();
                el.hurtTime = hurt2;
            }
        }
        catch (Throwable th2)
        {
        }
        GL11.glPopMatrix();
    }

    public static void renderEntity(EntityLivingBase el)
    {
        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        try
        {
            float backup = RenderLiving.NAME_TAG_RANGE;
            RenderLiving.NAME_TAG_RANGE = 0.0f;

            Render render = Minecraft.getMinecraft()
                .getRenderManager()
                .getEntityRenderObject(el);

            if (render != null)
            {
                render.doRender(el, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
            }

            RenderLiving.NAME_TAG_RANGE = backup;
        }
        catch (Throwable ignored)
        {
        }

        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }

    public DIGuiTools(Minecraft par1Minecraft)
    {
        super(par1Minecraft);
    }
}
