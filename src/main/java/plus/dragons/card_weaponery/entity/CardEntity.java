package plus.dragons.card_weaponery.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ForgeEventFactory;
import plus.dragons.card_weaponery.forgeregistry.CardWeaponryRegistries;
import plus.dragons.card_weaponery.card.CardFeature;
import plus.dragons.card_weaponery.misc.Configuration;
import plus.dragons.card_weaponery.misc.ModDamage;
import plus.dragons.card_weaponery.ini.EntityRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import plus.dragons.card_weaponery.ini.ItemRegistry;

import java.util.*;

public class CardEntity extends Projectile implements IEntityAdditionalSpawnData {
    private static final EntityDataAccessor<Boolean> CAN_PICK_UP = SynchedEntityData.defineId(CardEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> LIFETIME = SynchedEntityData.defineId(CardEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> ROTATION = SynchedEntityData.defineId(CardEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> ROTATION_SPEED = SynchedEntityData.defineId(CardEntity.class, EntityDataSerializers.FLOAT);

    private List<CardFeature> completeFeatures = new ArrayList<>();
    private List<CardFeature> hitBlockFeatures = new ArrayList<>();
    private List<CardFeature> hitEntityFeatures = new ArrayList<>();
    private List<CardFeature> onFlyFeatures = new ArrayList<>();
    private List<CardFeature> movementFeatures = new ArrayList<>();
    private float damage = Configuration.CARD_DAMAGE.get().floatValue();

    public CardEntity(EntityType<? extends CardEntity> entityType, Level level) {
        super(entityType, level);
    }

    public CardEntity(LivingEntity livingEntity, Level level, List<CardFeature> features) {
        super(EntityRegistry.FLYING_CARD.get(), level);
        this.reapplyPosition();
        this.entityData.set(CAN_PICK_UP, false);
        this.entityData.set(LIFETIME, 20*60);
        this.entityData.set(ROTATION,0.0F);
        this.entityData.set(ROTATION_SPEED,20.0F);
        this.importFeatures(features);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double pDistance) {
        double d0 = this.getBoundingBox().getSize() * 4.0D;
        if (Double.isNaN(d0)) {
            d0 = 4.0D;
        }
        d0 *= 128.0D;
        return pDistance < d0 * d0;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(CAN_PICK_UP, false);
        this.entityData.define(LIFETIME, 20*60);
        this.entityData.define(ROTATION,0.0F);
        this.entityData.define(ROTATION_SPEED,20.0F);
    }

    @Override
    public void tick() {
        // Removal Check
        Entity entity = this.getOwner();
        if (!level.isClientSide()) {
            if (blockPosition().getY() >= 384 && blockPosition().getY() <= -102)
                remove(RemovalReason.DISCARDED);
            if (shouldBecomeItemStack()) {
                this.spawnAtLocation(toItemStack(), 0.1F);
                remove(RemovalReason.DISCARDED);
            } else {
                setRemainingLifetime(getRemainingLifetime()-1);
                // If it stop in middle path, set it to retrievable
                if(!justBeenThrown(20) && getDeltaMovement().lengthSqr()<0.1){
                    var s = getRotationSpeedTick();
                    if(s!=0){
                        s-=20;
                        setRotationSpeedTick(s);
                    }
                    setPickUpStatus(true);
                }
            }
        }

        if (this.level.isClientSide || (entity == null || !entity.isRemoved()) && this.level.hasChunkAt(this.blockPosition())) {
            super.tick();
            HitResult hitresult = ProjectileUtil.getHitResult(this, this::canHitEntity);
            if (hitresult.getType() != HitResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, hitresult)) {
                this.onHit(hitresult);
            }
            if(!canPickUp()){
                for(CardFeature feature:onFlyFeatures){
                    feature.onFly(this);
                }
            }
            // Handle Movement
            this.checkInsideBlocks();
            Vec3 vec3 = this.getDeltaMovement();
            for(CardFeature feature:movementFeatures){
                vec3 = feature.modifyMovement(this, this.getDeltaMovement());
            }
            double d0 = this.getX() + vec3.x;
            double d1 = this.getY() + vec3.y;
            double d2 = this.getZ() + vec3.z;
            this.setDeltaMovement(vec3);
            this.setPos(d0, d1, d2);
            if(!level.isClientSide()&&!canPickUp()){
                var r = getRotationAngle();
                r += getRotationSpeedTick();
                r = r>360? r-360: r;
                setRotationAngle(r);
            }
        } else {
            this.discard();
        }
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        if(entity instanceof Villager){
            return Configuration.HURT_VILLAGER.get();
        } else if(entity instanceof Player player){
            if(player==getOwner()) return false;
            if(this.getOwner() instanceof Player playerOwner)
                return playerOwner.canHarmPlayer(player);
            return true;
        } else if(entity instanceof OwnableEntity ownable){
            return ownable.getOwner() != this.getOwner();
        }
        return super.canHitEntity(entity) && !entity.noPhysics;
    }

    @Override
    protected void onHit(HitResult rayTraceResult) {
        super.onHit(rayTraceResult);
    }

    @Override
    protected void onHitEntity(EntityHitResult entityRayTraceResult) {
        super.onHitEntity(entityRayTraceResult);
        if(!canPickUp()){
            Entity entity = entityRayTraceResult.getEntity();
            if (entity instanceof LivingEntity && !canPickUp()) {
                for(CardFeature feature:hitEntityFeatures){
                    feature.onHitEntity(this,entityRayTraceResult);
                }
                if(damage>0){
                    entity.hurt(ModDamage.causeCardDamage(this,this.getOwner()),damage);
                }else if(damage<0){
                    ((LivingEntity) entity).heal(damage);
                }
                remove(RemovalReason.DISCARDED);
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult blockRayTraceResult) {
        super.onHitBlock(blockRayTraceResult);
        for(CardFeature feature:hitBlockFeatures){
            feature.onHitBlock(this,blockRayTraceResult);
        }
        if (!this.isRemoved())
            stayOnBlock(blockRayTraceResult);
    }

    private boolean shouldBecomeItemStack() {
        if(getRemainingLifetime() <= 0) return true;
        return canPickUp() && !level.getEntities(this, this.getBoundingBox(), (entity -> entity instanceof Player)).isEmpty();
    }

    private ItemStack toItemStack(){
        // TODO
        return ItemRegistry.BLANK_CARD.get().getDefaultInstance();
    }

    private void stayOnBlock(BlockHitResult blockRayTraceResult) {
        var s = getRotationSpeedTick();
        if(s!=0){
            s-=20;
            setRotationSpeedTick(s);
        }
        Vec3 vector3d = blockRayTraceResult.getLocation().subtract(this.getX(), this.getY(), this.getZ());
        this.setDeltaMovement(vector3d);
        Vec3 vector3d1 = vector3d.normalize().scale((double) 0.05F);
        this.setPosRaw(this.getX() - vector3d1.x, this.getY() - vector3d1.y, this.getZ() - vector3d1.z);
        setPickUpStatus(true);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundNBT) {
        super.readAdditionalSaveData(compoundNBT);
        setRemainingLifetime(compoundNBT.getInt("remaining_life_time"));
        setPickUpStatus(compoundNBT.getBoolean("can_pickup"));
        setRotationSpeedTick(compoundNBT.getFloat("rotation_speed"));
        setRotationAngle(compoundNBT.getFloat("rotation"));
        var a = compoundNBT.getInt("feature_count");
        List<CardFeature> ar = new ArrayList<>();
        for(int i=1;i<=a;i++){
            ar.add(CardWeaponryRegistries.CARD_FEATURES.get().getValue(new ResourceLocation(compoundNBT.getString("feature_" + i))));
        }
        importFeatures(ar);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        compoundNBT.putInt("remaining_life_time", getRemainingLifetime());
        compoundNBT.putBoolean("can_pickup", canPickUp());
        compoundNBT.putFloat("rotation_speed", getRotationSpeedTick());
        compoundNBT.putFloat("rotation", getRotationAngle());
        compoundNBT.putInt("feature_count",completeFeatures.size());
        var a = 1;
        for(var feature:completeFeatures){
            compoundNBT.putString("feature_" + a,feature.getRegistryName().toString());
            a++;
        }
    }

    private void importFeatures(List<CardFeature> features){
        completeFeatures.addAll(features);
        completeFeatures.sort(Comparator.comparingInt(CardFeature::priority));
        var checkSet = new HashSet<>(features);
        hitBlockFeatures.addAll(completeFeatures);
        hitEntityFeatures.addAll(completeFeatures);
        movementFeatures.addAll(completeFeatures);
        onFlyFeatures.addAll(completeFeatures);
        for(CardFeature cardFeature: checkSet){
            if(cardFeature.blockEventMap()!=null){
                for(Map.Entry<CardFeature.BlockEventType,Set<CardFeature>> entry: cardFeature.blockEventMap().entrySet()){
                    if(entry.getKey() == CardFeature.BlockEventType.HIT_ENTITY)
                        hitEntityFeatures.removeIf(e->entry.getValue().contains(e));
                    if(entry.getKey() == CardFeature.BlockEventType.HIT_BLOCK)
                        hitBlockFeatures.removeIf(e->entry.getValue().contains(e));
                    if(entry.getKey() == CardFeature.BlockEventType.ON_FLY)
                        onFlyFeatures.removeIf(e->entry.getValue().contains(e));
                    if(entry.getKey() == CardFeature.BlockEventType.MOVEMENT_MODIFICATION)
                        movementFeatures.removeIf(e->entry.getValue().contains(e));
                }
            }
        }
        for(CardFeature cardFeature:completeFeatures){
            damage = cardFeature.modifyDamage(damage);
        }
    }


    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeInt(completeFeatures.size());
        for(var feature:completeFeatures){
            buffer.writeRegistryId(feature);
        }
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        var size = additionalData.readInt();
        List<CardFeature> ar = new ArrayList<>();
        for(int i=0;i<size;i++){
            ar.add(additionalData.readRegistryId());
        }
        this.importFeatures(ar);
    }

    @Override
    public float getBrightness() {
        // return Configuration.FLYING_CARD_BRIGHTNESS.get().floatValue();
        return 0;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        return false;
    }

    private boolean canPickUp() {
        return this.entityData.get(CAN_PICK_UP);
    }

    public float getRotationAngle(){
        return this.entityData.get(ROTATION);
    }

    private void setRotationAngle(float angle){
        this.entityData.set(ROTATION,angle);
    }

    public float getRotationSpeedTick(){
        return this.entityData.get(ROTATION_SPEED);
    }

    private void setRotationSpeedTick(float speed){
        if(speed<0) speed=0;
        this.entityData.set(ROTATION_SPEED,speed);
    }

    private void setPickUpStatus(boolean b) {
        entityData.set(CAN_PICK_UP, b);
    }

    public int getRemainingLifetime(){
        return this.entityData.get(LIFETIME);
    }

    private void setRemainingLifetime(int lifetime){
        entityData.set(LIFETIME, lifetime);
    }

    // For Delay Handling
    // 5 tick lifespan is counted
    public boolean justBeenThrown(int delay){
        return getRemainingLifetime() > 60 * 20 - delay;
    }

}
