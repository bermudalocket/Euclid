package com.bermudalocket.euclid.logblock;

import com.bermudalocket.euclid.util.RGBA;

public enum InspectionType {

    TOOLBLOCK(new RGBA(46, 49, 49, 1)), // dark gray
    QUERY(RGBA.BLACK);

    private RGBA _bgColor;

    InspectionType(RGBA bgColor) {
        _bgColor = bgColor;
    }

    public RGBA getBgColor() {
        return _bgColor;
    }

}
