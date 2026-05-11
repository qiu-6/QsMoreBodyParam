package net.qiu.morebodyparam.component.bleed;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.random.Random;
import net.qiu.lib.QsLibAPI;
import net.qiu.morebodyparam.component.entityComponentRegister;
import net.qiu.morebodyparam.config.modConfig;
import net.qiu.morebodyparam.particle.modParticles;

public class bleedComponentImpl implements bleedComponent, AutoSyncedComponent, ServerTickingComponent {

    public static final String MOD_TAG = "bleed";

    private static final String BLEED_DURATION_KEY  = "q_bleedDuration";
    private static final String BLEED_INTENSITY_KEY = "q_bleedIntensity";
    private static final String BLEED_COMBO_KEY     = "q_bleedCombo";

    public static final int MAX_BLEED_INTENSITY = 2;
    public static final int MAX_COMBO           = 20;

    private static final int   MAX_BLEED_DURATION = modConfig.maximum_bleeding_duration * 60 * 20;

    private static final float HIGH_BLEED_DRAIN    = modConfig.blood_high_drain;
    private static final float MID_BLEED_DRAIN     = modConfig.blood_mid_drain;
    private static final float LOW_BLEED_DRAIN     = modConfig.blood_low_drain;

    private static final Random random = Random.create();

    private final PlayerEntity player;

    private int   bleedDuration  = 0;
    private int   bleedIntensity = 0;
    private int   bleedCombo     = 0;
    private float bloodLossTracker = 0.0f;

    public bleedComponentImpl(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public void serverTick() {

        if (bleedDuration == 0) return;

        drainBlood();
        spawnParticles();
    }

    @Override
    public int getBleedDuration() {
        return bleedDuration;
    }

    @Override
    public int getBleedIntensity() {
        return bleedIntensity;
    }

    @Override
    public void setBleed(int duration, int intensity) {
        bleedDuration = Math.min(duration, MAX_BLEED_DURATION);

        if (bleedDuration == 0 || intensity < 0) {
            bleedDuration = 0;
            bleedIntensity = 0;
            bloodLossTracker = 0.0f;
            entityComponentRegister.BLEED_COMPONENT.sync(player);
            return;
        }

        bleedIntensity = Math.min(intensity, MAX_BLEED_INTENSITY);
        entityComponentRegister.BLEED_COMPONENT.sync(player);
    }

    @Override
    public int getCombo() {
        return bleedCombo;
    }

    @Override
    public void setCombo(int newCombo) {
        bleedCombo = newCombo;
        if (bleedCombo >= MAX_COMBO) {
            bleedIntensity = Math.min(bleedIntensity + 1, MAX_BLEED_INTENSITY);
            bleedCombo = 0;
        }
    }

    // -------------------------------------------------------------------------
    // Internal helpers
    // -------------------------------------------------------------------------

    /**
     * Accumulates fractional blood-drain each tick based on intensity and the
     * player's movement state, then removes one blood point when the tracker
     * reaches a whole number.  Also decrements {@link #bleedDuration}.
     */
    private void drainBlood() {
        if (player.isCreative() || player.isSpectator()) return;

        float drainPerTick = 1.0f / (60 * 20 *
                switch (bleedIntensity) {
                    case 2  -> HIGH_BLEED_DRAIN;
                    case 1  -> MID_BLEED_DRAIN;
                    default -> LOW_BLEED_DRAIN;
                });

        float multiplier = switch (QsLibAPI.movement.getState(player)) {
            case IDLE -> player.isInsideWaterOrBubbleColumn()
                    ? modConfig.bleed_water_multiplier : 1.0f;
            case WALKING -> modConfig.bleed_walk_multiplier;
            case SPRINTING -> modConfig.bleed_sprint_multiplier;
            case SWIMMING -> modConfig.bleed_walk_multiplier * modConfig.bleed_water_multiplier;
            case SPRINTING_SWIMMING -> modConfig.bleed_sprint_multiplier * modConfig.bleed_water_multiplier;
        };

        bloodLossTracker += drainPerTick * multiplier;

        if (bloodLossTracker >= 1) {
            bloodLossTracker = 0.0f;

            // Delegate the actual blood reduction to bloodLevelComponent
            entityComponentRegister.BLOOD_COMPONENT.maybeGet(player).ifPresent(bloodLevel -> {
                if (bloodLevel.getBlood() > 0) {
                    bloodLevel.setBlood(bloodLevel.getBlood() - 1);
                }
            });
        }

        if (bleedDuration > 0) bleedDuration--;

        entityComponentRegister.BLEED_COMPONENT.sync(player);
    }

    private void spawnParticles() {
        if (!(player instanceof ServerPlayerEntity serverPlayer)) return;

        // Higher intensity → more frequent particle bursts
        if (random.nextInt(3 - bleedIntensity) == 0) {
            serverPlayer.getServerWorld().spawnParticles(
                    player.isSubmergedIn(FluidTags.WATER) ? modParticles.BLEED_PARTICLE_IN_WATER : modParticles.BLEED_PARTICLE,
                    player.getX(),
                    player.getY() + (player.isInSwimmingPose() ? 0.3 : 1),
                    player.getZ(),
                    5, 0.1, 0.2, 0.1, 0
            );
        }
    }

    // -------------------------------------------------------------------------
    // NBT
    // -------------------------------------------------------------------------

    @Override
    public void readFromNbt(NbtCompound tag) {
        bleedDuration = tag.contains(BLEED_DURATION_KEY) ? tag.getInt(BLEED_DURATION_KEY) : 0;
        bleedIntensity = tag.contains(BLEED_INTENSITY_KEY) ? tag.getInt(BLEED_INTENSITY_KEY) : 0;
        bleedCombo = tag.contains(BLEED_COMBO_KEY) ? tag.getInt(BLEED_COMBO_KEY) : 0;
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt(BLEED_DURATION_KEY, bleedDuration);
        tag.putInt(BLEED_INTENSITY_KEY, bleedIntensity);
        tag.putInt(BLEED_COMBO_KEY, bleedCombo);
    }

    // -------------------------------------------------------------------------
    // Sync packet
    // -------------------------------------------------------------------------

    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
        buf.writeInt(bleedDuration);
        buf.writeInt(bleedIntensity);
        buf.writeFloat(bloodLossTracker);
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        this.bleedDuration = buf.readInt();
        this.bleedIntensity = buf.readInt();
        this.bloodLossTracker = buf.readFloat();
    }
}