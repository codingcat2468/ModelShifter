package com.codingcat.modelshifter.client.render.entity;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.EntityType;
import software.bernie.geckolib.animatable.GeoReplacedEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.util.GeckoLibUtil;

public class ReplacedPlayerEntity implements GeoReplacedEntity {
    public static final RawAnimation SNEAK = RawAnimation.begin().thenPlayAndHold("move.sneak");
    private final AnimatableInstanceCache cache;
    private final boolean alwaysWalk;
    private final boolean isUI;

    public ReplacedPlayerEntity(boolean isUI, boolean alwaysWalk) {
        this.alwaysWalk = alwaysWalk;
        this.isUI = isUI;
        this.cache = GeckoLibUtil.createInstanceCache(this);
    }

    @Override
    public boolean shouldPlayAnimsWhileGamePaused() {
        return this.isUI;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<GeoReplacedEntity>(this, 2, state -> {
            if (isUI) {
                if (alwaysWalk)
                    return state.setAndContinue(DefaultAnimations.RUN);

                return state.setAndContinue(DefaultAnimations.IDLE);
            }

            if (!(state.getData(DataTickets.ENTITY) instanceof AbstractClientPlayerEntity player))
                return PlayState.STOP;
            if (player.isSneaking())
                return state.setAndContinue(SNEAK);
            if (player.isSprinting())
                return state.setAndContinue(DefaultAnimations.RUN);
            if (state.isMoving())
                return state.setAndContinue(DefaultAnimations.WALK);

            return state.setAndContinue(DefaultAnimations.IDLE);
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public EntityType<?> getReplacingEntityType() {
        return EntityType.PLAYER;
    }
}