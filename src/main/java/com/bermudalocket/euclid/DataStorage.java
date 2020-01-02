package com.bermudalocket.euclid;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.BlockPos;

public class DataStorage {

    public static final DataStorage INSTANCE = new DataStorage();

    private boolean worldEditFound = true;
    private ClientPlayerEntity player;
    private final Selection selection = new Selection();

    private DataStorage() {
    }

    public ClientPlayerEntity getPlayer() {
        return this.player;
    }

    public void updatePlayerState(ClientPlayerEntity player) {
        this.player = player;
    }

    public void setSelection(Selection.Pos index, BlockPos pos) {
        this.selection.setPosition(index, pos);
    }

    public Selection getSelection() {
        return this.selection;
    }

    public void clearSelection() {
        this.selection.reset();
    }

    public boolean isWorldEditFound() {
        return worldEditFound;
    }

    public void setWorldEditFound(boolean worldEditFound) {
        this.worldEditFound = worldEditFound;
    }

}
