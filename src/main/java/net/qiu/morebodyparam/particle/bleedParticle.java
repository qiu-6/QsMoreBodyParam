package net.qiu.morebodyparam.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

public class bleedParticle extends SpriteBillboardParticle {

    private static final Random random = Random.create();

    public bleedParticle(ClientWorld clientWorld, double x, double y, double z, SpriteProvider spriteProvider, double vx, double vy, double vz) {
        super(clientWorld, x, y, z, vx, vy, vz);

        this.gravityStrength = 0.981f;
        this.velocityX = random.nextFloat() * 0.1 - 0.05;
        this.velocityZ = random.nextFloat() * 0.1 - 0.05;
        this.velocityY = random.nextFloat() * 0.1 - 0.05;
        this.maxAge = 20;
        this.setSpriteForAge(spriteProvider);
        this.scale = 0.1f;
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        if (camera.isThirdPerson()) {
            super.buildGeometry(vertexConsumer, camera, tickDelta);
        }
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {

        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }


        @Override
        public @Nullable Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new bleedParticle(world, x, y, z, this.spriteProvider, velocityX, velocityY, velocityZ);
        }
    }
}
