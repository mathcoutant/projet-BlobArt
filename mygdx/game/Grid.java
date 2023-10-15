package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;

/**A matrix of Cells used in Levels*/
public class Grid {
    public Cell[][] cells;

    public Grid(Cell[][] cells){
        this.cells = cells;
    }

    // new constructor used to create a new Level
    public Grid (int x, int y) {
        cells = new Cell[x][y];
        for(int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                cells[i][j] = new PaintableCell(Color.WHITE);
            }
        }
    }

    public void render(Batch batch){
        for(int x = 0; x < cells.length; x++) {
            for (int y = 0; y < cells[0].length; y++) {
                cells[x][y].render(batch);
            }
        }
    }

    public void onWalk(int x,int y, Player player){
        cells[x][y].onWalk(player);
    }

    public boolean checkCompleteGrid() {
        for(int x = 0; x < cells.length; x++) {
            for (int y = 0; y < cells[0].length; y++) {
                if (!(cells[x][y]).checkCompleteCell()){return false;}
            }
        }
        return true;
    }

    public void reset(){
        for(int x = 0; x < cells.length; x++) {
            for (int y = 0; y < cells[0].length; y++) {
                if ((cells[x][y]) instanceof PaintableCell){
                    ((PaintableCell) cells[x][y]).reset();
                }
            }
        }
    }

    public int cellToInt(int x, int y){
        if (cells[x][y] instanceof PaintableCell){
            return 0;
        } else if (cells[x][y] instanceof BucketCell) {
            return 1;
        } else if (cells[x][y] instanceof WallCell) {
            return 2;
        }
        return 0;
    }
}
