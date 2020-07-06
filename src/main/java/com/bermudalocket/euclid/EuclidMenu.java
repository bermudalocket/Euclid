package com.bermudalocket.euclid;

import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class EuclidMenu {

    public static Style GREEN_TEXT = Style.EMPTY.withColor(Formatting.GREEN);

    // subcommands
    public static Text CLEAR = new LiteralText("[clear] ").setStyle(GREEN_TEXT
        .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new LiteralText("Clear the current LogBlock result set (/e clear).")))
        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/e clear"))
    );

    public static Text CLEAR_GHOST = new LiteralText("[clear preview] ").setStyle(Style.EMPTY.withColor(Formatting.GOLD)
        .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new LiteralText("Clear the ghost wireframe preview (/e clearpreview).")))
        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/e clearpreview"))
    );

    public static Text REMAINDER = new LiteralText("[hide] [show] [ratio] [pre]")
            .setStyle(Style.EMPTY.withColor(Formatting.GRAY));

    public static Text CONFIG = new LiteralText("[config] ")
            .setStyle(Style.EMPTY.withColor(Formatting.GREEN)
            .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new LiteralText("Open the Euclid config menu (/e config).")))
            .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/e config"))
    );

    public static Text EUCLID_MENU = new LiteralText("Euclid: ").setStyle(Style.EMPTY.withBold(true)).append(CONFIG);

    public static Text LB_MENU =
            new LiteralText("LogBlock: ").setStyle(Style.EMPTY.withBold(true))
            .append(CLEAR)
            .append(REMAINDER);

    public static Text LB_FAVS = new LiteralText("LB Shortcuts: ").setStyle(Style.EMPTY.withBold(true));

    public static Text VERSION = new LiteralText("Euclid v1.1").setStyle(Style.EMPTY.withColor(Formatting.GREEN).withBold(true));

    public static Text INFO_MSG =
            new LiteralText("Click here to report a bug on GitHub.")
                    .setStyle(Style.EMPTY.withColor(Formatting.GRAY)
                    .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new LiteralText("https://github.com/bermudalocket/Euclid")))
                    .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/bermudalocket/Euclid")));

    public static Text WE_MENU = new LiteralText("WorldEdit: ").setStyle(Style.EMPTY.withBold(true)).append(CLEAR_GHOST);
}
