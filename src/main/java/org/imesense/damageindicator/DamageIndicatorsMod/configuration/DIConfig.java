package org.imesense.damageindicator.DamageIndicatorsMod.configuration;

import java.awt.RenderingHints;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
/* loaded from: input.jar:DamageIndicatorsMod/configuration/DIConfig.class */
public class DIConfig {
    public final File CONFIG_FILE;
    private static DIConfig diConfig;
    public float Size = 3.0f;
    public float Gravity = 0.8f;
    public float BounceStrength = 1.5f;
    public float ScaleFilter = 0.0f;
    public float transparency = 1.0f;
    public float guiScale = 0.76f;
    public int DIColor = 16755200;
    public int Lifespan = 12;
    public boolean CustomFont = true;
    public int packetrange = 30;
    public boolean alwaysRender = false;
    public boolean portraitEnabled = true;
    public boolean popOffsEnabled = true;
    public boolean enablePotionEffects = true;
    public int mouseoverRange = 30;
    public int healColor = 65280;
    public int portraitLifetime = 160;
    public int locX = 15;
    public int locY = 15;
    public boolean lockPosition = true;
    private String formattedDIColor = "FFAA00";
    private String formattedHealColor = "00FF00";
    public byte checkForUpdates = 2;
    public boolean DebugHidesWindow = true;
    public String selectedSkin = "/assets/defaultskins/default/";
    public boolean alternateRenderingMethod = false;
    public boolean highCompatibilityMod = false;
    public boolean supressBossUI = false;
    public boolean showCriticalStrikes = true;
    public boolean useDropShadows = true;
    public RenderingHints hints = populateHints();

    private DIConfig(File file, int check) {
        this.CONFIG_FILE = file;
        if (check == 1) {
            if (!file.delete()) {
                file.deleteOnExit();
                return;
            }
            return;
        }
        loadConfig();
    }

    public static void loadConfig(File file) {
        if (file == null) {
            diConfig = new DIConfig(mainInstance().CONFIG_FILE, 0);
        } else {
            diConfig = new DIConfig(file, 0);
        }
    }

    public static DIConfig useNewConfig(DIConfig newConfig) {
        diConfig = newConfig;
        return mainInstance();
    }

    public static void overrideConfigAndSave(DIConfig newConfig) {
        try {
            Configuration config = new Configuration(newConfig.CONFIG_FILE);
            config.load();
            config.addCustomCategoryComment("PopOffs", "These Settings effect the digits that bounce off mobs when they take damage.");
            config.addCustomCategoryComment("Portrait", "These Settings effect the current health portrait window on the hud.");
            config.addCustomCategoryComment("PopOffs.Behavior", "This subcategory holds behavioral settings to do with PopOffs.");
            config.addCustomCategoryComment("Portrait.Behavior", "This subcategory holds behavioral settings to do with Portraits.");
            config.addCustomCategoryComment("PopOffs.Appearance", "This subcategory holds appearance settings to do with PopOffs.");
            config.addCustomCategoryComment("Portrait.Appearance", "This subcategory holds appearance settings to do with Portraits.");
            config.addCustomCategoryComment("Internal", "Don't Modify these settings unless explicitly told to or if you know what you are doing.\n This could result in the lose of settings or an unexpected crash.");
            Property prop = config.get("PopOffs.Behavior", "Lifespan", newConfig.Lifespan);
            prop.set(newConfig.Lifespan);
            Property prop2 = config.get("Portrait.Appearance", "Portrait_xPos", newConfig.locX);
            prop2.set(newConfig.locX);
            Property prop3 = config.get("Portrait.Appearance", "Portrait_Skin", newConfig.selectedSkin);
            prop3.set(newConfig.selectedSkin);
            Property prop4 = config.get("Portrait.Appearance", "Portrait_yPos", newConfig.locY);
            prop4.set(newConfig.locY);
            Property prop5 = config.get("PopOffs.Appearance", "Always_Render", newConfig.alwaysRender);
            prop5.set(newConfig.alwaysRender);
            Property prop6 = config.get("PopOffs.Behavior", "UpdateBehavior", newConfig.checkForUpdates);
            prop6.set(newConfig.checkForUpdates);
            Property prop7 = config.get("PopOffs.Appearance", "Transparency", newConfig.transparency);
            prop7.set(newConfig.transparency);
            Property prop8 = config.get("Portrait.Appearance", "Lock_Mob_Position", newConfig.lockPosition);
            prop8.set(newConfig.lockPosition);
            Property prop9 = config.get("Portrait.Appearance", "Gui_Scale", newConfig.guiScale);
            prop9.set(newConfig.guiScale);
            Property prop10 = config.get("PopOffs.Appearance", "Build_TexturePack_Font", newConfig.CustomFont);
            prop10.set(newConfig.CustomFont);
            Property prop11 = config.get("PopOffs.Appearance", "Scale_Smoothing_Filter", newConfig.ScaleFilter);
            prop11.set(newConfig.ScaleFilter);
            newConfig.formattedDIColor = Integer.toHexString(newConfig.DIColor);
            Property prop12 = config.get("PopOffs.Appearance", "Color", newConfig.formattedDIColor);
            prop12.set(newConfig.formattedDIColor);
            Property prop13 = config.get("PopOffs.Appearance", "Heal_Color", Integer.toHexString(newConfig.healColor & 16777215));
            prop13.set(Integer.toHexString(newConfig.healColor));
            Property prop14 = config.get("Portrait.Appearance", "Range", newConfig.mouseoverRange);
            prop14.set(newConfig.mouseoverRange);
            Property prop15 = config.get("Portrait.Behavior", "Enable", newConfig.portraitEnabled);
            prop15.set(newConfig.portraitEnabled);
            Property prop16 = config.get("PopOffs.Behavior", "Gravity", newConfig.Gravity);
            prop16.set(newConfig.Gravity);
            Property prop17 = config.get("PopOffs.Behavior", "Enabled", newConfig.popOffsEnabled);
            prop17.set(newConfig.popOffsEnabled);
            Property prop18 = config.get("PopOffs.Behavior", "Bounce_Strength", newConfig.BounceStrength);
            prop18.set(newConfig.BounceStrength);
            Property prop19 = config.get("PopOffs.Behavior", "Range", newConfig.packetrange);
            prop19.set(newConfig.packetrange);
            Property prop20 = config.get("PopOffs.Behavior", "Size", newConfig.Size);
            prop20.set(newConfig.Size);
            Property prop21 = config.get("Portrait.Behavior", "Portrait_Lifetime", newConfig.portraitLifetime);
            prop21.set(newConfig.portraitLifetime);
            Property prop22 = config.get("Portrait.Behavior", "Show Potion Effects", newConfig.enablePotionEffects);
            prop22.set(newConfig.enablePotionEffects);
            Property prop23 = config.get("Portrait.Behavior", "DebugHidesWindow", newConfig.DebugHidesWindow);
            prop23.set(newConfig.DebugHidesWindow);
            Property prop24 = config.get("Portrait.Behavior", "SupressBossHealth", newConfig.supressBossUI);
            prop24.set(newConfig.supressBossUI);
            Property prop25 = config.get("Portrait.Behavior", "AlternateRenderMethod", newConfig.alternateRenderingMethod);
            prop25.set(newConfig.alternateRenderingMethod);
            Property prop26 = config.get("Portrait.Behavior", "HighCompatibilityMode", newConfig.highCompatibilityMod);
            prop26.set(newConfig.highCompatibilityMod);
            Property prop27 = config.get("PopOffs.Behavior", "ShowCriticalHits", newConfig.showCriticalStrikes);
            prop27.set(newConfig.showCriticalStrikes);
            Property prop28 = config.get("PopOffs.Appearance", "useDropShadows", newConfig.useDropShadows);
            prop28.set(newConfig.useDropShadows);
            Property prop29 = config.get("Internal", "version", "");
            prop29.set("1");
            config.save();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    public static DIConfig mainInstance() {
        return diConfig;
    }

    private void loadConfig() {
        try {
            boolean flag = this.CONFIG_FILE.exists();
            Configuration config = new Configuration(this.CONFIG_FILE);
            config.load();
            if (flag && !config.get("Internal", "version", "0").getString().equals("1")) {
                overrideConfigAndSave(new DIConfig(this.CONFIG_FILE, 1));
                return;
            }
            config.addCustomCategoryComment("PopOffs", "These Settings effect the digits that bounce off mobs when they take damage.");
            config.addCustomCategoryComment("Portrait", "These Settings effect the current health portrait window on the hud.");
            config.addCustomCategoryComment("PopOffs.Behavior", "This subcategory holds behavioral settings to do with PopOffs.");
            config.addCustomCategoryComment("Portrait.Behavior", "This subcategory holds behavioral settings to do with Portraits.");
            config.addCustomCategoryComment("PopOffs.Appearance", "This subcategory holds appearance settings to do with PopOffs.");
            config.addCustomCategoryComment("Portrait.Appearance", "This subcategory holds appearance settings to do with Portraits.");
            config.addCustomCategoryComment("Internal", "Don't Modify these settings unless explicitly told to or if you know what you are doing.\n This could result in the lose of settings or an unexpected crash.");
            Property prop = config.get("PopOffs.Behavior", "Lifespan", this.Lifespan);
            this.Lifespan = prop.getInt(this.Lifespan);
            prop.set(this.Lifespan);
            this.locX = config.get("Portrait.Appearance", "Portrait_xPos", this.locX).getInt(this.locX);
            this.locY = config.get("Portrait.Appearance", "Portrait_yPos", this.locY).getInt(this.locY);
            this.selectedSkin = config.get("Portrait.Appearance", "Portrait_Skin", this.selectedSkin).getString();
            this.alwaysRender = config.get("PopOffs.Appearance", "Enable_Depth_Test", this.alwaysRender).getBoolean(this.alwaysRender);
            this.checkForUpdates = (byte) config.get("PopOffs.Behavior", "UpdateBehavior", this.checkForUpdates).getInt(this.checkForUpdates);
            this.supressBossUI = config.get("Portrait.Behavior", "SupressBossHealth", this.supressBossUI).getBoolean(this.supressBossUI);
            this.transparency = (float) config.get("PopOffs.Appearance", "Transparency", this.transparency).getDouble(this.transparency);
            Property prop2 = config.get("Portrait.Appearance", "Gui_Scale", this.guiScale);
            this.guiScale = (float) prop2.getDouble(this.guiScale);
            prop2.set(this.guiScale);
            Property prop3 = config.get("Portrait.Appearance", "Lock_Mob_Position", this.lockPosition);
            this.lockPosition = prop3.getBoolean(this.lockPosition);
            prop3.set(this.lockPosition);
            Property prop4 = config.get("PopOffs.Appearance", "Build_TexturePack_Font", this.CustomFont);
            this.CustomFont = prop4.getBoolean(this.CustomFont);
            prop4.set(this.CustomFont);
            this.ScaleFilter = (float) config.get("PopOffs.Appearance", "Scale_Smoothing_Filter", this.ScaleFilter).getDouble(this.ScaleFilter);
            this.formattedDIColor = config.get("PopOffs.Appearance", "Color", this.formattedDIColor).getString();
            this.DIColor = (int) Long.parseLong(this.formattedDIColor, 16);
            this.formattedHealColor = config.get("PopOffs.Appearance", "Heal_Color", this.formattedHealColor).getString();
            this.healColor = (int) Long.parseLong(this.formattedHealColor, 16);
            Property prop5 = config.get("Portrait.Appearance", "Range", this.mouseoverRange);
            this.mouseoverRange = prop5.getInt(this.mouseoverRange);
            if (this.mouseoverRange <= 0) {
                this.mouseoverRange = 20;
            }
            if (this.mouseoverRange > 200) {
                this.mouseoverRange = 200;
            }
            prop5.set(this.mouseoverRange);
            this.portraitEnabled = config.get("Portrait.Behavior", "Enable", this.portraitEnabled).getBoolean(this.portraitEnabled);
            Property prop6 = config.get("PopOffs.Behavior", "Gravity", "1.600");
            try {
                this.Gravity = Float.valueOf(prop6.getString()).floatValue();
            } catch (NumberFormatException e) {
                this.Gravity = 0.8f;
                prop6.set(this.Gravity);
            }
            this.popOffsEnabled = config.get("PopOffs.Behavior", "Enabled", this.popOffsEnabled).getBoolean(this.popOffsEnabled);
            Property prop7 = config.get("PopOffs.Behavior", "Bounce_Strength", this.BounceStrength);
            try {
                this.BounceStrength = Float.valueOf(prop7.getString()).floatValue();
            } catch (NumberFormatException e2) {
                this.BounceStrength = 1.5f;
                prop7.set(this.BounceStrength);
            }
            this.packetrange = config.get("PopOffs.Behavior", "Range", this.packetrange).getInt(this.packetrange);
            Property prop8 = config.get("PopOffs.Behavior", "Size", this.Size);
            try {
                this.Size = Float.valueOf(prop8.getString()).floatValue();
            } catch (NumberFormatException e3) {
                this.Size = 3.0f;
                prop8.set(this.Size);
            }
            this.portraitLifetime = config.get("Portrait.Behavior", "Portrait_Lifetime", this.portraitLifetime).getInt(this.portraitLifetime);
            this.enablePotionEffects = config.get("Portrait.Behavior", "Show Potion Effects", this.enablePotionEffects).getBoolean(this.enablePotionEffects);
            this.DebugHidesWindow = config.get("Portrait.Behavior", "DebugHidesWindow", this.DebugHidesWindow).getBoolean(this.DebugHidesWindow);
            this.alternateRenderingMethod = config.get("Portrait.Behavior", "AlternateRenderMethod", this.alternateRenderingMethod).getBoolean(this.alternateRenderingMethod);
            this.highCompatibilityMod = config.get("Portrait.Behavior", "HighCompatibilityMode", this.highCompatibilityMod).getBoolean(this.highCompatibilityMod);
            this.showCriticalStrikes = config.get("PopOffs.Behavior", "ShowCriticalHits", this.showCriticalStrikes).getBoolean(this.showCriticalStrikes);
            this.useDropShadows = config.get("PopOffs.Appearance", "useDropShadows", this.useDropShadows).getBoolean(this.useDropShadows);
            config.get("Internal", "version", "").set("1");
            config.save();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    private RenderingHints populateHints() {
        Map hintsMap = new HashMap();
        hintsMap.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        hintsMap.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        return new RenderingHints(hintsMap);
    }
}
