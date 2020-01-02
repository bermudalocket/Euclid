package com.bermudalocket.euclid.util;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.DefaultedRegistry;

import java.util.HashMap;
import java.util.HashSet;

public class BlockHelper {

    public static BlockPos parseBlockPos(String x, String y, String z) {
        return new BlockPos(Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(z));
    }

    private static final HashMap<Block, RGBA> COLOR_MAP = new HashMap<>();

    private static final HashMap<Block, RGBA> HIGHEST_PRIORITY = new HashMap<>();

    private static final HashSet<Block> LOW_PRIORITY = new HashSet<>();

    static {
        LOW_PRIORITY.add(Blocks.STONE);

        HIGHEST_PRIORITY.put(Blocks.IRON_ORE, new RGBA(1.0f, 0.89411765f, 0.76862746f, 1f));
        blockColorGroup(new RGBA(0f, 1f, 1f), Blocks.DIAMOND_BLOCK, Blocks.DIAMOND_ORE);
        blockColorGroup(new RGBA(0.0f, 1.0f, 0.49803922f), Blocks.EMERALD_BLOCK, Blocks.EMERALD_ORE);
        blockColorGroup(new RGBA(1.0f, 0.84313726f, 0.0f), Blocks.GOLD_BLOCK, Blocks.GOLD_ORE);
        blockColorGroup(new RGBA(0.972549f, 0.972549f, 1.0f), Blocks.IRON_BLOCK, Blocks.NETHER_QUARTZ_ORE);
        blockColorGroup(RGBA.BLACK, Blocks.COAL_BLOCK, Blocks.COAL_ORE);
        blockColorGroup(RGBA.BLUE, Blocks.LAPIS_BLOCK, Blocks.LAPIS_ORE);
        blockColorGroup(RGBA.RED, Blocks.REDSTONE_ORE, Blocks.REDSTONE_BLOCK,
            Blocks.REDSTONE_TORCH, Blocks.REDSTONE_WIRE, Blocks.REDSTONE_WALL_TORCH,
            Blocks.POWERED_RAIL, Blocks.PISTON, Blocks.STICKY_PISTON, Blocks.REPEATER,
            Blocks.COMPARATOR);

        blockColorGroup(RGBA.RED, "RED_");
        blockColorGroup(new RGBA(1.0f, 0.64705884f, 0.0f), "ORANGE_");
        blockColorGroup(RGBA.YELLOW, "YELLOW_");
        blockColorGroup(RGBA.GREEN, "LIME_");
        blockColorGroup(new RGBA(0.0f, 0.5019608f, 0.0f), "GREEN_");
        blockColorGroup(new RGBA(0.6901961f, 0.8784314f, 0.9019608f), "LIGHT_BLUE_");
        blockColorGroup(new RGBA(0.0f, 0.0f, 0.54509807f), "DARK_BLUE_");
        blockColorGroup(RGBA.BLUE, "BLUE_");
        blockColorGroup(new RGBA(1.0f, 0.0f, 1.0f), "MAGENTA_");
        blockColorGroup(new RGBA(1.0f, 0.4117647f, 0.7058824f), "PINK_");
        blockColorGroup(new RGBA(Formatting.DARK_PURPLE.getColorValue(), 1), "PURPLE_");
        blockColorGroup(new RGBA(1.0f, 0.98039216f, 0.9411765f), "WHITE_");
        blockColorGroup(new RGBA(0.6627451f, 0.6627451f, 0.6627451f), "DARK_GRAY_");
        blockColorGroup(new RGBA(0.827451f, 0.827451f, 0.827451f), "LIGHT_GRAY_");
        blockColorGroup(new RGBA(0.5019608f, 0.5019608f, 0.5019608f), "GRAY_");
        blockColorGroup(new RGBA(0.54509807f, 0.27058825f, 0.07450981f), "BROWN_");
        blockColorGroup(new RGBA(0f, 0f, 0f, 1f), "BLACK_");
    }

    private static void blockColorGroup(RGBA color, String keyword) {
        System.out.println("Loading keyword group " + keyword);
        for (Block block : DefaultedRegistry.BLOCK) {
            if (block == null || COLOR_MAP.containsKey(block)) {
                continue;
            }
            if (block.getTranslationKey().toUpperCase().contains(keyword)) {
                COLOR_MAP.put(block, color);
                System.out.println("Block " + block + " added for keyword " + keyword);
            }
        }
    }

    private static void blockColorGroup(RGBA color, Block... blocks) {
        for (Block block : blocks) {
            HIGHEST_PRIORITY.put(block, color);
        }
    }

    public static Block blockTypeFromString(String string) {
        return DefaultedRegistry.BLOCK.get(new Identifier(string.toLowerCase()));
    }

    public static RGBA getBlockColor(Block block) {
        RGBA blockColor = null;
        if (HIGHEST_PRIORITY.containsKey(block)) {
            blockColor = HIGHEST_PRIORITY.get(block);
        }
        if (COLOR_MAP.containsKey(block)) {
            blockColor = COLOR_MAP.get(block);
        }
        if (blockColor != null) {
            if (LOW_PRIORITY.contains(block)) {
                blockColor.setOpacity(0.3f);
            }
            return blockColor;
        }
        if (blockColor == null) {
            blockColor = RGBA.YELLOW;
        }
        COLOR_MAP.put(block, blockColor);
        return blockColor;
    }

}
