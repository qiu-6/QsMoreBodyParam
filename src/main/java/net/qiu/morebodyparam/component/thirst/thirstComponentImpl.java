package net.qiu.morebodyparam.component.thirst;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.biome.Biome;
import net.qiu.morebodyparam.component.entityComponentRegister;
import net.qiu.morebodyparam.config.modConfig;
import net.qiu.morebodyparam.util.modTags;

public class thirstComponentImpl implements thirstComponent, AutoSyncedComponent, ServerTickingComponent {

    public static boolean DISABLED_IN_PEACEFUL = modConfig.disable_thirst_in_peaceful;
    public static final String MOD_TAG = "thirst";
    public static final String THIRST_KEY = "thirst";
    public static final String THIRST_TRACKER_KEY = "thirstIncrement";
    private static float THIRST_PER_TICK = 1.0f / (modConfig.thirst_natural_drain * 20.0f);
    private static float OPTIMAL_TEMPERATURE = modConfig.thirst_optimal_temperature;

    private int thirst = 20;
    private float thirstTracker = 0.0f;
    private final PlayerEntity player;

    public thirstComponentImpl(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public int getThirst() {
        return thirst;
    }

    @Override
    public void setThirst(int value) {
        if (player.isSpectator() || player.isCreative()) {
            return;
        }

        thirst = Math.max(0, Math.min(20, value));

        entityComponentRegister.THIRST_COMPONENT.sync(player);
        if (thirst == 0) {
            player.damage(modTags.damageTypes.of(player, modTags.damageTypes.THIRST), Float.MAX_VALUE);
        }
    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound) {
        thirst = nbtCompound.contains(THIRST_KEY) ? nbtCompound.getInt(THIRST_KEY) : 20;
        thirstTracker = nbtCompound.contains(THIRST_TRACKER_KEY) ? nbtCompound.getFloat(THIRST_TRACKER_KEY) : 0.0f;
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound) {
        nbtCompound.putInt(THIRST_KEY, thirst);
        nbtCompound.putFloat(THIRST_TRACKER_KEY, thirstTracker);
    }

    @Override
    public void serverTick() {
        incrementThirstTracker();
    }

    private void incrementThirstTracker() {

        if ((player.getWorld().getDifficulty() == Difficulty.PEACEFUL) && DISABLED_IN_PEACEFUL) return;

        thirstTracker += THIRST_PER_TICK * biomeMultiplication();
        if (thirstTracker >= 1) {
            setThirst(thirst - 1);
            thirstTracker = 0;
        }
    }

    private float biomeMultiplication() {

        RegistryEntry<Biome> biome = player.getWorld().getBiome(player.getBlockPos());
        float temperature = biome.value().getTemperature();

        temperature = Math.min(temperature, 2.0f);

        if (temperature > OPTIMAL_TEMPERATURE) {
            return (modConfig.thirst_hot_multiplier * MathHelper.getLerpProgress(temperature, OPTIMAL_TEMPERATURE, 2.0f));
        } else if (temperature < OPTIMAL_TEMPERATURE) {
            return (modConfig.thirst_cold_multiplier * (temperature / OPTIMAL_TEMPERATURE));
        } else {
            return 1.0f;
        }
    }

    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
        buf.writeVarInt(this.thirst);
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        this.thirst = buf.readVarInt();
    }
}
