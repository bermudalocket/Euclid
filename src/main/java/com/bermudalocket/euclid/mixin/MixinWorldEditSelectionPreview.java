package com.bermudalocket.euclid.mixin;

import com.bermudalocket.euclid.DataStorage;
import com.bermudalocket.euclid.Selection;
import com.bermudalocket.euclid.render.WorldEditRenderer;
import com.bermudalocket.euclid.util.Direction;
import com.bermudalocket.euclid.util.RGBA;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
public abstract class MixinWorldEditSelectionPreview {

    @Shadow
    protected TextFieldWidget chatField;

    @Inject(method = "render", at = @At("HEAD"))
    private void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        try {
            String text = chatField.getText();
            if (text == null) {
                return;
            }
            String[] split = text.split(" ");
            Selection selection = DataStorage.getSelection();
            if (selection.isComplete()) {
                final BlockPos pos1 = selection.getMinUnsafe();
                final BlockPos pos2 = selection.getMaxUnsafe();
                if (text.startsWith("//shift") && split.length == 3) {
                    int num = Integer.parseInt(split[1]);
                    Direction dir = Direction.fromLetter(split[2]);
                    if (dir != null) {
                        Vec3d translate = dir.vector.multiply(num);
                        Vec3d minShift = new Vec3d(pos1.getX(), pos1.getY(), pos1.getZ()).add(translate);
                        Vec3d maxShift = new Vec3d(pos2.getX(), pos2.getY(), pos2.getZ()).add(translate);
                        //WorldEditRenderer.INSTANCE.drawGrid(minShift, );
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
                    WorldEditRenderer.INSTANCE.drawGrid(newPos1, newPos2, RGBA.GRAY, null, delta);
                } else if (text.startsWith("//expand") && split.length == 3) {
                    int num = Integer.parseInt(split[1]);
                    Direction dir = Direction.fromLetter(split[2]);
                    if (dir != null) {
                        BlockPos ghostFloor, ghostCeil;
                        switch (dir) {

                            // https://gamepedia.cursecdn.com/minecraft_gamepedia/5/51/Coordinates.png

                            case UP:
                                int highestY = (pos1.getY() > pos2.getY() ? pos1 : pos2).getY();
                                ghostFloor = new BlockPos(pos1.getX(), highestY, pos1.getZ());
                                ghostCeil = new BlockPos(pos2.getX(), highestY + num, pos2.getZ());
                                break;

                            case DOWN:
                                int lowestY = (pos1.getY() < pos2.getY() ? pos1 : pos2).getY();
                                ghostFloor = new BlockPos(pos1.getX(), lowestY, pos1.getZ());
                                ghostCeil = new BlockPos(pos2.getX(), lowestY - num, pos2.getZ());
                                break;

                            case NORTH:
                                int biggestZ = (pos1.getZ() > pos2.getZ() ? pos1 : pos2).getZ();
                                ghostFloor = new BlockPos(pos1.getX(), pos1.getY(), biggestZ);
                                ghostCeil = new BlockPos(pos2.getX(), pos2.getY(), biggestZ + num);
                                break;

                            case SOUTH:
                                // smallest z
                                int smallestZ = (pos1.getZ() > pos2.getZ() ? pos2 : pos1).getZ();
                                ghostFloor = new BlockPos(pos1.getX(), pos1.getY(), smallestZ);
                                ghostCeil = new BlockPos(pos2.getX(), pos2.getY(), smallestZ - num);
                                break;

                            case EAST:
                                // biggest X
                                int biggestX = (pos1.getX() > pos2.getX() ? pos1 : pos2).getX();
                                ghostFloor = new BlockPos(biggestX, pos1.getY(), pos1.getZ());
                                ghostCeil = new BlockPos(biggestX + num, pos2.getY(), pos2.getZ());
                                break;

                            case WEST:
                                // smallest X
                                int smallestX = (pos1.getX() > pos2.getX() ? pos2 : pos1).getX();
                                ghostFloor = new BlockPos(smallestX, pos1.getY(), pos1.getZ());
                                ghostCeil = new BlockPos(smallestX - num, pos2.getY(), pos2.getZ());
                                break;

                            default:
                                return;
                        }
                        DataStorage.setGhostWireframe(ghostFloor, ghostCeil);
                    }
                }
            }
        } catch (NumberFormatException e) {
            // probably just changing around the args
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
