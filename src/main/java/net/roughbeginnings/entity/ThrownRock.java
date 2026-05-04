package net.roughbeginnings.entity;

import net.roughbeginnings.init.EntityInit;
import net.roughbeginnings.init.ItemInit;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class ThrownRock extends ThrowableItemProjectile {

    private static final float SHATTER_CHANCE = 0.15f;

    public ThrownRock(EntityType<? extends ThrownRock> type, Level level) {
        super(type, level);
    }

    public ThrownRock(Level level, LivingEntity shooter) {
        super(EntityInit.THROWN_ROCK.get(), shooter, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ItemInit.ROCK_ITEM.get();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity entity = result.getEntity();
        entity.hurt(this.damageSources().thrown(this, this.getOwner()), 2.0f);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (this.level().isClientSide()) return;

        ServerLevel server = (ServerLevel) this.level();
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();

        if (this.random.nextFloat() < SHATTER_CHANCE) {
            // Shatter: stone-break particles + sound, no item drop
            server.sendParticles(
                    new BlockParticleOption(ParticleTypes.BLOCK, Blocks.STONE.defaultBlockState()),
                    x, y, z, 12, 0.2, 0.2, 0.2, 0.0);
            server.playSound(null, x, y, z, SoundEvents.STONE_BREAK, SoundSource.NEUTRAL,
                    0.6f, 1.0f + (this.random.nextFloat() - this.random.nextFloat()) * 0.2f);
        } else {
            // Drop the rock back as an item
            ItemStack drop = new ItemStack(ItemInit.ROCK_ITEM.get());
            ItemEntity itemEntity = new ItemEntity(server, x, y, z, drop);
            itemEntity.setDefaultPickUpDelay();
            server.addFreshEntity(itemEntity);
            server.playSound(null, x, y, z, SoundEvents.STONE_HIT, SoundSource.NEUTRAL, 0.4f, 0.9f);
        }

        this.discard();
    }
}
