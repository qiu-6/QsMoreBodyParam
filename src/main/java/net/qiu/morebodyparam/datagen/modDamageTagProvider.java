package net.qiu.morebodyparam.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.DamageTypeTags;
import net.qiu.morebodyparam.util.modTags;

import java.util.concurrent.CompletableFuture;

public class modDamageTagProvider extends FabricTagProvider<DamageType> {

    public modDamageTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.DAMAGE_TYPE, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup lookup) {
        getOrCreateTagBuilder(modTags.damageTags.APPLY_BLEED_DAMAGE)
                .add(DamageTypes.ARROW)
                .add(DamageTypes.CACTUS)
                .add(DamageTypes.FALLING_STALACTITE)
                .add(DamageTypes.PLAYER_ATTACK)
                .add(DamageTypes.SWEET_BERRY_BUSH)
                .add(DamageTypes.TRIDENT);

        getOrCreateTagBuilder(modTags.damageTags.REMOVE_BLEED_DAMAGE)
                .add(DamageTypes.FIREBALL)
                .add(DamageTypes.HOT_FLOOR)
                .add(DamageTypes.IN_FIRE)
                .add(DamageTypes.LAVA)
                .add(DamageTypes.ON_FIRE)
                .add(DamageTypes.UNATTRIBUTED_FIREBALL);

        getOrCreateTagBuilder(DamageTypeTags.BYPASSES_COOLDOWN)
                .addOptional(modTags.damageTypes.BLEED)
                .addOptional(modTags.damageTypes.THIRST);

        getOrCreateTagBuilder(DamageTypeTags.BYPASSES_EFFECTS)
                .addOptional(modTags.damageTypes.BLEED)
                .addOptional(modTags.damageTypes.THIRST);

        getOrCreateTagBuilder(DamageTypeTags.BYPASSES_SHIELD)
                .addOptional(modTags.damageTypes.BLEED)
                .addOptional(modTags.damageTypes.THIRST);
    }
}
