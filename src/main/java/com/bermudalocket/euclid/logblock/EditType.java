package com.bermudalocket.euclid.logblock;

import com.bermudalocket.euclid.util.RGBA;

public enum EditType {

    CREATED(RGBA.GREEN),
    DESTROYED(RGBA.RED),
    REPLACED(RGBA.YELLOW);

    private RGBA _color;

    EditType(RGBA color) {
        _color = color;
    }

    public RGBA getColor() {
        return _color;
    }

    public static String getDesc(EditType type) {
        return type.toString().toLowerCase();
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
