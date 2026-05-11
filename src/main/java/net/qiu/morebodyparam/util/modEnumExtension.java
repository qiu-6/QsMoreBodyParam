package net.qiu.morebodyparam.util;

import net.minecraft.util.UseAction;
import net.qiu.morebodyparam.QsMoreBodyParameters;
import net.qiu.morebodyparam.mixin.useActionInvoker;

public class modEnumExtension {

    public static final UseAction APPLY_BANDAGE = useActionInvoker.create("APPLY_BANDAGE", UseAction.values().length);

    public static void register() {
        // Just calling this method triggers the static field initialization above
        QsMoreBodyParameters.LOGGER.info("Registered custom UseActions for Q's more body parameters mod");
    }
}
