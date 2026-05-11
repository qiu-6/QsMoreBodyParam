package net.qiu.morebodyparam.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import net.qiu.morebodyparam.QsMoreBodyParameters;
import net.qiu.morebodyparam.component.entityComponentRegister;
import net.qiu.morebodyparam.component.thirst.thirstComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.qiu.morebodyparam.config.modConfig.heavy_blood_loss;

@Mixin(InGameHud.class)
public abstract class hudInjection {

	@Shadow @Final private MinecraftClient client;
	@Shadow private int scaledWidth;
	@Shadow private int scaledHeight;
	@Shadow protected abstract PlayerEntity getCameraPlayer();

	// Empty icon
	@Unique
	private static final Identifier ICON_EMPTY = new Identifier(QsMoreBodyParameters.MOD_ID, "textures/gui/iconempty.png");

	// Thirst bar icon
	@Unique
	private static final Identifier THIRST_ICON_FULL = new Identifier(QsMoreBodyParameters.MOD_ID, "textures/gui/thirsticonfull.png");
	@Unique
	private static final Identifier THIRST_ICON_HALF = new Identifier(QsMoreBodyParameters.MOD_ID, "textures/gui/thirsticonhalf.png");

	// Blood bar icon
	@Unique
	private static final Identifier BLOOD_ICON_FULL = new Identifier(QsMoreBodyParameters.MOD_ID, "textures/gui/bloodiconfull.png");
	@Unique
	private static final Identifier BLOOD_ICON_HALF = new Identifier(QsMoreBodyParameters.MOD_ID, "textures/gui/bloodiconhalf.png");

	@Unique
	private static int BLOOD_ICON_VIBRATE_THRESHOLD = heavy_blood_loss;

	@Unique
	private final Random random = Random.create();

	@Inject(method = "renderStatusBars", at = @At("TAIL"))
	private void renderMoreBars(DrawContext context, CallbackInfo ci) {

		PlayerEntity player = this.getCameraPlayer();

		context.getMatrices().push();

		// ── Shared horizontal anchor ──────────────────────────────────────────
		int _x = this.scaledWidth / 2 + 1;

		// Base Y for blood
		int thirstY = this.scaledHeight - 49;
		if (player.isSubmergedInWater() || player.getAir() < player.getMaxAir()) {
			thirstY -= 10;
		}

		// Blood bar sits 10 px above the thirst bar
		int bloodY = thirstY - 10;

		// ── Thirst bar ────────────────────────────────────────────────────────
		int thirstLevel = entityComponentRegister.THIRST_COMPONENT.get(player).getThirst();

		for (int i = 0; i < 10; i++) {
			int x = _x + i * 8 + 9;
			int y = thirstY;

			// Shake effect when very thirsty
			if (thirstLevel <= 4) {
				y += (random.nextInt(3) - 1);
			}


			if (i * 2 + 1 < thirstLevel) {
				context.drawTexture(THIRST_ICON_FULL, x, y, 0, 0, 9, 9, 9, 9);
			} else if (i * 2 + 1 == thirstLevel) {
				context.drawTexture(THIRST_ICON_HALF, x, y, 0, 0, 9, 9, 9, 9);
			} else {
				context.drawTexture(ICON_EMPTY, x, y, 0, 0, 9, 9, 9, 9);
			}
		}

		// ── Blood bar ────────────────────────────────────────────────────────
		int bloodLevel = entityComponentRegister.BLOOD_COMPONENT.get(player).getBlood();

		for (int i = 0; i < 10; i++) {
			int x = _x + i * 8 + 9;
			int y = bloodY;

			// Shake effect when critically low on blood
			if (bloodLevel <= BLOOD_ICON_VIBRATE_THRESHOLD) {
				y += (random.nextInt(3) - 1);
			}

			if (i * 2 + 1 < bloodLevel) {
				context.drawTexture(BLOOD_ICON_FULL,  x, y, 0, 0, 9, 9, 9, 9);
			} else if (i * 2 + 1 == bloodLevel) {
				context.drawTexture(BLOOD_ICON_HALF,  x, y, 0, 0, 9, 9, 9, 9);
			} else {
				context.drawTexture(ICON_EMPTY, x, y, 0, 0, 9, 9, 9, 9);
			}
		}

		context.getMatrices().pop();
	}
}