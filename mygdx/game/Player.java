package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Player {
    private Grid grid;
    private Color color;
    private Sprite spriteColor, spriteFix;
    int playerPosInitX;
    int playerPosInitY;
    Color playerColorInit;
    int posX;
    int posY;

    public Player(Grid grid, int playerPosInitX , int playerPosInitY, Color playerColorInit) {
        this.color = playerColorInit;
        this.playerColorInit =playerColorInit;
        this.spriteColor = new Sprite(new Texture(Gdx.files.internal("cells/playerColor.png")));
        spriteColor.setColor(color);
        this.spriteFix = new Sprite(new Texture(Gdx.files.internal("cells/playerFix.png")));
        this.playerPosInitX = this.posX = playerPosInitX;
        this.playerPosInitY = this.posY = playerPosInitY;
        this.grid=grid;
    }

    // Move the player on the grid
    public void movePlayer(int moveX, int moveY) {
        if (grid.cells[posX+moveX][posY+moveY] instanceof PaintableCell) {
            this.posX += moveX;
            this.posY += moveY;
            GameScreen.moveCount += 1;
        } else {
            grid.onWalk(posX + moveX, posY + moveY, this);
        }
        grid.onWalk(posX, posY, this);
    }

    public void render(Batch batch, float dimCell, float originX, float originY) {
        this.spriteColor.setPosition(originX + posX*dimCell,originY + posY*dimCell);
        spriteColor.draw(batch);
        this.spriteFix.setPosition(originX + posX*dimCell,originY + posY*dimCell);
        spriteFix.draw(batch);
    }

    // sets the dimensions of the sprite
    public void setPlayerSprite(float dimCell) {
        spriteColor.setSize(dimCell, dimCell);
        spriteFix.setSize(dimCell, dimCell);
    }

    public void reset() {
        posX = playerPosInitX;
        posY = playerPosInitY;
        this.setColor(playerColorInit);
    }

    public Color getColor() {
        return color;
    }

    public void setPosition(int x, int y){
        posX = x;
        posY = y;
        playerPosInitX = x;
        playerPosInitY = y;
    }

    public void setColor(Color color) {
        this.color = color;
        spriteColor.setColor(color);
    }
}
