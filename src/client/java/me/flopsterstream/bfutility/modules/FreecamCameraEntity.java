package me.flopsterstream.bfutility.modules;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;

public class FreecamCameraEntity extends Entity {

    public FreecamCameraEntity(ClientWorld world) {
        super(EntityType.PLAYER, world);
        this.noClip = true;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(DataTracker.registerData(FreecamCameraEntity.class, TrackedDataHandlerRegistry.BOOLEAN), false);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {}

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {}

    @Override
    public boolean shouldRender(double distance) {
        return false;
    }

    @Override
    public Vec3d getPos() {
        return new Vec3d(this.getX(), this.getY(), this.getZ());
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        return false; // Ignore damage
    }
}
