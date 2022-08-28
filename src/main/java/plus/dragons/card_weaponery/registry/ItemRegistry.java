package plus.dragons.card_weaponery.registry;

import plus.dragons.card_weaponery.CardWeaponry;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import plus.dragons.card_weaponery.item.*;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CardWeaponry.MODID);
    public static final RegistryObject<Item> BLANK_CARD = ITEMS.register("blank_card", BlankCard::new);
}
