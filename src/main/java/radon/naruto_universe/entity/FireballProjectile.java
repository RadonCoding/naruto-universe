package radon.naruto_universe.entity;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import radon.naruto_universe.NarutoDamageSource;
import radon.naruto_universe.capability.ninja.NinjaTrait;
import radon.naruto_universe.client.particle.VaporParticle;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.util.RenderUtils;

public class FireballProjectile extends JutsuProjectile implements GeoAnimatable {
    public static final RawAnimation SPIN = RawAnimation.begin().thenLoop("misc.spin");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final EntityDataAccessor<Float> DATA_SIZE = SynchedEntityData.defineId(FireballProjectile.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DATA_TIME = SynchedEntityData.defineId(FireballProjectile.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_LIFE = SynchedEntityData.defineId(FireballProjectile.class, EntityDataSerializers.INT);

    public FireballProjectile(EntityType<? extends FireballProjectile> pEntityType, Level level) {
        super(pEntityType, level);
    }

    public FireballProjectile(LivingEntity pShooter, double pOffsetX, double pOffsetY, double pOffsetZ, float power, float damage, float size, float maxSize) {
        super(NarutoEntities.FIREBALL.get(), pShooter, pOffsetX, pOffsetY, pOffsetZ, power, damage, NinjaTrait.FIRE_RELEASE);

        this.entityData.set(DATA_SIZE, Math.min(maxSize, power * size));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();

        this.entityData.define(DATA_SIZE, 0.0F);
        this.entityData.define(DATA_TIME, 0);
        this.entityData.define(DATA_LIFE, 60);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);

        pCompound.putFloat("size", this.entityData.get(DATA_SIZE));
        pCompound.putInt("time", this.entityData.get(DATA_TIME));
        pCompound.putInt("life", this.entityData.get(DATA_LIFE));
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);

        this.entityData.set(DATA_SIZE, pCompound.getFloat("size"));
        this.entityData.set(DATA_TIME, pCompound.getInt("time"));
        this.entityData.set(DATA_LIFE, pCompound.getInt("life"));
    }

    @Override
    public @NotNull EntityDimensions getDimensions(@NotNull Pose pPose) {
        return EntityDimensions.fixed(this.getSize(), this.getSize());
    }

    public float getSize() {
        return this.entityData.get(DATA_SIZE);
    }

    public int getTime() {
        return this.entityData.get(DATA_TIME);
    }

    private void setTime(int time) {
        this.entityData.set(DATA_TIME, time);
    }

    public int getLife() {
        return this.entityData.get(DATA_LIFE);
    }

    private void setLife(int life) {
        this.entityData.set(DATA_LIFE, life);
    }

    @Override
    public void tick() {
        super.tick();

        this.refreshDimensions();

        int time = this.getTime();
        this.setTime(++time);

        int life = this.getLife();

        if (this.isInWaterOrRain()) {
            this.setLife(--life);

            if (life % 5 == 0) {
                this.playSound(SoundEvents.FIRE_EXTINGUISH, 1F, 1.0F);
            }
        }

        if (this.isInWater() || life <= 0) {
            this.level.addAlwaysVisibleParticle(ParticleTypes.CLOUD, this.getX(), this.getY() + this.getBbHeight(), this.getZ(),
                    0.0D, 0.0D, 0.0D);
            this.level.playSound(null, this.blockPosition(), SoundEvents.LAVA_EXTINGUISH, SoundSource.MASTER, 3.0F, 1.0F);
            this.discard();
        }

        if (time % 10 == 0) {
            this.level.playSound(null, this.blockPosition(), SoundEvents.FIRE_AMBIENT, SoundSource.AMBIENT, 1.0F, 1.0F);
        }

        for (int i = 0; i < 6; i++) {
            double offsetX = (this.random.nextDouble() * 0.5F) * (random.nextBoolean() ? -1 : 1);
            double offsetY = (this.getBbHeight() / 2.0) + ((this.random.nextDouble() * 0.5F) * (random.nextBoolean() ? -1 : 1));
            double offsetZ = (this.random.nextDouble() * 0.5F) * (random.nextBoolean() ? -1 : 1);

            this.level.addAlwaysVisibleParticle(new VaporParticle.VaporParticleOptions(VaporParticle.VaporParticleOptions.FLAME_COLOR, this.getSize() * 2.0F, 0.5F,
                            true, this.random.nextInt(1, 20)), true, this.getX() + offsetX, this.getY() + offsetY, this.getZ() + offsetZ,
                    0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    protected @NotNull ParticleOptions getTrailParticle() {
        return ParticleTypes.SMOKE;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    protected void onHit(@NotNull HitResult pResult) {
        super.onHit(pResult);

        if (!this.level.isClientSide) {
            float power = this.getPower() * 0.25F;
            float size = this.getSize() * 0.75F;
            float explosion = size * power;
            this.level.explode(this, this.getX(), this.getY(), this.getZ(), explosion,
                    true, Level.ExplosionInteraction.MOB);
            this.discard();
        }
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult pResult) {
        super.onHitEntity(pResult);

        if (!this.level.isClientSide) {
            Entity target = pResult.getEntity();
            Entity owner = this.getOwner();

            if (owner != null) {
                float power = this.getPower() * 0.75F;
                float size = this.getSize() * 0.5F;
                float damage = this.getDamage() * power * size;
                target.hurt(NarutoDamageSource.jutsu(owner, this), damage);
            }
        }
    }

    @Override
    public boolean ignoreExplosion() {
        return true;
    }

    private PlayState predicate(AnimationState<FireballProjectile> animationState) {
        animationState.setAnimation(SPIN);
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public double getTick(Object o) {
        return RenderUtils.getCurrentTick();
    }
}
