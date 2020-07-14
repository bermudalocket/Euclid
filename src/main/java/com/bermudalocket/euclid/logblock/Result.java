package com.bermudalocket.euclid.logblock;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

public class Result {

    public final int id;
    public final String player;
    public final Block block;
    public final BlockPos blockPos;
    public boolean visible;
    public long timestamp;
    public final EditType editType;

    public Result(int id, String player, Block block, BlockPos pos, long timestamp, EditType editType) {
        this.id = id;
        this.player = player;
        this.block = block;
        this.blockPos = pos;
        this.timestamp = timestamp;
        this.visible = true;
        this.editType = editType;
    }

    // offsets this timestamp by 1000ms
    public void offsetTimestamp() {
        timestamp -= 1;
    }

    public boolean overrides(Result result) {
        return this.blockPos == result.blockPos && this.timestamp > result.timestamp;
    }

    @Override
    public String toString() {
        return "Result{" +
                "id=" + id +
                ", player='" + player + '\'' +
                ", block=" + block +
                ", blockPos=" + blockPos +
                ", visible=" + visible +
                ", timestamp=" + timestamp +
                ", editType=" + editType +
                '}';
    }
}
