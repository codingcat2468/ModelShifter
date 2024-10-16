package com.codingcat.modelshifter.client.render.model;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.render.entity.ReplacedPlayerEntity;
import net.minecraft.util.Identifier;
//? >=1.21 {
import software.bernie.geckolib.animation.AnimationState;
//?} else {
/*import java.util.function.DoubleSupplier;
*///?}
//? >1.20.4 {
import software.bernie.geckolib.loading.math.MathParser;
import software.bernie.geckolib.loading.math.MolangQueries;
//?} else {
/*import software.bernie.geckolib.core.molang.MolangParser;
import software.bernie.geckolib.core.molang.MolangQueries;
*///?}
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class GuiPlayerModel extends DefaultedEntityGeoModel<ReplacedPlayerEntity> {
    public GuiPlayerModel(Identifier assetSubpath) {
        super(assetSubpath);
        this.withAltTexture(ModelShifterClient.EMPTY_TEXTURE);
    }

    //? <1.21 {
    /*@SuppressWarnings("removal")
    @Override
    public void applyMolangQueries(ReplacedPlayerEntity animatable, double animTime) {
        DoubleSupplier lifeTimeSupplier = () -> animTime / 20d;
        //? >1.20.4 {
        /^MathParser.setVariable(MolangQueries.LIFE_TIME, lifeTimeSupplier);
        ^///?} else {
        MolangParser.INSTANCE.setMemoizedValue(MolangQueries.LIFE_TIME, lifeTimeSupplier);
        //?}
    }
    *///?} else {
    @Override
    public void applyMolangQueries(AnimationState<ReplacedPlayerEntity> animationState, double animTime) {
        MathParser.setVariable(MolangQueries.LIFE_TIME, () -> animTime / 20d);
    }
    //?}
}