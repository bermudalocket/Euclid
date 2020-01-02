package com.bermudalocket.euclid.util;

import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class StyledTextBuilder {

    private String string;

    private Style style;

    public StyledTextBuilder(String string) {
        this.string = string;
        this.style = new Style();
    }

    public Text build() {
        return new LiteralText(string).setStyle(style);
    }

    public StyledTextBuilder bold(boolean bold) {
        this.style.setBold(bold);
        return this;
    }

    public StyledTextBuilder italic(boolean italic) {
        this.style.setItalic(italic);
        return this;
    }

    public StyledTextBuilder underline(boolean underline) {
        this.style.setUnderline(underline);
        return this;
    }

    public StyledTextBuilder obfuscate(boolean obfuscate) {
        this.style.setObfuscated(obfuscate);
        return this;
    }

    public StyledTextBuilder color(Formatting color) {
        this.style.setColor(color);
        return this;
    }

    public StyledTextBuilder onHover(HoverEvent.Action action, String string) {
        this.style.setHoverEvent(new HoverEvent(action, new LiteralText(string)));
        return this;
    }

    public StyledTextBuilder onClick(ClickEvent.Action action, String string) {
        this.style.setClickEvent(new ClickEvent(action, string));
        return this;
    }

}
