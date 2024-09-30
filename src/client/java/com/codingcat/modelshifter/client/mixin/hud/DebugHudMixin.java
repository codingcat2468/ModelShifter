package com.codingcat.modelshifter.client.mixin.hud;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.api.registry.ModelRegistry;
import com.codingcat.modelshifter.client.api.renderer.AdditionalRendererState;
import com.codingcat.modelshifter.client.api.renderer.DynamicAdditionalRendererHolder;
import com.codingcat.modelshifter.client.api.renderer.PlayerDependentStateHolder;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(DebugHud.class)
public class DebugHudMixin {
    @Inject(
            method = "getLeftText",
            at = @At(value = "RETURN", ordinal = 1),
            cancellable = true)
    public void injectMSDebugInfo(CallbackInfoReturnable<List<String>> cir) {
        ArrayList<String> list = new ArrayList<>(cir.getReturnValue());
        PlayerDependentStateHolder stateHolder = ModelShifterClient.state;
        DynamicAdditionalRendererHolder rendererHolder = ModelShifterClient.holder;
        GameProfile gameProfile = MinecraftClient.getInstance().getGameProfile();
        AdditionalRendererState state = stateHolder.getState(gameProfile.getId());
        Identifier modelId = null;
        if (state.getPlayerModel() != null) {
            Optional<Identifier> model = ModelRegistry.findId(state.getPlayerModel());
            modelId = model.orElse(null);
        }

        String renderInfo = state.isRendererEnabled() ? String.format("active, model: %s", modelId) : "disabled";
        String activeRenderers = getCountString(rendererHolder.getRendererCount());
        String totalStates = getCountString(stateHolder.getStateCount());
        String activeStates = getCountString(stateHolder.getActiveStateCount());
        list.add(String.format("[MS: SELF] Additional renderer %s", renderInfo));
        list.add(String.format("[MS: ALL] Renderer instances: %s, state override instances: %s/%s", activeRenderers, activeStates, totalStates));

        cir.setReturnValue(list);
    }

    @Unique
    private String getCountString(int count) {
        return count > 0 ? String.valueOf(count) : "none";
    }
}
