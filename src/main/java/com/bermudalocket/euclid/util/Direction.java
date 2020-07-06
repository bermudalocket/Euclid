package com.bermudalocket.euclid.util;

import net.minecraft.util.math.Vec3d;

public enum Direction {

       UP("u", new Vec3d(0, 1, 0)),
     DOWN("d", new Vec3d(0, -1, 0)),
    NORTH("n", new Vec3d(0, 0, -1)),
    SOUTH("s", new Vec3d(0, 0, 1)),
     EAST("e", new Vec3d(1, 0, 0)),
     WEST("w", new Vec3d(-1, 0, 0));

    public final Vec3d vector;
    public final String letter;

    Direction(String letter, Vec3d vector) {
        this.vector = vector;
        this.letter = letter;
    }

    public static Direction fromLetter(String string) {
        for (Direction direction : values()) {
            if (direction.letter.equalsIgnoreCase(string)) {
                return direction;
            }
        }
        return null;
    }

}
