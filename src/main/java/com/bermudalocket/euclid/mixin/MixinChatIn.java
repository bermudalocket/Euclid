package com.bermudalocket.euclid.mixin;

import com.bermudalocket.euclid.DataStorage;
import com.bermudalocket.euclid.logblock.LogBlockResults;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.packet.ChatMessageS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class MixinChatIn {

    @Inject(method = "onChatMessage", at = @At("RETURN"))
    public void onChatMessage(ChatMessageS2CPacket packet, CallbackInfo ci) {
        String msg = packet.getMessage().getString().toLowerCase();
        if (msg.contains("/we cui<--")) {
            DataStorage.INSTANCE.setWorldEditFound(false);
        } else {
            if (msg.contains("block changes in the last") && !msg.contains("selection")) {
                LogBlockResults.INSTANCE.parseToolBlockResultsHeader(msg);
            } else if (msg.contains("destroyed") || msg.contains("created")) {
                LogBlockResults.INSTANCE.parseResult(msg);
            }
        }
    }

}
