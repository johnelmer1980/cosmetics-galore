package com.cosmeticsgalore.mixin;

import com.cosmeticsgalore.client.CosmeticsManager;
import com.cosmeticsgalore.client.CosmeticsRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends EntityRenderer<AbstractClientPlayerEntity> {

	protected PlayerEntityRendererMixin() {
		super(null);
	}

	@Inject(method = "render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("RETURN"))
	private void onRender(AbstractClientPlayerEntity player, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
		try {
			// Fetch cosmetics if not already cached
			CosmeticsManager.PlayerCosmetics cosmetics = CosmeticsManager.getCosmetics(player.getUuid());

			// If cosmetics aren't loaded yet, fetch them
			if (cosmetics.cape == null && cosmetics.hat == null) {
				CosmeticsManager.fetchCosmetics(player.getUuid(), player.getName().getString());
			}

			// Debug: Log when we have cosmetics to render
			if (cosmetics.hasCape() || cosmetics.hasAura() || cosmetics.hasHat()) {
				com.cosmeticsgalore.CosmeticsGalore.LOGGER.info("Rendering cosmetics for {}: cape={}, aura={}, hat={}",
					player.getName().getString(), cosmetics.cape, cosmetics.aura, cosmetics.hat);
			}

			// Render cosmetics
			CosmeticsRenderer.render(player, matrices, vertexConsumers, light, cosmetics);
		} catch (Exception e) {
			com.cosmeticsgalore.CosmeticsGalore.LOGGER.error("Error in cosmetics render mixin: {}", e.getMessage());
		}
	}
}
