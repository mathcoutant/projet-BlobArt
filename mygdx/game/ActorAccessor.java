package com.mygdx.game;

import com.badlogic.gdx.scenes.scene2d.Actor;

import aurelienribon.tweenengine.TweenAccessor;


public class ActorAccessor implements TweenAccessor<Actor> {
    public static final int positionX = 0;
    public static final int positionY = 1;

    @Override
    public int getValues(Actor actor, int i, float[] floats) {
        switch (i) {
            case positionX:
                floats[0] = actor.getX();
                return 1;
            case positionY:
                floats[0] = actor.getY();
                return 1;
            default:
                assert false;
                return -1;
        }
    }

    @Override
    public void setValues(Actor actor, int i, float[] floats) {
        switch (i) {
            case positionX:
                actor.setPosition(floats[0],actor.getY());
                break;
            case positionY:
                actor.setPosition(actor.getX(),floats[0]);
                break;
            default:
                assert false;
        }
    }
}