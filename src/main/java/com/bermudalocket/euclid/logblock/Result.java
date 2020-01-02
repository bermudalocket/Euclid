package com.bermudalocket.euclid.logblock;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

public class Result {

    private int _id;
    private final String _player;
    private final Block _block;
    private final BlockPos _pos;
    private final EditType _editType;
    private final InspectionType _inspectionType;
    private boolean _visible = true;
    private long _timestamp;

    public Result(int id, String player, Block block, BlockPos pos, EditType editType, InspectionType inspectType, long timestamp) {
        _id = id;
        _player = player;
        _block = block;
        _pos = pos;
        _editType = editType;
        _inspectionType = inspectType;
        _timestamp = timestamp;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    public long getTimestamp() {
        return _timestamp;
    }

    public void offsetTimestamp() {
        _timestamp += 1000 + Math.random()*1000;
    }

    public String getPlayerName() {
        return _player;
    }

    public Block getBlock() {
        return _block;
    }

    public boolean isHidden() {
        return !_visible;
    }

    public void setVisible(boolean visible) {
        _visible = visible;
    }

    public void hide() {
        _visible = false;
    }

    public void show() {
        _visible = true;
    }

    public BlockPos getPos() {
        return _pos;
    }

    public EditType getEditType() {
        return _editType;
    }

    public InspectionType getInspectionType() {
        return _inspectionType;
    }

    @Override
    public String toString() {
        return String.format("[%d | %s %s %s at %s at %s",
            _id,
            _player,
            EditType.getDesc(_editType),
            _block,
            _pos.getX() + ", " + _pos.getY() + ", " + _pos.getZ() + ")",
            _timestamp);
    }

    public boolean overrides(Result result) {
        return _pos == result.getPos() && _timestamp > result.getTimestamp();
    }

}
