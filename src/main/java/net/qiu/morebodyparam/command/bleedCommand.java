package net.qiu.morebodyparam.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.qiu.morebodyparam.component.entityComponentRegister;
import net.qiu.morebodyparam.config.modConfig;

import static net.qiu.morebodyparam.component.bleed.bleedComponentImpl.MAX_BLEED_INTENSITY;
import static net.qiu.morebodyparam.component.bleed.bleedComponentImpl.MAX_COMBO;

public class bleedCommand {

    private static int MAX_BLEED_DURATION = modConfig.maximum_bleeding_duration * 20 * 60;
    private static final int MAX_BLEEDING_INTENSITY = MAX_BLEED_INTENSITY;
    private static final int MAX_BLEED_COMBO = MAX_COMBO;

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher)  {

        dispatcher.register(CommandManager.literal("bleed")

                .requires(source -> source.hasPermissionLevel(1))
                .then(CommandManager.literal("get")
                        .executes(ctx -> getBleed(
                                ctx,
                                null
                        ))

                        .then(CommandManager.argument("target", EntityArgumentType.player())
                                .executes(ctx -> getBleed(
                                        ctx,
                                        EntityArgumentType.getPlayer(ctx, "target")
                                ))
                        )
                )

                .then(CommandManager.literal("combo")
                        .then(CommandManager.literal("get")
                                .executes(ctx -> getCombo(
                                        ctx,
                                        null
                                ))

                                .then(CommandManager.argument("target", EntityArgumentType.player())
                                        .executes(ctx -> getCombo(
                                                ctx,
                                                EntityArgumentType.getPlayer(ctx, "target")
                                        ))
                                )
                        )
                )

                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("set")
                        .then(CommandManager.argument("combo", IntegerArgumentType.integer(0, MAX_BLEED_COMBO - 1))
                                .executes(ctx -> setCombo(
                                        ctx,
                                        null,
                                        IntegerArgumentType.getInteger(ctx, "combo")
                                ))

                        )

                        .then(CommandManager.argument("target", EntityArgumentType.player())
                                .then(CommandManager.argument("combo", IntegerArgumentType.integer(0, MAX_BLEED_COMBO - 1))
                                        .executes(ctx -> setCombo(
                                                ctx,
                                                EntityArgumentType.getPlayer(ctx, "target"),
                                                IntegerArgumentType.getInteger(ctx, "combo")
                                        ))

                                )
                        )
                )

                .then(CommandManager.literal("set")
                        .then(CommandManager.argument("duration", IntegerArgumentType.integer(0, MAX_BLEED_DURATION))
                                .executes(ctx -> setBleed(
                                        ctx,
                                        null,
                                        IntegerArgumentType.getInteger(ctx, "duration"),
                                        -114514
                                ))

                                .then(CommandManager.argument("intensity", IntegerArgumentType.integer(0, MAX_BLEEDING_INTENSITY))
                                        .executes(ctx -> setBleed(
                                                ctx,
                                                null,
                                                IntegerArgumentType.getInteger(ctx, "duration"),
                                                IntegerArgumentType.getInteger(ctx, "intensity")
                                                )
                                        )
                                )
                        )

                        .then(CommandManager.literal("infinite")
                                .executes(ctx -> setBleed(
                                        ctx,
                                        null,
                                        -114514,
                                        -114514
                                ))

                                .then(CommandManager.argument("intensity", IntegerArgumentType.integer(0, MAX_BLEEDING_INTENSITY))
                                        .executes(ctx -> setBleed(
                                                        ctx,
                                                        null,
                                                        -114514,
                                                        IntegerArgumentType.getInteger(ctx, "intensity")
                                                )
                                        )
                                )
                        )

                        .then(CommandManager.argument("target", EntityArgumentType.player())
                                .then(CommandManager.argument("duration", IntegerArgumentType.integer(0, MAX_BLEED_DURATION))
                                        .executes(ctx -> setBleed(
                                                ctx,
                                                EntityArgumentType.getPlayer(ctx, "target"),
                                                IntegerArgumentType.getInteger(ctx, "duration"),
                                                -114514
                                        ))

                                        .then(CommandManager.argument("intensity", IntegerArgumentType.integer(0, MAX_BLEEDING_INTENSITY))
                                                .executes(ctx -> setBleed(
                                                                ctx,
                                                        EntityArgumentType.getPlayer(ctx, "target"),
                                                                IntegerArgumentType.getInteger(ctx, "duration"),
                                                                IntegerArgumentType.getInteger(ctx, "intensity")
                                                        )
                                                )
                                        )
                                )

                                .then(CommandManager.literal("infinite")
                                        .executes(ctx -> setBleed(
                                                ctx,
                                                EntityArgumentType.getPlayer(ctx, "target"),
                                                -114514,
                                                -114514
                                        ))

                                        .then(CommandManager.argument("intensity", IntegerArgumentType.integer(0, MAX_BLEEDING_INTENSITY))
                                                .executes(ctx -> setBleed(
                                                        ctx,
                                                        EntityArgumentType.getPlayer(ctx, "target"),
                                                        -114514,
                                                        IntegerArgumentType.getInteger(ctx, "intensity")
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
    }

    private static int getBleed(CommandContext<ServerCommandSource> ctx, ServerPlayerEntity player) {

        ServerCommandSource source= ctx.getSource();

        ServerPlayerEntity serverPlayer = player;
        if (serverPlayer == null) {
            try {
                serverPlayer = source.getPlayerOrThrow();
            } catch (Exception e) {
                source.sendError(Text.translatable("commands.qmorebodyparam.componenterror"));
                return 0;
            }
        }
        ServerPlayerEntity finalServerPlayer = serverPlayer;

        return entityComponentRegister.BLEED_COMPONENT.maybeGet(finalServerPlayer).map(bleedComponent -> {

            String duration = (bleedComponent.getBleedDuration() < 0) ? "infinite" : String.valueOf(bleedComponent.getBleedDuration());

            source.sendFeedback(() ->
                    Text.translatable("commands.qmorebodyparam.getbleed", duration, bleedComponent.getBleedIntensity(), bleedComponent.getCombo()),
                    false);
            return 1;
        }).orElseGet(() -> {
            source.sendError(
                    Text.translatable("commands.qmorebodyparam.getblooderror", finalServerPlayer.getName().getString()));
            return 0;
        });
    }

    private static int setBleed(CommandContext<ServerCommandSource> ctx, ServerPlayerEntity player, int inputDuration, int inputIntensity) {

        ServerCommandSource source= ctx.getSource();

        ServerPlayerEntity serverPlayer = player;
        if (serverPlayer == null) {
            try {
                serverPlayer = source.getPlayerOrThrow();
            } catch (Exception e) {
                source.sendError(Text.translatable("commands.qmorebodyparam.componenterror"));
                return 0;
            }
        }
        ServerPlayerEntity finalServerPlayer = serverPlayer;

        return entityComponentRegister.BLEED_COMPONENT.maybeGet(finalServerPlayer).map(bleedComponent -> {

            int intensity = (inputIntensity >= 0) ? inputIntensity : bleedComponent.getBleedIntensity();

            bleedComponent.setBleed(inputDuration, intensity);

            source.sendFeedback(() ->
                    Text.translatable("commands.qmorebodyparam.updatebleed", finalServerPlayer.getName().getString()), false);
            return 1;
        }).orElseGet(() -> {
            source.sendError(Text.translatable("commands.qmorebodyparam.getbleederror", finalServerPlayer.getName().getString()));
            return 0;
        });
    }

    private static int getCombo(CommandContext<ServerCommandSource> ctx, ServerPlayerEntity player) {

        ServerCommandSource source= ctx.getSource();

        ServerPlayerEntity serverPlayer = player;
        if (serverPlayer == null) {
            try {
                serverPlayer = source.getPlayerOrThrow();
            } catch (Exception e) {
                source.sendError(Text.translatable("commands.qmorebodyparam.componenterror"));
                return 0;
            }
        }
        ServerPlayerEntity finalServerPlayer = serverPlayer;

        return entityComponentRegister.BLEED_COMPONENT.maybeGet(finalServerPlayer).map(bleedComponent -> {

            source.sendFeedback(()->
                    Text.translatable("commands.qmorebodyparam.getbleedcombo", finalServerPlayer.getName().getString(), bleedComponent.getCombo()), false);
            return 1;
        }).orElseGet(() -> {
            source.sendError(Text.translatable("commands.qmorebodyparam.getbleederror", finalServerPlayer.getName().getString()));
            return 0;
        });
    }

    private static int setCombo(CommandContext<ServerCommandSource> ctx, ServerPlayerEntity player, int newCombo) {

        ServerCommandSource source= ctx.getSource();

        ServerPlayerEntity serverPlayer = player;
        if (serverPlayer == null) {
            try {
                serverPlayer = source.getPlayerOrThrow();
            } catch (Exception e) {
                source.sendError(Text.translatable("commands.qmorebodyparam.componenterror"));
                return 0;
            }
        }
        ServerPlayerEntity finalServerPlayer = serverPlayer;

        return entityComponentRegister.BLEED_COMPONENT.maybeGet(finalServerPlayer).map(bleedComponent -> {

            bleedComponent.setCombo(newCombo);

            source.sendFeedback(()->
                    Text.translatable("commands.qmorebodyparam.setbleedcombo", finalServerPlayer.getName().getString(), bleedComponent.getCombo()), false);
            return 1;
        }).orElseGet(() -> {
            source.sendError(Text.translatable("commands.qmorebodyparam.getbleederror", finalServerPlayer.getName().getString()));
            return 0;
        });
    }
}
