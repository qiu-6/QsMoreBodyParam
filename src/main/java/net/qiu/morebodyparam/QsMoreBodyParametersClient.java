package net.qiu.morebodyparam;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.qiu.morebodyparam.network.modNetwork;
import net.qiu.morebodyparam.particle.bleedParticle;
import net.qiu.morebodyparam.particle.bleedParticlesInWater;
import net.qiu.morebodyparam.particle.modParticles;

public class QsMoreBodyParametersClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        QsMoreBodyParameters.LOGGER.info("Registering Q's more body parameters at client side.");
        modNetwork.registerC2SPackets();

        ParticleFactoryRegistry.getInstance().register(modParticles.BLEED_PARTICLE, bleedParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(modParticles.BLEED_PARTICLE_IN_WATER, bleedParticlesInWater.Factory::new);
    }
}
