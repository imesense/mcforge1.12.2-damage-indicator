package DamageIndicatorsMod.gui;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
/* loaded from: input.jar:DamageIndicatorsMod/gui/GuiToolTip.class */
public class GuiToolTip extends Gui {
    public int HEIGHT;
    private final AdvancedGui PARENT;
    public String[] stringLines;
    public int WIDTH;
    public float alpha = 1.0f;
    public int borderColor = -12320649;
    public int borderWidth = 1;
    public boolean Centered = true;
    public boolean centerVertically = true;
    public int fontColor = -1;
    public int gradientEnd = -16777216;
    public int gradientStart = -16777216;
    public int iconIndex = 0;
    public int lineSpacing = 11;
    public String TextureFile = null;
    public boolean useTexture = false;
    public int xPos = 0;
    public int yPos = 0;
    FontRenderer cfr = Minecraft.func_71410_x().field_71466_p;

    public GuiToolTip(AdvancedGui parentGui, int width, int height) {
        this.HEIGHT = 128;
        this.WIDTH = 48;
        this.PARENT = parentGui;
        this.WIDTH = width;
        this.HEIGHT = height;
    }

    public void drawCenteredStringNoShadow(FontRenderer par1FontRenderer, String par2Str, int par3, int par4, int par5) {
        this.cfr.func_78264_a(true);
        if (((par5 >> 24) & 255) > 16) {
            this.cfr.func_175065_a(par2Str, MathHelper.func_76141_d(par3 - ((par1FontRenderer.func_78256_a(par2Str) / 2.0f) * 0.75f)) - 8, par4, par5, false);
        }
        this.cfr.func_78264_a(false);
    }

    public void drawStrings(FontRenderer par1FontRenderer) {
        drawStrings(par1FontRenderer, this.xPos, this.yPos);
    }

    public void drawStrings(FontRenderer par1FontRenderer, int x, int y) {
        drawStrings(par1FontRenderer, x, y, this.borderColor, this.gradientStart, this.gradientEnd, this.fontColor);
    }

    public void drawStrings(FontRenderer par1FontRenderer, int x, int y, int border, int gradStart, int gradEnd, int fontcolor) {
        drawStrings(par1FontRenderer, x, y, border, gradStart, gradEnd, fontcolor, this.stringLines);
    }

    public void drawStrings(FontRenderer par1FontRenderer, int x, int y, int border, int gradStart, int gradEnd, int font, boolean centered, String[] lines) {
        GL11.glDepthFunc(519);
        float[] components = new Color(this.gradientStart).getComponents(new float[4]);
        GL11.glColor4f(components[0], components[1], components[2], components[3]);
        if (this.useTexture) {
            int lineNumber = 0 + ((this.iconIndex % 8) * 18);
            int arr$ = 198 + ((this.iconIndex / 8) * 18);
            this.PARENT.func_73729_b(x, y, lineNumber, arr$, this.WIDTH, this.HEIGHT);
        } else {
            this.PARENT.func_73733_a(x, y, x + this.WIDTH, y + this.HEIGHT, gradStart, gradEnd);
            func_73734_a(x, y, x + this.WIDTH, y + this.borderWidth, border);
            func_73734_a(x, (y + this.HEIGHT) - this.borderWidth, x + this.WIDTH, y + this.HEIGHT, border);
            func_73734_a(x, y, x + this.borderWidth, y + this.HEIGHT, border);
            func_73734_a((x + this.WIDTH) - this.borderWidth, y, x + this.WIDTH, y + this.HEIGHT, border);
        }
        int lineNumber2 = 0;
        for (String string : lines) {
            int linecount = lines.length;
            int verticalOffset = MathHelper.func_76141_d((this.HEIGHT / 2.0f) - (((linecount * (par1FontRenderer.field_78288_b + 2.0f)) * 1.0f) / 2.0f));
            if (centered) {
                if (this.centerVertically) {
                    drawCenteredStringNoShadow(par1FontRenderer, string, x + (this.WIDTH / 2), y + verticalOffset + (lineNumber2 * (par1FontRenderer.field_78288_b + 2)), font);
                } else {
                    drawCenteredStringNoShadow(par1FontRenderer, string, x + (this.WIDTH / 2), y + 3 + (lineNumber2 * this.lineSpacing), font);
                }
            } else {
                par1FontRenderer.func_78276_b(string, x + 3, y + 3 + (lineNumber2 * this.lineSpacing), font);
            }
            lineNumber2++;
        }
        GL11.glDepthFunc(515);
        GL11.glClear(256);
    }

    public void drawStrings(FontRenderer par1FontRenderer, int x, int y, int border, int gradStart, int gradEnd, int font, String[] lines) {
        drawStrings(par1FontRenderer, x, y, border, gradStart, gradEnd, font, this.Centered, lines);
    }

    public void drawStrings(FontRenderer par1FontRenderer, int x, int y, String[] lines) {
        drawStrings(par1FontRenderer, x, y, this.borderColor, this.gradientStart, this.gradientEnd, this.fontColor, lines);
    }

    public void drawStrings(FontRenderer par1FontRenderer, String[] lines) {
        drawStrings(par1FontRenderer, this.xPos, this.yPos, lines);
    }

    public void drawStringsWithDifferentColors(FontRenderer par1FontRenderer, int x, int y, int border, int gradStart, int gradEnd, boolean centered, String colonDelimetedString, int[] colors) {
        drawStringsWithDifferentColors(par1FontRenderer, x, y, border, gradStart, gradEnd, centered, colonDelimetedString.split(":"), colors);
    }

    public void drawStringsWithDifferentColors(FontRenderer par1FontRenderer, int x, int y, int border, int gradStart, int gradEnd, boolean centered, String[] lines, int[] colors) {
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0f, 0.0f, 1800.0f);
        if (lines.length != colors.length) {
            throw new IllegalArgumentException("The number of string lines must be equal to the number of colors passed in");
        }
        if (this.useTexture) {
            float[] arr$ = new Color(this.gradientStart).getComponents(new float[4]);
            GL11.glColor4f(arr$[0], arr$[1], arr$[2], arr$[3]);
            int len$ = 0 + ((this.iconIndex % 8) * 18);
            int i$ = 198 + ((this.iconIndex / 8) * 18);
            this.PARENT.func_73729_b(x, y, len$, i$, this.WIDTH, this.HEIGHT);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        } else {
            this.PARENT.func_73733_a(x, y, this.WIDTH, this.HEIGHT, gradStart, gradEnd);
            func_73734_a(x, y, x + this.WIDTH, y + this.borderWidth, border);
            func_73734_a(x, (y + this.HEIGHT) - this.borderWidth, x + this.WIDTH, y + this.HEIGHT, border);
            func_73734_a(x, y, x + this.borderWidth, y + this.HEIGHT, border);
            func_73734_a((x + this.WIDTH) - this.borderWidth, y, x + this.WIDTH, y + this.HEIGHT, border);
        }
        int var18 = 0;
        for (String string : lines) {
            int linecount = lines.length;
            int verticalSpacing = MathHelper.func_76141_d(this.HEIGHT / (linecount + 1));
            if (centered) {
                if (this.centerVertically) {
                    func_73732_a(par1FontRenderer, string, x + (this.WIDTH / 2), (y + (verticalSpacing * var18)) - (par1FontRenderer.field_78288_b / 2), colors[var18]);
                } else {
                    func_73732_a(par1FontRenderer, string, x + (this.WIDTH / 2), y + 3 + (var18 * this.lineSpacing), colors[var18]);
                }
            } else {
                func_73731_b(par1FontRenderer, string, x + 3, y + 3 + (var18 * this.lineSpacing), colors[var18]);
            }
            var18++;
        }
        GL11.glPopMatrix();
    }

    public static void addVertexWithUV(double x, double y, double z, double u, double v) {
        GL11.glTexCoord2d(u, v);
        GL11.glVertex3d(x, y, z);
    }

    public static void addVertex(double x, double y, double z) {
        GL11.glVertex3d(x, y, z);
    }

    public void drawStringsWithDifferentColors(FontRenderer par1FontRenderer, int[] colors) {
        drawStringsWithDifferentColors(par1FontRenderer, this.xPos, this.yPos, this.borderColor, this.gradientStart, this.gradientEnd, this.Centered, this.stringLines, colors);
    }

    public boolean isCentered() {
        return this.Centered;
    }

    public boolean isCenterVertically() {
        return this.centerVertically;
    }

    public boolean isUsingTexture() {
        return this.useTexture;
    }

    public void setBasicColors(int border, int gradStart, int gradEnd, int font) {
        this.borderColor = border;
        this.gradientStart = gradStart;
        this.gradientEnd = gradEnd;
        this.fontColor = font;
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
    }

    public void setCentered(boolean Centered) {
        this.Centered = Centered;
    }

    public void setCenterVertically(boolean centerVertically) {
        this.centerVertically = centerVertically;
    }

    public void setDontUseTexture() {
        this.useTexture = false;
    }

    public void setGlobalAlpha(float trans) {
        this.alpha = trans > 0.0f ? trans > 1.0f ? 1.0f : trans : 0.0f;
        Color color = new Color(this.borderColor);
        float[] temp = color.getColorComponents(new float[3]);
        Color color2 = new Color(temp[0], temp[1], temp[2], this.alpha);
        this.borderColor = color2.getRGB();
        Color color3 = new Color(this.gradientStart);
        float[] temp2 = color3.getColorComponents(new float[3]);
        Color color4 = new Color(temp2[0], temp2[1], temp2[2], this.alpha);
        this.gradientStart = color4.getRGB();
        Color color5 = new Color(this.gradientEnd);
        float[] temp3 = color5.getColorComponents(new float[3]);
        Color color6 = new Color(temp3[0], temp3[1], temp3[2], this.alpha);
        this.gradientEnd = color6.getRGB();
        Color color7 = new Color(this.fontColor);
        float[] temp4 = color7.getColorComponents(new float[3]);
        Color color8 = new Color(temp4[0], temp4[1], temp4[2], this.alpha);
        this.fontColor = color8.getRGB();
    }

    public void setGlobalAlpha(int trans) {
        setGlobalAlpha(((trans > 0 ? trans > 255 ? 255 : trans : 0) / 255.0f) * 1.0f);
    }

    public void setLineSpacing(int lineSpacing) {
        this.lineSpacing = lineSpacing;
    }

    public void setPos(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public void setStringLines(String[] stringLines) {
        this.stringLines = stringLines;
    }

    public void setTextLines(String[] lines) {
        this.stringLines = lines;
    }

    public void setTextureFile(String TextureFile, int iconIndex) {
        this.TextureFile = TextureFile;
        this.iconIndex = iconIndex;
        this.useTexture = true;
    }

    public void setUpForDraw(int x, int y) {
        setUpForDraw(x, y, this.borderColor, this.gradientStart, this.gradientEnd, this.fontColor, this.Centered, this.centerVertically);
    }

    public void setUpForDraw(int x, int y, int borderColor, int baseColor, int gradEndColor, int defaultFontColor, boolean centeredHorizontally, boolean centeredVerTically) {
        setUpForDraw(x, y, borderColor, baseColor, gradEndColor, defaultFontColor, centeredHorizontally, centeredVerTically, this.WIDTH, this.HEIGHT);
    }

    public void setUpForDraw(int x, int y, int borderColor, int baseColor, int gradEndColor, int defaultFontColor, boolean centeredHorizontally, boolean centeredVerTically, int newWidth, int newHeight) {
        setUpForDraw(x, y, borderColor, baseColor, gradEndColor, defaultFontColor, centeredHorizontally, centeredVerTically, newWidth, newHeight, this.TextureFile, this.iconIndex);
    }

    public void setUpForDraw(int x, int y, int borderColor, int baseColor, int gradEndColor, int defaultFontColor, boolean centeredHorizontally, boolean centeredVertically, int newWidth, int newHeight, String texture, int iconIndex) {
        setUpForDraw(x, y, borderColor, baseColor, gradEndColor, defaultFontColor, centeredHorizontally, centeredVertically, newWidth, newHeight, texture, iconIndex, this.stringLines);
    }

    public void setUpForDraw(int x, int y, int borderColor, int baseColor, int gradEndColor, int defaultFontColor, boolean centeredHorizontally, boolean centeredVertically, int newWidth, int newHeight, String texture, int iconIndex, String[] lines) {
        this.xPos = x;
        this.yPos = y;
        this.borderColor = borderColor;
        this.gradientStart = baseColor;
        this.gradientEnd = gradEndColor;
        this.fontColor = defaultFontColor;
        this.Centered = centeredHorizontally;
        this.centerVertically = centeredVertically;
        this.WIDTH = newWidth;
        this.HEIGHT = newHeight;
        this.stringLines = lines;
        if (texture != null && !"".equals(texture)) {
            setTextureFile(texture, iconIndex);
        } else {
            setDontUseTexture();
        }
    }

    public void setUpForDraw(int x, int y, String[] lines) {
        this.stringLines = lines;
        setUpForDraw(x, y, this.borderColor, this.gradientStart, this.gradientEnd, this.fontColor, this.Centered, this.centerVertically);
    }
}
