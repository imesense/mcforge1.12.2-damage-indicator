package DamageIndicatorsMod.rendering;

import DamageIndicatorsMod.configuration.DIConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
@SideOnly(Side.CLIENT)
/* loaded from: input.jar:DamageIndicatorsMod/rendering/DIWordParticles.class */
public class DIWordParticles extends Particle {
    private String critical;
    public boolean criticalhit;
    public static DynamicTexture texID;
    public int Damage;
    public int curTexID;
    boolean heal;
    boolean grow;

    /* renamed from: ul */
    float f4ul;

    /* renamed from: ur */
    float f5ur;

    /* renamed from: vl */
    float f6vl;

    /* renamed from: vr */
    float f7vr;
    float locX;
    float locY;
    float locZ;
    float lastPar2;
    float red;
    float green;
    float blue;
    float alpha;
    float yOffset;
    public boolean shouldOnTop;
    FontRenderer fontRenderer;
    public static DIConfig diConfig = DIConfig.mainInstance();
    public static boolean isOptifinePresent = false;

    public DIWordParticles(World par1World, double par2, double par4, double par6, double par8, double par10, double par12) {
        this(par1World, par2, par4, par6, par8, par10, par12, 0);
        this.criticalhit = true;
        this.field_70545_g = -0.05f;
    }

    public DIWordParticles(World par1World, double par2, double par4, double par6, double par8, double par10, double par12, int damage) {
        super(par1World, par2, par4, par6, par8, par10, par12);
        this.critical = "Critical!";
        this.criticalhit = false;
        this.heal = false;
        this.grow = true;
        this.shouldOnTop = false;
        this.Damage = damage;
        func_187115_a(0.2f, 0.2f);
        this.yOffset = this.field_187135_o * 1.1f;
        func_187109_b(par2, par4, par6);
        this.field_187129_i = par8;
        this.field_187130_j = par10;
        this.field_187131_k = par12;
        float var15 = MathHelper.func_76133_a((this.field_187129_i * this.field_187129_i) + (this.field_187130_j * this.field_187130_j) + (this.field_187131_k * this.field_187131_k));
        this.field_187129_i = (this.field_187129_i / var15) * 0.12d;
        this.field_187130_j = (this.field_187130_j / var15) * 0.12d;
        this.field_187131_k = (this.field_187131_k / var15) * 0.12d;
        this.field_70548_b = 1.5f;
        this.field_70549_c = 1.5f;
        this.field_70545_g = diConfig.Gravity;
        this.field_70544_f = diConfig.Size;
        this.field_70547_e = diConfig.Lifespan;
        this.field_70546_d = 0;
        if (this.Damage < 0) {
            this.heal = true;
            this.Damage = Math.abs(this.Damage);
        }
        try {
            int baseColor = this.heal ? diConfig.healColor : diConfig.DIColor;
            this.red = ((baseColor >> 16) & 255) / 255.0f;
            this.green = ((baseColor >> 8) & 255) / 255.0f;
            this.blue = (baseColor & 255) / 255.0f;
            this.alpha = diConfig.transparency * 0.9947f;
            this.f4ul = ((this.Damage - (MathHelper.func_76141_d(this.Damage / 16.0f) * 16.0f)) % 16.0f) / 16.0f;
            this.f5ur = this.f4ul + 0.0624375f;
            this.f6vl = ((MathHelper.func_76141_d(this.Damage / 16.0f) * 16.0f) / 16.0f) / 16.0f;
            this.f7vr = this.f6vl + 0.0624375f;
        } catch (Throwable th) {
        }
    }

    public void func_187110_a(double x, double y, double z) {
        super.func_187110_a(x, y, z);
    }

    public void func_180434_a(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        this.shouldOnTop = Minecraft.func_71410_x().field_71439_g.func_70685_l(entityIn);
        double rotationYaw = -Minecraft.func_71410_x().field_71439_g.field_70177_z;
        double rotationPitch = Minecraft.func_71410_x().field_71439_g.field_70125_A;
        float size = 0.1f * this.field_70544_f;
        try {
            this.locX = (float) ((this.field_187123_c + ((this.field_187126_f - this.field_187123_c) * partialTicks)) - field_70556_an);
            this.locY = (float) ((this.field_187124_d + ((this.field_187127_g - this.field_187124_d) * partialTicks)) - field_70554_ao);
            this.locZ = (float) ((this.field_187125_e + ((this.field_187128_h - this.field_187125_e) * partialTicks)) - field_70555_ap);
            float f = rotationX * size;
            float f2 = rotationZ * size;
            float f3 = rotationYZ * size;
            float f4 = rotationXY * size;
            float f5 = rotationXZ * size;
        } catch (Throwable th) {
        }
        GL11.glPushMatrix();
        if (this.shouldOnTop) {
            GL11.glDepthFunc(519);
        } else {
            GL11.glDepthFunc(515);
        }
        GL11.glTranslatef(this.locX, this.locY, this.locZ);
        GL11.glRotated(rotationYaw, 0.0d, 1.0d, 0.0d);
        GL11.glRotated(rotationPitch, 1.0d, 0.0d, 0.0d);
        GL11.glScalef(-1.0f, -1.0f, 1.0f);
        GL11.glScaled(this.field_70544_f * 0.008d, this.field_70544_f * 0.008d, this.field_70544_f * 0.008d);
        if (this.criticalhit) {
            GL11.glScaled(0.5d, 0.5d, 0.5d);
        }
        this.fontRenderer = Minecraft.func_71410_x().field_71466_p;
        OpenGlHelper.func_77475_a(OpenGlHelper.field_77476_b, 240.0f, 0.003662109f);
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
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        if (this.criticalhit && DIConfig.mainInstance().showCriticalStrikes) {
            renderText(this.critical, this.fontRenderer.func_78256_a(this.critical) / (-2.0f), this.fontRenderer.field_78288_b / (-2.0f), 204, 0, 0);
        } else if (!this.criticalhit) {
            int color = this.heal ? DIConfig.mainInstance().healColor : DIConfig.mainInstance().DIColor;
            renderText(String.valueOf(this.Damage), this.fontRenderer.func_78256_a(this.Damage + "") / (-2.0f), this.fontRenderer.field_78288_b / (-2.0f), (color >> 16) & 255, (color >> 8) & 255, (color >> 0) & 255);
        }
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glDepthFunc(515);
        GL11.glPopMatrix();
        if (this.grow) {
            this.field_70544_f *= 1.08f;
            if (this.field_70544_f > diConfig.Size * 3.0d) {
                this.grow = false;
                return;
            }
            return;
        }
        this.field_70544_f *= 0.96f;
    }

    public void renderText(String str, float posX, float posY, int red, int green, int blue) {
        if (DIConfig.mainInstance().useDropShadows) {
            int r = red;
            int g = green;
            int b = blue;
            if (red > green && red > blue) {
                r = 255;
                g = 0;
                b = 0;
            } else if (green > red && green > blue) {
                r = 0;
                g = 255;
                b = 0;
            } else if (blue > red && blue > green) {
                r = 0;
                g = 0;
                b = 255;
            }
            this.fontRenderer.func_78276_b(str, 1, 1, (((int) (this.alpha * 200.0d)) & 255) << 24);
            GL11.glPushMatrix();
            GL11.glTranslated(-0.2d, -0.2d, 0.0d);
            GL11.glScaled(1.075d, 1.075d, 1.0d);
            this.fontRenderer.func_78276_b(str, 0, 0, ((((int) (this.alpha * 64.0d)) & 255) << 24) | ((((red + r) / 2) & 255) << 16) | ((((green + g) / 2) & 255) << 8) | ((((blue + b) / 2) & 255) << 0));
            GL11.glPopMatrix();
            this.fontRenderer.func_78276_b(str, 0, 0, ((((int) (this.alpha * 128.0d)) & 255) << 24) | (((((red + red) + r) / 3) & 255) << 16) | (((((green + green) + g) / 3) & 255) << 8) | (((((blue + blue) + b) / 3) & 255) << 0));
            GL11.glPushMatrix();
            GL11.glTranslated(0.15d, 0.15d, 0.0d);
            GL11.glScaled(0.95d, 0.95d, 1.0d);
            this.fontRenderer.func_78276_b(str, 0, 0, ((((int) (this.alpha * 255.0d)) & 255) << 24) | ((red & 255) << 16) | ((green & 255) << 8) | ((blue & 255) << 0));
            GL11.glPopMatrix();
        } else {
            this.fontRenderer.func_78276_b(str, 0, 0, ((((int) (this.alpha * 255.0d)) & 255) << 24) | ((red & 255) << 16) | ((green & 255) << 8) | ((blue & 255) << 0));
        }
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public int func_70537_b() {
        return 3;
    }
}
