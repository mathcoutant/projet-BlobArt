package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;


public class WallCell implements Cell{

    private Color color;
    private Sprite sprite;

    public WallCell(Color wallColor){
        this.color = wallColor;
        this.sprite = new Sprite(new Texture(Gdx.files.internal("cells/wall.png")));
        sprite.setColor(color);
    }

    @Override
    public void onWalk(Player p) {}

    @Override
    public void setCellSprite(int x, int y, float dimCell, float originX, float originY) {
        this.sprite.setPosition(originX + x*dimCell,originY + y*dimCell);
        this.sprite.setSize(dimCell, dimCell);
    }

    @Override
    public boolean checkCompleteCell() {
        return true;
    }

    @Override
    public void editColor(Color color) {
        this.sprite.setColor(color);
        this.color = color;
    }

    @Override
    public Color getColor() {
        return color.cpy();
    }

    @Override
    public Color getBasicColor() {
        return color;
    }

    @Override
    public void render(Batch batch) { sprite.draw(batch); }

    public void dispose(){
        sprite.getTexture().dispose();
    }
}
