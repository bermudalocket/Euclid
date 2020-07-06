package com.bermudalocket.euclid.mixin;

import com.bermudalocket.euclid.Configuration;
import com.bermudalocket.euclid.DataStorage;
import com.bermudalocket.euclid.Euclid;
import com.bermudalocket.euclid.EuclidMenu;
import com.bermudalocket.euclid.logblock.LogBlockResults;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class MixinChatOut {

    @Inject(method = "sendMessage(Ljava/lang/String;Z)V", at = @At("HEAD"), cancellable = true)
    public void onPlayerChat(String msg, boolean addToChat, CallbackInfo ci) {
        // don't send euclid commands to the server
        if (msg.startsWith("/euclid") || msg.startsWith("/e ")) {
            ci.cancel();
        }

        if (msg.equalsIgnoreCase("/euclid") || msg.equalsIgnoreCase("/e")) {
            Euclid.player().ifPresent((player) -> {
                player.sendMessage(EuclidMenu.VERSION, false);
                player.sendMessage(EuclidMenu.INFO_MSG, false);
                player.sendMessage(EuclidMenu.EUCLID_MENU, false);
                player.sendMessage(EuclidMenu.LB_MENU, false);
                player.sendMessage(EuclidMenu.LB_FAVS, false);
                player.sendMessage(EuclidMenu.WE_MENU, false);
            });
        }

        // parse command
        if (isWorldEditCommand(msg)) {
            DataStorage.clearGhost();
        } else if (this.checkCommand(msg, "config")) {
            MinecraftClient.getInstance().openScreen(Configuration.INSTANCE.configScreen);
        } else if (this.checkCommand(msg, "clear")) {
            LogBlockResults.clear();
        } else if (this.checkCommand(msg, "ratio")) {
            LogBlockResults.ratio();
        } else if (this.checkCommand(msg, "pre")) {
            LogBlockResults.pre();
        }
    }

    private boolean checkCommand(String input, String subcommand) {
        return (input.startsWith("/euclid ") || input.startsWith("/e ")) && input.contains(subcommand);
    }

    private static boolean isWorldEditCommand(String msg) {
        return msg.startsWith("//sel")
            || msg.startsWith("//expand")
            || msg.startsWith("//contract")
            || msg.startsWith("//stack")
            || msg.startsWith("//inset")
            || msg.startsWith("//outset");
    }

}
