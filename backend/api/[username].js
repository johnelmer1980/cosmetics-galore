import { createClient } from 'redis';

let client;

async function getRedisClient() {
  if (!client) {
    client = createClient({
      url: process.env.REDIS_URL
    });
    client.on('error', (err) => console.log('Redis Client Error', err));
    await client.connect();
  }
  return client;
}

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
    // Fetch cosmetics from Redis
    const redis = await getRedisClient();
    const data = await redis.get(`cosmetics:${username.toLowerCase()}`);

    if (!data) {
      return res.status(404).json({
        error: 'No cosmetics found for this user',
        username: username
      });
    }

    const cosmetics = JSON.parse(data);

    return res.status(200).json({
      username: username,
      ...cosmetics
    });
  } catch (error) {
    console.error('Error fetching cosmetics:', error);
    return res.status(500).json({ error: 'Internal server error' });
  }
}
