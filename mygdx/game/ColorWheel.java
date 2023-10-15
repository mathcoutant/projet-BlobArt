package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.GdxRuntimeException;

/** The ColorWheel is used is the editor to pick a color */
public class ColorWheel extends ChangeListener {
    private Sprite sprite;
    private Slider slider;
    private final String fragmentShader;
    private final String vertexShader;
    private final ShaderProgram shaderProgram;
    private float value = 1f;

    public ColorWheel(Slider slider){
        this.slider = slider;
        sprite = new Sprite(new Texture(Gdx.files.internal("cells/paintable.png")));
        vertexShader = Gdx.files.internal("Shaders/vertex.glsl").readString();
        fragmentShader = Gdx.files.internal("Shaders/fragment.glsl").readString();
        shaderProgram = new ShaderProgram(vertexShader,fragmentShader);
        ShaderProgram.pedantic = false;
        if (!shaderProgram.isCompiled()) {
            throw new GdxRuntimeException(shaderProgram.getLog());
        }
        sprite.setPosition(1200,500);
        sprite.setScale(10,10);
    }

    @Override
    public void changed(ChangeEvent event, Actor actor) {
        value = slider.getValue();
    }

    public boolean mouseOnWheel(int mouseX,int mouseY){
        Rectangle bound = sprite.getBoundingRectangle();
        Circle circle = new Circle(bound.getCenter(new Vector2()),bound.width/2f-1f);
        return circle.contains(mouseX,Gdx.graphics.getHeight()-mouseY);
    }

    public void draw(SpriteBatch batch) {
        batch.setShader(shaderProgram);
        shaderProgram.setUniformf("u_value",value);
        sprite.draw(batch);
        batch.setShader(SpriteBatch.createDefaultShader());
    }
}
