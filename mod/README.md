# Capes Mod - Minecraft Fabric Mod

A Minecraft 1.21.1 Fabric mod that allows players to add custom cosmetics (capes, hats, headbands, shields, swords, cloaks, and auras) that are visible to other users of the mod.

## Features

- **Custom Cosmetics**: Add various cosmetic items to your character
- **Web-Based Management**: Select cosmetics via a web interface
- **Synced Across Players**: All mod users can see each other's cosmetics
- **Multiple Cosmetic Types**:
  - Capes
  - Hats
  - Headbands
  - Shields
  - Swords
  - Cloaks
  - Auras

## Installation

1. Install Fabric Loader 0.16.5+ for Minecraft 1.21.1
2. Install Fabric API
3. Download the Capes Mod jar file
4. Place it in your `.minecraft/mods` folder
5. Launch Minecraft

## Building from Source

Requirements:
- Java 21 or higher
- Gradle (included via wrapper)

Build commands:
```bash
cd mod
./gradlew build
```

The compiled jar will be in `build/libs/`

## Configuration

After deploying the backend to Vercel, update the API URL:
- Edit `src/client/java/com/capesmod/client/CosmeticsManager.java`
- Change `API_URL` to your Vercel deployment URL

## Usage

1. Visit the web interface at your Vercel deployment URL
2. Enter your Minecraft username
3. Select your desired cosmetics
4. Click "Save Cosmetics"
5. Your cosmetics will appear in-game to all mod users

## Development Notes

The mod currently has placeholder rendering functions. To implement full cosmetic rendering:

1. Add texture files to `src/main/resources/assets/capesmod/textures/`
2. Implement the rendering logic in `CosmeticsRenderer.java`
3. Use Minecraft's rendering system to draw textures/models on players

## License

MIT License
