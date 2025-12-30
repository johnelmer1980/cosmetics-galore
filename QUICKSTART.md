# Quick Start Guide

Get your Capes Mod up and running in 10 minutes!

## Prerequisites

- Java 21+
- Node.js 18+
- Vercel account (free)
- Minecraft 1.21.1 with Fabric Loader

## 5-Step Setup

### 1. Deploy Backend (2 minutes)

```bash
cd backend
npm install
npx vercel
```

Follow prompts, then add Vercel KV:
- Go to vercel.com → your project → Storage → Create Database → KV
- Run `npx vercel --prod` to redeploy

**Save your URL**: `https://your-project.vercel.app`

### 2. Configure Mod (1 minute)

Edit `mod/src/client/java/com/capesmod/client/CosmeticsManager.java`:

```java
private static final String API_URL = "https://your-project.vercel.app/api/cosmetics";
```

### 3. Build Mod (2 minutes)

```bash
cd mod
./gradlew build
```

Output: `mod/build/libs/capes-mod-1.0.0.jar`

### 4. Install Mod (2 minutes)

1. Install Fabric Loader 0.16.5+ for Minecraft 1.21.1
2. Install Fabric API mod
3. Copy `capes-mod-1.0.0.jar` to `.minecraft/mods/`

### 5. Test (3 minutes)

1. Visit `https://your-project.vercel.app`
2. Enter your Minecraft username
3. Select cosmetics → Save
4. Launch Minecraft 1.21.1
5. Check logs for "Capes Mod initialized!"

## Verify It Works

**Backend Test**:
```bash
curl https://your-project.vercel.app/api/cosmetics/YourUsername
```

Should return JSON with your cosmetics.

**In-Game Test**:
- Check Minecraft logs for "Loaded cosmetics for YourUsername"
- Currently shows debug logs (rendering needs implementation)

## What's Next?

The mod is functional but cosmetics rendering is placeholder. To make cosmetics visible:

1. **Add Textures**: Create texture files in `mod/src/main/resources/assets/capesmod/textures/`
2. **Implement Rendering**: Complete the TODO methods in `CosmeticsRenderer.java`
3. **Test**: Rebuild and see your cosmetics in-game!

See full implementation guide in the main README.

## Common Issues

**"Failed to fetch cosmetics"**
→ Check API_URL in CosmeticsManager.java matches your Vercel URL

**Mod won't load**
→ Verify Fabric Loader 0.16.5+ and Fabric API are installed

**Website shows errors**
→ Ensure Vercel KV database is connected

## Need Help?

Check these files:
- `DEPLOYMENT.md` - Detailed deployment steps
- `README.md` - Full project documentation
- `mod/README.md` - Mod-specific info
- `backend/README.md` - Backend API details
