package com.bermudalocket.euclid.mixin;

import net.minecraft.client.gui.screen.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ChatScreen.class)
public abstract class MixinWorldEditSelectionPreview {
/*
    @Shadow
    protected TextFieldWidget chatField;

    @Inject(method="keyPressed", at = @At("HEAD"))
    private void keyPressed(int key1, int key2, int key3, CallbackInfoReturnable ci) {
        if (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), 341)) {
            if (key1 == GLFW.GLFW_KEY_A) {
                chatField.moveCursor(-1 * chatField.getText().length());
                return;
            } else if (key1 == GLFW.GLFW_KEY_E) {
                chatField.moveCursor(999);
                return;
            } else if (key1 == GLFW.GLFW_KEY_LEFT) {
                chatField.setCursor(indexOfLastWord(chatField.getText(), chatField.getCursor()));
                return;
            }
        }
    }

    private int indexOfLastWord(String msg, int position) {
        char[] chars = msg.toCharArray();
        for (int i = position; i > 0; i--) {
            if (String.valueOf(chars[i]).equals(" ")) {
                return i;
            }
        }
        return 0;
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        try {
            String text = chatField.getText();
            if (text != null) {

            }

            String[] split = text.split(" ");
            Selection selection = DataStorage.INSTANCE.getSelection();
            if (selection.isComplete()) {
                final BlockPos pos1 = selection.getMinUnsafe();
                final BlockPos pos2 = selection.getMaxUnsafe();
                if (text.startsWith("//stack") && split.length == 3) {
                    int num = Integer.parseInt(split[1]);
                    Direction dir = Direction.fromLetter(split[2]);
                    if (dir != null) {
                        RenderTaskPool.addRenderTask(() -> GhostWireframeBuilder.stack(pos1, pos2, num, dir), RenderTask.Type.GHOST_WIREFRAME);
                    }
                } else if (text.startsWith("//shift") && split.length == 3) {
                    int num = Integer.parseInt(split[1]);
                    Direction dir = Direction.fromLetter(split[2]);
                    if (dir != null) {
                        Vec3d translate = dir.getVector().multiply(num);
                        Vec3d minShift = new Vec3d(pos1.getX(), pos1.getY(), pos1.getZ()).add(translate);
                        Vec3d maxShift = new Vec3d(pos2.getX(), pos2.getY(), pos2.getZ()).add(translate);
                        RenderTaskPool.addRenderTask(() -> WireframeBuilder.render(new BlockPos(minShift), new BlockPos(maxShift), RGBA.GRAY), RenderTask.Type.GHOST_WIREFRAME);
                    }
                } else if ((text.startsWith("//outset") || text.startsWith("//inset")) && split.length == 2) {
                    int num = Integer.parseInt(split[1]);
                    BlockPos newPos1, newPos2;
                    if (text.startsWith("//outset")) {
                        newPos1 = pos1.add(-num, -num, -num);
                        newPos2 = pos2.add(num, num, num);
                    } else if (text.startsWith("//inset")) {
                        newPos1 = pos1.add(num, num, num);
                        newPos2 = pos2.add(-num, -num, -num);
                    } else {
                        return;
                    }
                    RenderTaskPool.addRenderTask(() -> WireframeBuilder.render(newPos1, newPos2, RGBA.GRAY), RenderTask.Type.GHOST_WIREFRAME);
                } else if (text.startsWith("//expand") && split.length == 3) {
                    int num = Integer.parseInt(split[1]);
                    Direction dir = Direction.fromLetter(split[2]);
                    if (dir != null) {
                        RenderTaskPool.addRenderTask(() -> GhostWireframeBuilder.expand(num, dir), RenderTask.Type.GHOST_WIREFRAME);
                    }
                }
            }
        } catch (Exception ignored) {
            RenderTaskPool.clearRenderTasks(RenderTask.Type.GHOST_WIREFRAME);
        }
    }
*/
}
