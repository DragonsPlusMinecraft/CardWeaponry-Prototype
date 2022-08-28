package plus.dragons.card_weaponery.registry;

import plus.dragons.card_weaponery.CardWeaponry;
import plus.dragons.card_weaponery.entity.CardTrapEntity;
import plus.dragons.card_weaponery.entity.CardEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, CardWeaponry.MODID);
    public static final RegistryObject<EntityType<CardEntity>> FLYING_CARD = ENTITIES.register("flying_card",
            () -> EntityType.Builder.<CardEntity>of(CardEntity::new, MobCategory.MISC)
                    .sized(.6F, .06F)
                    .clientTrackingRange(10).updateInterval(5)
                    .build("flying_card"));
    public static final RegistryObject<EntityType<CardTrapEntity>> CARD_TRAP = ENTITIES.register("card_trap",
            () -> EntityType.Builder.<CardTrapEntity>of(CardTrapEntity::new, MobCategory.MISC)
                    .sized(.3F, .02F)
                    .clientTrackingRange(10)
                    .build("card_trap"));
}
