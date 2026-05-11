package net.qiu.morebodyparam.datagen;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.BiConsumer;

import static net.qiu.morebodyparam.QsMoreBodyParameters.MOD_ID;

public class modParticleProvider extends FabricCodecDataProvider<modParticleProvider.particleTextures> {

    public modParticleProvider(FabricDataOutput dataOutput) {
        super(dataOutput, DataOutput.OutputType.RESOURCE_PACK, "particles", particleTextures.CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, particleTextures> provider) {
        provider.accept(
                new Identifier(MOD_ID, "bleed_particle"),
                new particleTextures(List.of(
                        new Identifier(MOD_ID, "bleed")
                ))
        );

        provider.accept(
                new Identifier(MOD_ID, "bleed_particles_in_water"),
                new particleTextures(List.of(
                        new Identifier(MOD_ID, "bleed")
                ))
        );
    }

    public record particleTextures(List<Identifier> textures) {
        public static final Codec<particleTextures> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Identifier.CODEC.listOf().fieldOf("textures").forGetter(particleTextures::textures)
                ).apply(instance, particleTextures::new)
        );
    }

    @Override
    public String getName() {
        return "Particle definition";
    }
}
