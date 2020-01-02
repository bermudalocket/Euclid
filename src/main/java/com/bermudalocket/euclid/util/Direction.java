package com.bermudalocket.euclid.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.function.BiFunction;

public enum Direction {

       UP("u", (b, i) -> new BlockPos[]{b[0], b[1].up(i)}, new Vec3d(0, 1, 0)),
     DOWN("d", (b, i) -> new BlockPos[]{b[0].down(i), b[1]}, new Vec3d(0, -1, 0)),
    NORTH("n", (b, i) -> new BlockPos[]{b[0].north(i), b[1]}, new Vec3d(0, 0, -1)),
    SOUTH("s", (b, i) -> new BlockPos[]{b[0], b[1].south(i)}, new Vec3d(0, 0, 1)),
     EAST("e", (b, i) -> new BlockPos[]{b[0], b[1].east(i)}, new Vec3d(1, 0, 0)),
     WEST("w", (b, i) -> new BlockPos[]{b[0].west(i), b[1]}, new Vec3d(-1, 0, 0));

    private Vec3d _vector;
    private String _letter;
    private BiFunction<BlockPos[], Integer, BlockPos[]> _translator;

    Direction(String letter, BiFunction<BlockPos[], Integer, BlockPos[]> translator, Vec3d vector) {
        _vector = vector;
        _letter = letter;
        _translator = translator;
    }

    public Vec3d getVector() {
        return _vector;
    }

    public Vector getBasis() {
        return new Vector(_vector);
    }

    public BlockPos[] translate(BlockPos[] minMax, int n) {
        return _translator.apply(minMax, n);
    }

    public String getLetter() {
        return _letter;
    }

    public static Direction fromLetter(String string) {
        for (Direction direction : values()) {
            if (direction.getLetter().equalsIgnoreCase(string)) {
                return direction;
            }
        }
        return null;
    }

}
