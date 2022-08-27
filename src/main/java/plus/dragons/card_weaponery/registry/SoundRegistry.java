package plus.dragons.card_weaponery.registry;

import plus.dragons.card_weaponery.CardWeaponry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SoundRegistry {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, CardWeaponry.MODID);
    public static final RegistryObject<SoundEvent> THROW_CARD = SOUNDS.register("throw_card", () -> new SoundEvent(new ResourceLocation(CardWeaponry.MODID, "throw_card")));
    public static final RegistryObject<SoundEvent> COLORED_CARD_HIT = SOUNDS.register("colored_card_hit", () -> new SoundEvent(new ResourceLocation(CardWeaponry.MODID, "colored_card_hit")));
    public static final RegistryObject<SoundEvent> ELEMENTAL_CARD_HIT = SOUNDS.register("elemental_card_hit", () -> new SoundEvent(new ResourceLocation(CardWeaponry.MODID, "elemental_card_hit")));
    public static final RegistryObject<SoundEvent> TRAP_CARD_HIT = SOUNDS.register("trap_card_hit", () -> new SoundEvent(new ResourceLocation(CardWeaponry.MODID, "trap_card_hit")));
    public static final RegistryObject<SoundEvent> CUTTING_CARD = SOUNDS.register("cutting_card", () -> new SoundEvent(new ResourceLocation(CardWeaponry.MODID, "cutting_card")));
    public static final RegistryObject<SoundEvent> BLOCK_CARD_ON_RUN = SOUNDS.register("block_card_on_run", () -> new SoundEvent(new ResourceLocation(CardWeaponry.MODID, "block_card_on_run")));


}
