package com.cosmeticsgalore.mixin;

import com.cosmeticsgalore.client.CosmeticsManager;
import com.cosmeticsgalore.client.CosmeticsRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends EntityRenderer<AbstractClientPlayerEntity, PlayerEntityRenderState> {

	protected PlayerEntityRendererMixin() {
		super(null);
	}

	// Target the new updateRenderState method to store player reference
	// Then use the render method with the new signature
	@Inject(method = "updateRenderState(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;F)V", at = @At("RETURN"))
	private void onUpdateRenderState(AbstractClientPlayerEntity player, PlayerEntityRenderState state, float tickDelta, CallbackInfo ci) {
		try {
			// Associate state with player for later rendering
			CosmeticsRenderer.associateStateWithPlayer(state, player);

			// Fetch cosmetics if not already cached
			CosmeticsManager.PlayerCosmetics cosmetics = CosmeticsManager.getCosmetics(player.getGameProfile().id());

			// If cosmetics aren't loaded yet, fetch them
			if (cosmetics.cape == null && cosmetics.hat == null) {
				CosmeticsManager.fetchCosmetics(player.getGameProfile().id(), player.getName().getString());
			}
		} catch (Exception e) {
			com.cosmeticsgalore.CosmeticsGalore.LOGGER.error("Error fetching cosmetics: {}", e.getMessage());
		}
	}

	@Inject(method = "render(Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("RETURN"))
	private void onRender(PlayerEntityRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
		try {
			// We need to get the player from the state
			// For now, we'll render cosmetics based on what we have cached
			// This is a limitation of the new render state system
			// TODO: Find a better way to associate state with player entity
			CosmeticsRenderer.renderFromState(state, matrices, vertexConsumers, light);
		} catch (Exception e) {
			com.cosmeticsgalore.CosmeticsGalore.LOGGER.error("Error in cosmetics render mixin: {}", e.getMessage());
		}
	}
}
