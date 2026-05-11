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

public class thirstLevelCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {

        dispatcher.register(CommandManager.literal("thirstlevel")
                .requires(source -> source.hasPermissionLevel(1))

                // /thirst get – targets the command sender
                .then(CommandManager.literal("get")
                        .executes(ctx -> getThirst(
                                ctx,
                                null)
                        )

                        // /thirst get <targets> – targets other players
                        .then(CommandManager.argument("target", EntityArgumentType.player())
                                .executes(ctx -> getThirst(
                                        ctx,
                                        EntityArgumentType.getPlayer(ctx, "target"))
                                )
                        )
                )

                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("set")

                        // /thirst set <value>  – targets the command sender
                        .then(CommandManager.argument("value", IntegerArgumentType.integer(0, 20))
                                .executes(ctx -> setThirst(
                                        ctx,
                                        null,
                                        IntegerArgumentType.getInteger(ctx, "value"))
                                )
                        )

                        // /thirst set <targets> <value>  – targets one or more players
                        .then(CommandManager.argument("targets", EntityArgumentType.player())
                                .then(CommandManager.argument("value", IntegerArgumentType.integer(0, 20))
                                        .executes(ctx -> setThirst(ctx,
                                                EntityArgumentType.getPlayer(ctx, "targets"),
                                                IntegerArgumentType.getInteger(ctx, "value"))
                                        )
                                )
                        )
                )
        );
    }

    private static int getThirst(CommandContext<ServerCommandSource> ctx, ServerPlayerEntity player) {

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

        return entityComponentRegister.THIRST_COMPONENT.maybeGet(finalServerPlayer).map(thirst -> {
            int level = thirst.getThirst();
            source.sendFeedback(
                    () -> Text.translatable("commands.qmorebodyparam.getthirst", finalServerPlayer.getName().getString(), level),
                    false
            );
            return 1;
        }).orElseGet(() -> {
            source.sendError(
                    Text.translatable("commands.qmorebodyparam.getthirsterror", finalServerPlayer.getName().getString())
            );
            return 0;
        });
    }

    private static int setThirst(CommandContext<ServerCommandSource> ctx, ServerPlayerEntity player, int value) {

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

        return entityComponentRegister.THIRST_COMPONENT.maybeGet(finalServerPlayer).map(thirst -> {

            thirst.setThirst(value);

            source.sendFeedback(
                    () -> Text.translatable("commands.qmorebodyparam.setthirst", finalServerPlayer.getName().getString(), value), false);
            return 1;
        }).orElseGet(() -> {
            source.sendError(Text.translatable("commands.qmorebodyparam.getthirsterror", finalServerPlayer.getName().getString()));
            return 0;
        });
    }
}
