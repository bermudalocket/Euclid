package com.bermudalocket.euclid.render;

import com.bermudalocket.euclid.logblock.LogBlockResults;
import com.bermudalocket.euclid.logblock.Result;
import com.bermudalocket.euclid.util.BlockHelper;
import com.bermudalocket.euclid.util.RGBA;

public class LogBlockRenderer implements TickableRenderer {

    public static final LogBlockRenderer INSTANCE = new LogBlockRenderer();

    private LogBlockRenderer() { }

    @Override
    public void onClientTick(float tickDelta) {
        LogBlockResults.INSTANCE.getResults().forEach((player, results) -> {
            for (Result result : results) {
                if (result.isHidden()) {
                    continue;
                }
                this.drawRectangularPrism(result.getPos(), BlockHelper.getBlockColor(result.getBlock()), tickDelta);
                LogBlockResults.INSTANCE.getPrevious(result).ifPresent(prev ->
                    this.drawLineConnectingTwoBlocks(prev.getPos(), result.getPos(), RGBA.PINK, tickDelta)
                );
            }
        });
    }

}
