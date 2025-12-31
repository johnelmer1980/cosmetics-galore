package com.cosmeticsgalore.client;

import com.cosmeticsgalore.CosmeticsGalore;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

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
	}
}
