package com.duzo.theocraftkits.commands;

import com.duzo.theocraftkits.util.IEntityDataSaver;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.sun.jdi.connect.Connector;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.command.PlaceCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.ItemScatterer;
import org.apache.logging.log4j.core.jmx.Server;

import static com.duzo.theocraftkits.util.PlayerDataUtil.canUseKits;

public class KitCommand implements Command<ServerCommandSource> {
    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();
        String kit = StringArgumentType.getString(context,"kit_name");

        // Since theres only one kit, theres no point in me coding the checks and configs for adding other kits. @TODO extra kits if needed :)
        if (!kit.equals("spawn")) {
            source.sendMessage(
                    Text.literal("Invalid kit!")
                    .fillStyle(Style.EMPTY.withColor(TextColor.fromFormatting(
                            Formatting.RED))
                            .withBold(true)
                    ));
            source.sendMessage(
                    Text.literal("Valid kit(s): spawn")
                            .fillStyle(Style.EMPTY.withColor(TextColor.fromFormatting(
                                    Formatting.RED
                            ))));
            return 0;
        }

        System.out.println(canUseKits((IEntityDataSaver) player));

        if (!canUseKits((IEntityDataSaver) player)) {
            source.sendMessage(
                    Text.literal("You are unable to use kits until your next death.")
                            .fillStyle(Style.EMPTY.withColor(TextColor.fromFormatting(
                                            Formatting.RED))
                                    .withBold(true)
                            ));
            return 0;
        }

        // Giving the player the items
        // I would make it vary depending on the kit given, but see earlier message about why I'm not.

        giveStackOrDrop(player,new ItemStack(Items.STONE_SWORD));
        giveStackOrDrop(player,new ItemStack(Items.STONE_AXE));
        giveStackOrDrop(player,new ItemStack(Items.STONE_PICKAXE));
        giveStackOrDrop(player,new ItemStack(Items.STONE_SHOVEL));


        ItemStack carrots = new ItemStack(Items.CARROT);
        carrots.setCount(64);
        giveStackOrDrop(player,carrots);

        giveStackOrDrop(player,new ItemStack(Items.LEATHER_HELMET));
        giveStackOrDrop(player,new ItemStack(Items.LEATHER_CHESTPLATE));
        giveStackOrDrop(player,new ItemStack(Items.LEATHER_LEGGINGS));
        giveStackOrDrop(player,new ItemStack(Items.LEATHER_BOOTS));

        canUseKits((IEntityDataSaver) player,false);

        // Can't do translatables, those require the mod on the clients side, just do literals.
        source.sendMessage(
                Text.literal("Successfully gave " + player.getName().getString() + " kit " + kit)
                .fillStyle(Style.EMPTY.withColor(TextColor.fromFormatting(
                        Formatting.GREEN
                ))));

        return 0;
    }

    public static void giveStackOrDrop(ServerPlayerEntity player, ItemStack stack) {
        boolean success = player.giveItemStack(stack);

        if (!success) {
            ServerWorld world = player.getServerWorld();
            world.spawnEntity(new ItemEntity(world,player.getX(),player.getY() + 1,player.getZ(),stack));
        }
    }
}
