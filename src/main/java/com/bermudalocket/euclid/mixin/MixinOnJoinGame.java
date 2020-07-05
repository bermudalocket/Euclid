package com.bermudalocket.euclid.mixin;

import com.bermudalocket.euclid.Euclid;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class MixinOnJoinGame {

    @Inject(method = "onGameJoin", at = @At("RETURN"))
    private void onJoinGame(GameJoinS2CPacket packetIn, CallbackInfo ci) {
        Euclid.registerCallbacks();
    }

}
