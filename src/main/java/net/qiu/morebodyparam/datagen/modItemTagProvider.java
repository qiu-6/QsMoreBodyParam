package net.qiu.morebodyparam.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

import static net.qiu.morebodyparam.QsMoreBodyParameters.MOD_ID;

public class modItemTagProvider extends FabricTagProvider.ItemTagProvider {

    public modItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup lookup) {

        getOrCreateTagBuilder(TagKey.of(RegistryKeys.ITEM, new Identifier(MOD_ID, "is_bandage")))
                .add(Items.DRIED_KELP)
                .add(Items.PAPER);
    }
}
