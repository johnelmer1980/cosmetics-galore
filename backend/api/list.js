// List all available cosmetic items
export default async function handler(req, res) {
  // Enable CORS
  res.setHeader('Access-Control-Allow-Origin', '*');
  res.setHeader('Access-Control-Allow-Methods', 'GET, OPTIONS');
  res.setHeader('Access-Control-Allow-Headers', 'Content-Type');

  if (req.method === 'OPTIONS') {
    return res.status(200).end();
  }

  // Return available cosmetics (particle-based only for Minecraft 1.21.10)
  const availableCosmetics = {
    auras: [
      { id: 'fire_aura', name: 'Fire Aura', description: 'Flames surrounding you' },
      { id: 'ice_aura', name: 'Ice Aura', description: 'Snowflakes falling around you' },
      { id: 'lightning_aura', name: 'Lightning Aura', description: 'Electric sparks crackling' },
      { id: 'hearts_aura', name: 'Hearts Aura', description: 'Floating hearts' },
      { id: 'soul_aura', name: 'Soul Aura', description: 'Blue soul fire flames' },
      { id: 'enchant_aura', name: 'Enchant Aura', description: 'Magical enchantment particles' },
      { id: 'portal_aura', name: 'Portal Aura', description: 'Swirling portal particles' },
      { id: 'cherry_aura', name: 'Cherry Blossom Aura', description: 'Falling cherry blossom petals' }
    ],
    wings: [
      { id: 'angel_wings', name: 'Angel Wings', description: 'White feather particle wings' },
      { id: 'demon_wings', name: 'Demon Wings', description: 'Dark smoke particle wings' },
      { id: 'dragon_wings', name: 'Dragon Wings', description: 'Fiery dragon wings' },
      { id: 'butterfly_wings', name: 'Butterfly Wings', description: 'Colorful butterfly effect' },
      { id: 'fairy_wings', name: 'Fairy Wings', description: 'Sparkly magical wings' }
    ],
    trails: [
      { id: 'rainbow_trail', name: 'Rainbow Trail', description: 'Rainbow particles behind you' },
      { id: 'star_trail', name: 'Star Trail', description: 'Twinkling stars follow you' },
      { id: 'smoke_trail', name: 'Smoke Trail', description: 'Mysterious smoke trail' },
      { id: 'redstone_trail', name: 'Redstone Trail', description: 'Redstone dust particles' },
      { id: 'slime_trail', name: 'Slime Trail', description: 'Green slime particles' }
    ],
    crowns: [
      { id: 'golden_crown', name: 'Golden Crown', description: 'Golden sparkle particles above head' },
      { id: 'diamond_crown', name: 'Diamond Crown', description: 'Diamond sparkles' },
      { id: 'star_crown', name: 'Star Crown', description: 'Rotating stars above head' },
      { id: 'halo', name: 'Halo', description: 'Angelic white particle ring' }
    ],
    footsteps: [
      { id: 'fire_steps', name: 'Fire Steps', description: 'Leave flames where you walk' },
      { id: 'flower_steps', name: 'Flower Steps', description: 'Flowers bloom with each step' },
      { id: 'water_steps', name: 'Water Steps', description: 'Water splashes' },
      { id: 'note_steps', name: 'Note Steps', description: 'Musical notes appear' }
    ]
  };

  return res.status(200).json(availableCosmetics);
}
