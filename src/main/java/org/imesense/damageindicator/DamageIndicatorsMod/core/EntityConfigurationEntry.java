package org.imesense.damageindicator.DamageIndicatorsMod.core;

import org.imesense.damageindicator.DamageIndicatorMod;

import java.io.File;
import java.util.HashMap;
import java.util.regex.Pattern;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import static org.imesense.damageindicator.DamageIndicatorMod.MOD_CONFIG_DIR;

/* loaded from: input.jar:DamageIndicatorsMod/core/EntityConfigurationEntry.class */
public class EntityConfigurationEntry {
    public static HashMap<Integer, Integer> maxHealthOverride = new HashMap<>(200);
    private static boolean lasttimefailed = false;
    public final boolean AppendBaby;
    public final float BabyScaleFactor;
    public final Class Clazz;
    public final float EntitySizeScaling;
    public float eyeHeight;
    public final boolean IgnoreThisMob;
    public int maxHP;
    public final String NameOverride;
    public final float ScaleFactor;
    public final float XOffset;
    public final float YOffset;
    public final boolean DisableMob;

    public void save() {
        saveEntityConfig(this);
    }

    public static EntityConfigurationEntry generateDefaultConfiguration(Configuration config, Class entry) {
        boolean ignore = false;
        float scaleFactor = 22.0f;
        float yOffset = -5.0f;
        float SizeModifier = 0.0f;
        if (entry == EntityIronGolem.class) {
            scaleFactor = 16.0f;
        } else if (entry == EntitySlime.class || entry == EntityMagmaCube.class) {
            scaleFactor = 5.0f;
            SizeModifier = 2.0f;
            yOffset = -5.0f;
        } else if (entry == EntityEnderman.class) {
            scaleFactor = 15.0f;
        } else if (entry == EntityGhast.class) {
            scaleFactor = 7.0f;
            yOffset = -20.0f;
        } else if (entry == EntitySquid.class) {
            yOffset = -17.0f;
        } else if (entry == EntityOcelot.class) {
            scaleFactor = 25.0f;
            yOffset = -5.0f;
        } else if (entry == EntityWither.class) {
            scaleFactor = 15.0f;
            yOffset = 5.0f;
        } else if (EntityPlayer.class.isAssignableFrom(entry)) {
            yOffset = 20.0f;
        } else if (entry.getName().equalsIgnoreCase("thaumcraft.common.entities.EntityWisp")) {
            yOffset = -14.0f;
        } else if (entry.getName().equalsIgnoreCase("drzhark.mocreatures.MoCEntityWerewolf")) {
            scaleFactor = 20.0f;
            yOffset = -4.0f;
        } else if (entry.getName().equalsIgnoreCase("drzhark.mocreatures.MoCEntityOgre")) {
            scaleFactor = 12.0f;
        } else if (entry.getName().equalsIgnoreCase("xolova.blued00r.divinerpg.mobs.EntityCyclops")) {
            scaleFactor = 10.0f;
        } else if (entry.getName().equalsIgnoreCase("xolova.blued00r.divinerpg.mobs.EntityEnergyGolem")) {
            scaleFactor = 10.0f;
        } else if (entry.getName().equalsIgnoreCase("xolova.blued00r.divinerpg.mobs.EntityCaveclops")) {
            scaleFactor = 10.0f;
        } else if (Loader.isModLoaded("RDVehicleTools")) {
            try {
                Class clazz = Class.forName("net.richdigitsmods.vehiclecore.vehicles.EntityVehicleCore");
                if (clazz.isAssignableFrom(entry)) {
                    ignore = true;
                }
            } catch (Throwable th) {
            }
        }
        return loadEntityConfig(config, new EntityConfigurationEntry(entry, scaleFactor, 0.0f, yOffset, SizeModifier, 2.0f, true, "", ignore, 20, 1.5f, false));
    }

    public static EntityConfigurationEntry loadEntityConfig(Configuration config, EntityConfigurationEntry ece) {
        return loadEntityConfig(config, ece, null);
    }

    public static EntityConfigurationEntry loadEntityConfig(Configuration config, EntityConfigurationEntry ece, EntityLiving el) {
        float scaleFactor;
        float xOffset;
        float yOffset;
        float SizeModifier;
        float babyScaleFactor;
        boolean disableMob;
        Class entry = ece.Clazz;
        String mod = "Vanilla";
        EntityRegistry.EntityRegistration er = EntityRegistry.instance().lookupModSpawn(ece.Clazz, true);
        if (er != null) {
            try {
                mod = er.getContainer().getMetadata().name.replaceAll(Pattern.quote("."), "");
            } catch (Throwable th) {
            }
        }
        String CatagoryName = entry.getName();
        if (CatagoryName.lastIndexOf(".") != -1) {
            CatagoryName = CatagoryName.substring(CatagoryName.lastIndexOf("."), CatagoryName.length()).replaceAll(Pattern.quote("."), "");
        }
        String CatagoryName2 = (mod + "." + CatagoryName).replaceAll("[^a-zA-Z0-9\\s\\!\\:\\.\\&\\$]", "");
        config.addCustomCategoryComment(CatagoryName2, "These settings are to help other modders and users to make custom mobs fit correctly in the preview window.");
        Property prop = config.get(CatagoryName2, "Scale_Factor", String.valueOf(ece.ScaleFactor));
        try {
            scaleFactor = Float.valueOf(prop.getString()).floatValue();
        } catch (Throwable th2) {
            System.err.println("Invalid or malformed configuration entry for " + prop.getName());
            scaleFactor = ece.ScaleFactor;
            prop.set(String.valueOf(22.0f));
        }
        String entityName = config.get(CatagoryName2, "Name", "").getString();
        boolean appendBabyName = config.get(CatagoryName2, "Append_Baby_Name", ece.AppendBaby).getBoolean(ece.AppendBaby);
        boolean ignore = config.get(CatagoryName2, "Ignore_This_Mob", ece.IgnoreThisMob).getBoolean(ece.IgnoreThisMob);
        Property prop2 = config.get(CatagoryName2, "X_Offset", String.valueOf(ece.XOffset));
        try {
            xOffset = Float.valueOf(prop2.getString()).floatValue();
        } catch (Throwable th3) {
            System.err.println("Invalid or malformed configuration entry for " + prop2.getName());
            prop2.set(String.valueOf(ece.XOffset));
            xOffset = ece.XOffset;
        }
        Property prop3 = config.get(CatagoryName2, "Y_Offset", String.valueOf(ece.YOffset));
        try {
            yOffset = Float.valueOf(prop3.getString()).floatValue();
        } catch (Throwable th4) {
            System.err.println("Invalid or malformed configuration entry for " + prop3.getName());
            prop3.set(String.valueOf(ece.YOffset));
            yOffset = ece.YOffset;
        }
        Property prop4 = config.get(CatagoryName2, "Size_Modifier", String.valueOf(ece.EntitySizeScaling));
        try {
            SizeModifier = Float.valueOf(prop4.getString()).floatValue();
        } catch (Throwable th5) {
            System.err.println("Invalid or malformed configuration entry for " + prop4.getName());
            prop4.set(String.valueOf(ece.EntitySizeScaling));
            SizeModifier = ece.EntitySizeScaling;
        }
        Property prop5 = config.get(CatagoryName2, "Baby_Scale_Modifier", ece.BabyScaleFactor);
        try {
            babyScaleFactor = Float.valueOf(prop5.getString()).floatValue();
        } catch (Throwable th6) {
            System.err.println("Invalid or malformed configuration entry for " + prop5.getName());
            prop5.set(String.valueOf(ece.BabyScaleFactor));
            babyScaleFactor = ece.BabyScaleFactor;
        }
        Property prop6 = config.get(CatagoryName2, "Disable_Mob", ece.DisableMob);
        try {
            disableMob = Boolean.valueOf(prop6.getString()).booleanValue();
        } catch (Throwable th7) {
            System.err.println("Invalid or malformed configuration entry for " + prop6.getName());
            prop6.set(String.valueOf(ece.DisableMob));
            disableMob = ece.DisableMob;
        }
        EntityConfigurationEntry tmp = new EntityConfigurationEntry(entry, scaleFactor, xOffset, yOffset, SizeModifier, babyScaleFactor, appendBabyName, entityName, ignore, ece.maxHP, ece.eyeHeight, disableMob);
        return tmp;
    }

    public static Configuration getEntityConfiguration() {
        File configDir = MOD_CONFIG_DIR;
        File configfile = new File(configDir, "DIAdvancedCompatibility.cfg");
        try {
            // Создаем директорию, если не существует
            configDir.mkdirs();

            // Создаем файл, если не существует
            if (!configfile.exists()) {
                configfile.createNewFile();
            }

            // Загружаем конфигурацию, если файл существует и не пустой
            Configuration configuration = new Configuration(configfile);
            if (configfile.exists() && configfile.length() > 0) {
                configuration.load();
            }
            return configuration;
        } catch (Exception e) {
            if (configfile.exists()) {
                if (!lasttimefailed) {
                    DamageIndicatorMod.logger.warn("Per mob configuration file was corrupt! Attempting to purge and recreate...");
                    if (!configfile.delete()) {
                        configfile.deleteOnExit();
                    }
                    lasttimefailed = true;
                    return getEntityConfiguration();
                }
                DamageIndicatorMod.logger.warn("Failed to recreate configuration! Configuration should be deleted when minecraft closes.");
                throw new RuntimeException("DIAdvancedCompatibility was corrupt and was unable to recreate the file.");
            }
            throw new RuntimeException("Exception while creating " + configfile.getAbsolutePath(), e);
        }
    }

    public static void saveEntityConfig(EntityConfigurationEntry ece) {
        Class entry = ece.Clazz;
        String mod = "Vanilla";
        EntityRegistry.EntityRegistration er = EntityRegistry.instance().lookupModSpawn(ece.Clazz, true);
        if (er != null) {
            try {
                mod = er.getContainer().getMetadata().name.replaceAll(Pattern.quote("."), "_");
            } catch (Throwable th) {
            }
        }
        String CatagoryName = entry.getName();
        if (CatagoryName.lastIndexOf(".") != -1) {
            CatagoryName = CatagoryName.substring(CatagoryName.lastIndexOf("."), CatagoryName.length()).replaceAll(Pattern.quote("."), "");
        }
        String CatagoryName2 = (mod + "." + CatagoryName).replaceAll("[^a-zA-Z0-9\\s\\!\\:\\.\\&\\$]", "");
        Configuration config = getEntityConfiguration();
        config.addCustomCategoryComment(CatagoryName2, "These settings are to help other modders and users to make custom mobs fit correctly in the preview window.");
        config.get(CatagoryName2, "Scale_Factor", String.valueOf(ece.ScaleFactor)).set(String.valueOf(ece.ScaleFactor));
        if (ece.NameOverride == null || "".equals(ece.NameOverride)) {
            config.get(CatagoryName2, "Name", ece.NameOverride).set("");
        } else {
            config.get(CatagoryName2, "Name", ece.NameOverride).set(ece.NameOverride);
        }
        config.get(CatagoryName2, "Ignore_This_Mob", ece.IgnoreThisMob).set(ece.IgnoreThisMob);
        config.get(CatagoryName2, "Append_Baby_Name", ece.AppendBaby).set(ece.AppendBaby);
        config.get(CatagoryName2, "X_Offset", String.valueOf(ece.XOffset)).set(ece.XOffset);
        config.get(CatagoryName2, "Y_Offset", String.valueOf(ece.YOffset)).set(ece.YOffset);
        config.get(CatagoryName2, "Size_Modifier", String.valueOf(ece.EntitySizeScaling)).set(ece.EntitySizeScaling);
        config.get(CatagoryName2, "Baby_Scale_Modifier", String.valueOf(ece.BabyScaleFactor)).set(ece.BabyScaleFactor);
        config.get(CatagoryName2, "Disable_Mob", String.valueOf(ece.DisableMob)).set(ece.DisableMob);
        config.save();
    }

    public EntityConfigurationEntry(Class clazz, float scale, float xoffset, float yoffset, float sizeScaling, float babyscale, boolean appendBaby, boolean ignoreThisMob, int maxHP, float eyeHeight, boolean disableMob) {
        this(clazz, scale, xoffset, yoffset, sizeScaling, babyscale, appendBaby, "", ignoreThisMob, maxHP, eyeHeight, disableMob);
    }

    public EntityConfigurationEntry(Class clazz, float scale, float xoffset, float yoffset, float sizeScaling, float babyscale, boolean appendBaby, String nameOverride, boolean ignoreThisMob, int maxHP, float eyeHeight, boolean disableMob) {
        this.IgnoreThisMob = ignoreThisMob;
        this.Clazz = clazz;
        this.ScaleFactor = scale;
        this.XOffset = xoffset;
        this.YOffset = yoffset;
        this.EntitySizeScaling = sizeScaling;
        this.BabyScaleFactor = babyscale;
        this.AppendBaby = appendBaby;
        this.DisableMob = disableMob;
        if (nameOverride != null) {
            this.NameOverride = nameOverride;
        } else {
            this.NameOverride = "";
        }
        this.maxHP = maxHP;
        this.eyeHeight = eyeHeight;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (hashCode() != obj.hashCode()) {
            return false;
        }
        return obj.toString().equals(toString());
    }

    public void SetInfo(int maxh, float eyeh) {
        this.maxHP = maxh;
        this.eyeHeight = eyeh;
    }

    public int hashCode() {
        return (this.Clazz.getName() + "-" + this.NameOverride + "-" + this.ScaleFactor + "-" + this.BabyScaleFactor + "-" + this.EntitySizeScaling + "-" + this.eyeHeight + "-" + this.XOffset + "-" + this.YOffset + String.valueOf(this.DisableMob)).hashCode();
    }

    public String toString() {
        String eol = System.getProperty("line.separator");
        StringBuilder output = new StringBuilder();
        output.append(eol).append("---------------------------------").append(eol).append("Class Name: ").append(this.Clazz.getName()).append(eol).append("ScaleFactor: ").append(String.valueOf(this.ScaleFactor)).append(eol).append("Name Override: ").append(this.NameOverride).append(eol).append("AppendBabyName: ").append(String.valueOf(this.AppendBaby)).append(eol).append("X Offset: ").append(String.valueOf(this.XOffset)).append(eol).append("Y Offset: ").append(String.valueOf(this.YOffset)).append(eol).append("Size Modifier: ").append(String.valueOf(this.EntitySizeScaling)).append(eol).append("Baby Scale Modifier: ").append(String.valueOf(this.BabyScaleFactor)).append(eol).append("Ignored: ").append(String.valueOf(this.IgnoreThisMob)).append(eol).append("DisableMob: ").append(String.valueOf(this.DisableMob)).append(eol).append("---------------------------------").append(eol);
        return output.toString();
    }
}
