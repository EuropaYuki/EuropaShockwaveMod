package com.EuropaYuki.EuropaShockwaveMod;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

import java.util.List;

public class EuropaShockwaveItem extends Item {
    public EuropaShockwaveItem() {
        super(new Properties().rarity(Rarity.EPIC).fireResistant());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (!world.isClientSide && player != null) {
            List<Entity> entities = world.getEntities(player, player.getBoundingBox().inflate(20));
            for (Entity entity : entities) {
                if (entity instanceof LivingEntity && entity != player) {
                    LivingEntity target = (LivingEntity) entity;
                    target.hurt(entity.damageSources().playerAttack(player), 10.0F);
                    target.push(1.0, 7.0, 1.0);
                    world.playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 1.0F, 1.0F);
                    if (world instanceof ServerLevel) {
                        ((ServerLevel) world).sendParticles(ParticleTypes.EXPLOSION, target.getX(), target.getY(), target.getZ(), 10, 0.5, 0.5, 0.5, 0.1);
                    }
                }
            }
        }
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }
}
