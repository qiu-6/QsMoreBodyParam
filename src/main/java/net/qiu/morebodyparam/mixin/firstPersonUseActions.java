package net.qiu.morebodyparam.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.qiu.morebodyparam.util.modEnumExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class firstPersonUseActions {

    @Inject(method = "renderFirstPersonItem", at = @At("HEAD"), cancellable = true)
    private void handleCustomUseAction(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {

        if (player.isUsingItem() && player.getActiveHand() == hand ) {

            if (item.getUseAction() == modEnumExtension.APPLY_BANDAGE) {

                matrices.push();

                float useTime = (float)player.getItemUseTime() + tickDelta;
                float yOffset = (float)Math.sin(useTime * 0.1f) * 0.1f;

                matrices.translate(0.5f, yOffset, 0f);

                this.renderItem(
                        player, item,
                        hand == Hand.MAIN_HAND ? ModelTransformationMode.FIRST_PERSON_RIGHT_HAND : ModelTransformationMode.FIRST_PERSON_LEFT_HAND,
                        hand == Hand.OFF_HAND,
                        matrices, vertexConsumers, light
                );

                matrices.pop(); // Restore the state
                ci.cancel();
            }
        }
    }

    @Shadow
    public abstract void renderItem(LivingEntity entity, ItemStack item, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light);
}
