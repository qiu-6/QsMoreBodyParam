package net.qiu.morebodyparam;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.entity.damage.DamageScaling;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.qiu.morebodyparam.datagen.*;
import net.qiu.morebodyparam.util.modTags;

public class QsMoreBodyParametersDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(modDamageTypeProvider::new);
		pack.addProvider(modDamageTagProvider::new);
		pack.addProvider(modLanguageProvider::new);
		pack.addProvider(modParticleProvider::new);
		pack.addProvider(modItemTagProvider::new);
	}

	@Override
	public void buildRegistry(RegistryBuilder registryBuilder) {

		registryBuilder.addRegistry(RegistryKeys.DAMAGE_TYPE, entries -> {
			entries.register(modTags.damageTypes.BLEED, new DamageType("bleed", DamageScaling.NEVER, 0.0f));
		});
	}
}
