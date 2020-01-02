package com.bermudalocket.euclid;

import com.bermudalocket.euclid.util.StyledTextBuilder;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class EuclidMenu {

    // subcommands
    public static Text CLEAR = new LiteralText("[clear] ").setStyle(new Style()
        .setColor(Formatting.GREEN)
        .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new LiteralText("Clear the current LogBlock result set (/e clear).")))
        .setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/e clear"))
    );

    public static Text CLEAR_GHOST = new LiteralText("[clear preview] ").setStyle(new Style()
        .setColor(Formatting.GOLD)
        .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new LiteralText("Clear the ghost wireframe preview (/e clearpreview).")))
        .setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/e clearpreview"))
    );

    public static Text REMAINDER = new LiteralText("[hide] [show] [ratio] [pre]")
            .setStyle(new Style().setColor(Formatting.GRAY).setBold(false));

    public static Text CONFIG = new LiteralText("[config] ")
            .setStyle(new Style()
            .setColor(Formatting.GREEN)
            .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new LiteralText("Open the Euclid config menu (/e config).")))
            .setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/e config"))
    );

    public static Text EUCLID_MENU =
            new LiteralText("Euclid: ").setStyle(new Style().setBold(true))
            .append(CONFIG);

    public static Text LB_MENU =
            new LiteralText("LogBlock: ").setStyle(new Style().setBold(true))
            .append(CLEAR)
            .append(REMAINDER);

    public static Text LB_FAVS = new StyledTextBuilder("LB Shortcuts: ").bold(true).build();

    public static Text DIVIDER = new StyledTextBuilder("-----------------------------------------------------")
            .build();

    public static Text VERSION =
            new StyledTextBuilder("Euclid v1.0 Beta 6")
                    .color(Formatting.LIGHT_PURPLE)
                    .bold(true)
                    .build();

    public static Text INFO_MSG =
            new LiteralText("Click here to report a bug on GitHub.")
                    .setStyle(new Style()
                    .setColor(Formatting.GRAY)
                    .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new LiteralText("https://github.com/bermudalocket/Euclid")))
                    .setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/bermudalocket/Euclid")));

    public static final Text ERROR_MSG =
            new StyledTextBuilder("Error: WorldEdit not found. Some Euclid features will not work.")
                    .color(Formatting.RED)
                    .build();

    static {
        for (int i = 1; i <= 5; i++) {
            LB_FAVS.append(new StyledTextBuilder("[" + i + "] ").color(Formatting.GRAY).onHover(HoverEvent.Action.SHOW_TEXT, "/lb sel time 1d coords").build());
        }
    }

    public static Text WE_MENU =
            new LiteralText("WorldEdit: ").setStyle(new Style().setBold(true))
            .append(CLEAR_GHOST);
}
