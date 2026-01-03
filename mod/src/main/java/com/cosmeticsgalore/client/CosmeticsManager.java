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

		if (json.has("cape") && !json.get("cape").isJsonNull()) {
			cosmetics.cape = json.get("cape").getAsString();
		}
		if (json.has("hat") && !json.get("hat").isJsonNull()) {
			cosmetics.hat = json.get("hat").getAsString();
		}
		if (json.has("headband") && !json.get("headband").isJsonNull()) {
			cosmetics.headband = json.get("headband").getAsString();
		}
		if (json.has("shield") && !json.get("shield").isJsonNull()) {
			cosmetics.shield = json.get("shield").getAsString();
		}
		if (json.has("sword") && !json.get("sword").isJsonNull()) {
			cosmetics.sword = json.get("sword").getAsString();
		}
		if (json.has("cloak") && !json.get("cloak").isJsonNull()) {
			cosmetics.cloak = json.get("cloak").getAsString();
		}
		if (json.has("aura") && !json.get("aura").isJsonNull()) {
			cosmetics.aura = json.get("aura").getAsString();
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
		public String cape;
		public String hat;
		public String headband;
		public String shield;
		public String sword;
		public String cloak;
		public String aura;

		public PlayerCosmetics() {
			this.cape = null;
			this.hat = null;
			this.headband = null;
			this.shield = null;
			this.sword = null;
			this.cloak = null;
			this.aura = null;
		}

		public boolean hasCape() { return cape != null && !cape.isEmpty(); }
		public boolean hasHat() { return hat != null && !hat.isEmpty(); }
		public boolean hasHeadband() { return headband != null && !headband.isEmpty(); }
		public boolean hasShield() { return shield != null && !shield.isEmpty(); }
		public boolean hasSword() { return sword != null && !sword.isEmpty(); }
		public boolean hasCloak() { return cloak != null && !cloak.isEmpty(); }
		public boolean hasAura() { return aura != null && !aura.isEmpty(); }
	}
}
