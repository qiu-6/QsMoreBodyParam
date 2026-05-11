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

import java.util.Collection;

public class bloodLevelCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {

        dispatcher.register(CommandManager.literal("bloodlevel")
                .requires(source -> source.hasPermissionLevel(1))

                // /blood get – targets the command sender
                .then(CommandManager.literal("get")
                        .executes(ctx -> getBlood(
                                ctx,
                                null)
                        )

                        // /blood get <targets> – targets other players
                        .then(CommandManager.argument("target", EntityArgumentType.player())
                                .executes(ctx -> getBlood(
                                        ctx,
                                        EntityArgumentType.getPlayer(ctx, "target"))
                                )
                        )
                )

                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("set")

                        // /blood set <value>  – targets the command sender
                        .then(CommandManager.argument("value", IntegerArgumentType.integer(0, 20))
                                .executes(ctx -> setBlood(
                                        ctx,
                                        null,
                                        IntegerArgumentType.getInteger(ctx, "value"))
                                )
                        )

                        // /blood set <targets> <value>  – targets one or more players
                        .then(CommandManager.argument("targets", EntityArgumentType.player())
                                .then(CommandManager.argument("value", IntegerArgumentType.integer(0, 20))
                                        .executes(ctx -> setBlood(ctx,
                                                EntityArgumentType.getPlayer(ctx, "targets"),
                                                IntegerArgumentType.getInteger(ctx, "value"))
                                        )
                                )
                        )
                )
        );
    }

    private static int getBlood(CommandContext<ServerCommandSource> ctx, ServerPlayerEntity player) {

        ServerCommandSource source = ctx.getSource();

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

        return entityComponentRegister.BLOOD_COMPONENT.maybeGet(finalServerPlayer).map(blood -> {
            int level = blood.getBlood();
            source.sendFeedback(
                    () -> Text.translatable("commands.qmorebodyparam.getblood", finalServerPlayer.getName().getString(), level),
                    false
            );
            return 1;
        }).orElseGet(() -> {
            source.sendError(
                    Text.translatable("commands.qmorebodyparam.getblooderror", finalServerPlayer.getName().getString())
            );
            return 0;
        });
    }

    private static int setBlood(CommandContext<ServerCommandSource> ctx, ServerPlayerEntity player, int value) {

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

        return entityComponentRegister.BLOOD_COMPONENT.maybeGet(finalServerPlayer).map(blood -> {

            blood.setBlood(value);

            source.sendFeedback(
                    () -> Text.translatable("commands.qmorebodyparam.setblood", finalServerPlayer.getName().getString(), value), false);
            return 1;
        }).orElseGet(() -> {
            source.sendError(Text.translatable("commands.qmorebodyparam.getblooderror", finalServerPlayer.getName().getString()));
            return 0;
        });
    }
}
