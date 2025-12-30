# Cosmetics Rendering Implementation Guide

This guide explains how to implement the actual rendering of cosmetics in the mod.

## Current State

The mod infrastructure is complete but rendering is placeholder (debug logs only). You need to implement the actual texture/model rendering.

## Where to Start

All rendering happens in: `mod/src/client/java/com/capesmod/client/CosmeticsRenderer.java`

## Implementation Approaches

### Approach 1: Texture Overlays (Easiest - Capes, Headbands)

For flat cosmetics like capes and headbands, render textures on the player model.

**Example: Cape Rendering**

```java
private static void renderCape(AbstractClientPlayerEntity player, MatrixStack matrices,
                               VertexConsumerProvider vertexConsumers, int light, String capeId) {
    // 1. Get texture identifier
    Identifier texture = Identifier.of("capesmod", "textures/capes/" + capeId + ".png");

    // 2. Get vertex consumer
    VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(texture));

    // 3. Position and render
    matrices.push();
    matrices.translate(0.0, 0.0, 0.125); // Position behind player

    // Render quad for cape
    MatrixStack.Entry entry = matrices.peek();
    Matrix4f positionMatrix = entry.getPositionMatrix();
    Matrix3f normalMatrix = entry.getNormalMatrix();

    // Define cape vertices (simplified)
    float width = 0.5f;
    float height = 1.0f;

    // Front face
    vertexConsumer.vertex(positionMatrix, -width, 0, 0).color(255, 255, 255, 255)
        .texture(0, 0).overlay(OverlayTexture.DEFAULT_UV).light(light)
        .normal(normalMatrix, 0, 0, 1).next();
    // ... add other vertices

    matrices.pop();
}
```

**Textures to create**:
- Place in: `mod/src/main/resources/assets/capesmod/textures/capes/`
- Files: `red_cape.png`, `blue_cape.png`, etc.
- Size: 64x32 pixels (standard cape texture)

### Approach 2: 3D Models (Advanced - Hats, Shields, Swords)

For 3D items, use Minecraft's model system.

**Example: Hat Rendering**

```java
private static void renderHat(AbstractClientPlayerEntity player, MatrixStack matrices,
                              VertexConsumerProvider vertexConsumers, int light, String hatId) {
    // 1. Load model
    MinecraftClient client = MinecraftClient.getInstance();
    BakedModel model = client.getBakedModelManager()
        .getModel(Identifier.of("capesmod", "item/" + hatId));

    // 2. Position on head
    matrices.push();
    matrices.translate(0.0, 1.8, 0.0); // Move to head position
    matrices.scale(0.5f, 0.5f, 0.5f); // Scale down

    // 3. Render model
    client.getItemRenderer().renderItem(
        new ItemStack(Items.DIAMOND_HELMET), // Placeholder, use custom model
        ModelTransformationMode.HEAD,
        light,
        OverlayTexture.DEFAULT_UV,
        matrices,
        vertexConsumers,
        player.getWorld(),
        0
    );

    matrices.pop();
}
```

**Models to create**:
- Place in: `mod/src/main/resources/assets/capesmod/models/item/`
- Files: `top_hat.json`, `wizard_hat.json`, etc.
- Format: Standard Minecraft JSON model format

### Approach 3: Particle Effects (Auras)

For particle-based cosmetics like auras.

**Example: Aura Rendering**

```java
private static void renderAura(AbstractClientPlayerEntity player, MatrixStack matrices,
                               VertexConsumerProvider vertexConsumers, int light, String auraId) {
    World world = player.getWorld();

    // Spawn particles around player
    if (world.getTime() % 5 == 0) { // Every 5 ticks
        double x = player.getX();
        double y = player.getY() + 1.0;
        double z = player.getZ();

        switch (auraId) {
            case "fire_aura":
                for (int i = 0; i < 3; i++) {
                    double offsetX = (Math.random() - 0.5) * 0.5;
                    double offsetZ = (Math.random() - 0.5) * 0.5;
                    world.addParticle(
                        ParticleTypes.FLAME,
                        x + offsetX, y, z + offsetZ,
                        0, 0.05, 0 // Velocity
                    );
                }
                break;

            case "ice_aura":
                for (int i = 0; i < 2; i++) {
                    double offsetX = (Math.random() - 0.5) * 0.5;
                    double offsetZ = (Math.random() - 0.5) * 0.5;
                    world.addParticle(
                        ParticleTypes.SNOWFLAKE,
                        x + offsetX, y, z + offsetZ,
                        0, -0.05, 0
                    );
                }
                break;

            // Add other aura types...
        }
    }
}
```

## Step-by-Step Implementation

### Step 1: Create Texture Files

1. Create cape textures (64x32 PNG):
   ```
   mod/src/main/resources/assets/capesmod/textures/capes/
   ├── red_cape.png
   ├── blue_cape.png
   ├── gold_cape.png
   └── rainbow_cape.png
   ```

2. Use image editing software (GIMP, Photoshop, Paint.NET)
3. Design simple colored capes as a starting point

### Step 2: Implement Cape Rendering

1. Open `CosmeticsRenderer.java`
2. Implement the `renderCape()` method using Approach 1 above
3. Test with `./gradlew build` and install in Minecraft

### Step 3: Add Particle Auras

1. Implement `renderAura()` using Approach 3
2. Test in-game - should see particles immediately
3. Adjust particle count/speed/type as needed

### Step 4: Advanced - 3D Models

1. Create JSON model files for hats
2. Create texture files for models
3. Implement `renderHat()` using Approach 2
4. Test and iterate

## Testing Tips

1. **Enable Debug Logging**:
   - Current code already has debug logs
   - Watch console for "Rendering X for player Y"

2. **Test with Single Player**:
   - Easier to debug than multiplayer
   - Use F5 (third-person) to see your own cosmetics

3. **Start Simple**:
   - Get capes working first (easiest)
   - Then auras (no textures needed)
   - Finally hats/shields/swords (most complex)

4. **Hot Reload**:
   - Some changes can be tested without full rebuild
   - Use `/reload` command in-game for texture changes

## Common Issues & Solutions

### Textures Not Loading
```
Solution: Check texture path matches identifier
- File: mod/src/main/resources/assets/capesmod/textures/capes/red_cape.png
- Code: Identifier.of("capesmod", "textures/capes/red_cape.png")
```

### Cosmetics Render in Wrong Position
```
Solution: Adjust matrices.translate() values
- X: left/right
- Y: up/down
- Z: forward/back
```

### Performance Issues
```
Solution:
- Cache textures/models, don't reload every frame
- Limit particle count
- Use LOD (level of detail) for distant players
```

## Resources

- **Fabric Wiki**: https://fabricmc.net/wiki/tutorial:rendering
- **Minecraft Rendering**: Check vanilla player renderer code
- **Texture Format**: Standard Minecraft texture format
- **Model Format**: Minecraft JSON model specification

## Example: Complete Cape Implementation

Here's a minimal working cape renderer:

```java
private static void renderCape(AbstractClientPlayerEntity player, MatrixStack matrices,
                               VertexConsumerProvider vertexConsumers, int light, String capeId) {
    Identifier texture = Identifier.of("capesmod", "textures/capes/" + capeId + ".png");

    matrices.push();
    matrices.translate(0.0, 0.0, 0.125);

    // Simple cape rendering - you can expand this
    // For now, just log that it would render
    CapesMod.LOGGER.info("Would render cape texture: {}", texture);

    // TODO: Implement actual rendering with vertices

    matrices.pop();
}
```

Replace the TODO with actual vertex rendering once you have textures!

## Next Steps After Rendering

1. **Animations**: Add flowing cape physics, rotating hats
2. **Combinations**: Ensure cosmetics don't conflict visually
3. **Options**: Allow players to toggle cosmetics visibility
4. **Performance**: Optimize for many players with cosmetics
