package com.codingcat.modelshifter.client.render.entity;

import net.minecraft.entity.EntityType;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoReplacedEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.util.GeckoLibUtil;

public class ReplacedPlayerEntity implements GeoReplacedEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final boolean alwaysWalk;
    private final boolean isUI;

    public ReplacedPlayerEntity(boolean isUI, boolean alwaysWalk) {
        this.alwaysWalk = alwaysWalk;
        this.isUI = isUI;
    }

    @Override
    public boolean shouldPlayAnimsWhileGamePaused() {
        return this.isUI;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        if (alwaysWalk) {
            controllers.add(new AnimationController<GeoAnimatable>(this, "walk", state -> state.setAndContinue(DefaultAnimations.WALK)));
            return;
        }


        controllers.add(DefaultAnimations.genericWalkIdleController(this));
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