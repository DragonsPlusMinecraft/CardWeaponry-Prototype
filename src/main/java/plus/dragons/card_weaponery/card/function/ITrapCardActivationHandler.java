package plus.dragons.card_weaponery.card.function;

import plus.dragons.card_weaponery.entity.CardTrapEntity;
import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface ITrapCardActivationHandler {
    void handleTrap(CardTrapEntity trap, LivingEntity victim);
}
