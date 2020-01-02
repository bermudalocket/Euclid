package com.bermudalocket.euclid.util;

import java.awt.*;
import java.util.HashMap;

public class RGBA {

    public static final float DEFAULT_ALPHA = 0.8f;

    public static final RGBA RED = new RGBA(1f, 0f, 0f, 0.25f);

    public static final RGBA GREEN = new RGBA(0f, 1f, 0f, DEFAULT_ALPHA);

    public static final RGBA BLUE = new RGBA(0f, 0f, 1f, DEFAULT_ALPHA);

    public static final RGBA YELLOW = new RGBA(1f, 1f, 0f, DEFAULT_ALPHA);

    public static final RGBA PINK = new RGBA(1.0f, 0.078431375f, 0.5764706f, 1f);

    public static final RGBA BLACK = new RGBA(0f, 0f, 0f, DEFAULT_ALPHA);

    public static final RGBA WHITE = new RGBA(1f, 1f, 1f, DEFAULT_ALPHA);

    public static final RGBA GRAY = new RGBA(0.5f, 0.5f, 0.5f, 0.4f);

    private final float[] _rgba;

    private static final HashMap<RGBA, Integer> COLOR_INT_CACHE = new HashMap<>();

    public RGBA(float red, float green, float blue, float alpha) {
        _rgba = new float[]{ red, green, blue, alpha };
    }

    public RGBA(int red, int green, int blue, int alpha) {
        this(red/255f, green/255f, blue/255f, alpha);
    }

    public RGBA(float red, float green, float blue) {
        this(red, green, blue, 1f);
    }

    public RGBA(int rgb, float alpha) {
        this(rgb & 255, (rgb >> 8) & 255, (rgb >> 16) & 255, alpha);
    }

    public int toInt() {
        return COLOR_INT_CACHE.computeIfAbsent(this, c -> new Color(red(), green(), blue(), alpha()).getRGB());
    }

    @Override
    public String toString() {
        return String.format("(%d, %d, %d, %d)",
            (int) red(),
            (int) green(),
            (int) blue(),
            (int) alpha());
    }

    public float red() {
        return _rgba[0];
    }

    public float green() {
        return _rgba[1];
    }

    public float blue() {
        return _rgba[2];
    }

    public float alpha() {
        return _rgba[3];
    }

    public void setOpacity(float alpha) {
        _rgba[3] = alpha;
    }

    public RGBA setAlpha(float alpha) {
        return new RGBA(red(), green(), blue(), alpha);
    }

}
