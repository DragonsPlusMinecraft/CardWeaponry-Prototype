package plus.dragons.card_weaponery.registry;

import plus.dragons.card_weaponery.CardWeaponry;
import plus.dragons.card_weaponery.card.CommonCards;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import plus.dragons.card_weaponery.item.*;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CardWeaponry.MODID);

    public static final RegistryObject<Item> CARD_STACK = ITEMS.register("card_stack", CardSwitcher::new);
    public static final RegistryObject<Item> BLANK_CARD = ITEMS.register("blank_card", BlankCard::new);
    public static final RegistryObject<Item> PROTOTYPE_CORE = ITEMS.register("prototype_core", Intermediates::new);

    public static final RegistryObject<Item> FLAME_CARD = ITEMS.register("flame_card", () -> new ElementalCard(CommonCards.FLAME));
    public static final RegistryObject<Item> TORRENT_CARD = ITEMS.register("torrent_card", () -> new ElementalCard(CommonCards.TORRENT));
    public static final RegistryObject<Item> THUNDER_CARD = ITEMS.register("thunder_card", () -> new ElementalCard(CommonCards.THUNDER));
    public static final RegistryObject<Item> BRAMBLE_CARD = ITEMS.register("bramble_card", () -> new ElementalCard(CommonCards.BRAMBLE));
    public static final RegistryObject<Item> EARTH_CARD = ITEMS.register("earth_card", () -> new ElementalCard(CommonCards.EARTH));
    public static final RegistryObject<Item> END_CARD = ITEMS.register("end_card", () -> new ElementalCard(CommonCards.END));

    public static final RegistryObject<Item> FIELD_CORE = ITEMS.register("field_core", Intermediates::new);
    public static final RegistryObject<Item> PURIFICATION_CORE = ITEMS.register("purification_core", Intermediates::new);
    public static final RegistryObject<Item> SEAL_CORE = ITEMS.register("seal_core", Intermediates::new);
    public static final RegistryObject<Item> SUNNY_CORE = ITEMS.register("sunny_core", Intermediates::new);
    public static final RegistryObject<Item> RAINY_CORE = ITEMS.register("rainy_core", Intermediates::new);
    public static final RegistryObject<Item> THUNDERSTORM_CORE = ITEMS.register("thunderstorm_core", Intermediates::new);
    public static final RegistryObject<Item> BLOOM_CORE = ITEMS.register("bloom_core", Intermediates::new);


}
