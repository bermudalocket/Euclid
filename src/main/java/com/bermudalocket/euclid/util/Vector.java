package com.bermudalocket.euclid.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Vector {

    public static final Vec3d HALF_UNIT = new Vec3d(0.5, 0.5, 0.5);

    private final int _x, _y, _z;

    public Vector(int x, int y, int z) {
        _x = x;
        _y = y;
        _z = z;
    }

    public Vector(BlockPos pos) {
        _x = pos.getX();
        _y = pos.getY();
        _z = pos.getZ();
    }

    public Vector(Vec3d vec) {
        _x = (int) vec.x;
        _y = (int) vec.y;
        _z = (int) vec.z;
    }

    @Override
    public String toString() {
        return "(" + _x + ", " + _y + ", " + _z + ")";
    }

    public Vector add(Vector vector) {
        return new Vector(_x + vector._x, _y + vector._y, _z + vector._z);
    }

    public Vector add(int i) {
        return new Vector(_x + i, _y + i, _z + i);
    }

    public Vector add(int x, int y, int z) {
        return new Vector(_x + x, _y + y, _z + z);
    }

    public Vector scale(int scale) {
        return new Vector(_x * scale, _y * scale, _z * scale);
    }

    public BlockPos toBlockPos() {
        return new BlockPos(_x, _y, _z);
    }

    public int x() {
        return _x;
    }

    public int y() {
        return _y;
    }

    public int z() {
        return _z;
    }

}
