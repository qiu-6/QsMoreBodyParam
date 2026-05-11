package net.qiu.morebodyparam.mixin;

import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.qiu.morebodyparam.util.modEnumExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityModel.class)
public abstract class thirdPersonUseActions {

    @Inject(method = "positionRightArm", at = @At("TAIL"))
    private void poseCustomAction(LivingEntity entity, CallbackInfo ci) {

        if (entity.getActiveItem().getUseAction() == modEnumExtension.APPLY_BANDAGE) {

            BipedEntityModel<?> model = (BipedEntityModel<?>)(Object)this;

            model.rightArm.setAngles((float) Math.toRadians(-140), (float) Math.toRadians(-60), (float) Math.toRadians(85));
        }
    }
}
