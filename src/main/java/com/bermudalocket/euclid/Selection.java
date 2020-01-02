package com.bermudalocket.euclid;

import com.bermudalocket.euclid.util.RGBA;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;
import java.util.function.BiFunction;

public class Selection {

    public enum Pos {
        FIRST(RGBA.BLUE),
        SECOND(RGBA.GREEN);

        private RGBA color;

        Pos(RGBA color) {
            this.color = color;
        }

        public RGBA getColor() {
            return this.color;
        }

        public static Pos fromOrdinal(int i) {
            for (Pos pos : values()) {
                if (pos.ordinal() == i) {
                    return pos;
                }
            }
            return null;
        }

    }

    private BlockPos[] pos = new BlockPos[2];

    public Selection() { }

    public Optional<BlockPos> getPosition(Pos index) {
        return Optional.ofNullable(this.pos[index.ordinal()]);
    }

    public void setPosition(Pos index, BlockPos pos) {
        this.pos[index.ordinal()] = pos;
    }

    public BlockPos getMinUnsafe() {
        return this.getRelative(Math::min);
    }

    public BlockPos getMaxUnsafe() {
        return this.getRelative(Math::max);
    }

    public Optional<BlockPos> getMin() {
        return Optional.ofNullable(this.getRelative(Math::min));
    }

    public Optional<BlockPos> getMax() {
        return Optional.ofNullable(this.getRelative(Math::max));
    }

    private BlockPos getRelative(BiFunction<Integer, Integer, Integer> comparison) {
        if (!this.isComplete()) return null;
        BlockPos pos1 = pos[0];
        BlockPos pos2 = pos[1];
        return new BlockPos(
            comparison.apply(pos1.getX(), pos2.getX()),
            comparison.apply(pos1.getY(), pos2.getY()),
            comparison.apply(pos1.getZ(), pos2.getZ())
        );
    }

    public boolean isComplete() {
        return this.pos[0] != null && this.pos[1] != null;
    }

    public boolean isEmpty() {
        return this.pos[0] == null && this.pos[1] == null;
    }

    public void reset() {
        this.pos[0] = null;
        this.pos[1] = null;
    }

}
