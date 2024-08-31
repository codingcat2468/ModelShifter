package com.codingcat.modelshifter.client.render.model;

import com.codingcat.modelshifter.client.render.entity.ReplacedPlayerEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class GuiPlayerModel extends DefaultedEntityGeoModel<ReplacedPlayerEntity> {
    public GuiPlayerModel(Identifier assetSubpath) {
        super(assetSubpath);
    }

    @SuppressWarnings("removal")
    @Override
    public void applyMolangQueries(ReplacedPlayerEntity animatable, double animTime) {}
}
