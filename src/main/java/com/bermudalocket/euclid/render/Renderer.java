package com.bermudalocket.euclid.render;

import com.bermudalocket.euclid.Euclid;
import com.bermudalocket.euclid.util.RGBA;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

public interface Renderer {

    default void translate(float tickDelta) {
        this.translate(false, tickDelta);
    }

    default void translate(boolean back, float tickDelta) {
        Euclid.INSTANCE.getPlayer().ifPresent(player -> {
            double dx = player.prevX + (player.getPos().getX() - player.prevX) * tickDelta;
            double dy = player.prevY + (player.getPos().getY() - player.prevY) * tickDelta;
            double dz = player.prevZ + (player.getPos().getZ() - player.prevZ) * tickDelta;
            int multiplier = back ? 1 : -1;
            RenderSystem.translated(multiplier * dx, multiplier * (dy + player.getStandingEyeHeight()), multiplier * dz);
        });
    }

    default void startRendering() {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(GL11.GL_NOTEQUAL);
        RenderSystem.lineWidth(1.5f);
        RenderSystem.disableTexture();
        RenderSystem.depthMask(true);
        RenderSystem.disableFog();
        RenderSystem.disableLighting();
    }

    default void endRendering() {
        RenderSystem.depthFunc(GL11.GL_LEQUAL);
        RenderSystem.enableTexture();
    }

    default void drawLineConnectingTwoBlocks(BlockPos a, BlockPos b, RGBA color, float tickDelta) {
        double x1 = a.getX() + 0.5;
        double y1 = a.getY() + 0.5;
        double z1 = a.getZ() + 0.5;
        double x2 = b.getX() + 0.5;
        double y2 = b.getY() + 0.5;
        double z2 = b.getZ() + 0.5;
        BufferBuilder builder = Tessellator.getInstance().getBuffer();
        builder.begin(GL11.GL_LINE, VertexFormats.POSITION_COLOR);
        this.startRendering();
        this.translate(tickDelta);
        builder.vertex(x1, y1, z1).color(color.red(), color.green(), color.blue(), color.alpha()).next();
        builder.vertex(x2, y2, z2).color(color.red(), color.green(), color.blue(), color.alpha()).next();
        Tessellator.getInstance().draw();
        this.endRendering();
    }

    default void drawRectangularPrism(BlockPos pos, RGBA color, float tickDelta) {
        this.drawRectangularPrism(pos, pos, color, tickDelta);
    }

    default void drawRectangularPrism(BlockPos pos, BlockPos pos2, RGBA color, float tickDelta) {
        this.drawRectangularPrism(pos, pos2, color, 1.5f, tickDelta);
    }

    default void drawRectangularPrism(BlockPos pos, BlockPos pos2, RGBA color, float line, float tickDelta) {
        this.drawRectangularPrism(pos, pos2, color, line, null, tickDelta);
    }

    default void drawRectangularPrism(BlockPos pos, BlockPos pos2, RGBA color, float line, Runnable postInit, float tickDelta) {
        pos2 = pos2.add(1, 1, 1);
        double x1 = pos.getX();
        double y1 = pos.getY();
        double z1 = pos.getZ();
        double x2 = pos2.getX();
        double y2 = pos2.getY();
        double z2 = pos2.getZ();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        this.startRendering();
        this.translate(tickDelta);
        if (postInit != null) {
            postInit.run();
        }
        RenderSystem.lineWidth(line);
        // bottom
        builder.begin(GL11.GL_LINE_LOOP, VertexFormats.POSITION_COLOR);
        builder.vertex(x1, y1, z1).color(color.red(), color.green(), color.blue(), color.alpha()).next();
        builder.vertex(x2, y1, z1).color(color.red(), color.green(), color.blue(), color.alpha()).next();
        builder.vertex(x2, y1, z2).color(color.red(), color.green(), color.blue(), color.alpha()).next();
        builder.vertex(x1, y1, z2).color(color.red(), color.green(), color.blue(), color.alpha()).next();
        builder.vertex(x1, y1, z1).color(color.red(), color.green(), color.blue(), color.alpha()).next();
        tessellator.draw();
        // top
        builder.begin(GL11.GL_LINE_LOOP, VertexFormats.POSITION_COLOR);
        builder.vertex(x1, y2, z1).color(color.red(), color.green(), color.blue(), color.alpha()).next();
        builder.vertex(x2, y2, z1).color(color.red(), color.green(), color.blue(), color.alpha()).next();
        builder.vertex(x2, y2, z2).color(color.red(), color.green(), color.blue(), color.alpha()).next();
        builder.vertex(x1, y2, z2).color(color.red(), color.green(), color.blue(), color.alpha()).next();
        builder.vertex(x1, y2, z1).color(color.red(), color.green(), color.blue(), color.alpha()).next();
        tessellator.draw();
        // sides
        builder.begin(GL11.GL_LINES, VertexFormats.POSITION_COLOR);
        builder.vertex(x1, y1, z1).color(color.red(), color.green(), color.blue(), color.alpha()).next();
        builder.vertex(x1, y2, z1).color(color.red(), color.green(), color.blue(), color.alpha()).next();
        builder.vertex(x2, y1, z2).color(color.red(), color.green(), color.blue(), color.alpha()).next();
        builder.vertex(x2, y2, z2).color(color.red(), color.green(), color.blue(), color.alpha()).next();
        builder.vertex(x2, y1, z1).color(color.red(), color.green(), color.blue(), color.alpha()).next();
        builder.vertex(x2, y2, z1).color(color.red(), color.green(), color.blue(), color.alpha()).next();
        builder.vertex(x1, y1, z2).color(color.red(), color.green(), color.blue(), color.alpha()).next();
        builder.vertex(x1, y2, z2).color(color.red(), color.green(), color.blue(), color.alpha()).next();
        tessellator.draw();
        this.endRendering();
        this.translate(true, tickDelta);
    }

    default void drawGrid(BlockPos min, BlockPos max, RGBA color, Runnable postInit, float tickDelta) {
        max = max.add(1, 1, 1);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        this.translate(tickDelta);
        this.startRendering();
        if (postInit != null) {
            postInit.run();
        }
        builder.begin(GL11.GL_LINE_LOOP, VertexFormats.POSITION_COLOR);

        // yz plane
        for (int x = min.getX(); x <= max.getX(); x++) {
            builder.vertex(x, min.getY(), min.getZ()).color(color.red(), color.green(), color.blue(), color.alpha()).next();
            builder.vertex(x, min.getY(), max.getZ()).color(color.red(), color.green(), color.blue(), color.alpha()).next();
            builder.vertex(x, max.getY(), max.getZ()).color(color.red(), color.green(), color.blue(), color.alpha()).next();
            builder.vertex(x, max.getY(), min.getZ()).color(color.red(), color.green(), color.blue(), color.alpha()).next();
            builder.vertex(x, min.getY(), min.getZ()).color(color.red(), color.green(), color.blue(), color.alpha()).next();
        }

        // xy plane
        for (double z = min.getZ(); z <= max.getZ(); z++) {
            builder.vertex(min.getX(), min.getY(), z).color(color.red(), color.green(), color.blue(), color.alpha()).next();
            builder.vertex(max.getX(), min.getY(), z).color(color.red(), color.green(), color.blue(), color.alpha()).next();
            builder.vertex(max.getX(), max.getY(), z).color(color.red(), color.green(), color.blue(), color.alpha()).next();
            builder.vertex(min.getX(), max.getY(), z).color(color.red(), color.green(), color.blue(), color.alpha()).next();
            builder.vertex(min.getX(), min.getY(), z).color(color.red(), color.green(), color.blue(), color.alpha()).next();
        }

        // xz plane
        for (double y = min.getY(); y <= max.getY(); y++) {
            builder.vertex(min.getX(), y, min.getZ()).color(color.red(), color.green(), color.blue(), color.alpha()).next();
            builder.vertex(max.getX(), y, min.getZ()).color(color.red(), color.green(), color.blue(), color.alpha()).next();
            builder.vertex(max.getX(), y, max.getZ()).color(color.red(), color.green(), color.blue(), color.alpha()).next();
            builder.vertex(min.getX(), y, max.getZ()).color(color.red(), color.green(), color.blue(), color.alpha()).next();
            builder.vertex(min.getX(), y, min.getZ()).color(color.red(), color.green(), color.blue(), color.alpha()).next();
        }

        tessellator.draw();
        this.endRendering();
        this.translate(true, tickDelta);
    }

}
