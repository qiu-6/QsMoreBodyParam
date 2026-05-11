package net.qiu.morebodyparam.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.fluid.FluidState;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

public class bleedParticlesInWater extends SpriteBillboardParticle {

    private static final Random random = Random.create();

    public bleedParticlesInWater(ClientWorld clientWorld, double x, double y, double z, SpriteProvider spriteProvider, double vx, double vy, double vz) {
        super(clientWorld, x, y, z, vx, vy, vz);

        this.gravityStrength = 0;
        this.velocityMultiplier = 0.8f;
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
    public void tick() {
        // Save the old position for interpolation
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;

        if (this.age++ >= this.maxAge) {
            this.markDead();
        } else {
            // 1. Get the fluid state at the particle's position
            BlockPos pos = BlockPos.ofFloored(this.x, this.y, this.z);
            FluidState fluidState = this.world.getFluidState(pos);

            if (!fluidState.isEmpty()) {
                // 2. Get the velocity of the current
                Vec3d flow = fluidState.getVelocity(this.world, pos);

                // 3. Apply the flow to the particle's velocity
                // You can multiply 'flow' by a decimal to make it move slower/faster
                this.velocityX += flow.x * 0.05;
                this.velocityY += flow.y * 0.05;
                this.velocityZ += flow.z * 0.05;
            }

            // 4. Move the particle based on new velocity
            this.move(this.velocityX, this.velocityY, this.velocityZ);

            // 5. Apply friction so it doesn't accelerate forever
            this.velocityX *= 0.98;
            this.velocityY *= 0.98;
            this.velocityZ *= 0.98;
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
            return new bleedParticlesInWater(world, x, y, z, this.spriteProvider, velocityX, velocityY, velocityZ);
        }
    }
}
