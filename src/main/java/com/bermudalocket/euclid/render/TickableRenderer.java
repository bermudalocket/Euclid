package com.bermudalocket.euclid.render;

public interface TickableRenderer extends Renderer {

    void onClientTick(float tickDelta);

}
