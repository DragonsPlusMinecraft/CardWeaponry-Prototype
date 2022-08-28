package plus.dragons.card_weaponery;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import plus.dragons.card_weaponery.card.CardFeature;

import java.util.function.Supplier;

public class CardWeaponryForgeRegistries{
        public static Supplier<IForgeRegistry<CardFeature>> CARD_FEATURES = null;
        public static final ResourceKey<Registry<CardFeature>> CARD_FEATURES_KEY = ResourceKey.createRegistryKey(new ResourceLocation(CardWeaponry.MODID,"card_feature"));

}
