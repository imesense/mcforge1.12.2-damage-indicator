package org.imesense.damageindicator.DamageIndicatorsMod.gui;

import org.imesense.damageindicator.DITextures.AbstractSkin;
import org.imesense.damageindicator.DITextures.EnumSkinPart;
import org.imesense.damageindicator.DamageIndicatorsMod.configuration.DIConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings;
import org.lwjgl.opengl.GL11;
/* loaded from: input.jar:DamageIndicatorsMod/gui/SkinGui.class */
public class SkinGui extends GuiScreen {
    private SkinSlot SkinSlot;
    DIConfig diConfig;

    public SkinGui(GuiScreen par1, GameSettings par2) {
    }

    public void initGui() {
        this.SkinSlot = new SkinSlot(this);
        this.buttonList.add(new GuiButton(1, this.width - 24, 4, 20, 20, "X"));
    }

    protected void actionPerformed(GuiButton par1GuiButton) {
        Minecraft.getMinecraft().displayGuiScreen(new RepositionGui());
    }

    public void onGuiClosed() {
        RepositionGui rp = new RepositionGui();
        rp.onGuiClosed();
    }

    public void drawDefaultBackground() {
    }

    protected void drawBackground() {
    }

    public void drawBackground(int par1) {
    }

    public void drawScreen(int par1, int par2, float par3) {
        this.SkinSlot.drawScreen(par1, par2, par3);
        super.drawScreen(par1, par2, par3);
        GL11.glPushAttrib(278529);
        GL11.glPushMatrix();
        GL11.glTranslatef((1.0f - DIConfig.guiScale) * DIConfig.locX, (1.0f - DIConfig.guiScale) * DIConfig.locY, 0.0f);
        GL11.glScalef(DIConfig.guiScale, DIConfig.guiScale, 1.0f);
        float headPosX = DIConfig.locX;
        float headPosX2 = headPosX + ((((Integer) AbstractSkin.getActiveSkin().getSkinValue(EnumSkinPart.CONFIGMOBPREVIEWX)).intValue() + (((Integer) AbstractSkin.getActiveSkin().getSkinValue(EnumSkinPart.CONFIGBACKGROUNDWIDTH)).intValue() / 2.0f)) * DIConfig.guiScale);
        float headPosY = DIConfig.locY;
        float headPosY2 = headPosY + ((((Integer) AbstractSkin.getActiveSkin().getSkinValue(EnumSkinPart.CONFIGMOBPREVIEWY)).intValue() + (((Integer) AbstractSkin.getActiveSkin().getSkinValue(EnumSkinPart.CONFIGBACKGROUNDHEIGHT)).intValue() / 2.0f)) * DIConfig.guiScale);
        float headPosX3 = par1 - headPosX2;
        float headPosY3 = par2 - headPosY2;
        float f2 = this.mc.player.renderYawOffset;
        float f3 = this.mc.player.rotationYaw;
        float f4 = this.mc.player.rotationPitch;
        float f5 = this.mc.player.prevRotationYawHead;
        float f6 = this.mc.player.rotationYawHead;
        this.mc.player.renderYawOffset = (((float) Math.atan(headPosX3 / 40.0f)) * 20.0f) + 35.0f;
        this.mc.player.rotationYaw = ((float) Math.atan(headPosX3 / 40.0f)) * 40.0f;
        this.mc.player.rotationPitch = ((float) Math.atan(headPosY3 / 40.0f)) * 20.0f;
        this.mc.player.rotationYawHead = this.mc.player.rotationYaw;
        this.mc.player.prevRotationYawHead = this.mc.player.rotationYaw;
        Minecraft.getMinecraft().getRenderManager().playerViewY = 180.0f;
        GL11.glPushClientAttrib(1);
        DIGuiTools.DrawPortraitSkinned(this.diConfig.locX, this.diConfig.locY, this.mc.player.getName(), (int) Math.ceil(this.mc.player.getMaxHealth()), (int) Math.ceil(this.mc.player.getHealth()), this.mc.player);
        GL11.glPopClientAttrib();
        this.mc.player.renderYawOffset = f2;
        this.mc.player.rotationYaw = f3;
        this.mc.player.rotationPitch = f4;
        this.mc.player.prevRotationYawHead = f5;
        this.mc.player.rotationYawHead = f6;
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }
}
