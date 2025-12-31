package com.cosmeticsgalore.mixin;

import com.cosmeticsgalore.client.CosmeticsManager;
import com.cosmeticsgalore.client.CosmeticsRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {

	@Inject(method = "render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("TAIL"))
	private void onRender(AbstractClientPlayerEntity player, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
		// Fetch cosmetics if not already cached
		CosmeticsManager.PlayerCosmetics cosmetics = CosmeticsManager.getCosmetics(player.getUuid());

		// If cosmetics aren't loaded yet, fetch them
		if (cosmetics.cape == null && cosmetics.hat == null) {
			CosmeticsManager.fetchCosmetics(player.getUuid(), player.getName().getString());
		}

		// Render cosmetics
		CosmeticsRenderer.render(player, matrices, vertexConsumers, light, cosmetics);
	}
}
