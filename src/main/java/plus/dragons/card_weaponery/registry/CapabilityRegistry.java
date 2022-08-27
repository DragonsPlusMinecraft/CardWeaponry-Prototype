package plus.dragons.card_weaponery.registry;

import plus.dragons.card_weaponery.capability.cardtype.CardTypeData;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CapabilityRegistry {
    @SubscribeEvent
    public static void onSetUpEvent(RegisterCapabilitiesEvent event) {
        event.register(CardTypeData.class);
    }
}
