package org.imesense.damageindicator;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.Logger;

import net.minecraft.client.resources.I18n;
import net.minecraft.command.ServerCommandManager;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.imesense.damageindicator.DamageIndicatorsMod.configuration.DIConfig;
import org.imesense.damageindicator.DamageIndicatorsMod.core.DIPermissions;
import org.imesense.damageindicator.DamageIndicatorsMod.core.DIPotionEffects;
import org.imesense.damageindicator.DamageIndicatorsMod.server.CommandDI;
import org.imesense.damageindicator.DamageIndicatorsMod.server.DIProxy;

/**
 * Main class for Damage Indicator modification
 * <p>
 * This class serves as the primary entry point for the mod and handles all major
 * lifecycle events in the Forge mod loading process. It includes functionality for:
 * <ul>
 *     <li>Mixin configuration loading</li>
 *     <li>Localization support</li>
 *     <li>Standard Forge mod lifecycle management</li>
 * </ul>
 *
 * {@code @Mod} annotation configures the basic mod metadata and dependencies.
 *
 * @see Mod
 */
@Mod(
    useMetadata = true,
    modid = DamageIndicatorMod.MOD_ID,
    name = DamageIndicatorMod.NAME,
    version = DamageIndicatorMod.VERSION,
    dependencies = "required-after:fermiumbooter",
    acceptableRemoteVersions = "*",
    acceptedMinecraftVersions = "[1.12.2]"
)
public final class DamageIndicatorMod
{
    /**
     * Modification unique identifier
     */
    public static final String MOD_ID = "damageindicator";

    /**
     * Modification name
     */
    public static final String NAME = "Damage Indicator";

    /**
     * Minecraft version
     */
    public static final String VERSION = "1.12.2-14.23.5.2864";

    public static Logger log;
    @Mod.Instance("damageindicator")
    public static DamageIndicatorMod instance;
    @SidedProxy(
        clientSide = "org.imesense.damageindicator.DamageIndicatorsMod.client.DIClientProxy",
        serverSide = "org.imesense.damageindicator.DamageIndicatorsMod.server.DIProxy",
        modId = "damageindicator"
    )
    public static DIProxy proxy;
    int packetID = 0;
    CommandDI cdi = new CommandDI();
    public static boolean s_bUpdateMessageSent = false;
    public static String s_sUpdateMessage = "";
    public static Set<String> donators = new HashSet();
    public static final SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel("DIMod");

    /**
     * Logs a method call to the logger.
     *
     * @param methodName the name of the method being called
     */
    private void logMethodCall(String methodName)
    {
        log.info(
            "Called {}.{} method",
            this.getClass().getName(),
            methodName
        );
    }

    /**
     * Sets the localized metadata for the mod (client-side only).
     *
     * @param event pre-initialization event containing mod metadata
     *
     * @see FMLPreInitializationEvent
     * @see ModMetadata
     * @see I18n
     */
    @SideOnly(Side.CLIENT)
    static void setLocaleMetadata(FMLPreInitializationEvent event)
    {
        ModMetadata metadata = event.getModMetadata();
        metadata.name = I18n.format("mod." + MOD_ID + ".name");
        metadata.description = I18n.format("mod." + MOD_ID + ".description");
    }

    /**
     * Handles the mod pre-initialization phase.
     * <p>
     * This method:
     * <ul>
     *   <li>Sets up localization (client-side only)</li>
     *   <li>Loads and validates Mixin configuration files</li>
     *   <li>Logs all Mixin configurations found</li>
     * </ul>
     *
     * @param event the pre-initialization event
     *
     * @see FMLPreInitializationEvent
     */
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        log = event.getModLog();
        try {
            DIConfig.loadConfig(event.getSuggestedConfigurationFile());
        } catch (Throwable ex) {
            ex.printStackTrace();
            if (!event.getSuggestedConfigurationFile().delete()) {
                event.getSuggestedConfigurationFile().deleteOnExit();
            }
            DIConfig.loadConfig(event.getSuggestedConfigurationFile());
        }
        try {
            network.registerMessage(DIPermissions.Handler.class, DIPermissions.class, this.packetID, Side.SERVER);
            SimpleNetworkWrapper simpleNetworkWrapper = network;
            int i = this.packetID;
            this.packetID = i + 1;
            simpleNetworkWrapper.registerMessage(DIPermissions.Handler.class, DIPermissions.class, i, Side.CLIENT);
            network.registerMessage(DIPotionEffects.Handler.class, DIPotionEffects.class, this.packetID, Side.SERVER);
            SimpleNetworkWrapper simpleNetworkWrapper2 = network;
            int i2 = this.packetID;
            this.packetID = i2 + 1;
            simpleNetworkWrapper2.registerMessage(DIPotionEffects.Handler.class, DIPotionEffects.class, i2, Side.CLIENT);
        } catch (Throwable ex2) {
            ex2.printStackTrace();
        }
    }

    /**
     * Handles the mod initialization phase.
     *
     * @param event the initialization event
     *
     * @see FMLInitializationEvent
     */
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        logMethodCall(
            new Object(){}
                .getClass()
                .getEnclosingMethod()
                .getName()
        );
    }

    /**
     * Handles the mod post-initialization phase.
     *
     * @param event the post-initialization event
     *
     * @see FMLPostInitializationEvent
     */
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        logMethodCall(
            new Object(){}
                .getClass()
                .getEnclosingMethod()
                .getName()
        );
    }

    @EventHandler
    public void load(FMLInitializationEvent event) {
        proxy.register();
    }

    @EventHandler
    public void load(FMLPostInitializationEvent event) {
    }

    /**
     * Handles the load complete event.
     *
     * @param event the load complete event
     *
     * @see FMLLoadCompleteEvent
     */
    @EventHandler
    public void onLoadComplete(FMLLoadCompleteEvent event)
    {
        logMethodCall(
            new Object(){}
                .getClass()
                .getEnclosingMethod()
                .getName()
        );
    }

    /**
     * Handles server starting event.
     *
     * @param event the server starting event
     *
     * @see FMLServerStartingEvent
     */
    @EventHandler
    public void serverLoad(FMLServerStartingEvent event)
    {
        logMethodCall(
            new Object(){}
                .getClass()
                .getEnclosingMethod()
                .getName()
        );
    }

    @EventHandler
    public void serverStarted(FMLServerStartedEvent evt) {
        try {
            ServerCommandManager scm = (ServerCommandManager) FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager();
            if (!scm.getCommands().containsKey(this.cdi.getName())) {
                scm.registerCommand(this.cdi);
            }
        } catch (Throwable th) {
        }
    }

    /**
     * Handles server stopped event.
     *
     * @param event the server stopped event
     *
     * @see FMLServerStoppedEvent
     */
    @EventHandler
    public void serverStopped(FMLServerStoppedEvent event)
    {
        logMethodCall(
            new Object(){}
                .getClass()
                .getEnclosingMethod()
                .getName()
        );
    }
}
