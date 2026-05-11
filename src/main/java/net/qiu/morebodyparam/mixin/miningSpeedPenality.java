package net.qiu.morebodyparam.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.qiu.morebodyparam.component.blood.bloodLevelComponent;
import net.qiu.morebodyparam.component.entityComponentRegister;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class miningSpeedPenality {



    @Inject(method = "getBlockBreakingSpeed", at = @At("RETURN"), cancellable = true)
    private void reduceMiningSpeed(BlockState block, CallbackInfoReturnable<Float> cir) {

        PlayerEntity player = (PlayerEntity) (Object) this;

        float modifier = entityComponentRegister.BLOOD_COMPONENT.maybeGet(player).map(bloodLevelComponent::getMiningSpeedMultiplier).orElse(1f);

        cir.setReturnValue(cir.getReturnValue() * modifier);
    }
}
