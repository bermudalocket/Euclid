package com.bermudalocket.euclid.logblock;

import com.bermudalocket.euclid.util.BlockHelper;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Optional;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogBlockResults {

    public static final LogBlockResults INSTANCE = new LogBlockResults();

    private static final Calendar CALENDAR = Calendar.getInstance();

    private final HashMap<String, TreeSet<Result>> resultsByPlayer = new HashMap<>();

    private final ConcurrentLinkedQueue<BlockPos> pendingBlockPositions = new ConcurrentLinkedQueue<>();

    private Result mostRecentResult;

    // block changes in the last n days at x:y:z in world
    private static final Pattern TOOLBLOCK_INIT_PATTERN = Pattern.compile(
            "(block changes in the last ([\\d]+ [\\w]+) at)\\s([\\d-]+):([\\d-]+):([\\d-]+)\\s(in)\\s([\\w-]+):"
    );

    // 07-30 1:23:41 somePlayer1 created dirt
    private static final Pattern TOOLBLOCK_RESULT_PATTERN = Pattern.compile(
            "([\\d]+-[\\d]+)\\s([\\d]+:[\\d]+:[\\d]+)\\s([a-zA-Z0-9]+)\\s(created|destroyed)\\s([a-zA-Z_]+)"
    );

    // (1) 07-30 01:02:03 somePlayer1 created dirt at 23:4:2500
    private static final Pattern ORDERED_SEARCH_RESULT_PATTERN = Pattern.compile(
            "(\\(\\d+\\))\\s([\\d]+-[\\d]+)\\s([\\d]+:[\\d]+:[\\d]+)\\s([a-zA-Z0-9]+)\\s(created|destroyed)\\s([a-zA-Z_]+)\\s(at)\\s([-\\d]+:[-\\d]+:[-\\d]+)"
    );

    private LogBlockResults() { }

    public void parseToolBlockResultsHeader(String message) {
        Matcher matcher = TOOLBLOCK_INIT_PATTERN.matcher(message);
        if (matcher.find()) {
            int x = Integer.parseInt(matcher.group(3));
            int y = Integer.parseInt(matcher.group(4));
            int z = Integer.parseInt(matcher.group(5));
            this.await(new BlockPos(x, y, z));
        }
    }

    public void parseResult(String message) {
        Matcher matcher = (this.isWaiting() ? TOOLBLOCK_RESULT_PATTERN : ORDERED_SEARCH_RESULT_PATTERN).matcher(message);
        if (matcher.find()) {
            if (this.isWaiting()) {
                this.parseToolBlockResult(matcher);
            } else{
                this.parseSearchResults(matcher);
            }
        }
    }

    public void await(BlockPos pos) {
        this.pendingBlockPositions.add(pos);
    }

    public void complete(String player, Block block, EditType editType, long timestamp) {
        BlockPos pos = this.pendingBlockPositions.poll();
        Result result = new Result(-1, player, block, pos, editType, InspectionType.TOOLBLOCK, timestamp);
        this.addResult(result);
    }

    public boolean isWaiting() {
        return !this.pendingBlockPositions.isEmpty();
    }

    private long dateTimeToTimestamp(String date, String time) {
        String[] dateParts = date.split("-");
        String[] timeParts = time.split(":");
        int month = Integer.parseInt(dateParts[0]);
        int day = Integer.parseInt(dateParts[1]);
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);
        int second = Integer.parseInt(timeParts[2]);
        CALENDAR.set(java.util.Calendar.MONTH, month);
        CALENDAR.set(java.util.Calendar.DAY_OF_MONTH, day);
        CALENDAR.set(java.util.Calendar.HOUR, hour);
        CALENDAR.set(java.util.Calendar.MINUTE, minute);
        CALENDAR.set(java.util.Calendar.SECOND, second);
        return CALENDAR.getTimeInMillis();
    }

    public void addResult(Result result) {
        String player = result.getPlayerName();
        TreeSet<Result> playerResults = resultsByPlayer.getOrDefault(player, new TreeSet<>((e1, e2) -> (int) (e2.getTimestamp() - e1.getTimestamp())));
        if (playerResults.stream().anyMatch(otherResult -> otherResult.overrides(result))) {
            result.setVisible(false);
        }
        if (playerResults.stream().anyMatch(otherResult -> otherResult.getTimestamp() == result.getTimestamp())) {
            result.offsetTimestamp();
        }
        playerResults.add(result);
        mostRecentResult = result;
        resultsByPlayer.put(player, playerResults);
    }

    public HashMap<String, TreeSet<Result>> getResults() {
        return resultsByPlayer;
    }

    public void clear() {
        resultsByPlayer.clear();
    }

    public void pre() {
        String query = String.format(
            "/lb player %s before %s coords limit 20",
            mostRecentResult.getPlayerName(),
            timeToString(mostRecentResult.getTimestamp())
        );
        MinecraftClient.getInstance().player.sendChatMessage(query);
    }

    public void show(Predicate<Result> predicate) {
        resultsByPlayer.values().stream()
                       .flatMap(TreeSet::stream)
                       .filter(predicate)
                       .forEach(Result::show);
    }

    public void hide(Predicate<Result> predicate) {
        resultsByPlayer.values().stream()
                       .flatMap(TreeSet::stream)
                       .filter(predicate)
                       .forEach(Result::hide);
    }

    public Optional<Result> getPrevious(Result result) {
        Result prev = resultsByPlayer.get(result.getPlayerName()).lower(result);
        return prev == null ? Optional.empty()
                            : prev.isHidden() ? getPrevious(prev) : Optional.of(prev);
    }

    public void ratio() {
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

    public String timeToString(long time) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTimeInMillis(time);
        return String.format("%d.%d.%d %02d:%02d:%02d",
                             cal.get(java.util.Calendar.DAY_OF_MONTH), cal.get(java.util.Calendar.MONTH),
                             cal.get(java.util.Calendar.YEAR), cal.get(java.util.Calendar.HOUR_OF_DAY),
                             cal.get(java.util.Calendar.MINUTE), cal.get(java.util.Calendar.SECOND));
    }

    public void parseSearchResults(Matcher matcher) {
        if (!matcher.find()) {
            return;
        }
        // raw input
        String id = matcher.group(1).replace("(", "").replace(")", "");
        String time = matcher.group(3);
        String date = matcher.group(2);
        String player = matcher.group(4);
        String type = matcher.group(5);
        String blockType = matcher.group(6);
        String[] coords = matcher.group(8).split(":");

        // processed/cast input
        int idNo = Integer.parseInt(id);
        int x = Integer.parseInt(coords[0]);
        int y = Integer.parseInt(coords[1]);
        int z = Integer.parseInt(coords[2]);
        BlockPos blockPos = new BlockPos(x, y, z);
        EditType editType = EditType.fromString(type);
        long timestamp = this.dateTimeToTimestamp(time, date);
        Block destroyedBlock = BlockHelper.blockTypeFromString(blockType);

        Result result = new Result(idNo, player, destroyedBlock, blockPos, editType, InspectionType.QUERY, timestamp);
        addResult(result);
    }

    public void parseToolBlockResult(Matcher matcher)  {
        if (!matcher.find()) {
            return;
        }
        String player = matcher.group(3);
        EditType editType = EditType.fromString(matcher.group(4));
        Block block = BlockHelper.blockTypeFromString(matcher.group(5));
        long timestamp = this.dateTimeToTimestamp(matcher.group(1), matcher.group(2));
        this.complete(player, block, editType, timestamp);
    }

}
