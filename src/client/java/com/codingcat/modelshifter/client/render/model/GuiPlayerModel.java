package com.codingcat.modelshifter.client.render.model;

import com.codingcat.modelshifter.client.render.entity.ReplacedPlayerEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.loading.math.MathParser;
import software.bernie.geckolib.loading.math.MolangQueries;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class GuiPlayerModel extends DefaultedEntityGeoModel<ReplacedPlayerEntity> {
    public GuiPlayerModel(Identifier assetSubpath) {
        super(assetSubpath);
    }

    //? <1.21 {
    /*@SuppressWarnings("removal")
    public void applyMolangQueries(ReplacedPlayerEntity animatable, double animTime) {
        MathParser.setVariable(MolangQueries.LIFE_TIME, () -> animTime / 20d);
    }
    *///?} else {
    @Override
    public void applyMolangQueries(AnimationState<ReplacedPlayerEntity> animationState, double animTime) {
        MathParser.setVariable(MolangQueries.LIFE_TIME, () -> animTime / 20d);
    }
    //?}
}
