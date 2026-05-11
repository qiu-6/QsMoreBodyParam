package net.qiu.morebodyparam.mixin;

import net.minecraft.util.UseAction;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(UseAction.class)
public interface useActionInvoker {

    @Invoker("<init>")
    static UseAction create(String name, int ordinal) {
        throw new AssertionError(); // Mixin replaces this at runtime
    }
}
