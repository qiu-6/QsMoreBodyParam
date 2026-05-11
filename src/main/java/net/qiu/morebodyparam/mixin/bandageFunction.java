package net.qiu.morebodyparam.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.qiu.morebodyparam.component.entityComponentRegister;
import net.qiu.morebodyparam.util.modEnumExtension;
import net.qiu.morebodyparam.util.modTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class bandageFunction {

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void startUsing(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {

        ItemStack stack = user.getStackInHand(hand);

        if (!stack.isIn(modTags.itemTags.IS_BANDAGE)) return;
        if (entityComponentRegister.BLEED_COMPONENT.get(user).getBleedDuration() == 0) return;

        user.setCurrentHand(hand);
        cir.setReturnValue(TypedActionResult.consume(stack));
    }

    @Inject(method = "getUseAction", at = @At("HEAD"), cancellable = true)
    private void setAnimation(ItemStack stack, CallbackInfoReturnable<UseAction> cir) {

        if (!stack.isIn(modTags.itemTags.IS_BANDAGE)) return;

        cir.setReturnValue(modEnumExtension.APPLY_BANDAGE);
    }

    @Inject(method = "getMaxUseTime", at = @At("HEAD"), cancellable = true)
    private void setMaxTime(ItemStack stack, CallbackInfoReturnable<Integer> cir) {

        if (!stack.isIn(modTags.itemTags.IS_BANDAGE)) return;

        cir.setReturnValue(60);
    }

    @Inject(method = "finishUsing", at = @At("HEAD"), cancellable = true)
    private void onFinish(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        if (stack.isIn(modTags.itemTags.IS_BANDAGE)) {

            if (!world.isClient) return;

            if (!(user instanceof PlayerEntity player && player.getAbilities().creativeMode)) {
                stack.decrement(1);

                entityComponentRegister.BLEED_COMPONENT.maybeGet(user).ifPresent(bleedComponent ->
                        bleedComponent.setBleed(bleedComponent.getBleedDuration(), bleedComponent.getBleedIntensity() - 1));
            }
            // Return the stack to indicate the process is done
            cir.setReturnValue(stack);
        }
    }
}
