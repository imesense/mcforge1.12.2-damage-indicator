package DamageIndicatorsMod.gui;

import DITextures.AbstractSkin;
import DITextures.EnumSkinPart;
import DamageIndicatorsMod.configuration.DIConfig;
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

    public void func_73866_w_() {
        this.SkinSlot = new SkinSlot(this);
        this.field_146292_n.add(new GuiButton(1, this.field_146294_l - 24, 4, 20, 20, "X"));
    }

    protected void func_146284_a(GuiButton par1GuiButton) {
        Minecraft.func_71410_x().func_147108_a(new RepositionGui());
    }

    public void func_146281_b() {
        RepositionGui rp = new RepositionGui();
        rp.diConfig = DIConfig.mainInstance();
        rp.func_146281_b();
    }

    public void func_146276_q_() {
    }

    protected void drawBackground() {
    }

    public void func_146278_c(int par1) {
    }

    public void func_73863_a(int par1, int par2, float par3) {
        this.diConfig = DIConfig.mainInstance();
        this.SkinSlot.drawScreen(par1, par2, par3);
        super.func_73863_a(par1, par2, par3);
        GL11.glPushAttrib(278529);
        GL11.glPushMatrix();
        GL11.glTranslatef((1.0f - DIConfig.mainInstance().guiScale) * DIConfig.mainInstance().locX, (1.0f - DIConfig.mainInstance().guiScale) * DIConfig.mainInstance().locY, 0.0f);
        GL11.glScalef(DIConfig.mainInstance().guiScale, DIConfig.mainInstance().guiScale, 1.0f);
        float headPosX = DIConfig.mainInstance().locX;
        float headPosX2 = headPosX + ((((Integer) AbstractSkin.getActiveSkin().getSkinValue(EnumSkinPart.CONFIGMOBPREVIEWX)).intValue() + (((Integer) AbstractSkin.getActiveSkin().getSkinValue(EnumSkinPart.CONFIGBACKGROUNDWIDTH)).intValue() / 2.0f)) * DIConfig.mainInstance().guiScale);
        float headPosY = DIConfig.mainInstance().locY;
        float headPosY2 = headPosY + ((((Integer) AbstractSkin.getActiveSkin().getSkinValue(EnumSkinPart.CONFIGMOBPREVIEWY)).intValue() + (((Integer) AbstractSkin.getActiveSkin().getSkinValue(EnumSkinPart.CONFIGBACKGROUNDHEIGHT)).intValue() / 2.0f)) * DIConfig.mainInstance().guiScale);
        float headPosX3 = par1 - headPosX2;
        float headPosY3 = par2 - headPosY2;
        float f2 = this.field_146297_k.field_71439_g.field_70761_aq;
        float f3 = this.field_146297_k.field_71439_g.field_70177_z;
        float f4 = this.field_146297_k.field_71439_g.field_70125_A;
        float f5 = this.field_146297_k.field_71439_g.field_70758_at;
        float f6 = this.field_146297_k.field_71439_g.field_70759_as;
        this.field_146297_k.field_71439_g.field_70761_aq = (((float) Math.atan(headPosX3 / 40.0f)) * 20.0f) + 35.0f;
        this.field_146297_k.field_71439_g.field_70177_z = ((float) Math.atan(headPosX3 / 40.0f)) * 40.0f;
        this.field_146297_k.field_71439_g.field_70125_A = ((float) Math.atan(headPosY3 / 40.0f)) * 20.0f;
        this.field_146297_k.field_71439_g.field_70759_as = this.field_146297_k.field_71439_g.field_70177_z;
        this.field_146297_k.field_71439_g.field_70758_at = this.field_146297_k.field_71439_g.field_70177_z;
        Minecraft.func_71410_x().func_175598_ae().field_78735_i = 180.0f;
        GL11.glPushClientAttrib(1);
        DIGuiTools.DrawPortraitSkinned(this.diConfig.locX, this.diConfig.locY, this.field_146297_k.field_71439_g.func_70005_c_(), (int) Math.ceil(this.field_146297_k.field_71439_g.func_110138_aP()), (int) Math.ceil(this.field_146297_k.field_71439_g.func_110143_aJ()), this.field_146297_k.field_71439_g);
        GL11.glPopClientAttrib();
        this.field_146297_k.field_71439_g.field_70761_aq = f2;
        this.field_146297_k.field_71439_g.field_70177_z = f3;
        this.field_146297_k.field_71439_g.field_70125_A = f4;
        this.field_146297_k.field_71439_g.field_70758_at = f5;
        this.field_146297_k.field_71439_g.field_70759_as = f6;
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }
}
