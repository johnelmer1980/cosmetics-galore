package com.cosmeticsgalore.client;

import com.cosmeticsgalore.CosmeticsGalore;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;

public class CosmeticsGaloreClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		CosmeticsGalore.LOGGER.info("Cosmetics Galore Client initialized!");

		// Initialize cosmetics manager
		CosmeticsManager.initialize();

		// Refresh cosmetics data periodically (every 5 minutes)
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.world != null && client.world.getTime() % 6000 == 0) {
				CosmeticsManager.refreshAllCosmetics();
			}
		});

		// Render particle auras for nearby players
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.world != null && client.player != null) {
				client.world.getPlayers().forEach(player -> {
					if (player instanceof AbstractClientPlayerEntity clientPlayer) {
						CosmeticsManager.PlayerCosmetics cosmetics = CosmeticsManager.getCosmetics(clientPlayer.getGameProfile().id());

						// Render aura particles
						if (cosmetics.hasAura()) {
							CosmeticsRenderer.renderAuraParticles(clientPlayer, cosmetics.aura);
						}
					}
				});
			}
		});

		CosmeticsGalore.LOGGER.info("Cosmetics Galore initialized with particle aura support!");
		CosmeticsGalore.LOGGER.info("NOTE: Cape/hat/shield rendering requires WorldRenderEvents (removed in 1.21.9+)");
		CosmeticsGalore.LOGGER.info("See https://github.com/FabricMC/fabric/issues/4902 for status");
	}
}
