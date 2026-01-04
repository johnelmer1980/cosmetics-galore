package com.cosmeticsgalore.client;

import com.cosmeticsgalore.CosmeticsGalore;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.feature.CapeFeatureRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.world.ClientWorld;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CosmeticsRenderer {

	// Store player references during updateRenderState for later use in render
	private static final Map<PlayerEntityRenderState, AbstractClientPlayerEntity> stateToPlayerMap = new ConcurrentHashMap<>();

	// Public method to associate state with player
	public static void associateStateWithPlayer(PlayerEntityRenderState state, AbstractClientPlayerEntity player) {
		stateToPlayerMap.put(state, player);
	}

	// Public method to get player from state
	public static AbstractClientPlayerEntity getPlayerFromState(PlayerEntityRenderState state) {
		return stateToPlayerMap.get(state);
	}

	// Public method to render aura particles for a player
	public static void renderAuraParticles(AbstractClientPlayerEntity player, String auraId) {
		if (player == null || auraId == null) {
			return;
		}

		MinecraftClient client = MinecraftClient.getInstance();
		ClientWorld world = client.world;
		if (world == null || client.particleManager == null) {
			return;
		}

		// Only spawn particles every few ticks to avoid lag
		if (world.getTime() % 4 != 0) {
			return;
		}

		// Spawn particles at player position
		spawnAuraParticles(player, world, client, auraId);
	}

	// Public method to render wings particles
	public static void renderWingsParticles(AbstractClientPlayerEntity player, String wingsId) {
		if (player == null || wingsId == null) {
			return;
		}

		MinecraftClient client = MinecraftClient.getInstance();
		ClientWorld world = client.world;
		if (world == null || client.particleManager == null) {
			return;
		}

		// Only spawn particles every few ticks
		if (world.getTime() % 3 != 0) {
			return;
		}

		spawnWingsParticles(player, world, client, wingsId);
	}

	// Public method to render trail particles
	public static void renderTrailParticles(AbstractClientPlayerEntity player, String trailId) {
		if (player == null || trailId == null) {
			return;
		}

		MinecraftClient client = MinecraftClient.getInstance();
		ClientWorld world = client.world;
		if (world == null || client.particleManager == null) {
			return;
		}

		// Only spawn trail if player is moving
		if (player.getVelocity().lengthSquared() < 0.01) {
			return;
		}

		// Spawn trail particles every tick when moving
		spawnTrailParticles(player, world, client, trailId);
	}

	// Public method to render crown particles
	public static void renderCrownParticles(AbstractClientPlayerEntity player, String crownId) {
		if (player == null || crownId == null) {
			return;
		}

		MinecraftClient client = MinecraftClient.getInstance();
		ClientWorld world = client.world;
		if (world == null || client.particleManager == null) {
			return;
		}

		// Spawn crown particles every few ticks
		if (world.getTime() % 5 != 0) {
			return;
		}

		spawnCrownParticles(player, world, client, crownId);
	}

	// Public method to render footstep particles
	public static void renderFootstepParticles(AbstractClientPlayerEntity player, String footstepsId) {
		if (player == null || footstepsId == null) {
			return;
		}

		MinecraftClient client = MinecraftClient.getInstance();
		ClientWorld world = client.world;
		if (world == null || client.particleManager == null) {
			return;
		}

		// Only spawn footsteps if player is moving and on ground
		if (player.getVelocity().lengthSquared() < 0.01 || !player.isOnGround()) {
			return;
		}

		// Spawn footstep particles every few ticks when walking
		if (world.getTime() % 6 != 0) {
			return;
		}

		spawnFootstepParticles(player, world, client, footstepsId);
	}


	private static void spawnAuraParticles(AbstractClientPlayerEntity player, ClientWorld world, MinecraftClient client, String auraId) {
		// Use player position directly
		double x = player.getX();
		double y = player.getY() + 1.0;
		double z = player.getZ();

		switch (auraId) {
			case "fire_aura":
				// Flame particles in a circle around player
				for (int i = 0; i < 2; i++) {
					double angle = Math.random() * Math.PI * 2;
					double radius = 0.4 + Math.random() * 0.3;
					double offsetX = Math.cos(angle) * radius;
					double offsetZ = Math.sin(angle) * radius;
					double offsetY = (Math.random() - 0.5) * 1.5;

					client.particleManager.addParticle(
						ParticleTypes.FLAME,
						x + offsetX, y + offsetY, z + offsetZ,
						0.0, 0.05, 0.0
					);
				}
				break;

			case "ice_aura":
				// Snowflake particles falling around player
				for (int i = 0; i < 2; i++) {
					double offsetX = (Math.random() - 0.5) * 0.8;
					double offsetZ = (Math.random() - 0.5) * 0.8;
					double offsetY = Math.random() * 1.5;

					client.particleManager.addParticle(
						ParticleTypes.SNOWFLAKE,
						x + offsetX, y + offsetY, z + offsetZ,
						0.0, -0.02, 0.0
					);
				}
				break;

			case "lightning_aura":
				// Electric sparks
				for (int i = 0; i < 3; i++) {
					double offsetX = (Math.random() - 0.5) * 0.6;
					double offsetZ = (Math.random() - 0.5) * 0.6;
					double offsetY = Math.random() * 1.8;

					client.particleManager.addParticle(
						ParticleTypes.ELECTRIC_SPARK,
						x + offsetX, y + offsetY, z + offsetZ,
						0.0, 0.0, 0.0
					);
				}
				break;

			case "hearts_aura":
				// Heart particles floating up
				if (world.getTime() % 8 == 0) {
					double offsetX = (Math.random() - 0.5) * 0.6;
					double offsetZ = (Math.random() - 0.5) * 0.6;

					client.particleManager.addParticle(
						ParticleTypes.HEART,
						x + offsetX, y, z + offsetZ,
						0.0, 0.1, 0.0
					);
				}
				break;

			case "soul_aura":
				// Soul fire flame particles (blue flames)
				for (int i = 0; i < 2; i++) {
					double angle = Math.random() * Math.PI * 2;
					double radius = 0.4 + Math.random() * 0.3;
					double offsetX = Math.cos(angle) * radius;
					double offsetZ = Math.sin(angle) * radius;
					double offsetY = (Math.random() - 0.5) * 1.5;

					client.particleManager.addParticle(
						ParticleTypes.SOUL_FIRE_FLAME,
						x + offsetX, y + offsetY, z + offsetZ,
						0.0, 0.05, 0.0
					);
				}
				break;

			case "enchant_aura":
				// Enchantment table particles
				for (int i = 0; i < 3; i++) {
					double offsetX = (Math.random() - 0.5) * 0.8;
					double offsetZ = (Math.random() - 0.5) * 0.8;
					double offsetY = Math.random() * 1.5;

					client.particleManager.addParticle(
						ParticleTypes.ENCHANT,
						x + offsetX, y + offsetY, z + offsetZ,
						(Math.random() - 0.5) * 0.1, 0.05, (Math.random() - 0.5) * 0.1
					);
				}
				break;

			case "portal_aura":
				// Portal particles swirling
				for (int i = 0; i < 2; i++) {
					double angle = (world.getTime() * 0.1 + Math.random() * 2) % (Math.PI * 2);
					double radius = 0.5;
					double offsetX = Math.cos(angle) * radius;
					double offsetZ = Math.sin(angle) * radius;
					double offsetY = Math.random() * 1.8;

					client.particleManager.addParticle(
						ParticleTypes.PORTAL,
						x + offsetX, y + offsetY, z + offsetZ,
						(Math.random() - 0.5) * 0.1, -0.05, (Math.random() - 0.5) * 0.1
					);
				}
				break;

			case "cherry_aura":
				// Cherry blossom petals
				if (world.getTime() % 6 == 0) {
					double offsetX = (Math.random() - 0.5) * 0.8;
					double offsetZ = (Math.random() - 0.5) * 0.8;
					double offsetY = Math.random() * 2.0;

					client.particleManager.addParticle(
						ParticleTypes.CHERRY_LEAVES,
						x + offsetX, y + offsetY, z + offsetZ,
						(Math.random() - 0.5) * 0.05, -0.03, (Math.random() - 0.5) * 0.05
					);
				}
				break;

			default:
				CosmeticsGalore.LOGGER.debug("Unknown aura type: {}", auraId);
				break;
		}
	}

	private static void spawnWingsParticles(AbstractClientPlayerEntity player, ClientWorld world, MinecraftClient client, String wingsId) {
		double x = player.getX();
		double y = player.getY() + 1.3; // Shoulder height
		double z = player.getZ();

		// Calculate wing positions based on player rotation
		float yaw = (float) Math.toRadians(player.getYaw());
		double leftX = x + Math.cos(yaw + Math.PI / 2) * 0.3;
		double leftZ = z + Math.sin(yaw + Math.PI / 2) * 0.3;
		double rightX = x + Math.cos(yaw - Math.PI / 2) * 0.3;
		double rightZ = z + Math.sin(yaw - Math.PI / 2) * 0.3;

		switch (wingsId) {
			case "angel_wings":
				// White feather-like particles from shoulders
				for (int i = 0; i < 2; i++) {
					double offsetY = (Math.random() - 0.5) * 0.5;
					client.particleManager.addParticle(ParticleTypes.END_ROD, leftX, y + offsetY, leftZ, 0.0, 0.0, 0.0);
					client.particleManager.addParticle(ParticleTypes.END_ROD, rightX, y + offsetY, rightZ, 0.0, 0.0, 0.0);
				}
				break;

			case "demon_wings":
				// Dark smoke particles
				for (int i = 0; i < 2; i++) {
					double offsetY = (Math.random() - 0.5) * 0.5;
					client.particleManager.addParticle(ParticleTypes.LARGE_SMOKE, leftX, y + offsetY, leftZ, 0.0, -0.02, 0.0);
					client.particleManager.addParticle(ParticleTypes.LARGE_SMOKE, rightX, y + offsetY, rightZ, 0.0, -0.02, 0.0);
				}
				break;

			case "dragon_wings":
				// Fiery particles
				for (int i = 0; i < 2; i++) {
					double offsetY = (Math.random() - 0.5) * 0.5;
					client.particleManager.addParticle(ParticleTypes.FLAME, leftX, y + offsetY, leftZ, 0.0, 0.02, 0.0);
					client.particleManager.addParticle(ParticleTypes.FLAME, rightX, y + offsetY, rightZ, 0.0, 0.02, 0.0);
				}
				break;

			case "butterfly_wings":
				// Colorful sparkle particles
				if (world.getTime() % 2 == 0) {
					client.particleManager.addParticle(ParticleTypes.WAX_ON, leftX, y, leftZ, 0.0, 0.0, 0.0);
					client.particleManager.addParticle(ParticleTypes.WAX_ON, rightX, y, rightZ, 0.0, 0.0, 0.0);
				}
				break;

			case "fairy_wings":
				// Sparkly magical particles
				for (int i = 0; i < 2; i++) {
					double offsetY = (Math.random() - 0.5) * 0.5;
					client.particleManager.addParticle(ParticleTypes.ENCHANT, leftX, y + offsetY, leftZ, 0.0, 0.01, 0.0);
					client.particleManager.addParticle(ParticleTypes.ENCHANT, rightX, y + offsetY, rightZ, 0.0, 0.01, 0.0);
				}
				break;

			default:
				CosmeticsGalore.LOGGER.debug("Unknown wings type: {}", wingsId);
				break;
		}
	}

	private static void spawnTrailParticles(AbstractClientPlayerEntity player, ClientWorld world, MinecraftClient client, String trailId) {
		double x = player.getX();
		double y = player.getY() + 0.1; // Ground level
		double z = player.getZ();

		switch (trailId) {
			case "rainbow_trail":
				// Multiple colored particles (using various particle types for color variety)
				client.particleManager.addParticle(ParticleTypes.WAX_ON, x, y, z, 0.0, 0.0, 0.0);
				client.particleManager.addParticle(ParticleTypes.SCRAPE, x, y, z, 0.0, 0.0, 0.0);
				break;

			case "star_trail":
				// Twinkling star particles
				if (world.getTime() % 2 == 0) {
					client.particleManager.addParticle(ParticleTypes.END_ROD, x, y + 0.3, z, 0.0, 0.0, 0.0);
				}
				break;

			case "smoke_trail":
				// Smoke particles
				client.particleManager.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 0.0, 0.05, 0.0);
				break;

			case "redstone_trail":
				// Redstone dust particles
				client.particleManager.addParticle(ParticleTypes.DUST_PLUME, x, y, z, 0.0, 0.0, 0.0);
				break;

			case "slime_trail":
				// Slime particles
				client.particleManager.addParticle(ParticleTypes.ITEM_SLIME, x, y, z, 0.0, 0.0, 0.0);
				break;

			default:
				CosmeticsGalore.LOGGER.debug("Unknown trail type: {}", trailId);
				break;
		}
	}

	private static void spawnCrownParticles(AbstractClientPlayerEntity player, ClientWorld world, MinecraftClient client, String crownId) {
		double x = player.getX();
		double y = player.getY() + 2.2; // Above head
		double z = player.getZ();

		switch (crownId) {
			case "golden_crown":
				// Golden sparkle particles in a circle above head
				double angle = (world.getTime() * 0.1) % (Math.PI * 2);
				for (int i = 0; i < 3; i++) {
					double crownAngle = angle + (i * Math.PI * 2 / 3);
					double radius = 0.3;
					double offsetX = Math.cos(crownAngle) * radius;
					double offsetZ = Math.sin(crownAngle) * radius;
					client.particleManager.addParticle(ParticleTypes.WAX_ON, x + offsetX, y, z + offsetZ, 0.0, 0.0, 0.0);
				}
				break;

			case "diamond_crown":
				// Diamond sparkles
				double diamondAngle = (world.getTime() * 0.15) % (Math.PI * 2);
				for (int i = 0; i < 4; i++) {
					double crownAngle = diamondAngle + (i * Math.PI * 2 / 4);
					double radius = 0.3;
					double offsetX = Math.cos(crownAngle) * radius;
					double offsetZ = Math.sin(crownAngle) * radius;
					client.particleManager.addParticle(ParticleTypes.GLOW, x + offsetX, y, z + offsetZ, 0.0, 0.0, 0.0);
				}
				break;

			case "star_crown":
				// Rotating stars
				double starAngle = (world.getTime() * 0.2) % (Math.PI * 2);
				for (int i = 0; i < 5; i++) {
					double crownAngle = starAngle + (i * Math.PI * 2 / 5);
					double radius = 0.35;
					double offsetX = Math.cos(crownAngle) * radius;
					double offsetZ = Math.sin(crownAngle) * radius;
					client.particleManager.addParticle(ParticleTypes.END_ROD, x + offsetX, y, z + offsetZ, 0.0, 0.0, 0.0);
				}
				break;

			case "halo":
				// Angelic white particle ring
				double haloAngle = (world.getTime() * 0.08) % (Math.PI * 2);
				for (int i = 0; i < 8; i++) {
					double crownAngle = haloAngle + (i * Math.PI * 2 / 8);
					double radius = 0.4;
					double offsetX = Math.cos(crownAngle) * radius;
					double offsetZ = Math.sin(crownAngle) * radius;
					client.particleManager.addParticle(ParticleTypes.END_ROD, x + offsetX, y + 0.1, z + offsetZ, 0.0, 0.0, 0.0);
				}
				break;

			default:
				CosmeticsGalore.LOGGER.debug("Unknown crown type: {}", crownId);
				break;
		}
	}

	private static void spawnFootstepParticles(AbstractClientPlayerEntity player, ClientWorld world, MinecraftClient client, String footstepsId) {
		double x = player.getX();
		double y = player.getY() + 0.05; // Just above ground
		double z = player.getZ();

		switch (footstepsId) {
			case "fire_steps":
				// Flame particles at feet
				for (int i = 0; i < 3; i++) {
					double offsetX = (Math.random() - 0.5) * 0.3;
					double offsetZ = (Math.random() - 0.5) * 0.3;
					client.particleManager.addParticle(ParticleTypes.FLAME, x + offsetX, y, z + offsetZ, 0.0, 0.01, 0.0);
				}
				break;

			case "flower_steps":
				// Flower/nature particles
				if (world.getTime() % 2 == 0) {
					double offsetX = (Math.random() - 0.5) * 0.3;
					double offsetZ = (Math.random() - 0.5) * 0.3;
					client.particleManager.addParticle(ParticleTypes.CHERRY_LEAVES, x + offsetX, y, z + offsetZ, 0.0, 0.02, 0.0);
				}
				break;

			case "water_steps":
				// Water splash particles
				for (int i = 0; i < 2; i++) {
					double offsetX = (Math.random() - 0.5) * 0.3;
					double offsetZ = (Math.random() - 0.5) * 0.3;
					client.particleManager.addParticle(ParticleTypes.SPLASH, x + offsetX, y, z + offsetZ, 0.0, 0.05, 0.0);
				}
				break;

			case "note_steps":
				// Musical note particles
				if (world.getTime() % 2 == 0) {
					double offsetX = (Math.random() - 0.5) * 0.3;
					double offsetZ = (Math.random() - 0.5) * 0.3;
					client.particleManager.addParticle(ParticleTypes.NOTE, x + offsetX, y + 0.2, z + offsetZ, 0.0, 0.05, 0.0);
				}
				break;

			default:
				CosmeticsGalore.LOGGER.debug("Unknown footsteps type: {}", footstepsId);
				break;
		}
	}
}
