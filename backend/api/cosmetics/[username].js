import { kv } from '@vercel/kv';

export default async function handler(req, res) {
  // Enable CORS
  res.setHeader('Access-Control-Allow-Origin', '*');
  res.setHeader('Access-Control-Allow-Methods', 'GET, OPTIONS');
  res.setHeader('Access-Control-Allow-Headers', 'Content-Type');

  if (req.method === 'OPTIONS') {
    return res.status(200).end();
  }

  const { username } = req.query;

  if (!username) {
    return res.status(400).json({ error: 'Username is required' });
  }

  try {
    // Fetch cosmetics from Vercel KV
    const cosmetics = await kv.get(`cosmetics:${username.toLowerCase()}`);

    if (!cosmetics) {
      return res.status(404).json({
        error: 'No cosmetics found for this user',
        username: username
      });
    }

    return res.status(200).json({
      username: username,
      ...cosmetics
    });
  } catch (error) {
    console.error('Error fetching cosmetics:', error);
    return res.status(500).json({ error: 'Internal server error' });
  }
}
