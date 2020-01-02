package com.bermudalocket.euclid.mixin;

import com.bermudalocket.euclid.Configuration;
import com.bermudalocket.euclid.DataStorage;
import com.bermudalocket.euclid.DelayedTaskPool;
import com.bermudalocket.euclid.Euclid;
import com.bermudalocket.euclid.EuclidMenu;
import io.netty.buffer.Unpooled;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.packet.GameJoinS2CPacket;
import net.minecraft.server.network.packet.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.charset.StandardCharsets;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class MixinOnJoinGame {

    @Inject(method = "onGameJoin", at = @At("RETURN"))
    private void onJoinGame(GameJoinS2CPacket packetIn, CallbackInfo ci) {
        // general init
        Euclid.INSTANCE.onJoinGame();
        Euclid.INSTANCE.getPlayer().ifPresent(player -> {
            // let the server know we want worldedit:cui packets
            player.networkHandler.sendPacket(
                new CustomPayloadC2SPacket(
                    new Identifier("minecraft:register"),
                    new PacketByteBuf(Unpooled.wrappedBuffer("worldedit:cui".getBytes(StandardCharsets.UTF_8)))
                )
            );
            PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
            buffer.writeString("v|4");
            player.networkHandler.sendPacket(
                    new CustomPayloadC2SPacket(new Identifier("worldedit:cui"), buffer)
            );

            // handshake
            if (Configuration.INSTANCE.SEND_INIT_HANDSHAKE_AUTOMATICALLY) {
                player.sendChatMessage("/we cui");
                DelayedTaskPool.add(40, () -> {
                    if (DataStorage.INSTANCE.isWorldEditFound()) {
                        Euclid.INSTANCE.sendEuclidMenu();
                    } else {
                        player.sendMessage(EuclidMenu.ERROR_MSG);
                    }
                });
            }

        });
    }



}
