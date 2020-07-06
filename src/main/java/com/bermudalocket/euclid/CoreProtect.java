package com.bermudalocket.euclid;

import java.util.regex.Pattern;

public class CoreProtect {

    public static final Pattern INSPECTION_BEGAN = Pattern.compile("CoreProtect - Inspector now enabled\\.");

    public static final Pattern INSPECTION_ENDED = Pattern.compile("CoreProtect - Inspector now disabled\\.");

    public static final Pattern INSPECTION_HEADER = Pattern.compile("^----- CoreProtect ----- \\(x(?<x>[0-9]+)\\/y(?<y>[0-9]+)\\/z(?<z>[0-9]+)\\)$");

    public static final Pattern INSPECTION_RESULT = Pattern.compile("^(?<timeamount>\\d+.\\d+)\\/(?<timedescriptor>[a-z]) ago - (?<player>.+) (?<action>broke|placed) (?<block>.+)\\.$");

}
