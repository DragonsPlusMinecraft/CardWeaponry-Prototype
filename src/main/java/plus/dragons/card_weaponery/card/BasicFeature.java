package plus.dragons.card_weaponery.card;

import net.minecraft.world.phys.Vec3;
import plus.dragons.card_weaponery.entity.CardEntity;

public class BasicFeature extends CardFeature{
    @Override
    public Vec3 modifyMovement(CardEntity card, Vec3 movement){
        if(card.isInWater()) return movement.scale(0.9);
        return movement;
    }
}
