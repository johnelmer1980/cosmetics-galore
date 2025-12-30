# Deployment Guide

## Step 1: Deploy Backend to Vercel

### Prerequisites
- Vercel account (free tier)
- Node.js installed locally

### Deployment Steps

1. **Install Vercel CLI**:
   ```bash
   npm install -g vercel
   ```

2. **Navigate to backend folder**:
   ```bash
   cd backend
   npm install
   ```

3. **Deploy to Vercel**:
   ```bash
   vercel
   ```
   - Follow the prompts
   - Choose a project name (e.g., `capes-mod-api`)
   - Deploy to production

4. **Add Vercel KV Storage**:
   - Go to your project dashboard on vercel.com
   - Navigate to Storage tab
   - Click "Create Database"
   - Select "KV" (Redis-based key-value store)
   - Choose a name and create
   - Environment variables are automatically added

5. **Redeploy** (to pick up the KV environment variables):
   ```bash
   vercel --prod
   ```

6. **Note your deployment URL**:
   - It will be something like: `https://capes-mod-api.vercel.app`

## Step 2: Configure the Mod

1. **Update API URL in the mod**:
   - Open `mod/src/client/java/com/capesmod/client/CosmeticsManager.java`
   - Find this line:
     ```java
     private static final String API_URL = "https://your-vercel-app.vercel.app/api/cosmetics";
     ```
   - Replace with your actual Vercel URL:
     ```java
     private static final String API_URL = "https://capes-mod-api.vercel.app/api/cosmetics";
     ```

## Step 3: Build the Mod

1. **Navigate to mod folder**:
   ```bash
   cd mod
   ```

2. **Build with Gradle**:
   ```bash
   ./gradlew build
   ```
   - On Windows: `gradlew.bat build`

3. **Find your mod jar**:
   - Location: `mod/build/libs/capes-mod-1.0.0.jar`

## Step 4: Test Everything

1. **Test the web interface**:
   - Visit your Vercel URL (e.g., `https://capes-mod-api.vercel.app`)
   - Enter a Minecraft username
   - Select some cosmetics
   - Click "Save Cosmetics"
   - Verify success message appears

2. **Test the API**:
   - Open: `https://capes-mod-api.vercel.app/api/cosmetics/YourUsername`
   - You should see JSON with your cosmetics

3. **Install and test the mod**:
   - Copy `capes-mod-1.0.0.jar` to your `.minecraft/mods` folder
   - Make sure you have Fabric Loader 0.16.5+ and Fabric API installed
   - Launch Minecraft 1.21.1
   - Check logs for "Capes Mod initialized!" message

## Step 5: Distribution

### Sharing with Others

1. **Share the mod jar**:
   - Distribute `capes-mod-1.0.0.jar` to users
   - They need Fabric Loader and Fabric API installed

2. **Share the website**:
   - Give users your Vercel URL
   - They can select cosmetics for their username

3. **Optional: Custom Domain**:
   - In Vercel dashboard, go to Settings > Domains
   - Add a custom domain (e.g., `capesmod.yourdomain.com`)
   - Update the API URL in the mod if you use a custom domain

## Troubleshooting

### Backend Issues

**Problem**: API returns 500 errors
- **Solution**: Check Vercel logs in dashboard
- Ensure Vercel KV is properly connected
- Verify environment variables are set

**Problem**: CORS errors in browser
- **Solution**: CORS headers are already set in the API
- If still having issues, check browser console

### Mod Issues

**Problem**: Mod doesn't load
- **Solution**:
  - Verify Fabric Loader version is 0.16.5+
  - Check Minecraft version is 1.21.1
  - Ensure Fabric API is installed
  - Check logs for errors

**Problem**: Cosmetics don't appear
- **Solution**:
  - Check mod logs for "Loaded cosmetics for [username]"
  - Verify API URL is correct in CosmeticsManager.java
  - Test API endpoint in browser
  - Rendering is currently placeholder - you need to implement actual rendering

**Problem**: "Failed to fetch cosmetics" in logs
- **Solution**:
  - Verify internet connection
  - Check API URL is accessible
  - Look for firewall/antivirus blocking

## Updating

### Update Backend
```bash
cd backend
vercel --prod
```

### Update Mod
1. Make changes
2. Rebuild: `./gradlew build`
3. Replace jar in mods folder
4. Restart Minecraft

## Cost Estimate

**Vercel Free Tier Limits**:
- 100 GB bandwidth/month
- 100,000 serverless function invocations/month
- Vercel KV: 256 MB storage, 60 hours compute/month

For a small/medium player base, this should be completely free!

## Next Steps

1. Implement actual cosmetic rendering (textures, models, particles)
2. Add more cosmetic items
3. Consider adding authentication/verification
4. Add admin panel for managing available cosmetics
5. Implement caching in the mod to reduce API calls
