package net.qiu.morebodyparam.component.blood;

import dev.onyxstudios.cca.api.v3.component.load.ServerLoadAwareComponent;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.random.Random;
import net.qiu.lib.component.movement.moveComponentReg;
import net.qiu.morebodyparam.component.entityComponentRegister;
import net.qiu.morebodyparam.config.modConfig;
import net.qiu.morebodyparam.particle.modParticles;
import net.qiu.morebodyparam.util.modTags;

import java.util.Objects;
import java.util.UUID;

public class bloodLevelComponentImpl implements bloodLevelComponent, AutoSyncedComponent, ServerTickingComponent, ServerLoadAwareComponent {

    public static final String MOD_TAG = "blood_level";

    private static final String BLOOD_KEY = "q_blood";
    private static final String BLOOD_LOSS_KEY = "q_bloodLossIncrement";
    private static final String BLOOD_GAIN_KEY = "q_bloodGainIncrement";

    private final PlayerEntity player;

    private int blood = 20;
    private float bloodGainTracker = 0.0f;

    private static boolean DISABLED_IN_PEACEFUL = modConfig.disable_blood_in_peaceful;
    private static boolean USE_COMPLEX = modConfig.complex_blood_threshold;

    private static int MILD_BLOOD_LOSS_THRESHOLD = modConfig.mild_blood_loss;
    private static int HEAVY_BLOOD_LOSS_THRESHOLD = modConfig.heavy_blood_loss;

    private static int ATTACK_REDUCTION_THRESHOLD = modConfig.attack_reduction_threshold;
    private static int ATTACK_SPEED_REDUCTION_THRESHOLD = modConfig.hit_speed_reduction_threshold;
    private static int SPEED_REDUCTION_THRESHOLD = modConfig.speed_reduction_threshold;
    private static int MINING_SPEED_REDUCTION_THRESHOLD = modConfig.mining_speed_reduction_threshold;

    private static final UUID ATTACK_DAMAGE_UUID = UUID.fromString("49919ee8-2cc3-4feb-990f-2fac31cc69d7");
    private static final UUID ATTACK_SPEED_UUID = UUID.fromString("4ff9f215-4e11-49dd-886d-7e37b6457003");
    private static final UUID SPEED_PENALTY_UUID = UUID.fromString("163166c0-929d-4392-bab6-6a472b826538");

    private static int ATTACK_PENALITY = modConfig.blood_attack_penality;
    private static int ATTACK_SPEED_PENALITY = modConfig.blood_attack_speed_penality;
    private static int SPEED_PENALITY = modConfig.blood_speed_penality;
    private static int MINING_SPEED_PENALITY = modConfig.blood_mining_speed_penality;

    private static float GAIN_TRACKER_PER_TICK = modConfig.blood_regen_per_day / 24000f; // 24000f = default tick per day

    public bloodLevelComponentImpl(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public void serverTick() {
        incrementGainTracker();
    }

    @Override
    public int getBlood() {
        return blood;
    }

    @Override
    public void setBlood(int newBlood) {

        if (player.isSpectator() || player.isCreative()) return;
        if (newBlood == blood) return;

        if (Math.min(newBlood, 20) > blood) {
            bloodGainTracker = 0.0f;
        }

        blood = Math.max(0, Math.min(newBlood, 20));
        updateEffects();
        entityComponentRegister.BLOOD_COMPONENT.sync(player);
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return (blood <= (USE_COMPLEX ? MINING_SPEED_REDUCTION_THRESHOLD : HEAVY_BLOOD_LOSS_THRESHOLD)) ? (1f - MINING_SPEED_PENALITY / 100f) : 1f;
    }

    private void incrementGainTracker() {

        bloodGainTracker += switch (player.getWorld().getDifficulty()) {
            case PEACEFUL -> DISABLED_IN_PEACEFUL ? 0.5f : GAIN_TRACKER_PER_TICK;
            case EASY, NORMAL, HARD -> GAIN_TRACKER_PER_TICK;
        };

        if (bloodGainTracker >= 1) {
            setBlood(blood + 1);
        }
    }

    private void updateEffects() {

        if (player.getWorld().isClient()) return;

        if (blood == 0) {
            player.damage(modTags.damageTypes.of(player, modTags.damageTypes.BLEED), Float.MAX_VALUE);
            return;
        }

        updatePerEffect(MILD_BLOOD_LOSS_THRESHOLD, ATTACK_REDUCTION_THRESHOLD, EntityAttributes.GENERIC_ATTACK_DAMAGE, ATTACK_DAMAGE_UUID, ATTACK_PENALITY);
        updatePerEffect(MILD_BLOOD_LOSS_THRESHOLD, ATTACK_SPEED_REDUCTION_THRESHOLD, EntityAttributes.GENERIC_ATTACK_SPEED, ATTACK_SPEED_UUID, ATTACK_SPEED_PENALITY);
        updatePerEffect(HEAVY_BLOOD_LOSS_THRESHOLD, SPEED_REDUCTION_THRESHOLD, EntityAttributes.GENERIC_MOVEMENT_SPEED, SPEED_PENALTY_UUID, SPEED_PENALITY);
    }

    private void updatePerEffect(int simpleThreshold, int complexThreshold, EntityAttribute attribute, UUID uuid, int modifier) {

        int threshold = USE_COMPLEX ? complexThreshold: simpleThreshold;

        EntityAttributeInstance attributeInstance = player.getAttributeInstance(attribute);

        if (attributeInstance == null) return;

        if (blood <= threshold) {

            if (attributeInstance.getModifier(uuid) == null) {
                String name = attribute.getTranslationKey().substring(attribute.getTranslationKey().lastIndexOf('.') + 1);

                EntityAttributeModifier attributeModifier = new EntityAttributeModifier(
                        uuid,
                        name.replace('_', ' ') + "penality from blood loss",
                        -1 * modifier / 100f,
                        EntityAttributeModifier.Operation.MULTIPLY_TOTAL
                );

                attributeInstance.addPersistentModifier(attributeModifier);
            }
        } else {
            attributeInstance.removeModifier(uuid);
        }
    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound) {
        blood = nbtCompound.contains(BLOOD_KEY) ? nbtCompound.getInt(BLOOD_KEY) : 20;
        bloodGainTracker = nbtCompound.contains(BLOOD_GAIN_KEY) ? nbtCompound.getFloat(BLOOD_GAIN_KEY) : 0.0f;
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound) {
        nbtCompound.putInt(BLOOD_KEY, blood);
        nbtCompound.putFloat(BLOOD_GAIN_KEY, bloodGainTracker);
    }

    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
        buf.writeInt(blood);
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        this.blood = buf.readInt();
    }

    @Override
    public void loadServerside() {

        Objects.requireNonNull(player.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE)).removeModifier(ATTACK_DAMAGE_UUID);
        Objects.requireNonNull(player.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_SPEED)).removeModifier(ATTACK_SPEED_UUID);
        Objects.requireNonNull(player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)).removeModifier(SPEED_PENALTY_UUID);

        updateEffects();
    }
}
