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

	public static void render(AbstractClientPlayerEntity player, MatrixStack matrices,
							  VertexConsumerProvider vertexConsumers, int light,
							  CosmeticsManager.PlayerCosmetics cosmetics, Vec3d cameraPos) {

		matrices.push();

		// Calculate camera-relative position
		double relX = player.getX() - cameraPos.x;
		double relY = player.getY() - cameraPos.y;
		double relZ = player.getZ() - cameraPos.z;

		// Translate to player position (camera-relative)
		matrices.translate(relX, relY, relZ);

		// Render cape
		if (cosmetics.hasCape()) {
			renderCape(player, matrices, vertexConsumers, light, cosmetics.cape);
		}

		// Render hat
		if (cosmetics.hasHat()) {
			renderHat(player, matrices, vertexConsumers, light, cosmetics.hat);
		}

		// Render headband
		if (cosmetics.hasHeadband()) {
			renderHeadband(player, matrices, vertexConsumers, light, cosmetics.headband);
		}

		// Render shield
		if (cosmetics.hasShield()) {
			renderShield(player, matrices, vertexConsumers, light, cosmetics.shield);
		}

		// Render sword
		if (cosmetics.hasSword()) {
			renderSword(player, matrices, vertexConsumers, light, cosmetics.sword);
		}

		// Render cloak
		if (cosmetics.hasCloak()) {
			renderCloak(player, matrices, vertexConsumers, light, cosmetics.cloak);
		}

		// Note: Aura particles are rendered via ClientTickEvents, not here

		matrices.pop();
	}

	private static void renderCape(AbstractClientPlayerEntity player, MatrixStack matrices,
								   VertexConsumerProvider vertexConsumers, int light, String capeId) {
		// Get custom cape texture
		Identifier capeTexture = Identifier.of("cosmeticsgalore", "textures/entity/capes/" + capeId + ".png");

		// Calculate cape animation based on player velocity and rotation
		float yaw = player.getYaw();
		Vec3d velocity = player.getVelocity();
		float capeFlow = (float)(velocity.lengthSquared()) * 2.0F;
		capeFlow = MathHelper.clamp(capeFlow, 0.0F, 1.0F);

		// Simple idle animation
		float capeIdle = MathHelper.sin(player.age * 0.067F) * 0.05F;

		matrices.push();

		// Rotate to face away from player
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - yaw));

		// Position cape behind player
		matrices.translate(0.0F, 0.0F, 0.125F);

		// Apply cape flow based on movement
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(6.0F + capeFlow * 10.0F + capeIdle));

		// Apply rotations using matrix stack
		MatrixStack.Entry entry = matrices.peek();

		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntitySolid(capeTexture));
		Matrix4f positionMatrix = entry.getPositionMatrix();
		Matrix3f normalMatrix = entry.getNormalMatrix();

		// Render cape geometry
		for (int part = 0; part < 16; ++part) {
			float y1 = (float) part / 16.0F;
			float y2 = (float) (part + 1) / 16.0F;
			float z1 = y1 * 0.0625F;
			float z2 = y2 * 0.0625F;

			// Front face
			vertex(vertexConsumer, positionMatrix, normalMatrix, light, -0.15625F, -y1, z1, 0.0F, y1);
			vertex(vertexConsumer, positionMatrix, normalMatrix, light, 0.15625F, -y1, z1, 0.09375F, y1);
			vertex(vertexConsumer, positionMatrix, normalMatrix, light, 0.15625F, -y2, z2, 0.09375F, y2);
			vertex(vertexConsumer, positionMatrix, normalMatrix, light, -0.15625F, -y2, z2, 0.0F, y2);

			// Back face
			vertex(vertexConsumer, positionMatrix, normalMatrix, light, 0.15625F, -y1, z1, 0.09375F, y1);
			vertex(vertexConsumer, positionMatrix, normalMatrix, light, -0.15625F, -y1, z1, 0.0F, y1);
			vertex(vertexConsumer, positionMatrix, normalMatrix, light, -0.15625F, -y2, z2, 0.0F, y2);
			vertex(vertexConsumer, positionMatrix, normalMatrix, light, 0.15625F, -y2, z2, 0.09375F, y2);
		}

		matrices.pop();
	}

	private static void vertex(VertexConsumer vertexConsumer, Matrix4f positionMatrix, Matrix3f normalMatrix,
							   int light, float x, float y, float z, float u, float v) {
		vertexConsumer.vertex(positionMatrix, x, y, z)
				.color(255, 255, 255, 255)
				.texture(u, v)
				.overlay(OverlayTexture.DEFAULT_UV)
				.light(light)
				.normal(normalMatrix.m00, normalMatrix.m01, normalMatrix.m02);
	}

	private static void renderHat(AbstractClientPlayerEntity player, MatrixStack matrices,
								  VertexConsumerProvider vertexConsumers, int light, String hatId) {
		// TODO: Implement hat rendering
		// This will render a 3D model or texture on the player's head
		CosmeticsGalore.LOGGER.debug("Rendering hat: {} for player {}", hatId, player.getName().getString());
	}

	private static void renderHeadband(AbstractClientPlayerEntity player, MatrixStack matrices,
									   VertexConsumerProvider vertexConsumers, int light, String headbandId) {
		// TODO: Implement headband rendering
		CosmeticsGalore.LOGGER.debug("Rendering headband: {} for player {}", headbandId, player.getName().getString());
	}

	private static void renderShield(AbstractClientPlayerEntity player, MatrixStack matrices,
									 VertexConsumerProvider vertexConsumers, int light, String shieldId) {
		// TODO: Implement shield rendering (on back or side)
		CosmeticsGalore.LOGGER.debug("Rendering shield: {} for player {}", shieldId, player.getName().getString());
	}

	private static void renderSword(AbstractClientPlayerEntity player, MatrixStack matrices,
									VertexConsumerProvider vertexConsumers, int light, String swordId) {
		// TODO: Implement sword rendering (sheathed on hip)
		CosmeticsGalore.LOGGER.debug("Rendering sword: {} for player {}", swordId, player.getName().getString());
	}

	private static void renderCloak(AbstractClientPlayerEntity player, MatrixStack matrices,
									VertexConsumerProvider vertexConsumers, int light, String cloakId) {
		// TODO: Implement cloak rendering (similar to cape but different style)
		CosmeticsGalore.LOGGER.debug("Rendering cloak: {} for player {}", cloakId, player.getName().getString());
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
}
