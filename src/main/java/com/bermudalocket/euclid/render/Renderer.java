package com.bermudalocket.euclid.render;

import com.bermudalocket.euclid.Euclid;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

import java.awt.Color;

public interface Renderer {

    default void translate(float tickDelta) {
        this.translate(false, tickDelta);
    }

    @SuppressWarnings("deprecation")
    default void translate(boolean back, float tickDelta) {
        Euclid.player().ifPresent(player -> {
            double dx = player.prevX + (player.getPos().getX() - player.prevX) * tickDelta;
            double dy = player.prevY + (player.getPos().getY() - player.prevY) * tickDelta;
            double dz = player.prevZ + (player.getPos().getZ() - player.prevZ) * tickDelta;
            int multiplier = back ? 1 : -1;
            RenderSystem.translated(multiplier * dx, multiplier * (dy + player.getStandingEyeHeight()), multiplier * dz);
        });
    }

    @SuppressWarnings("deprecation")
    default void startRendering() {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(GL11.GL_NOTEQUAL);
        RenderSystem.lineWidth(3f);
        RenderSystem.disableTexture();
        RenderSystem.depthMask(true);
        RenderSystem.disableFog();
        RenderSystem.disableLighting();
    }

    default void endRendering() {
        RenderSystem.depthFunc(GL11.GL_LEQUAL);
        RenderSystem.enableTexture();
    }

    default void drawLineConnectingTwoBlocks(BlockPos a, BlockPos b, Color color, float tickDelta) {
        double x1 = a.getX() + 0.5;
        double y1 = a.getY() + 0.5;
        double z1 = a.getZ() + 0.5;
        double x2 = b.getX() + 0.5;
        double y2 = b.getY() + 0.5;
        double z2 = b.getZ() + 0.5;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        this.startRendering();
        this.translate(tickDelta);
        RenderSystem.lineWidth(3);

        builder.begin(GL11.GL_LINES, VertexFormats.POSITION_COLOR);
        builder.vertex(x1, y1, z1).color(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f, color.getAlpha()/255f).next();
        builder.vertex(x2, y2, z2).color(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f, color.getAlpha()/255f).next();
        tessellator.draw();
        this.endRendering();
        this.translate(true, tickDelta);
    }

    default void drawRectangularPrismWithInset(BlockPos pos, Color color, int insetLevel, float tickDelta) {
        this.drawRectangularPrism(pos, pos, color, insetLevel, tickDelta);
    }

    default void drawRectangularPrism(BlockPos pos, Color color, float tickDelta) {
        this.drawRectangularPrism(pos, pos, color, 0, tickDelta);
    }

    default void drawRectangularPrism(BlockPos pos, BlockPos pos2, Color color, int insetLevel, float tickDelta) {
        this.drawRectangularPrism(pos, pos2, color, 3f, insetLevel, tickDelta);
    }

    default void drawRectangularPrism(BlockPos pos, BlockPos pos2, Color color, float line, int insetLevel, float tickDelta) {
        this.drawRectangularPrism(pos, pos2, color, line, null, tickDelta, insetLevel);
    }

    default void drawRectangularPrism(BlockPos pos, BlockPos pos2, Color color, float line, Runnable postInit, float tickDelta, int insetLevel) {
        pos2 = pos2.add(1, 1, 1);
        double x1 = pos.getX() + (0.08 * insetLevel);
        double y1 = pos.getY() + (0.08 * insetLevel);
        double z1 = pos.getZ() + (0.08 * insetLevel);
        double x2 = pos2.getX() - (0.08 * insetLevel);
        double y2 = pos2.getY() - (0.08 * insetLevel);
        double z2 = pos2.getZ() - (0.08 * insetLevel);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        this.startRendering();
        this.translate(tickDelta);
        if (postInit != null) {
            postInit.run();
        }
        RenderSystem.lineWidth(line);

        float red = color.getRed()/255f;
        float green = color.getGreen()/255f;
        float blue = color.getBlue()/255f;
        float alpha = color.getAlpha()/255f;
        // bottom
        builder.begin(GL11.GL_LINE_LOOP, VertexFormats.POSITION_COLOR);
        builder.vertex(x1, y1, z1).color(red, green, blue, alpha).next();
        builder.vertex(x2, y1, z1).color(red, green, blue, alpha).next();
        builder.vertex(x2, y1, z2).color(red, green, blue, alpha).next();
        builder.vertex(x1, y1, z2).color(red, green, blue, alpha).next();
        builder.vertex(x1, y1, z1).color(red, green, blue, alpha).next();
        tessellator.draw();
        // top
        builder.begin(GL11.GL_LINE_LOOP, VertexFormats.POSITION_COLOR);
        builder.vertex(x1, y2, z1).color(red, green, blue, alpha).next();
        builder.vertex(x2, y2, z1).color(red, green, blue, alpha).next();
        builder.vertex(x2, y2, z2).color(red, green, blue, alpha).next();
        builder.vertex(x1, y2, z2).color(red, green, blue, alpha).next();
        builder.vertex(x1, y2, z1).color(red, green, blue, alpha).next();
        tessellator.draw();
        // sides
        builder.begin(GL11.GL_LINES, VertexFormats.POSITION_COLOR);
        builder.vertex(x1, y1, z1).color(red, green, blue, alpha).next();
        builder.vertex(x1, y2, z1).color(red, green, blue, alpha).next();
        builder.vertex(x2, y1, z2).color(red, green, blue, alpha).next();
        builder.vertex(x2, y2, z2).color(red, green, blue, alpha).next();
        builder.vertex(x2, y1, z1).color(red, green, blue, alpha).next();
        builder.vertex(x2, y2, z1).color(red, green, blue, alpha).next();
        builder.vertex(x1, y1, z2).color(red, green, blue, alpha).next();
        builder.vertex(x1, y2, z2).color(red, green, blue, alpha).next();
        tessellator.draw();
        this.endRendering();
        this.translate(true, tickDelta);
    }

    default void drawGrid(BlockPos min, BlockPos max, Color color, Runnable postInit, float tickDelta) {
        max = max.add(1, 1, 1);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        this.translate(tickDelta);
        this.startRendering();
        if (postInit != null) {
            postInit.run();
        }
        builder.begin(GL11.GL_LINE_LOOP, VertexFormats.POSITION_COLOR);
        float red = color.getRed()/255f;
        float green = color.getGreen()/255f;
        float blue = color.getBlue()/255f;
        float alpha = color.getAlpha()/255f;

        // yz plane
        for (int x = min.getX(); x <= max.getX(); x++) {
            builder.vertex(x, min.getY(), min.getZ()).color(red, green, blue, alpha).next();
            builder.vertex(x, min.getY(), max.getZ()).color(red, green, blue, alpha).next();
            builder.vertex(x, max.getY(), max.getZ()).color(red, green, blue, alpha).next();
            builder.vertex(x, max.getY(), min.getZ()).color(red, green, blue, alpha).next();
            builder.vertex(x, min.getY(), min.getZ()).color(red, green, blue, alpha).next();
        }

        // xy plane
        for (double z = min.getZ(); z <= max.getZ(); z++) {
            builder.vertex(min.getX(), min.getY(), z).color(red, green, blue, alpha).next();
            builder.vertex(max.getX(), min.getY(), z).color(red, green, blue, alpha).next();
            builder.vertex(max.getX(), max.getY(), z).color(red, green, blue, alpha).next();
            builder.vertex(min.getX(), max.getY(), z).color(red, green, blue, alpha).next();
            builder.vertex(min.getX(), min.getY(), z).color(red, green, blue, alpha).next();
        }

        // xz plane
        for (double y = min.getY(); y <= max.getY(); y++) {
            builder.vertex(min.getX(), y, min.getZ()).color(red, green, blue, alpha).next();
            builder.vertex(max.getX(), y, min.getZ()).color(red, green, blue, alpha).next();
            builder.vertex(max.getX(), y, max.getZ()).color(red, green, blue, alpha).next();
            builder.vertex(min.getX(), y, max.getZ()).color(red, green, blue, alpha).next();
            builder.vertex(min.getX(), y, min.getZ()).color(red, green, blue, alpha).next();
        }

        tessellator.draw();
        this.endRendering();
        this.translate(true, tickDelta);
    }

}
