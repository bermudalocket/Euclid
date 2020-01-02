package com.bermudalocket.euclid.mixin;

import com.bermudalocket.euclid.Configuration;
import com.bermudalocket.euclid.DataStorage;
import com.bermudalocket.euclid.DelayedTaskPool;
import com.bermudalocket.euclid.Euclid;
import com.bermudalocket.euclid.EuclidMenu;
import com.bermudalocket.euclid.Selection;
import com.bermudalocket.euclid.logblock.EditType;
import com.bermudalocket.euclid.logblock.InspectionType;
import com.bermudalocket.euclid.logblock.LogBlockResults;
import com.bermudalocket.euclid.logblock.Result;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class MixinChatOut {

    @Inject(method = "sendMessage(Ljava/lang/String;Z)V", at = @At("HEAD"), cancellable = true)
    public void onPlayerChat(String msg, boolean addToChat, CallbackInfo ci) {
        if (!Euclid.INSTANCE.getPlayer().isPresent()) {
            return;
        }
        ClientPlayerEntity player = Euclid.INSTANCE.getPlayer().get();
        // don't send euclid commands to the server
        if (msg.startsWith("/euclid ") || msg.startsWith("/e ")) {
            ci.cancel();
        }

        if (msg.equalsIgnoreCase("/euclid") || msg.equalsIgnoreCase("/e")) {
            Euclid.INSTANCE.sendEuclidMenu();
            player.sendMessage(EuclidMenu.EUCLID_MENU);
            player.sendMessage(EuclidMenu.LB_MENU);
            player.sendMessage(EuclidMenu.LB_FAVS);
            player.sendMessage(EuclidMenu.WE_MENU);
            if (!ci.isCancelled()) {
                ci.cancel();
            }
        }

        // parse command
        if (msg.contains("/region select") || msg.contains("/rg sel")) {
            DelayedTaskPool.add(5, () -> player.sendChatMessage("/we cui"));

        } else if (isWorldEditCommand(msg)) {
            //RenderTaskPool.clearRenderTasks(RenderTask.Type.GHOST_WIREFRAME);

        } else if (this.checkCommand(msg, "test")) {
            DataStorage.INSTANCE.setSelection(Selection.Pos.FIRST, new BlockPos(0, 40, 0));
            DataStorage.INSTANCE.setSelection(Selection.Pos.SECOND, new BlockPos(20, 100, 20));
            LogBlockResults.INSTANCE.addResult(new Result(1, "bermudalocket", new Block(Block.Settings.of(Material.REDSTONE_LAMP)), new BlockPos(20, 70, 20),
                                                          EditType.DESTROYED, InspectionType.TOOLBLOCK, System.currentTimeMillis()));
            LogBlockResults.INSTANCE.addResult(new Result(1, "bermudalocket", new Block(Block.Settings.of(Material.REDSTONE_LAMP)), new BlockPos(22, 72, 22),
                                                          EditType.DESTROYED, InspectionType.TOOLBLOCK, System.currentTimeMillis()-1000*30));

        } else if (this.checkCommand(msg, "config")) {
            MinecraftClient.getInstance().openScreen(Configuration.INSTANCE.getConfigScreen());

        } else if (this.checkCommand(msg, "clear")) {
            LogBlockResults.INSTANCE.clear();

        } else if (this.checkCommand(msg, "ratio")) {
            LogBlockResults.INSTANCE.ratio();

        } else if (this.checkCommand(msg, "pre")) {
            LogBlockResults.INSTANCE.pre();

        } else if (this.checkCommand(msg, "clearpreview")) {
            //RenderTaskPool.clearRenderTasks(RenderTask.Type.GHOST_WIREFRAME);

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
