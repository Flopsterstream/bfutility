package me.flopsterstream.bfutility;


import net.fabricmc.api.ModInitializer;

import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class BFUtility implements ModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("refullbright");
	public static boolean enabled = false;

	@Override
	public void onInitialize() {


		LOGGER.info("Successfully loaded reFullbright");
	}
}