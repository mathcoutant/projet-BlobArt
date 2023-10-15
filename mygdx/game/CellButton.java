package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class CellButton extends ImageButton {
    public  int index;
    private Image selectionBox;
    private Image buttonImage;


    public CellButton(int cellType,String path){
        super(new TextureRegionDrawable(new Texture(Gdx.files.internal(path))));
        this.index = cellType;

        selectionBox = new Image(new Texture(Gdx.files.internal("ui/SelectionSquare.png")));
        selectionBox.setVisible(false);
        this.addActor(selectionBox);

        buttonImage = this.getImage();

    }

    @Override
    public void setColor(Color color) {
        buttonImage.setColor(color);
    }

    @Override
    public Color getColor() {
        return buttonImage.getColor().cpy();
    }

    public void select(){
        selectionBox.setVisible(true);
    }

    public void unselect(){
        selectionBox.setVisible(false);
    }
}
