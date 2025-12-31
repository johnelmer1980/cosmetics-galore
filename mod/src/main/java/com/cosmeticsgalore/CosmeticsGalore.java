package com.cosmeticsgalore;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CosmeticsGalore implements ModInitializer {
	public static final String MOD_ID = "cosmeticsgalore";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Cosmetics Galore initialized!");
	}
}
