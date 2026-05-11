package net.qiu.morebodyparam.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import static net.qiu.morebodyparam.QsMoreBodyParameters.MOD_ID;

public class modTags {

    public static class damageTags {

        public static final TagKey<DamageType> APPLY_BLEED_DAMAGE =
                createTag("can_apply_bleed");

        public static final TagKey<DamageType> REMOVE_BLEED_DAMAGE =
                createTag("can_remove_bleed");

        private static TagKey<DamageType> createTag(String name) {
            return TagKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(MOD_ID, name));
        }
    }

    public static class damageTypes {

        public static final RegistryKey<DamageType> BLEED = createTag("bleed");
        public static final RegistryKey<DamageType> THIRST = createTag("thirst");

        public static DamageSource of(Entity entity, RegistryKey<DamageType> key) {
            return new DamageSource(
                    entity.getWorld().getRegistryManager()
                            .get(RegistryKeys.DAMAGE_TYPE)
                            .entryOf(key)
            );
        }

        private static RegistryKey<DamageType> createTag(String name) {
            return RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(MOD_ID, name));
        }
    }

    public static class itemTags {
        public static final TagKey<Item> IS_BANDAGE = createTag("is_bandage");

        public static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, new Identifier(MOD_ID, name));
        }
    }
}
