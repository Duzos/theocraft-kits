package com.duzo.theocraftkits;

import com.duzo.theocraftkits.commands.KitCommand;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class TheoKits implements ModInitializer {
    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register(this::registerCommands);
    }

    private void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess access, CommandManager.RegistrationEnvironment env) {
        // @TODO suggestions, but its not too big right now as theres only "spawn". So don't bother.
        dispatcher.register(
                CommandManager.literal("kit")
                        .then(CommandManager.argument("kit_name", StringArgumentType.word())
                                .executes(new KitCommand())));
    }
}
