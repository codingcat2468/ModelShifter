package com.codingcat.modelshifter.client.api.renderer.animation;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.constant.DefaultAnimations;

import java.util.Comparator;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class ModelAnimationController<T> {
    public static final String SNEAK_NAME = "move.sneak";
    private final HashSet<PredicateBasedAnimation<T>> animations;

    public ModelAnimationController() {
        this.animations = new HashSet<>();
    }

    public static ModelAnimationController<PlayerEntity> createDefaultController() {
        return new ModelAnimationController<PlayerEntity>()
                .add(0, Entity::isInSneakingPose, animation -> animation.thenPlayAndHold(SNEAK_NAME))
                .add(1, Entity::isSprinting, animation -> DefaultAnimations.RUN)
                .add(2, ModelAnimationController::checkMovement, animation -> DefaultAnimations.WALK)
                .add(3, animation -> DefaultAnimations.IDLE);
    }

    private static boolean checkMovement(PlayerEntity player) {
        Vec3d velocity = player.getVelocity();
        float avgVelocity = (float) (Math.abs(velocity.x) + Math.abs(velocity.z)) / 2f;

        return avgVelocity >= 0.015f;
    }

    public ModelAnimationController<T> add(int priority, @NotNull Predicate<T> predicate, @NotNull Function<RawAnimation, @NotNull RawAnimation> appendData) {
        this.animations.add(new PredicateBasedAnimation<>(priority, predicate, appendData.apply(RawAnimation.begin())));
        return this;
    }

    public ModelAnimationController<T> add(int priority, @NotNull Function<RawAnimation, @NotNull RawAnimation> appendData) {
        return this.add(priority, player -> true, appendData);
    }

    public ModelAnimationController<T> remove(int priority) {
        this.animations.removeIf(animation -> animation.priority() == priority);
        return this;
    }

    public Optional<PredicateBasedAnimation<T>> get(int priority) {
        return this.animations.stream()
                .filter(animation -> animation.priority() == priority)
                .findFirst();
    }

    public ModelAnimationController<T> replace(int priority, @NotNull Function<RawAnimation, @NotNull RawAnimation> appendData) throws NoSuchElementException {
        Optional<PredicateBasedAnimation<T>> oldAnimation = this.get(priority);
        this.remove(priority);
        return this.add(priority, oldAnimation.orElseThrow().predicate(), appendData);
    }

    @Nullable
    public RawAnimation getAnimation(T object) {
        Optional<PredicateBasedAnimation<T>> animation = this.animations
                .stream()
                .filter(a -> a.predicate().test(object))
                .min(Comparator.comparingInt(PredicateBasedAnimation::priority));

        return animation.map(PredicateBasedAnimation::animation).orElse(null);
    }
}
