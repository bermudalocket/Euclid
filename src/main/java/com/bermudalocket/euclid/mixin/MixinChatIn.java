package com.bermudalocket.euclid.mixin;

import com.bermudalocket.euclid.Euclid;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class MixinChatIn {

    @Inject(method = "onGameMessage", at = @At("RETURN"))
    public void onGameMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
        Euclid.handleChat(packet);
    }

}