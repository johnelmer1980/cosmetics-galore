# Capes Mod - Architecture Overview

## System Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                         USER INTERACTION                             │
└─────────────────────────────────────────────────────────────────────┘

                             ┌──────────────┐
                             │   Player     │
                             │  (Browser)   │
                             └──────┬───────┘
                                    │
                                    │ 1. Visit website
                                    │ 2. Select cosmetics
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│                        WEB FRONTEND                                  │
│  ┌────────────────────────────────────────────────────────────┐    │
│  │  index.html (Vercel Static)                                │    │
│  │  - Username input                                          │    │
│  │  - Cosmetics selection UI                                  │    │
│  │  - Save button                                             │    │
│  └────────────────────────────────────────────────────────────┘    │
└─────────────────────────────────────┬───────────────────────────────┘
                                      │
                                      │ HTTP POST/GET
                                      │
                                      ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    VERCEL BACKEND (API)                              │
│  ┌────────────────────────────────────────────────────────────┐    │
│  │  Serverless Functions                                      │    │
│  │  ┌──────────────────────────────────────────────────────┐  │    │
│  │  │ GET /api/cosmetics/[username]                        │  │    │
│  │  │  → Fetch user's cosmetics                            │  │    │
│  │  └──────────────────────────────────────────────────────┘  │    │
│  │  ┌──────────────────────────────────────────────────────┐  │    │
│  │  │ POST /api/cosmetics/update                           │  │    │
│  │  │  → Save user's cosmetics                             │  │    │
│  │  └──────────────────────────────────────────────────────┘  │    │
│  │  ┌──────────────────────────────────────────────────────┐  │    │
│  │  │ GET /api/cosmetics/list                              │  │    │
│  │  │  → Get available cosmetic items                      │  │    │
│  │  └──────────────────────────────────────────────────────┘  │    │
│  └────────────────────────────────────────────────────────────┘    │
└─────────────────────────────────────┬───────────────────────────────┘
                                      │
                                      │ Read/Write
                                      │
                                      ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    VERCEL KV (DATABASE)                              │
│  ┌────────────────────────────────────────────────────────────┐    │
│  │  Redis-based Key-Value Store                              │    │
│  │                                                            │    │
│  │  cosmetics:player1 → { cape: "red", hat: "crown", ... }   │    │
│  │  cosmetics:player2 → { cape: "blue", aura: "fire", ... }  │    │
│  │  cosmetics:player3 → { ... }                              │    │
│  └────────────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────────────┘

                                      ▲
                                      │
                                      │ HTTP GET (fetch cosmetics)
                                      │
┌─────────────────────────────────────────────────────────────────────┐
│                   MINECRAFT CLIENT (MOD)                             │
│  ┌────────────────────────────────────────────────────────────┐    │
│  │  CapesModClient                                            │    │
│  │  - Initializes on client start                            │    │
│  │  - Periodic refresh (every 5 minutes)                     │    │
│  └────────────────────────────────────────────────────────────┘    │
│                                                                      │
│  ┌────────────────────────────────────────────────────────────┐    │
│  │  CosmeticsManager                                          │    │
│  │  - HTTP client                                             │    │
│  │  - Fetches cosmetics from API                             │    │
│  │  - Caches cosmetics per UUID                              │    │
│  └────────────────────────────────────────────────────────────┘    │
│                                                                      │
│  ┌────────────────────────────────────────────────────────────┐    │
│  │  PlayerEntityRendererMixin                                 │    │
│  │  - Hooks into player rendering                            │    │
│  │  - Triggers cosmetic fetch if needed                      │    │
│  │  - Calls CosmeticsRenderer                                │    │
│  └────────────────────────────────────────────────────────────┘    │
│                                                                      │
│  ┌────────────────────────────────────────────────────────────┐    │
│  │  CosmeticsRenderer                                         │    │
│  │  - Renders capes (TODO)                                    │    │
│  │  - Renders hats (TODO)                                     │    │
│  │  - Renders shields, swords, cloaks (TODO)                 │    │
│  │  - Renders auras with particles (TODO)                    │    │
│  └────────────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────────────┘

```

## Data Flow

### Flow 1: User Selects Cosmetics

```
Player → Website → API (POST /update) → Vercel KV
                                          ↓
                                    Save cosmetics
                                    { username: "Player",
                                      cape: "red_cape",
                                      hat: "crown",
                                      aura: "fire" }
```

### Flow 2: Cosmetics Display In-Game

```
1. Player joins game with mod installed
   ↓
2. PlayerEntityRendererMixin detects player
   ↓
3. CosmeticsManager checks cache
   ├─ If cached: use cached data
   └─ If not cached: fetch from API
       ↓
       HTTP GET /api/cosmetics/[username]
       ↓
       Vercel KV returns cosmetics
       ↓
       Cache cosmetics locally
   ↓
4. CosmeticsRenderer receives cosmetics
   ↓
5. Render each cosmetic on player
   - Textures for capes/headbands
   - Models for hats/shields/swords
   - Particles for auras
```

## Component Responsibilities

### Backend Components

| Component | Responsibility | File |
|-----------|---------------|------|
| **Web UI** | User interface for cosmetic selection | `backend/public/index.html` |
| **Get Cosmetics API** | Fetch user's saved cosmetics | `backend/api/cosmetics/[username].js` |
| **Update API** | Save user's cosmetic choices | `backend/api/cosmetics/update.js` |
| **List API** | Provide available cosmetic items | `backend/api/cosmetics/list.js` |
| **Vercel KV** | Persistent storage | Managed by Vercel |

### Mod Components

| Component | Responsibility | File |
|-----------|---------------|------|
| **CapesMod** | Main mod initialization | `CapesMod.java` |
| **CapesModClient** | Client-side initialization | `CapesModClient.java` |
| **CosmeticsManager** | API communication & caching | `CosmeticsManager.java` |
| **PlayerEntityRendererMixin** | Inject into player rendering | `PlayerEntityRendererMixin.java` |
| **CosmeticsRenderer** | Render cosmetics on players | `CosmeticsRenderer.java` |

## Technology Stack Details

```
Frontend (Browser)
├── HTML5
├── CSS3 (Grid layout, animations)
└── Vanilla JavaScript (Fetch API)

Backend (Vercel)
├── Node.js 18+
├── Vercel Serverless Functions
└── Vercel KV (Redis)

Mod (Minecraft)
├── Java 21
├── Fabric Loader 0.16.5
├── Fabric API
├── Gson (JSON parsing)
└── Java HTTP Client
```

## Network Protocol

### Request: Save Cosmetics
```http
POST /api/cosmetics/update HTTP/1.1
Host: your-app.vercel.app
Content-Type: application/json

{
  "username": "Steve",
  "cape": "red_cape",
  "hat": "crown",
  "headband": null,
  "shield": "diamond_shield",
  "sword": null,
  "cloak": null,
  "aura": "fire_aura"
}
```

### Response: Save Cosmetics
```http
HTTP/1.1 200 OK
Content-Type: application/json

{
  "success": true,
  "username": "Steve",
  "cosmetics": {
    "cape": "red_cape",
    "hat": "crown",
    ...
  }
}
```

### Request: Get Cosmetics
```http
GET /api/cosmetics/Steve HTTP/1.1
Host: your-app.vercel.app
```

### Response: Get Cosmetics
```http
HTTP/1.1 200 OK
Content-Type: application/json

{
  "username": "Steve",
  "cape": "red_cape",
  "hat": "crown",
  "headband": null,
  "shield": "diamond_shield",
  "sword": null,
  "cloak": null,
  "aura": "fire_aura"
}
```

## Performance Considerations

### Caching Strategy
```
Client-side cache (Mod):
- Cache cosmetics per player UUID
- TTL: Until client restart or manual refresh
- Reduces API calls from thousands to ~10 per session

Server-side:
- Vercel KV provides fast Redis-based storage
- Sub-millisecond read latency
- No additional caching needed
```

### Scaling Estimates

**With Vercel Free Tier**:
- 100,000 API calls/month
- If 100 players play 10 hours/month
- ~10 API calls per player per session
- Total: ~100 calls per player/month = 10,000 calls
- **Headroom**: 10x capacity

**Storage**:
- Each user: ~200 bytes
- 256 MB free tier = ~1.3 million users
- **Plenty of capacity**

## Security Model

**Current**: Trust-based
- No authentication required
- Anyone can set cosmetics for any username
- Suitable for small communities

**Future Enhancements**:
- Mojang API integration for username verification
- Session-based authentication
- Rate limiting per IP
- CAPTCHA for abuse prevention

## Deployment Architecture

```
Developer Machine
    ↓
    git push / vercel deploy
    ↓
GitHub (optional)
    ↓
Vercel Build System
    ↓
    ├─→ Build Static Files (public/)
    └─→ Deploy Serverless Functions (api/)
    ↓
Vercel CDN (Global)
    ├─→ Edge Network (static content)
    └─→ Serverless Functions (dynamic API)
    ↓
Vercel KV (Database)

Players download mod.jar
    ↓
Install in .minecraft/mods/
    ↓
Mod fetches from Vercel CDN
```

## Future Architecture Enhancements

1. **CDN for Textures**: Host cosmetic textures on CDN, download dynamically
2. **Real-time Sync**: WebSocket for instant cosmetic updates
3. **Mod Marketplace**: Allow users to upload custom cosmetics
4. **Analytics**: Track popular cosmetics, usage patterns
5. **Premium Features**: Subscription system for exclusive cosmetics
