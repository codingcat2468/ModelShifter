package com.codingcat.modelshifter.client.render.entity;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoReplacedEntity;
//? >1.20.4 {
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
//?} else {
/*import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
*///?}
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Function;

public class ReplacedPlayerEntity implements GeoReplacedEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    @NotNull
    private final Function<PlayerEntity, RawAnimation> animationFunction;
    private final boolean ignorePausedGame;
    private final boolean usePlayerEntity;

    public ReplacedPlayerEntity(@NotNull Function<PlayerEntity, RawAnimation> animationFunction, boolean usePlayerEntity, boolean ignorePausedGame) {
        this.animationFunction = animationFunction;
        this.ignorePausedGame = ignorePausedGame;
        this.usePlayerEntity = usePlayerEntity;
    }

    @Override
    public boolean shouldPlayAnimsWhileGamePaused() {
        return this.ignorePausedGame;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<GeoReplacedEntity>(this, 2, state -> {
            if (!this.usePlayerEntity)
                return getAnimation(null, state);

            if (!(state.getData(DataTickets.ENTITY) instanceof AbstractClientPlayerEntity player))
                return PlayState.STOP;

            return getAnimation(player, state);
        }));
    }

    private PlayState getAnimation(@Nullable PlayerEntity player, AnimationState<GeoReplacedEntity> state) {
        RawAnimation animation = this.animationFunction.apply(player);
        return animation != null ? state.setAndContinue(animation) : PlayState.STOP;
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