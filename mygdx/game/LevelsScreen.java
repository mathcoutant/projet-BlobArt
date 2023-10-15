package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;

/**A screen representing the level selection menu*/
public class LevelsScreen extends ScreenAdapter {
    private BlobArt game;
    private InputMultiplexer inputMultiplexer;
    private Stage stage;
    private ScrollPane.ScrollPaneStyle scrollPaneStyle;
    private ScrollPane scrollpane;
    private Table princTable, descTable, scrollpaneContainer, overviewContainer, buttonsTable;
    private List<Level> list;
    private BitmapFont font;
    private TextureAtlas atlas;
    private Skin skin;
    private Level selectedLevel;
    private Label.LabelStyle labelStyleTitle, labelStyleStandard;
    private Label levelTitle, labelComplete, labelTime, labelMove;
    private ImageButton.ImageButtonStyle playButtonStyle, backButtonStyle, editButtonStyle, newLevelButtonStyle;
    private ImageButton playButton, backButton, editButton, newLevelButton;
    private Sprite background;
    private ShapeRenderer shapeRenderer;

    public LevelsScreen(BlobArt game, InputMultiplexer inputMultiplexer){
        this.game=game;
        this.inputMultiplexer = inputMultiplexer;

        // creating the atlas and the skin for textures
        this.atlas = new TextureAtlas(Gdx.files.internal("ui/levelsscreen.atlas"));
        this.skin = new Skin(atlas);

        // the background image
        this.background = skin.getSprite("LevelsScreen_background");

        // creating the font
        font = new BitmapFont(Gdx.files.internal("font/rageitalic_white.fnt"), false);
        font.getData().markupEnabled = true;

        // creating the selector of level
        List.ListStyle listStyle = new List.ListStyle(font, Color.BLACK, Color.BLACK, skin.getDrawable("ScrollpaneList") );
        this.list = new List<>(listStyle);
        list.setItems(BlobArt.levels);
        list.setSelectedIndex(0);
        this.selectedLevel = new Level(new Cell[][]{{new PaintableCell(Color.BLACK)},{new PaintableCell(Color.BLACK)}} ,0,0, Color.BLACK, "Niveau zéro", 0, 0,0,0, 0); // only way I found to avoid a bug

        // creating the scrollpane which allows to scroll trough the list
        this.scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        this.scrollpane= new ScrollPane(list,scrollPaneStyle);
        scrollpane.setScrollingDisabled(true, false);

        this.playButtonStyle = new ImageButton.ImageButtonStyle();
        playButtonStyle.imageDown = skin.getDrawable("PlayLevelButton_down");
        playButtonStyle.imageOver = skin.getDrawable("PlayLevelButton_over");
        playButtonStyle.imageUp = skin.getDrawable("PlayLevelButton_up");
        this.playButton = new ImageButton(playButtonStyle);

        this.backButtonStyle = new ImageButton.ImageButtonStyle();
        backButtonStyle.imageDown = skin.getDrawable("backButton-Pressed");
        backButtonStyle.imageUp = skin.getDrawable("backButton-Up");
        backButtonStyle.imageOver = skin.getDrawable("backButton-Over");
        this.backButton = new ImageButton(backButtonStyle);

        this.editButtonStyle = new ImageButton.ImageButtonStyle();
        editButtonStyle.imageDown = skin.getDrawable("EditButton_Pressed");
        editButtonStyle.imageUp = skin.getDrawable("EditButton_Up");
        editButtonStyle.imageOver = skin.getDrawable("EditButton_Over");
        this.editButton = new ImageButton(editButtonStyle);

        this.newLevelButtonStyle = new ImageButton.ImageButtonStyle();
        newLevelButtonStyle.imageDown = skin.getDrawable("NewLevelButton_Pressed");
        newLevelButtonStyle.imageUp = skin.getDrawable("NewLevelButton_Up");
        newLevelButtonStyle.imageOver = skin.getDrawable("NewLevelButton_Over");
        this.newLevelButton = new ImageButton(newLevelButtonStyle);

        // creating the elements on the right side of the screen
        this.labelStyleTitle = new Label.LabelStyle();
        labelStyleTitle.background = skin.getDrawable("LabelLevelName");
        labelStyleTitle.font = font;
        this.levelTitle = new Label("[BLACK]"+selectedLevel.name, labelStyleTitle);
        levelTitle.setAlignment(1);
        levelTitle.setFontScale(1.5f);

        this.labelStyleStandard = new Label.LabelStyle();
        labelStyleStandard.font = font;
        this.labelComplete = new Label("[BLACK]Uncompleted", labelStyleStandard);
        labelComplete.setAlignment(1);
        this.labelTime = new Label("[BLACK]error", labelStyleStandard);
        labelTime.setAlignment(1);
        this.labelMove = new Label("[BLACK]error", labelStyleStandard);
        labelMove.setAlignment(1);

        // creating the tables
        this.princTable = new Table();
        princTable.setBounds(0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.buttonsTable = new Table();
        buttonsTable.add(backButton).left();
        buttonsTable.add(editButton).padLeft(princTable.getWidth()/10).left();
        buttonsTable.add(newLevelButton).padLeft(princTable.getWidth()/30).left(); // TODO: buttons to place here
        princTable.add(buttonsTable).height(princTable.getWidth()/13).expandX().row();
        this.scrollpaneContainer = new Table();
        scrollpaneContainer.add(scrollpane).width(princTable.getWidth()*2/5);
        princTable.add(scrollpaneContainer).pad(princTable.getWidth()/19,princTable.getWidth()/20,princTable.getWidth()/20,princTable.getWidth()/20).top().expandY().expandX();
        this.descTable = new Table();
        descTable.add(levelTitle).height((princTable.getHeight()/10)).row();
        this.overviewContainer = new Table();
        overviewContainer.add(labelComplete).width(princTable.getHeight()/4);
        overviewContainer.add().width(princTable.getHeight()/4).row();
        overviewContainer.add(labelTime).left().colspan(2).padTop(princTable.getHeight()/10).row();
        overviewContainer.add(labelMove).colspan(2).padTop(princTable.getHeight()/20).left();
        descTable.add(overviewContainer).height(princTable.getHeight()/2.5f).expandX().pad(princTable.getHeight()/20,0,princTable.getHeight()/20,0).row();
        descTable.add(playButton);
        princTable.add(descTable).width(princTable.getWidth()/2);

        // creating the stage
        this.stage = new Stage();
        stage.addActor(princTable);
        inputMultiplexer.addProcessor(stage);

        this.shapeRenderer = new ShapeRenderer();

        //stage.setDebugAll(true); // to see the outline of all the elements on screen
    }

    @Override
    public void show() {
        // what the buttons do
        this.playButton.addListener(new InputListener()
        {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            //the touchUp function is called only when a button is released and the touchDown function previously returned true
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new GameScreen(game, inputMultiplexer, selectedLevel));
            }
        });
        this.backButton.addListener(new InputListener()
        {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new TitleScreen(game, inputMultiplexer, false));
            }
        });
        this.editButton.addListener(new InputListener()
        {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new LevelEditorScreen(game, inputMultiplexer, selectedLevel));
            }
        });
        this.newLevelButton.addListener(new InputListener()
        {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Level newLevel = new Level();
                game.levelListAppend(newLevel);
                game.setScreen(new LevelEditorScreen(game, inputMultiplexer, newLevel));
            }
        });
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        game.batch.begin();
        background.draw(game.batch);
        game.batch.end();
        stage.act(delta);
        stage.draw();
        if (selectedLevel !=  list.getSelected()) {
            selectedLevel =  list.getSelected();
            updateScreen();
        }
        selectedLevel.drawModel(shapeRenderer);
    }

    @Override
    public void hide() {
        inputMultiplexer.removeProcessor(stage);
        skin.dispose();
        font.dispose();
        shapeRenderer.dispose();
        stage.dispose();
        atlas.dispose();
        dispose();
    }

    // called to update the information on the right of the screen
    public void updateScreen(){
        selectedLevel.setModelDimension(princTable.getWidth()-descTable.getWidth()*4/10, princTable.getHeight()/2.1f, descTable.getWidth()/5, princTable.getHeight()/4);
        levelTitle.setText("[BLACK]"+selectedLevel.name);
        if(selectedLevel.doneOrNot==0){
            labelComplete.setText("[#009900]Completed");
            if(selectedLevel.bestTime<=selectedLevel.bestTimeGoal) {
                labelTime.setText("[BLACK]Best Time : [#009900]" + String.format(java.util.Locale.US,"%.2f",selectedLevel.bestTime) + "s [BLACK]/ " + String.format(java.util.Locale.US,"%.2f",selectedLevel.bestTimeGoal) + "s");
            } else {
                labelTime.setText("[BLACK]Best Time : " + String.format(java.util.Locale.US,"%.2f",selectedLevel.bestTime) + "s [BLACK]/ " + String.format(java.util.Locale.US,"%.2f",selectedLevel.bestTimeGoal) + "s");
            }
            if(selectedLevel.bestMoveCount<=selectedLevel.bestMoveCountGoal) {
                labelMove.setText("[BLACK]Moves : [#009900]" + selectedLevel.bestMoveCount + "[BLACK] / " + selectedLevel.bestMoveCountGoal);
            } else {
                labelMove.setText("[BLACK]Moves : " + selectedLevel.bestMoveCount + "[BLACK] / " + selectedLevel.bestMoveCountGoal);
            }
        } else {
            labelComplete.setText("[BLACK]Uncompleted");
            labelTime.setText("[BLACK]Best Time : " + "-- s / " + String.format(java.util.Locale.US,"%.2f",selectedLevel.bestTimeGoal) + "s");
            labelMove.setText("[BLACK]Moves : " + "-- / " + selectedLevel.bestMoveCountGoal);
        }
    }
}
