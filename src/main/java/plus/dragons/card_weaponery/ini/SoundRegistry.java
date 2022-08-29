package plus.dragons.card_weaponery.ini;

import plus.dragons.card_weaponery.CardWeaponry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SoundRegistry {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, CardWeaponry.MODID);
    public static final RegistryObject<SoundEvent> THROW_CARD = SOUNDS.register("throw_card", () -> new SoundEvent(new ResourceLocation(CardWeaponry.MODID, "throw_card")));


}
