package net.qiu.morebodyparam.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.entity.damage.DamageScaling;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryWrapper;
import net.qiu.morebodyparam.util.modTags;

import java.util.concurrent.CompletableFuture;


public class modDamageTypeProvider extends FabricDynamicRegistryProvider {
    public modDamageTypeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup, Entries entries) {
        entries.add(modTags.damageTypes.BLEED, new DamageType("bleed", DamageScaling.NEVER, 0.0f));
        entries.add(modTags.damageTypes.THIRST, new DamageType("thirst", DamageScaling.NEVER, 0.0f));
    }

    @Override
    public String getName() {
        return "Damage Types";
    }
}
