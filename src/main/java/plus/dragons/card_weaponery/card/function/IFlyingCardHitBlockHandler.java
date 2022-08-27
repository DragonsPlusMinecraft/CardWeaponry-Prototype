package plus.dragons.card_weaponery.card.function;

import plus.dragons.card_weaponery.entity.FlyingCardEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

@FunctionalInterface
public interface IFlyingCardHitBlockHandler {
    void handleHit(FlyingCardEntity card, BlockPos pos, Direction face);
}
