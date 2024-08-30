package com.codingcat.modelshifter.client.impl.model;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.api.model.PlayerModel;
import com.codingcat.modelshifter.client.api.state.DisabledFeatureRenderers;
import net.minecraft.util.Identifier;

public class AmongUsPlayerModel extends PlayerModel {
    public AmongUsPlayerModel() {
        super(new Identifier(ModelShifterClient.MOD_ID, "among_us_player"), new DisabledFeatureRenderers(
                true,
                false,
                false,
                true,
                true,
                false,
                false,
                true
        ));
    }
}
