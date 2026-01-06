package DamageIndicatorsMod.gui;

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
        this.field_146124_l = true;
        this.field_146126_j = Message;
    }

    public int getWidth() {
        return this.field_146120_f;
    }

    public void func_191745_a(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        int offset = mc.field_71466_p.func_78256_a(this.field_146126_j) + 5;
        if (DIGuiTools.widgetsPNG == null) {
            try {
                BufferedImage ex = ImageIO.read(Minecraft.class.getResourceAsStream("/assets/minecraft/textures/gui/widgets.png"));
                DIGuiTools.widgetsPNG = new DynamicTexture(ex);
            } catch (Throwable var6) {
                var6.printStackTrace();
            }
        }
        if (this.field_146124_l) {
            mc.field_71466_p.func_175063_a(this.field_146126_j, this.field_146128_h, this.field_146129_i, Color.white.getRGB());
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        } else {
            mc.field_71466_p.func_175063_a(this.field_146126_j, this.field_146128_h, this.field_146129_i, Color.GRAY.getRGB());
            GL11.glColor4f(0.5f, 0.5f, 0.5f, 1.0f);
        }
        DIGuiTools.widgetsPNG.func_110564_a();
        if (!this.checked) {
            drawTexturedModalRect128(this.field_146128_h + offset, this.field_146129_i, 240, 0, 8, 8);
        } else {
            drawTexturedModalRect128(this.field_146128_h + offset, this.field_146129_i, 232, 0, 8, 8);
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
        addVertexWithUV(par1 + 0, par2 + par6, this.field_73735_i, (par3 + 0) * 0.007813f, (par4 + par6) * 0.007813f);
        addVertexWithUV(par1 + par5, par2 + par6, this.field_73735_i, (par3 + par5) * 0.007813f, (par4 + par6) * 0.007813f);
        addVertexWithUV(par1 + par5, par2 + 0, this.field_73735_i, (par3 + par5) * 0.007813f, (par4 + 0) * 0.007813f);
        addVertexWithUV(par1 + 0, par2 + 0, this.field_73735_i, (par3 + 0) * 0.007813f, (par4 + 0) * 0.007813f);
        GL11.glEnd();
    }

    public boolean isChecked() {
        return this.checked;
    }

    public void func_146118_a(int par1, int par2) {
        super.func_146118_a(par1, par2);
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean toggle() {
        this.checked = !this.checked;
        return this.checked;
    }
}
