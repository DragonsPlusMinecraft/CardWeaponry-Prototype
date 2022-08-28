package plus.dragons.card_weaponery.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;
import plus.dragons.card_weaponery.CardWeaponry;
import plus.dragons.card_weaponery.CardWeaponryForgeRegistries;
import plus.dragons.card_weaponery.card.CardFeature;

//@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ForgeRegistryEntryRegistry {

   public static final DeferredRegister<CardFeature> CARD_FEATURE_REGISTRY = DeferredRegister.create(CardWeaponryForgeRegistries.CARD_FEATURES_KEY, CardWeaponry.MODID);

    public static void register(IEventBus bus) {
        System.out.println("Register New Registry");
        CardWeaponryForgeRegistries.CARD_FEATURES = CARD_FEATURE_REGISTRY.makeRegistry(CardFeature.class, RegistryBuilder::new);
        CARD_FEATURE_REGISTRY.register(bus);
    }

    /*@SubscribeEvent
    public void onNewRegistry(NewRegistryEvent event){
        System.out.println("Register New Registry");
        RegistryBuilder<CardFeature> registryBuilder = new RegistryBuilder<>();
        registryBuilder.setName(new ResourceLocation(CardWeaponry.MODID, "card_feature"));
        registryBuilder.setType(CardFeature.class);
        CardWeaponryForgeRegistries.CARD_FEATURES = event.create(registryBuilder);
    }*/
}
