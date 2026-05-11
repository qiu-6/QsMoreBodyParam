package net.qiu.morebodyparam.mixin;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.qiu.lib.QsLibApi;
import net.qiu.morebodyparam.component.entityComponentRegister;
import net.qiu.morebodyparam.config.modConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BucketItem.class)
public abstract class waterBucketDrinkable extends Item {

    @Shadow private final Fluid fluid;

    @Unique
    private static final int BUCKET_THIRST_REGEN = modConfig.thirst_regen_water_bucket;

    public waterBucketDrinkable(Settings settings, Fluid fluid) {
        super(settings);
        this.fluid = fluid;
    }

    @Unique
    private boolean isDrinkable(ItemStack stack) {
        // Only allow drinking if the bucket contains a fluid that isn't EMPTY or LAVA.
        return this.fluid == Fluids.WATER && stack.getNbt() == null;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 32;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return isDrinkable(stack) ? UseAction.DRINK : UseAction.NONE;
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void injectDrinkLogic(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir){

        ItemStack itemStack = user.getStackInHand(hand);

        if (!isDrinkable(itemStack)) return;

        boolean isThirstFull = !user.getAbilities().creativeMode &&
                entityComponentRegister.THIRST_COMPONENT.maybeGet(user).map(thirst ->
                                thirst.getThirst() >= 20)
                .orElseGet(() -> {
                    QsLibApi.error.ccaNotRegistered(entityComponentRegister.THIRST_COMPONENT, user);
                    return false;
                });

        if (isThirstFull) return;

        HitResult hitResult = raycast(world, user, RaycastContext.FluidHandling.NONE);

        if (hitResult.getType() == HitResult.Type.MISS || user.isSneaking()){
            cir.setReturnValue(ItemUsage.consumeHeldItem(world, user, hand));
        }
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient) {
            if (user instanceof ServerPlayerEntity serverPlayer) {
                Criteria.CONSUME_ITEM.trigger(serverPlayer, stack);
            }
        }

        if (user instanceof PlayerEntity player ) {
            if (player.isCreative()) {
                return stack;
            } else {
                entityComponentRegister.THIRST_COMPONENT.maybeGet(player).ifPresent(thirst -> {
                    thirst.setThirst(thirst.getThirst() + BUCKET_THIRST_REGEN);
                    entityComponentRegister.THIRST_COMPONENT.sync(player);
                });
            }
        }

        return new ItemStack(Items.BUCKET);
    }
}
