package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;

/**A color palette is used to store the colors used in a level*/
public class ColorPalette {
    private Array<Color> Palette;


    public ColorPalette() {
        Palette = new Array<Color>();
    }

    public void addColor(Color color) {
        Palette.add(color);
    }

    // Mix equally two color and return the resulting color (not used in the game)
    public static Color mixColor(Color color1, Color color2) {
        float r = (color1.r + color2.r) / 2f;
        float g = (color1.g + color2.g) / 2f;
        float b = (color1.b + color2.b) / 2f;
        return new Color(r, g, b, 1.0f);
    }

    // Mix two color and add the result to the palette (not used in the game)
    public void mixAndAdd(Color color1, Color color2) {
        addColor(mixColor(color1,color2));
    }

    public Color getColor(int i){
        return Palette.get(i);
    }

    public int getPaletteSize(){ return Palette.size;}

    public int getIndexOfColor (Color color) {
        return Palette.indexOf(color, false);
    }
}
