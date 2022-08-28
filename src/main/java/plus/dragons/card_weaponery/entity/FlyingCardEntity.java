package plus.dragons.card_weaponery.entity;

import net.minecraft.world.entity.npc.Villager;
import plus.dragons.card_weaponery.misc.Configuration;
import plus.dragons.card_weaponery.registry.EntityRegistry;
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
import plus.dragons.card_weaponery.registry.ItemRegistry;

public class FlyingCardEntity extends Projectile implements IEntityAdditionalSpawnData {
    private static final EntityDataAccessor<Boolean> CAN_PICK_UP = SynchedEntityData.defineId(FlyingCardEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> LIFETIME = SynchedEntityData.defineId(FlyingCardEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> ROTATION = SynchedEntityData.defineId(FlyingCardEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> ROTATION_SPEED = SynchedEntityData.defineId(FlyingCardEntity.class, EntityDataSerializers.FLOAT);

    public FlyingCardEntity(EntityType<? extends FlyingCardEntity> entityType, Level level) {
        super(entityType, level);
    }

    public FlyingCardEntity(LivingEntity livingEntity, Level level) {
        super(EntityRegistry.FLYING_CARD.get(), level);
        this.moveTo(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), this.getYRot(), this.getXRot());
        this.reapplyPosition();
        this.entityData.set(CAN_PICK_UP, false);
        this.entityData.set(LIFETIME, 20*60);
        this.entityData.set(ROTATION,0.0F);
        this.entityData.set(ROTATION_SPEED,20.0F);
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
            // This Height limit needs to be changed in 1.17
            if (blockPosition().getY() >= 384)
                remove(RemovalReason.DISCARDED);
            if (getRemainingLifetime() <= 0) {
                this.spawnAtLocation(ItemRegistry.BLANK_CARD.get().getDefaultInstance(), 0.1F);
                remove(RemovalReason.DISCARDED);
            } else {
                setRemainingLifetime(getRemainingLifetime()-1);
                // If it stop in middle path
                if(!justBeenThrown(20) && getDeltaMovement().lengthSqr()<0.1){
                    var s = getRotationSpeedTick();
                    if(s!=0){
                        s-=20;
                        setRotationSpeedTick(s);
                    }
                    setPickUpStatus(true);
                }
                // Pickup Card on Ground
                if (canPickUp() && qualifiedToBeRetrieved()) {
                    this.spawnAtLocation(ItemRegistry.BLANK_CARD.get().getDefaultInstance(), 0.1F);
                    remove(RemovalReason.DISCARDED);
                }
            }
        }

        if (this.level.isClientSide || (entity == null || !entity.isRemoved()) && this.level.hasChunkAt(this.blockPosition())) {
            super.tick();
            // Handle hit result
            HitResult hitresult = ProjectileUtil.getHitResult(this, this::canHitEntity);
            if (hitresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
                this.onHit(hitresult);
            }
            // Handle on fly event
            if(!canPickUp()){
                // card.onFly(this);
            }
            // Handle Movement
            this.checkInsideBlocks();
            Vec3 vec3 = this.getDeltaMovement();
            double d0 = this.getX() + vec3.x;
            double d1 = this.getY() + vec3.y;
            double d2 = this.getZ() + vec3.z;
            float f = this.getInertia();
            if (this.isInWater()) {
                f = 0.9F;
            }
            this.setDeltaMovement(vec3.scale(f));
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

    protected float getInertia() {
        return 1.0F;
    }

    @Override
    protected void onHit(HitResult rayTraceResult) {
        super.onHit(rayTraceResult);
    }

    @Override
    protected void onHitEntity(EntityHitResult entityRayTraceResult) {
        super.onHitEntity(entityRayTraceResult);
        Entity entity = entityRayTraceResult.getEntity();
        if (entity instanceof LivingEntity && !canPickUp()) {
            // card.hitEntity(this, (LivingEntity) entity);
            remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult blockRayTraceResult) {
        super.onHitBlock(blockRayTraceResult);
        // card.hitBlock(this, blockRayTraceResult.getBlockPos(), blockRayTraceResult.getDirection());
        if (!this.isRemoved())
            stayOnBlock(blockRayTraceResult);
    }

    private boolean qualifiedToBeRetrieved() {
        return !level.getEntities(this, this.getBoundingBox(), (entity -> entity instanceof Player)).isEmpty();
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
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        compoundNBT.putInt("remaining_life_time", getRemainingLifetime());
        compoundNBT.putBoolean("can_pickup", canPickUp());
        compoundNBT.putFloat("rotation_speed", getRotationSpeedTick());
        compoundNBT.putFloat("rotation", getRotationAngle());
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        // additional spawn data
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        // additional spawn data
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
