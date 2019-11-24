package appeng.core;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

/**
 * @author BrockWS
 */
@Mod.EventBusSubscriber
public class TestCrash {

    @SubscribeEvent
    public static void onServerStartingEvent(FMLServerStartingEvent event) {
//        if (true)
//            throw new RuntimeException("broke");
    }
}
