package plus.dragons.card_weaponery.card.function;

import plus.dragons.card_weaponery.entity.FlyingCardEntity;

@FunctionalInterface
public interface IFlyingCardOnFlyHandler {
    void handle(FlyingCardEntity card);
}
