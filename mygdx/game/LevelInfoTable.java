package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class LevelInfoTable extends Table {

    private TextField widthField,heightField,playerXField,playerYField,nameField,timeGoalField,moveGoalField;
    private LevelEditorScreen editorScreen;

    public LevelInfoTable(final LevelEditorScreen editorScreen){
        super();

        this.editorScreen = editorScreen;
        SpriteDrawable background = new SpriteDrawable();
        background.setSprite(new Sprite(new Texture(Gdx.files.internal("ui/box.png"))));

        TextField.TextFieldStyle textFieldStyle =
                new TextField.TextFieldStyle(new BitmapFont(), Color.WHITE,null,null,background);
        Label.LabelStyle labelStyle = new Label.LabelStyle(new BitmapFont(), Color.DARK_GRAY);


        int width = editorScreen.level.grid.cells.length;
        int height = editorScreen.level.grid.cells[0].length;

        widthField = new TextField(Integer.toString(width),textFieldStyle);
        widthField.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());

        heightField = new TextField(Integer.toString(height),textFieldStyle);
        heightField.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());

        playerXField = new TextField("1",textFieldStyle);
        playerXField.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());

        playerYField = new TextField("1",textFieldStyle);
        playerYField.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());

        nameField = new TextField(editorScreen.level.name,textFieldStyle);
        nameField.setMaxLength(15);

        timeGoalField = new TextField("10",textFieldStyle);
        timeGoalField.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());

        moveGoalField = new TextField("10",textFieldStyle);
        moveGoalField.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());

        TextField.TextFieldListener textFieldListener = new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                 String text = textField.getText();
                if(text.equals("")){
                    textField.setText("1");
                    updateSize();
                }
                else {
                    int value = Integer.parseInt(text);
                    value = Math.min(32,value);
                    textField.setText(Integer.toString(value));
                    updateSize();
                }
                int width = Integer.parseInt(widthField.getText());
                int playerX = Integer.parseInt(playerXField.getText());
                int newX = Math.min(playerX,width);

                int height = Integer.parseInt(heightField.getText());
                int playerY = Integer.parseInt(playerYField.getText());
                int newY = Math.min(playerY, height);

                playerXField.setText(Integer.toString(newX));
                playerYField.setText(Integer.toString(newY));
                editorScreen.level.player.setPosition(newX-1,newY-1);

            }
        };


        widthField.setTextFieldListener(textFieldListener);
        heightField.setTextFieldListener(textFieldListener);
        playerXField.setTextFieldListener(textFieldListener);
        playerYField.setTextFieldListener(textFieldListener);

        this.add(new Label("width",labelStyle)).padRight(2f);
        this.add(widthField).width(Gdx.graphics.getWidth()*0.05f).padRight(2f);
        this.add(new Label("player X",labelStyle)).padRight(2f);
        this.add(playerXField).width(Gdx.graphics.getWidth()*0.05f).pad(2f).row();
        this.add(new Label("height",labelStyle)).padRight(2f);
        this.add(heightField).width(Gdx.graphics.getWidth()*0.05f).padRight(2f);
        this.add(new Label("player Y",labelStyle)).padRight(2f);
        this.add(playerYField).width(Gdx.graphics.getWidth()*0.05f).pad(2f).row();
        this.add(new Label("level name",labelStyle));
        this.add(nameField).width(Gdx.graphics.getWidth()*0.05f).pad(2f).row();
        this.add(new Label("time",labelStyle)).pad(2f);
        this.add(timeGoalField).width(Gdx.graphics.getWidth()*0.05f).padRight(2f);
        this.add(new Label("move",labelStyle)).padRight(2f);
        this.add(moveGoalField).width(Gdx.graphics.getWidth()*0.05f).padRight(2f).row();


    }

    public void updateSize(){
        int w = Integer.parseInt(widthField.getText());
        int h = Integer.parseInt(heightField.getText());
        editorScreen.changeGridSize(w,h);
    }

    public String getName(){
        String text = nameField.getText();
        return text.equals("")?"unnamed":text;
    }

    public int getMoveGoal(){
        String text = moveGoalField.getText();
        if(text.equals("")){
            return 10;
        }
        else{
            return Integer.parseInt(text);
        }
    }

    public int getTimeGoal(){
        String text = timeGoalField.getText();
        if(text.equals("")){
            return 10;
        }
        else{
            return Integer.parseInt(text);
        }
    }



}
