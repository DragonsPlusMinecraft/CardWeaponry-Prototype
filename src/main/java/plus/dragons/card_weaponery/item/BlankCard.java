package plus.dragons.card_weaponery.item;

import plus.dragons.card_weaponery.entity.CardEntity;
import plus.dragons.card_weaponery.misc.Configuration;
import plus.dragons.card_weaponery.misc.ModGroup;
import plus.dragons.card_weaponery.ini.CardFeatureRegistry;
import plus.dragons.card_weaponery.ini.SoundRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class BlankCard extends Item {

    public BlankCard() {
        super(new Properties().tab(ModGroup.GENERAL));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (!worldIn.isClientSide()) {
            CardEntity cardEntity = new CardEntity(player, worldIn, List.of(CardFeatureRegistry.BASIC.get()));
            cardEntity.setOwner(player);
            cardEntity.setPos(player.getX(), player.getY() + player.getEyeHeight(), player.getZ());
            cardEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, Configuration.CARD_SPEED.get().floatValue(), 0);
            worldIn.addFreshEntity(cardEntity);

            double d0 = -Mth.sin(player.getYRot() * ((float)Math.PI / 180F));
            double d1 = Mth.cos(player.getYRot() * ((float)Math.PI / 180F));
            ((ServerLevel)worldIn).sendParticles(ParticleTypes.SWEEP_ATTACK, player.getX() + d0, player.getY(0.5D), player.getZ() + d1, 0, d0, 0.0D, d1, 0.0D);

            worldIn.playSound((Player) null, player.getX(), player.getY(), player.getZ(), SoundRegistry.THROW_CARD.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

            if (!player.getAbilities().instabuild) {
                itemStack.shrink(1);
            }
        }
        return InteractionResultHolder.sidedSuccess(itemStack, worldIn.isClientSide());
    }
}
