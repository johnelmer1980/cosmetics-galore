# Capes Mod Backend

Backend API for the Capes Mod Minecraft cosmetics system.

## Setup

1. Install dependencies:
```bash
npm install
```

2. Set up Vercel KV:
   - Create a Vercel account at https://vercel.com
   - Create a new project
   - Add Vercel KV storage to your project
   - The environment variables will be automatically configured

3. Deploy to Vercel:
```bash
npm install -g vercel
vercel
```

4. Update the API URL in the mod:
   - Edit `mod/src/client/java/com/capesmod/client/CosmeticsManager.java`
   - Replace `https://your-vercel-app.vercel.app` with your actual Vercel deployment URL

## API Endpoints

### GET /api/cosmetics/[username]
Get cosmetics for a specific username.

### POST /api/cosmetics/update
Update cosmetics for a username.
Body: `{ username, cape, hat, headband, shield, sword, cloak, aura }`

### GET /api/cosmetics/list
Get all available cosmetic items.

## Local Development

Run locally with Vercel CLI:
```bash
vercel dev
```

## Environment Variables

The following are automatically configured when you add Vercel KV:
- `KV_REST_API_URL`
- `KV_REST_API_TOKEN`
- `KV_REST_API_READ_ONLY_TOKEN`
