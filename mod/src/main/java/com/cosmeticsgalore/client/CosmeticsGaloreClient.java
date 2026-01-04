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
		CosmeticsGalore.LOGGER.info("NOTE: Fabric API 0.138.4 doesn't include WorldRenderEvents");
		CosmeticsGalore.LOGGER.info("Cape/hat/shield rendering will be added when upgrading to newer Fabric API");
	}
}
