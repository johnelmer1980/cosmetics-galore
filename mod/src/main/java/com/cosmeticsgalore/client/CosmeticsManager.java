package com.cosmeticsgalore.client;

import com.cosmeticsgalore.CosmeticsGalore;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;

public class CosmeticsManager {
	private static final String API_URL = "https://cosmetics-galore.vercel.app/api";
	private static final HttpClient httpClient = HttpClient.newHttpClient();
	private static final Gson gson = new Gson();
	private static final Map<UUID, PlayerCosmetics> cosmeticsCache = new ConcurrentHashMap<>();

	public static void initialize() {
		CosmeticsGalore.LOGGER.info("Cosmetics Manager initialized");
	}

	public static void fetchCosmetics(UUID playerUuid, String username) {
		CompletableFuture.runAsync(() -> {
			try {
				HttpRequest request = HttpRequest.newBuilder()
						.uri(URI.create(API_URL + "/" + username))
						.GET()
						.build();

				httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
						.thenAccept(response -> {
							if (response.statusCode() == 200) {
								JsonObject json = gson.fromJson(response.body(), JsonObject.class);
								PlayerCosmetics cosmetics = parseCosmetics(json);
								cosmeticsCache.put(playerUuid, cosmetics);
								CosmeticsGalore.LOGGER.info("Loaded cosmetics for {}", username);
							}
						})
						.exceptionally(e -> {
							CosmeticsGalore.LOGGER.error("Failed to fetch cosmetics for {}: {}", username, e.getMessage());
							return null;
						});
			} catch (Exception e) {
				CosmeticsGalore.LOGGER.error("Error fetching cosmetics: {}", e.getMessage());
			}
		});
	}

	private static PlayerCosmetics parseCosmetics(JsonObject json) {
		PlayerCosmetics cosmetics = new PlayerCosmetics();

		// Only parse particle-based cosmetics (compatible with 1.21.10)
		if (json.has("aura") && !json.get("aura").isJsonNull()) {
			cosmetics.aura = json.get("aura").getAsString();
		}
		if (json.has("wings") && !json.get("wings").isJsonNull()) {
			cosmetics.wings = json.get("wings").getAsString();
		}
		if (json.has("trail") && !json.get("trail").isJsonNull()) {
			cosmetics.trail = json.get("trail").getAsString();
		}
		if (json.has("crown") && !json.get("crown").isJsonNull()) {
			cosmetics.crown = json.get("crown").getAsString();
		}
		if (json.has("footsteps") && !json.get("footsteps").isJsonNull()) {
			cosmetics.footsteps = json.get("footsteps").getAsString();
		}

		return cosmetics;
	}

	public static PlayerCosmetics getCosmetics(UUID playerUuid) {
		return cosmeticsCache.getOrDefault(playerUuid, new PlayerCosmetics());
	}

	public static void refreshAllCosmetics() {
		// Refresh cosmetics for all cached players
		cosmeticsCache.keySet().forEach(uuid -> {
			// In a real implementation, you'd track usernames too
			// For now, this is a placeholder for the refresh logic
		});
	}

	public static class PlayerCosmetics {
		public String aura;
		public String wings;
		public String trail;
		public String crown;
		public String footsteps;

		public PlayerCosmetics() {
			this.aura = null;
			this.wings = null;
			this.trail = null;
			this.crown = null;
			this.footsteps = null;
		}

		public boolean hasAura() { return aura != null && !aura.isEmpty(); }
		public boolean hasWings() { return wings != null && !wings.isEmpty(); }
		public boolean hasTrail() { return trail != null && !trail.isEmpty(); }
		public boolean hasCrown() { return crown != null && !crown.isEmpty(); }
		public boolean hasFootsteps() { return footsteps != null && !footsteps.isEmpty(); }
	}
}
