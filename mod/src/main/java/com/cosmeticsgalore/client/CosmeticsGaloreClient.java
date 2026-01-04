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

		// Fetch cosmetics for nearby players
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.world != null && client.player != null) {
				client.world.getPlayers().forEach(player -> {
					if (player instanceof AbstractClientPlayerEntity clientPlayer) {
						CosmeticsManager.PlayerCosmetics cosmetics = CosmeticsManager.getCosmetics(clientPlayer.getGameProfile().id());

						// If cosmetics aren't loaded yet, fetch them
						if (cosmetics.cape == null && cosmetics.hat == null) {
							CosmeticsManager.fetchCosmetics(clientPlayer.getGameProfile().id(), clientPlayer.getName().getString());
						}
					}
				});
			}
		});

		CosmeticsGalore.LOGGER.info("NOTE: Cosmetics rendering requires the mixin to work. If cosmetics don't appear, the mixin may not be compatible with your Minecraft version.");
	}
}
