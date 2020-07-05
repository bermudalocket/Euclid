package com.bermudalocket.euclid;

import com.bermudalocket.euclid.logblock.EditType;
import com.bermudalocket.euclid.logblock.InspectionType;
import com.bermudalocket.euclid.logblock.LogBlockResults;
import com.bermudalocket.euclid.logblock.Result;
import com.bermudalocket.euclid.render.LogBlockRenderer;
import com.bermudalocket.euclid.render.TickableRenderer;
import com.bermudalocket.euclid.render.WorldEditRenderer;
import com.bermudalocket.euclid.util.BlockHelper;
import com.google.common.collect.ImmutableSet;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;

public class Euclid implements ModInitializer {

    private static final Set<TickableRenderer> RENDERERS = ImmutableSet.of(
        WorldEditRenderer.INSTANCE,
        LogBlockRenderer.INSTANCE
    );

    public static void tick(float tickDelta) {
        RENDERERS.forEach(tickableRenderer -> tickableRenderer.onClientTick(tickDelta));
    }

    public static void handleChat(GameMessageS2CPacket packet) {
        String message = packet.getMessage().getString();

        Matcher regex = LogBlockResults.TOOLBLOCK_INIT_PATTERN.matcher(message);
        if (regex.find()) {
            try {
                int x = Integer.parseInt(regex.group("x"));
                int y = Integer.parseInt(regex.group("y"));
                int z = Integer.parseInt(regex.group("z"));
                BlockPos pos = new BlockPos(x, y, z);
                LogBlockResults.await(pos);
            } catch (NumberFormatException e) {
                // log
            }
            return;
        }

        regex = LogBlockResults.TOOLBLOCK_RESULT_PATTERN.matcher(message);
        if (regex.find()) {
            try {
                int month = Integer.parseInt(regex.group("month"));
                int day = Integer.parseInt(regex.group("day"));
                int hour = Integer.parseInt(regex.group("hour"));
                int minute = Integer.parseInt(regex.group("minute"));
                String player = regex.group("player");
                String action = regex.group("action");
                String blockType = regex.group("block");

                long timestamp = epochTimestampFromTimeAndDate(month, day, hour, minute);
                Block block = BlockHelper.blockTypeFromString(blockType);
                EditType editType = EditType.fromString(action.toUpperCase());

                LogBlockResults.complete(player, block, editType, timestamp);
            } catch (NumberFormatException e) {
                // log
            }
            return;
        }

        regex = LogBlockResults.ORDERED_SEARCH_RESULT_PATTERN.matcher(message);
        if (regex.find()) {
            try {
                int id = Integer.parseInt(regex.group("id"));
                int month = Integer.parseInt(regex.group("month"));
                int day = Integer.parseInt(regex.group("day"));
                int hour = Integer.parseInt(regex.group("hour"));
                int minute = Integer.parseInt(regex.group("minute"));
                String player = regex.group("player");
                String action = regex.group("action");
                String blockType = regex.group("block");
                int x = Integer.parseInt(regex.group("x"));
                int y = Integer.parseInt(regex.group("y"));
                int z = Integer.parseInt(regex.group("z"));
                System.out.println("info = " + id + month + day + hour + minute + player + action + blockType + x + y + z);

                Block block = BlockHelper.blockTypeFromString(blockType);
                BlockPos pos = new BlockPos(x, y, z);
                EditType editType = EditType.valueOf(action.toUpperCase());
                InspectionType inspectType = InspectionType.QUERY;
                long timestamp = epochTimestampFromTimeAndDate(month, day, hour, minute);

                Result result = new Result(id, player, block, pos, editType, inspectType, timestamp);

                System.out.println("result = " + result);
                LogBlockResults.addResult(result);
            } catch (NumberFormatException e) {
                // log
            }
        }
    }

    private static long epochTimestampFromTimeAndDate(int month, int day, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR, hour);
        calendar.set(Calendar.MINUTE, minute);
        return calendar.getTimeInMillis();
    }

    public static Optional<ClientPlayerEntity> getPlayer() {
        try {
            MinecraftClient client = MinecraftClient.getInstance();
            return Optional.ofNullable(client.player);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<ClientPlayNetworkHandler> networkHandler() {
        return Optional.ofNullable(MinecraftClient.getInstance().getNetworkHandler());
    }

    public static void registerCallbacks() {
        // custom payload callback
        ClientSidePacketRegistry.INSTANCE.register(new Identifier("worldedit:cui"), Euclid::handleWECUIPacket);

        // register cui packet
        Euclid.networkHandler().ifPresent((networkHandler) -> {
            networkHandler.sendPacket(new CustomPayloadC2SPacket(
                new Identifier("minecraft:register"),
                new PacketByteBuf(Unpooled.wrappedBuffer("worldedit:cui".getBytes(StandardCharsets.UTF_8)))
            ));
            networkHandler.sendPacket(new CustomPayloadC2SPacket(
                new Identifier("worldedit:cui"),
                new PacketByteBuf(Unpooled.wrappedBuffer("v|4".getBytes(StandardCharsets.UTF_8)))
            ));
        });
    }

    private static void handleWECUIPacket(PacketContext packetContext, PacketByteBuf packetByteBuf) {
        try {
            byte[] bytes = new byte[packetByteBuf.readableBytes()];
            for (int i = 0; i < packetByteBuf.readableBytes(); i++) {
                bytes[i] = packetByteBuf.getByte(i);
            }
            String msg = new String(bytes, StandardCharsets.UTF_8);
            String[] args = msg.split("\\|");
            if (args.length == 2 && args[0].equalsIgnoreCase("s")) {
                DataStorage.clearSelection();
            } else if (args.length == 6 && args[0].equalsIgnoreCase("p")) {
                int posIndex = Integer.parseInt(args[1]);
                Selection.Pos pos = posIndex == 0 ? Selection.Pos.FIRST : Selection.Pos.SECOND;
                int x = Integer.parseInt(args[2]);
                int y = Integer.parseInt(args[3]);
                int z = Integer.parseInt(args[4]);
                BlockPos blockPos = new BlockPos(x, y, z);
                DataStorage.setSelection(pos, blockPos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInitialize() {
        System.out.println("-- EUCLID: init");
    }

}
