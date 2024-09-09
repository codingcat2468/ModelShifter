package com.codingcat.modelshifter.client.render.entity;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.EntityType;
import org.jetbrains.annotations.Nullable;
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
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final RawAnimation overrideAnimation;
    private final boolean ignorePausedGame;

    public ReplacedPlayerEntity(@Nullable RawAnimation overrideAnimation, boolean ignorePausedGame) {
        this.overrideAnimation = overrideAnimation;
        this.ignorePausedGame = ignorePausedGame;
    }

    @Override
    public boolean shouldPlayAnimsWhileGamePaused() {
        return this.ignorePausedGame;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<GeoReplacedEntity>(this, 2, state -> {
            if (this.overrideAnimation != null)
                return state.setAndContinue(this.overrideAnimation);

            if (!(state.getData(DataTickets.ENTITY) instanceof AbstractClientPlayerEntity player))
                return PlayState.STOP;
            if (player.isInSneakingPose())
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