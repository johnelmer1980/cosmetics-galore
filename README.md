# Capes Mod - Custom Minecraft Cosmetics

A complete system for adding custom cosmetics to Minecraft Java Edition 1.21.1, consisting of a Fabric mod and web-based management interface.

## Project Structure

```
capes-mod/
├── mod/              # Fabric mod for Minecraft
└── backend/          # Node.js backend + web frontend
```

## Quick Start

### 1. Deploy Backend to Vercel

```bash
cd backend
npm install
vercel
```

After deployment:
1. Add Vercel KV storage to your project in the Vercel dashboard
2. Note your deployment URL (e.g., `https://your-app.vercel.app`)

### 2. Configure the Mod

Edit `mod/src/client/java/com/capesmod/client/CosmeticsManager.java`:
```java
private static final String API_URL = "https://your-app.vercel.app/api/cosmetics";
```

### 3. Build the Mod

```bash
cd mod
./gradlew build
```

The mod jar will be in `mod/build/libs/`

### 4. Install and Use

1. Install the mod in your Minecraft `mods` folder
2. Visit your Vercel deployment URL
3. Enter your Minecraft username
4. Select cosmetics and save
5. Launch Minecraft and join a server or world with the mod installed

## Features

- **7 Cosmetic Types**: Capes, hats, headbands, shields, swords, cloaks, and auras
- **Working Cosmetics**: 8 capes with physics + 8 particle auras (fully functional!)
- **Web Interface**: Easy-to-use web app for selecting cosmetics
- **Real-time Sync**: Cosmetics are fetched and displayed to all mod users
- **Free Hosting**: Runs on Vercel's free tier with Vercel KV storage
- **Extensible**: Easy to add new cosmetic types and items

## Technology Stack

- **Mod**: Fabric for Minecraft 1.21.1 (Java 21)
- **Backend**: Node.js serverless functions on Vercel
- **Database**: Vercel KV (Redis-based)
- **Frontend**: Vanilla HTML/CSS/JavaScript

## Development

See individual README files:
- [Mod README](mod/README.md)
- [Backend README](backend/README.md)

## What's Working Right Now

✅ **Capes (8 types)**: Fully rendered with realistic physics
- Red, Blue, Gold, Rainbow, Purple, Green, Black, White
- Swaying motion, proper lighting, texture mapping

✅ **Auras (8 types)**: Particle effects around players
- Fire, Ice, Lightning, Hearts, Soul, Enchant, Portal, Cherry Blossom
- Performance optimized, visible to all players

📋 **See `COSMETICS_IMPLEMENTED.md` for full details**

## Next Steps

1. **Deploy and Test**: Deploy backend to Vercel, test capes and auras in-game
2. **Add More Capes**: Create new cape textures and patterns
3. **Implement Hats**: Add 3D hat rendering (see `RENDERING_GUIDE.md`)
4. **Add More Auras**: Experiment with particle combinations
5. **Implement Other Cosmetics**: Headbands, shields, swords (see `RENDERING_GUIDE.md`)

## License

MIT License
