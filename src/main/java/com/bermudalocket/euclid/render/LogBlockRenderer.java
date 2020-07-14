package com.bermudalocket.euclid.render;

import com.bermudalocket.euclid.logblock.LogBlockResults;
import com.bermudalocket.euclid.logblock.Result;

import java.awt.Color;

public class LogBlockRenderer implements TickableRenderer {

    public static final LogBlockRenderer INSTANCE = new LogBlockRenderer();

    private LogBlockRenderer() { }

    private static Color PINK = new Color(255, 145, 255, 128);

    @Override
    public void onClientTick(float tickDelta) {
        LogBlockResults.getResults().forEach((player, results) -> {
            for (Result result : results) {
                if (result.blockPos == null) {
                    continue;
                }
                int overrideLevel = results.stream().filter(otherResult -> otherResult.overrides(result)).mapToInt(e -> 1).sum();
                this.drawRectangularPrismWithInset(result.blockPos, result.editType.color, overrideLevel, tickDelta);
                LogBlockResults.getPrevious(result)
                               .filter(p -> p.blockPos != result.blockPos)
                               .ifPresent(prev -> this.drawLineConnectingTwoBlocks(prev.blockPos, result.blockPos, PINK, tickDelta));
            }
        });
    }

}
