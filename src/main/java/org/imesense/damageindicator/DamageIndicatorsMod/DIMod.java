package DamageIndicatorsMod;

import DamageIndicatorsMod.configuration.DIConfig;
import DamageIndicatorsMod.core.DIPermissions;
import DamageIndicatorsMod.core.DIPotionEffects;
import DamageIndicatorsMod.server.CommandDI;
import DamageIndicatorsMod.server.DIProxy;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.command.ServerCommandManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;
@Mod(useMetadata = true, modid = "damageindicatorsmod", name = "Damage Indicators Mod", acceptableRemoteVersions = "*", acceptedMinecraftVersions = "[1.12.2]")
/* loaded from: input.jar:DamageIndicatorsMod/DIMod.class */
public class DIMod {
    public static Logger log;
    @Mod.Instance("damageindicatorsmod")
    public static DIMod instance;
    @SidedProxy(clientSide = "DamageIndicatorsMod.client.DIClientProxy", serverSide = "DamageIndicatorsMod.server.DIProxy", modId = "damageindicatorsmod")
    public static DIProxy proxy;
    int packetID = 0;
    CommandDI cdi = new CommandDI();
    public static boolean s_bUpdateMessageSent = false;
    public static String s_sUpdateMessage = "";
    public static Set<String> donators = new HashSet();
    public static final SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel("DIMod");

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
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

    @Mod.EventHandler
    public void load(FMLInitializationEvent event) {
        proxy.register();
    }

    @Mod.EventHandler
    public void load(FMLPostInitializationEvent event) {
    }

    @Mod.EventHandler
    public void serverStarted(FMLServerStartedEvent evt) {
        try {
            ServerCommandManager scm = FMLCommonHandler.instance().getMinecraftServerInstance().func_71187_D();
            if (!scm.func_71555_a().containsKey(this.cdi.func_71517_b())) {
                scm.func_71560_a(this.cdi);
            }
        } catch (Throwable th) {
        }
    }
}
