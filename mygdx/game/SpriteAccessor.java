package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;

import aurelienribon.tweenengine.TweenAccessor;


public class SpriteAccessor implements TweenAccessor<Sprite> {
    public static final int alpha = 0;
    public static final int positionY = 1;
    public static final int positionX = 2;

    @Override
    public int getValues(Sprite sprite, int i, float[] floats) {
        switch (i) {
            case alpha:
                floats[0] = sprite.getColor().a;
                return 1;
            case positionY:
                floats[0] = sprite.getY();
                return 1;
            case positionX:
                floats[0] = sprite.getX();
                return 1;
            default:
                assert false;
                return -1;
        }
    }

    @Override
    public void setValues(Sprite sprite, int i, float[] floats) {
        switch (i) {
            case alpha:
                sprite.setColor(sprite.getColor().r,sprite.getColor().g,sprite.getColor().b, floats[0]);
                break;
            case positionY:
                sprite.setPosition(sprite.getX(),floats[0]);
                break;
            case positionX:
                sprite.setPosition(floats[0],sprite.getY());
                break;
            default:
                assert false;
        }
    }
}
