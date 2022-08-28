package plus.dragons.card_weaponery.card;

import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.Nullable;
import plus.dragons.card_weaponery.entity.CardEntity;

import java.util.*;

public abstract class CardFeature extends ForgeRegistryEntry<CardFeature> {

    public float getInitialSpeedBonus(){
        return 0;
    }

    /**
     * Please keep notice It is suggest to use multiplication instead of subtraction & addition.
     * If the final value < 0, healing will be dealt.
     */
    public float modifyDamage(float damage){
        return damage;
    }

    public Vec3 modifyMovement(CardEntity card, Vec3 movement){
        return movement;
    }

    public void onHitBlock(CardEntity card, BlockHitResult blockHitResult){}

    /**
     * Please notice: It is suggest not to deal a damage here.
     * All damages will be dealt as once after an entity is hit.
     * to modify damage dealt, please Override {@link CardFeature#modifyDamage(float)}
     */
    public void onHitEntity(CardEntity card, EntityHitResult entityHitResult){}

    public void onFly(CardEntity card){}

    public int priority(){
        return 1;
    }

    @Nullable
    public CardFeatureEventBlockMap blockEventMap(){
        return null;
    }


    public int maxOnCard(){
        return 1;
    }

    public boolean isCompatibleWith(CardFeature other){
        return this.checkCompatibility(other) && other.checkCompatibility(this);
    }

    protected boolean checkCompatibility(CardFeature other) {
        return this != other;
    }

    public enum BlockEventType{
        MOVEMENT_MODIFICATION,
        HIT_ENTITY,
        HIT_BLOCK,
        ON_FLY
    }

    public static class CardFeatureEventBlockMap extends HashMap<BlockEventType, Set<CardFeature>>{

        public static class Builder{
            HashSet<CardFeature> he = null;
            HashSet<CardFeature> hb = null;
            HashSet<CardFeature> of = null;
            HashSet<CardFeature> mm = null;

            public Builder(){}

            public void blockHitEntity(CardFeature cardFeature){
                if(he==null) he = new HashSet<>();
                he.add(cardFeature);
            }

            public void blockHitEntity(CardFeature... cardFeatures){
                if(he==null) he = new HashSet<>(List.of(cardFeatures));
                else he.addAll(List.of(cardFeatures));
            }

            public void blockHitBlock(CardFeature cardFeature){
                if(hb==null) hb = new HashSet<>();
                hb.add(cardFeature);
            }

            public void blockHitBlock(CardFeature... cardFeatures){
                if(hb==null) hb = new HashSet<>(List.of(cardFeatures));
                else hb.addAll(List.of(cardFeatures));
            }

            public void blockOnFly(CardFeature cardFeature){
                if(of==null) of = new HashSet<>();
                of.add(cardFeature);
            }

            public void blockOnFly(CardFeature... cardFeatures){
                if(of==null) of = new HashSet<>(List.of(cardFeatures));
                else of.addAll(List.of(cardFeatures));
            }

            public void blockMovementModify(CardFeature cardFeature){
                if(mm==null) mm = new HashSet<>();
                mm.add(cardFeature);
            }

            public void blockMovementModify(CardFeature... cardFeatures){
                if(mm==null) mm = new HashSet<>(List.of(cardFeatures));
                else mm.addAll(List.of(cardFeatures));
            }

            public CardFeatureEventBlockMap build(){
                CardFeatureEventBlockMap ret = new CardFeatureEventBlockMap();
                if(he!=null) ret.put(BlockEventType.HIT_ENTITY,he);
                if(he!=null) ret.put(BlockEventType.HIT_BLOCK,he);
                if(of!=null) ret.put(BlockEventType.ON_FLY,of);
                if(mm!=null) ret.put(BlockEventType.MOVEMENT_MODIFICATION,mm);
                return ret;
            }
        }
    }
}
