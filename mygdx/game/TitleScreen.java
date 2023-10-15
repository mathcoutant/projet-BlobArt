package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Cubic;


public class TitleScreen extends ScreenAdapter {
    private BlobArt game;
    private InputMultiplexer inputMultiplexer;
    private TextureAtlas atlas;
    private Skin skin;
    private Sprite background, backgroundRight, backgroundLeft;
    private ImageButton.ImageButtonStyle playButtonStyle, closeButtonStyle, optionButtonStyle ;
    private ImageButton playButton, closeButton, optionButton;
    private Stage stage;
    private Table table, optionButtonContainer, closeButtonContainer;
    private TweenManager tweenManager;
    private boolean toggleAnimation;

    public TitleScreen(BlobArt game, InputMultiplexer inputMultiplexer, boolean toggleAnimation){
        this.game=game;
        this.inputMultiplexer = inputMultiplexer;

        // creating the atlas and the skin for textures
        this.atlas = new TextureAtlas(Gdx.files.internal("ui/titlescreen.atlas"));
        this.skin = new Skin(atlas);

        // the background image
        this.background = skin.getSprite("TitleScreen_background");
        this.backgroundRight = skin.getSprite("TitleScreen_background_right");
        this.backgroundLeft = skin.getSprite("TitleScreen_background_left");

        // creating the buttons
        this.playButtonStyle = new ImageButton.ImageButtonStyle();
        playButtonStyle.imageDown = skin.getDrawable("playButton-Pressed");
        playButtonStyle.imageOver = skin.getDrawable("playButton-Over");
        playButtonStyle.imageUp = skin.getDrawable("playButton-Up");
        this.playButton = new ImageButton(playButtonStyle);

        this.closeButtonStyle = new ImageButton.ImageButtonStyle();
        closeButtonStyle.imageDown = skin.getDrawable("closebutton-Pressed");
        closeButtonStyle.imageUp = skin.getDrawable("closebutton-Up");
        closeButtonStyle.imageOver = skin.getDrawable("closebutton-Over");
        this.closeButton = new ImageButton(closeButtonStyle);

        /*this.optionButtonStyle = new ImageButton.ImageButtonStyle();
        optionButtonStyle.imageDown = skin.getDrawable("optionButton-Pressed");
        optionButtonStyle.imageOver = skin.getDrawable("optionButton-Over");
        optionButtonStyle.imageUp = skin.getDrawable("optionButton-Up");
        this.optionButton = new ImageButton(optionButtonStyle);*/

        // setting the table to place the buttons at the right place on the screen
        this.table = new Table();
        table.setBounds(0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.closeButtonContainer = new Table();
        closeButtonContainer.add(closeButton).expandX().padLeft(table.getWidth()/20).left();
        table.add(closeButtonContainer).width(table.getWidth()/3).height(table.getHeight()/5);
        table.add().width(table.getWidth()/3);
        table.add().width(table.getWidth()/3).row();
        table.add().height(table.getHeight()*2.3f/5).row();
        /*
        this.optionButtonContainer = new Table();
        optionButtonContainer.add(optionButton);
        //table.add(optionButtonContainer).height(table.getHeight()*1.7f/5);
        */
        table.add().height(table.getHeight()*1.7f/5);
        table.add(playButton);

        // setting up the stage
        this.stage = new Stage();
        inputMultiplexer.addProcessor(stage);
        stage.addActor(table);

        //stage.setDebugAll(true); // to see the outline of all the elements on screen

        // Animations
        this.tweenManager = new TweenManager();
        this.toggleAnimation = toggleAnimation;
    }

    @Override
    public void show() {
        if (toggleAnimation) {
            Tween.registerAccessor(Sprite.class, new SpriteAccessor());
            Tween.registerAccessor(Actor.class, new ActorAccessor());

            // Animation : state at the beginning
            Tween.set(background, SpriteAccessor.alpha).target(0).start(tweenManager);
            Tween.set(backgroundRight, SpriteAccessor.alpha).target(0).start(tweenManager);
            Tween.set(backgroundLeft, SpriteAccessor.alpha).target(0).start(tweenManager);
            Tween.set(playButton, ActorAccessor.positionY).target(-300).start(tweenManager);
            //Tween.set(optionButton, ActorAccessor.positionY).target(-300).start(tweenManager);
            Tween.set(closeButton, ActorAccessor.positionY).target(300).start(tweenManager);

            // Animation : state at the end
            Tween.to(background, SpriteAccessor.alpha, 1).target(1).start(tweenManager);
            Tween.to(backgroundRight, SpriteAccessor.alpha, 1).target(1).start(tweenManager);
            Tween.to(backgroundLeft, SpriteAccessor.alpha, 1).target(1).start(tweenManager);
            Tween.from(backgroundRight, SpriteAccessor.positionX, 2).target(300).ease(Cubic.OUT).start(tweenManager);
            Tween.from(backgroundLeft, SpriteAccessor.positionX, 2).target(-300).ease(Cubic.OUT).start(tweenManager);
            Tween.to(playButton, ActorAccessor.positionY, 2).target(table.getHeight() * 1.7f / 10 - playButton.getHeight() / 2).ease(Cubic.OUT).start(tweenManager);
            //Tween.to(optionButton, ActorAccessor.positionY, 2).target(table.getHeight() * 1.7f / 10 - optionButton.getHeight() / 2).ease(Cubic.OUT).start(tweenManager);
            Tween.to(closeButton, ActorAccessor.positionY, 2).target(table.getHeight() / 10 - closeButton.getHeight() / 2).ease(Cubic.OUT).start(tweenManager);
        }

        // describing what the buttons do
        this.playButton.addListener(new InputListener()
        {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            //the touchUp function is called only when a button is released and the touchDown function previously returned true
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new LevelsScreen(game, inputMultiplexer));
            }
        });
        this.closeButton.addListener(new InputListener()
        {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
            }
        });
        /*
        this.optionButton.addListener(new InputListener()
        {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                //à compléter
            }
        });*/
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        tweenManager.update(delta);

        game.batch.begin();
        background.draw(game.batch);
        backgroundLeft.draw(game.batch);
        backgroundRight.draw(game.batch);
        game.batch.end();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void hide() {
        inputMultiplexer.removeProcessor(stage);
        skin.dispose();
        stage.dispose();
        atlas.dispose();
    }

}


