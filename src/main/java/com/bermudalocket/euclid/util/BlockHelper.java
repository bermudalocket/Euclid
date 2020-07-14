package com.bermudalocket.euclid.util;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DefaultedRegistry;

import java.awt.Color;
import java.util.HashMap;

public class BlockHelper {

    private static final HashMap<Block, Color> COLOR_MAP = new HashMap<>();

    private static final HashMap<Block, Color> HIGHEST_PRIORITY = new HashMap<>();

    static {
        HIGHEST_PRIORITY.put(Blocks.IRON_ORE, new Color(1.0f, 0.89411765f, 0.76862746f, 1f));

        buildHighPriorityColorGroup(new Color(0f, 1f, 1f), Blocks.DIAMOND_BLOCK, Blocks.DIAMOND_ORE);
        buildHighPriorityColorGroup(new Color(0.0f, 1.0f, 0.49803922f), Blocks.EMERALD_BLOCK, Blocks.EMERALD_ORE);
        buildHighPriorityColorGroup(new Color(1.0f, 0.84313726f, 0.0f), Blocks.GOLD_BLOCK, Blocks.GOLD_ORE);
        buildHighPriorityColorGroup(new Color(0.972549f, 0.972549f, 1.0f), Blocks.IRON_BLOCK, Blocks.NETHER_QUARTZ_ORE);
        buildHighPriorityColorGroup(Color.BLACK, Blocks.COAL_BLOCK, Blocks.COAL_ORE);
        buildHighPriorityColorGroup(Color.BLUE, Blocks.LAPIS_BLOCK, Blocks.LAPIS_ORE);
        buildHighPriorityColorGroup(Color.RED, Blocks.REDSTONE_ORE, Blocks.REDSTONE_BLOCK,
                                    Blocks.REDSTONE_TORCH, Blocks.REDSTONE_WIRE, Blocks.REDSTONE_WALL_TORCH,
                                    Blocks.POWERED_RAIL, Blocks.PISTON, Blocks.STICKY_PISTON, Blocks.REPEATER,
                                    Blocks.COMPARATOR);

        keywordColorGroup(Color.RED, "RED_");
        keywordColorGroup(new Color(1.0f, 0.64705884f, 0.0f), "ORANGE_");
        keywordColorGroup(Color.YELLOW, "YELLOW_");
        keywordColorGroup(Color.GREEN, "LIME_");
        keywordColorGroup(new Color(0.0f, 0.5019608f, 0.0f), "GREEN_");
        keywordColorGroup(new Color(0.6901961f, 0.8784314f, 0.9019608f), "LIGHT_BLUE_");
        keywordColorGroup(new Color(0.0f, 0.0f, 0.54509807f), "DARK_BLUE_");
        keywordColorGroup(new Color(0f, 0f, 1f, 1f), "BLUE_");
        keywordColorGroup(new Color(1f, 0f, 1f), "MAGENTA_");
        keywordColorGroup(new Color(1f, 0.4117647f, 0.7058824f), "PINK_");
        keywordColorGroup(new Color(150, 100, 255), "PURPLE_");
        keywordColorGroup(new Color(1, 0.98039216f, 0.9411765f), "WHITE_");
        keywordColorGroup(new Color(0.6627451f, 0.6627451f, 0.6627451f), "DARK_GRAY_");
        keywordColorGroup(new Color(0.827451f, 0.827451f, 0.827451f), "LIGHT_GRAY_");
        keywordColorGroup(new Color(0.5019608f, 0.5019608f, 0.5019608f), "GRAY_");
        keywordColorGroup(new Color(0.54509807f, 0.27058825f, 0.07450981f), "BROWN_");
        keywordColorGroup(new Color(0, 0, 0, 1), "BLACK_");
    }

    private static void keywordColorGroup(Color color, String keyword) {
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

    private static void buildHighPriorityColorGroup(Color color, Block... blocks) {
        for (Block block : blocks) {
            HIGHEST_PRIORITY.put(block, color);
        }
    }

    public static Block blockTypeFromString(String string) {
        return DefaultedRegistry.BLOCK.get(new Identifier(string.toLowerCase()));
    }

    public static Color getBlockColor(Block block) {
        if (HIGHEST_PRIORITY.containsKey(block)) {
            return HIGHEST_PRIORITY.get(block);
        } else if (COLOR_MAP.containsKey(block)) {
            return COLOR_MAP.get(block);
        }
        return Color.YELLOW;
    }

}
