package com.bermudalocket.euclid;

import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class DataStorage {

    private static final Selection selection = new Selection();

    private static final Selection ghost = new Selection();

    public static Selection getSelection() {
        return selection;
    }

    public static void setSelection(Selection.Pos index, BlockPos pos) {
        selection.setPosition(index, pos);
    }

    public static void clearSelection() {
        selection.reset();
    }

    public static Optional<Selection> getGhostIfComplete() {
        return Optional.ofNullable(ghost.isComplete() ? ghost : null);
    }

    public static void setGhostWireframe(BlockPos pos1, BlockPos pos2) {
        ghost.setPosition(Selection.Pos.FIRST, pos1);
        ghost.setPosition(Selection.Pos.SECOND, pos2);
    }

    public static void clearGhost() {
        ghost.reset();
    }

}
