package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


public class ColorPaletteButton extends ImageButton {
    private final Image leftSelection,rightSelection,buttonImage;

    public ColorPaletteButton(){
        super(new TextureRegionDrawable(new Texture(Gdx.files.internal("ui/ColorButton.png"))));
        leftSelection = new Image(new Texture(Gdx.files.internal("ui/SelectionSquare.png")));
        rightSelection = new Image(new Texture(Gdx.files.internal("ui/SelectionSquare.png")));

        leftSelection.setColor(Color.RED);
        rightSelection.setColor(Color.BLUE);

        this.addActor(leftSelection);
        this.addActor(rightSelection);

        leftSelection.setVisible(false);
        rightSelection.setVisible(false);

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

    // display the right overlay when the palette is clicked
    public void select(int button){
        if(button==0){
            leftSelection.setVisible(true);
            rightSelection.setVisible(false);
        }
        else{
            rightSelection.setVisible(true);
            leftSelection.setVisible(false);
        }
    }

    public void unselect(){
        leftSelection.setVisible(false);
        rightSelection.setVisible(false);
    }
}
