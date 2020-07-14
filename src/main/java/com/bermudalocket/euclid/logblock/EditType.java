package com.bermudalocket.euclid.logblock;

import java.awt.Color;

public enum EditType {

    CREATED(Color.GREEN),
    DESTROYED(Color.RED),
    REPLACED(Color.YELLOW);

    public final Color color;

    EditType(Color color) {
        this.color = color;
    }

    public static EditType fromString(String string) {
        for (EditType editType : values()) {
            if (editType.toString().equalsIgnoreCase(string)) {
                return editType;
            }
        }
        return null;
    }

}
