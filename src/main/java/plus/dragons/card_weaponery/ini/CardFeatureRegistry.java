package plus.dragons.card_weaponery.ini;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import plus.dragons.card_weaponery.CardWeaponry;
import plus.dragons.card_weaponery.forgeregistry.CardWeaponryRegistries;
import plus.dragons.card_weaponery.card.BasicFeature;
import plus.dragons.card_weaponery.card.CardFeature;

public class CardFeatureRegistry {
    public static final DeferredRegister<CardFeature> CARD_FEATURES = DeferredRegister.create(CardWeaponryRegistries.CARD_FEATURES_KEY, CardWeaponry.MODID);
    public static final RegistryObject<CardFeature> BASIC = CARD_FEATURES.register("basic", BasicFeature::new);
}
