package net.qiu.morebodyparam.component;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.util.Identifier;
import net.qiu.morebodyparam.component.bleed.bleedComponent;
import net.qiu.morebodyparam.component.bleed.bleedComponentImpl;
import net.qiu.morebodyparam.component.blood.bloodLevelComponent;
import net.qiu.morebodyparam.component.blood.bloodLevelComponentImpl;
import net.qiu.morebodyparam.component.thirst.thirstComponent;
import net.qiu.morebodyparam.component.thirst.thirstComponentImpl;

import static net.qiu.morebodyparam.QsMoreBodyParameters.MOD_ID;

public class entityComponentRegister implements EntityComponentInitializer {

    public static final ComponentKey<bloodLevelComponent> BLOOD_COMPONENT =
            ComponentRegistry.getOrCreate(
                    new Identifier(MOD_ID, bloodLevelComponentImpl.MOD_TAG),
                    bloodLevelComponent.class
            );

    public static final ComponentKey<thirstComponent> THIRST_COMPONENT =
            ComponentRegistry.getOrCreate(
                    new Identifier(MOD_ID, thirstComponentImpl.MOD_TAG),
                    thirstComponent.class
            );

    public static final ComponentKey<bleedComponent>  BLEED_COMPONENT =
            ComponentRegistry.getOrCreate(
                    new Identifier(MOD_ID, bleedComponentImpl.MOD_TAG),
                    bleedComponent.class
            );

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry entityComponentFactoryRegistry) {

        entityComponentFactoryRegistry.registerForPlayers(
                BLOOD_COMPONENT,
                bloodLevelComponentImpl::new,
                RespawnCopyStrategy.LOSSLESS_ONLY
        );

        entityComponentFactoryRegistry.registerForPlayers(
                THIRST_COMPONENT,
                thirstComponentImpl::new,
                RespawnCopyStrategy.LOSSLESS_ONLY
        );

        entityComponentFactoryRegistry.registerForPlayers(
                BLEED_COMPONENT,
                bleedComponentImpl::new,
                RespawnCopyStrategy.LOSSLESS_ONLY
        );
    }
}
