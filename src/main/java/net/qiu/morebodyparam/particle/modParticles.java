package net.qiu.morebodyparam.particle;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static net.qiu.morebodyparam.QsMoreBodyParameters.MOD_ID;

public class modParticles {

    public static final DefaultParticleType BLEED_PARTICLE = FabricParticleTypes.simple();
    public static final DefaultParticleType BLEED_PARTICLE_IN_WATER = FabricParticleTypes.simple();

    public static void registerParticles() {
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(MOD_ID, "bleed_particle"),
                BLEED_PARTICLE);

        Registry.register(Registries.PARTICLE_TYPE, new Identifier(MOD_ID, "bleed_particles_in_water"),
                BLEED_PARTICLE_IN_WATER);
    }
}
