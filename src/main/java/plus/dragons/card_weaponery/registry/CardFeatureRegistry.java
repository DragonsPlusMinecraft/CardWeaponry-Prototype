package plus.dragons.card_weaponery.registry;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import plus.dragons.card_weaponery.CardWeaponry;
import plus.dragons.card_weaponery.CardWeaponryForgeRegistries;
import plus.dragons.card_weaponery.card.BasicFeature;
import plus.dragons.card_weaponery.card.CardFeature;

public class CardFeatureRegistry {
    public static final DeferredRegister<CardFeature> CARD_FEATURES = DeferredRegister.create(CardWeaponryForgeRegistries.CARD_FEATURES_KEY, CardWeaponry.MODID);
    public static final RegistryObject<CardFeature> BASIC = CARD_FEATURES.register("basic", BasicFeature::new);
}
