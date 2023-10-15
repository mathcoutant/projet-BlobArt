package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

/**This screen is used when the player plays*/
public class GameScreen extends ScreenAdapter{
    private BlobArt game;
    private Level level;
    private PlayerController playerController;
    private InputMultiplexer inputMultiplexer;
    private Stage stage;
    private TextureAtlas atlas;
    private Skin skin;
    private Table globalTable, infoTable, backButtonContainer;
    private BitmapFont font;
    private Label.LabelStyle labelTitleStyle, labelTimerStyle, labelMoveCountStyle;
    private Label levelTitle, labelTimer, labelMoveCount;
    private ImageButton.ImageButtonStyle backButtonStyle;
    private ImageButton backButton;
    private Sprite background;
    private float dimCell;
    private float originX;
    private float originY;
    private ShapeRenderer shapeRenderer;
    private boolean isLevelCompleted=false;
    private float timer=0;
    public static int moveCount=0;
    private long timeCompleted;


    public GameScreen(BlobArt game, InputMultiplexer inputMultiplexer, Level level) {
        this.game=game;
        this.level = level;
        this.playerController = new PlayerController(level.player, level.grid.cells.length, level.grid.cells[0].length);
        this.shapeRenderer = new ShapeRenderer();

        // creating the atlas and the skin for textures
        this.atlas = new TextureAtlas(Gdx.files.internal("ui/gamescreen.atlas"));
        this.skin = new Skin(atlas);

        this.background = skin.getSprite("GameScreen_background");

        // creating the font
        this.font = new BitmapFont(Gdx.files.internal("font/rageitalic_black.fnt"), false);

        // creating the button
        this.backButtonStyle = new ImageButton.ImageButtonStyle();
        backButtonStyle.imageDown = skin.getDrawable("backButton-Pressed");
        backButtonStyle.imageUp = skin.getDrawable("backButton-Up");
        backButtonStyle.imageOver = skin.getDrawable("backButton-Over");
        this.backButton = new ImageButton(backButtonStyle);

        // setting the labels
        this.labelTitleStyle = new Label.LabelStyle();
        labelTitleStyle.background = skin.getDrawable("LabelLevelName");
        labelTitleStyle.font = font;
        this.levelTitle = new Label(level.name, labelTitleStyle);
        levelTitle.setAlignment(1);
        levelTitle.setFontScale(1.5f);

        this.labelTimerStyle = new Label.LabelStyle();
        labelTimerStyle.font = font;
        this.labelTimer = new Label("0", labelTimerStyle);
        labelTimer.setAlignment(1);

        this.labelMoveCountStyle = new Label.LabelStyle();
        labelMoveCountStyle.font = font;
        this.labelMoveCount = new Label("0", labelMoveCountStyle);
        labelMoveCount.setAlignment(1);

        // setting the table
        this.globalTable = new Table();
        globalTable.setBounds(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        globalTable.add().colspan(3).height(Gdx.graphics.getHeight()/20f).row();
        this.backButtonContainer = new Table();
        backButtonContainer.add(backButton).row();
        backButtonContainer.add().expandY();
        globalTable.add(backButtonContainer).height(Gdx.graphics.getHeight()*9f/10f).width(Gdx.graphics.getWidth()/10f);
        globalTable.add().width(Gdx.graphics.getWidth()*6f/10f);
        this.infoTable = new Table();
        globalTable.add(infoTable).expand().row();
        infoTable.add(levelTitle).height(Gdx.graphics.getHeight()/10f).minWidth(Gdx.graphics.getWidth()/10f).row();
        infoTable.add().height(Gdx.graphics.getHeight()*2f/10f).padTop(Gdx.graphics.getHeight()/20f).row();
        infoTable.add(labelTimer).height(Gdx.graphics.getHeight()/10f).padTop(Gdx.graphics.getHeight()/10f).row();
        infoTable.add(labelMoveCount).height(Gdx.graphics.getHeight()/10f).padTop(Gdx.graphics.getHeight()/20f).row();
        infoTable.add().height(Gdx.graphics.getHeight()*5f/20f);  // à changer pour ajouter des infos
        globalTable.add().colspan(3).height(Gdx.graphics.getHeight()/20f);

        // setting the stage
        this.stage = new Stage();
        stage.addActor(globalTable);

        //stage.setDebugAll(true); // to see the outline of all the elements on screen

        // setting the inputProcessors
        this.inputMultiplexer=inputMultiplexer;
        inputMultiplexer.addProcessor(playerController);
        inputMultiplexer.addProcessor(stage);
    }

    @Override
    public void show() {
        // sets the sprites of the cells and the player
        float windowWidth = Gdx.graphics.getWidth();
        float windowHeight = Gdx.graphics.getHeight();
        float nbrCellX = level.grid.cells.length;
        float nbrCellY = level.grid.cells[0].length;
        if (.9f*windowHeight/nbrCellY < .6f*windowWidth/nbrCellX) {
            this.dimCell = (9f / 10f) * windowHeight / nbrCellY;
            this.originY = (1f / 20f) * windowHeight;
            this.originX = (1f / 2f) * ( (8f / 10f) * windowWidth - nbrCellX * this.dimCell );
        } else {
            this.dimCell = (6f / 10f) * ( windowWidth / nbrCellX );
            this.originX = (1f / 10f) * windowWidth;
            this.originY = (1f / 2f) * ( windowHeight - nbrCellY * this.dimCell );
        }
        Cell[][] cells = level.grid.cells;
        for(int x = 0; x < cells.length; x++) {
            for (int y = 0; y < cells[0].length; y++) {
                cells[x][y].setCellSprite(x, y, dimCell, originX, originY);
            }
        }
        level.player.setPlayerSprite(dimCell);

        // sets the dimensions of the model
        level.setModelDimension(Gdx.graphics.getWidth()*8f/10f,Gdx.graphics.getHeight()*12f/20f,Gdx.graphics.getWidth()/10f,Gdx.graphics.getHeight()*2f/10f);

        // what the buttons do
        this.backButton.addListener(new InputListener()
        {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                inputMultiplexer.removeProcessor(playerController);
                inputMultiplexer.removeProcessor(stage);
                game.setScreen(new LevelsScreen(game, inputMultiplexer));
            }
        });
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(255, 255, 255, 1);

        // update the information
        timer += delta;
        labelTimer.setText("Time : " + String.format(java.util.Locale.US,"%.0f",timer));
        labelMoveCount.setText(moveCount + " Moves");

        // render all the elements
        game.batch.begin();
        background.draw(game.batch);
        level.render(game.batch, dimCell, originX, originY, isLevelCompleted);
        game.batch.end();
        stage.act(delta);
        stage.draw();

        // display the model
        level.drawModel(shapeRenderer);

        // check if the level is completed each time a key is pressed
        if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY) && !isLevelCompleted){
            if(level.checkCompleteLevel()){
                inputMultiplexer.removeProcessor(playerController);
                inputMultiplexer.removeProcessor(stage);
                isLevelCompleted = true;
                if (timer<level.bestTime){
                    level.bestTime = timer;
                }
                if (moveCount< level.bestMoveCount) {
                    level.bestMoveCount = moveCount;
                }
                level.doneOrNot = 0;
                level.updateStars();
                timeCompleted = TimeUtils.millis();
            }
        }
        // to wait one second after the completion
        if (isLevelCompleted){
            if (TimeUtils.timeSinceMillis(timeCompleted)>1000) {
                game.setScreen(new LevelsScreen(game, inputMultiplexer));
            }
        }
    }

    @Override
    public void hide() {
        moveCount=0;
        level.reset();
        shapeRenderer.dispose();
        stage.dispose();
        font.dispose();
        atlas.dispose();
        dispose();
    }
}
