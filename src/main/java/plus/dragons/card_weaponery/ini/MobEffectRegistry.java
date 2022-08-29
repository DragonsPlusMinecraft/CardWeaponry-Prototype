package plus.dragons.card_weaponery.ini;

import plus.dragons.card_weaponery.CardWeaponry;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MobEffectRegistry {
    public static final DeferredRegister<MobEffect> MOB_EFFECT = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, CardWeaponry.MODID);

/*    public static final RegistryObject<MobEffect> DIZZY = MOB_EFFECT.register("dizzy", () -> new HiddenEffect(MobEffectCategory.HARMFUL)
            .addAttributeModifier(Attributes.FOLLOW_RANGE, "831CF4BC-ED83-4072-A2A2-C115DD72317F", -0.96d, AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(Attributes.MOVEMENT_SPEED, "6E21DF28-A639-43E5-A189-D9ECFAE3AA39", -0.67D, AttributeModifier.Operation.MULTIPLY_TOTAL));*/
}
