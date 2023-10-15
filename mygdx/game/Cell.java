package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Disposable;

public interface Cell extends Disposable {

    // Called when the player walk on the Cell
    public void onWalk(Player p);

    // Resize and set the position for the Sprite of the Cell
    public void setCellSprite(int x, int y, float dimCell, float originX, float originY);

    // Return true when the cell has the correct color
    public boolean checkCompleteCell();

    public void editColor(Color col);

    public Color getColor();

    public Color getBasicColor();

    public void render(Batch b);
}
