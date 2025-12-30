import { kv } from '@vercel/kv';

export default async function handler(req, res) {
  // Enable CORS
  res.setHeader('Access-Control-Allow-Origin', '*');
  res.setHeader('Access-Control-Allow-Methods', 'POST, OPTIONS');
  res.setHeader('Access-Control-Allow-Headers', 'Content-Type');

  if (req.method === 'OPTIONS') {
    return res.status(200).end();
  }

  if (req.method !== 'POST') {
    return res.status(405).json({ error: 'Method not allowed' });
  }

  const { username, cape, hat, headband, shield, sword, cloak, aura } = req.body;

  if (!username) {
    return res.status(400).json({ error: 'Username is required' });
  }

  // Validate cosmetic IDs (optional - you can add more validation)
  const cosmetics = {
    cape: cape || null,
    hat: hat || null,
    headband: headband || null,
    shield: shield || null,
    sword: sword || null,
    cloak: cloak || null,
    aura: aura || null
  };

  try {
    // Store cosmetics in Vercel KV
    await kv.set(`cosmetics:${username.toLowerCase()}`, cosmetics);

    return res.status(200).json({
      success: true,
      username: username,
      cosmetics: cosmetics
    });
  } catch (error) {
    console.error('Error updating cosmetics:', error);
    return res.status(500).json({ error: 'Internal server error' });
  }
}
