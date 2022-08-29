package plus.dragons.card_weaponery.ini;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryBuilder;
import plus.dragons.card_weaponery.CardWeaponry;
import plus.dragons.card_weaponery.forgeregistry.CardWeaponryRegistries;
import plus.dragons.card_weaponery.card.CardFeature;

public class ForgeRegistryEntryRegistry {

   public static final DeferredRegister<CardFeature> CARD_FEATURE_REGISTRY = DeferredRegister.create(CardWeaponryRegistries.CARD_FEATURES_KEY, CardWeaponry.MODID);

    public static void register(IEventBus bus) {
        CardWeaponryRegistries.CARD_FEATURES = CARD_FEATURE_REGISTRY.makeRegistry(CardFeature.class, RegistryBuilder::new);
        CARD_FEATURE_REGISTRY.register(bus);
    }
}
