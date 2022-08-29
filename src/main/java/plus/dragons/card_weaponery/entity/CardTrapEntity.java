package plus.dragons.card_weaponery.entity;

import plus.dragons.card_weaponery.ini.EntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CardTrapEntity extends Entity implements IEntityAdditionalSpawnData {
    private UUID ownerUUID;

    public CardTrapEntity(EntityType<? extends CardTrapEntity> entityType, Level level) {
        super(entityType, level);
    }

    public CardTrapEntity(Level world) {
        super(EntityRegistry.CARD_TRAP.get(), world);
    }

    @Override
    public float getBrightness() {
        // return Configuration.TRAP_CARD_BRIGHTNESS.get().floatValue();
        return 0;
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (!player.level.isClientSide()) {
            /*if (card.getRetrievedItem().isPresent())
                player.level.addFreshEntity(new ItemEntity(player.level, this.getX(), this.getY(), this.getZ(), card.getRetrievedItem().get().getDefaultInstance()));*/
            remove(RemovalReason.DISCARDED);
        }
        return InteractionResult.sidedSuccess(player.level.isClientSide());
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();

        // Handle activated by entity
        List<LivingEntity> targets = getTriggeringTarget();
        if (!targets.isEmpty()) {
            activateTrap(targets);
        }

        if (isAlive()) {
            // Natural falling and movement
            setDeltaMovement(getDeltaMovement().add(0.0D, -0.04D, 0.0D));

            if (level.isClientSide) {
                noPhysics = false;
            } else {
                noPhysics = !level.noCollision(this);
                if (noPhysics) {
                    moveTowardsClosestSpace(getX(), (getBoundingBox().minY + getBoundingBox().maxY) / 2.0D, getZ());
                }
            }

            if (!onGround || getDeltaMovement().horizontalDistanceSqr() > (double) 1.0E-5F || (tickCount + getId()) % 4 == 0) {
                move(MoverType.SELF, getDeltaMovement());
                float f1 = 0.98F;
                if (onGround) {
                    f1 = level.getBlockState(new BlockPos(getX(), getY() - 1.0D, getZ())).getFriction(level, new BlockPos(getX(), getY() - 1.0D, getZ()), this) * 0.98F;
                }

                this.setDeltaMovement(getDeltaMovement().multiply(f1, 0.98D, f1));
                if (onGround) {
                    Vec3 vector3d1 = getDeltaMovement();
                    if (vector3d1.y < 0.0D) {
                        setDeltaMovement(vector3d1.multiply(1.0D, -0.5D, 1.0D));
                    }
                }
            }
        }
    }

    private List<LivingEntity> getTriggeringTarget() {
        return level.getEntities(this, getBoundingBox().expandTowards(0, 1, 0), entity -> entity instanceof LivingEntity)
                .stream().map(entity -> (LivingEntity) entity).collect(Collectors.toList());
    }

    private void activateTrap(List<LivingEntity> targets) {
        for (LivingEntity livingEntity : targets)
            // card.activate(this, livingEntity);
        remove(RemovalReason.DISCARDED);
    }

    public void setOwner(@Nullable Entity entity) {
        if (entity != null) {
            this.ownerUUID = entity.getUUID();
        }
    }

    @Nullable
    public UUID getOwnerUUID(){
        if (ownerUUID != null) {
            return ownerUUID;
        }
        return null;
    }

    @Nullable
    public Entity getOwner() {
        if (ownerUUID != null && level instanceof ServerLevel) {
            return ((ServerLevel) level).getEntity(ownerUUID);
        }
        return null;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundNBT) {
        if (compoundNBT.contains("owner_uuid"))
            ownerUUID = compoundNBT.getUUID("owner_uuid");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundNBT) {
        if (ownerUUID != null) {
            compoundNBT.putUUID("owner_uuid", ownerUUID);
        }
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

}
