// List all available cosmetic items
export default async function handler(req, res) {
  // Enable CORS
  res.setHeader('Access-Control-Allow-Origin', '*');
  res.setHeader('Access-Control-Allow-Methods', 'GET, OPTIONS');
  res.setHeader('Access-Control-Allow-Headers', 'Content-Type');

  if (req.method === 'OPTIONS') {
    return res.status(200).end();
  }

  // Return available cosmetics
  const availableCosmetics = {
    capes: [
      { id: 'red_cape', name: 'Red Cape', description: 'A vibrant red cape' },
      { id: 'blue_cape', name: 'Blue Cape', description: 'A cool blue cape' },
      { id: 'gold_cape', name: 'Gold Cape', description: 'A luxurious gold cape' },
      { id: 'rainbow_cape', name: 'Rainbow Cape', description: 'A colorful rainbow cape' }
    ],
    hats: [
      { id: 'top_hat', name: 'Top Hat', description: 'A classy top hat' },
      { id: 'wizard_hat', name: 'Wizard Hat', description: 'A magical wizard hat' },
      { id: 'crown', name: 'Crown', description: 'A royal crown' },
      { id: 'halo', name: 'Halo', description: 'An angelic halo' }
    ],
    headbands: [
      { id: 'ninja_headband', name: 'Ninja Headband', description: 'A stealthy ninja headband' },
      { id: 'flower_headband', name: 'Flower Headband', description: 'A beautiful flower headband' }
    ],
    shields: [
      { id: 'wooden_shield', name: 'Wooden Shield', description: 'A basic wooden shield' },
      { id: 'iron_shield', name: 'Iron Shield', description: 'A sturdy iron shield' },
      { id: 'diamond_shield', name: 'Diamond Shield', description: 'A rare diamond shield' }
    ],
    swords: [
      { id: 'katana', name: 'Katana', description: 'A sharp katana' },
      { id: 'excalibur', name: 'Excalibur', description: 'The legendary Excalibur' },
      { id: 'lightsaber', name: 'Lightsaber', description: 'An energy blade' }
    ],
    cloaks: [
      { id: 'shadow_cloak', name: 'Shadow Cloak', description: 'A dark shadow cloak' },
      { id: 'invisibility_cloak', name: 'Invisibility Cloak', description: 'A partially transparent cloak' }
    ],
    auras: [
      { id: 'fire_aura', name: 'Fire Aura', description: 'Flames surrounding you' },
      { id: 'ice_aura', name: 'Ice Aura', description: 'Frost particles around you' },
      { id: 'lightning_aura', name: 'Lightning Aura', description: 'Electric sparks' },
      { id: 'hearts_aura', name: 'Hearts Aura', description: 'Floating hearts' }
    ]
  };

  return res.status(200).json(availableCosmetics);
}
