package com.bermudalocket.euclid.logblock;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

public class Result {

    public final int id;
    private final String player;
    private final Block block;
    private final BlockPos blockPos;
    private boolean visible;
    private long timestamp;

    public Result(int id, String player, Block block, BlockPos pos, long timestamp) {
        this.id = id;
        this.player = player;
        this.block = block;
        this.blockPos = pos;
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Offsets the timestamp by 1000 ms.
     */
    public void offsetTimestamp() {
        timestamp += 1000;
    }

    public String getPlayerName() {
        return player;
    }

    public Block getBlock() {
        return block;
    }

    public boolean isHidden() {
        return !visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public BlockPos getPos() {
        return this.blockPos;
    }

    public boolean overrides(Result result) {
        return blockPos == result.getPos() && timestamp > result.getTimestamp();
    }

}
