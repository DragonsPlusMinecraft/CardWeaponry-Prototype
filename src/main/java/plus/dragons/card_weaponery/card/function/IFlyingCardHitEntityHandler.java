package plus.dragons.card_weaponery.card.function;

import plus.dragons.card_weaponery.entity.FlyingCardEntity;
import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface IFlyingCardHitEntityHandler {
    void handleHit(FlyingCardEntity card, LivingEntity victim);
}
