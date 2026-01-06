package DamageIndicatorsMod.gui;

import DITextures.AbstractSkin;
import DamageIndicatorsMod.configuration.DIConfig;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
/* loaded from: input.jar:DamageIndicatorsMod/gui/SkinSlot.class */
public class SkinSlot {
    private SkinGui parentTexturePackGui;

    /* renamed from: mc */
    private Minecraft f3mc;
    public int selectedEntry;
    private final Minecraft client;
    protected final int listWidth;
    protected final int listHeight;
    protected final int top;
    protected final int bottom;
    private final int right;
    protected final int left;
    protected final int slotHeight;
    private int scrollUpActionId;
    private int scrollDownActionId;
    protected int mouseX;
    protected int mouseY;
    private float initialMouseClickY;
    private float scrollFactor;
    private float scrollDistance;
    private int selectedIndex;
    private long lastClickTime;
    private boolean showSelectionBox;
    private boolean field_77243_s;
    private int field_77242_t;
    int boxLocX;
    int boxWidth;
    int boxHeight;
    int boxLocY;

    protected int getSize() {
        return AbstractSkin.AVAILABLESKINS.size();
    }

    public SkinSlot(SkinGui par1GuiTexturePacks) {
        this(Minecraft.func_71410_x(), par1GuiTexturePacks.field_146294_l - 128, par1GuiTexturePacks.field_146295_m - 128, 64, par1GuiTexturePacks.field_146295_m - 64, 64, 32);
        this.parentTexturePackGui = par1GuiTexturePacks;
        this.selectedEntry = 0;
        if (DIConfig.mainInstance().portraitEnabled) {
            this.selectedEntry = AbstractSkin.AVAILABLESKINS.indexOf(DIConfig.mainInstance().selectedSkin);
        }
    }

    protected void elementClicked(int par1, boolean par2) {
        DIConfig.mainInstance().selectedSkin = AbstractSkin.AVAILABLESKINS.get(par1);
        AbstractSkin.setSkin(DIConfig.mainInstance().selectedSkin);
        if (par2) {
            Minecraft.func_71410_x().func_147108_a(this.parentTexturePackGui);
        }
        this.selectedEntry = par1;
    }

    protected boolean isSelected(int index) {
        return this.selectedEntry == index;
    }

    protected void drawSlot(int par1, int par2, int par3, int par4) {
        String text1 = AbstractSkin.getSkinName(AbstractSkin.AVAILABLESKINS.get(par1));
        String text2 = AbstractSkin.getAuthor(AbstractSkin.AVAILABLESKINS.get(par1));
        int var10003 = this.left + 4;
        this.parentTexturePackGui.func_73731_b(Minecraft.func_71410_x().field_71466_p, text1, var10003, par3 + 3, 3398963);
        int var100032 = this.left + 4;
        this.parentTexturePackGui.func_73731_b(Minecraft.func_71410_x().field_71466_p, text2, var100032, par3 + 15, 3398963);
    }

    public SkinSlot(Minecraft client, int width, int height, int top, int bottom, int left, int entryHeight) {
        this.selectedEntry = 0;
        this.initialMouseClickY = -2.0f;
        this.selectedIndex = -1;
        this.lastClickTime = 0L;
        this.showSelectionBox = true;
        this.client = client;
        this.listWidth = width;
        this.listHeight = height;
        this.top = top;
        this.bottom = bottom;
        this.slotHeight = entryHeight;
        this.left = left;
        this.right = width + this.left;
        this.f3mc = client;
        ScaledResolution scaledresolution = new ScaledResolution(this.f3mc);
        this.boxLocX = MathHelper.func_76141_d(left * scaledresolution.func_78325_e());
        this.boxWidth = MathHelper.func_76141_d(width * scaledresolution.func_78325_e());
        this.boxHeight = MathHelper.func_76141_d(height * scaledresolution.func_78325_e());
        this.boxLocY = MathHelper.func_76141_d(top * scaledresolution.func_78325_e());
        this.selectedEntry = AbstractSkin.AVAILABLESKINS.indexOf(DIConfig.mainInstance().selectedSkin);
    }

    public void setShowSelectionBox(boolean par1) {
        this.showSelectionBox = par1;
    }

    protected void func_77223_a(boolean par1, int par2) {
        this.field_77243_s = par1;
        this.field_77242_t = par2;
        if (!par1) {
            this.field_77242_t = 0;
        }
    }

    protected int getContentHeight() {
        return (getSize() * this.slotHeight) + this.field_77242_t;
    }

    protected void func_77222_a(int par1, int par2, Tessellator par3Tessellator) {
    }

    protected void func_77224_a(int par1, int par2) {
    }

    protected void func_77215_b(int par1, int par2) {
    }

    public int func_77210_c(int par1, int par2) {
        int var3 = this.left + 1;
        int var4 = (this.left + this.listWidth) - 7;
        int var5 = (((par2 - this.top) - this.field_77242_t) + ((int) this.scrollDistance)) - 4;
        int var6 = var5 / this.slotHeight;
        if (par1 < var3 || par1 > var4 || var6 < 0 || var5 < 0 || var6 >= getSize()) {
            return -1;
        }
        return var6;
    }

    public void registerScrollButtons(List par1List, int par2, int par3) {
        this.scrollUpActionId = par2;
        this.scrollDownActionId = par3;
    }

    private void applyScrollLimits() {
        int var1 = getContentHeight() - ((this.bottom - this.top) - 4);
        if (var1 < 0) {
            var1 /= 2;
        }
        if (this.scrollDistance < 0.0f) {
            this.scrollDistance = 0.0f;
        }
        if (this.scrollDistance > var1) {
            this.scrollDistance = var1;
        }
    }

    public void actionPerformed(GuiButton button) {
        if (button.field_146124_l) {
            if (button.field_146127_k == this.scrollUpActionId) {
                this.scrollDistance -= (this.slotHeight * 2) / 3;
                this.initialMouseClickY = -2.0f;
                applyScrollLimits();
            } else if (button.field_146127_k == this.scrollDownActionId) {
                this.scrollDistance += (this.slotHeight * 2) / 3;
                this.initialMouseClickY = -2.0f;
                applyScrollLimits();
            }
        }
    }

    public static void addVertexWithUV(double x, double y, double z, double u, double v) {
        GL11.glTexCoord2d(u, v);
        GL11.glVertex3d(x, y, z);
    }

    public static void addVertex(double x, double y, double z) {
        GL11.glVertex3d(x, y, z);
    }

    public void drawScreen(int mouseX, int mouseY, float par3) {
        try {
            GL11.glEnable(3089);
            GL11.glScissor(this.boxLocX, this.boxLocY, this.boxWidth, this.boxHeight);
            this.mouseX = mouseX;
            this.mouseY = mouseY;
            int ex = getSize();
            int scrollBarXStart = (this.left + this.listWidth) - 6;
            int scrollBarXEnd = scrollBarXStart + 6;
            int boxLeft = this.left;
            int boxRight = scrollBarXStart - 1;
            if (Mouse.isButtonDown(0)) {
                if (this.initialMouseClickY == -1.0f) {
                    boolean var18 = true;
                    if (mouseY >= this.top && mouseY <= this.bottom) {
                        int var10 = (((mouseY - this.top) - this.field_77242_t) + ((int) this.scrollDistance)) - 4;
                        int var11 = var10 / this.slotHeight;
                        if (mouseX >= boxLeft && mouseX <= boxRight && var11 >= 0 && var10 >= 0 && var11 < ex) {
                            boolean var17 = var11 == this.selectedIndex && System.currentTimeMillis() - this.lastClickTime < 250;
                            elementClicked(var11, var17);
                            this.selectedIndex = var11;
                            this.lastClickTime = System.currentTimeMillis();
                        } else if (mouseX >= boxLeft && mouseX <= boxRight && var10 < 0) {
                            func_77224_a(mouseX - boxLeft, ((mouseY - this.top) + ((int) this.scrollDistance)) - 4);
                            var18 = false;
                        }
                        if (mouseX >= scrollBarXStart && mouseX <= scrollBarXEnd) {
                            this.scrollFactor = -1.0f;
                            int var19 = getContentHeight() - ((this.bottom - this.top) - 4);
                            if (var19 < 1) {
                                var19 = 1;
                            }
                            int var13 = (int) (((this.bottom - this.top) * (this.bottom - this.top)) / getContentHeight());
                            if (var13 < 32) {
                                var13 = 32;
                            }
                            if (var13 > (this.bottom - this.top) - 8) {
                                var13 = (this.bottom - this.top) - 8;
                            }
                            this.scrollFactor /= ((this.bottom - this.top) - var13) / var19;
                        } else {
                            this.scrollFactor = 1.0f;
                        }
                        if (var18) {
                            this.initialMouseClickY = mouseY;
                        } else {
                            this.initialMouseClickY = -2.0f;
                        }
                    } else {
                        this.initialMouseClickY = -2.0f;
                    }
                } else if (this.initialMouseClickY >= 0.0f) {
                    this.scrollDistance -= (mouseY - this.initialMouseClickY) * this.scrollFactor;
                    this.initialMouseClickY = mouseY;
                }
            } else {
                while (Mouse.next()) {
                    int var181 = Mouse.getEventDWheel();
                    if (var181 != 0) {
                        if (var181 > 0) {
                            var181 = -1;
                        } else if (var181 < 0) {
                            var181 = 1;
                        }
                        this.scrollDistance += (var181 * this.slotHeight) / 2;
                    }
                }
                this.initialMouseClickY = -1.0f;
            }
            applyScrollLimits();
            GL11.glDisable(2896);
            GL11.glDisable(2912);
            GL11.glDisable(3553);
            GL11.glEnable(3042);
            GL11.glEnable(3008);
            GL11.glBlendFunc(770, 771);
            GL11.glBegin(7);
            GL11.glColor4f(0.3f, 0.3f, 0.3f, 0.5f);
            addVertexWithUV(this.left, this.bottom, 0.0d, 0.0d, 1.0d);
            addVertexWithUV(this.right, this.bottom, 0.0d, 1.0d, 1.0d);
            addVertexWithUV(this.right, this.top, 0.0d, 1.0d, 0.0d);
            addVertexWithUV(this.left, this.top, 0.0d, 0.0d, 0.0d);
            GL11.glEnd();
            GL11.glEnable(3553);
            int var102 = (this.top + 4) - ((int) this.scrollDistance);
            if (this.field_77243_s) {
            }
            for (int var112 = 0; var112 < ex; var112++) {
                int var192 = var102 + (var112 * this.slotHeight) + this.field_77242_t;
                int var132 = this.slotHeight - 4;
                if (var192 <= this.bottom && var192 + var132 >= this.top) {
                    if (this.showSelectionBox && isSelected(var112)) {
                        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                        GL11.glEnable(3042);
                        GL11.glBlendFunc(770, 771);
                        GL11.glDisable(3553);
                        GL11.glBegin(7);
                        float j = (float) (128.0f / 255.0d);
                        float k = (float) (128.0f / 255.0d);
                        float l = (float) (128.0f / 255.0d);
                        GL11.glColor3f(j, k, l);
                        addVertexWithUV(boxLeft, var192 + var132 + 2, 0.0d, 0.0d, 1.0d);
                        addVertexWithUV(boxRight, var192 + var132 + 2, 0.0d, 1.0d, 1.0d);
                        addVertexWithUV(boxRight, var192 - 2, 0.0d, 1.0d, 0.0d);
                        addVertexWithUV(boxLeft, var192 - 2, 0.0d, 0.0d, 0.0d);
                        GL11.glColor3f(0.0f, 0.0f, 0.0f);
                        addVertexWithUV(boxLeft + 1, var192 + var132 + 1, 0.0d, 0.0d, 1.0d);
                        addVertexWithUV(boxRight - 1, var192 + var132 + 1, 0.0d, 1.0d, 1.0d);
                        addVertexWithUV(boxRight - 1, var192 - 1, 0.0d, 1.0d, 0.0d);
                        addVertexWithUV(boxLeft + 1, var192 - 1, 0.0d, 0.0d, 0.0d);
                        GL11.glEnd();
                        GL11.glEnable(3553);
                    }
                    drawSlot(var112, boxRight, var192, getSize());
                }
            }
            GL11.glDisable(2929);
            overlayBackground(0, this.top, 255, 255);
            overlayBackground(this.bottom, this.listHeight, 255, 255);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glDisable(3008);
            GL11.glShadeModel(7425);
            GL11.glDisable(3553);
            GL11.glBegin(7);
            GL11.glColor3f(0.0f, 0.0f, 0.0f);
            addVertexWithUV(this.left, this.top + 4, 0.0d, 0.0d, 1.0d);
            addVertexWithUV(this.right, this.top + 4, 0.0d, 1.0d, 1.0d);
            GL11.glColor3f(1.0f, 1.0f, 1.0f);
            addVertexWithUV(this.right, this.top, 0.0d, 1.0d, 0.0d);
            addVertexWithUV(this.left, this.top, 0.0d, 0.0d, 0.0d);
            GL11.glEnd();
            GL11.glBegin(7);
            addVertexWithUV(this.left, this.bottom, 0.0d, 0.0d, 1.0d);
            addVertexWithUV(this.right, this.bottom, 0.0d, 1.0d, 1.0d);
            GL11.glColor3f(0.0f, 0.0f, 0.0f);
            addVertexWithUV(this.right, this.bottom - 4, 0.0d, 1.0d, 0.0d);
            addVertexWithUV(this.left, this.bottom - 4, 0.0d, 0.0d, 0.0d);
            GL11.glEnd();
            int var193 = getContentHeight() - ((this.bottom - this.top) - 4);
            if (var193 > 0) {
                int var133 = ((this.bottom - this.top) * (this.bottom - this.top)) / getContentHeight();
                if (var133 < 32) {
                    var133 = 32;
                }
                if (var133 > (this.bottom - this.top) - 8) {
                    var133 = (this.bottom - this.top) - 8;
                }
                int var14 = ((((int) this.scrollDistance) * ((this.bottom - this.top) - var133)) / var193) + this.top;
                if (var14 < this.top) {
                    var14 = this.top;
                }
                GL11.glBegin(7);
                GL11.glColor3f(0.0f, 0.0f, 0.0f);
                addVertexWithUV(scrollBarXStart, this.bottom, 0.0d, 0.0d, 1.0d);
                addVertexWithUV(scrollBarXEnd, this.bottom, 0.0d, 1.0d, 1.0d);
                addVertexWithUV(scrollBarXEnd, this.top, 0.0d, 1.0d, 0.0d);
                addVertexWithUV(scrollBarXStart, this.top, 0.0d, 0.0d, 0.0d);
                GL11.glEnd();
                GL11.glBegin(7);
                float j2 = (float) (128.0f / 255.0d);
                float k2 = (float) (128.0f / 255.0d);
                float l2 = (float) (128.0f / 255.0d);
                GL11.glColor3f(j2, k2, l2);
                addVertexWithUV(scrollBarXStart, var14 + var133, 0.0d, 0.0d, 1.0d);
                addVertexWithUV(scrollBarXEnd, var14 + var133, 0.0d, 1.0d, 1.0d);
                addVertexWithUV(scrollBarXEnd, var14, 0.0d, 1.0d, 0.0d);
                addVertexWithUV(scrollBarXStart, var14, 0.0d, 0.0d, 0.0d);
                GL11.glEnd();
                GL11.glBegin(7);
                float j3 = (float) (192.0f / 255.0d);
                float k3 = (float) (192.0f / 255.0d);
                float l3 = (float) (192.0f / 255.0d);
                GL11.glColor3f(j3, k3, l3);
                addVertexWithUV(scrollBarXStart, (var14 + var133) - 1, 0.0d, 0.0d, 1.0d);
                addVertexWithUV(scrollBarXEnd - 1, (var14 + var133) - 1, 0.0d, 1.0d, 1.0d);
                addVertexWithUV(scrollBarXEnd - 1, var14, 0.0d, 1.0d, 0.0d);
                addVertexWithUV(scrollBarXStart, var14, 0.0d, 0.0d, 0.0d);
                GL11.glEnd();
            }
            func_77215_b(mouseX, mouseY);
            GL11.glEnable(3553);
            GL11.glShadeModel(7424);
            GL11.glEnable(3008);
            GL11.glDisable(3042);
            GL11.glDisable(3089);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        } catch (Throwable var171) {
            var171.printStackTrace();
        }
    }

    private void overlayBackground(int par1, int par2, int par3, int par4) {
    }
}
