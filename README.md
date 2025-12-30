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

## Next Steps

1. **Add Textures**: Create texture files for each cosmetic item
2. **Implement Rendering**: Complete the rendering logic in `CosmeticsRenderer.java`
3. **Add 3D Models**: Use Minecraft's model system for items like hats and shields
4. **Particle Effects**: Implement particle effects for auras
5. **Add More Cosmetics**: Expand the cosmetics list in `backend/api/cosmetics/list.js`

## License

MIT License
