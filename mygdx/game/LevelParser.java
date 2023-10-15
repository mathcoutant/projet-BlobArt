package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

/**This class is used to load a Level from a file*/
public class LevelParser {

    private BufferedReader reader;
    private ColorPalette palette;

    // return an array of Levels from a directory
    public Level[] parseFile (FileHandle filehandle) {
        FileHandle[] fileList = filehandle.list();
        Level[] levelList = new Level[fileList.length];
        for (int i = 0 ; i< levelList.length ; i++){
            levelList[i]=this.parse(fileList[i]);
        }
        return levelList;
    }

    // convert an array of Levels into files in a directory
    public void updateFiles(FileHandle filehandle, Level[] levels){
        FileHandle[] fileList = filehandle.list();
        for (int i = 0 ; i< fileList.length ; i++){
            filehandle.emptyDirectory();
        }
        for (int i = 0 ; i< levels.length ; i++){
            String stringLevel = stringify(levels[i]);
            String fileName = "level_"+i+"_"+levels[i].name+".txt";
            FileHandle file = Gdx.files.local(filehandle.path() + "/" + fileName);
            file.writeString(stringLevel, false);
        }
    }

    // return a String representing a Level
    public String stringify(Level level) {
        ColorPalette palette = new ColorPalette();
        for (int i = 0; i<level.grid.cells.length; i++){
            for (int j = 0; j<level.grid.cells[0].length; j++){
                if (palette.getIndexOfColor(level.grid.cells[i][j].getBasicColor()) == -1){
                    palette.addColor(level.grid.cells[i][j].getBasicColor());
                }
                if(level.grid.cells[i][j] instanceof PaintableCell){
                    if(palette.getIndexOfColor(((PaintableCell)level.grid.cells[i][j]).getGoalColor()) == -1){
                        palette.addColor(((PaintableCell) level.grid.cells[i][j]).getGoalColor());
                    }
                }
                if(palette.getIndexOfColor(level.player.getColor()) == -1){
                    palette.addColor(level.player.getColor());
                }
            }
        }
        System.out.println(palette.getPaletteSize());

        String result = level.name + "\n" + palette.getPaletteSize() + "\n";
        for (int i=0; i< palette.getPaletteSize(); i++){
            result += palette.getColor(i).toString() + "\n";
        }
        result += level.grid.cells.length + "\n" + level.grid.cells[0].length + "\n";
        result += level.player.playerPosInitX + "\n" + level.player.playerPosInitY + "\n";
        result += palette.getIndexOfColor(level.player.playerColorInit) + "\n";
        result += level.bestTime+ "\n" + level.bestMoveCount + "\n";
        result += level.bestTimeGoal+ "\n" + level.bestMoveCountGoal + "\n";
        result += level.doneOrNot + "\n";
        for (int i=level.grid.cells[0].length-1; i>-1; i--) {
            for (int j=0; j<level.grid.cells.length; j++){
                result += level.grid.cellToInt(j, i) + " " + palette.getIndexOfColor(level.grid.cells[j][i].getBasicColor());
                if(level.grid.cells[j][i] instanceof PaintableCell) {
                   result += " " + palette.getIndexOfColor(((PaintableCell)level.grid.cells[j][i]).getGoalColor());
                }
                    if (j!=level.grid.cells.length-1){
                    result += ",";
                }
            }
            result += "\n";
        }

        return result;
    }

    // return a Level from a text file
    public Level parse(FileHandle fileHandle) {
        try {
            reader =  new BufferedReader(fileHandle.reader());

            // we get the name of the level
            String name = reader.readLine();

            // we get the color palette of the level
            int paletteSize = Integer.parseInt(reader.readLine());
            palette = new ColorPalette();
            for (int i = 0; i < paletteSize; i++) {
                palette.addColor(Color.valueOf(reader.readLine()));
            }

            // we get the dimensions of the grid
            int width = Integer.parseInt(reader.readLine());
            int height = Integer.parseInt(reader.readLine());

            // we get player-related information
            int initX = Integer.parseInt(reader.readLine());
            int initY = Integer.parseInt(reader.readLine());
            Color initColor = palette.getColor(Integer.parseInt(reader.readLine()));

            // we get the stats
            float time = Float.parseFloat(reader.readLine());
            int moves = Integer.parseInt(reader.readLine());

            // we get the goals
            float timeGoal = Float.parseFloat(reader.readLine());
            int movesGoal = Integer.parseInt(reader.readLine());

            // we get if the level is completed
            int doneOrNot = Integer.parseInt(reader.readLine());

            // we generate the cell matrix
            Cell[][] cells = new Cell[width][height];
            for (int j = 0; j < height; j++) {
                List<String> line = new ArrayList<String>(Arrays.asList(reader.readLine().split(",")));
                for(int i = 0; i < width; i++){
                    cells[i][height-(j+1)] = parseCell(line.get(i).split(" "));
                }
            }

            return new Level(cells,initX,initY,initColor,name, time, moves, timeGoal, movesGoal, doneOrNot);
        }
        catch (Exception e)
        {
            return new Level(666);
        }
    }

    // return the Cell corresponding to the String in argument
    private Cell parseCell(String[] cellParam) {
        if(cellParam[0].equals("0")){
            Color color = palette.getColor(Integer.parseInt(cellParam[1]));
            if(cellParam.length == 2){
                return new PaintableCell(color);
            }
            else {
                Color goalColor = palette.getColor(Integer.parseInt(cellParam[2]));
                return new PaintableCell(color, goalColor);
            }
        }
        else if (cellParam[0].equals("1")){
            Color color = palette.getColor(Integer.parseInt(cellParam[1]));
            return new BucketCell(color);
        }
        else if (cellParam[0].equals("2")){
            Color color = palette.getColor(Integer.parseInt(cellParam[1]));
            return new WallCell(color);
        }
            return new PaintableCell(Color.WHITE);
    }
}

