package com.cosmeticsgalore.client;

import com.cosmeticsgalore.CosmeticsGalore;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
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

		// Register HUD render callback to render cosmetics
		HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
			MinecraftClient client = MinecraftClient.getInstance();
			if (client.world != null && client.player != null) {
				// Render cosmetics for all nearby players
				client.world.getPlayers().forEach(player -> {
					if (player instanceof AbstractClientPlayerEntity clientPlayer) {
						CosmeticsManager.PlayerCosmetics cosmetics = CosmeticsManager.getCosmetics(clientPlayer.getUuid());

						// If cosmetics aren't loaded yet, fetch them
						if (cosmetics.cape == null && cosmetics.hat == null) {
							CosmeticsManager.fetchCosmetics(clientPlayer.getUuid(), clientPlayer.getName().getString());
						}
					}
				});
			}
		});
	}
}
