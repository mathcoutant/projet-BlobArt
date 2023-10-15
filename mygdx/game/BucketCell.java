package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**A bucket is a special cell from which the player can change color*/
public class BucketCell implements Cell {

    private Color color;
    private Sprite spriteColor, spriteBG;

    public BucketCell(Color bucketColor){
        this.spriteColor = new Sprite(new Texture(Gdx.files.internal("cells/bucketColor.png")));
        this.spriteBG = new Sprite(new Texture(Gdx.files.internal("cells/bucketBG.png")));
        this.color=bucketColor;
        spriteColor.setColor(color);
    }

    @Override
    public void onWalk(Player player) {
        player.setColor(this.color);
    }

    @Override
    public void setCellSprite(int x, int y, float dimCell, float originX, float originY) {
        this.spriteColor.setPosition(originX + x*dimCell,originY + y*dimCell);
        this.spriteColor.setSize(dimCell, dimCell);
        this.spriteBG.setPosition(originX + x*dimCell,originY + y*dimCell);
        this.spriteBG.setSize(dimCell, dimCell);
    }

    @Override
    public boolean checkCompleteCell() {
        return true;
    }

    @Override
    public void editColor(Color col){
        this.spriteColor.setColor(col);
        this.color = col;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public Color getBasicColor() {
        return color;
    }

    @Override
    public void render(Batch batch) {
        spriteBG.draw(batch);
        spriteColor.draw(batch);
    }

    public void dispose(){
        spriteColor.getTexture().dispose();
        spriteBG.getTexture().dispose();
    }
}
