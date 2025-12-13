package org.imesense.damageindicator.DamageIndicatorsMod.gui;

import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;
/* loaded from: input.jar:DamageIndicatorsMod/gui/GuiCheckBox.class */
public class GuiCheckBox extends GuiButton {
    public boolean checked;

    public GuiCheckBox(int id, int x, int y, int w, int h, String Message) {
        super(id, x, y, w, h, Message);
        this.checked = false;
        this.enabled = true;
        this.displayString = Message;
    }

    public int getWidth() {
        return this.width;
    }

    public void func_191745_a(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        int offset = mc.fontRenderer.getStringWidth(this.displayString) + 5;
        if (DIGuiTools.widgetsPNG == null) {
            try {
                BufferedImage ex = ImageIO.read(Minecraft.class.getResourceAsStream("/assets/minecraft/textures/gui/widgets.png"));
                DIGuiTools.widgetsPNG = new DynamicTexture(ex);
            } catch (Throwable var6) {
                var6.printStackTrace();
            }
        }
        if (this.enabled) {
            mc.fontRenderer.drawStringWithShadow(this.displayString, this.x, this.y, Color.white.getRGB());
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        } else {
            mc.fontRenderer.drawStringWithShadow(this.displayString, this.x, this.y, Color.GRAY.getRGB());
            GL11.glColor4f(0.5f, 0.5f, 0.5f, 1.0f);
        }
        DIGuiTools.widgetsPNG.updateDynamicTexture();
        if (!this.checked) {
            drawTexturedModalRect128(this.x + offset, this.y, 240, 0, 8, 8);
        } else {
            drawTexturedModalRect128(this.x + offset, this.y, 232, 0, 8, 8);
        }
    }

    public static void addVertexWithUV(double x, double y, double z, double u, double v) {
        GL11.glTexCoord2d(u, v);
        GL11.glVertex3d(x, y, z);
    }

    public static void addVertex(double x, double y, double z) {
        GL11.glVertex3d(x, y, z);
    }

    public void drawTexturedModalRect128(int par1, int par2, int par3, int par4, int par5, int par6) {
        GL11.glBegin(7);
        addVertexWithUV(par1 + 0, par2 + par6, this.zLevel, (par3 + 0) * 0.007813f, (par4 + par6) * 0.007813f);
        addVertexWithUV(par1 + par5, par2 + par6, this.zLevel, (par3 + par5) * 0.007813f, (par4 + par6) * 0.007813f);
        addVertexWithUV(par1 + par5, par2 + 0, this.zLevel, (par3 + par5) * 0.007813f, (par4 + 0) * 0.007813f);
        addVertexWithUV(par1 + 0, par2 + 0, this.zLevel, (par3 + 0) * 0.007813f, (par4 + 0) * 0.007813f);
        GL11.glEnd();
    }

    public boolean isChecked() {
        return this.checked;
    }

    public void mouseReleased(int par1, int par2) {
        super.mouseReleased(par1, par2);
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean toggle() {
        this.checked = !this.checked;
        return this.checked;
    }
}
