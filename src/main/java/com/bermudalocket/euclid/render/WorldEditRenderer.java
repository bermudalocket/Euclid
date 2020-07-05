package com.bermudalocket.euclid.render;

import com.bermudalocket.euclid.Configuration;
import com.bermudalocket.euclid.DataStorage;
import com.bermudalocket.euclid.Selection;
import com.bermudalocket.euclid.util.RGBA;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

public class WorldEditRenderer implements TickableRenderer {

    public static final WorldEditRenderer INSTANCE = new WorldEditRenderer();

    private WorldEditRenderer() { }

    @Override
    public void onClientTick(float tickDelta) {
        Selection selection = DataStorage.getSelection();
        if (selection.isEmpty()) {
            return;
        }
        selection.getPosition(Selection.Pos.FIRST)
                 .ifPresent(pos -> this.drawRectangularPrism(pos, RGBA.GREEN, tickDelta));
        selection.getPosition(Selection.Pos.SECOND)
                 .ifPresent(pos -> this.drawRectangularPrism(pos, RGBA.BLUE, tickDelta));
        if (selection.isComplete()) {
            RGBA color = Configuration.INSTANCE.getWireframeColor();
            BlockPos min = selection.getMinUnsafe();
            BlockPos max = selection.getMaxUnsafe();
            // super hacky "depth perception"
            this.drawGrid(min, max, color, () -> RenderSystem.depthFunc(GL11.GL_LESS), tickDelta);
            RenderSystem.clearDepth(1.0);
            this.drawGrid(min, max, color.setAlpha(color.alpha()/2f), () -> RenderSystem.depthFunc(GL11.GL_GREATER), tickDelta);
            RenderSystem.clearDepth(1.0);
            this.drawRectangularPrism(min, max, color, 3f, () -> RenderSystem.depthFunc(GL11.GL_LESS), tickDelta);
            RenderSystem.clearDepth(1.0);
            this.drawRectangularPrism(min, max, color.setAlpha(color.alpha()/2f), 3f, () -> RenderSystem.depthFunc(GL11.GL_GREATER), tickDelta);
        }

        DataStorage.getGhostIfComplete().ifPresent((ghost) ->
            this.drawGrid(ghost.getMinUnsafe(), ghost.getMaxUnsafe(), RGBA.GRAY, null, tickDelta)
        );
    }

}
