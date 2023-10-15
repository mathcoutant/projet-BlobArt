package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.ScreenUtils;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;


/**This screen appear at the launching of the game and shows the credits*/
public class SplashScreen extends ScreenAdapter {
    private BlobArt game;
    private InputMultiplexer inputMultiplexer;
    private TweenManager tweenManager;
    private Sprite splashScreenCredits;

    public SplashScreen(BlobArt game, InputMultiplexer inputMultiplexer) {
        this.game=game;
        this.inputMultiplexer=inputMultiplexer;
        this.tweenManager = new TweenManager();
        this.splashScreenCredits = new Sprite(new Texture(Gdx.files.internal("ui/SplashScreenCredits.png")));
        splashScreenCredits.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        splashScreenCredits.setColor(splashScreenCredits.getColor().r, splashScreenCredits.getColor().g, splashScreenCredits.getColor().b, 0);
    }

    @Override
    public void show() {
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());

        // creating the animation
        Tween.to(splashScreenCredits, SpriteAccessor.alpha, 1).target(1).start(tweenManager);
        Tween.from(splashScreenCredits, SpriteAccessor.positionY, 1).target(splashScreenCredits.getY()+150).start(tweenManager);
        Tween.to(splashScreenCredits, SpriteAccessor.alpha, 1).target(0).delay(2.5f).start(tweenManager);
        Tween.to(splashScreenCredits, SpriteAccessor.positionY, 1).target(splashScreenCredits.getY()-150).delay(2.5f).setCallback(new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> baseTween) {
                game.setScreen(new TitleScreen(game, inputMultiplexer, true));
            }
        }).start(tweenManager);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        tweenManager.update(delta);

        game.batch.begin();
        splashScreenCredits.draw(game.batch);
        game.batch.end();
    }

    @Override
    public void hide() {
        dispose();
    }

}