package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**A class representing cell that the player can paint*/
public class PaintableCell implements Cell {

    private Color initialColor;
    private Color color;
    private Color goalColor;
    private Sprite sprite;

    private boolean goalColorShowing = false;

    public PaintableCell(Color goalColor){
        this.color = Color.WHITE;
        this.initialColor = Color.WHITE;
        this.goalColor=goalColor;
        this.sprite = new Sprite(new Texture(Gdx.files.internal("cells/paintable.png")));
        this.sprite.setColor(color);
    }

    public PaintableCell(Color color, Color goalColor){
        this.sprite = new Sprite(new Texture(Gdx.files.internal("cells/paintable.png")));
        this.color = color;
        this.initialColor = color;
        this.sprite.setColor(color);
        this.goalColor=goalColor;
    }

    @Override
    public void onWalk(Player player){
        this.setBasicColor(player.getColor());
    }

    @Override
    public void setCellSprite(int x, int y, float dimCell, float originX, float originY) {
        this.sprite.setPosition(originX + x*dimCell,originY + y*dimCell);
        this.sprite.setSize(dimCell, dimCell);
    }

    @Override
    public boolean checkCompleteCell(){
        return color.equals(goalColor);
    }

    public void setBasicColor(Color col){
        color = col;
        sprite.setColor(col);
    }

    @Override
    public void editColor(Color col) {
        if(goalColorShowing){
            goalColor = col;
        }
        else{
            this.initialColor = col;
        }
        sprite.setColor(col);
    }

    @Override
    public Color getColor(){
        return sprite.getColor().cpy();
    }

    @Override
    public Color getBasicColor() {
        return initialColor;
    }

    @Override
    public void render(Batch batch) { sprite.draw(batch); }

    public Color getGoalColor() {
        return goalColor;
    }

    public void toggle(){
        goalColorShowing = !goalColorShowing;
        if(goalColorShowing){
            sprite.setColor(goalColor);
        }
        else{
            sprite.setColor(initialColor);
        }
    }

    public void reset(){
        this.sprite.setColor(initialColor);
        goalColorShowing = false;
        this.color = initialColor;
    }

    public void dispose(){
        sprite.getTexture().dispose();
    }
}
