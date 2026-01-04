package com.cosmeticsgalore.mixin;

import com.cosmeticsgalore.client.CosmeticsManager;
import com.cosmeticsgalore.client.CosmeticsRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.entity.PlayerLikeEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends EntityRenderer<AbstractClientPlayerEntity, PlayerEntityRenderState> {

	protected PlayerEntityRendererMixin() {
		super(null);
	}

	// Target the updateRenderState method to fetch cosmetics and associate state with player
	@Inject(method = "updateRenderState(Lnet/minecraft/entity/PlayerLikeEntity;Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;F)V", at = @At("RETURN"))
	private void onUpdateRenderState(PlayerLikeEntity player, PlayerEntityRenderState state, float tickDelta, CallbackInfo ci) {
		try {
			// Only handle client players
			if (!(player instanceof AbstractClientPlayerEntity)) {
				return;
			}

			AbstractClientPlayerEntity clientPlayer = (AbstractClientPlayerEntity) player;

			// Associate state with player for feature renderer
			CosmeticsRenderer.associateStateWithPlayer(state, clientPlayer);

			// Fetch cosmetics if not already cached
			CosmeticsManager.PlayerCosmetics cosmetics = CosmeticsManager.getCosmetics(clientPlayer.getGameProfile().id());

			// If cosmetics aren't loaded yet, fetch them
			if (cosmetics.cape == null && cosmetics.hat == null && cosmetics.aura == null) {
				CosmeticsManager.fetchCosmetics(clientPlayer.getGameProfile().id(), clientPlayer.getName().getString());
			}
		} catch (Exception e) {
			com.cosmeticsgalore.CosmeticsGalore.LOGGER.error("Error fetching cosmetics: {}", e.getMessage());
		}
	}
}
