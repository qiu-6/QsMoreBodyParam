package net.qiu.morebodyparam;

import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.util.Identifier;
import net.qiu.morebodyparam.command.bleedCommand;
import net.qiu.morebodyparam.command.bloodLevelCommand;
import net.qiu.morebodyparam.command.thirstLevelCommand;
import net.qiu.morebodyparam.config.modConfig;
import net.qiu.morebodyparam.network.modNetwork;
import net.qiu.morebodyparam.particle.modParticles;
import net.qiu.morebodyparam.util.modEnumExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QsMoreBodyParameters implements ModInitializer {
	public static final String MOD_ID = "qmorebodyparam";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

		LOGGER.info("Registering  Q's more body parameters.");

		modNetwork.registerS2CPackets();

		MidnightConfig.init(MOD_ID, modConfig.class);

		modEnumExtension.register();

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			bloodLevelCommand.register(dispatcher);
			thirstLevelCommand.register(dispatcher);
			bleedCommand.register(dispatcher);
		});


		modParticles.registerParticles();
	}
}