package com.bermudalocket.euclid;

import com.bermudalocket.euclid.render.LogBlockRenderer;
import com.bermudalocket.euclid.render.TickableRenderer;
import com.bermudalocket.euclid.render.WorldEditRenderer;
import com.bermudalocket.euclid.util.BlockHelper;
import com.google.common.collect.ImmutableSet;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Set;

public class Euclid implements ModInitializer {

    public static Euclid INSTANCE;

    private MinecraftClient minecraftClient;

    private static final Set<TickableRenderer> RENDERERS = ImmutableSet.of(
        WorldEditRenderer.INSTANCE,
        LogBlockRenderer.INSTANCE
    );

    public Euclid() {
        INSTANCE = this;
    }

    public static void tick(float tickDelta) {
        RENDERERS.forEach(tickableRenderer -> tickableRenderer.onClientTick(tickDelta));
    }

    public Optional<ClientPlayerEntity> getPlayer() {
        try {
            return Optional.ofNullable(this.getMinecraftClient().player);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public MinecraftClient getMinecraftClient() {
        if (this.minecraftClient == null) {
            this.minecraftClient = MinecraftClient.getInstance();
        }
        return this.minecraftClient;
    }

    public void messagePlayer(String msg) {
        this.getPlayer().ifPresent(player -> {
            player.sendMessage(new LiteralText(msg).setStyle(new Style().setColor(Formatting.GRAY)));
        });
    }

    public void sendEuclidMenu() {
        this.getPlayer().ifPresent(player -> {
            player.sendMessage(EuclidMenu.VERSION);
            player.sendMessage(EuclidMenu.INFO_MSG);
        });
    }

    public void onJoinGame() {
        // client tick callback
        ClientTickCallback.EVENT.register(client -> {
            ClientPlayerEntity player = client.player;
            if (player != null) {
                DelayedTaskPool.tickAll();
                DataStorage.INSTANCE.updatePlayerState(player);
            }
        });

        // custom payload callback
        ClientSidePacketRegistry.INSTANCE.register(new Identifier("worldedit:cui"), (packetContext, packetByteBuf) -> {
            byte[] bytes = new byte[packetByteBuf.readableBytes()];
            for (int i = 0; i < packetByteBuf.readableBytes(); i++) {
                bytes[i] = packetByteBuf.getByte(i);
            }
            try {
                String msg = new String(bytes, StandardCharsets.UTF_8);
                String[] args = msg.split("\\|");
                if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("s")) {
                        DataStorage.INSTANCE.clearSelection();
                    }
                }
                if (args.length == 6) {
                    if (args[0].equalsIgnoreCase("p")) {
                        int posIndex = Integer.parseInt(args[1]); // can be 0 or 1
                        Selection.Pos pos = Selection.Pos.fromOrdinal(posIndex);
                        BlockPos blockPos = BlockHelper.parseBlockPos(args[2], args[3], args[4]);
                        DataStorage.INSTANCE.setSelection(pos, blockPos);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onInitialize() { }

}
