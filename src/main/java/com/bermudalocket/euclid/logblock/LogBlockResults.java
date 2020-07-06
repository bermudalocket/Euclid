package com.bermudalocket.euclid.logblock;

import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Optional;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Pattern;

public class LogBlockResults {

    private static final HashMap<String, TreeSet<Result>> resultsByPlayer = new HashMap<>();

    private static class PlayerResultsFactory {

        static TreeSet<Result> create() {
            return new TreeSet<>((a, b) -> (int) (b.getTimestamp() - a.getTimestamp()));
        }
    }

    private static final ConcurrentLinkedQueue<BlockPos> pendingBlockPositions = new ConcurrentLinkedQueue<>();

    private static Result mostRecentResult;

    // block changes in the last # [timeperiod] at (x):(y):(z) in [world]
    public static final Pattern TOOLBLOCK_INIT_PATTERN = Pattern.compile(
        "Block changes in the last \\d+ [a-zA-Z]+ at (?<x>\\d+):(?<y>\\d+):(?<z>\\d+) in \\w+:"
    );

    // 07-30 1:23:41 somePlayer1 created dirt
    public static final Pattern TOOLBLOCK_RESULT_PATTERN = Pattern.compile(
        "^\\[(?<month>\\d+)-(?<day>\\d+) (?<hour>\\d+):(?<minute>\\d+)\\] (?<player>\\w+) (?<action>destroyed|created|replaced) (?<block>\\w+)"
    );

    // (1) 07-30 01:02 somePlayer1 created DIRT at 23, 4, 2500
    public static final Pattern ORDERED_SEARCH_RESULT_PATTERN = Pattern.compile(
            "\\((?<id>[0-9]+)\\) \\[(?<month>\\d+)-(?<day>\\d+) (?<hour>\\d+):(?<minute>\\d+)] (?<player>\\w+) (?<action>destroyed|created|replaced) (?<block>\\w+) at (?<x>\\d+), (?<y>\\d+), (?<z>\\d+)"
    );

    public static void await(BlockPos pos) {
        pendingBlockPositions.add(pos);
    }

    public static void complete(String player, Block block, EditType editType, long timestamp) {
        BlockPos pos = pendingBlockPositions.poll();
        Result result = new Result(-1, player, block, pos, timestamp);
        addResult(result);
    }

    public static boolean isWaiting() {
        return !pendingBlockPositions.isEmpty();
    }

    public static void addResult(Result result) {
        String player = result.getPlayerName();
        TreeSet<Result> playerResults = resultsByPlayer.getOrDefault(player, PlayerResultsFactory.create());

        // do any other results override this one?
        if (playerResults.stream().anyMatch(otherResult -> otherResult.overrides(result))) {
            result.setVisible(false);
        }

        // do any other results have the exact same epoch timestamp? this is actually very likely
        // as the logblock interface only returns HH:MM
        if (playerResults.stream().anyMatch(otherResult -> otherResult.getTimestamp() == result.getTimestamp())) {
            result.offsetTimestamp();
        }

        playerResults.add(result);
        mostRecentResult = result;
        resultsByPlayer.put(player, playerResults);

        System.out.println(resultsByPlayer);
    }

    public static HashMap<String, TreeSet<Result>> getResults() {
        return resultsByPlayer;
    }

    public static void clear() {
        resultsByPlayer.clear();
    }

    public static void pre() {
        String query = String.format(
            "/lb player %s before %s coords limit 20",
            mostRecentResult.getPlayerName(),
            timeToString(mostRecentResult.getTimestamp())
        );
        MinecraftClient.getInstance().player.sendChatMessage(query);
    }

    public static Optional<Result> getPrevious(Result result) {
        Result prev = resultsByPlayer.get(result.getPlayerName()).lower(result);
        return prev == null ? Optional.empty()
                            : prev.isHidden() ? getPrevious(prev) : Optional.of(prev);
    }

    public static void ratio() {
        if (!resultsByPlayer.isEmpty()) {
            String player = mostRecentResult.getPlayerName();
            long latest = resultsByPlayer.get(player).first().getTimestamp();
            long earliest = resultsByPlayer.get(player).last().getTimestamp();
            String latestString = timeToString(latest);
            String earliestString = timeToString(earliest);
            String query = String.format(
                "/lb player %s since %s before %s sum b block stone diamond_ore",
                player, earliestString, latestString
            );
            MinecraftClient.getInstance().player.sendChatMessage(query);
        }
    }

    public static String timeToString(long time) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTimeInMillis(time);
        return String.format("%d.%d.%d %02d:%02d:%02d",
                             cal.get(java.util.Calendar.DAY_OF_MONTH), cal.get(java.util.Calendar.MONTH),
                             cal.get(java.util.Calendar.YEAR), cal.get(java.util.Calendar.HOUR_OF_DAY),
                             cal.get(java.util.Calendar.MINUTE), cal.get(java.util.Calendar.SECOND));
    }

}
