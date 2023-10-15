package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Level{
    Player player;
    Grid grid;
    String name;
    int stars;
    float bestTime;
    int bestMoveCount;
    float bestTimeGoal;
    int bestMoveCountGoal;
    int doneOrNot;  // 0=done

    // for the model
    private float oriX, oriY, dimCellModel;

    public Level( Cell[][] cells, int playerPosInitX, int playerPosInitY, Color playerColorInit, String name, float bestTime, int bestMoveCount,float bestTimeGoal, int bestMoveCountGoal, int doneOrNot) {
        this.grid = new Grid(cells);
        player = new Player(grid, playerPosInitX, playerPosInitY, playerColorInit);
        grid.onWalk(playerPosInitX, playerPosInitY, player);
        this.name =name;
        this.bestTime = bestTime;
        this.bestMoveCount = bestMoveCount;
        this.bestTimeGoal = bestTimeGoal;
        this.bestMoveCountGoal = bestMoveCountGoal;
        this.doneOrNot = doneOrNot;

        updateStars();
    }

    // constructor to return a basic level when the player wants to create a new level
    public Level(){
        this.grid = new Grid(8,8);
        player = new Player(grid, 0,0,Color.WHITE);
        this.name = "Unnamed";
        this.bestTime = 1000;
        this.bestMoveCount = 1000;
        this.bestTimeGoal = 10;
        this.bestMoveCountGoal = 10;
        this.doneOrNot = 1;
    }

    // constructor used to return a "Error" level when the file is invalid
    public Level(int error){
        this.grid = new Grid(3,3);
        player = new Player(grid, 1,1,Color.RED);
        this.name = "Error";
        this.bestTime = 1000;
        this.bestMoveCount = 1000;
        this.bestTimeGoal = -1;
        this.bestMoveCountGoal = -1;
        this.doneOrNot = error;
    }

    public boolean checkCompleteLevel(){ return grid.checkCompleteGrid();}

    // called when the player quits the GameScreen
    public void reset() {
        player.reset();
        grid.reset();
        grid.onWalk(player.posX, player.posY, player);
    }

    public void render(Batch batch, float dimCell, float originX, float originY, boolean isLevelCompleted) {
        grid.render(batch);

        // the player is not displayed if the level is completed
        if (!isLevelCompleted) {
            player.render(batch, dimCell, originX, originY);
        }
    }

    // sets the dimensions of the model so that it's contained in a rectangle
    public void setModelDimension(float containerOriginX, float containerOriginY, float width, float height) {
        if (height/grid.cells[0].length < width/grid.cells.length) {
            this.dimCellModel = height / grid.cells[0].length;
            this.oriY = containerOriginY;
            this.oriX = containerOriginX + (1f / 2f) * (width - grid.cells.length * dimCellModel );
        } else {
            this.dimCellModel = width / grid.cells.length;
            this.oriX = containerOriginX;
            this.oriY = containerOriginY + (1f / 2f) * (height - grid.cells[0].length * dimCellModel );
        }
    }

    public void drawModel(ShapeRenderer shapeRenderer){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for(int x = 0; x < grid.cells.length; x++) {
            for (int y = 0; y < grid.cells[0].length; y++) {
                if (grid.cells[x][y] instanceof PaintableCell){
                    Color cellColor = ((PaintableCell) grid.cells[x][y]).getGoalColor();
                    shapeRenderer.rect(oriX+x*dimCellModel, oriY + y*dimCellModel,dimCellModel,dimCellModel,cellColor,cellColor,cellColor,cellColor);
                }
                else if (grid.cells[x][y] instanceof WallCell){
                    Color cellColor = ((WallCell) grid.cells[x][y]).getBasicColor();
                    shapeRenderer.rect(oriX+x*dimCellModel, oriY + y*dimCellModel,dimCellModel,dimCellModel,cellColor,cellColor,cellColor,cellColor);
                }
                /* else if (grid.cells[x][y] instanceof BucketCell){
                    Color cellColor = ((BucketCell) grid.cells[x][y]).getBasicColor();
                    shapeRenderer.rect(oriX+x*dimCellModel, oriY + y*dimCellModel,dimCellModel,dimCellModel,Color.GRAY,Color.GRAY,cellColor,cellColor);
                } */
            }
        }
        shapeRenderer.end();
    }

    /** Methods for the LevelsScreen */

    @Override
    public String toString() {     // returns the name and the right number of stars
        String nameEX = name + "   [#999900]";
        for (int i=0; i<stars; i++){
            nameEX += "*";
        }
        return nameEX;
    }

    // setting the number of stars the level has
    public void updateStars(){
        stars = 0;
        if (bestTime<=bestTimeGoal) { stars += 1; }
        if (bestMoveCount<=bestMoveCountGoal) { stars += 1; }
        if(doneOrNot==0){stars += 1;}
    }

    /** Methods for the editor screen */

    public void changeGridSize(int x,int y){
        int w = grid.cells.length;
        int h = grid.cells[0].length;
        Grid newGrid = new Grid(x,y);
        for(int i = 0; i < Math.min(w,x) ; i++){
            for(int j = 0; j < Math.min(h,y); j++){
                newGrid.cells[i][j] = grid.cells[i][j];
            }
        }
        grid = newGrid;
    }

    public void setLevelInfo(String newName,int moveCountGoal,int timeGoal){
        name = newName;
        bestMoveCountGoal = moveCountGoal;
        bestTimeGoal = timeGoal;
    }
}