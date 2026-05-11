package net.qiu.morebodyparam.mixin;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.Difficulty;
import net.qiu.morebodyparam.component.entityComponentRegister;
import net.qiu.morebodyparam.config.modConfig;
import net.qiu.morebodyparam.util.modTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class damageModifyBleed {

    @Unique
    private static float BLEED_DURATION = modConfig.bleed_duration_added_per_hit;
    @Unique
    private static float BLEED_DURATION_REDUCTION = modConfig.bleed_duration_reduced_per_damage;
    @Unique
    private static int HIGH_BLEED_THRESHOLD = modConfig.high_bleed_threshold;
    @Unique
    private static int MID_BLEED_THRESHOLD = modConfig.mid_bleed_threshold;
    @Unique
    private static int LOW_BLEED_THRESHOLD = modConfig.low_bleed_threshold;

    @Inject(method = "applyDamage", at = @At("RETURN"))
    private void bleedOnAttack(DamageSource source, float amount, CallbackInfo ci) {

        PlayerEntity player = (PlayerEntity) (Object) this;

        if (player.isDead()) return;

        // Disabled in peaceful difficulty
        if (modConfig.disable_blood_in_peaceful && player.getWorld().getDifficulty() == Difficulty.PEACEFUL) {
            return;
        }

        // Only run logic on the server to prevent desync
        if (player.getWorld().isClient()) return;

        if (source.isIn(modTags.damageTags.APPLY_BLEED_DAMAGE)) {

            if (amount > LOW_BLEED_THRESHOLD) {
                int intensity;
                if (amount >= HIGH_BLEED_THRESHOLD) {
                    intensity = 2;
                } else if (amount >= MID_BLEED_THRESHOLD) {
                    intensity = 1;
                } else {
                    intensity = 0;
                }

                entityComponentRegister.BLEED_COMPONENT.maybeGet(player).ifPresent(bleedComponent -> {
                    bleedComponent.setBleed(bleedComponent.getBleedDuration() + Math.round(BLEED_DURATION * 60 * 20), intensity);
                    bleedComponent.setCombo(bleedComponent.getCombo() + 1);
                });
            }
        } else if (source.isIn(modTags.damageTags.REMOVE_BLEED_DAMAGE)) {

            entityComponentRegister.BLEED_COMPONENT.maybeGet(player).ifPresent(bleedComponent -> {

                if (bleedComponent.getBleedDuration() <= 0) return;

                int newDuration = Math.round(bleedComponent.getBleedDuration() - (20 * 60 * BLEED_DURATION_REDUCTION));

                newDuration = Math.max(0, newDuration);

                bleedComponent.setBleed(newDuration, bleedComponent.getBleedIntensity());
            });
        }
    }
}
